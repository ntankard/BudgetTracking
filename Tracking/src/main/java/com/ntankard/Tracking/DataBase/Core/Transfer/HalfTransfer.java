package com.ntankard.Tracking.DataBase.Core.Transfer;

import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.CurrencyBound;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.javaObjectDatabase.CoreObject.DataObject;
import com.ntankard.javaObjectDatabase.CoreObject.Field.DataField_Schema;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.Derived_DataCore;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.source.ExternalSource;
import com.ntankard.javaObjectDatabase.CoreObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.Database.ParameterMap;

import java.util.List;

import static com.ntankard.Tracking.DataBase.Core.Transfer.Transfer.*;
import static com.ntankard.javaObjectDatabase.CoreObject.Field.Properties.Display_Properties.DataType.CURRENCY;
import static com.ntankard.javaObjectDatabase.CoreObject.Field.Properties.Display_Properties.INFO_DISPLAY;
import static com.ntankard.javaObjectDatabase.CoreObject.Field.Properties.Display_Properties.TRACE_DISPLAY;

/**
 * One half of the transaction
 */
@ParameterMap(shouldSave = false)
public abstract class HalfTransfer extends DataObject implements CurrencyBound {

    public interface HalfTransferList extends List<HalfTransfer> {
    }

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
    public static DataObject_Schema getFieldContainer() {
        DataObject_Schema dataObjectSchema = DataObject.getFieldContainer();

        // ID
        // Transfer ====================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(HalfTransfer_Transfer, Transfer.class));
        dataObjectSchema.get(HalfTransfer_Transfer).getDisplayProperties().setVerbosityLevel(INFO_DISPLAY);
        // Period ======================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(HalfTransfer_Period, Period.class));
        dataObjectSchema.<Period>get(HalfTransfer_Period).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>(
                        (Derived_DataCore.Calculator<Period, HalfTransfer>) container -> {
                            if (container.isSource()) {
                                return container.getTransfer().getSourcePeriodGet();
                            }
                            return container.getTransfer().getDestinationPeriodGet();

                        }
                        , new ExternalSource.ExternalSource_Factory<>(HalfTransfer_Transfer, Transfer_SourcePeriodGet)
                        , new ExternalSource.ExternalSource_Factory<>(HalfTransfer_Transfer, Transfer_DestinationPeriodGet)));
        dataObjectSchema.<Pool>get(HalfTransfer_Period).getDisplayProperties().setDisplaySet(false);
        // Pool ========================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(HalfTransfer_Pool, Pool.class));
        dataObjectSchema.<Pool>get(HalfTransfer_Pool).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>(
                        (Derived_DataCore.Calculator<Pool, HalfTransfer>) container -> {
                            if (container.isSource()) {
                                return container.getTransfer().getSource();
                            }
                            return container.getTransfer().getDestination();

                        }
                        , new ExternalSource.ExternalSource_Factory<>(HalfTransfer_Transfer, Transfer_Source)
                        , new ExternalSource.ExternalSource_Factory<>(HalfTransfer_Transfer, Transfer_Destination)));
        dataObjectSchema.<Pool>get(HalfTransfer_Pool).getDisplayProperties().setDisplaySet(false);
        // Value =======================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(HalfTransfer_Value, Double.class));
        dataObjectSchema.get(HalfTransfer_Value).getDisplayProperties().setDataType(CURRENCY);
        dataObjectSchema.<Double>get(HalfTransfer_Value).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>(
                        (Derived_DataCore.Calculator<Double, HalfTransfer>) container -> {
                            if (container.isSource()) {
                                return container.getTransfer().getValue(true);
                            } else {
                                return container.getTransfer().getValue(false);
                            }
                        }
                        , new ExternalSource.ExternalSource_Factory<>(HalfTransfer_Transfer, Transfer_Value)));
        // Currency ====================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(HalfTransfer_Currency, Currency.class));
        dataObjectSchema.get(HalfTransfer_Currency).getDisplayProperties().setVerbosityLevel(INFO_DISPLAY);
        dataObjectSchema.<Currency>get(HalfTransfer_Currency).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>(
                        (Derived_DataCore.Calculator<Currency, HalfTransfer>) container -> {
                            if (container.isSource()) {
                                return container.getTransfer().getSourceCurrencyGet();
                            }
                            return container.getTransfer().getDestinationCurrencyGet();

                        }
                        , new ExternalSource.ExternalSource_Factory<>(HalfTransfer_Transfer, Transfer_SourceCurrencyGet)
                        , new ExternalSource.ExternalSource_Factory<>(HalfTransfer_Transfer, Transfer_DestinationCurrencyGet)));
        dataObjectSchema.<Pool>get(HalfTransfer_Currency).getDisplayProperties().setDisplaySet(false);
        // Source ======================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(HalfTransfer_Source, Boolean.class));
        dataObjectSchema.<Pool>get(HalfTransfer_Source).getDisplayProperties().setVerbosityLevel(TRACE_DISPLAY);
        //==============================================================================================================
        // Parents
        // Children

        return dataObjectSchema.endLayer(HalfTransfer.class);
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
