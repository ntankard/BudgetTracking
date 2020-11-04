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
                return newValue == null || !bankTransfer.getPeriod().equals(newValue);
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
                }, new LocalSource.LocalSource_Factory<>(Transfer_Destination)));
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
        // DestinationValue
        // DestinationCurrency
        // SourceCurrencyGet
        // DestinationCurrencyGet
        // SourcePeriodGet
        // DestinationPeriodGet
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
        super.remove_impl();
    }

    /**
     * {@inheritDoc
     */
    @Override
    @SuppressWarnings({"SuspiciousMethodCalls", "unchecked"})
    public <T extends DataObject> List<T> sourceOptions(Class<T> type, String fieldName) {
        switch (fieldName) {
            case "DestinationPeriod": {
                List<T> toReturn = super.sourceOptions(type, fieldName);
                toReturn.add(null);
                toReturn.remove(getPeriod());
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

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public Period getDestinationPeriod() {
        return get(BankTransfer_DestinationPeriod);
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
