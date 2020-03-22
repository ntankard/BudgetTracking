package com.ntankard.Tracking.DataBase.Core;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.DataObject_Field;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Field;
import com.ntankard.Tracking.DataBase.Core.Transfer.Bank.BankTransfer;

import java.util.List;

@ClassExtensionProperties(includeParent = true)
public class Receipt extends DataObject {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static List<Field<?>> getFields() {
        List<Field<?>> toReturn = DataObject.getFields();
        toReturn.add(new Field<>("getFileName", String.class));
        toReturn.add(new DataObject_Field<>("getBankTransfer", BankTransfer.class));
        return toReturn;
    }

    /**
     * Create a new Receipt object
     */
    public static Receipt make(Integer id, String fileName, BankTransfer bankTransfer) {
        return assembleDataObject(Receipt.getFields(), new Receipt()
                , "getId", id
                , "getFileName", fileName
                , "getBankTransfer", bankTransfer
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

    // 1000000--getID

    @DisplayProperties(order = 1100000)
    public BankTransfer getBankTransfer() {
        return get("getBankTransfer");
    }

    @DisplayProperties(order = 1200000)
    public String getFileName() {
        return get("getFileName");
    }

    // 1300000----isFirstFile (Above)
    // 2000000--getParents (Above)
    // 3000000--getChildren

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @SetterProperties(localSourceMethod = "sourceOptions", displaySet = false)
    public void setBankTransfer(BankTransfer bankTransfer) {
        set("getBankTransfer", bankTransfer);
    }
}
