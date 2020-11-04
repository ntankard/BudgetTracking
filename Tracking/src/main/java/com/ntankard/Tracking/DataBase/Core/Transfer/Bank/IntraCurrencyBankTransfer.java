package com.ntankard.Tracking.DataBase.Core.Transfer.Bank;

import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.javaObjectDatabase.CoreObject.DataObject;
import com.ntankard.javaObjectDatabase.CoreObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.CoreObject.Field.DataField_Schema;
import com.ntankard.javaObjectDatabase.CoreObject.Field.Filter.Dependant_FieldFilter;
import com.ntankard.javaObjectDatabase.CoreObject.Field.Filter.FieldFilter;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.Derived_DataCore.Calculator;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.Derived_DataCore.Derived_DataCore_Factory;

import static com.ntankard.javaObjectDatabase.CoreObject.Field.Properties.Display_Properties.DataType.CURRENCY;

public class IntraCurrencyBankTransfer extends BankTransfer {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String BankTransfer_DestinationCurrency = "getDestinationCurrency";
    public static final String BankTransfer_DestinationValue = "getDestinationValue";

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getFieldContainer() {
        DataObject_Schema dataObjectSchema = BankTransfer.getFieldContainer();

        // ID
        // Description
        // Period ======================================================================================================
        dataObjectSchema.get(Transfer_Period).setManualCanEdit(true);
        dataObjectSchema.<Period>get(Transfer_Period).addFilter(new FieldFilter<Period, DataObject>() { // Here
            @Override
            public boolean isValid(Period newValue, Period pastValue, DataObject container) {
                BankTransfer bankTransfer = ((BankTransfer) container);
                if (bankTransfer.isAllValid()) {
                    return bankTransfer.getDestinationPeriod() == null || !bankTransfer.getDestinationPeriod().equals(newValue);
                }
                return true;
            }
        });
        // Source
        // Value
        // Currency
        // DestinationPeriod
        // Category
        // Bank
        // FundEvent
        // Destination
        // DestinationValue ============================================================================================
        dataObjectSchema.add(Transfer_Destination, new DataField_Schema<>(BankTransfer_DestinationValue, Double.class, false));
        dataObjectSchema.get(BankTransfer_DestinationValue).getDisplayProperties().setDataType(CURRENCY);
        dataObjectSchema.get(BankTransfer_DestinationValue).setManualCanEdit(true);
        // TODO probably best to replace this with a conversation step that takes 0 and makes it null so it dosnt fail
        dataObjectSchema.<Double>get(BankTransfer_DestinationValue).addFilter(new Dependant_FieldFilter<Double, BankTransfer>(Transfer_Destination, Transfer_Source) {
            @Override
            public boolean isValid(Double newValue, Double pastValue, BankTransfer bankTransfer) {
                return !newValue.equals(0.0);
            }
        });
        // DestinationCurrency =========================================================================================
        dataObjectSchema.add(new DataField_Schema<>(BankTransfer_DestinationCurrency, Currency.class, false));
        dataObjectSchema.<Currency>get(BankTransfer_DestinationCurrency).setDataCore_factory(
                new Derived_DataCore_Factory<>(container -> {
                    BankTransfer bankTransfer = ((BankTransfer) container);
                    return ((Bank) bankTransfer.getDestination()).getCurrency();
                }
                        , new com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.source.LocalSource.LocalSource_Factory<>((Transfer_Destination))));
        // SourceCurrencyGet
        // DestinationCurrencyGet ======================================================================================
        dataObjectSchema.<Currency>get(Transfer_DestinationCurrencyGet).setDataCore_factory(
                new Derived_DataCore_Factory<>((Calculator<Currency, IntraCurrencyBankTransfer>) container ->
                        container.getDestinationCurrency()
                        , new com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.source.LocalSource.LocalSource_Factory<>((BankTransfer_DestinationCurrency))));
        // SourcePeriodGet
        // DestinationPeriodGet ========================================================================================
        dataObjectSchema.<Period>get(Transfer_DestinationPeriodGet).setDataCore_factory(
                new Derived_DataCore_Factory<>((Calculator<Period, BankTransfer>) container -> {
                    if (container.getDestinationPeriod() != null) {
                        return container.getDestinationPeriod();
                    } else {
                        return container.getPeriod();
                    }
                }
                        , new com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.source.LocalSource.LocalSource_Factory<>((Transfer_Period))
                        , new com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.source.LocalSource.LocalSource_Factory<>((BankTransfer_DestinationPeriod))));
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(IntraCurrencyBankTransfer.class);
    }

    /**
     * Create a new RePayFundTransfer object
     */
    public static IntraCurrencyBankTransfer make(Integer id, String description,
                                                 Period period, Bank source, Double value,
                                                 Period destinationPeriod, Pool destination, Double destinationValue) {
        return assembleDataObject(IntraCurrencyBankTransfer.getFieldContainer(), new IntraCurrencyBankTransfer()
                , DataObject_Id, id
                , Transfer_Description, description
                , Transfer_Period, period
                , Transfer_Source, source
                , Transfer_Value, value
                , BankTransfer_DestinationPeriod, destinationPeriod
                , Transfer_Destination, destination
                , BankTransfer_DestinationValue, destinationValue
        );
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public Currency getDestinationCurrency() {
        return get(BankTransfer_DestinationCurrency);
    }

    public Double getDestinationValue() {
        return get(BankTransfer_DestinationValue);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public void setDestinationValue(Double destinationValue) {
        set(BankTransfer_DestinationValue, destinationValue);
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################# HalfTransfer Interface #############################################
    //------------------------------------------------------------------------------------------------------------------

    @Override
    protected Double getValue(boolean isSource) {
        if (isSource) {
            return -getValue();
        } else {
            if (getDestinationValue() != null) {
                return getDestinationValue();
            } else {
                return getValue();
            }
        }
    }
}
