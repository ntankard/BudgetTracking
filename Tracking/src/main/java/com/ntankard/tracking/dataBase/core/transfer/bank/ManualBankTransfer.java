package com.ntankard.tracking.dataBase.core.transfer.bank;

import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.end.EndSource_Schema;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore;
import com.ntankard.tracking.dataBase.core.Currency;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.tracking.dataBase.core.period.Period;
import com.ntankard.tracking.dataBase.core.pool.Bank;
import com.ntankard.tracking.dataBase.core.pool.Pool;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore.Calculator;

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
        dataObjectSchema.<Currency>get(Transfer_DestinationCurrencyGet).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Schema<>(
                        (Calculator<Currency, ManualBankTransfer>) container ->
                                container.getCurrency()
                        , new EndSource_Schema<>((Transfer_Currency))));
        // SourcePeriodGet
        // DestinationPeriodGet ========================================================================================
        dataObjectSchema.<Period>get(Transfer_DestinationPeriodGet).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Schema<>((Calculator<Period, BankTransfer>) container -> {
                    if (container.getDestinationPeriod() != null) {
                        return container.getDestinationPeriod();
                    } else {
                        return container.getPeriod();
                    }
                }
                        , new EndSource_Schema<>((Transfer_Period))
                        , new EndSource_Schema<>((BankTransfer_DestinationPeriod))));
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
