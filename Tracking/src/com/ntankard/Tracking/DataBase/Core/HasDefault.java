package com.ntankard.Tracking.DataBase.Core;

import com.ntankard.ClassExtension.ClassExtensionProperties;

@ClassExtensionProperties(includeParent = true)
public interface HasDefault {

    /**
     * Is this the default object for this data object set
     *
     * @return True if this is this the default object for this data object set
     */
    boolean isDefault();
}
