package com.ntankard.Tracking.DataBase.Core.RecurringPayment;

import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.SolidCategory;
import com.ntankard.Tracking.DataBase.Core.Transfer.Bank.RecurringBankTransfer;
import com.ntankard.javaObjectDatabase.CoreObject.FieldContainer;

public class FixedRecurringPayment extends RecurringPayment {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = RecurringPayment.getFieldContainer();

        // Class behavior
        fieldContainer.addObjectFactory(RecurringBankTransfer.Factory);

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

        return fieldContainer.finaliseContainer(FixedRecurringPayment.class);
    }

    /**
     * Create a new FixedRecurringPayment object
     */
    public static FixedRecurringPayment make(Integer id, String name, Double value, ExistingPeriod start, ExistingPeriod end, Bank bank, SolidCategory solidCategory) {
        return assembleDataObject(FixedRecurringPayment.getFieldContainer(), new FixedRecurringPayment()
                , DataObject_Id, id
                , NamedDataObject_Name, name
                , RecurringPayment_Value, value
                , RecurringPayment_Start, start
                , RecurringPayment_End, end
                , RecurringPayment_Bank, bank
                , RecurringPayment_Category, solidCategory
        );
    }
}
