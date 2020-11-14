package com.ntankard.tracking.dataBase.core;

import com.ntankard.javaObjectDatabase.coreObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.coreObject.DataObject;
import com.ntankard.javaObjectDatabase.coreObject.field.DataField_Schema;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase_Schema;
import com.ntankard.tracking.dataBase.core.transfer.bank.BankTransfer;

public class Receipt extends DataObject {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String Receipt_FileName = "getFileName";
    public static final String Receipt_BankTransfer = "getBankTransfer";

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getFieldContainer() {
        DataObject_Schema dataObjectSchema = DataObject.getFieldContainer();

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
     * Create a new Receipt object
     */
    public static Receipt make(Integer id, String fileName, BankTransfer bankTransfer) {
        TrackingDatabase trackingDatabase = bankTransfer.getTrackingDatabase();
        TrackingDatabase_Schema trackingDatabase_schema = trackingDatabase.getSchema();
        return assembleDataObject(trackingDatabase, trackingDatabase_schema.getClassSchema(Receipt.class), new Receipt()
                , DataObject_Id, id
                , Receipt_FileName, fileName
                , Receipt_BankTransfer, bankTransfer
        );
    }

    /**
     * {@inheritDoc
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
