package com.ntankard.tracking.dataBase.core.pool.fundEvent;

import com.ntankard.tracking.dataBase.core.transfer.fund.rePay.TaxRePayFundTransfer;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;

public class TaxFundEvent extends FundEvent {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String TaxFundEvent_Percentage = "getPercentage";

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getFieldContainer() {
        DataObject_Schema dataObjectSchema = FundEvent.getFieldContainer();

        // Class behavior
        dataObjectSchema.addObjectFactory(TaxRePayFundTransfer.Factory);

        // ID
        // Name
        // Category
        // Percentage ==================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(TaxFundEvent_Percentage, Double.class));
        //==============================================================================================================
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(TaxFundEvent.class);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public Double getPercentage() {
        return get(TaxFundEvent_Percentage);
    }
}
