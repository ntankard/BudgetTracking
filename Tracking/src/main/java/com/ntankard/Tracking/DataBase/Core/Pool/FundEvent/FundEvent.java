package com.ntankard.Tracking.DataBase.Core.Pool.FundEvent;

import com.ntankard.Tracking.DataBase.Core.Pool.Category.SolidCategory;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.FundEvent_Summary;
import com.ntankard.javaObjectDatabase.CoreObject.Field.DataField;
import com.ntankard.javaObjectDatabase.CoreObject.FieldContainer;

public abstract class FundEvent extends Pool {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String FundEvent_Category = "getCategory";

    /**
     * Get all the fields for this object
     */
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = Pool.getFieldContainer();

        // Class behavior
        fieldContainer.addObjectFactory(FundEvent_Summary.Factory);

        // ID
        // Name
        // Category ====================================================================================================
        fieldContainer.add(new DataField<>(FundEvent_Category, SolidCategory.class));
        //==============================================================================================================
        // Parents
        // Children

        return fieldContainer.endLayer(FundEvent.class);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public SolidCategory getCategory() {
        return get(FundEvent_Category);
    }
}
