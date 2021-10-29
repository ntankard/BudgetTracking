package com.ntankard.budgetTracking.dataBase.core.fileManagement.statement;

import com.ntankard.budgetTracking.dataBase.core.pool.Bank;
import com.ntankard.dynamicGUI.javaObjectDatabase.Displayable_DataObject;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.Database;

public class StatementTransactionAutoGroup extends DataObject {

    private static final String StatementTransactionAutoGroup_Prefix = "StatementTransactionAutoGroup_";

    public static final String StatementTransactionAutoGroup_Bank = StatementTransactionAutoGroup_Prefix + "Bank";
    public static final String StatementTransactionAutoGroup_Regex = StatementTransactionAutoGroup_Prefix + "Regex";
    public static final String StatementTransactionAutoGroup_Name = StatementTransactionAutoGroup_Prefix + "Name";

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = Displayable_DataObject.getDataObjectSchema();

        // ID
        dataObjectSchema.add(new DataField_Schema<>(StatementTransactionAutoGroup_Bank, Bank.class));
        dataObjectSchema.add(new DataField_Schema<>(StatementTransactionAutoGroup_Regex, String.class));
        dataObjectSchema.add(new DataField_Schema<>(StatementTransactionAutoGroup_Name, String.class));
        // Children

        // Bank ========================================================================================================
        dataObjectSchema.get(StatementTransactionAutoGroup_Bank).setManualCanEdit(true);
        // Regex =======================================================================================================
        dataObjectSchema.get(StatementTransactionAutoGroup_Regex).setManualCanEdit(true);
        // Name ========================================================================================================
        dataObjectSchema.get(StatementTransactionAutoGroup_Name).setManualCanEdit(true);
        //==============================================================================================================

        return dataObjectSchema.finaliseContainer(StatementTransactionAutoGroup.class);
    }

    /**
     * Constructor
     */
    public StatementTransactionAutoGroup(Database database) {
        super(database);
    }

    /**
     * Constructor
     */
    public StatementTransactionAutoGroup(Bank bank, String regex, String name) {
        this(bank.getTrackingDatabase());
        setAllValues(DataObject_Id, getTrackingDatabase().getNextId()
                , StatementTransactionAutoGroup_Bank, bank
                , StatementTransactionAutoGroup_Regex, regex
                , StatementTransactionAutoGroup_Name, name
        );
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public Bank getBank() {
        return get(StatementTransactionAutoGroup_Bank);
    }

    public String getRegex() {
        return get(StatementTransactionAutoGroup_Regex);
    }

    public String getName() {
        return get(StatementTransactionAutoGroup_Name);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public void setBank(Bank bank) {
        set(StatementTransactionAutoGroup_Bank, bank);
    }

    public void setRegex(String regex) {
        set(StatementTransactionAutoGroup_Regex, regex);
    }

    public void setName(String name) {
        set(StatementTransactionAutoGroup_Name, name);
    }
}
