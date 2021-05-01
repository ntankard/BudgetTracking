package com.ntankard.tracking.dataBase.interfaces.summary.pool;

import com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema.Calculator;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.*;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.end.End_Source_Schema;
import com.ntankard.javaObjectDatabase.util.set.SetFilter;
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
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.dataObject.interfaces.Ordered;
import com.ntankard.javaObjectDatabase.database.ParameterMap;
import com.ntankard.javaObjectDatabase.database.Database;

import java.util.List;

import static com.ntankard.javaObjectDatabase.dataField.dataCore.DataCore_Factory.createMultiParentList;
import static com.ntankard.tracking.dataBase.core.Currency.Currency_ToPrimary;
import static com.ntankard.tracking.dataBase.core.period.ExistingPeriod.ExistingPeriod_Order;
import static com.ntankard.tracking.dataBase.core.pool.Bank.*;
import static com.ntankard.tracking.dataBase.core.StatementEnd.StatementEnd_End;
import static com.ntankard.tracking.dataBase.core.transfer.HalfTransfer.*;
import static com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties.DataType.CURRENCY;
import static com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties.INFO_DISPLAY;
import static com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties.TRACE_DISPLAY;

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
            PoolSummary_Period, (generator, secondaryGenerator) -> new Bank_Summary(secondaryGenerator, generator)
    );

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = PoolSummary.getDataObjectSchema();

        // Class behavior
        dataObjectSchema.setMyFactory(Factory);

        // ID
        // Period
        // Pool
        // BankSummarySet ==============================================================================================
        dataObjectSchema.add(new ListDataField_Schema<>(Bank_Summary_BankSummarySet, Bank_SummaryList.class));
        dataObjectSchema.get(Bank_Summary_BankSummarySet).getProperty(Display_Properties.class).setVerbosityLevel(TRACE_DISPLAY);
        dataObjectSchema.<List<Bank_Summary>>get(Bank_Summary_BankSummarySet).setDataCore_schema(
                createMultiParentList(
                        Bank_Summary.class,
                        PoolSummary_Pool));
        // PreviousBankSummary =========================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Bank_Summary_PreviousBankSummary, Bank_Summary.class, true));
        dataObjectSchema.get(Bank_Summary_PreviousBankSummary).getProperty(Display_Properties.class).setVerbosityLevel(TRACE_DISPLAY);
        dataObjectSchema.<Bank_Summary>get(Bank_Summary_PreviousBankSummary).setDataCore_schema(
                new Derived_DataCore_Schema<>(
                        (Calculator<Bank_Summary, Bank_Summary>) container -> {
                            List<Bank_Summary> bank_summaries = container.getBankSummarySet();
                            for (Bank_Summary bankSummary : bank_summaries) {
                                if (bankSummary.getPeriod().getOrder() == container.getPeriod().getOrder() - 1) {
                                    return bankSummary;
                                }
                            }
                            return null;
                        }
                        , new End_Source_Schema<>((Bank_Summary_BankSummarySet)))); // TODO you need to watch period
        // StatementEndSet ================================================================================================
        dataObjectSchema.add(new ListDataField_Schema<>(Bank_Summary_StatementEndSet, StatementEndList.class));
        dataObjectSchema.get(Bank_Summary_StatementEndSet).getProperty(Display_Properties.class).setVerbosityLevel(TRACE_DISPLAY);
        dataObjectSchema.<List<StatementEnd>>get(Bank_Summary_StatementEndSet).setDataCore_schema(
                createMultiParentList(
                        StatementEnd.class,
                        PoolSummary_Period,
                        PoolSummary_Pool));
        // StatementEnd ================================================================================================`
        dataObjectSchema.add(new DataField_Schema<>(Bank_Summary_StatementEnd, StatementEnd.class, true));
        dataObjectSchema.<StatementEnd>get(Bank_Summary_StatementEnd).setDataCore_schema(
                new Derived_DataCore_Schema<StatementEnd, Bank_Summary>(
                        container -> {
                            if (container.getStatementEndSet().isEmpty()) {
                                return null;
                            }
                            return container.getStatementEndSet().get(0);
                        },
                        new End_Source_Schema<>((Bank_Summary_StatementEndSet))));
        // Currency ====================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Bank_Summary_Currency, Currency.class, false));
        dataObjectSchema.get(Bank_Summary_Currency).getProperty(Display_Properties.class).setVerbosityLevel(INFO_DISPLAY);
        dataObjectSchema.<Currency>get(Bank_Summary_Currency).setDataCore_schema(
                new Derived_DataCore_Schema<Currency, Bank_Summary>
                        (dataObject -> dataObject.getPool().getCurrency()
                                , Source_Factory.makeSourceChain(PoolSummary_Pool, Bank_Currency)));
        // Start =======================================================================================================
        dataObjectSchema.<Double>get(PoolSummary_Start).setDataCore_schema(
                new Derived_DataCore_Schema<>(
                        (Calculator<Double, Bank_Summary>) container -> {
                            Bank_Summary previous = container.getPreviousBankSummary();
                            if (previous == null) {
                                return container.getPool().getStart();
                            }
                            return previous.getStatementEnd().getEnd();
                        }
                        , Source_Factory.makeSourceChain((Bank_Summary_PreviousBankSummary), Bank_Summary_StatementEnd)
                        , Source_Factory.makeSourceChain((PoolSummary_Pool), Bank_Start)));
        // End =========================================================================================================
        dataObjectSchema.<Double>get(PoolSummary_End).setDataCore_schema(
                new Derived_DataCore_Schema<Double, Bank_Summary>(
                        container -> {
                            if (container.getStatementEnd() == null) { // TODO this is needed because you allow statement end to be null. If you don't it fails the initial check. Look at fixing this
                                return -1.0;
                            }
                            return container.getStatementEnd().getEnd();
                        }
                        , Source_Factory.makeSourceChain((Bank_Summary_StatementEnd), StatementEnd_End)));
        dataObjectSchema.<Double>get(PoolSummary_End).setSetterFunction((toSet, container) -> {
            ((Bank_Summary) container).getStatementEnd().setEnd(toSet);
        });
        // TransferSum =================================================================================================
        dataObjectSchema.<Double>get(PoolSummary_TransferSum).setDataCore_schema(
                new Derived_DataCore_Schema<>(
                        (Calculator<Double, PoolSummary<Bank>>) container ->
                                container.getTransferSetSum() / container.getCurrency().getToPrimary()
                        , new End_Source_Schema<>((PoolSummary_TransferSetSum))
                        , Source_Factory.makeSourceChain((Bank_Summary_Currency), Currency_ToPrimary)));
        // Missing
        // Valid =======================================================================================================
        dataObjectSchema.<Boolean>get(PoolSummary_Valid).setDataCore_schema(
                new Derived_DataCore_Schema<>(
                        (Calculator<Boolean, PoolSummary<Bank>>) container -> container.getMissing().equals(0.00)
                        , new End_Source_Schema<>((PoolSummary_Missing))));
        // NetTransferSet ==============================================================================================
        dataObjectSchema.add(new ListDataField_Schema<>(Bank_Summary_NetTransferSet, HalfTransferList.class));
        dataObjectSchema.get(Bank_Summary_NetTransferSet).getProperty(Display_Properties.class).setVerbosityLevel(TRACE_DISPLAY);
        dataObjectSchema.<List<HalfTransfer>>get(Bank_Summary_NetTransferSet).setDataCore_schema(
                createMultiParentList(
                        HalfTransfer.class,
                        new TransferType_HalfTransfer_Filter(BankTransfer.class,
                                new TransferDestination_HalfTransfer_Filter(Bank.class)), PoolSummary_Period,
                        PoolSummary_Pool
                ));
        // NetTransferSum ==============================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Bank_Summary_NetTransferSum, Double.class, true));
        dataObjectSchema.get(Bank_Summary_NetTransferSum).getProperty(Display_Properties.class).setVerbosityLevel(TRACE_DISPLAY);
        dataObjectSchema.<Double>get(Bank_Summary_NetTransferSum).setDataCore_schema(
                new Derived_DataCore_Schema<>(
                        container -> {
                            double sum = 0.0;
                            for (HalfTransfer halfTransfer : container.<List<HalfTransfer>>get(Bank_Summary_NetTransferSet)) {
                                sum += halfTransfer.getValue() * halfTransfer.getCurrency().getToPrimary();
                            }
                            return Currency.round(sum);
                        },
                        Source_Factory.makeSharedStepSourceChain(Bank_Summary_NetTransferSet, HalfTransfer_Value, HalfTransfer_Currency)
                ));
        // NetTransfer =================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Bank_Summary_NetTransfer, Double.class));
        dataObjectSchema.get(Bank_Summary_NetTransfer).getProperty(Display_Properties.class).setDataType(CURRENCY);
        dataObjectSchema.<Double>get(Bank_Summary_NetTransfer).setDataCore_schema(
                new Derived_DataCore_Schema<>(
                        (Calculator<Double, Bank_Summary>) container ->
                                container.getNetTransferSum() / container.getCurrency().getToPrimary()
                        , new End_Source_Schema<>((Bank_Summary_NetTransferSum))
                        , Source_Factory.makeSourceChain((Bank_Summary_Currency), Currency_ToPrimary)));
        // SpendSet ====================================================================================================
        dataObjectSchema.add(new ListDataField_Schema<>(Bank_Summary_SpendSet, HalfTransferList.class));
        dataObjectSchema.get(Bank_Summary_SpendSet).getProperty(Display_Properties.class).setVerbosityLevel(TRACE_DISPLAY);
        SetFilter<HalfTransfer> setFilter = new TransferType_HalfTransfer_Filter(BankTransfer.class,
                new SetFilter<HalfTransfer>(null) {
                    @Override
                    protected boolean shouldAdd_Impl(HalfTransfer dataObject) {
                        return !Bank.class.isAssignableFrom(dataObject.getTransfer().getDestination().getClass());
                    }
                });
        dataObjectSchema.<List<HalfTransfer>>get(Bank_Summary_SpendSet).setDataCore_schema(createMultiParentList(HalfTransfer.class, setFilter, PoolSummary_Period, PoolSummary_Pool));
        // SpendSum ====================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Bank_Summary_SpendSum, Double.class, true));
        dataObjectSchema.get(Bank_Summary_SpendSum).getProperty(Display_Properties.class).setVerbosityLevel(TRACE_DISPLAY);
        dataObjectSchema.<Double>get(Bank_Summary_SpendSum).setDataCore_schema(
                new Derived_DataCore_Schema<Double, Bank_Summary>(
                        container -> {
                            double sum = 0.0;
                            for (HalfTransfer halfTransfer : container.<List<HalfTransfer>>get(Bank_Summary_SpendSet)) {
                                sum += halfTransfer.getValue() * halfTransfer.getCurrency().getToPrimary();
                            }
                            return Currency.round(sum);
                        }
                        , Source_Factory.makeSharedStepSourceChain(
                        Bank_Summary_SpendSet,
                        HalfTransfer_Value,
                        HalfTransfer_Currency)));
        // Spend =======================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Bank_Summary_Spend, Double.class));
        dataObjectSchema.get(Bank_Summary_Spend).getProperty(Display_Properties.class).setDataType(CURRENCY);
        dataObjectSchema.<Double>get(Bank_Summary_Spend).setDataCore_schema(
                new Derived_DataCore_Schema<>(
                        (Calculator<Double, Bank_Summary>) container ->
                                container.getSpendSum() / container.getCurrency().getToPrimary()
                        , new End_Source_Schema<>((Bank_Summary_SpendSum))
                        , Source_Factory.makeSourceChain((Bank_Summary_Currency), Currency_ToPrimary)));
        // Order =======================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Bank_Summary_Order, Integer.class));
        dataObjectSchema.get(Bank_Summary_Order).getProperty(Display_Properties.class).setVerbosityLevel(TRACE_DISPLAY);
        dataObjectSchema.<Integer>get(Bank_Summary_Order).setDataCore_schema(
                new Derived_DataCore_Schema<>(
                        (Calculator<Integer, Bank_Summary>) container ->
                                container.getPeriod().getOrder() * 10000 + container.getPool().getOrder()
                        , Source_Factory.makeSourceChain((PoolSummary_Pool), Bank_Order)
                        , Source_Factory.makeSourceChain((PoolSummary_Period), ExistingPeriod_Order)));
        //==============================================================================================================
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(Bank_Summary.class);
    }

    /**
     * Constructor
     */
    public Bank_Summary(Database database) {
        super(database);
    }

    /**
     * Constructor
     */
    public Bank_Summary(Period period, Pool pool) {
        this(period.getTrackingDatabase());
        setAllValues(DataObject_Id, getTrackingDatabase().getNextId()
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
