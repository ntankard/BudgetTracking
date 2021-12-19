package com.ntankard.budgetTracking.dataBase.core.fileManagement.statement;

import com.ntankard.budgetTracking.dataBase.core.period.ExistingPeriod;
import com.ntankard.budgetTracking.dataBase.core.pool.Bank;
import com.ntankard.dynamicGUI.javaObjectDatabase.Displayable_DataObject;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.Database;

public class StatementFolder extends DataObject {

    private static final String StatementFolder_Prefix = "StatementFolder_";

    public static final String StatementFolder_Period = StatementFolder_Prefix + "Period";
    public static final String StatementFolder_Bank = StatementFolder_Prefix + "Bank";

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = Displayable_DataObject.getDataObjectSchema();

        // ID
        dataObjectSchema.add(new DataField_Schema<>(StatementFolder_Period, ExistingPeriod.class));
        dataObjectSchema.add(new DataField_Schema<>(StatementFolder_Bank, Bank.class));
        // Children

        return dataObjectSchema.finaliseContainer(StatementFolder.class);
    }

    /**
     * Constructor
     */
    public StatementFolder(Database database) {
        super(database);
    }

    /**
     * Constructor
     */
    public StatementFolder(ExistingPeriod period, Bank bank) {
        this(period.getTrackingDatabase());
        setAllValues(DataObject_Id, getTrackingDatabase().getNextId()
                , StatementFolder_Period, period
                , StatementFolder_Bank, bank
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
}
