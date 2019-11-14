package com.ntankard.Tracking.DataBase.Core.Pool;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.NamedDataObject;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

@ClassExtensionProperties(includeParent = true)
public abstract class Pool extends NamedDataObject {

    /**
     * Constructor
     */
    @ParameterMap(shouldSave = false)
    public Pool(Integer id, String name) {
        super(id, name);
    }
}
