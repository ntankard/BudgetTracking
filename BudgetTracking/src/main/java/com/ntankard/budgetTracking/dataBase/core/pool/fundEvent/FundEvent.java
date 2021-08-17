package com.ntankard.budgetTracking.dataBase.core.pool.fundEvent;

import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.budgetTracking.dataBase.core.pool.category.SolidCategory;
import com.ntankard.budgetTracking.dataBase.core.pool.Pool;
import com.ntankard.budgetTracking.dataBase.interfaces.summary.pool.FundEvent_Summary;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;

public abstract class FundEvent extends Pool {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String FundEvent_Category = "getCategory";
    public static final String FundEvent_IsDone = "getIsDone";

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = Pool.getDataObjectSchema();

        // Class behavior
        dataObjectSchema.addObjectFactory(FundEvent_Summary.Factory);

        // ID
        // Name
        // Category ====================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(FundEvent_Category, SolidCategory.class));
        // IsDone ====================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(FundEvent_IsDone, Boolean.class));
        dataObjectSchema.get(FundEvent_IsDone).setManualCanEdit(true);
        //==============================================================================================================
        // Parents
        // Children

        return dataObjectSchema.endLayer(FundEvent.class);
    }

    /**
     * Constructor
     */
    public FundEvent(Database database) {
        super(database);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public SolidCategory getCategory() {
        return get(FundEvent_Category);
    }

    public Boolean getIsDone() {
        return get(FundEvent_IsDone);
    }
}
