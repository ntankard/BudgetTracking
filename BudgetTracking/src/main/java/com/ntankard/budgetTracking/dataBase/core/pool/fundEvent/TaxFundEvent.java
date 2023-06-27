package com.ntankard.budgetTracking.dataBase.core.pool.fundEvent;

import com.ntankard.budgetTracking.dataBase.core.period.ExistingPeriod;
import com.ntankard.budgetTracking.dataBase.core.transfer.fund.rePay.TaxRePayFundTransfer;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.validator.NumberRange_FieldValidator;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.Database;

public class TaxFundEvent extends FundEvent {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String TaxFundEvent_Start = "getStart";
    public static final String TaxFundEvent_Duration = "getDuration";
    public static final String TaxFundEvent_Percentage = "getPercentage";

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = FundEvent.getDataObjectSchema();

        // Class behavior
        dataObjectSchema.addObjectFactory(TaxRePayFundTransfer.Factory);

        // ID
        // Name
        // Category
        // Start =======================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(TaxFundEvent_Start, ExistingPeriod.class));
        dataObjectSchema.get(TaxFundEvent_Start).setManualCanEdit(true);
        // Duration ====================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(TaxFundEvent_Duration, Integer.class));
        dataObjectSchema.<Integer>get(TaxFundEvent_Duration).addValidator(new NumberRange_FieldValidator<>(1, null));
        dataObjectSchema.get(TaxFundEvent_Duration).setManualCanEdit(true);
        // Percentage ==================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(TaxFundEvent_Percentage, Double.class));
        //==============================================================================================================
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(TaxFundEvent.class);
    }

    /**
     * Constructor
     */
    public TaxFundEvent(Database database, Object... args) {
        super(database, args);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public Double getPercentage() {
        return get(TaxFundEvent_Percentage);
    }

    public ExistingPeriod getStart() {
        return get(TaxFundEvent_Start);
    }

    public Integer getDuration() {
        return get(TaxFundEvent_Duration);
    }
}
