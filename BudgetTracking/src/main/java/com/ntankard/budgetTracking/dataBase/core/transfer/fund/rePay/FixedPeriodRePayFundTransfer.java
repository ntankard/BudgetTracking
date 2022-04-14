package com.ntankard.budgetTracking.dataBase.core.transfer.fund.rePay;

import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema.Calculator;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.end.End_Source_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Source_Factory;
import com.ntankard.budgetTracking.dataBase.core.Currency;
import com.ntankard.budgetTracking.dataBase.core.period.ExistingPeriod;
import com.ntankard.budgetTracking.dataBase.core.period.Period;
import com.ntankard.budgetTracking.dataBase.core.pool.fundEvent.FixedPeriodFundEvent;
import com.ntankard.budgetTracking.dataBase.core.pool.fundEvent.FundEvent;
import com.ntankard.javaObjectDatabase.dataObject.factory.DoubleParentFactory;
import com.ntankard.javaObjectDatabase.database.ParameterMap;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.Database;

import static com.ntankard.budgetTracking.dataBase.core.pool.fundEvent.FixedPeriodFundEvent.*;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Source_Factory.makeSharedStepSourceChain;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Source_Factory.makeSourceChain;

@ParameterMap(shouldSave = false)
public class FixedPeriodRePayFundTransfer extends RePayFundTransfer {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static DoubleParentFactory<?, ?, ?> Factory = new DoubleParentFactory<>(
            FixedPeriodRePayFundTransfer.class,
            ExistingPeriod.class,
            Transfer_Period, FixedPeriodFundEvent.class,
            Transfer_Source, (generator, secondaryGenerator) -> new FixedPeriodRePayFundTransfer(
            generator,
            secondaryGenerator,
            generator.getTrackingDatabase().getDefault(Currency.class))
    );

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = RePayFundTransfer.getDataObjectSchema();

        // Class behavior
        dataObjectSchema.setMyFactory(Factory);

        // ID
        // Description
        // Period
        // Source
        // Value =======================================================================================================
        dataObjectSchema.<Double>get(Transfer_Value).setDataCore_schema(
                new Derived_DataCore_Schema<>(
                        (Calculator<Double, FixedPeriodRePayFundTransfer>) container -> {
                            FixedPeriodFundEvent fixedPeriodFundEvent = (FixedPeriodFundEvent) container.getSource();
                            if (!container.getPeriod().isWithin(fixedPeriodFundEvent.getStart(), fixedPeriodFundEvent.getDuration())) {
                                return -0.0;
                            }
                            return fixedPeriodFundEvent.getRepayAmount();
                        }
                        , Source_Factory.append(makeSharedStepSourceChain(Transfer_Source, FixedPeriodFundEvent_Start, FixedPeriodFundEvent_Duration, FixedPeriodFundEvent_RepayAmount)
                        , makeSourceChain(Transfer_Period))));
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
     * Constructor
     */
    public FixedPeriodRePayFundTransfer(Database database) {
        super(database);
    }

    /**
     * Constructor
     */
    public FixedPeriodRePayFundTransfer(Period period, FundEvent source, Currency currency) {
        this(period.getTrackingDatabase());
        setAllValues(DataObject_Id, getTrackingDatabase().getNextId()
                , Transfer_Period, period
                , Transfer_Source, source
                , Transfer_Currency, currency
        );
    }
}
