package com.ntankard.Tracking.DataBase.Core.BaseObject.Interface;

import com.ntankard.ClassExtension.ClassExtensionProperties;

@ClassExtensionProperties(includeParent = true)
public interface HasDefault {

    /**
     * Is this the default object for this data object set
     *
     * @return True if this is this the default object for this data object set
     */
    Boolean isDefault();
}
