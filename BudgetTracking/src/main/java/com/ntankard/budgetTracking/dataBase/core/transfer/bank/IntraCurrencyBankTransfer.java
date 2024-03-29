package com.ntankard.budgetTracking.dataBase.core.transfer.bank;

import com.ntankard.budgetTracking.dataBase.core.Currency;
import com.ntankard.budgetTracking.dataBase.core.period.Period;
import com.ntankard.budgetTracking.dataBase.core.pool.Bank;
import com.ntankard.budgetTracking.dataBase.core.pool.Pool;
import com.ntankard.budgetTracking.dataBase.core.transfer.Transfer;
import com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema.Calculator;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.Database;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties.DataType.CURRENCY;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.DataCore_Factory.createDirectDerivedDataCore;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Source_Factory.makeSourceChain;

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
        // Source ======================================================================================================
        dataObjectSchema.get(Transfer_Source).setManualCanEdit(true);
        //==============================================================================================================
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
                        , makeSourceChain(Transfer_Destination)));
        // SourceCurrencyGet
        // DestinationCurrencyGet ======================================================================================
        dataObjectSchema.<Currency>get(Transfer_DestinationCurrencyGet).setDataCore_schema(createDirectDerivedDataCore(BankTransfer_DestinationCurrency));
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
                        , makeSourceChain(Transfer_Period)
                        , makeSourceChain(BankTransfer_DestinationPeriod)));
        // SourceValueGet ==============================================================================================
        dataObjectSchema.<Double>get(Transfer_SourceValueGet).setDataCore_schema(
                new Derived_DataCore_Schema<>
                        (container -> -((Transfer) container).getValue()
                                , makeSourceChain(Transfer_Value)));
        // DestinationValueGet ========================================================================================
        dataObjectSchema.<Double>get(Transfer_DestinationValueGet).setDataCore_schema(
                new Derived_DataCore_Schema<>((Calculator<Double, IntraCurrencyBankTransfer>) container -> {
                    if (container.getDestinationValue() != null) {
                        return container.getDestinationValue();
                    } else {
                        return container.getValue();
                    }
                }
                        , makeSourceChain(Transfer_Value)
                        , makeSourceChain(BankTransfer_DestinationValue)));
        //==============================================================================================================
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(IntraCurrencyBankTransfer.class);
    }

    /**
     * @inheritDoc
     */
    @Override
    @SuppressWarnings({"SuspiciousMethodCalls", "unchecked"})
    public <T extends DataObject> List<T> sourceOptions(Class<T> type, String fieldName) {
        switch (fieldName) {
            case "FundEvent":
            case "Category": {
                return new ArrayList<>();
            }
            case "Destination":
            case "Bank": {
                List<T> toReturn = super.sourceOptions(type, "Bank");
                toReturn.remove(getSource());
                toReturn.removeIf(t -> !Bank.class.isAssignableFrom(t.getClass()));
                toReturn.removeIf(t -> ((Bank) t).getCurrency().equals(((Bank) getSource()).getCurrency()));
                return toReturn;
            }
            case "Source": {
                List<T> toReturn = new ArrayList<>();
                for (Bank bank : super.sourceOptions(Bank.class, fieldName)) {
                    toReturn.add((T) bank);
                }
                return toReturn;
            }
            case "Period": {
                List<T> toReturn = super.sourceOptions(type, fieldName);
                toReturn.remove(getDestinationPeriod());
                return toReturn;
            }
        }
        return super.sourceOptions(type, fieldName);
    }

    /**
     * Constructor
     */
    public IntraCurrencyBankTransfer(Database database, Object... args) {
        super(database, args);
    }

    /**
     * Constructor
     */
    public IntraCurrencyBankTransfer(String description,
                                     Period period, Bank source, Double value,
                                     Period destinationPeriod, Pool destination, Double destinationValue) {
        super(period.getTrackingDatabase()
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
}
