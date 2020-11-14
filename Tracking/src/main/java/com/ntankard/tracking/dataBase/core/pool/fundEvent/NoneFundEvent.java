package com.ntankard.tracking.dataBase.core.pool.fundEvent;

import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.javaObjectDatabase.database.Database_Schema;
import com.ntankard.tracking.dataBase.core.pool.category.SolidCategory;
import com.ntankard.javaObjectDatabase.coreObject.DataObject_Schema;

public class NoneFundEvent extends FundEvent {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getFieldContainer() {
        DataObject_Schema dataObjectSchema = FundEvent.getFieldContainer();

        // ID
        // Name
        // Category
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(NoneFundEvent.class);
    }

    /**
     * Create a new SavingsFundEvent object
     */
    public static NoneFundEvent make(Integer id, String name, SolidCategory solidCategory) {
        Database database = solidCategory.getTrackingDatabase();
        Database_Schema database_schema = database.getSchema();
        return assembleDataObject(database, database_schema.getClassSchema(NoneFundEvent.class), new NoneFundEvent()
                , DataObject_Id, id
                , NamedDataObject_Name, name
                , FundEvent_Category, solidCategory
        );
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void remove() {
        super.remove_impl();
    }
}
