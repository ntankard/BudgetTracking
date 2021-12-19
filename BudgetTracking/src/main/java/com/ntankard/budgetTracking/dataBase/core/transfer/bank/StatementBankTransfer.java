package com.ntankard.budgetTracking.dataBase.core.transfer.bank;

import com.ntankard.budgetTracking.dataBase.core.Currency;
import com.ntankard.budgetTracking.dataBase.core.fileManagement.statement.StatementTransaction;
import com.ntankard.budgetTracking.dataBase.core.fileManagement.statement.StatementTransaction.StatementTransactionList;
import com.ntankard.budgetTracking.dataBase.core.fileManagement.statement.StatementTransactionAutoGroup;
import com.ntankard.budgetTracking.dataBase.core.period.Period;
import com.ntankard.budgetTracking.dataBase.core.pool.Bank;
import com.ntankard.budgetTracking.dataBase.core.pool.Pool;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.ListDataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Source_Factory;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.end.End_Source_Schema;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.Database;

import java.util.List;

import static com.ntankard.budgetTracking.dataBase.core.fileManagement.statement.StatementTransaction.StatementTransaction_Currency;
import static com.ntankard.budgetTracking.dataBase.core.fileManagement.statement.StatementTransaction.StatementTransaction_Value;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.DataCore_Factory.createSelfParentList;

public class StatementBankTransfer extends BankTransfer {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    private static final String StatementBankTransfer_Prefix = "StatementBankTransfer_";
    public static final String StatementBankTransfer_AutoSource = StatementBankTransfer_Prefix + "AutoSource";
    public static final String StatementBankTransfer_StatementTransactionSet = StatementBankTransfer_Prefix + "StatementTransactionSet";

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = BankTransfer.getDataObjectSchema();

        // ID
        // Description (Below)
        // Period ======================================================================================================
        dataObjectSchema.get(Transfer_Period).setManualCanEdit(false);
        //==============================================================================================================
        // Source
        // Value =======================================================================================================
        dataObjectSchema.get(Transfer_Value).setManualCanEdit(false);
        dataObjectSchema.<Double>get(Transfer_Value).setDataCore_schema(
                new Derived_DataCore_Schema<>(
                        container -> {
                            double sum = 0.0;
                            for (StatementTransaction statementTransaction : container.<List<StatementTransaction>>get(StatementBankTransfer_StatementTransactionSet)) {
                                sum += statementTransaction.getValue();
                            }
                            return Currency.round(sum);
                        },
                        Source_Factory.makeSharedStepSourceChain(StatementBankTransfer_StatementTransactionSet, StatementTransaction_Value, StatementTransaction_Currency)
                ));
        //==============================================================================================================
        // Currency
        // DestinationPeriod
        // Category
        // Bank
        // FundEvent
        // Destination
        // AutoSource ==================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(StatementBankTransfer_AutoSource, StatementTransactionAutoGroup.class, true));
        dataObjectSchema.get(StatementBankTransfer_AutoSource).setManualCanEdit(true);
        // StatementTransactionSet =====================================================================================
        dataObjectSchema.add(new ListDataField_Schema<>(StatementBankTransfer_StatementTransactionSet, StatementTransactionList.class));
        dataObjectSchema.<List<StatementTransaction>>get(StatementBankTransfer_StatementTransactionSet).setDataCore_schema(
                createSelfParentList(StatementTransaction.class, null));
        //==============================================================================================================
        // DestinationValue
        // SourceCurrencyGet
        // DestinationCurrencyGet ======================================================================================
        dataObjectSchema.<Currency>get(Transfer_DestinationCurrencyGet).setDataCore_schema(
                new Derived_DataCore_Schema<>(
                        (Derived_DataCore_Schema.Calculator<Currency, StatementBankTransfer>) container ->
                                container.getCurrency()
                        , new End_Source_Schema<>((Transfer_Currency))));
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
                        , new End_Source_Schema<>((Transfer_Period))
                        , new End_Source_Schema<>((BankTransfer_DestinationPeriod))));
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(StatementBankTransfer.class);
    }

    /**
     * @inheritDoc
     */
    @Override
    public String toString() {
        return getPeriod().toString() + " - " + getDescription();
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
    public StatementBankTransfer(Database database) {
        super(database);
    }

    /**
     * Constructor
     */
    public StatementBankTransfer(Period period, Bank source, Period destinationPeriod, Pool destination, String description, StatementTransactionAutoGroup autoSource) {
        this(period.getTrackingDatabase());
        setAllValues(DataObject_Id, getTrackingDatabase().getNextId()
                , Transfer_Period, period
                , Transfer_Source, source
                , BankTransfer_DestinationPeriod, destinationPeriod
                , Transfer_Destination, destination
                , Transfer_Description, description
                , StatementBankTransfer_AutoSource, autoSource
        );
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public StatementTransactionAutoGroup getAutoSource() {
        return get(StatementBankTransfer_AutoSource);
    }
}
