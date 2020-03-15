package com.ntankard.Tracking.DataBase.Core;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.DataObject_Field;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Field;
import com.ntankard.Tracking.DataBase.Core.Transfer.Bank.BankTransfer;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import java.util.List;

@ClassExtensionProperties(includeParent = true)
public class Receipt extends DataObject {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static List<Field<?>> getFields(Integer id, String fileName, BankTransfer bankTransfer, DataObject container) {
        List<Field<?>> toReturn = DataObject.getFields(id, container);
        toReturn.add(new Field<>("fileName", String.class, fileName, container));
        toReturn.add(new DataObject_Field<>("bankTransfer", BankTransfer.class, bankTransfer, container));
        return toReturn;
    }

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getFileName", "getBankTransfer"})
    public Receipt(Integer id, String fileName, BankTransfer bankTransfer) {
        super();
        setFields(getFields(id, fileName, bankTransfer, this));
    }

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
        return get("bankTransfer");
    }

    @DisplayProperties(order = 1200000)
    public String getFileName() {
        return get("fileName");
    }

    // 1300000----isFirstFile (Above)
    // 2000000--getParents (Above)
    // 3000000--getChildren

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @SetterProperties(localSourceMethod = "sourceOptions", displaySet = false)
    public void setBankTransfer(BankTransfer bankTransfer) {
        set("bankTransfer", bankTransfer);
    }
}
