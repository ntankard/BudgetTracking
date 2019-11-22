package com.ntankard.Tracking.DataBase.Core.Pool.Fund;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.NamedDataObject;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.DEBUG_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class FundEvent extends NamedDataObject {

    // My parent
    private Fund fund;

    // My values
    private Boolean isFundDefault;

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getName", "getFund", "isFundDefault"})
    public FundEvent(Integer id, String name, Fund fund, Boolean isFundDefault) {
        super(id, name);
        this.fund = fund;
        this.isFundDefault = isFundDefault;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 21)
    public List<DataObject> getParents() {
        List<DataObject> toReturn = new ArrayList<>();
        toReturn.add(fund);
        return toReturn;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @DisplayProperties(order = 3)
    public Fund getFund() {
        return fund;
    }

    @DisplayProperties(order = 4)
    public Boolean isFundDefault() {
        return isFundDefault;
    }
}
