package com.ntankard.budgetTracking.dataBase.core;

import com.ntankard.dynamicGUI.javaObjectDatabase.Displayable_DataObject;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.javaObjectDatabase.database.Database_Schema;
import com.ntankard.budgetTracking.dataBase.core.transfer.bank.BankTransfer;

public class Receipt extends DataObject {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String Receipt_FileName = "getFileName";
    public static final String Receipt_BankTransfer = "getBankTransfer";

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = Displayable_DataObject.getDataObjectSchema();

        // ID
        // FileName ======================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Receipt_FileName, String.class));
        // BankTransfer ========================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Receipt_BankTransfer, BankTransfer.class));
        dataObjectSchema.get(Receipt_BankTransfer).setManualCanEdit(true);
        //==============================================================================================================
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(Receipt.class);
    }

    /**
     * Constructor
     */
    public Receipt(Database database) {
        super(database);
    }

    /**
     * Constructor
     */
    public Receipt(String fileName, BankTransfer bankTransfer) {
        this(bankTransfer.getTrackingDatabase());
        setAllValues(DataObject_Id, getTrackingDatabase().getNextId()
                , Receipt_FileName, fileName
                , Receipt_BankTransfer, bankTransfer
        );
    }

    /**
     * @inheritDoc
     */
    @Override
    public void remove() {
        super.remove_impl();
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public BankTransfer getBankTransfer() {
        return get(Receipt_BankTransfer);
    }

    public String getFileName() {
        return get(Receipt_FileName);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public void setBankTransfer(BankTransfer bankTransfer) {
        set(Receipt_BankTransfer, bankTransfer);
    }
}
