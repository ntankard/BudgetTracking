package com.ntankard.Tracking.DataBase.Core;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.MemberProperties;

@ClassExtensionProperties(includeParent = true)
public abstract class IdDataObject extends DataObject {

    // My values
    private String id;

    /**
     * Constructor
     */
    public IdDataObject(String id) {
        this.id = id;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
    public String getId() {
        return id;
    }
}
