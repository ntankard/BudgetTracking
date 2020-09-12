package com.ntankard.Tracking.DataBase.Core.Transfer.Bank;

import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.Category;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.SolidCategory;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Core.Transfer.Transfer;
import com.ntankard.javaObjectDatabase.CoreObject.Field.DataCore.Derived_DataCore;
import com.ntankard.javaObjectDatabase.CoreObject.Field.DataCore.ValueRead_DataCore;
import com.ntankard.javaObjectDatabase.CoreObject.Field.DataField;
import com.ntankard.javaObjectDatabase.CoreObject.Field.Filter.Dependant_FieldFilter;
import com.ntankard.javaObjectDatabase.CoreObject.Field.Filter.FieldFilter;
import com.ntankard.javaObjectDatabase.CoreObject.Field.Listener.FieldChangeListener;
import com.ntankard.javaObjectDatabase.CoreObject.Field.Listener.Marked_FieldChangeListener;
import com.ntankard.javaObjectDatabase.CoreObject.FieldContainer;
import com.ntankard.javaObjectDatabase.CoreObject.DataObject;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank_Currency;
import static com.ntankard.javaObjectDatabase.CoreObject.Field.Properties.Display_Properties.DataType.CURRENCY;

public abstract class BankTransfer extends Transfer {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String BankTransfer_DestinationPeriod = "getDestinationPeriod";
    public static final String BankTransfer_Category = "getCategory";
    public static final String BankTransfer_Bank = "getBank";
    public static final String BankTransfer_FundEvent = "getFundEvent";
    public static final String BankTransfer_DestinationValue = "getDestinationValue";
    public static final String BankTransfer_DestinationCurrency = "getDestinationCurrency";

    public static final String Transfer_Destination_ListenerMark = "getDestinationListenerMark";
    public static final String Transfer_Source_ListenerMark = "getSourceListenerMark";

    /**
     * Get all the fields for this object
     */
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = Transfer.getFieldContainer();

        // ID
        // Description
        // Period ======================================================================================================
        fieldContainer.<Period>get(Transfer_Period).setDataCore(new ValueRead_DataCore<>(true));
        fieldContainer.<Period>get(Transfer_Period).addFilter(new FieldFilter<Period, DataObject>() {
            @Override
            public boolean isValid(Period value, DataObject container) {
                BankTransfer bankTransfer = ((BankTransfer) container);
                return bankTransfer.getDestinationPeriod() == null || !bankTransfer.getDestinationPeriod().equals(value);
            }
        });
        // Source ======================================================================================================
        fieldContainer.add(Transfer_Period, new DataField<>(Transfer_Source, Bank.class));
        fieldContainer.get(Transfer_Source).addChangeListener(new Marked_FieldChangeListener<Object>(Transfer_Source_ListenerMark) {
            @Override
            public void valueChanged(DataField<Object> field, Object oldValue, Object newValue) {
                // TODO Hidden error here, basically when this object setups up it triggers the change listener but when an object is being build the fields have not been added yet. This check patches the problem but may be a larger issue
                if (field.getState().equals(DataField.NewFieldState.N_ACTIVE)) {
                    BankTransfer thisTransfer = (BankTransfer) field.getContainer();
                    if (!(thisTransfer.getDestination() instanceof Bank && thisTransfer.getDestinationCurrency() != null)) {

                        thisTransfer.setDestinationValue(null);
                    }
                    thisTransfer.validateParents();
                }
            }
        });
        // Value =======================================================================================================
        fieldContainer.get(Transfer_Value).setDataCore(new ValueRead_DataCore<>(true));
        // Currency ====================================================================================================
        fieldContainer.<Currency>get(Transfer_Currency).setDataCore(new Derived_DataCore<>(new Derived_DataCore.DirectExternalSource<>(fieldContainer.get(Transfer_Source), Bank_Currency)));
        // DestinationPeriod ===========================================================================================
        fieldContainer.add(Transfer_Currency, new DataField<>(BankTransfer_DestinationPeriod, Period.class, true));
        fieldContainer.<Pool>get(BankTransfer_DestinationPeriod).setDataCore(new ValueRead_DataCore<>(true));
        fieldContainer.<Period>get(BankTransfer_DestinationPeriod).addFilter(new Dependant_FieldFilter<Period, BankTransfer>(Transfer_Period, Transfer_Destination) {
            @Override
            public boolean isValid(Period value, BankTransfer bankTransfer) {
                if (value != null && !bankTransfer.doseSupportDestinationPeriod())
                    return false;
                return !bankTransfer.getPeriod().equals(value);
            }
        });
        // Category ====================================================================================================
        fieldContainer.add(new Tracking_DataField<>(BankTransfer_Category, Category.class, true));
        fieldContainer.<Category>get(BankTransfer_Category).setDataCore(new MethodSet_DataCore<>(
                container -> {
                    if (((BankTransfer) container).getDestination() instanceof SolidCategory) {
                        return (SolidCategory) ((BankTransfer) container).getDestination();
                    }
                    return null;
                },
                (container, toSet) -> ((BankTransfer) container).setDestination(toSet)));
        // Bank ========================================================================================================
        fieldContainer.add(new Tracking_DataField<>(BankTransfer_Bank, Bank.class, true));
        fieldContainer.<Bank>get(BankTransfer_Bank).setDataCore(new MethodSet_DataCore<>(
                container -> {
                    if (((BankTransfer) container).getDestination() instanceof Bank) {
                        return (Bank) ((BankTransfer) container).getDestination();
                    }
                    return null;
                },
                (container, toSet) -> ((BankTransfer) container).setDestination(toSet)));
        // FundEvent ===================================================================================================
        fieldContainer.add(new Tracking_DataField<>(BankTransfer_FundEvent, FundEvent.class, true));
        fieldContainer.<FundEvent>get(BankTransfer_FundEvent).setDataCore(new MethodSet_DataCore<>(
                container -> {
                    if (((BankTransfer) container).getDestination() instanceof FundEvent) {
                        return (FundEvent) ((BankTransfer) container).getDestination();
                    }
                    return null;
                },
                (container, toSet) -> ((BankTransfer) container).setDestination(toSet)));
        // Destination =================================================================================================
        fieldContainer.<Pool>get(Transfer_Destination).setDataCore(new ValueRead_DataCore<>(true));
        fieldContainer.<Pool>get(Transfer_Destination).addFilter(new Dependant_FieldFilter<Pool, BankTransfer>(Transfer_Source) {
            @Override
            public boolean isValid(Pool value, BankTransfer bankTransfer) {
                return !bankTransfer.getSource().equals(value);
            }
        });
        // TODO This might be a problem, its only safe because it goes through the impl method below, if it was ever set directly it would break
        fieldContainer.<Pool>get(Transfer_Destination).addChangeListener(new Marked_FieldChangeListener<Pool>(Transfer_Destination_ListenerMark) {
            @Override
            public void valueChanged(DataField<Pool> field, Pool oldValue, Pool newValue) {
                if (field.getState().equals(DataField.NewFieldState.N_ACTIVE)) {
                    BankTransfer bankTransfer = ((BankTransfer) field.getContainer());
                    // Destination Period can only be maintained if its a Bank to Bank transfer
                    if (!bankTransfer.doseSupportDestinationPeriod()) {
                        bankTransfer.setDestinationPeriod(null);
                    }

                    // Destination value can only be maintained if its a bank to bank transfer with different currencies
                    if (!bankTransfer.doseSupportDestinationValue()) {
                        bankTransfer.setDestinationValue(null);
                    }
                }
            }
        });
        // DestinationValue ============================================================================================
        fieldContainer.add(Transfer_Destination, new DataField<>(BankTransfer_DestinationValue, Double.class, true));
        fieldContainer.get(BankTransfer_DestinationValue).getDisplayProperties().setDataType(CURRENCY);
        fieldContainer.get(BankTransfer_DestinationValue).setDataCore(new ValueRead_DataCore<>(true));
        // TODO probably best to replace this with a conversation step that takes 0 and makes it null so it dosnt fail
        fieldContainer.<Double>get(BankTransfer_DestinationValue).addFilter(new Dependant_FieldFilter<Double, BankTransfer>(Transfer_Destination, Transfer_Source) {
            @Override
            public boolean isValid(Double value, BankTransfer bankTransfer) {
                if (value != null && !bankTransfer.doseSupportDestinationValue()) {
                    return false;
                }
                return value == null || !value.equals(0.0);
            }
        });
        // DestinationCurrency =========================================================================================
        fieldContainer.add(new DataField<>(BankTransfer_DestinationCurrency, Currency.class, true));
        fieldContainer.<Currency>get(BankTransfer_DestinationCurrency).setDataCore(
                new Derived_DataCore<>(container -> {
                    BankTransfer bankTransfer = ((BankTransfer) container);
                    if (bankTransfer.doseSupportDestinationCurrency()) {
                        return ((Bank) bankTransfer.getDestination()).getCurrency();
                    }
                    return null;
                }
                        , new Derived_DataCore.LocalSource<>(fieldContainer.get(Transfer_Destination))
                        , new Derived_DataCore.LocalSource<>(fieldContainer.get(Transfer_Source))));
        // SourceCurrencyGet
        // DestinationCurrencyGet ======================================================================================
        fieldContainer.<Currency>get(Transfer_DestinationCurrencyGet).setDataCore(
                new Derived_DataCore<>((Derived_DataCore.Converter<Currency, BankTransfer>) container -> {
                    if (container.getDestinationCurrency() != null) {
                        return container.getDestinationCurrency();
                    } else {
                        return container.getCurrency();
                    }
                }
                        , new Derived_DataCore.LocalSource<>(fieldContainer.get(Transfer_Currency))
                        , new Derived_DataCore.LocalSource<>(fieldContainer.get(BankTransfer_DestinationCurrency))));
        // SourcePeriodGet
        // DestinationPeriodGet ========================================================================================
        fieldContainer.<Period>get(Transfer_DestinationPeriodGet).setDataCore(
                new Derived_DataCore<>((Derived_DataCore.Converter<Period, BankTransfer>) container -> {
                    if (container.getDestinationPeriod() != null) {
                        return container.getDestinationPeriod();
                    } else {
                        return container.getPeriod();
                    }
                }
                        , new Derived_DataCore.LocalSource<>(fieldContainer.get(Transfer_Period))
                        , new Derived_DataCore.LocalSource<>(fieldContainer.get(BankTransfer_DestinationPeriod))));
        //==============================================================================================================
        // Parents
        // Children

        return fieldContainer.endLayer(BankTransfer.class);
    }

    /**
     * {@inheritDoc
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    protected void remove_impl() {
        FieldChangeListener toRemove = null;
        for (FieldChangeListener<?> fieldChangeListener : fieldMap.get(Transfer_Source).getFieldChangeListeners()) {
            if (fieldChangeListener instanceof Marked_FieldChangeListener) {
                if (((Marked_FieldChangeListener<?>) fieldChangeListener).getId().equals(Transfer_Source_ListenerMark)) {
                    toRemove = fieldChangeListener;
                    break;
                }
            }
        }
        if (toRemove != null) {
            fieldMap.get(Transfer_Source).removeChangeListener(toRemove);
        }

        toRemove = null;
        for (FieldChangeListener<?> fieldChangeListener : fieldMap.get(Transfer_Destination).getFieldChangeListeners()) {
            if (fieldChangeListener instanceof Marked_FieldChangeListener) {
                if (((Marked_FieldChangeListener<?>) fieldChangeListener).getId().equals(Transfer_Destination_ListenerMark)) {
                    toRemove = fieldChangeListener;
                    break;
                }
            }
        }
        if (toRemove != null) {
            fieldMap.get(Transfer_Destination).removeChangeListener(toRemove);
        }
        super.remove_impl();
    }

    /**
     * {@inheritDoc
     */
    @Override
    @SuppressWarnings({"SuspiciousMethodCalls", "unchecked"})
    public <T extends DataObject> List<T> sourceOptions(Class<T> type, String fieldName) {
        switch (fieldName) {
            case "DestinationPeriod":
                if (doseSupportDestinationPeriod()) {
                    List<T> toReturn = super.sourceOptions(type, fieldName);
                    toReturn.add(null);
                    toReturn.remove(getPeriod());
                    return toReturn;
                } else {
                    List<T> toReturn = new ArrayList<>();
                    toReturn.add(null);
                    return toReturn;
                }
            case "Destination":
            case "Bank": {
                List<T> toReturn = super.sourceOptions(type, fieldName);
                toReturn.remove(getSource());
                return toReturn;
            }
            case "Source": {
                List<T> toReturn = new ArrayList<>();
                for (Bank bank : super.sourceOptions(Bank.class, fieldName)) {
                    toReturn.add((T) bank);
                }
                return toReturn;
            }
            case "Period": {
                List<T> toReturn = super.sourceOptions(type, fieldName);
                toReturn.remove(getDestinationPeriod());
                return toReturn;
            }
        }
        return super.sourceOptions(type, fieldName);
    }

    /**
     * Dose this object, in its current state support setting a destination value?
     *
     * @return True if it supports setting a destination value
     */
    boolean doseSupportDestinationValue() {
        return doseSupportDestinationCurrency();
    }

    /**
     * Dose this object, in its current state support setting a destination currency?
     *
     * @return True if it supports setting a destination currency
     */
    boolean doseSupportDestinationCurrency() {
        if (getDestination() instanceof Bank) {
            Currency sourceCurrency = ((Bank) getSource()).getCurrency();
            Currency destinationCurrency = ((Bank) getDestination()).getCurrency();
            return !sourceCurrency.equals(destinationCurrency);
        }
        return false;
    }

    /**
     * Dose this object, in its current state support setting a destination period?
     *
     * @return True if it supports setting a destination period
     */
    boolean doseSupportDestinationPeriod() {
        return getDestination() instanceof Bank || getDestination() instanceof SolidCategory;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public Period getDestinationPeriod() {
        return get(BankTransfer_DestinationPeriod);
    }

    public Double getDestinationValue() {
        return get(BankTransfer_DestinationValue);
    }

    public Currency getDestinationCurrency() {
        return get(BankTransfer_DestinationCurrency);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    protected void setDestinationPeriod(Period destinationPeriod) {
        set(BankTransfer_DestinationPeriod, destinationPeriod);
    }

    protected void setDestination(Pool destination) {
        set(Transfer_Destination, destination);
    }

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
