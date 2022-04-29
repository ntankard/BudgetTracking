package com.ntankard.budgetTracking.dataBase.core.pool;

import com.ntankard.budgetTracking.dataBase.core.baseObject.NamedDataObject;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.Database;

public abstract class Pool extends NamedDataObject {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = NamedDataObject.getDataObjectSchema();

        // ID
        // Name
        // Parents
        // Children

        return dataObjectSchema.endLayer(Pool.class);
    }

    /**
     * Constructor
     */
    public Pool(Database database, Object... args) {
        super(database, args);
    }
}
