package com.ntankard.budgetTracking.dataBase.core.fileManagement.statement;

import com.ntankard.budgetTracking.dataBase.core.period.Period;
import com.ntankard.dynamicGUI.javaObjectDatabase.Displayable_DataObject;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.Database;

import java.util.Date;
import java.util.List;

public class TransactionLine extends DataObject {

    public interface TransactionLineList extends List<TransactionLine> {
    }
    private static final String TransactionLine_Prefix = "TransactionLine_";

    public static final String TransactionLine_StatementDocument = TransactionLine_Prefix + "StatementDocument";
    public static final String TransactionLine_Period = TransactionLine_Prefix + "Period";
    public static final String TransactionLine_Date = TransactionLine_Prefix + "Date";
    public static final String TransactionLine_Value = TransactionLine_Prefix + "Value";
    public static final String TransactionLine_Description = TransactionLine_Prefix + "Description";
    public static final String TransactionLine_StatementTransaction = TransactionLine_Prefix + "StatementTransaction";
    public static final String TransactionLine_RawLine = TransactionLine_Prefix + "RawLine";

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = Displayable_DataObject.getDataObjectSchema();

        // ID
        dataObjectSchema.add(new DataField_Schema<>(TransactionLine_StatementDocument, StatementDocument.class));
        dataObjectSchema.add(new DataField_Schema<>(TransactionLine_Period, Period.class));
        dataObjectSchema.add(new DataField_Schema<>(TransactionLine_Date, Date.class));
        dataObjectSchema.add(new DataField_Schema<>(TransactionLine_Value, Double.class));
        dataObjectSchema.add(new DataField_Schema<>(TransactionLine_Description, String.class));
        dataObjectSchema.add(new DataField_Schema<>(TransactionLine_StatementTransaction, StatementTransaction.class, true));
        dataObjectSchema.add(new DataField_Schema<>(TransactionLine_RawLine, String.class));
        // Children

        // StatementTransaction ========================================================================================
        dataObjectSchema.get(TransactionLine_StatementTransaction).setManualCanEdit(true);
        //==============================================================================================================

        return dataObjectSchema.finaliseContainer(TransactionLine.class);
    }

    /**
     * Constructor
     */
    public TransactionLine(Database database) {
        super(database);
    }

    /**
     * Constructor
     */
    public TransactionLine(StatementDocument statementDocument, Period period, Date date, Double value, String description, StatementTransaction statementTransaction, String rawLine) {
        this(statementDocument.getTrackingDatabase());
        setAllValues(DataObject_Id, getTrackingDatabase().getNextId()
                , TransactionLine_StatementDocument, statementDocument
                , TransactionLine_Period, period
                , TransactionLine_Date, date
                , TransactionLine_Value, value
                , TransactionLine_Description, description
                , TransactionLine_StatementTransaction, statementTransaction
                , TransactionLine_RawLine, rawLine
        );
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public StatementDocument getStatementDocument() {
        return get(TransactionLine_StatementDocument);
    }

    public Period getPeriod() {
        return get(TransactionLine_Period);
    }

    public Date getDate() {
        return get(TransactionLine_Date);
    }

    public Double getValue() {
        return get(TransactionLine_Value);
    }

    public String getDescription() {
        return get(TransactionLine_Description);
    }

    public StatementTransaction getStatementTransaction() {
        return get(TransactionLine_StatementTransaction);
    }

    public String getRawLine() {
        return get(TransactionLine_RawLine);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public void setStatementTransaction(StatementTransaction statementTransaction) {
        set(TransactionLine_StatementTransaction, statementTransaction);
    }
}
