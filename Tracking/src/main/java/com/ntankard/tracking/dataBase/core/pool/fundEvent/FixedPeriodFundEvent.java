package com.ntankard.tracking.dataBase.core.pool.fundEvent;

import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.javaObjectDatabase.database.Database_Schema;
import com.ntankard.tracking.dataBase.core.Currency;
import com.ntankard.tracking.dataBase.core.period.ExistingPeriod;
import com.ntankard.tracking.dataBase.core.pool.category.SolidCategory;
import com.ntankard.tracking.dataBase.core.transfer.fund.rePay.FixedPeriodRePayFundTransfer;
import com.ntankard.tracking.dataBase.core.transfer.fund.rePay.RePayFundTransfer;
import com.ntankard.tracking.dataBase.core.transfer.HalfTransfer;
import com.ntankard.tracking.dataBase.interfaces.set.filter.NotTransferType_HalfTransfer_Filter;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.filter.IntegerRange_FieldFilter;
import com.ntankard.javaObjectDatabase.dataField.ListDataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.Children_ListDataCore;
import com.ntankard.javaObjectDatabase.dataField.dataCore.Static_DataCore;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.List_Source;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Local_Source;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;

import java.util.List;

import static com.ntankard.tracking.dataBase.core.transfer.HalfTransfer.HalfTransfer_Currency;
import static com.ntankard.tracking.dataBase.core.transfer.HalfTransfer.HalfTransfer_Value;

public class FixedPeriodFundEvent extends FundEvent {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String FixedPeriodFundEvent_Start = "getStart";
    public static final String FixedPeriodFundEvent_Duration = "getDuration";
    public static final String FixedPeriodFundEvent_Self = "getSelf";
    public static final String FixedPeriodFundEvent_NonRepaySet = "getNonRepaySet";
    public static final String FixedPeriodFundEvent_NonRepaySum = "getNonRepaySum";
    public static final String FixedPeriodFundEvent_RepayAmount = "getRepayAmount";

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getFieldContainer() {
        DataObject_Schema dataObjectSchema = FundEvent.getFieldContainer();

        // Class behavior
        dataObjectSchema.addObjectFactory(FixedPeriodRePayFundTransfer.Factory);

        // ID
        // Name
        // Category ====================================================================================================
        dataObjectSchema.get(FundEvent_Category).setManualCanEdit(true);
        // Start =======================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(FixedPeriodFundEvent_Start, ExistingPeriod.class));
        dataObjectSchema.get(FixedPeriodFundEvent_Start).setManualCanEdit(true);
        // Duration ====================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(FixedPeriodFundEvent_Duration, Integer.class));
        dataObjectSchema.<Integer>get(FixedPeriodFundEvent_Duration).addFilter(new IntegerRange_FieldFilter<>(1, null));
        dataObjectSchema.get(FixedPeriodFundEvent_Duration).setManualCanEdit(true);
        // Self ========================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(FixedPeriodFundEvent_Self, FixedPeriodFundEvent.class));
        dataObjectSchema.<FixedPeriodFundEvent>get(FixedPeriodFundEvent_Self).setDataCore_factory(
                new Static_DataCore.Static_DataCore_Factory<>(dataField ->
                        (FixedPeriodFundEvent) dataField.getContainer()));
        // NonRepaySet =================================================================================================
        dataObjectSchema.add(new ListDataField_Schema<>(FixedPeriodFundEvent_NonRepaySet, HalfTransfer.HalfTransferList.class));
        dataObjectSchema.<List<HalfTransfer>>get(FixedPeriodFundEvent_NonRepaySet).setDataCore_factory(
                new Children_ListDataCore.Children_ListDataCore_Factory<HalfTransfer>(
                        HalfTransfer.class,
                        new NotTransferType_HalfTransfer_Filter(RePayFundTransfer.class),
                        new Children_ListDataCore.ParentAccess.ParentAccess_Factory<HalfTransfer>(FixedPeriodFundEvent_Self)));
        // NonRepaySum =================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(FixedPeriodFundEvent_NonRepaySum, Double.class));
        dataObjectSchema.<Double>get(FixedPeriodFundEvent_NonRepaySum).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<Double, FixedPeriodFundEvent>(
                        container -> {
                            double sum = 0.0;
                            for (HalfTransfer halfTransfer : container.getNonRepaySet()) {
                                sum += halfTransfer.getValue() * halfTransfer.getCurrency().getToPrimary();
                            }
                            return Currency.round(sum);
                        }
                        , new List_Source.ListSource_Factory<>(
                        FixedPeriodFundEvent_NonRepaySet,
                        HalfTransfer_Value,
                        HalfTransfer_Currency))); // TODO possible problem here, we have a 3 layer nested dependency. getToPrimary
        // RepayAmount =================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(FixedPeriodFundEvent_RepayAmount, Double.class));
        dataObjectSchema.<Double>get(FixedPeriodFundEvent_RepayAmount).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>(
                        (Derived_DataCore.Calculator<Double, FixedPeriodFundEvent>)
                                container -> container.getNonRepaySum() / container.getDuration()
                        , new Local_Source.LocalSource_Factory<>(FixedPeriodFundEvent_NonRepaySum)
                        , new Local_Source.LocalSource_Factory<>(FixedPeriodFundEvent_Duration)));
        //==============================================================================================================
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(FixedPeriodFundEvent.class);
    }

    /**
     * Create a new FixedPeriodFundEvent object
     */
    public static FixedPeriodFundEvent make(Integer id, String name, SolidCategory solidCategory, ExistingPeriod start, Integer duration) {
        Database database = solidCategory.getTrackingDatabase();
        Database_Schema database_schema = database.getSchema();
        return assembleDataObject(database, database_schema.getClassSchema(FixedPeriodFundEvent.class), new FixedPeriodFundEvent()
                , DataObject_Id, id
                , NamedDataObject_Name, name
                , FundEvent_Category, solidCategory
                , FixedPeriodFundEvent_Start, start
                , FixedPeriodFundEvent_Duration, duration
        );
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public ExistingPeriod getStart() {
        return get(FixedPeriodFundEvent_Start);
    }

    public Integer getDuration() {
        return get(FixedPeriodFundEvent_Duration);
    }

    public Double getRepayAmount() {
        return get(FixedPeriodFundEvent_RepayAmount);
    }

    public Double getNonRepaySum() {
        return get(FixedPeriodFundEvent_NonRepaySum);
    }

    public List<HalfTransfer> getNonRepaySet() {
        return get(FixedPeriodFundEvent_NonRepaySet);
    }

    //------------------------------------------------------------------------------------------------------------------
    //##################################################### Setter #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public void setCategory(SolidCategory solidCategory) {
        set(FundEvent_Category, solidCategory);
    }

    public void setStart(ExistingPeriod start) {
        set(FixedPeriodFundEvent_Start, start);
    }

    public void setDuration(Integer duration) {
        set(FixedPeriodFundEvent_Duration, duration);
    }
}

//    /**
//     * Is this fund event active in this period?
//     *
//     * @param period The period to check
//     * @return True if its active at this time
//     */
//    public Boolean isActiveThisPeriod(Period period) {
//        List<Period> periods = new ArrayList<>();
//
//        if (!isChargeThisPeriod(period)) {
//            for (ManualFundTransfer useCategoryFundTransfer : new OneParent_Children_Set<>(ManualFundTransfer.class, this).get()) {
//                if (!periods.contains(useCategoryFundTransfer.getPeriod())) {
//                    periods.add(useCategoryFundTransfer.getPeriod());
//                }
//            }
//            if (periods.size() == 0) {
//                return false;
//            }
//            periods.sort(new Ordered_Comparator());
//
//            if (period.getOrder() < getStart().getOrder()) {
//                return period.getOrder() >= periods.get(0).getOrder();
//            } else {
//                return period.getOrder() <= periods.get(periods.size() - 1).getOrder();
//            }
//        }
//        return true;
//    }
