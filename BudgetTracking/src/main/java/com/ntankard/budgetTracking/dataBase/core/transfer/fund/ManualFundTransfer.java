package com.ntankard.budgetTracking.dataBase.core.transfer.fund;

import com.ntankard.budgetTracking.dataBase.core.Currency;
import com.ntankard.budgetTracking.dataBase.core.period.Period;
import com.ntankard.budgetTracking.dataBase.core.pool.Pool;
import com.ntankard.budgetTracking.dataBase.core.pool.fundEvent.FundEvent;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.Database;

import static com.ntankard.budgetTracking.dataBase.core.pool.fundEvent.FundEvent.FundEvent_Category;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.DataCore_Factory.createDirectDerivedDataCore;

public class ManualFundTransfer extends FundTransfer {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = FundTransfer.getDataObjectSchema();

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
        dataObjectSchema.<Pool>get(Transfer_Destination).setDataCore_schema(createDirectDerivedDataCore(Transfer_Source, FundEvent_Category));
        // SourceCurrencyGet
        // DestinationCurrencyGet
        // SourcePeriodGet
        // DestinationPeriodGet
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(ManualFundTransfer.class);
    }

    /**
     * Constructor
     */
    public ManualFundTransfer(Database database, Object... args) {
        super(database, args);
    }

    /**
     * Constructor
     */
    public ManualFundTransfer(String description, Period period, FundEvent source, Double value, Currency currency) {
        super(period.getTrackingDatabase()
                , Transfer_Description, description
                , Transfer_Period, period
                , Transfer_Source, source
                , Transfer_Value, value
                , Transfer_Currency, currency
        );
    }
}
