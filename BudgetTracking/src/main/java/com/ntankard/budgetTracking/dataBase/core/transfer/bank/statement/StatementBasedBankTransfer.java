package com.ntankard.budgetTracking.dataBase.core.transfer.bank.statement;

import com.ntankard.budgetTracking.dataBase.core.Currency;
import com.ntankard.budgetTracking.dataBase.core.fileManagement.statement.StatementTransaction;
import com.ntankard.budgetTracking.dataBase.core.fileManagement.statement.StatementTransactionTranslationAutoGroup;
import com.ntankard.budgetTracking.dataBase.core.period.Period;
import com.ntankard.budgetTracking.dataBase.core.pool.Bank;
import com.ntankard.budgetTracking.dataBase.core.pool.Pool;
import com.ntankard.budgetTracking.dataBase.core.transfer.bank.BankTransfer;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.ListDataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Source_Factory;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.Database;

import java.util.List;

import static com.ntankard.budgetTracking.dataBase.core.fileManagement.statement.StatementTransaction.StatementTransaction_Currency;
import static com.ntankard.budgetTracking.dataBase.core.fileManagement.statement.StatementTransaction.StatementTransaction_Value;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.DataCore_Factory.createDirectDerivedDataCore;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.DataCore_Factory.createSelfParentList;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Source_Factory.makeSharedStepSourceChain;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Source_Factory.makeSourceChain;

public abstract class StatementBasedBankTransfer extends BankTransfer {
    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    private static final String StatementBankTransfer_Prefix = "StatementBankTransfer_";
    public static final String StatementBankTransfer_AutoSource = StatementBankTransfer_Prefix + "AutoSource";
    public static final String StatementBankTransfer_StatementTransactionSet = StatementBankTransfer_Prefix + "StatementTransactionSet";
    public static final String StatementBankTransfer_Multiply = StatementBankTransfer_Prefix + "Multiply";

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
                new Derived_DataCore_Schema<Double, StatementBasedBankTransfer>(
                        container -> {
                            double sum = 0.0;
                            for (StatementTransaction statementTransaction : container.<List<StatementTransaction>>get(StatementBankTransfer_StatementTransactionSet)) {
                                sum += statementTransaction.getValue();
                            }
                            return Currency.round(sum * (Double) container.get(StatementBankTransfer_Multiply));
                        }
                        , Source_Factory.append(makeSharedStepSourceChain(StatementBankTransfer_StatementTransactionSet, StatementTransaction_Value, StatementTransaction_Currency)
                        , makeSourceChain(StatementBankTransfer_Multiply))
                ));
        //==============================================================================================================
        // Currency
        // DestinationPeriod
        // Category
        // Bank
        // FundEvent
        // Destination
        // AutoSource ==================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(StatementBankTransfer_AutoSource, StatementTransactionTranslationAutoGroup.class, true));
        dataObjectSchema.get(StatementBankTransfer_AutoSource).setManualCanEdit(true);
        // StatementTransactionSet =====================================================================================
        dataObjectSchema.add(new ListDataField_Schema<>(StatementBankTransfer_StatementTransactionSet, StatementTransaction.StatementTransactionList.class));
        dataObjectSchema.<List<StatementTransaction>>get(StatementBankTransfer_StatementTransactionSet).setDataCore_schema(
                createSelfParentList(StatementTransaction.class, null));
        // Multiply ==================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(StatementBankTransfer_Multiply, Double.class, false));
        dataObjectSchema.get(StatementBankTransfer_Multiply).setManualCanEdit(true);
        //==============================================================================================================
        // DestinationValue
        // SourceCurrencyGet
        // DestinationCurrencyGet
        // SourcePeriodGet
        // DestinationPeriodGet
        // Parents
        // Children

        return dataObjectSchema.endLayer(StatementBasedBankTransfer.class);
    }

    /**
     * @inheritDoc
     */
    @Override
    public String toString() {
        return getPeriod().toString() + " - " + getDescription();
    }

    /**
     * Constructor
     */
    public StatementBasedBankTransfer(Database database, Object... args) {
        super(database, args);
    }

    /**
     * Constructor
     */
    public StatementBasedBankTransfer(Period period, Bank source, Period destinationPeriod, Pool destination, String description, StatementTransactionTranslationAutoGroup autoSource, Double multiply) {
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

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public StatementTransactionTranslationAutoGroup getAutoSource() {
        return get(StatementBankTransfer_AutoSource);
    }
}
