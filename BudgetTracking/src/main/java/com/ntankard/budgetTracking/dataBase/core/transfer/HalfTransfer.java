package com.ntankard.budgetTracking.dataBase.core.transfer;

import com.ntankard.budgetTracking.dataBase.core.Currency;
import com.ntankard.budgetTracking.dataBase.core.baseObject.interfaces.CurrencyBound;
import com.ntankard.budgetTracking.dataBase.core.period.Period;
import com.ntankard.budgetTracking.dataBase.core.pool.Pool;
import com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties;
import com.ntankard.dynamicGUI.javaObjectDatabase.Displayable_DataObject;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema.Calculator;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.javaObjectDatabase.database.ParameterMap;

import java.util.List;

import static com.ntankard.budgetTracking.dataBase.core.transfer.Transfer.*;
import static com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties.DataType.CURRENCY;
import static com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties.INFO_DISPLAY;
import static com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties.TRACE_DISPLAY;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Source_Factory.makeSharedStepSourceChain;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Source_Factory.makeSourceChain;

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
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = Displayable_DataObject.getDataObjectSchema();

        // ID
        // Transfer ====================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(HalfTransfer_Transfer, Transfer.class));
        dataObjectSchema.get(HalfTransfer_Transfer).getProperty(Display_Properties.class).setVerbosityLevel(INFO_DISPLAY);
        // Period ======================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(HalfTransfer_Period, Period.class));
        dataObjectSchema.<Period>get(HalfTransfer_Period).setDataCore_schema(
                new Derived_DataCore_Schema<>(
                        (Calculator<Period, HalfTransfer>) container -> {
                            if (container.isSource()) {
                                return container.getTransfer().getSourcePeriodGet();
                            }
                            return container.getTransfer().getDestinationPeriodGet();

                        }
                        , makeSharedStepSourceChain(HalfTransfer_Transfer, Transfer_SourcePeriodGet, Transfer_DestinationPeriodGet)));
        dataObjectSchema.<Pool>get(HalfTransfer_Period).getProperty(Display_Properties.class).setDisplaySet(false);
        // Pool ========================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(HalfTransfer_Pool, Pool.class));
        dataObjectSchema.<Pool>get(HalfTransfer_Pool).setDataCore_schema(
                new Derived_DataCore_Schema<>(
                        (Calculator<Pool, HalfTransfer>) container -> {
                            if (container.isSource()) {
                                return container.getTransfer().getSource();
                            }
                            return container.getTransfer().getDestination();

                        }
                        , makeSharedStepSourceChain(HalfTransfer_Transfer, Transfer_Source, Transfer_Destination)));
        dataObjectSchema.<Pool>get(HalfTransfer_Pool).getProperty(Display_Properties.class).setDisplaySet(false);
        // Value =======================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(HalfTransfer_Value, Double.class));
        dataObjectSchema.get(HalfTransfer_Value).getProperty(Display_Properties.class).setDataType(CURRENCY);
        dataObjectSchema.<Double>get(HalfTransfer_Value).setDataCore_schema(
                new Derived_DataCore_Schema<>(
                        (Calculator<Double, HalfTransfer>) container -> {
                            if (container.isSource()) {
                                return container.getTransfer().getValue(true);
                            } else {
                                return container.getTransfer().getValue(false);
                            }
                        }
                        , makeSourceChain(HalfTransfer_Transfer, Transfer_Value)));
        // Currency ====================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(HalfTransfer_Currency, Currency.class));
        dataObjectSchema.get(HalfTransfer_Currency).getProperty(Display_Properties.class).setVerbosityLevel(INFO_DISPLAY);
        dataObjectSchema.<Currency>get(HalfTransfer_Currency).setDataCore_schema(
                new Derived_DataCore_Schema<>(
                        (Calculator<Currency, HalfTransfer>) container -> {
                            if (container.isSource()) {
                                return container.getTransfer().getSourceCurrencyGet();
                            }
                            return container.getTransfer().getDestinationCurrencyGet();

                        }
                        , makeSharedStepSourceChain(HalfTransfer_Transfer, Transfer_SourceCurrencyGet, Transfer_DestinationCurrencyGet)));
        dataObjectSchema.<Pool>get(HalfTransfer_Currency).getProperty(Display_Properties.class).setDisplaySet(false);
        // Source ======================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(HalfTransfer_Source, Boolean.class));
        dataObjectSchema.<Pool>get(HalfTransfer_Source).getProperty(Display_Properties.class).setVerbosityLevel(TRACE_DISPLAY);
        //==============================================================================================================
        // Parents
        // Children

        return dataObjectSchema.endLayer(HalfTransfer.class);
    }

    /**
     * Constructor
     */
    public HalfTransfer(Database database, Object... args) {
        super(database, args);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void remove() {
        remove_impl();
    }

    /**
     * @inheritDoc
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
