package com.ntankard.budgetTracking.dataBase.core.fileManagement.statement;

import com.ntankard.budgetTracking.dataBase.core.baseObject.NamedDataObject;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.Database;

public class TranslationTypes extends NamedDataObject {


    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = NamedDataObject.getDataObjectSchema();

        // ID
        // Name
        // Children

        return dataObjectSchema.finaliseContainer(TranslationTypes.class);
    }

    /**
     * Constructor
     */
    public TranslationTypes(Database database, Object... args) {
        super(database, args);
    }

    /**
     * Constructor
     */
    public TranslationTypes(Database database, String name) {
        super(database
                , NamedDataObject_Name, name
        );
    }
}
