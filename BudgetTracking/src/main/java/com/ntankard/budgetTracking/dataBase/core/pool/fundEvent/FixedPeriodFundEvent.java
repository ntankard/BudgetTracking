package com.ntankard.budgetTracking.dataBase.core.pool.fundEvent;

import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema.Calculator;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.end.End_Source_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Source_Factory;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.budgetTracking.dataBase.core.Currency;
import com.ntankard.budgetTracking.dataBase.core.period.ExistingPeriod;
import com.ntankard.budgetTracking.dataBase.core.pool.category.SolidCategory;
import com.ntankard.budgetTracking.dataBase.core.transfer.fund.rePay.FixedPeriodRePayFundTransfer;
import com.ntankard.budgetTracking.dataBase.core.transfer.fund.rePay.RePayFundTransfer;
import com.ntankard.budgetTracking.dataBase.core.transfer.HalfTransfer;
import com.ntankard.budgetTracking.dataBase.interfaces.set.filter.NotTransferType_HalfTransfer_Filter;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.validator.NumberRange_FieldValidator;
import com.ntankard.javaObjectDatabase.dataField.ListDataField_Schema;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;

import java.util.List;

import static com.ntankard.javaObjectDatabase.dataField.dataCore.DataCore_Factory.createSelfParentList;
import static com.ntankard.budgetTracking.dataBase.core.transfer.HalfTransfer.HalfTransfer_Currency;
import static com.ntankard.budgetTracking.dataBase.core.transfer.HalfTransfer.HalfTransfer_Value;

public class FixedPeriodFundEvent extends FundEvent {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String FixedPeriodFundEvent_Start = "getStart";
    public static final String FixedPeriodFundEvent_Duration = "getDuration";
    public static final String FixedPeriodFundEvent_NonRepaySet = "getNonRepaySet";
    public static final String FixedPeriodFundEvent_NonRepaySum = "getNonRepaySum";
    public static final String FixedPeriodFundEvent_RepayAmount = "getRepayAmount";

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = FundEvent.getDataObjectSchema();

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
        dataObjectSchema.<Integer>get(FixedPeriodFundEvent_Duration).addValidator(new NumberRange_FieldValidator<>(1, null));
        dataObjectSchema.get(FixedPeriodFundEvent_Duration).setManualCanEdit(true);
        // NonRepaySet =================================================================================================
        dataObjectSchema.add(new ListDataField_Schema<>(FixedPeriodFundEvent_NonRepaySet, HalfTransfer.HalfTransferList.class));
        dataObjectSchema.<List<HalfTransfer>>get(FixedPeriodFundEvent_NonRepaySet).setDataCore_schema(
                createSelfParentList(
                        HalfTransfer.class,
                        new NotTransferType_HalfTransfer_Filter(RePayFundTransfer.class)));
        // NonRepaySum =================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(FixedPeriodFundEvent_NonRepaySum, Double.class));

        dataObjectSchema.<Double>get(FixedPeriodFundEvent_NonRepaySum).setDataCore_schema(
                new Derived_DataCore_Schema<Double, FixedPeriodFundEvent>(
                        container -> {
                            double sum = 0.0;
                            for (HalfTransfer halfTransfer : container.getNonRepaySet()) {
                                sum += halfTransfer.getValue() * halfTransfer.getCurrency().getToPrimary();
                            }
                            return Currency.round(sum);
                        }
                        , Source_Factory.makeSharedStepSourceChain(
                        FixedPeriodFundEvent_NonRepaySet,
                        HalfTransfer_Value,
                        HalfTransfer_Currency))); // TODO possible problem here, we have a 3 layer nested dependency. getToPrimary
        // RepayAmount =================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(FixedPeriodFundEvent_RepayAmount, Double.class));
        dataObjectSchema.<Double>get(FixedPeriodFundEvent_RepayAmount).setDataCore_schema(
                new Derived_DataCore_Schema<>(
                        (Calculator<Double, FixedPeriodFundEvent>)
                                container -> container.getNonRepaySum() / container.getDuration()
                        , new End_Source_Schema<>(FixedPeriodFundEvent_NonRepaySum)
                        , new End_Source_Schema<>(FixedPeriodFundEvent_Duration)));
        //==============================================================================================================
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(FixedPeriodFundEvent.class);
    }

    /**
     * Constructor
     */
    public FixedPeriodFundEvent(Database database) {
        super(database);
    }

    /**
     * Constructor
     */
    public FixedPeriodFundEvent(String name, SolidCategory solidCategory, ExistingPeriod start, Integer duration, Boolean isDone) {
        this(solidCategory.getTrackingDatabase());
        setAllValues(DataObject_Id, getTrackingDatabase().getNextId()
                , NamedDataObject_Name, name
                , FundEvent_Category, solidCategory
                , FixedPeriodFundEvent_Start, start
                , FixedPeriodFundEvent_Duration, duration
                , FundEvent_IsDone, isDone
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
