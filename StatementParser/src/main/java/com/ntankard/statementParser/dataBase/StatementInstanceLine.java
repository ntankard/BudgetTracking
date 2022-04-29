package com.ntankard.statementParser.dataBase;

import com.ntankard.dynamicGUI.javaObjectDatabase.Displayable_DataObject;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;

import java.util.Date;

public class StatementInstanceLine extends DataObject {

    private static final String StatementInstanceLine_Prefix = "StatementInstanceLine_";

    public static final String StatementInstanceLine_StatementInstance = StatementInstanceLine_Prefix + "StatementInstance";
    public static final String StatementInstanceLine_TransactionPeriod = StatementInstanceLine_Prefix + "TransactionPeriod";
    public static final String StatementInstanceLine_Date = StatementInstanceLine_Prefix + "Date";
    public static final String StatementInstanceLine_Description = StatementInstanceLine_Prefix + "Description";
    public static final String StatementInstanceLine_Value = StatementInstanceLine_Prefix + "Value";
    public static final String StatementInstanceLine_Line = StatementInstanceLine_Prefix + "Line";
    public static final String StatementInstanceLine_RawLine = StatementInstanceLine_Prefix + "RawLine";
    public static final String StatementInstanceLine_Transaction = StatementInstanceLine_Prefix + "Transaction";

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = Displayable_DataObject.getDataObjectSchema();

        // ID
        dataObjectSchema.add(new DataField_Schema<>(StatementInstanceLine_StatementInstance, StatementInstance.class));
        dataObjectSchema.add(new DataField_Schema<>(StatementInstanceLine_TransactionPeriod, TransactionPeriod.class));
        dataObjectSchema.add(new DataField_Schema<>(StatementInstanceLine_Date, Date.class));
        dataObjectSchema.add(new DataField_Schema<>(StatementInstanceLine_Description, String.class));
        dataObjectSchema.add(new DataField_Schema<>(StatementInstanceLine_Value, Double.class));
        dataObjectSchema.add(new DataField_Schema<>(StatementInstanceLine_Line, String[].class));
        dataObjectSchema.add(new DataField_Schema<>(StatementInstanceLine_RawLine, String.class));
        dataObjectSchema.add(new DataField_Schema<>(StatementInstanceLine_Transaction, Transaction.class, true));
        // ChildrenField

        // StatementInstanceLine_Transaction ===========================================================================
        dataObjectSchema.get(StatementInstanceLine_Transaction).setManualCanEdit(true);
        // =============================================================================================================

        return dataObjectSchema.finaliseContainer(StatementInstanceLine.class);
    }

    /**
     * Constructor
     */
    public StatementInstanceLine(StatementInstance statementInstance, TransactionPeriod transactionPeriod, Date date, String description, Double value, String line) {
        super(statementInstance.getTrackingDatabase()
                , StatementInstanceLine_StatementInstance, statementInstance
                , StatementInstanceLine_TransactionPeriod, transactionPeriod
                , StatementInstanceLine_Date, date
                , StatementInstanceLine_Description, description
                , StatementInstanceLine_Value, value
                , StatementInstanceLine_Line, line
                , StatementInstanceLine_RawLine, line
                , StatementInstanceLine_Transaction, null
        );
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public StatementInstance getStatementInstance() {
        return get(StatementInstanceLine_StatementInstance);
    }

    public TransactionPeriod getTransactionPeriod() {
        return get(StatementInstanceLine_TransactionPeriod);
    }

    public Date getDate() {
        return get(StatementInstanceLine_Date);
    }

    public String getDescription() {
        return get(StatementInstanceLine_Description);
    }

    public Double getValue() {
        return get(StatementInstanceLine_Value);
    }

    public String[] getLine() {
        return get(StatementInstanceLine_Line);
    }

    public String getRawLine() {
        return get(StatementInstanceLine_RawLine);
    }

    public void setTransaction(Transaction transaction) {
        set(StatementInstanceLine_Transaction, transaction);
    }
}
