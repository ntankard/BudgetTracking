package com.ntankard.tracking.dataBase.core.transfer.bank;

import com.ntankard.javaObjectDatabase.database.TrackingDatabase;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase_Schema;
import com.ntankard.tracking.dataBase.core.Currency;
import com.ntankard.javaObjectDatabase.coreObject.DataObject;
import com.ntankard.javaObjectDatabase.coreObject.DataObject_Schema;
import com.ntankard.tracking.dataBase.core.period.Period;
import com.ntankard.tracking.dataBase.core.pool.Bank;
import com.ntankard.tracking.dataBase.core.pool.Pool;
import com.ntankard.javaObjectDatabase.coreObject.field.filter.FieldFilter;
import com.ntankard.javaObjectDatabase.coreObject.field.dataCore.derived.Derived_DataCore.Calculator;

public class ManualBankTransfer extends BankTransfer {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getFieldContainer() {
        DataObject_Schema dataObjectSchema = BankTransfer.getFieldContainer();

        // ID
        // Description
        // Period
        // Source
        // Value
        // Currency
        // DestinationPeriod
        // Category
        // Bank
        // FundEvent
        // Destination
        // SourceCurrencyGet
        // DestinationCurrencyGet ======================================================================================
        dataObjectSchema.<Currency>get(Transfer_DestinationCurrencyGet).setDataCore_factory(
                new com.ntankard.javaObjectDatabase.coreObject.field.dataCore.derived.Derived_DataCore.Derived_DataCore_Factory<>(
                        (Calculator<Currency, ManualBankTransfer>) container ->
                                container.getCurrency()
                        , new com.ntankard.javaObjectDatabase.coreObject.field.dataCore.derived.source.LocalSource.LocalSource_Factory<>((Transfer_Currency))));
        // SourcePeriodGet
        // DestinationPeriodGet ========================================================================================
        dataObjectSchema.<Period>get(Transfer_DestinationPeriodGet).setDataCore_factory(
                new com.ntankard.javaObjectDatabase.coreObject.field.dataCore.derived.Derived_DataCore.Derived_DataCore_Factory<>((Calculator<Period, BankTransfer>) container -> {
                    if (container.getDestinationPeriod() != null) {
                        return container.getDestinationPeriod();
                    } else {
                        return container.getPeriod();
                    }
                }
                        , new com.ntankard.javaObjectDatabase.coreObject.field.dataCore.derived.source.LocalSource.LocalSource_Factory<>((Transfer_Period))
                        , new com.ntankard.javaObjectDatabase.coreObject.field.dataCore.derived.source.LocalSource.LocalSource_Factory<>((BankTransfer_DestinationPeriod))));
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(ManualBankTransfer.class);
    }

    /**
     * Create a new RePayFundTransfer object
     */
    public static ManualBankTransfer make(Integer id, String description,
                                          Period period, Bank source, Double value,
                                          Period destinationPeriod, Pool destination) {
        TrackingDatabase trackingDatabase = period.getTrackingDatabase();
        TrackingDatabase_Schema trackingDatabase_schema = trackingDatabase.getSchema();
        return assembleDataObject(trackingDatabase, trackingDatabase_schema.getClassSchema(ManualBankTransfer.class), new ManualBankTransfer()
                , DataObject_Id, id
                , Transfer_Description, description
                , Transfer_Period, period
                , Transfer_Source, source
                , Transfer_Value, value
                , BankTransfer_DestinationPeriod, destinationPeriod
                , Transfer_Destination, destination
        );
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################# HalfTransfer Interface #############################################
    //------------------------------------------------------------------------------------------------------------------

    @Override
    protected Double getValue(boolean isSource) {
        if (isSource) {
            return -getValue();
        } else {
            return getValue();
        }
    }
}
