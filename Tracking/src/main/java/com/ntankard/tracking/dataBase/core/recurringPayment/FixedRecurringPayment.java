package com.ntankard.tracking.dataBase.core.recurringPayment;

import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.javaObjectDatabase.database.Database_Schema;
import com.ntankard.tracking.dataBase.core.period.ExistingPeriod;
import com.ntankard.tracking.dataBase.core.pool.Bank;
import com.ntankard.tracking.dataBase.core.pool.category.SolidCategory;
import com.ntankard.tracking.dataBase.core.transfer.bank.RecurringBankTransfer;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;

public class FixedRecurringPayment extends RecurringPayment {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getFieldContainer() {
        DataObject_Schema dataObjectSchema = RecurringPayment.getFieldContainer();

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
     * Create a new FixedRecurringPayment object
     */
    public static FixedRecurringPayment make(Integer id, String name, Double value, ExistingPeriod start, Bank bank, SolidCategory solidCategory, Integer duration) {
        Database database = solidCategory.getTrackingDatabase();
        Database_Schema database_schema = database.getSchema();
        return assembleDataObject(database, database_schema.getClassSchema(FixedRecurringPayment.class), new FixedRecurringPayment()
                , DataObject_Id, id
                , NamedDataObject_Name, name
                , RecurringPayment_Value, value
                , RecurringPayment_Start, start
                , RecurringPayment_Bank, bank
                , RecurringPayment_Category, solidCategory
                , RecurringPayment_Duration, duration
        );
    }
}
