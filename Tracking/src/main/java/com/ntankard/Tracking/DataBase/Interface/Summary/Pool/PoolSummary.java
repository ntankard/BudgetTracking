package com.ntankard.Tracking.DataBase.Interface.Summary.Pool;

import com.ntankard.dynamicGUI.CoreObject.Field.DataCore.Method_DataCore;
import com.ntankard.dynamicGUI.CoreObject.Field.DataField;
import com.ntankard.dynamicGUI.CoreObject.FieldContainer;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.CurrencyBound;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Tracking_DataField;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Set.Extended.Sum.PeriodPool_SumSet;

import java.util.Map;

import static com.ntankard.dynamicGUI.CoreObject.Field.Properties.Display_Properties.DataContext.ZERO_TARGET;
import static com.ntankard.dynamicGUI.CoreObject.Field.Properties.Display_Properties.DataType.CURRENCY;
import static com.ntankard.dynamicGUI.CoreObject.Field.Properties.Display_Properties.DEBUG_DISPLAY;
import static com.ntankard.dynamicGUI.CoreObject.Field.Properties.Display_Properties.INFO_DISPLAY;

public abstract class PoolSummary<PoolType extends Pool> extends DataObject implements CurrencyBound {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String PoolSummary_Period = "getPeriod";
    public static final String PoolSummary_Pool = "getPool";
    public static final String PoolSummary_Currency = "getCurrency";
    public static final String PoolSummary_Start = "getStart";
    public static final String PoolSummary_End = "getEnd";
    public static final String PoolSummary_Net = "getNet";
    public static final String PoolSummary_TransferSum = "getTransferSum";
    public static final String PoolSummary_Missing = "getMissing";
    public static final String PoolSummary_Valid = "isValid";

    /**
     * Get all the fields for this object
     */
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = DataObject.getFieldContainer();

        // ID
        // Period ======================================================================================================
        fieldContainer.add(new Tracking_DataField<>(PoolSummary_Period, Period.class, false, false));
        fieldContainer.get(PoolSummary_Period).getDisplayProperties().setVerbosityLevel(DEBUG_DISPLAY);
        // Pool ========================================================================================================
        fieldContainer.add(new Tracking_DataField<>(PoolSummary_Pool, Pool.class, false, false));
        // Currency ====================================================================================================
        fieldContainer.add(new Tracking_DataField<>(PoolSummary_Currency, Currency.class, false, false));
        fieldContainer.get(PoolSummary_Currency).getDisplayProperties().setVerbosityLevel(INFO_DISPLAY);
        fieldContainer.get(PoolSummary_Currency).setDataCore(new Method_DataCore<>(container -> ((PoolSummary<?>) container).getCurrency_impl()));
        // Start =======================================================================================================
        fieldContainer.add(new Tracking_DataField<>(PoolSummary_Start, Double.class));
        fieldContainer.get(PoolSummary_Start).getDisplayProperties().setDataType(CURRENCY);
        // End =========================================================================================================
        fieldContainer.add(new Tracking_DataField<>(PoolSummary_End, Double.class));
        fieldContainer.get(PoolSummary_End).getDisplayProperties().setDataType(CURRENCY);
        // Net =========================================================================================================
        fieldContainer.add(new Tracking_DataField<>(PoolSummary_Net, Double.class));
        fieldContainer.get(PoolSummary_Net).getDisplayProperties().setDataType(CURRENCY);
        fieldContainer.get(PoolSummary_Net).setDataCore(new Method_DataCore<>(container -> ((PoolSummary<?>) container).getNet_impl()));
        // TransferSum =================================================================================================
        fieldContainer.add(new Tracking_DataField<>(PoolSummary_TransferSum, Double.class));
        fieldContainer.get(PoolSummary_TransferSum).getDisplayProperties().setDataType(CURRENCY);
        fieldContainer.get(PoolSummary_TransferSum).setDataCore(new Method_DataCore<>(container -> ((PoolSummary<?>) container).getTransferSum_impl()));
        // Missing =====================================================================================================
        fieldContainer.add(new Tracking_DataField<>(PoolSummary_Missing, Double.class));
        fieldContainer.get(PoolSummary_Missing).getDisplayProperties().setDataType(CURRENCY);
        fieldContainer.get(PoolSummary_Missing).getDisplayProperties().setDataContext(ZERO_TARGET);
        fieldContainer.get(PoolSummary_Missing).setDataCore(new Method_DataCore<>(container -> ((PoolSummary<?>) container).getMissing_impl()));
        // Valid =======================================================================================================
        fieldContainer.add(new Tracking_DataField<>(PoolSummary_Valid, Boolean.class));
        fieldContainer.get(PoolSummary_Valid).getDisplayProperties().setVerbosityLevel(INFO_DISPLAY);
        fieldContainer.get(PoolSummary_Valid).setDataCore(new Method_DataCore<>(container -> ((PoolSummary<?>) container).isValid_impl()));
        // Parents =====================================================================================================
        fieldContainer.get(DataObject_Parents).getDisplayProperties().setShouldDisplay(false);
        fieldContainer.get(DataObject_Parents).setDataCore(new Method_DataCore<>(container -> {
            throw new UnsupportedOperationException();
        }));
        //==============================================================================================================
        // Children

        return fieldContainer.endLayer(PoolSummary.class);
    }

    // Transfer sum ----------------------------------------------------------------------------------------------------

    private Currency getCurrency_impl() {
        return TrackingDatabase.get().getDefault(Currency.class);
    }

    // Start End -------------------------------------------------------------------------------------------------------

    private Double getNet_impl() {
        return getEnd() - getStart();
    }

    private Double getTransferSum_impl() {
        return new PeriodPool_SumSet(getPeriod(), getPool()).getTotal() / getCurrency().getToPrimary();
    }

    // Validity --------------------------------------------------------------------------------------------------------

    private Double getMissing_impl() {
        return Currency.round(getTransferSum() - getNet());
    }

    private Boolean isValid_impl() {
        return getMissing().equals(0.00);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void add() {
        for (Map.Entry<String, DataField<?>> field : fieldMap.entrySet()) {
            field.getValue().add();
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public Period getPeriod() {
        return get(PoolSummary_Period);
    }

    public PoolType getPool() {
        return get(PoolSummary_Pool);
    }

    @Override
    public Currency getCurrency() {
        return get(PoolSummary_Currency);
    }

    public Double getStart() {
        return get(PoolSummary_Start);
    }

    public Double getEnd() {
        return get(PoolSummary_End);
    }

    public Double getNet() {
        return get(PoolSummary_Net);
    }

    public Double getTransferSum() {
        return get(PoolSummary_TransferSum);
    }

    public Double getMissing() {
        return get(PoolSummary_Missing);
    }

    public Boolean isValid() {
        return get(PoolSummary_Valid);
    }
}
