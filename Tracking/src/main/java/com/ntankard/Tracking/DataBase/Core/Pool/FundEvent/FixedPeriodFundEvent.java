package com.ntankard.Tracking.DataBase.Core.Pool.FundEvent;

import com.ntankard.Tracking.DataBase.Core.BaseObject.Factory.DoubleParentFactory;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Tracking_DataField;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.SolidCategory;
import com.ntankard.Tracking.DataBase.Core.Transfer.Fund.RePay.FixedPeriodRePayFundTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfer.Fund.RePay.RePayFundTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfer.HalfTransfer;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Set.Extended.Sum.Transfer_SumSet;
import com.ntankard.Tracking.DataBase.Interface.Set.Filter.NotTransferType_HalfTransfer_Filter;
import com.ntankard.Tracking.DataBase.Interface.Set.OneParent_Children_Set;
import com.ntankard.dynamicGUI.CoreObject.Field.DataCore.Method_DataCore;
import com.ntankard.dynamicGUI.CoreObject.Field.DataCore.ValueRead_DataCore;
import com.ntankard.dynamicGUI.CoreObject.Field.Filter.IntegerRange_FieldFilter;
import com.ntankard.dynamicGUI.CoreObject.FieldContainer;

public class FixedPeriodFundEvent extends FundEvent {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String FixedPeriodFundEvent_Start = "getStart";
    public static final String FixedPeriodFundEvent_Duration = "getDuration";
    public static final String FixedPeriodFundEvent_RepayAmount = "getRepayAmount";

    /**
     * Get all the fields for this object
     */
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = FundEvent.getFieldContainer();

        // Class behavior
        fieldContainer.addObjectFactory(new DoubleParentFactory<FixedPeriodRePayFundTransfer, FixedPeriodFundEvent, ExistingPeriod>(
                FixedPeriodRePayFundTransfer.class,
                ExistingPeriod.class,
                (generator, secondaryGenerator) -> FixedPeriodRePayFundTransfer.make(
                        TrackingDatabase.get().getNextId(),
                        secondaryGenerator,
                        generator,
                        TrackingDatabase.get().getDefault(Currency.class))
        ));

        // ID
        // Name
        // Category ====================================================================================================
        fieldContainer.<SolidCategory>get(FundEvent_Category).setDataCore(new ValueRead_DataCore<>(true));
        // Start =======================================================================================================
        fieldContainer.add(new Tracking_DataField<>(FixedPeriodFundEvent_Start, ExistingPeriod.class));
        fieldContainer.<ExistingPeriod>get(FixedPeriodFundEvent_Start).setDataCore(new ValueRead_DataCore<>(true));
        // Duration ====================================================================================================
        fieldContainer.add(new Tracking_DataField<>(FixedPeriodFundEvent_Duration, Integer.class));
        fieldContainer.<Integer>get(FixedPeriodFundEvent_Duration).addFilter(new IntegerRange_FieldFilter<>(1, null));
        fieldContainer.get(FixedPeriodFundEvent_Duration).setDataCore(new ValueRead_DataCore<>(true));
        // RepayAmount =================================================================================================
        fieldContainer.add(new Tracking_DataField<>(FixedPeriodFundEvent_RepayAmount, Double.class));
        fieldContainer.<Double>get(FixedPeriodFundEvent_RepayAmount).setDataCore(new Method_DataCore<Double, FixedPeriodFundEvent>(container -> {
            return new Transfer_SumSet<>(new OneParent_Children_Set<>(HalfTransfer.class, container, new NotTransferType_HalfTransfer_Filter(RePayFundTransfer.class)), container).getTotal() / container.getDuration();
        }));
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
