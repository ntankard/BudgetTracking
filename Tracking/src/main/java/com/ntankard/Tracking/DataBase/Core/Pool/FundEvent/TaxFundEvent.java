package com.ntankard.Tracking.DataBase.Core.Pool.FundEvent;

import com.ntankard.Tracking.DataBase.Core.Transfer.Fund.RePay.TaxRePayFundTransfer;
import com.ntankard.javaObjectDatabase.CoreObject.Field.DataField;
import com.ntankard.javaObjectDatabase.CoreObject.FieldContainer;

public class TaxFundEvent extends FundEvent {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String TaxFundEvent_Percentage = "getPercentage";

    /**
     * Get all the fields for this object
     */
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = FundEvent.getFieldContainer();

        // Class behavior
        fieldContainer.addObjectFactory(TaxRePayFundTransfer.Factory);

        // ID
        // Name
        // Category
        // Percentage ==================================================================================================
        fieldContainer.add(new DataField<>(TaxFundEvent_Percentage, Double.class));
        //==============================================================================================================
        // Parents
        // Children

        return fieldContainer.finaliseContainer(TaxFundEvent.class);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public Double getPercentage() {
        return get(TaxFundEvent_Percentage);
    }
}
