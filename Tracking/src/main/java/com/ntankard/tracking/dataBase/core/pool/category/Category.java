package com.ntankard.tracking.dataBase.core.pool.category;

import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.tracking.dataBase.core.pool.Pool;

public abstract class Category extends Pool {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = Pool.getDataObjectSchema();

        // ID
        // Name
        // Parents
        // Children

        return dataObjectSchema.endLayer(Category.class);
    }

    /**
     * Constructor
     */
    public Category(Database database) {
        super(database);
    }
}
