package com.ntankard.tracking.dataBase.interfaces.summary.pool;

import com.ntankard.javaObjectDatabase.database.Database_Schema;
import com.ntankard.tracking.dataBase.core.baseObject.interfaces.CurrencyBound;
import com.ntankard.tracking.dataBase.core.Currency;
import com.ntankard.tracking.dataBase.core.period.ExistingPeriod;
import com.ntankard.tracking.dataBase.core.period.Period;
import com.ntankard.tracking.dataBase.core.pool.Bank;
import com.ntankard.tracking.dataBase.core.pool.category.SolidCategory;
import com.ntankard.tracking.dataBase.core.pool.Pool;
import com.ntankard.tracking.dataBase.core.StatementEnd;
import com.ntankard.tracking.dataBase.core.StatementEnd.StatementEndList;
import com.ntankard.tracking.dataBase.core.transfer.bank.BankTransfer;
import com.ntankard.tracking.dataBase.core.transfer.HalfTransfer;
import com.ntankard.tracking.dataBase.interfaces.set.filter.TransferDestination_HalfTransfer_Filter;
import com.ntankard.tracking.dataBase.interfaces.set.filter.TransferType_HalfTransfer_Filter;
import com.ntankard.javaObjectDatabase.dataObject.factory.DoubleParentFactory;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.ListDataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.Children_ListDataCore;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.DirectExternal_Source;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.External_Source;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.List_Source;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Local_Source;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.dataObject.interfaces.Ordered;
import com.ntankard.javaObjectDatabase.database.ParameterMap;
import com.ntankard.javaObjectDatabase.database.Database;

import java.util.List;

import static com.ntankard.tracking.dataBase.core.Currency.Currency_ToPrimary;
import static com.ntankard.tracking.dataBase.core.period.ExistingPeriod.ExistingPeriod_Order;
import static com.ntankard.tracking.dataBase.core.pool.Bank.*;
import static com.ntankard.tracking.dataBase.core.StatementEnd.StatementEnd_End;
import static com.ntankard.tracking.dataBase.core.transfer.HalfTransfer.*;
import static com.ntankard.javaObjectDatabase.dataField.properties.Display_Properties.DataType.CURRENCY;
import static com.ntankard.javaObjectDatabase.dataField.properties.Display_Properties.INFO_DISPLAY;
import static com.ntankard.javaObjectDatabase.dataField.properties.Display_Properties.TRACE_DISPLAY;

@ParameterMap(shouldSave = false)
public class Bank_Summary extends PoolSummary<Bank> implements CurrencyBound, Ordered {

    public interface Bank_SummaryList extends List<Bank_Summary> {
    }

    public static final String Bank_Summary_Currency = "getCurrency";
    public static final String Bank_Summary_StatementEndSet = "getStatementEndSet";
    public static final String Bank_Summary_StatementEnd = "getStatementEnd";
    public static final String Bank_Summary_BankSummarySet = "getBankSummarySet";
    public static final String Bank_Summary_PreviousBankSummary = "getPreviousBankSummary";
    public static final String Bank_Summary_NetTransferSet = "getNetTransferSet";
    public static final String Bank_Summary_NetTransferSum = "getNetTransferSum"; // In primary currency
    public static final String Bank_Summary_NetTransfer = "getNetTransfer";
    public static final String Bank_Summary_SpendSet = "getSpendSet";
    public static final String Bank_Summary_SpendSum = "getSpendSum"; // In primary currency
    public static final String Bank_Summary_Spend = "getSpend";
    public static final String Bank_Summary_Order = "getOrder";

    public static DoubleParentFactory<?, ?, ?> Factory = new DoubleParentFactory<>(
            Bank_Summary.class,
            Bank.class,
            PoolSummary_Pool, ExistingPeriod.class,
            PoolSummary_Period, (generator, secondaryGenerator) -> Bank_Summary.make(generator.getTrackingDatabase().getNextId(), secondaryGenerator, generator)
    );

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getFieldContainer() {
        DataObject_Schema dataObjectSchema = PoolSummary.getFieldContainer();

        // Class behavior
        dataObjectSchema.setMyFactory(Factory);

        // ID
        // Period
        // Pool
        // BankSummarySet ==============================================================================================
        dataObjectSchema.add(new ListDataField_Schema<>(Bank_Summary_BankSummarySet, Bank_SummaryList.class));
        dataObjectSchema.get(Bank_Summary_BankSummarySet).getDisplayProperties().setVerbosityLevel(TRACE_DISPLAY);
        dataObjectSchema.<List<Bank_Summary>>get(Bank_Summary_BankSummarySet).setDataCore_factory(
                new Children_ListDataCore.Children_ListDataCore_Factory<>(
                        Bank_Summary.class,
                        new Children_ListDataCore.ParentAccess.ParentAccess_Factory<>((PoolSummary_Pool))));
        // PreviousBankSummary =========================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Bank_Summary_PreviousBankSummary, Bank_Summary.class, true));
        dataObjectSchema.get(Bank_Summary_PreviousBankSummary).getDisplayProperties().setVerbosityLevel(TRACE_DISPLAY);
        dataObjectSchema.<Bank_Summary>get(Bank_Summary_PreviousBankSummary).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>(
                        (Derived_DataCore.Calculator<Bank_Summary, Bank_Summary>) container -> {
                            List<Bank_Summary> bank_summaries = container.getBankSummarySet();
                            for (Bank_Summary bankSummary : bank_summaries) {
                                if (bankSummary.getPeriod().getOrder() == container.getPeriod().getOrder() - 1) {
                                    return bankSummary;
                                }
                            }
                            return null;
                        }
                        , new Local_Source.LocalSource_Factory<>((Bank_Summary_BankSummarySet)))); // TODO you need to watch period
        // StatementEndSet ================================================================================================
        dataObjectSchema.add(new ListDataField_Schema<>(Bank_Summary_StatementEndSet, StatementEndList.class));
        dataObjectSchema.get(Bank_Summary_StatementEndSet).getDisplayProperties().setVerbosityLevel(TRACE_DISPLAY);
        dataObjectSchema.<List<StatementEnd>>get(Bank_Summary_StatementEndSet).setDataCore_factory(
                new Children_ListDataCore.Children_ListDataCore_Factory<>(
                        StatementEnd.class,
                        new Children_ListDataCore.ParentAccess.ParentAccess_Factory<>((PoolSummary_Period)),
                        new Children_ListDataCore.ParentAccess.ParentAccess_Factory<>((PoolSummary_Pool))));
        // StatementEnd ================================================================================================`
        dataObjectSchema.add(new DataField_Schema<>(Bank_Summary_StatementEnd, StatementEnd.class, true));
        dataObjectSchema.<StatementEnd>get(Bank_Summary_StatementEnd).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<StatementEnd, Bank_Summary>(
                        container -> {
                            if (container.getStatementEndSet().isEmpty()) {
                                return null;
                            }
                            return container.getStatementEndSet().get(0);
                        },
                        new Local_Source.LocalSource_Factory<>((Bank_Summary_StatementEndSet))));
        // Currency ====================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Bank_Summary_Currency, Currency.class, false));
        dataObjectSchema.get(Bank_Summary_Currency).getDisplayProperties().setVerbosityLevel(INFO_DISPLAY);
        dataObjectSchema.<Currency>get(Bank_Summary_Currency).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>(
                        new DirectExternal_Source.DirectExternalSource_Factory<>(
                                (PoolSummary_Pool), Bank_Currency)));
        // Start =======================================================================================================
        dataObjectSchema.<Double>get(PoolSummary_Start).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>(
                        (Derived_DataCore.Calculator<Double, Bank_Summary>) container -> {
                            Bank_Summary previous = container.getPreviousBankSummary();
                            if (previous == null) {
                                return container.getPool().getStart();
                            }
                            return previous.getStatementEnd().getEnd();
                        }
                        , new External_Source.ExternalSource_Factory<>((Bank_Summary_PreviousBankSummary), Bank_Summary_StatementEnd)
                        , new External_Source.ExternalSource_Factory<>((PoolSummary_Pool), Bank_Start)));
        // End =========================================================================================================
        dataObjectSchema.<Double>get(PoolSummary_End).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<Double, Bank_Summary>(
                        container -> {
                            if (container.getStatementEnd() == null) { // TODO this is needed because you allow statement end to be null. If you don't it fails the initial check. Look at fixing this
                                return -1.0;
                            }
                            return container.getStatementEnd().getEnd();
                        }
                        , new External_Source.ExternalSource_Factory<>((Bank_Summary_StatementEnd), StatementEnd_End)));
        dataObjectSchema.<Double>get(PoolSummary_End).setSetterFunction((toSet, container) -> {
            ((Bank_Summary) container).getStatementEnd().setEnd(toSet);
        });
        // TransferSum =================================================================================================
        dataObjectSchema.<Double>get(PoolSummary_TransferSum).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>(
                        (Derived_DataCore.Calculator<Double, PoolSummary<Bank>>) container ->
                                container.getTransferSetSum() / container.getCurrency().getToPrimary()
                        , new Local_Source.LocalSource_Factory<>((PoolSummary_TransferSetSum))
                        , new External_Source.ExternalSource_Factory<>((Bank_Summary_Currency), Currency_ToPrimary)));
        // Missing
        // Valid =======================================================================================================
        dataObjectSchema.<Boolean>get(PoolSummary_Valid).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>(
                        (Derived_DataCore.Calculator<Boolean, PoolSummary<Bank>>) container -> container.getMissing().equals(0.00)
                        , new Local_Source.LocalSource_Factory<>((PoolSummary_Missing))));
        // NetTransferSet ==============================================================================================
        dataObjectSchema.add(new ListDataField_Schema<>(Bank_Summary_NetTransferSet, HalfTransferList.class));
        dataObjectSchema.get(Bank_Summary_NetTransferSet).getDisplayProperties().setVerbosityLevel(TRACE_DISPLAY);
        dataObjectSchema.<List<HalfTransfer>>get(Bank_Summary_NetTransferSet).setDataCore_factory(
                new Children_ListDataCore.Children_ListDataCore_Factory<>(
                        HalfTransfer.class,
                        new TransferType_HalfTransfer_Filter(BankTransfer.class,
                                new TransferDestination_HalfTransfer_Filter(Bank.class)),
                        new Children_ListDataCore.ParentAccess.ParentAccess_Factory<>((PoolSummary_Period)),
                        new Children_ListDataCore.ParentAccess.ParentAccess_Factory<>((PoolSummary_Pool))));
        // NetTransferSum ==============================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Bank_Summary_NetTransferSum, Double.class, true));
        dataObjectSchema.get(Bank_Summary_NetTransferSum).getDisplayProperties().setVerbosityLevel(TRACE_DISPLAY);
        dataObjectSchema.<Double>get(Bank_Summary_NetTransferSum).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<Double, Bank_Summary>(
                        container -> {
                            double sum = 0.0;
                            for (HalfTransfer halfTransfer : container.<List<HalfTransfer>>get(Bank_Summary_NetTransferSet)) {
                                sum += halfTransfer.getValue() * halfTransfer.getCurrency().getToPrimary();
                            }
                            return Currency.round(sum);
                        }
                        , new List_Source.ListSource_Factory<>(
                        Bank_Summary_NetTransferSet,
                        HalfTransfer_Value,
                        HalfTransfer_Currency
                )));
        // NetTransfer =================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Bank_Summary_NetTransfer, Double.class));
        dataObjectSchema.get(Bank_Summary_NetTransfer).getDisplayProperties().setDataType(CURRENCY);
        dataObjectSchema.<Double>get(Bank_Summary_NetTransfer).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>(
                        (Derived_DataCore.Calculator<Double, Bank_Summary>) container ->
                                container.getNetTransferSum() / container.getCurrency().getToPrimary()
                        , new Local_Source.LocalSource_Factory<>((Bank_Summary_NetTransferSum))
                        , new External_Source.ExternalSource_Factory<>((Bank_Summary_Currency), Currency_ToPrimary)));
        // SpendSet ====================================================================================================
        dataObjectSchema.add(new ListDataField_Schema<>(Bank_Summary_SpendSet, HalfTransferList.class));
        dataObjectSchema.get(Bank_Summary_SpendSet).getDisplayProperties().setVerbosityLevel(TRACE_DISPLAY);
        dataObjectSchema.<List<HalfTransfer>>get(Bank_Summary_SpendSet).setDataCore_factory(
                new Children_ListDataCore.Children_ListDataCore_Factory<>(
                        HalfTransfer.class,
                        new TransferType_HalfTransfer_Filter(BankTransfer.class,
                                new TransferDestination_HalfTransfer_Filter(SolidCategory.class)),
                        new Children_ListDataCore.ParentAccess.ParentAccess_Factory<>((PoolSummary_Period)),
                        new Children_ListDataCore.ParentAccess.ParentAccess_Factory<>((PoolSummary_Pool))));
        // SpendSum ====================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Bank_Summary_SpendSum, Double.class, true));
        dataObjectSchema.get(Bank_Summary_SpendSum).getDisplayProperties().setVerbosityLevel(TRACE_DISPLAY);
        dataObjectSchema.<Double>get(Bank_Summary_SpendSum).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<Double, Bank_Summary>(
                        container -> {
                            double sum = 0.0;
                            for (HalfTransfer halfTransfer : container.<List<HalfTransfer>>get(Bank_Summary_SpendSet)) {
                                sum += halfTransfer.getValue() * halfTransfer.getCurrency().getToPrimary();
                            }
                            return Currency.round(sum);
                        }
                        , new List_Source.ListSource_Factory<>(
                        Bank_Summary_SpendSet,
                        HalfTransfer_Value,
                        HalfTransfer_Currency)));
        // Spend =======================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Bank_Summary_Spend, Double.class));
        dataObjectSchema.get(Bank_Summary_Spend).getDisplayProperties().setDataType(CURRENCY);
        dataObjectSchema.<Double>get(Bank_Summary_Spend).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>(
                        (Derived_DataCore.Calculator<Double, Bank_Summary>) container ->
                                container.getSpendSum() / container.getCurrency().getToPrimary()
                        , new Local_Source.LocalSource_Factory<>((Bank_Summary_SpendSum))
                        , new External_Source.ExternalSource_Factory<>((Bank_Summary_Currency), Currency_ToPrimary)));
        // Order =======================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Bank_Summary_Order, Integer.class));
        dataObjectSchema.get(Bank_Summary_Order).getDisplayProperties().setVerbosityLevel(TRACE_DISPLAY);
        dataObjectSchema.<Integer>get(Bank_Summary_Order).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>(
                        (Derived_DataCore.Calculator<Integer, Bank_Summary>) container ->
                                container.getPeriod().getOrder() * 10000 + container.getPool().getOrder()
                        , new External_Source.ExternalSource_Factory<>((PoolSummary_Pool), Bank_Order)
                        , new External_Source.ExternalSource_Factory<>((PoolSummary_Period), ExistingPeriod_Order)));
        //==============================================================================================================
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(Bank_Summary.class);
    }

    /**
     * Create a new StatementEnd object
     */
    public static Bank_Summary make(Integer id, Period period, Pool pool) {
        Database database = period.getTrackingDatabase();
        Database_Schema database_schema = database.getSchema();
        return assembleDataObject(database, database_schema.getClassSchema(Bank_Summary.class), new Bank_Summary()
                , DataObject_Id, id
                , PoolSummary_Period, period
                , PoolSummary_Pool, pool
        );
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public List<Bank_Summary> getBankSummarySet() {
        return get(Bank_Summary_BankSummarySet);
    }

    public Bank_Summary getPreviousBankSummary() {
        return get(Bank_Summary_PreviousBankSummary);
    }

    public StatementEnd getStatementEnd() {
        return get(Bank_Summary_StatementEnd);
    }

    public Double getNetTransferSum() {
        return get(Bank_Summary_NetTransferSum);
    }

    public Double getNetTransfer() {
        return get(Bank_Summary_NetTransfer);
    }

    public Double getSpendSum() {
        return get(Bank_Summary_SpendSum);
    }

    public Double getSpend() {
        return get(Bank_Summary_Spend);
    }

    public Integer getOrder() {
        return get(Bank_Summary_Order);
    }

    @Override
    public Currency getCurrency() {
        return get(Bank_Summary_Currency);
    }

    public List<StatementEnd> getStatementEndSet() {
        return get(Bank_Summary_StatementEndSet);
    }
}
