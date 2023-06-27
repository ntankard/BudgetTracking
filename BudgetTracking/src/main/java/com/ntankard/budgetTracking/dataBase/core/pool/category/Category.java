package com.ntankard.budgetTracking.dataBase.core.pool.category;

import com.ntankard.budgetTracking.dataBase.core.pool.Pool;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.Database;

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
    public Category(Database database, Object... args) {
        super(database, args);
    }
}
