package com.ntankard.tracking.dataBase.core.pool.fundEvent;

import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.javaObjectDatabase.database.Database_Schema;
import com.ntankard.tracking.dataBase.core.pool.category.SolidCategory;
import com.ntankard.tracking.dataBase.core.transfer.fund.rePay.SavingsRePayFundTransfer;
import com.ntankard.javaObjectDatabase.coreObject.field.dataCore.Static_DataCore;
import com.ntankard.javaObjectDatabase.coreObject.DataObject_Schema;

public class SavingsFundEvent extends FundEvent {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getFieldContainer() {
        DataObject_Schema dataObjectSchema = FundEvent.getFieldContainer();

        // Class behavior
        dataObjectSchema.addObjectFactory(SavingsRePayFundTransfer.Factory);

        // ID
        // Name ========================================================================================================
        dataObjectSchema.get(NamedDataObject_Name).setManualCanEdit(false);
        dataObjectSchema.get(NamedDataObject_Name).setDataCore_factory(new Static_DataCore.Static_DataCore_Factory<>("Savings"));
        // =============================================================================================================
        // Category
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(SavingsFundEvent.class);
    }

    /**
     * Create a new SavingsFundEvent object
     */
    public static SavingsFundEvent make(Integer id, SolidCategory solidCategory) {
        Database database = solidCategory.getTrackingDatabase();
        Database_Schema database_schema = database.getSchema();
        return assembleDataObject(database, database_schema.getClassSchema(SavingsFundEvent.class), new SavingsFundEvent()
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
