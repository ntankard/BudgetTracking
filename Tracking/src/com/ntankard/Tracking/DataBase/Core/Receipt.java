package com.ntankard.Tracking.DataBase.Core;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.Transfers.BankCategoryTransfer;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.DEBUG_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class Receipt extends DataObject {

    // My parents
    private BankCategoryTransfer bankCategoryTransfer;

    // My values
    private String fileName;

    // Non save values
    private boolean firstFile = false;

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getFileName", "getBankCategoryTransfer"})
    public Receipt(Integer id, String fileName, BankCategoryTransfer bankCategoryTransfer) {
        super(id);
        this.fileName = fileName;
        this.bankCategoryTransfer = bankCategoryTransfer;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 21)
    public List<DataObject> getParents() {
        List<DataObject> toReturn = new ArrayList<>();
        toReturn.add(getBankCategoryTransfer());
        return toReturn;
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################### Non save members #################################################
    //------------------------------------------------------------------------------------------------------------------

    @DisplayProperties(order = 4)
    public boolean isFirstFile() {
        return firstFile;
    }

    public void setFirstFile(boolean firstFile) {
        this.firstFile = firstFile;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @DisplayProperties(order = 2)
    public BankCategoryTransfer getBankCategoryTransfer() {
        return bankCategoryTransfer;
    }

    @DisplayProperties(order = 3)
    public String getFileName() {
        return fileName;
    }
}
