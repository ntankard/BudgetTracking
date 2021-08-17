package com.ntankard.budgetTracking.dataBase.core.recurringPayment;

import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.javaObjectDatabase.database.Database_Schema;
import com.ntankard.budgetTracking.dataBase.core.period.ExistingPeriod;
import com.ntankard.budgetTracking.dataBase.core.pool.Bank;
import com.ntankard.budgetTracking.dataBase.core.pool.category.SolidCategory;
import com.ntankard.budgetTracking.dataBase.core.transfer.bank.RecurringBankTransfer;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;

public class FixedRecurringPayment extends RecurringPayment {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = RecurringPayment.getDataObjectSchema();

        // Class behavior
        dataObjectSchema.addObjectFactory(RecurringBankTransfer.Factory);

        // ID
        // Name
        // Start
        // End
        // Bank
        // Category
        // Value
        // Currency
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(FixedRecurringPayment.class);
    }

    /**
     * Constructor
     */
    public FixedRecurringPayment(Database database) {
        super(database);
    }

    /**
     * Constructor
     */
    public FixedRecurringPayment(String name, Double value, ExistingPeriod start, Bank bank, SolidCategory solidCategory, Integer duration) {
        this(solidCategory.getTrackingDatabase());
        setAllValues(DataObject_Id, getTrackingDatabase().getNextId()
                , NamedDataObject_Name, name
                , RecurringPayment_Value, value
                , RecurringPayment_Start, start
                , RecurringPayment_Bank, bank
                , RecurringPayment_Category, solidCategory
                , RecurringPayment_Duration, duration
        );
    }
}
