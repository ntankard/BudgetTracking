package com.ntankard.tracking.dataBase.core.pool.fundEvent;

import com.ntankard.tracking.dataBase.core.pool.category.SolidCategory;
import com.ntankard.tracking.dataBase.core.pool.Pool;
import com.ntankard.tracking.dataBase.interfaces.summary.pool.FundEvent_Summary;
import com.ntankard.javaObjectDatabase.coreObject.field.DataField_Schema;
import com.ntankard.javaObjectDatabase.coreObject.DataObject_Schema;

public abstract class FundEvent extends Pool {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String FundEvent_Category = "getCategory";

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getFieldContainer() {
        DataObject_Schema dataObjectSchema = Pool.getFieldContainer();

        // Class behavior
        dataObjectSchema.addObjectFactory(FundEvent_Summary.Factory);

        // ID
        // Name
        // Category ====================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(FundEvent_Category, SolidCategory.class));
        //==============================================================================================================
        // Parents
        // Children

        return dataObjectSchema.endLayer(FundEvent.class);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public SolidCategory getCategory() {
        return get(FundEvent_Category);
    }
}
