package com.ntankard.tracking.dataBase.core.pool;

import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.tracking.dataBase.core.baseObject.NamedDataObject;

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
