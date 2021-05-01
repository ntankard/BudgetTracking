package com.ntankard.statementParser.dataBase.transactionGroup;

import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.statementParser.dataBase.StatementFolder;
import com.ntankard.statementParser.dataBase.TransactionPeriod;

public class Matching_TransactionGroup extends TransactionGroup {

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = TransactionGroup.getDataObjectSchema();

        return dataObjectSchema.finaliseContainer(Matching_TransactionGroup.class);
    }

    /**
     * Constructor
     */
    public Matching_TransactionGroup(StatementFolder statementFolder, String name, TransactionPeriod transactionPeriod) {
        super(statementFolder.getTrackingDatabase());
        setAllValues(DataObject_Id, getTrackingDatabase().getNextId()
                , TransactionGroup_StatementFolder, statementFolder
                , TransactionGroup_Name, name
                , TransactionGroup_TransactionPeriod, transactionPeriod
        );
    }
}
