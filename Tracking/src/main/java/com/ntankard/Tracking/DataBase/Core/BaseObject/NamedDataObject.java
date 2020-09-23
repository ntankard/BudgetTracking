package com.ntankard.Tracking.DataBase.Core.BaseObject;

import com.ntankard.javaObjectDatabase.CoreObject.DataObject;
import com.ntankard.javaObjectDatabase.CoreObject.Field.DataField;
import com.ntankard.javaObjectDatabase.CoreObject.FieldContainer;

public abstract class NamedDataObject extends DataObject {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String NamedDataObject_Name = "getName";

    /**
     * Get all the fields for this object
     */
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = DataObject.getFieldContainer();

        // ID
        // Name ========================================================================================================
        fieldContainer.add(new DataField<>(NamedDataObject_Name, String.class));
        fieldContainer.get(NamedDataObject_Name).setCanEdit(true);
        //==============================================================================================================
        // Parents
        // Children

        return fieldContainer.endLayer(NamedDataObject.class);
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

    public String getName() {
        return get(NamedDataObject_Name);
    }
}
