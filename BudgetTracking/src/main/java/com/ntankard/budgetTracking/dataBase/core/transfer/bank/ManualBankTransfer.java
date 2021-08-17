package com.ntankard.budgetTracking.dataBase.core.transfer.bank;

import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema.Calculator;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.end.End_Source_Schema;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.budgetTracking.dataBase.core.Currency;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.budgetTracking.dataBase.core.period.Period;
import com.ntankard.budgetTracking.dataBase.core.pool.Bank;
import com.ntankard.budgetTracking.dataBase.core.pool.Pool;

import java.util.ArrayList;
import java.util.List;

public class ManualBankTransfer extends BankTransfer {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = BankTransfer.getDataObjectSchema();

        // ID
        // Description
        // Period
        // Source
        // Value
        // Currency
        // DestinationPeriod
        // Category
        // Bank
        // FundEvent
        // Destination
        // SourceCurrencyGet
        // DestinationCurrencyGet ======================================================================================
        dataObjectSchema.<Currency>get(Transfer_DestinationCurrencyGet).setDataCore_schema(
                new Derived_DataCore_Schema<>(
                        (Calculator<Currency, ManualBankTransfer>) container ->
                                container.getCurrency()
                        , new End_Source_Schema<>((Transfer_Currency))));
        // SourcePeriodGet
        // DestinationPeriodGet ========================================================================================
        dataObjectSchema.<Period>get(Transfer_DestinationPeriodGet).setDataCore_schema(
                new Derived_DataCore_Schema<>((Calculator<Period, BankTransfer>) container -> {
                    if (container.getDestinationPeriod() != null) {
                        return container.getDestinationPeriod();
                    } else {
                        return container.getPeriod();
                    }
                }
                        , new End_Source_Schema<>((Transfer_Period))
                        , new End_Source_Schema<>((BankTransfer_DestinationPeriod))));
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(ManualBankTransfer.class);
    }

    /**
     * Constructor
     */
    public ManualBankTransfer(String description,
                              Period period, Bank source, Double value,
                              Period destinationPeriod, Pool destination) {
        this(period.getTrackingDatabase());
        setAllValues(DataObject_Id, getTrackingDatabase().getNextId()
                , Transfer_Description, description
                , Transfer_Period, period
                , Transfer_Source, source
                , Transfer_Value, value
                , BankTransfer_DestinationPeriod, destinationPeriod
                , Transfer_Destination, destination
        );
    }

    /**
     * Constructor
     */
    public ManualBankTransfer(Database database) {
        super(database);
    }

    /**
     * @inheritDoc
     */
    @Override
    public <T extends DataObject> List<T> sourceOptions(Class<T> type, String fieldName) {
        switch (fieldName) {
            case "Destination":
            case "Bank": {
                List<T> toReturn = super.sourceOptions(type, fieldName);
                toReturn.removeIf(t -> {
                    if(Bank.class.isAssignableFrom(t.getClass())){
                        Bank bank = (Bank)t;
                        return !((Bank)getSource()).getCurrency().equals(bank.getCurrency());
                    }
                    return false;
                });
                return toReturn;
            }
        }
        return super.sourceOptions(type, fieldName);
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
