package com.ntankard.tracking.dataBase.core.transfer.fund.rePay;

import com.ntankard.javaObjectDatabase.database.Database_Schema;
import com.ntankard.tracking.dataBase.core.Currency;
import com.ntankard.tracking.dataBase.core.period.ExistingPeriod;
import com.ntankard.tracking.dataBase.core.period.Period;
import com.ntankard.tracking.dataBase.core.pool.fundEvent.FixedPeriodFundEvent;
import com.ntankard.tracking.dataBase.core.pool.fundEvent.FundEvent;
import com.ntankard.javaObjectDatabase.dataObject.factory.DoubleParentFactory;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.External_Source;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Local_Source;
import com.ntankard.javaObjectDatabase.database.ParameterMap;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.Database;

import static com.ntankard.tracking.dataBase.core.pool.fundEvent.FixedPeriodFundEvent.*;

@ParameterMap(shouldSave = false)
public class FixedPeriodRePayFundTransfer extends RePayFundTransfer {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static DoubleParentFactory<?, ?, ?> Factory = new DoubleParentFactory<>(
            FixedPeriodRePayFundTransfer.class,
            ExistingPeriod.class,
            Transfer_Period, FixedPeriodFundEvent.class,
            Transfer_Source, (generator, secondaryGenerator) -> FixedPeriodRePayFundTransfer.make(
            generator.getTrackingDatabase().getNextId(),
            generator,
            secondaryGenerator,
            generator.getTrackingDatabase().getDefault(Currency.class))
    );

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getFieldContainer() {
        DataObject_Schema dataObjectSchema = RePayFundTransfer.getFieldContainer();

        // Class behavior
        dataObjectSchema.setMyFactory(Factory);

        // ID
        // Description
        // Period
        // Source
        // Value =======================================================================================================
        dataObjectSchema.<Double>get(Transfer_Value).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>(
                        (Derived_DataCore.Calculator<Double, FixedPeriodRePayFundTransfer>) container -> {
                            FixedPeriodFundEvent fixedPeriodFundEvent = (FixedPeriodFundEvent) container.getSource();
                            if (!container.getPeriod().isWithin(fixedPeriodFundEvent.getStart(), fixedPeriodFundEvent.getDuration())) {
                                return -0.0;
                            }
                            return fixedPeriodFundEvent.getRepayAmount();
                        }
                        , new Local_Source.LocalSource_Factory<>((Transfer_Period))
                        , new External_Source.ExternalSource_Factory<>((Transfer_Source), FixedPeriodFundEvent_Start)
                        , new External_Source.ExternalSource_Factory<>((Transfer_Source), FixedPeriodFundEvent_Duration)
                        , new External_Source.ExternalSource_Factory<>((Transfer_Source), FixedPeriodFundEvent_RepayAmount)));
        // =============================================================================================================
        // Currency
        // Destination
        // SourceCurrencyGet
        // DestinationCurrencyGet
        // SourcePeriodGet
        // DestinationPeriodGet
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(FixedPeriodRePayFundTransfer.class);
    }

    /**
     * Create a new RePayFundTransfer object
     */
    public static FixedPeriodRePayFundTransfer make(Integer id, Period period, FundEvent source, Currency currency) {
        Database database = period.getTrackingDatabase();
        Database_Schema database_schema = database.getSchema();
        return assembleDataObject(database, database_schema.getClassSchema(FixedPeriodRePayFundTransfer.class), new FixedPeriodRePayFundTransfer()
                , DataObject_Id, id
                , Transfer_Period, period
                , Transfer_Source, source
                , Transfer_Currency, currency
        );
    }
}
