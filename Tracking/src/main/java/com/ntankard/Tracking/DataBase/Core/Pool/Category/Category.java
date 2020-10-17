package com.ntankard.Tracking.DataBase.Core.Pool.Category;

import com.ntankard.javaObjectDatabase.CoreObject.DataObject_Schema;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;

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
