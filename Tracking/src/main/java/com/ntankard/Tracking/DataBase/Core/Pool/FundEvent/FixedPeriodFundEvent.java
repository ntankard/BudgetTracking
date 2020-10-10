package com.ntankard.Tracking.DataBase.Core.Pool.FundEvent;

import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.SolidCategory;
import com.ntankard.Tracking.DataBase.Core.Transfer.Fund.RePay.FixedPeriodRePayFundTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfer.Fund.RePay.RePayFundTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfer.HalfTransfer;
import com.ntankard.Tracking.DataBase.Interface.Set.Filter.NotTransferType_HalfTransfer_Filter;
import com.ntankard.javaObjectDatabase.CoreObject.Field.DataField;
import com.ntankard.javaObjectDatabase.CoreObject.Field.Filter.IntegerRange_FieldFilter;
import com.ntankard.javaObjectDatabase.CoreObject.Field.ListDataField;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.Children_ListDataCore;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.Static_DataCore;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.Derived_DataCore;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.source.ListSource;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.source.LocalSource;
import com.ntankard.javaObjectDatabase.CoreObject.FieldContainer;

import java.util.List;

import static com.ntankard.Tracking.DataBase.Core.Transfer.HalfTransfer.HalfTransfer_Currency;
import static com.ntankard.Tracking.DataBase.Core.Transfer.HalfTransfer.HalfTransfer_Value;

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
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = FundEvent.getFieldContainer();

        // Class behavior
        fieldContainer.addObjectFactory(FixedPeriodRePayFundTransfer.Factory);

        // ID
        // Name
        // Category ====================================================================================================
        fieldContainer.get(FundEvent_Category).setCanEdit(true);
        // Start =======================================================================================================
        fieldContainer.add(new DataField<>(FixedPeriodFundEvent_Start, ExistingPeriod.class));
        fieldContainer.get(FixedPeriodFundEvent_Start).setCanEdit(true);
        // Duration ====================================================================================================
        fieldContainer.add(new DataField<>(FixedPeriodFundEvent_Duration, Integer.class));
        fieldContainer.<Integer>get(FixedPeriodFundEvent_Duration).addFilter(new IntegerRange_FieldFilter<>(1, null));
        fieldContainer.get(FixedPeriodFundEvent_Duration).setCanEdit(true);
        // Self ========================================================================================================
        fieldContainer.add(new DataField<>(FixedPeriodFundEvent_Self, FixedPeriodFundEvent.class));
        fieldContainer.<FixedPeriodFundEvent>get(FixedPeriodFundEvent_Self).setDataCore(
                new Static_DataCore<>(dataField ->
                        (FixedPeriodFundEvent) dataField.getContainer()));
        // NonRepaySet =================================================================================================
        fieldContainer.add(new ListDataField<>(FixedPeriodFundEvent_NonRepaySet, HalfTransfer.HalfTransferList.class));
        fieldContainer.<List<HalfTransfer>>get(FixedPeriodFundEvent_NonRepaySet).setDataCore(
                new Children_ListDataCore<>(
                        HalfTransfer.class,
                        new NotTransferType_HalfTransfer_Filter(RePayFundTransfer.class),
                        new Children_ListDataCore.ParentAccess<FixedPeriodFundEvent, HalfTransfer>(fieldContainer.get(FixedPeriodFundEvent_Self))));
        // NonRepaySum =================================================================================================
        fieldContainer.add(new DataField<>(FixedPeriodFundEvent_NonRepaySum, Double.class));
        fieldContainer.<Double>get(FixedPeriodFundEvent_NonRepaySum).setDataCore(
                new Derived_DataCore<Double, FixedPeriodFundEvent>(
                        container -> {
                            double sum = 0.0;
                            for (HalfTransfer halfTransfer : container.getNonRepaySet()) {
                                sum += halfTransfer.getValue() * halfTransfer.getCurrency().getToPrimary();
                            }
                            return Currency.round(sum);
                        }
                        , new ListSource<>(
                        (ListDataField<HalfTransfer>) fieldContainer.<List<HalfTransfer>>get(FixedPeriodFundEvent_NonRepaySet),
                        HalfTransfer_Value,
                        HalfTransfer_Currency))); // TODO possible problem here, we have a 3 layer nested dependency. getToPrimary
        // RepayAmount =================================================================================================
        fieldContainer.add(new DataField<>(FixedPeriodFundEvent_RepayAmount, Double.class));
        fieldContainer.<Double>get(FixedPeriodFundEvent_RepayAmount).setDataCore(
                new Derived_DataCore<>(
                        (Derived_DataCore.Calculator<Double, FixedPeriodFundEvent>)
                                container -> container.getNonRepaySum() / container.getDuration()
                        , new LocalSource<>(fieldContainer.get(FixedPeriodFundEvent_NonRepaySum))
                        , new LocalSource<>(fieldContainer.get(FixedPeriodFundEvent_Duration))));
        //==============================================================================================================
        // Parents
        // Children

        return fieldContainer.finaliseContainer(FixedPeriodFundEvent.class);
    }

    /**
     * Create a new FixedPeriodFundEvent object
     */
    public static FixedPeriodFundEvent make(Integer id, String name, SolidCategory solidCategory, ExistingPeriod start, Integer duration) {
        return assembleDataObject(FixedPeriodFundEvent.getFieldContainer(), new FixedPeriodFundEvent()
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
