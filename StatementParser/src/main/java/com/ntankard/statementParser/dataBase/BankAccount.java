package com.ntankard.statementParser.dataBase;

import com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties;
import com.ntankard.dynamicGUI.javaObjectDatabase.Displayable_DataObject;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.Database;

import static com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties.INFO_DISPLAY;

public class BankAccount extends DataObject {

    private static final String BankAccount_Prefix = "BankAccount_";

    public static final String BankAccount_Name = BankAccount_Prefix + "Name";
    public static final String BankAccount_Path = BankAccount_Prefix + "Path";

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = Displayable_DataObject.getDataObjectSchema();

        // ID
        dataObjectSchema.add(new DataField_Schema<>(BankAccount_Name, String.class));
        dataObjectSchema.add(new DataField_Schema<>(BankAccount_Path, String.class));
        // ChildrenField

        // BankAccount_Path ============================================================================================
        dataObjectSchema.get(BankAccount_Path).getProperty(Display_Properties.class).setVerbosityLevel(INFO_DISPLAY);

        return dataObjectSchema.finaliseContainer(BankAccount.class);
    }

    /**
     * Constructor
     */
    public BankAccount(Database database, Object... args) {
        super(database, args);
    }

    /**
     * Constructor
     */
    public BankAccount(Database database, String name, String path) {
        super(database
                , BankAccount_Name, name
                , BankAccount_Path, path
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
        return getName();
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public String getName() {
        return get(BankAccount_Name);
    }

    public String getPath() {
        return get(BankAccount_Path);
    }
}
