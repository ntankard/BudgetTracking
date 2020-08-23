package com.ntankard.Tracking.DataBase.Core.Pool.FundEvent;

import com.ntankard.Tracking.DataBase.Core.BaseObject.Tracking_DataField;
import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.SolidCategory;
import com.ntankard.Tracking.DataBase.Core.Transfer.Fund.ManualFundTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfer.Fund.RePayFundTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfer.HalfTransfer;
import com.ntankard.Tracking.DataBase.Interface.Set.Extended.Sum.Transfer_SumSet;
import com.ntankard.Tracking.DataBase.Interface.Set.Filter.NotTransferType_HalfTransfer_Filter;
import com.ntankard.Tracking.DataBase.Interface.Set.OneParent_Children_Set;
import com.ntankard.Tracking.Dispaly.Util.Comparators.Ordered_Comparator;
import com.ntankard.dynamicGUI.CoreObject.Factory.Dummy_Factory;
import com.ntankard.dynamicGUI.CoreObject.Field.DataCore.ValueRead_DataCore;
import com.ntankard.dynamicGUI.CoreObject.Field.DataField;
import com.ntankard.dynamicGUI.CoreObject.Field.Filter.IntegerRange_FieldFilter;
import com.ntankard.dynamicGUI.CoreObject.FieldContainer;

import java.util.ArrayList;
import java.util.List;

public class FixedPeriodFundEvent extends FundEvent {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String FixedPeriodFundEvent_Start = "getStart";
    public static final String FixedPeriodFundEvent_Duration = "getDuration";

    /**
     * Get all the fields for this object
     */
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = FundEvent.getFieldContainer();

        // Class behavior
        fieldContainer.addObjectFactory(new Dummy_Factory(RePayFundTransfer.class));

        // ID
        // Name
        // Category ====================================================================================================
        fieldContainer.<SolidCategory>get(FundEvent_Category).setDataCore(new ValueRead_DataCore<>(true));
        fieldContainer.get(FundEvent_Category).addChangeListener((field, oldValue, newValue) -> {
            if (field.getState().equals(DataField.NewFieldState.N_ACTIVE)) {
                ((FixedPeriodFundEvent) field.getContainer()).recreateRePay();
            }
        });
        // Start =======================================================================================================
        fieldContainer.add(new Tracking_DataField<>(FixedPeriodFundEvent_Start, ExistingPeriod.class));
        fieldContainer.<ExistingPeriod>get(FixedPeriodFundEvent_Start).setDataCore(new ValueRead_DataCore<>(true));
        fieldContainer.get(FixedPeriodFundEvent_Start).addChangeListener((field, oldValue, newValue) -> {
            if (field.getState().equals(DataField.NewFieldState.N_ACTIVE)) {
                ((FixedPeriodFundEvent) field.getContainer()).recreateRePay();
            }
        });
        // Duration ====================================================================================================
        fieldContainer.add(new Tracking_DataField<>(FixedPeriodFundEvent_Duration, Integer.class));
        fieldContainer.get(FixedPeriodFundEvent_Duration).addFilter(new IntegerRange_FieldFilter(1, null));
        fieldContainer.<Integer>get(FixedPeriodFundEvent_Duration).addFilter(new IntegerRange_FieldFilter(1, null));
        fieldContainer.get(FixedPeriodFundEvent_Duration).setDataCore(new ValueRead_DataCore<>(true));
        fieldContainer.get(FixedPeriodFundEvent_Duration).addChangeListener((field, oldValue, newValue) -> {
            if (field.getState().equals(DataField.NewFieldState.N_ACTIVE)) {
                ((FixedPeriodFundEvent) field.getContainer()).recreateRePay();
            }
        });
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

    /**
     * {@inheritDoc
     */
    @Override
    public void add() {
        super.add();
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Speciality ###################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc
     */
    @Override
    public Boolean isActiveThisPeriod(Period period) {
        List<Period> periods = new ArrayList<>();

        if (!isChargeThisPeriod(period)) {
            for (ManualFundTransfer useCategoryFundTransfer : new OneParent_Children_Set<>(ManualFundTransfer.class, this).get()) {
                if (!periods.contains(useCategoryFundTransfer.getPeriod())) {
                    periods.add(useCategoryFundTransfer.getPeriod());
                }
            }
            if (periods.size() == 0) {
                return false;
            }
            periods.sort(new Ordered_Comparator());

            if (period.getOrder() < getStart().getOrder()) {
                return period.getOrder() >= periods.get(0).getOrder();
            } else {
                return period.getOrder() <= periods.get(periods.size() - 1).getOrder();
            }
        }
        return true;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public Boolean isChargeThisPeriod(Period period) {
        return period.isWithin(getStart(), getDuration());
    }

    /**
     * {@inheritDoc
     */
    @Override
    public Double getCharge(Period period) {
        if (!isChargeThisPeriod(period)) {
            return -0.0;
        }
        return new Transfer_SumSet<>(new OneParent_Children_Set<>(HalfTransfer.class, this, new NotTransferType_HalfTransfer_Filter(RePayFundTransfer.class)), this).getTotal() / getDuration();
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
