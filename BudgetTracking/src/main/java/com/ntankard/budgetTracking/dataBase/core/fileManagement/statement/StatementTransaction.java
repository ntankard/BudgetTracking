package com.ntankard.budgetTracking.dataBase.core.fileManagement.statement;

import com.ntankard.budgetTracking.dataBase.core.Currency;
import com.ntankard.budgetTracking.dataBase.core.baseObject.interfaces.CurrencyBound;
import com.ntankard.budgetTracking.dataBase.core.fileManagement.statement.TransactionLine.TransactionLineList;
import com.ntankard.budgetTracking.dataBase.core.period.Period;
import com.ntankard.budgetTracking.dataBase.core.pool.Bank;
import com.ntankard.budgetTracking.dataBase.core.transfer.bank.StatementBankTransfer;
import com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties;
import com.ntankard.dynamicGUI.javaObjectDatabase.Displayable_DataObject;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.ListDataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.Database;

import java.awt.*;
import java.util.List;

import static com.ntankard.budgetTracking.dataBase.core.fileManagement.statement.StatementDocument.StatementDocument_StatementFolder;
import static com.ntankard.budgetTracking.dataBase.core.fileManagement.statement.StatementFolder.StatementFolder_Bank;
import static com.ntankard.budgetTracking.dataBase.core.fileManagement.statement.TransactionLine.*;
import static com.ntankard.budgetTracking.dataBase.core.pool.Bank.Bank_Currency;
import static com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties.DataType.CURRENCY;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.DataCore_Factory.createDefaultDirectDerivedDataCore;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.DataCore_Factory.createSelfParentList;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Source_Factory.makeSourceChain;

public class StatementTransaction extends DataObject implements CurrencyBound {

    public interface StatementTransactionList extends List<StatementTransaction> {
    }
    private static final String StatementTransaction_Prefix = "StatementTransaction_";

    public static final String StatementTransaction_TransactionLines = StatementTransaction_Prefix + "TransactionLines";
    public static final String StatementTransaction_CoreLine = StatementTransaction_Prefix + "CoreLine";
    public static final String StatementTransaction_Value = StatementTransaction_Prefix + "Value";
    public static final String StatementTransaction_Description = StatementTransaction_Prefix + "Description";
    public static final String StatementTransaction_Currency = StatementTransaction_Prefix + "Currency";
    public static final String StatementTransaction_StatementBankTransfer = StatementTransaction_Prefix + "StatementBankTransfer";
    public static final String StatementTransaction_Bank = StatementTransaction_Prefix + "Bank";
    public static final String StatementTransaction_Period = StatementTransaction_Prefix + "Period";
    public static final String StatementTransaction_StatementFolder = StatementTransaction_Prefix + "StatementFolder";

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = Displayable_DataObject.getDataObjectSchema();

        // ID
        dataObjectSchema.add(new ListDataField_Schema<>(StatementTransaction_TransactionLines, TransactionLineList.class));
        dataObjectSchema.add(new DataField_Schema<>(StatementTransaction_CoreLine, TransactionLine.class, true));
        dataObjectSchema.add(new DataField_Schema<>(StatementTransaction_Value, Double.class));
        dataObjectSchema.add(new DataField_Schema<>(StatementTransaction_Description, String.class));
        dataObjectSchema.add(new DataField_Schema<>(StatementTransaction_Currency, Currency.class));
        dataObjectSchema.add(new DataField_Schema<>(StatementTransaction_StatementBankTransfer, StatementBankTransfer.class, true));
        dataObjectSchema.add(new DataField_Schema<>(StatementTransaction_Bank, Bank.class));
        dataObjectSchema.add(new DataField_Schema<>(StatementTransaction_Period, Period.class));
        dataObjectSchema.add(new DataField_Schema<>(StatementTransaction_StatementFolder, StatementFolder.class));
        // Children

        // TransactionLines ============================================================================================
        dataObjectSchema.get(StatementTransaction_TransactionLines).getProperty(Display_Properties.class).setVerbosityLevel(Display_Properties.INFO_DISPLAY);
        dataObjectSchema.<List<TransactionLine>>get(StatementTransaction_TransactionLines).setDataCore_schema(
                createSelfParentList(TransactionLine.class, null));
        // CoreLine ====================================================================================================
        dataObjectSchema.get(StatementTransaction_CoreLine).getProperty(Display_Properties.class).setVerbosityLevel(Display_Properties.INFO_DISPLAY);
        dataObjectSchema.<TransactionLine>get(StatementTransaction_CoreLine).setDataCore_schema(
                new Derived_DataCore_Schema<TransactionLine, StatementTransaction>
                        (container -> container.getTransactionLines().size() == 0 ? null : container.getTransactionLines().get(0)
                                , makeSourceChain(StatementTransaction_TransactionLines)));
        // Value =======================================================================================================
        dataObjectSchema.get(StatementTransaction_Value).getProperty(Display_Properties.class).setDataType(CURRENCY);
        dataObjectSchema.<Double>get(StatementTransaction_Value).setDataCore_schema(
                createDefaultDirectDerivedDataCore(0.0,
                        StatementTransaction_CoreLine, TransactionLine_Value));
        // Description =================================================================================================
        dataObjectSchema.<String>get(StatementTransaction_Description).setDataCore_schema(
                createDefaultDirectDerivedDataCore("",
                        StatementTransaction_CoreLine, TransactionLine_Description));
        // Currency ====================================================================================================
        dataObjectSchema.get(StatementTransaction_Currency).getProperty(Display_Properties.class).setVerbosityLevel(Display_Properties.INFO_DISPLAY);
        dataObjectSchema.<Currency>get(StatementTransaction_Currency).setDataCore_schema(
                createDefaultDirectDerivedDataCore(container -> container.getTrackingDatabase().getDefault(Currency.class),
                        StatementTransaction_CoreLine, TransactionLine_StatementDocument, StatementDocument_StatementFolder, StatementFolder_Bank, Bank_Currency));
        // StatementBankTransfer =======================================================================================
        dataObjectSchema.get(StatementTransaction_StatementBankTransfer).setManualCanEdit(true);
        // Bank ========================================================================================================
        dataObjectSchema.<Bank>get(StatementTransaction_Bank).setDataCore_schema(
                createDefaultDirectDerivedDataCore(container -> container.getTrackingDatabase().getDefault(Bank.class),
                        StatementTransaction_CoreLine, TransactionLine_StatementDocument, StatementDocument_StatementFolder, StatementFolder_Bank));
        // Period ======================================================================================================
        dataObjectSchema.<Period>get(StatementTransaction_Period).setDataCore_schema(
                createDefaultDirectDerivedDataCore(container -> container.getTrackingDatabase().getDefault(Period.class),
                        StatementTransaction_CoreLine, TransactionLine_Period));
        // StatementFolder =============================================================================================
        dataObjectSchema.get(StatementTransaction_StatementFolder).getProperty(Display_Properties.class).setCustomColor((rowObject, value) -> ((StatementTransaction)rowObject).getPeriod() == ((StatementTransaction)rowObject).getStatementFolder().getPeriod() ? null : Color.ORANGE);
        //==============================================================================================================

        return dataObjectSchema.finaliseContainer(StatementTransaction.class);
    }

    /**
     * Constructor
     */
    public StatementTransaction(Database database) {
        super(database);
    }

    /**
     * Constructor
     */
    public StatementTransaction(StatementBankTransfer statementBankTransfer, StatementFolder statementFolder) {
        this(statementFolder.getTrackingDatabase());
        setAllValues(DataObject_Id, getTrackingDatabase().getNextId()
                , StatementTransaction_StatementBankTransfer, statementBankTransfer
                , StatementTransaction_StatementFolder, statementFolder
        );
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public List<TransactionLine> getTransactionLines() {
        return get(StatementTransaction_TransactionLines);
    }

    public TransactionLine getCoreLine() {
        return get(StatementTransaction_CoreLine);
    }

    public Double getValue() {
        return get(StatementTransaction_Value);
    }

    public String getDescription() {
        return get(StatementTransaction_Description);
    }

    public Currency getCurrency() {
        return get(StatementTransaction_Currency);
    }

    public StatementBankTransfer getStatementBankTransfer() {
        return get(StatementTransaction_StatementBankTransfer);
    }

    public Bank getBank() {
        return get(StatementTransaction_Bank);
    }

    public Period getPeriod() {
        return get(StatementTransaction_Period);
    }

    public StatementFolder getStatementFolder() {
        return get(StatementTransaction_StatementFolder);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public void setStatementBankTransfer(StatementBankTransfer statementBankTransfer) {
        set(StatementTransaction_StatementBankTransfer, statementBankTransfer);
    }
}
