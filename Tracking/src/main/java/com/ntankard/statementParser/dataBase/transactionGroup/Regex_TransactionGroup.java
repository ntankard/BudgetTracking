package com.ntankard.statementParser.dataBase.transactionGroup;

import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.statementParser.dataBase.GroupRegex;
import com.ntankard.statementParser.dataBase.StatementFolder;
import com.ntankard.statementParser.dataBase.TransactionPeriod;

public class Regex_TransactionGroup extends TransactionGroup {

    private static final String Regex_TransactionGroup_Prefix = "Regex_TransactionGroup_";

    public static final String TransactionGroup_Grouping = Regex_TransactionGroup_Prefix + "Grouping";

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = TransactionGroup.getDataObjectSchema();

        // TransactionGroup_Count ======================================================================================
        dataObjectSchema.add(new DataField_Schema<>(TransactionGroup_Grouping, GroupRegex.class, true));
        // =============================================================================================================


        return dataObjectSchema.finaliseContainer(Regex_TransactionGroup.class);
    }

    /**
     * Constructor
     */
    public Regex_TransactionGroup(GroupRegex groupRegex, StatementFolder statementFolder, TransactionPeriod transactionPeriod) {
        super(statementFolder.getTrackingDatabase());
        setAllValues(DataObject_Id, getTrackingDatabase().getNextId()
                , TransactionGroup_Grouping, groupRegex
                , TransactionGroup_StatementFolder, statementFolder
                , TransactionGroup_TransactionPeriod, transactionPeriod
                , TransactionGroup_Name, groupRegex.getName()
        );
    }
}
