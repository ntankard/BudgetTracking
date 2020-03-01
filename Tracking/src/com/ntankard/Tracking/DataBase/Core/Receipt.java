package com.ntankard.Tracking.DataBase.Core;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
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

    // Non save values
    private Boolean firstFile = false;

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
    //############################################### Non save members #################################################
    //------------------------------------------------------------------------------------------------------------------

    @DisplayProperties(order = 1300000)
    public Boolean isFirstFile() {
        return firstFile;
    }

    public void setFirstFile(Boolean firstFile) {
        if (firstFile == null) throw new IllegalArgumentException("firstFile is null");
        this.firstFile = firstFile;
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
}
