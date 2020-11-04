package com.ntankard.Tracking.DataBase.Core.Transfer.Bank;

import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.javaObjectDatabase.CoreObject.Factory.DoubleParentFactory;
import com.ntankard.javaObjectDatabase.CoreObject.Field.DataField_Schema;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Core.RecurringPayment.FixedRecurringPayment;
import com.ntankard.javaObjectDatabase.CoreObject.Field.Filter.Dependant_FieldFilter;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.Derived_DataCore;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.source.DirectExternalSource;
import com.ntankard.javaObjectDatabase.CoreObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.Database.TrackingDatabase;

import static com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.FixedPeriodFundEvent.NamedDataObject_Name;
import static com.ntankard.javaObjectDatabase.CoreObject.Factory.ObjectFactory.GeneratorMode.MULTIPLE_NO_ADD;
import static com.ntankard.javaObjectDatabase.CoreObject.Field.Properties.Display_Properties.DataType.CURRENCY;

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
                return RecurringBankTransfer.make(
                        TrackingDatabase.get().getNextId(),
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
    public static DataObject_Schema getFieldContainer() {
        DataObject_Schema dataObjectSchema = BankTransfer.getFieldContainer();

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
        dataObjectSchema.<Currency>get(Transfer_DestinationCurrencyGet).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>(
                        (Derived_DataCore.Calculator<Currency, BankTransfer>)
                                container -> container.getCurrency()
                        , new com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.source.LocalSource.LocalSource_Factory<>(Transfer_Currency)));
        // SourcePeriodGet
        // DestinationPeriodGet ========================================================================================
        dataObjectSchema.<Period>get(Transfer_DestinationPeriodGet).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>(
                        (Derived_DataCore.Calculator<Period, BankTransfer>) container ->
                                container.getPeriod()
                        , new com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.source.LocalSource.LocalSource_Factory<>(Transfer_Period)));
        // ParentPayment ===============================================================================================
        dataObjectSchema.add(new DataField_Schema<>(RecurringBankTransfer_ParentPayment, FixedRecurringPayment.class));
        //==============================================================================================================
        // Parents
        // Children

        // Description =================================================================================================
        dataObjectSchema.<String>get(Transfer_Description).setManualCanEdit(false);
        dataObjectSchema.<String>get(Transfer_Description).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>(
                        new DirectExternalSource.DirectExternalSource_Factory<>((RecurringBankTransfer_ParentPayment), NamedDataObject_Name)));
        //==============================================================================================================

        return dataObjectSchema.finaliseContainer(RecurringBankTransfer.class);
    }

    /**
     * Create a new RePayFundTransfer object
     */
    public static RecurringBankTransfer make(Integer id,
                                             Period period, Bank source, Double value,
                                             Period destinationPeriod, Pool destination,
                                             FixedRecurringPayment parentPayment) {
        return assembleDataObject(RecurringBankTransfer.getFieldContainer(), new RecurringBankTransfer()
                , DataObject_Id, id
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
