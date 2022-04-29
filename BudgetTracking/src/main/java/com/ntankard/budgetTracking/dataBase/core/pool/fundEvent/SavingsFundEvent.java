package com.ntankard.budgetTracking.dataBase.core.pool.fundEvent;

import com.ntankard.budgetTracking.dataBase.core.pool.category.SolidCategory;
import com.ntankard.budgetTracking.dataBase.core.transfer.fund.rePay.SavingsRePayFundTransfer;
import com.ntankard.javaObjectDatabase.dataField.dataCore.Static_DataCore_Schema;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.Database;

public class SavingsFundEvent extends FundEvent {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = FundEvent.getDataObjectSchema();

        // Class behavior
        dataObjectSchema.addObjectFactory(SavingsRePayFundTransfer.Factory);

        // ID
        // Name ========================================================================================================
        dataObjectSchema.get(NamedDataObject_Name).setManualCanEdit(false);
        dataObjectSchema.get(NamedDataObject_Name).setDataCore_schema(new Static_DataCore_Schema<>("Savings"));
        // =============================================================================================================
        // Category
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(SavingsFundEvent.class);
    }

    /**
     * Constructor
     */
    public SavingsFundEvent(Database database, Object... args) {
        super(database, args);
    }

    /**
     * Constructor
     */
    public SavingsFundEvent(SolidCategory solidCategory, Boolean isDone) {
        super(solidCategory.getTrackingDatabase()
                , FundEvent_Category, solidCategory
                , FundEvent_IsDone, isDone
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
