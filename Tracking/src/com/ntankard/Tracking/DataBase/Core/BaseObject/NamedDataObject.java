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

    @DisplayProperties(order = 2)
    public String getName() {
        return name;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public void setName(String name) {
        this.name = name;
    }
}
