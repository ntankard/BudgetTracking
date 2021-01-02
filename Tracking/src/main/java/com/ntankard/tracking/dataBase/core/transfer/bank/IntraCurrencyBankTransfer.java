package com.ntankard.tracking.dataBase.core.transfer.bank;

import com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema.Calculator;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.end.End_Source_Schema;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.tracking.dataBase.core.Currency;
import com.ntankard.tracking.dataBase.core.period.Period;
import com.ntankard.tracking.dataBase.core.pool.Bank;
import com.ntankard.tracking.dataBase.core.pool.Pool;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema;

import static com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties.DataType.CURRENCY;

public class IntraCurrencyBankTransfer extends BankTransfer {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String BankTransfer_DestinationCurrency = "getDestinationCurrency";
    public static final String BankTransfer_DestinationValue = "getDestinationValue";

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = BankTransfer.getDataObjectSchema();

        // ID
        // Description
        // Period
        // Source
        // Value
        // Currency
        // DestinationPeriod
        // Category
        // Bank
        // FundEvent
        // Destination
        // DestinationValue ============================================================================================
        dataObjectSchema.add(Transfer_Destination, new DataField_Schema<>(BankTransfer_DestinationValue, Double.class));
        dataObjectSchema.get(BankTransfer_DestinationValue).getProperty(Display_Properties.class).setDataType(CURRENCY);
        dataObjectSchema.get(BankTransfer_DestinationValue).setManualCanEdit(true);
        // DestinationCurrency =========================================================================================
        dataObjectSchema.add(new DataField_Schema<>(BankTransfer_DestinationCurrency, Currency.class, false));
        dataObjectSchema.<Currency>get(BankTransfer_DestinationCurrency).setDataCore_schema(
                new Derived_DataCore_Schema<>(container -> {
                    BankTransfer bankTransfer = ((BankTransfer) container);
                    return ((Bank) bankTransfer.getDestination()).getCurrency();
                }
                        , new End_Source_Schema<>((Transfer_Destination))));
        // SourceCurrencyGet
        // DestinationCurrencyGet ======================================================================================
        dataObjectSchema.<Currency>get(Transfer_DestinationCurrencyGet).setDataCore_schema(
                new Derived_DataCore_Schema<>((Calculator<Currency, IntraCurrencyBankTransfer>) container ->
                        container.getDestinationCurrency()
                        , new End_Source_Schema<>((BankTransfer_DestinationCurrency))));
        // SourcePeriodGet
        // DestinationPeriodGet ========================================================================================
        dataObjectSchema.<Period>get(Transfer_DestinationPeriodGet).setDataCore_schema(
                new Derived_DataCore_Schema<>((Calculator<Period, BankTransfer>) container -> {
                    if (container.getDestinationPeriod() != null) {
                        return container.getDestinationPeriod();
                    } else {
                        return container.getPeriod();
                    }
                }
                        , new End_Source_Schema<>((Transfer_Period))
                        , new End_Source_Schema<>((BankTransfer_DestinationPeriod))));
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(IntraCurrencyBankTransfer.class);
    }

    /**
     * Constructor
     */
    public IntraCurrencyBankTransfer(Database database) {
        super(database);
    }

    /**
     * Constructor
     */
    public IntraCurrencyBankTransfer(String description,
                                     Period period, Bank source, Double value,
                                     Period destinationPeriod, Pool destination, Double destinationValue) {
        this(period.getTrackingDatabase());
        setAllValues(DataObject_Id, getTrackingDatabase().getNextId()
                , Transfer_Description, description
                , Transfer_Period, period
                , Transfer_Source, source
                , Transfer_Value, value
                , BankTransfer_DestinationPeriod, destinationPeriod
                , Transfer_Destination, destination
                , BankTransfer_DestinationValue, destinationValue
        );
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public Currency getDestinationCurrency() {
        return get(BankTransfer_DestinationCurrency);
    }

    public Double getDestinationValue() {
        return get(BankTransfer_DestinationValue);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public void setDestinationValue(Double destinationValue) {
        set(BankTransfer_DestinationValue, destinationValue);
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################# HalfTransfer Interface #############################################
    //------------------------------------------------------------------------------------------------------------------

    @Override
    protected Double getValue(boolean isSource) {
        if (isSource) {
            return -getValue();
        } else {
            if (getDestinationValue() != null) {
                return getDestinationValue();
            } else {
                return getValue();
            }
        }
    }
}
