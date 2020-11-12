package com.ntankard.tracking.dataBase.core.pool.category;

import com.ntankard.javaObjectDatabase.coreObject.DataObject_Schema;
import com.ntankard.tracking.dataBase.core.pool.Pool;

public abstract class Category extends Pool {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getFieldContainer() {
        DataObject_Schema dataObjectSchema = Pool.getFieldContainer();

        // ID
        // Name
        // Parents
        // Children

        return dataObjectSchema.endLayer(Category.class);
    }
}
