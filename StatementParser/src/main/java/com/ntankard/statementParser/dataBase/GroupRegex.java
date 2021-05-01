package com.ntankard.statementParser.dataBase;

import com.ntankard.dynamicGUI.javaObjectDatabase.Displayable_DataObject;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.Database;

public class GroupRegex extends DataObject {

    private static final String GroupRegex_Prefix = "GroupRegex_";

    public static final String GroupRegex_BankAccount = GroupRegex_Prefix + "BankAccount";
    public static final String GroupRegex_Regex = GroupRegex_Prefix + "Regex";
    public static final String GroupRegex_Name = GroupRegex_Prefix + "Name";

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = Displayable_DataObject.getDataObjectSchema();

        // ID
        dataObjectSchema.add(new DataField_Schema<>(GroupRegex_BankAccount, BankAccount.class));
        dataObjectSchema.add(new DataField_Schema<>(GroupRegex_Regex, String.class));
        dataObjectSchema.add(new DataField_Schema<>(GroupRegex_Name, String.class));
        // ChildrenField

        return dataObjectSchema.finaliseContainer(GroupRegex.class);
    }

    /**
     * Constructor
     */
    public GroupRegex(Database database) {
        super(database);
    }

    /**
     * Constructor
     */
    public GroupRegex(BankAccount bankAccount, String regex, String name) {
        super(bankAccount.getTrackingDatabase());

        setAllValues(DataObject_Id, getTrackingDatabase().getNextId()
                , GroupRegex_BankAccount, bankAccount
                , GroupRegex_Regex, regex
                , GroupRegex_Name, name
        );
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public BankAccount getBankAccount() {
        return get(GroupRegex_BankAccount);
    }

    public String getRegex() {
        return get(GroupRegex_Regex);
    }

    public String getName() {
        return get(GroupRegex_Name);
    }
}
