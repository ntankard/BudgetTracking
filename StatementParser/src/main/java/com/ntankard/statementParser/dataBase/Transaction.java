package com.ntankard.statementParser.dataBase;

import com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties;
import com.ntankard.dynamicGUI.javaObjectDatabase.Displayable_DataObject;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.dataObject.interfaces.Ordered;
import com.ntankard.statementParser.dataBase.transactionGroup.TransactionGroup;

import java.util.Date;
import java.util.List;

import static com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties.DataType.CURRENCY_YEN;
import static com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties.INFO_DISPLAY;

public class Transaction extends DataObject implements Ordered {

    public interface TransactionList extends List<Transaction> {
    }

    private static final String Transaction_Prefix = "Transaction_";

    public static final String Transaction_StatementFolder = Transaction_Prefix + "StatementFolder";
    public static final String Transaction_TransactionPeriod = Transaction_Prefix + "TransactionPeriod";
    public static final String Transaction_Date = Transaction_Prefix + "Date";
    public static final String Transaction_Description = Transaction_Prefix + "Description";
    public static final String Transaction_Value = Transaction_Prefix + "Value";
    public static final String Transaction_Line = Transaction_Prefix + "Line";
    public static final String Transaction_RawLine = Transaction_Prefix + "RawLine";
    public static final String Transaction_TransactionGroup = Transaction_Prefix + "TransactionGroup";

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = Displayable_DataObject.getDataObjectSchema();

        // ID
        dataObjectSchema.add(new DataField_Schema<>(Transaction_StatementFolder, StatementFolder.class));
        dataObjectSchema.add(new DataField_Schema<>(Transaction_TransactionPeriod, TransactionPeriod.class));
        dataObjectSchema.add(new DataField_Schema<>(Transaction_Date, Date.class));
        dataObjectSchema.add(new DataField_Schema<>(Transaction_Description, String.class));
        dataObjectSchema.add(new DataField_Schema<>(Transaction_Value, Double.class));
        dataObjectSchema.add(new DataField_Schema<>(Transaction_Line, String[].class));
        dataObjectSchema.add(new DataField_Schema<>(Transaction_RawLine, String.class));
        dataObjectSchema.add(new DataField_Schema<>(Transaction_TransactionGroup, TransactionGroup.class, true));
        // ChildrenField

        // Transaction_StatementFolder =================================================================================
        dataObjectSchema.get(Transaction_StatementFolder).getProperty(Display_Properties.class).setVerbosityLevel(INFO_DISPLAY);
        // Transaction_Description =====================================================================================
        dataObjectSchema.get(Transaction_Description).setManualCanEdit(true);
        // Transaction_Value ===========================================================================================
        dataObjectSchema.get(Transaction_Value).getProperty(Display_Properties.class).setDataType(CURRENCY_YEN);
        // Transaction_Line ============================================================================================
        dataObjectSchema.get(Transaction_Line).getProperty(Display_Properties.class).setVerbosityLevel(INFO_DISPLAY);
        // Transaction_TransactionGroup ================================================================================
        dataObjectSchema.get(Transaction_TransactionGroup).getProperty(Display_Properties.class).setVerbosityLevel(INFO_DISPLAY);
        dataObjectSchema.get(Transaction_TransactionGroup).setManualCanEdit(true);
        // =============================================================================================================

        return dataObjectSchema.finaliseContainer(Transaction.class);
    }

    /**
     * Constructor
     */
    public Transaction(StatementFolder statementFolder, TransactionPeriod transactionPeriod, Date date, String description, Double value, String line) {
        super(statementFolder.getTrackingDatabase()
                , Transaction_StatementFolder, statementFolder
                , Transaction_TransactionPeriod, transactionPeriod
                , Transaction_Date, date
                , Transaction_Description, description
                , Transaction_Value, value
                , Transaction_Line, line
                , Transaction_RawLine, line
                , Transaction_TransactionGroup, null
        );
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### General #####################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @inheritDoc
     */
    @Override
    public String toString() {
        return getDescription();
    }

    /**
     * @inheritDoc
     */
    @Override
    public Integer getOrder() {
        return -(int) Math.round(getValue());
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public StatementFolder getStatementFolder() {
        return get(Transaction_StatementFolder);
    }

    public TransactionPeriod getTransactionPeriod() {
        return get(Transaction_TransactionPeriod);
    }

    public Date getDate() {
        return get(Transaction_Date);
    }

    public String getDescription() {
        return get(Transaction_Description);
    }

    public Double getValue() {
        return get(Transaction_Value);
    }

    public String[] getLine() {
        return get(Transaction_Line);
    }

    public String getRawLine() {
        return get(Transaction_RawLine);
    }

    public void setTransactionGroup(TransactionGroup transactionGroup) {
        set(Transaction_TransactionGroup, transactionGroup);
    }
}
