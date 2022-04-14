package com.ntankard.budgetTracking.dataBase.core.transfer.bank;

import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema.Calculator;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.end.End_Source_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Source_Factory;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.budgetTracking.dataBase.core.Currency;
import com.ntankard.budgetTracking.dataBase.core.period.ExistingPeriod;
import com.ntankard.javaObjectDatabase.dataObject.factory.DoubleParentFactory;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.budgetTracking.dataBase.core.period.Period;
import com.ntankard.budgetTracking.dataBase.core.pool.Bank;
import com.ntankard.budgetTracking.dataBase.core.pool.Pool;
import com.ntankard.budgetTracking.dataBase.core.recurringPayment.FixedRecurringPayment;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.Database;

import java.util.List;

import static com.ntankard.budgetTracking.dataBase.core.pool.fundEvent.FixedPeriodFundEvent.NamedDataObject_Name;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.DataCore_Factory.createDirectDerivedDataCore;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Source_Factory.makeSourceChain;
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
                    if (secondaryGenerator.getStart().getOrder() <= generator.getOrder() && (secondaryGenerator.getStart().getOrder() + secondaryGenerator.getDuration()) >= generator.getOrder()) {
                        value = secondaryGenerator.getValue(); // TODO this is not working, seems like its inverted? Fixed it but need to test
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
        dataObjectSchema.<Currency>get(Transfer_DestinationCurrencyGet).setDataCore_schema(createDirectDerivedDataCore(Transfer_Currency));
        // SourcePeriodGet
        // DestinationPeriodGet ========================================================================================
        dataObjectSchema.<Period>get(Transfer_DestinationPeriodGet).setDataCore_schema(createDirectDerivedDataCore(Transfer_Period));
        // ParentPayment ===============================================================================================
        dataObjectSchema.add(new DataField_Schema<>(RecurringBankTransfer_ParentPayment, FixedRecurringPayment.class));
        //==============================================================================================================
        // Parents
        // Children

        // Description =================================================================================================
        dataObjectSchema.<String>get(Transfer_Description).setManualCanEdit(false);
        dataObjectSchema.<String>get(Transfer_Description).setDataCore_schema(createDirectDerivedDataCore(RecurringBankTransfer_ParentPayment, NamedDataObject_Name));
        //==============================================================================================================

        return dataObjectSchema.finaliseContainer(RecurringBankTransfer.class);
    }

    /**
     * @inheritDoc
     */
    @Override // TODO add filter to match this
    public <T extends DataObject> List<T> sourceOptions(Class<T> type, String fieldName) {
        switch (fieldName) {
            case "Destination":
            case "Bank": {
                List<T> toReturn = super.sourceOptions(type, fieldName);
                toReturn.removeIf(t -> {
                    if (Bank.class.isAssignableFrom(t.getClass())) {
                        Bank bank = (Bank) t;
                        return !((Bank) getSource()).getCurrency().equals(bank.getCurrency());
                    }
                    return false;
                });
                return toReturn;
            }
        }
        return super.sourceOptions(type, fieldName);
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
