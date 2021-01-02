package com.ntankard.tracking.dataBase.core.transfer.fund;

import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Source_Factory;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.tracking.dataBase.core.Currency;
import com.ntankard.tracking.dataBase.core.period.Period;
import com.ntankard.tracking.dataBase.core.pool.Pool;
import com.ntankard.tracking.dataBase.core.pool.fundEvent.FundEvent;

import static com.ntankard.tracking.dataBase.core.pool.fundEvent.FundEvent.FundEvent_Category;

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
        dataObjectSchema.<Pool>get(Transfer_Destination).setDataCore_schema(
                new Derived_DataCore_Schema<Pool, ManualFundTransfer>
                        (dataObject -> ((FundEvent)dataObject.getSource()).getCategory()
                                , Source_Factory.makeSourceChain(Transfer_Source, FundEvent_Category)));
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
    public ManualFundTransfer(Database database) {
        super(database);
    }

    /**
     * Constructor
     */
    public ManualFundTransfer(String description, Period period, FundEvent source, Double value, Currency currency) {
        this(period.getTrackingDatabase());
        setAllValues(DataObject_Id, getTrackingDatabase().getNextId()
                , Transfer_Description, description
                , Transfer_Period, period
                , Transfer_Source, source
                , Transfer_Value, value
                , Transfer_Currency, currency
        );
    }
}
