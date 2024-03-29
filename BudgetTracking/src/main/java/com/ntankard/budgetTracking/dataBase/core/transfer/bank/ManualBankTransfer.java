package com.ntankard.budgetTracking.dataBase.core.transfer.bank;

import com.ntankard.budgetTracking.dataBase.core.Currency;
import com.ntankard.budgetTracking.dataBase.core.period.Period;
import com.ntankard.budgetTracking.dataBase.core.pool.Bank;
import com.ntankard.budgetTracking.dataBase.core.pool.Pool;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema.Calculator;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.Database;

import java.util.List;

import static com.ntankard.javaObjectDatabase.dataField.dataCore.DataCore_Factory.createDirectDerivedDataCore;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Source_Factory.makeSourceChain;

public class ManualBankTransfer extends BankTransfer {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

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
        // SourceCurrencyGet
        // DestinationCurrencyGet ======================================================================================
        dataObjectSchema.<Currency>get(Transfer_DestinationCurrencyGet).setDataCore_schema(createDirectDerivedDataCore(Transfer_Currency));
        //==============================================================================================================
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
        //==============================================================================================================
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(ManualBankTransfer.class);
    }

    /**
     * Constructor
     */
    public ManualBankTransfer(String description,
                              Period period, Bank source, Double value,
                              Period destinationPeriod, Pool destination) {
        super(period.getTrackingDatabase()
                , Transfer_Description, description
                , Transfer_Period, period
                , Transfer_Source, source
                , Transfer_Value, value
                , BankTransfer_DestinationPeriod, destinationPeriod
                , Transfer_Destination, destination
        );
    }

    /**
     * Constructor
     */
    public ManualBankTransfer(Database database, Object... args) {
        super(database, args);
    }

    /**
     * @inheritDoc
     */
    @Override
    public <T extends DataObject> List<T> sourceOptions(Class<T> type, String fieldName) {
        switch (fieldName) {
            case "Destination":
            case "Bank": {
                List<T> toReturn = super.sourceOptions(type, fieldName);
                toReturn.removeIf(t -> {
                    if (Bank.class.isAssignableFrom(t.getClass())) {
                        Bank bank = (Bank) t;
                        return !((Bank) getSource()).getCurrency().equals(bank.getCurrency());
                    }
                    return false;
                });
                return toReturn;
            }
        }
        return super.sourceOptions(type, fieldName);
    }
}
