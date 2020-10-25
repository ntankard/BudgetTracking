package com.ntankard.Tracking.DataBase.Core.Transfer.Bank;

import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.Category;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.SolidCategory;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Core.Transfer.Transfer;
import com.ntankard.javaObjectDatabase.CoreObject.DataObject;
import com.ntankard.javaObjectDatabase.CoreObject.Field.DataField_Schema;
import com.ntankard.javaObjectDatabase.CoreObject.Field.Filter.Dependant_FieldFilter;
import com.ntankard.javaObjectDatabase.CoreObject.Field.Filter.FieldFilter;
import com.ntankard.javaObjectDatabase.CoreObject.Field.Listener.FieldChangeListener;
import com.ntankard.javaObjectDatabase.CoreObject.Field.Listener.Marked_FieldChangeListener;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.Derived_DataCore;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.source.DirectExternalSource;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.source.LocalSource;
import com.ntankard.javaObjectDatabase.CoreObject.DataObject_Schema;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank_Currency;
import static com.ntankard.javaObjectDatabase.CoreObject.Field.Properties.Display_Properties.DataType.CURRENCY;

public abstract class BankTransfer extends Transfer {

    public interface BankTransferList extends List<BankTransfer> {
    }

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
    public static DataObject_Schema getFieldContainer() {
        DataObject_Schema dataObjectSchema = Transfer.getFieldContainer();

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
        // Source ======================================================================================================
        dataObjectSchema.add(Transfer_Period, new DataField_Schema<>(Transfer_Source, Bank.class));
//        fieldContainer.get(Transfer_Source).addChangeListener(new Marked_FieldChangeListener<Object>(Transfer_Source_ListenerMark) {
//            @Override
//            public void valueChanged(DataField<Object> field, Object oldValue, Object newValue) {
//                // TODO Hidden error here, basically when this object setups up it triggers the change listener but when an object is being build the fields have not been added yet. This check patches the problem but may be a larger issue
//                if (field.getState().equals(DataField.NewFieldState.N_ACTIVE)) {
//                    BankTransfer thisTransfer = (BankTransfer) field.getContainer();
//                    if (!(thisTransfer.getDestination() instanceof Bank && thisTransfer.getDestinationCurrency() != null)) {
//
//                        thisTransfer.setDestinationValue(null);
//                    }
//                    thisTransfer.validateParents();
//                }
//            }
//        });
        // Value =======================================================================================================
        dataObjectSchema.get(Transfer_Value).setManualCanEdit(true);
        // Currency ====================================================================================================
        dataObjectSchema.<Currency>get(Transfer_Currency).setDataCore_factory(new Derived_DataCore.Derived_DataCore_Factory<>(new DirectExternalSource.DirectExternalSource_Factory<>((Transfer_Source), Bank_Currency)));
        // DestinationPeriod ===========================================================================================
        dataObjectSchema.add(Transfer_Currency, new DataField_Schema<>(BankTransfer_DestinationPeriod, Period.class, true));
        dataObjectSchema.get(BankTransfer_DestinationPeriod).setManualCanEdit(true);
        dataObjectSchema.<Period>get(BankTransfer_DestinationPeriod).addFilter(new Dependant_FieldFilter<Period, BankTransfer>(Transfer_Period, Transfer_Destination) {
            @Override
            public boolean isValid(Period newValue, Period pastValue, BankTransfer bankTransfer) {
                if (newValue != null && !bankTransfer.doseSupportDestinationPeriod())
                    return false;
                return !bankTransfer.getPeriod().equals(newValue);
            }
        });
        // Category ====================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(BankTransfer_Category, Category.class, true));
        dataObjectSchema.get(BankTransfer_Category).setTellParent(false);
        dataObjectSchema.<Category>get(BankTransfer_Category).setSetterFunction((toSet, container) -> {
            ((BankTransfer) container).setDestination(toSet);
        });
        dataObjectSchema.<Category>get(BankTransfer_Category).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<Category, BankTransfer>(container -> {
                    if (container.getDestination() instanceof SolidCategory) {
                        return (SolidCategory) container.getDestination();
                    }
                    return null;
                }, new LocalSource.LocalSource_Factory<>((Transfer_Destination))));
        // Bank ========================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(BankTransfer_Bank, Bank.class, true));
        dataObjectSchema.get(BankTransfer_Bank).setTellParent(false);
        dataObjectSchema.<Bank>get(BankTransfer_Bank).setSetterFunction((toSet, container) -> {
            ((BankTransfer) container).setDestination(toSet);
        });
        dataObjectSchema.<Bank>get(BankTransfer_Bank).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<Bank, BankTransfer>(container -> {
                    if (container.getDestination() instanceof Bank) {
                        return (Bank) container.getDestination();
                    }
                    return null;
                }, new LocalSource.LocalSource_Factory<>((Transfer_Destination))));
        // FundEvent ===================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(BankTransfer_FundEvent, FundEvent.class, true));
        dataObjectSchema.get(BankTransfer_FundEvent).setTellParent(false);
        dataObjectSchema.<FundEvent>get(BankTransfer_FundEvent).setSetterFunction((toSet, container) -> {
            ((BankTransfer) container).setDestination(toSet);
        });
        dataObjectSchema.<FundEvent>get(BankTransfer_FundEvent).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<FundEvent, BankTransfer>(container -> {
                    if (container.getDestination() instanceof FundEvent) {
                        return (FundEvent) container.getDestination();
                    }
                    return null;
                }, new LocalSource.LocalSource_Factory<>((Transfer_Destination))));
        // Destination =================================================================================================
        dataObjectSchema.get(Transfer_Destination).setManualCanEdit(true);
        dataObjectSchema.<Pool>get(Transfer_Destination).addFilter(new Dependant_FieldFilter<Pool, BankTransfer>(Transfer_Source) {
            @Override
            public boolean isValid(Pool newValue, Pool pastValue, BankTransfer bankTransfer) {
                return !bankTransfer.getSource().equals(newValue);
            }
        });
        // TODO This might be a problem, its only safe because it goes through the impl method below, if it was ever set directly it would break
//        fieldContainer.<Pool>get(Transfer_Destination).addChangeListener(new Marked_FieldChangeListener<Pool>(Transfer_Destination_ListenerMark) {
//            @Override
//            public void valueChanged(DataField<Pool> field, Pool oldValue, Pool newValue) {
//                if (field.getState().equals(DataField.NewFieldState.N_ACTIVE)) {
//                    BankTransfer bankTransfer = ((BankTransfer) field.getContainer());
//                    // Destination Period can only be maintained if its a Bank to Bank transfer
//                    if (!bankTransfer.doseSupportDestinationPeriod()) {
//                        bankTransfer.setDestinationPeriod(null);
//                    }
//
//                    // Destination value can only be maintained if its a bank to bank transfer with different currencies
//                    if (!bankTransfer.doseSupportDestinationValue()) {
//                        bankTransfer.setDestinationValue(null);
//                    }
//                }
//            }
//        });
        // DestinationValue ============================================================================================
        dataObjectSchema.add(Transfer_Destination, new DataField_Schema<>(BankTransfer_DestinationValue, Double.class, true));
        dataObjectSchema.get(BankTransfer_DestinationValue).getDisplayProperties().setDataType(CURRENCY);
        dataObjectSchema.get(BankTransfer_DestinationValue).setManualCanEdit(true);
        // TODO probably best to replace this with a conversation step that takes 0 and makes it null so it dosnt fail
        dataObjectSchema.<Double>get(BankTransfer_DestinationValue).addFilter(new Dependant_FieldFilter<Double, BankTransfer>(Transfer_Destination, Transfer_Source) {
            @Override
            public boolean isValid(Double newValue, Double pastValue, BankTransfer bankTransfer) {
                if (newValue != null && !bankTransfer.doseSupportDestinationValue()) {
                    return false;
                }
                return newValue == null || !newValue.equals(0.0);
            }
        });
        // DestinationCurrency =========================================================================================
        dataObjectSchema.add(new DataField_Schema<>(BankTransfer_DestinationCurrency, Currency.class, true));
        dataObjectSchema.<Currency>get(BankTransfer_DestinationCurrency).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>(container -> {
                    BankTransfer bankTransfer = ((BankTransfer) container);
                    if (bankTransfer.doseSupportDestinationCurrency()) {
                        return ((Bank) bankTransfer.getDestination()).getCurrency();
                    }
                    return null;
                }
                        , new LocalSource.LocalSource_Factory<>((Transfer_Destination))
                        , new LocalSource.LocalSource_Factory<>((Transfer_Source))));
        // SourceCurrencyGet
        // DestinationCurrencyGet ======================================================================================
        dataObjectSchema.<Currency>get(Transfer_DestinationCurrencyGet).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>((Derived_DataCore.Calculator<Currency, BankTransfer>) container -> {
                    if (container.getDestinationCurrency() != null) {
                        return container.getDestinationCurrency();
                    } else {
                        return container.getCurrency();
                    }
                }
                        , new LocalSource.LocalSource_Factory<>((Transfer_Currency))
                        , new LocalSource.LocalSource_Factory<>((BankTransfer_DestinationCurrency))));
        // SourcePeriodGet
        // DestinationPeriodGet ========================================================================================
        dataObjectSchema.<Period>get(Transfer_DestinationPeriodGet).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>((Derived_DataCore.Calculator<Period, BankTransfer>) container -> {
                    if (container.getDestinationPeriod() != null) {
                        return container.getDestinationPeriod();
                    } else {
                        return container.getPeriod();
                    }
                }
                        , new LocalSource.LocalSource_Factory<>((Transfer_Period))
                        , new LocalSource.LocalSource_Factory<>((BankTransfer_DestinationPeriod))));
        //==============================================================================================================
        // Parents
        // Children

        return dataObjectSchema.endLayer(BankTransfer.class);
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
