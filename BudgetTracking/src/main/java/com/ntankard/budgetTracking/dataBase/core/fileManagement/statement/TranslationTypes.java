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
    public TranslationTypes(Database database) {
        super(database);
    }

    /**
     * Constructor
     */
    public TranslationTypes(Database database, String name) {
        this(database);
        setAllValues(DataObject_Id, getTrackingDatabase().getNextId()
                , NamedDataObject_Name, name
        );
    }
}
