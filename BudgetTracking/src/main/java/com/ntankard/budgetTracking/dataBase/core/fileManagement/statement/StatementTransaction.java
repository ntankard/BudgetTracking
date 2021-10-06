package com.ntankard.budgetTracking.dataBase.core.fileManagement.statement;

import com.ntankard.budgetTracking.dataBase.core.fileManagement.statement.TransactionLine.TransactionLineList;
import com.ntankard.dynamicGUI.javaObjectDatabase.Displayable_DataObject;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.ListDataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.Database;

import java.util.List;

import static com.ntankard.javaObjectDatabase.dataField.dataCore.DataCore_Factory.createSelfParentList;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Source_Factory.makeSourceChain;

public class StatementTransaction extends DataObject {

    private static final String StatementTransaction_Prefix = "StatementTransaction_";

    public static final String StatementTransaction_StatementFolder = StatementTransaction_Prefix + "StatementFolder";
    public static final String StatementTransaction_TransactionLines = StatementTransaction_Prefix + "TransactionLines";
    public static final String StatementTransaction_CoreLine = StatementTransaction_Prefix + "CoreLine";

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = Displayable_DataObject.getDataObjectSchema();

        // ID
        dataObjectSchema.add(new DataField_Schema<>(StatementTransaction_StatementFolder, StatementFolder.class));
        dataObjectSchema.add(new ListDataField_Schema<>(StatementTransaction_TransactionLines, TransactionLineList.class));
        dataObjectSchema.add(new DataField_Schema<>(StatementTransaction_CoreLine, TransactionLine.class, true));
        // Children

        // TransactionLines ============================================================================================
        dataObjectSchema.<List<TransactionLine>>get(StatementTransaction_TransactionLines).setDataCore_schema(
                createSelfParentList(TransactionLine.class, null));
        // CoreLine ====================================================================================================
        dataObjectSchema.<TransactionLine>get(StatementTransaction_CoreLine).setDataCore_schema(
                new Derived_DataCore_Schema<TransactionLine, StatementTransaction>
                        (container -> container.getTransactionLines().size() == 0 ? null : container.getTransactionLines().get(0)
                                , makeSourceChain(StatementTransaction_TransactionLines)));
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
    public StatementTransaction(StatementFolder statementFolder) {
        this(statementFolder.getTrackingDatabase());
        setAllValues(DataObject_Id, getTrackingDatabase().getNextId()
                , StatementTransaction_StatementFolder, statementFolder
        );
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public StatementFolder getStatementFolder() {
        return get(StatementTransaction_StatementFolder);
    }

    public List<TransactionLine> getTransactionLines() {
        return get(StatementTransaction_TransactionLines);
    }

    public TransactionLine getCoreLine() {
        return get(StatementTransaction_CoreLine);
    }
}
