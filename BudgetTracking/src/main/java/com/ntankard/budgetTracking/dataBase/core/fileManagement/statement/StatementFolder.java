package com.ntankard.budgetTracking.dataBase.core.fileManagement.statement;

import com.ntankard.budgetTracking.dataBase.core.period.ExistingPeriod;
import com.ntankard.budgetTracking.dataBase.core.pool.Bank;
import com.ntankard.dynamicGUI.javaObjectDatabase.Displayable_DataObject;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.Database;

public class StatementFolder extends Displayable_DataObject {

    private static final String StatementFolder_Prefix = "StatementFolder_";

    public static final String StatementFolder_Period = StatementFolder_Prefix + "Period";
    public static final String StatementFolder_Bank = StatementFolder_Prefix + "Bank";
    public static final String StatementFolder_TranslationTypes = StatementFolder_Prefix + "TranslationTypes";

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = Displayable_DataObject.getDataObjectSchema();

        // ID
        dataObjectSchema.add(new DataField_Schema<>(StatementFolder_Period, ExistingPeriod.class));
        dataObjectSchema.add(new DataField_Schema<>(StatementFolder_Bank, Bank.class));
        dataObjectSchema.add(new DataField_Schema<>(StatementFolder_TranslationTypes, TranslationTypes.class));
        // Children

        return dataObjectSchema.finaliseContainer(StatementFolder.class);
    }

    /**
     * Constructor
     */
    public StatementFolder(Database database, Object... args) {
        super(database, args);
    }

    /**
     * Constructor
     */
    public StatementFolder(ExistingPeriod period, Bank bank, TranslationTypes translationTypes) {
        super(period.getTrackingDatabase()
                , StatementFolder_Period, period
                , StatementFolder_Bank, bank
                , StatementFolder_TranslationTypes, translationTypes
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
        return getPeriod() + "-" + getBank();
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public ExistingPeriod getPeriod() {
        return get(StatementFolder_Period);
    }

    public Bank getBank() {
        return get(StatementFolder_Bank);
    }

    public TranslationTypes getTranslationTypes() {
        return get(StatementFolder_TranslationTypes);
    }
}
