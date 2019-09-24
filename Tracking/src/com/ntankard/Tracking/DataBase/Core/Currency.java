package com.ntankard.Tracking.DataBase.Core;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.Base.DataObject;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class Currency extends DataObject {

    // My parents

    // My values
    private String id;
    private double toSecondary;
    private double toPrimary;
    private boolean isPrimary;

    /**
     * Constructor
     */
    public Currency(String id, double toSecondary, double toPrimary, boolean isPrimary) {
        this.id = id;
        this.toSecondary = toSecondary;
        this.toPrimary = toPrimary;
        this.isPrimary = isPrimary;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public List<DataObject> getParents() {
        return new ArrayList<>();
    }

    /**
     * {@inheritDoc
     */
    @Override
    public String getId() {
        return id;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public double getToSecondary() {
        return toSecondary;
    }

    public double getToPrimary() {
        return toPrimary;
    }

    public boolean isPrimary() {
        return isPrimary;
    }
}
