package com.ntankard.tracking.dataBase.core.transfer.bank;

import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema.Calculator;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.end.End_Source_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Source_Factory;
import com.ntankard.tracking.dataBase.core.Currency;
import com.ntankard.tracking.dataBase.core.period.ExistingPeriod;
import com.ntankard.javaObjectDatabase.dataObject.factory.DoubleParentFactory;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.tracking.dataBase.core.period.Period;
import com.ntankard.tracking.dataBase.core.pool.Bank;
import com.ntankard.tracking.dataBase.core.pool.Pool;
import com.ntankard.tracking.dataBase.core.recurringPayment.FixedRecurringPayment;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.Database;

import static com.ntankard.tracking.dataBase.core.pool.fundEvent.FixedPeriodFundEvent.NamedDataObject_Name;
import static com.ntankard.javaObjectDatabase.dataObject.factory.ObjectFactory.GeneratorMode.MULTIPLE_NO_ADD;

public class RecurringBankTransfer extends BankTransfer {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String RecurringBankTransfer_ParentPayment = "getParentPayment";

    public static DoubleParentFactory<?, ?, ?> Factory = new DoubleParentFactory<>(
            RecurringBankTransfer.class,
            ExistingPeriod.class,
            Transfer_Period, FixedRecurringPayment.class,
            RecurringBankTransfer_ParentPayment,
            (generator, secondaryGenerator) -> {
                double value = 0.0;
                if (secondaryGenerator.getDuration() != null) {
                    if (secondaryGenerator.getStart().getOrder() + secondaryGenerator.getDuration() <= generator.getOrder()) {
                        value = secondaryGenerator.getValue();
                    }
                }
                return new RecurringBankTransfer(
                        generator,
                        secondaryGenerator.getBank(),
                        value,
                        null,
                        secondaryGenerator.getCategory(),
                        secondaryGenerator);
            },
            MULTIPLE_NO_ADD
    );

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = BankTransfer.getDataObjectSchema();

        // Class behavior
        dataObjectSchema.setMyFactory(Factory);

        // ID
        // Description (Below)
        // Period
        // Source ======================================================================================================
        dataObjectSchema.get(Transfer_Source).setManualCanEdit(true);
        //==============================================================================================================
        // Value
        // Currency
        // DestinationPeriod
        // Category
        // Bank
        // FundEvent
        // Destination
        // DestinationValue
        // SourceCurrencyGet
        // DestinationCurrencyGet ======================================================================================
        dataObjectSchema.<Currency>get(Transfer_DestinationCurrencyGet).setDataCore_schema(
                new Derived_DataCore_Schema<>(
                        (Calculator<Currency, BankTransfer>)
                                container -> container.getCurrency()
                        , new End_Source_Schema<>(Transfer_Currency)));
        // SourcePeriodGet
        // DestinationPeriodGet ========================================================================================
        dataObjectSchema.<Period>get(Transfer_DestinationPeriodGet).setDataCore_schema(
                new Derived_DataCore_Schema<>(
                        (Calculator<Period, BankTransfer>) container ->
                                container.getPeriod()
                        , new End_Source_Schema<>(Transfer_Period)));
        // ParentPayment ===============================================================================================
        dataObjectSchema.add(new DataField_Schema<>(RecurringBankTransfer_ParentPayment, FixedRecurringPayment.class));
        //==============================================================================================================
        // Parents
        // Children

        // Description =================================================================================================
        dataObjectSchema.<String>get(Transfer_Description).setManualCanEdit(false);
        dataObjectSchema.<String>get(Transfer_Description).setDataCore_schema(
                new Derived_DataCore_Schema<String, RecurringBankTransfer>
                        (dataObject -> dataObject.getParentPayment().getName()
                                , Source_Factory.makeSourceChain(RecurringBankTransfer_ParentPayment, NamedDataObject_Name)));
        //==============================================================================================================

        return dataObjectSchema.finaliseContainer(RecurringBankTransfer.class);
    }

    /**
     * Constructor
     */
    public RecurringBankTransfer(Database database) {
        super(database);
    }

    /**
     * Constructor
     */
    public RecurringBankTransfer(Period period, Bank source, Double value,
                                 Period destinationPeriod, Pool destination,
                                 FixedRecurringPayment parentPayment) {
        this(period.getTrackingDatabase());
        setAllValues(DataObject_Id, getTrackingDatabase().getNextId()
                , Transfer_Period, period
                , Transfer_Source, source
                , Transfer_Value, value
                , BankTransfer_DestinationPeriod, destinationPeriod
                , Transfer_Destination, destination
                , RecurringBankTransfer_ParentPayment, parentPayment
        );
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public FixedRecurringPayment getParentPayment() {
        return get(RecurringBankTransfer_ParentPayment);
    }
}
