package com.ntankard.Tracking.DataBase.Core.ReferenceTypes;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.DataObject;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.NonPeriodFund;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class NonPeriodFundEvent extends DataObject {

    // My parent
    private NonPeriodFund idNonPeriodFund;

    // My values
    private String idCode;

    /**
     * Constructor
     */
    public NonPeriodFundEvent(NonPeriodFund idNonPeriodFund, String idCode) {
        this.idNonPeriodFund = idNonPeriodFund;
        this.idCode = idCode;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public String getId() {
        return idNonPeriodFund.toString() + " " + idCode;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public List<DataObject> getParents() {
        List<DataObject> toReturn = new ArrayList<>();
        toReturn.add(idNonPeriodFund);
        return toReturn;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public NonPeriodFund getIdNonPeriodFund() {
        return idNonPeriodFund;
    }

    public String getIdCode() {
        return idCode;
    }
}
