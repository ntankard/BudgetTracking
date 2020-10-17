package com.ntankard.Tracking.DataBase.Core.Pool.FundEvent;

import com.ntankard.Tracking.DataBase.Core.Pool.Category.SolidCategory;
import com.ntankard.Tracking.DataBase.Core.Transfer.Fund.RePay.SavingsRePayFundTransfer;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.Static_DataCore;
import com.ntankard.javaObjectDatabase.CoreObject.FieldContainer;

public class SavingsFundEvent extends FundEvent {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = FundEvent.getFieldContainer();

        // Class behavior
        fieldContainer.addObjectFactory(SavingsRePayFundTransfer.Factory);

        // ID
        // Name ========================================================================================================
        fieldContainer.get(NamedDataObject_Name).setManualCanEdit(false);
        fieldContainer.get(NamedDataObject_Name).setDataCore_factory(new Static_DataCore.Static_DataCore_Factory<>("Savings"));
        // =============================================================================================================
        // Category
        // Parents
        // Children

        return fieldContainer.finaliseContainer(SavingsFundEvent.class);
    }

    /**
     * Create a new SavingsFundEvent object
     */
    public static SavingsFundEvent make(Integer id, SolidCategory solidCategory) {
        return assembleDataObject(SavingsFundEvent.getFieldContainer(), new SavingsFundEvent()
                , DataObject_Id, id
                , FundEvent_Category, solidCategory
        );
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @Override
    public String getName() {
        return get(NamedDataObject_Name);
    }
}
