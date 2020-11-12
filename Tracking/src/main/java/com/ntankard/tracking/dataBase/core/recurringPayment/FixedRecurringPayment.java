package com.ntankard.tracking.dataBase.core.recurringPayment;

import com.ntankard.tracking.dataBase.core.period.ExistingPeriod;
import com.ntankard.tracking.dataBase.core.pool.Bank;
import com.ntankard.tracking.dataBase.core.pool.category.SolidCategory;
import com.ntankard.tracking.dataBase.core.transfer.bank.RecurringBankTransfer;
import com.ntankard.javaObjectDatabase.coreObject.DataObject_Schema;

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
        return assembleDataObject(FixedRecurringPayment.getFieldContainer(), new FixedRecurringPayment()
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
