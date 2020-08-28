package com.ntankard.Tracking.DataBase.Core;

import com.ntankard.dynamicGUI.CoreObject.Field.DataCore.ValueRead_DataCore;
import com.ntankard.dynamicGUI.CoreObject.FieldContainer;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Tracking_DataField;
import com.ntankard.Tracking.DataBase.Core.Transfer.Bank.BankTransfer;

public class Receipt extends DataObject {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String Receipt_FileName = "getFileName";
    public static final String Receipt_BankTransfer = "getBankTransfer";

    /**
     * Get all the fields for this object
     */
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = DataObject.getFieldContainer();

        // ID
        // FileName ======================================================================================================
        fieldContainer.add(new Tracking_DataField<>(Receipt_FileName, String.class));
        // BankTransfer ========================================================================================================
        fieldContainer.add(new Tracking_DataField<>(Receipt_BankTransfer, BankTransfer.class));
        fieldContainer.get(Receipt_BankTransfer).setDataCore(new ValueRead_DataCore<>(true));
        //==============================================================================================================
        // Parents
        // Children

        return fieldContainer.finaliseContainer(Receipt.class);
    }

    /**
     * Create a new Receipt object
     */
    public static Receipt make(Integer id, String fileName, BankTransfer bankTransfer) {
        return assembleDataObject(Receipt.getFieldContainer(), new Receipt()
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