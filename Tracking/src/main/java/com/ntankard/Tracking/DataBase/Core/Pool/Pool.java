package com.ntankard.Tracking.DataBase.Core.Pool;

import com.ntankard.javaObjectDatabase.CoreObject.DataObject_Schema;
import com.ntankard.Tracking.DataBase.Core.BaseObject.NamedDataObject;

public abstract class Pool extends NamedDataObject {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getFieldContainer() {
        DataObject_Schema dataObjectSchema = NamedDataObject.getFieldContainer();

        // ID
        // Name
        // Parents
        // Children

        return dataObjectSchema.endLayer(Pool.class);
    }
}
