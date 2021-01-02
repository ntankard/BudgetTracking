package com.ntankard.tracking.dataBase.core.pool.fundEvent;

import com.ntankard.javaObjectDatabase.dataField.dataCore.Static_DataCore_Schema;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.tracking.dataBase.core.pool.category.SolidCategory;
import com.ntankard.tracking.dataBase.core.transfer.fund.rePay.SavingsRePayFundTransfer;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;

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
    public SavingsFundEvent(Database database) {
        super(database);
    }

    /**
     * Constructor
     */
    public SavingsFundEvent(SolidCategory solidCategory) {
        this(solidCategory.getTrackingDatabase());
        setAllValues(DataObject_Id, getTrackingDatabase().getNextId()
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
