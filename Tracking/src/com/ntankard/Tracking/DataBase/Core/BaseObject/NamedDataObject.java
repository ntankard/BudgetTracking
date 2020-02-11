package com.ntankard.Tracking.DataBase.Core.BaseObject;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

@ClassExtensionProperties(includeParent = true)
public abstract class NamedDataObject extends DataObject {

    // My values
    private String name;

    /**
     * Constructor
     */
    @ParameterMap(shouldSave = false)
    public NamedDataObject(Integer id, String name) {
        super(id);
        if (name == null) throw new IllegalArgumentException("Name is null");
        this.name = name;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public String toString() {
        return getName();
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    // 1000000--getID

    @DisplayProperties(order = 1100000)
    public String getName() {
        return name;
    }

    // 2000000--getParents
    // 3000000--getChildren

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public void setName(String name) {
        if (name == null) throw new IllegalArgumentException("ID can not be null");
        this.name = name;
    }
}
