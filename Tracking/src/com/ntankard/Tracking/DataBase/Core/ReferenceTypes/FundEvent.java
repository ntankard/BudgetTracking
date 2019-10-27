package com.ntankard.Tracking.DataBase.Core.ReferenceTypes;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.DataObject;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Fund;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class FundEvent extends DataObject {

    // My parent
    private Fund idFund;

    // My values
    private String idCode;

    /**
     * Constructor
     */
    public FundEvent(Fund idFund, String idCode) {
        this.idFund = idFund;
        this.idCode = idCode;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public String getId() {
        return idCode;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public List<DataObject> getParents() {
        List<DataObject> toReturn = new ArrayList<>();
        toReturn.add(idFund);
        return toReturn;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public String toString() {
        return idCode;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public Fund getIdFund() {
        return idFund;
    }

    public String getIdCode() {
        return idCode;
    }
}
