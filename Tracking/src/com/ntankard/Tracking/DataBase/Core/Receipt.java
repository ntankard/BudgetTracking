package com.ntankard.Tracking.DataBase.Core;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.Transfer.Bank.BankTransfer;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.DEBUG_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class Receipt extends DataObject {

    // My parents
    private BankTransfer bankTransfer;

    // My values
    private String fileName;

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getFileName", "getBankTransfer"})
    public Receipt(Integer id, String fileName, BankTransfer bankTransfer) {
        super(id);
        if (fileName == null) throw new IllegalArgumentException("FileName is null");
        if (bankTransfer == null) throw new IllegalArgumentException("BankCategoryTransfer is null");
        this.fileName = fileName;
        this.bankTransfer = bankTransfer;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 2000000)
    public List<DataObject> getParents() {
        List<DataObject> toReturn = new ArrayList<>();
        toReturn.add(getBankTransfer());
        return toReturn;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    // 1000000--getID

    @DisplayProperties(order = 1100000)
    public BankTransfer getBankTransfer() {
        return bankTransfer;
    }

    @DisplayProperties(order = 1200000)
    public String getFileName() {
        return fileName;
    }

    // 1300000----isFirstFile (Above)
    // 2000000--getParents (Above)
    // 3000000--getChildren

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @SetterProperties(localSourceMethod = "sourceOptions", displaySet = false)
    public void setBankTransfer(BankTransfer bankTransfer) {
        if (bankTransfer == null) throw new IllegalArgumentException("BankTransfer is null");
        this.bankTransfer.notifyChildUnLink(this);
        this.bankTransfer = bankTransfer;
        this.bankTransfer.notifyChildLink(this);
    }
}
