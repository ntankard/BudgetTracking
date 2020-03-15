package com.ntankard.Tracking.DataBase.Core.BaseObject;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Field;

import java.util.List;

@ClassExtensionProperties(includeParent = true)
public abstract class NamedDataObject extends DataObject {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static List<Field<?>> getFields(Integer id, String name, DataObject container) {
        List<Field<?>> toReturn = DataObject.getFields(id, container);
        toReturn.add(new Field<>("name", String.class, name, container));
        return toReturn;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### General #####################################################
    //------------------------------------------------------------------------------------------------------------------

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
        return get("name");
    }

    // 2000000--getParents
    // 3000000--getChildren

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public void setName(String name) {
        set("name", name);
    }
}
