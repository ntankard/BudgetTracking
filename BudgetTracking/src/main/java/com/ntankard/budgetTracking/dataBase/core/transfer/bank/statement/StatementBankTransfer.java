package com.ntankard.budgetTracking.dataBase.core.transfer.bank.statement;

import com.ntankard.budgetTracking.dataBase.core.Currency;
import com.ntankard.budgetTracking.dataBase.core.fileManagement.statement.StatementTransactionTranslationAutoGroup;
import com.ntankard.budgetTracking.dataBase.core.period.Period;
import com.ntankard.budgetTracking.dataBase.core.pool.Bank;
import com.ntankard.budgetTracking.dataBase.core.pool.Pool;
import com.ntankard.budgetTracking.dataBase.core.transfer.bank.BankTransfer;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.Database;

import java.util.List;

import static com.ntankard.javaObjectDatabase.dataField.dataCore.DataCore_Factory.createDirectDerivedDataCore;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Source_Factory.makeSourceChain;

public class StatementBankTransfer extends StatementBasedBankTransfer {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = StatementBasedBankTransfer.getDataObjectSchema();

        // ID
        // Description (Below)
        // Period
        // Source
        // Value
        // Currency
        // DestinationPeriod
        // Category
        // Bank
        // FundEvent
        // Destination
        // AutoSource
        // StatementTransactionSet
        // Multiply
        // DestinationValue
        // SourceCurrencyGet
        // DestinationCurrencyGet ======================================================================================
        dataObjectSchema.<Currency>get(Transfer_DestinationCurrencyGet).setDataCore_schema(createDirectDerivedDataCore(Transfer_Currency));
        // SourcePeriodGet
        // DestinationPeriodGet ========================================================================================
        dataObjectSchema.<Period>get(Transfer_DestinationPeriodGet).setDataCore_schema(
                new Derived_DataCore_Schema<>((Derived_DataCore_Schema.Calculator<Period, BankTransfer>) container -> {
                    if (container.getDestinationPeriod() != null) {
                        return container.getDestinationPeriod();
                    } else {
                        return container.getPeriod();
                    }
                }
                        , makeSourceChain(Transfer_Period)
                        , makeSourceChain(BankTransfer_DestinationPeriod)));
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(StatementBankTransfer.class);
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

    /**
     * Constructor
     */
    public StatementBankTransfer(Database database, Object... args) {
        super(database, args);
    }

    /**
     * Constructor
     */
    public StatementBankTransfer(Period period, Bank source, Period destinationPeriod, Pool destination, String description, StatementTransactionTranslationAutoGroup autoSource, Double multiply) {
        super(period.getTrackingDatabase()
                , Transfer_Period, period
                , Transfer_Source, source
                , BankTransfer_DestinationPeriod, destinationPeriod
                , Transfer_Destination, destination
                , Transfer_Description, description
                , StatementBankTransfer_AutoSource, autoSource
                , StatementBankTransfer_Multiply, multiply
        );
    }
}
