package com.ntankard.statementParser.dataBase.transactionGroup;

import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.statementParser.dataBase.StatementFolder;
import com.ntankard.statementParser.dataBase.TransactionPeriod;

public class Single_TransactionGroup extends TransactionGroup {

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = TransactionGroup.getDataObjectSchema();

        return dataObjectSchema.finaliseContainer(Single_TransactionGroup.class);
    }

    /**
     * Constructor
     */
    public Single_TransactionGroup(StatementFolder statementFolder, String name, TransactionPeriod transactionPeriod) {
        super(statementFolder.getTrackingDatabase()
                , TransactionGroup_StatementFolder, statementFolder
                , TransactionGroup_Name, name
                , TransactionGroup_TransactionPeriod, transactionPeriod
        );
    }
}
