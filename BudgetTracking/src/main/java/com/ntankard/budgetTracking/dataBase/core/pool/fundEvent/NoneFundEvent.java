package com.ntankard.budgetTracking.dataBase.core.pool.fundEvent;

import com.ntankard.budgetTracking.dataBase.core.pool.category.SolidCategory;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.Database;

public class NoneFundEvent extends FundEvent {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = FundEvent.getDataObjectSchema();

        // ID
        // Name
        // Category
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(NoneFundEvent.class);
    }

    /**
     * Constructor
     */
    public NoneFundEvent(Database database, Object... args) {
        super(database, args);
    }

    /**
     * Constructor
     */
    public NoneFundEvent(String name, SolidCategory solidCategory, Boolean isDone) {
        super(solidCategory.getTrackingDatabase()
                , NamedDataObject_Name, name
                , FundEvent_Category, solidCategory
                , FundEvent_IsDone, isDone
        );
    }

    /**
     * @inheritDoc
     */
    @Override
    public void remove() {
        super.remove_impl();
    }
}
