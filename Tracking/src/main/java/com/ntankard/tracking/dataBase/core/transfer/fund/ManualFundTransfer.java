package com.ntankard.tracking.dataBase.core.transfer.fund;

import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.DirectExternal_Source;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.javaObjectDatabase.database.Database_Schema;
import com.ntankard.tracking.dataBase.core.Currency;
import com.ntankard.tracking.dataBase.core.period.Period;
import com.ntankard.tracking.dataBase.core.pool.fundEvent.FundEvent;
import com.ntankard.tracking.dataBase.core.pool.Pool;

import static com.ntankard.tracking.dataBase.core.pool.fundEvent.FundEvent.FundEvent_Category;

public class ManualFundTransfer extends FundTransfer {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getFieldContainer() {
        DataObject_Schema dataObjectSchema = FundTransfer.getFieldContainer();

        // ID
        // Description
        // Period
        // Source ======================================================================================================
        dataObjectSchema.get(Transfer_Source).setManualCanEdit(true);
        // Value =======================================================================================================
        dataObjectSchema.get(Transfer_Value).setManualCanEdit(true);
        // Currency
        // Destination =================================================================================================
        // TODO this was failing when it was set on the RePay, this might be because they were being recreated or because there is a problem here, test
        dataObjectSchema.<Pool>get(Transfer_Destination).setDataCore_factory(new Derived_DataCore.Derived_DataCore_Factory<>(new DirectExternal_Source.DirectExternalSource_Factory<>((Transfer_Source), FundEvent_Category)));
        // SourceCurrencyGet
        // DestinationCurrencyGet
        // SourcePeriodGet
        // DestinationPeriodGet
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(ManualFundTransfer.class);
    }

    /**
     * Create a new RePayFundTransfer object
     */
    public static ManualFundTransfer make(Integer id, String description, Period period, FundEvent source, Double value, Currency currency) {
        Database database = period.getTrackingDatabase();
        Database_Schema database_schema = database.getSchema();
        return assembleDataObject(database, database_schema.getClassSchema(ManualFundTransfer.class), new ManualFundTransfer()
                , DataObject_Id, id
                , Transfer_Description, description
                , Transfer_Period, period
                , Transfer_Source, source
                , Transfer_Value, value
                , Transfer_Currency, currency
        );
    }
}
