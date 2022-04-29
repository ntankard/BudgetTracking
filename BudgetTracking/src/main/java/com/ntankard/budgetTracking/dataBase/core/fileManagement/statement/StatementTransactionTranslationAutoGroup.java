package com.ntankard.budgetTracking.dataBase.core.fileManagement.statement;

import com.ntankard.budgetTracking.dataBase.core.pool.Bank;
import com.ntankard.budgetTracking.dataBase.core.pool.Pool;
import com.ntankard.dynamicGUI.javaObjectDatabase.Displayable_DataObject;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.Database;

public class StatementTransactionTranslationAutoGroup extends Displayable_DataObject {

    private static final String StatementTransactionTranslationAutoGroup_Prefix = "StatementTransactionTranslationAutoGroup_";

    public static final String StatementTransactionTranslationAutoGroup_Bank = StatementTransactionTranslationAutoGroup_Prefix + "Bank";
    public static final String StatementTransactionTranslationAutoGroup_Translation = StatementTransactionTranslationAutoGroup_Prefix + "Translation";
    public static final String StatementTransactionTranslationAutoGroup_Pool = StatementTransactionTranslationAutoGroup_Prefix + "Pool";
    public static final String StatementTransactionTranslationAutoGroup_Multiply = StatementTransactionTranslationAutoGroup_Prefix + "Multiply";

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = Displayable_DataObject.getDataObjectSchema();

        // ID
        dataObjectSchema.add(new DataField_Schema<>(StatementTransactionTranslationAutoGroup_Bank, Bank.class));
        dataObjectSchema.add(new DataField_Schema<>(StatementTransactionTranslationAutoGroup_Translation, Translation.class));
        dataObjectSchema.add(new DataField_Schema<>(StatementTransactionTranslationAutoGroup_Pool, Pool.class));
        dataObjectSchema.add(new DataField_Schema<>(StatementTransactionTranslationAutoGroup_Multiply, Double.class));
        // Children

        // Bank ========================================================================================================
        dataObjectSchema.get(StatementTransactionTranslationAutoGroup_Bank).setManualCanEdit(true);
        // Translation =================================================================================================
        dataObjectSchema.get(StatementTransactionTranslationAutoGroup_Translation).setManualCanEdit(true);
        // Pool ========================================================================================================
        dataObjectSchema.get(StatementTransactionTranslationAutoGroup_Pool).setManualCanEdit(true);
        // Multiply ====================================================================================================
        dataObjectSchema.get(StatementTransactionTranslationAutoGroup_Multiply).setManualCanEdit(true);
        //==============================================================================================================

        return dataObjectSchema.finaliseContainer(StatementTransactionTranslationAutoGroup.class);
    }

    /**
     * Constructor
     */
    public StatementTransactionTranslationAutoGroup(Database database, Object... args) {
        super(database, args);
    }

    /**
     * Constructor
     */
    public StatementTransactionTranslationAutoGroup(Bank bank, Translation translation, Pool pool, Double multiply) {
        super(bank.getTrackingDatabase()
                , StatementTransactionTranslationAutoGroup_Bank, bank
                , StatementTransactionTranslationAutoGroup_Translation, translation
                , StatementTransactionTranslationAutoGroup_Pool, pool
                , StatementTransactionTranslationAutoGroup_Multiply, multiply
        );
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public Bank getBank() {
        return get(StatementTransactionTranslationAutoGroup_Bank);
    }

    public Translation getTranslation() {
        return get(StatementTransactionTranslationAutoGroup_Translation);
    }

    public Pool getPool() {
        return get(StatementTransactionTranslationAutoGroup_Pool);
    }

    public Double getMultiply() {
        return get(StatementTransactionTranslationAutoGroup_Multiply);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public void setBank(Bank bank) {
        set(StatementTransactionTranslationAutoGroup_Bank, bank);
    }

    public void setTranslation(Translation translation) {
        set(StatementTransactionTranslationAutoGroup_Translation, translation);
    }

    public void setPool(Pool pool) {
        set(StatementTransactionTranslationAutoGroup_Pool, pool);
    }

    public void setMultiply(Double multiply) {
        set(StatementTransactionTranslationAutoGroup_Multiply, multiply);
    }
}
