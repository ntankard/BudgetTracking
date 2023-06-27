package com.ntankard.budgetTracking.dataBase.core.recurringPayment;

import com.ntankard.budgetTracking.dataBase.core.period.ExistingPeriod;
import com.ntankard.budgetTracking.dataBase.core.pool.Bank;
import com.ntankard.budgetTracking.dataBase.core.pool.category.SolidCategory;
import com.ntankard.budgetTracking.dataBase.core.transfer.bank.RecurringBankTransfer;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.Database;

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
    public FixedRecurringPayment(Database database, Object... args) {
        super(database, args);
    }

    /**
     * Constructor
     */
    public FixedRecurringPayment(String name, Double value, ExistingPeriod start, Bank bank, SolidCategory solidCategory, Integer duration) {
        super(solidCategory.getTrackingDatabase()
                , NamedDataObject_Name, name
                , RecurringPayment_Value, value
                , RecurringPayment_Start, start
                , RecurringPayment_Bank, bank
                , RecurringPayment_Category, solidCategory
                , RecurringPayment_Duration, duration
        );
    }
}
