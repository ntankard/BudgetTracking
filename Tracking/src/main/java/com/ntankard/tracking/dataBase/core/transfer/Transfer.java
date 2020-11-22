package com.ntankard.tracking.dataBase.core.transfer;

import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.tracking.dataBase.core.baseObject.interfaces.CurrencyBound;
import com.ntankard.tracking.dataBase.core.Currency;
import com.ntankard.tracking.dataBase.core.period.Period;
import com.ntankard.tracking.dataBase.core.pool.Bank;
import com.ntankard.tracking.dataBase.core.pool.Pool;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Local_Source;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.util.set.Single_OneParent_Children_Set;

import java.util.List;

import static com.ntankard.javaObjectDatabase.dataField.properties.Display_Properties.DataType.CURRENCY;
import static com.ntankard.javaObjectDatabase.dataField.properties.Display_Properties.INFO_DISPLAY;

public abstract class Transfer extends DataObject implements CurrencyBound {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String Transfer_Description = "getDescription";
    public static final String Transfer_Period = "getPeriod";
    public static final String Transfer_Source = "getSource";
    public static final String Transfer_Value = "getValue";
    public static final String Transfer_Currency = "getCurrency";
    public static final String Transfer_Destination = "getDestination";

    public static final String Transfer_SourceCurrencyGet = "getSourceCurrencyGet";
    public static final String Transfer_DestinationCurrencyGet = "getDestinationCurrencyGet";

    public static final String Transfer_SourcePeriodGet = "getSourcePeriodGet";
    public static final String Transfer_DestinationPeriodGet = "getDestinationPeriodGet";

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = DataObject.getDataObjectSchema();

        // Class behavior
        dataObjectSchema.addObjectFactory(Source_HalfTransfer.Factory);
        dataObjectSchema.addObjectFactory(Destination_HalfTransfer.Factory);

        // ID
        // Description =================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Transfer_Description, String.class));
        dataObjectSchema.get(Transfer_Description).setManualCanEdit(true);
        // Period ======================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Transfer_Period, Period.class));
        //==============================================================================================================
        // Source
        // Value =======================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Transfer_Value, Double.class));
        dataObjectSchema.get(Transfer_Value).getDisplayProperties().setDataType(CURRENCY);
        // Currency ====================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Transfer_Currency, Currency.class));
        dataObjectSchema.get(Transfer_Currency).getDisplayProperties().setVerbosityLevel(INFO_DISPLAY);
        // Destination =================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Transfer_Destination, Pool.class));
        // SourceCurrencyGet ===========================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Transfer_SourceCurrencyGet, Currency.class));
        dataObjectSchema.get(Transfer_SourceCurrencyGet).setTellParent(false);
        dataObjectSchema.<Currency>get(Transfer_SourceCurrencyGet).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>
                        (container -> ((Transfer) container).getCurrency()
                                , new Local_Source.LocalSource_Factory<>(Transfer_Currency)));
        // DestinationCurrencyGet  =====================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Transfer_DestinationCurrencyGet, Currency.class));
        dataObjectSchema.get(Transfer_DestinationCurrencyGet).setTellParent(false);
        // SourcePeriodGet =================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Transfer_SourcePeriodGet, Period.class));
        dataObjectSchema.get(Transfer_SourcePeriodGet).setTellParent(false);
        dataObjectSchema.<Period>get(Transfer_SourcePeriodGet).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>
                        (container -> ((Transfer) container).getPeriod()
                                , new Local_Source.LocalSource_Factory<>(Transfer_Period)));
        // DestinationPeriodGet =================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Transfer_DestinationPeriodGet, Period.class));
        dataObjectSchema.get(Transfer_DestinationPeriodGet).setTellParent(false);
        //==============================================================================================================
        // Parents
        // Children

        return dataObjectSchema.endLayer(Transfer.class);
    }

    /**
     * Constructor
     */
    public Transfer(Database database) {
        super(database);
    }

    /**
     * @inheritDoc
     */
    @Override
    @SuppressWarnings("SuspiciousMethodCalls")
    public <T extends DataObject> List<T> sourceOptions(Class<T> type, String fieldName) {
        if (fieldName.equals("Source")) {
            if (getDestination() instanceof Bank) {
                List<T> toReturn = super.sourceOptions(type, fieldName);
                toReturn.remove(getDestination());
                return toReturn;
            }
        }
        return super.sourceOptions(type, fieldName);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public String getDescription() {
        return get(Transfer_Description);
    }

    public Period getPeriod() {
        return get(Transfer_Period);
    }

    public Pool getSource() {
        return get(Transfer_Source);
    }

    public Double getValue() {
        return get(Transfer_Value);
    }

    public Currency getCurrency() {
        return get(Transfer_Currency);
    }

    public Pool getDestination() {
        return get(Transfer_Destination);
    }

    public HalfTransfer toChaneGetSourceTransfer() {
        return new Single_OneParent_Children_Set<>(Source_HalfTransfer.class, this).getItem();
    }

    public HalfTransfer toChangeGetDestinationTransfer() {
        return new Single_OneParent_Children_Set<>(Destination_HalfTransfer.class, this).getItem();
    }

    public Currency getDestinationCurrencyGet() {
        return get(Transfer_DestinationCurrencyGet);
    }

    public Currency getSourceCurrencyGet() {
        return get(Transfer_SourceCurrencyGet);
    }

    public Period getDestinationPeriodGet() {
        return get(Transfer_DestinationPeriodGet);
    }

    public Period getSourcePeriodGet() {
        return get(Transfer_SourcePeriodGet);
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################# HalfTransfer Interface #############################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get the value for this half of the transaction
     *
     * @param isSource Get the source? Otherwise get the destination
     * @return The value this side should use
     */
    protected Double getValue(boolean isSource) {
        if (isSource) {
            return -getValue();
        } else {
            return getValue();
        }
    }
}
