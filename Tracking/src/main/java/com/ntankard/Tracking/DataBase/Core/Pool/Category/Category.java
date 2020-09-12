package com.ntankard.Tracking.DataBase.Core.Pool.Category;

import com.ntankard.javaObjectDatabase.CoreObject.FieldContainer;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;

public abstract class Category extends Pool {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = Pool.getFieldContainer();

        // ID
        // Name
        // Parents
        // Children

        return fieldContainer.endLayer(Category.class);
    }
}
