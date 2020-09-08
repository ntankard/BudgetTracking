package com.ntankard.Tracking.DataBase.Core.Transfer;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.CurrencyBound;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Tracking_DataField;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;
import com.ntankard.dynamicGUI.CoreObject.Field.DataCore.Derived_DataCore;
import com.ntankard.dynamicGUI.CoreObject.FieldContainer;

import static com.ntankard.Tracking.DataBase.Core.Transfer.Transfer.*;
import static com.ntankard.dynamicGUI.CoreObject.Field.Properties.Display_Properties.DataType.CURRENCY;
import static com.ntankard.dynamicGUI.CoreObject.Field.Properties.Display_Properties.INFO_DISPLAY;
import static com.ntankard.dynamicGUI.CoreObject.Field.Properties.Display_Properties.TRACE_DISPLAY;

/**
 * One half of the transaction
 */
@ParameterMap(shouldSave = false)
public class HalfTransfer extends DataObject implements CurrencyBound {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String HalfTransfer_Transfer = "getTransfer";
    public static final String HalfTransfer_Period = "getPeriod";
    public static final String HalfTransfer_Pool = "getPool";
    public static final String HalfTransfer_Value = "getValue";
    public static final String HalfTransfer_Currency = "getCurrency";
    public static final String HalfTransfer_Source = "getSource";

    /**
     * Get all the fields for this object
     */
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = DataObject.getFieldContainer();

        // ID
        // Transfer ====================================================================================================
        fieldContainer.add(new Tracking_DataField<>(HalfTransfer_Transfer, Transfer.class));
        fieldContainer.get(HalfTransfer_Transfer).getDisplayProperties().setVerbosityLevel(INFO_DISPLAY);
        // Period ======================================================================================================
        fieldContainer.add(new Tracking_DataField<>(HalfTransfer_Period, Period.class));
        fieldContainer.<Period>get(HalfTransfer_Period).setDataCore(
                new Derived_DataCore<>(
                        (Derived_DataCore.Converter<Period, HalfTransfer>) container -> {
                            if (container.isSource()) {
                                return container.getTransfer().getSourcePeriodGet();
                            }
                            return container.getTransfer().getDestinationPeriodGet();

                        }
                        , new Derived_DataCore.ExternalSource<>(fieldContainer.get(HalfTransfer_Transfer), Transfer_SourcePeriodGet)
                        , new Derived_DataCore.ExternalSource<>(fieldContainer.get(HalfTransfer_Transfer), Transfer_DestinationPeriodGet)));
        fieldContainer.<Pool>get(HalfTransfer_Period).getDisplayProperties().setDisplaySet(false);
        // Pool ========================================================================================================
        fieldContainer.add(new Tracking_DataField<>(HalfTransfer_Pool, Pool.class));
        fieldContainer.<Pool>get(HalfTransfer_Pool).setDataCore(
                new Derived_DataCore<>(
                        (Derived_DataCore.Converter<Pool, HalfTransfer>) container -> {
                            if (container.isSource()) {
                                return container.getTransfer().getSource();
                            }
                            return container.getTransfer().getDestination();

                        }
                        , new Derived_DataCore.ExternalSource<>(fieldContainer.get(HalfTransfer_Transfer), Transfer_Source)
                        , new Derived_DataCore.ExternalSource<>(fieldContainer.get(HalfTransfer_Transfer), Transfer_Destination)));
        fieldContainer.<Pool>get(HalfTransfer_Pool).getDisplayProperties().setDisplaySet(false);
        // Value =======================================================================================================
        fieldContainer.add(new Tracking_DataField<>(HalfTransfer_Value, Double.class));
        fieldContainer.get(HalfTransfer_Value).getDisplayProperties().setDataType(CURRENCY);
        fieldContainer.<Double>get(HalfTransfer_Value).setDataCore(
                new Derived_DataCore<>(
                        (Derived_DataCore.Converter<Double, HalfTransfer>) container -> {
                            if (container.isSource()) {
                                return container.getTransfer().getValue(true);
                            } else {
                                return container.getTransfer().getValue(false);
                            }
                        }
                        , new Derived_DataCore.ExternalSource<>(fieldContainer.get(HalfTransfer_Transfer), Transfer_Value)));
        // Currency ====================================================================================================
        fieldContainer.add(new Tracking_DataField<>(HalfTransfer_Currency, Currency.class));
        fieldContainer.get(HalfTransfer_Currency).getDisplayProperties().setVerbosityLevel(INFO_DISPLAY);
        fieldContainer.<Currency>get(HalfTransfer_Currency).setDataCore(
                new Derived_DataCore<>(
                        (Derived_DataCore.Converter<Currency, HalfTransfer>) container -> {
                            if (container.isSource()) {
                                return container.getTransfer().getSourceCurrencyGet();
                            }
                            return container.getTransfer().getDestinationCurrencyGet();

                        }
                        , new Derived_DataCore.ExternalSource<>(fieldContainer.get(HalfTransfer_Transfer), Transfer_SourceCurrencyGet)
                        , new Derived_DataCore.ExternalSource<>(fieldContainer.get(HalfTransfer_Transfer), Transfer_DestinationCurrencyGet)));
        fieldContainer.<Pool>get(HalfTransfer_Currency).getDisplayProperties().setDisplaySet(false);
        // Source ======================================================================================================
        fieldContainer.add(new Tracking_DataField<>(HalfTransfer_Source, Boolean.class));
        fieldContainer.<Pool>get(HalfTransfer_Source).getDisplayProperties().setVerbosityLevel(TRACE_DISPLAY);
        //==============================================================================================================
        // Parents
        // Children

        return fieldContainer.finaliseContainer(HalfTransfer.class);
    }

    /**
     * Create a new HalfTransfer object
     */
    public static HalfTransfer make(Integer id, Transfer transfer, Boolean isSource) {
        return assembleDataObject(HalfTransfer.getFieldContainer(), new HalfTransfer()
                , DataObject_Id, id
                , HalfTransfer_Source, isSource
                , HalfTransfer_Transfer, transfer
        );
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void remove() {
        remove_impl();
    }

    /**
     * {@inheritDoc
     */
    @Override
    public String toString() {
        return getPeriod().toString() + " " + getPool().toString() + " " + getCurrency().getNumberFormat().format(getValue());
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public Transfer getTransfer() {
        return get(HalfTransfer_Transfer);
    }

    public Period getPeriod() {
        return get(HalfTransfer_Period);
    }

    public Pool getPool() {
        return get(HalfTransfer_Pool);
    }

    public Double getValue() {
        return get(HalfTransfer_Value);
    }

    @Override
    public Currency getCurrency() {
        return get(HalfTransfer_Currency);
    }

    public Boolean isSource() {
        return get(HalfTransfer_Source);
    }
}
