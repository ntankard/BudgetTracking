package com.ntankard.tracking.dataBase.core.transfer.bank;

import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.end.End_Source_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Source_Factory;
import com.ntankard.javaObjectDatabase.dataField.validator.Shared_FieldValidator;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.tracking.dataBase.core.Currency;
import com.ntankard.tracking.dataBase.core.period.Period;
import com.ntankard.tracking.dataBase.core.pool.Bank;
import com.ntankard.tracking.dataBase.core.pool.category.Category;
import com.ntankard.tracking.dataBase.core.pool.category.SolidCategory;
import com.ntankard.tracking.dataBase.core.pool.fundEvent.FundEvent;
import com.ntankard.tracking.dataBase.core.pool.Pool;
import com.ntankard.tracking.dataBase.core.transfer.Transfer;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.tracking.dataBase.core.pool.Bank.Bank_Currency;

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
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = Transfer.getDataObjectSchema();

        Shared_FieldValidator<Period, Period, BankTransfer> period_sharedFilter = new Shared_FieldValidator<>(Transfer_Period, BankTransfer_DestinationPeriod,
                (firstNewValue, secondNewValue, container) -> {
                    if (secondNewValue != null) {
                        return !firstNewValue.equals(secondNewValue);
                    }
                    return true;
                });

        Shared_FieldValidator<Pool, Pool, BankTransfer> pool_sharedFilter = new Shared_FieldValidator<>(Transfer_Source, Transfer_Destination,
                (firstNewValue, secondNewValue, container) -> {
                    if (secondNewValue != null) {
                        return !firstNewValue.equals(secondNewValue);
                    }
                    return true;
                });

        // ID
        // Description
        // Period ======================================================================================================
        dataObjectSchema.get(Transfer_Period).setManualCanEdit(true);
        dataObjectSchema.<Period>get(Transfer_Period).addValidator(period_sharedFilter.getFirstFilter());
        // Source ======================================================================================================
        dataObjectSchema.add(Transfer_Period, new DataField_Schema<>(Transfer_Source, Bank.class));
        dataObjectSchema.<Pool>get(Transfer_Source).addValidator(pool_sharedFilter.getFirstFilter());
        // Value =======================================================================================================
        dataObjectSchema.get(Transfer_Value).setManualCanEdit(true);
        // Currency ====================================================================================================
        dataObjectSchema.<Currency>get(Transfer_Currency).setDataCore_schema(
                new Derived_DataCore_Schema<Currency, BankTransfer>
                        (dataObject -> ((Bank)dataObject.getSource()).getCurrency()
                                , Source_Factory.makeSourceChain(Transfer_Source, Bank_Currency)));
        // DestinationPeriod ===========================================================================================
        dataObjectSchema.add(Transfer_Currency, new DataField_Schema<>(BankTransfer_DestinationPeriod, Period.class, true));
        dataObjectSchema.get(BankTransfer_DestinationPeriod).setManualCanEdit(true);
        dataObjectSchema.<Period>get(BankTransfer_DestinationPeriod).addValidator(period_sharedFilter.getSecondFilter());
        // Category ====================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(BankTransfer_Category, Category.class, true));
        dataObjectSchema.get(BankTransfer_Category).setTellParent(false);
        dataObjectSchema.<Category>get(BankTransfer_Category).setSetterFunction((toSet, container) -> {
            ((BankTransfer) container).setDestination(toSet);
        });
        dataObjectSchema.<Category>get(BankTransfer_Category).setDataCore_schema(
                new Derived_DataCore_Schema<Category, BankTransfer>(container -> {
                    if (container.getDestination() instanceof SolidCategory) {
                        return (SolidCategory) container.getDestination();
                    }
                    return null;
                }, new End_Source_Schema<>(Transfer_Destination)));
        // Bank ========================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(BankTransfer_Bank, Bank.class, true));
        dataObjectSchema.get(BankTransfer_Bank).setTellParent(false);
        dataObjectSchema.<Bank>get(BankTransfer_Bank).setSetterFunction((toSet, container) -> {
            ((BankTransfer) container).setDestination(toSet);
        });
        dataObjectSchema.<Bank>get(BankTransfer_Bank).setDataCore_schema(
                new Derived_DataCore_Schema<Bank, BankTransfer>(container -> {
                    if (container.getDestination() instanceof Bank) {
                        return (Bank) container.getDestination();
                    }
                    return null;
                }, new End_Source_Schema<>((Transfer_Destination))));
        // FundEvent ===================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(BankTransfer_FundEvent, FundEvent.class, true));
        dataObjectSchema.get(BankTransfer_FundEvent).setTellParent(false);
        dataObjectSchema.<FundEvent>get(BankTransfer_FundEvent).setSetterFunction((toSet, container) -> {
            ((BankTransfer) container).setDestination(toSet);
        });
        dataObjectSchema.<FundEvent>get(BankTransfer_FundEvent).setDataCore_schema(
                new Derived_DataCore_Schema<FundEvent, BankTransfer>(container -> {
                    if (container.getDestination() instanceof FundEvent) {
                        return (FundEvent) container.getDestination();
                    }
                    return null;
                }, new End_Source_Schema<>((Transfer_Destination))));
        // Destination =================================================================================================
        dataObjectSchema.get(Transfer_Destination).setManualCanEdit(true);
        dataObjectSchema.<Pool>get(Transfer_Destination).addValidator(pool_sharedFilter.getSecondFilter());
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
     * Constructor
     */
    public BankTransfer(Database database) {
        super(database);
    }

    /**
     * @inheritDoc
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    protected void remove_impl() {
        super.remove_impl();
    }

    /**
     * @inheritDoc
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
