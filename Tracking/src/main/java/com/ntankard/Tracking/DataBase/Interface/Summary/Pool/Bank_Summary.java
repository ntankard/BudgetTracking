package com.ntankard.Tracking.DataBase.Interface.Summary.Pool;

import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.CurrencyBound;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.SolidCategory;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Core.StatementEnd;
import com.ntankard.Tracking.DataBase.Core.StatementEnd.StatementEndList;
import com.ntankard.Tracking.DataBase.Core.Transfer.Bank.BankTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfer.HalfTransfer;
import com.ntankard.Tracking.DataBase.Interface.Set.Filter.TransferDestination_HalfTransfer_Filter;
import com.ntankard.Tracking.DataBase.Interface.Set.Filter.TransferType_HalfTransfer_Filter;
import com.ntankard.javaObjectDatabase.CoreObject.Factory.DoubleParentFactory;
import com.ntankard.javaObjectDatabase.CoreObject.Field.DataField;
import com.ntankard.javaObjectDatabase.CoreObject.Field.ListDataField;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.Children_ListDataCore;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.Derived_DataCore;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.source.DirectExternalSource;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.source.ExternalSource;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.source.ListSource;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.source.LocalSource;
import com.ntankard.javaObjectDatabase.CoreObject.FieldContainer;
import com.ntankard.javaObjectDatabase.CoreObject.Interface.Ordered;
import com.ntankard.javaObjectDatabase.Database.ParameterMap;
import com.ntankard.javaObjectDatabase.Database.TrackingDatabase;

import java.util.List;

import static com.ntankard.Tracking.DataBase.Core.Currency.Currency_ToPrimary;
import static com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod.ExistingPeriod_Order;
import static com.ntankard.Tracking.DataBase.Core.Pool.Bank.*;
import static com.ntankard.Tracking.DataBase.Core.StatementEnd.StatementEnd_End;
import static com.ntankard.Tracking.DataBase.Core.Transfer.HalfTransfer.*;
import static com.ntankard.javaObjectDatabase.CoreObject.Field.Properties.Display_Properties.DataType.CURRENCY;
import static com.ntankard.javaObjectDatabase.CoreObject.Field.Properties.Display_Properties.INFO_DISPLAY;
import static com.ntankard.javaObjectDatabase.CoreObject.Field.Properties.Display_Properties.TRACE_DISPLAY;

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
            PoolSummary_Period, (generator, secondaryGenerator) -> Bank_Summary.make(TrackingDatabase.get().getNextId(), secondaryGenerator, generator)
    );

    /**
     * Get all the fields for this object
     */
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = PoolSummary.getFieldContainer();

        // Class behavior
        fieldContainer.setMyFactory(Factory);

        // ID
        // Period
        // Pool
        // BankSummarySet ==============================================================================================
        fieldContainer.add(new ListDataField<>(Bank_Summary_BankSummarySet, Bank_SummaryList.class));
        fieldContainer.get(Bank_Summary_BankSummarySet).getDisplayProperties().setVerbosityLevel(TRACE_DISPLAY);
        fieldContainer.<List<Bank_Summary>>get(Bank_Summary_BankSummarySet).setDataCore_factory(
                new Children_ListDataCore.Children_ListDataCore_Factory<>(
                        Bank_Summary.class,
                        new Children_ListDataCore.ParentAccess.ParentAccess_Factory<>((PoolSummary_Pool))));
        // PreviousBankSummary =========================================================================================
        fieldContainer.add(new DataField<>(Bank_Summary_PreviousBankSummary, Bank_Summary.class, true));
        fieldContainer.get(Bank_Summary_PreviousBankSummary).getDisplayProperties().setVerbosityLevel(TRACE_DISPLAY);
        fieldContainer.<Bank_Summary>get(Bank_Summary_PreviousBankSummary).setDataCore_factory(
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
                        , new LocalSource.LocalSource_Factory<>((Bank_Summary_BankSummarySet)))); // TODO you need to watch period
        // StatementEndSet ================================================================================================
        fieldContainer.add(new ListDataField<>(Bank_Summary_StatementEndSet, StatementEndList.class));
        fieldContainer.get(Bank_Summary_StatementEndSet).getDisplayProperties().setVerbosityLevel(TRACE_DISPLAY);
        fieldContainer.<List<StatementEnd>>get(Bank_Summary_StatementEndSet).setDataCore_factory(
                new Children_ListDataCore.Children_ListDataCore_Factory<>(
                        StatementEnd.class,
                        new Children_ListDataCore.ParentAccess.ParentAccess_Factory<>((PoolSummary_Period)),
                        new Children_ListDataCore.ParentAccess.ParentAccess_Factory<>((PoolSummary_Pool))));
        // StatementEnd ================================================================================================`
        fieldContainer.add(new DataField<>(Bank_Summary_StatementEnd, StatementEnd.class));
        fieldContainer.<StatementEnd>get(Bank_Summary_StatementEnd).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<StatementEnd, Bank_Summary>(
                        container -> {
                            if (container.getStatementEndSet().isEmpty()) {
                                return null;
                            }
                            return container.getStatementEndSet().get(0);
                        },
                        new LocalSource.LocalSource_Factory<>((Bank_Summary_StatementEndSet))));
        // Currency ====================================================================================================
        fieldContainer.add(new DataField<>(Bank_Summary_Currency, Currency.class, false));
        fieldContainer.get(Bank_Summary_Currency).getDisplayProperties().setVerbosityLevel(INFO_DISPLAY);
        fieldContainer.<Currency>get(Bank_Summary_Currency).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>(
                        new DirectExternalSource.DirectExternalSource_Factory<>(
                                (PoolSummary_Pool), Bank_Currency)));
        // Start =======================================================================================================
        fieldContainer.<Double>get(PoolSummary_Start).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>(
                        (Derived_DataCore.Calculator<Double, Bank_Summary>) container -> {
                            Bank_Summary previous = container.getPreviousBankSummary();
                            if (previous == null) {
                                return container.getPool().getStart();
                            }
                            return previous.getStatementEnd().getEnd();
                        }
                        , new ExternalSource.ExternalSource_Factory<>((Bank_Summary_PreviousBankSummary), Bank_Summary_StatementEnd)
                        , new ExternalSource.ExternalSource_Factory<>((PoolSummary_Pool), Bank_Start)));
        // End =========================================================================================================
        fieldContainer.<Double>get(PoolSummary_End).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<Double, Bank_Summary>(
                        container -> container.getStatementEnd().getEnd()
                        , new ExternalSource.ExternalSource_Factory<>((Bank_Summary_StatementEnd), StatementEnd_End)));
        fieldContainer.<Double>get(PoolSummary_End).setSetterFunction((toSet, container) -> {
            ((Bank_Summary) container).getStatementEnd().setEnd(toSet);
        });
        // TransferSum =================================================================================================
        fieldContainer.<Double>get(PoolSummary_TransferSum).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>(
                        (Derived_DataCore.Calculator<Double, PoolSummary<Bank>>) container ->
                                container.getTransferSetSum() / container.getCurrency().getToPrimary()
                        , new LocalSource.LocalSource_Factory<>((PoolSummary_TransferSetSum))
                        , new ExternalSource.ExternalSource_Factory<>((Bank_Summary_Currency), Currency_ToPrimary)));
        // Missing
        // Valid =======================================================================================================
        fieldContainer.<Boolean>get(PoolSummary_Valid).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>(
                        (Derived_DataCore.Calculator<Boolean, PoolSummary<Bank>>) container -> container.getMissing().equals(0.00)
                        , new LocalSource.LocalSource_Factory<>((PoolSummary_Missing))));
        // NetTransferSet ==============================================================================================
        fieldContainer.add(new ListDataField<>(Bank_Summary_NetTransferSet, HalfTransferList.class));
        fieldContainer.get(Bank_Summary_NetTransferSet).getDisplayProperties().setVerbosityLevel(TRACE_DISPLAY);
        fieldContainer.<List<HalfTransfer>>get(Bank_Summary_NetTransferSet).setDataCore_factory(
                new Children_ListDataCore.Children_ListDataCore_Factory<>(
                        HalfTransfer.class,
                        new TransferType_HalfTransfer_Filter(BankTransfer.class,
                                new TransferDestination_HalfTransfer_Filter(Bank.class)),
                        new Children_ListDataCore.ParentAccess.ParentAccess_Factory<>((PoolSummary_Period)),
                        new Children_ListDataCore.ParentAccess.ParentAccess_Factory<>((PoolSummary_Pool))));
        // NetTransferSum ==============================================================================================
        fieldContainer.add(new DataField<>(Bank_Summary_NetTransferSum, Double.class, true));
        fieldContainer.get(Bank_Summary_NetTransferSum).getDisplayProperties().setVerbosityLevel(TRACE_DISPLAY);
        fieldContainer.<Double>get(Bank_Summary_NetTransferSum).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<Double, Bank_Summary>(
                        container -> {
                            double sum = 0.0;
                            for (HalfTransfer halfTransfer : container.<List<HalfTransfer>>get(Bank_Summary_NetTransferSet)) {
                                sum += halfTransfer.getValue() * halfTransfer.getCurrency().getToPrimary();
                            }
                            return Currency.round(sum);
                        }
                        , new ListSource.ListSource_Factory<>(
                        Bank_Summary_NetTransferSet,
                        HalfTransfer_Value,
                        HalfTransfer_Currency
                )));
        // NetTransfer =================================================================================================
        fieldContainer.add(new DataField<>(Bank_Summary_NetTransfer, Double.class));
        fieldContainer.get(Bank_Summary_NetTransfer).getDisplayProperties().setDataType(CURRENCY);
        fieldContainer.<Double>get(Bank_Summary_NetTransfer).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>(
                        (Derived_DataCore.Calculator<Double, Bank_Summary>) container ->
                                container.getNetTransferSum() / container.getCurrency().getToPrimary()
                        , new LocalSource.LocalSource_Factory<>((Bank_Summary_NetTransferSum))
                        , new ExternalSource.ExternalSource_Factory<>((Bank_Summary_Currency), Currency_ToPrimary)));
        // SpendSet ====================================================================================================
        fieldContainer.add(new ListDataField<>(Bank_Summary_SpendSet, HalfTransferList.class));
        fieldContainer.get(Bank_Summary_SpendSet).getDisplayProperties().setVerbosityLevel(TRACE_DISPLAY);
        fieldContainer.<List<HalfTransfer>>get(Bank_Summary_SpendSet).setDataCore_factory(
                new Children_ListDataCore.Children_ListDataCore_Factory<>(
                        HalfTransfer.class,
                        new TransferType_HalfTransfer_Filter(BankTransfer.class,
                                new TransferDestination_HalfTransfer_Filter(SolidCategory.class)),
                        new Children_ListDataCore.ParentAccess.ParentAccess_Factory<>((PoolSummary_Period)),
                        new Children_ListDataCore.ParentAccess.ParentAccess_Factory<>((PoolSummary_Pool))));
        // SpendSum ====================================================================================================
        fieldContainer.add(new DataField<>(Bank_Summary_SpendSum, Double.class, true));
        fieldContainer.get(Bank_Summary_SpendSum).getDisplayProperties().setVerbosityLevel(TRACE_DISPLAY);
        fieldContainer.<Double>get(Bank_Summary_SpendSum).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<Double, Bank_Summary>(
                        container -> {
                            double sum = 0.0;
                            for (HalfTransfer halfTransfer : container.<List<HalfTransfer>>get(Bank_Summary_SpendSet)) {
                                sum += halfTransfer.getValue() * halfTransfer.getCurrency().getToPrimary();
                            }
                            return Currency.round(sum);
                        }
                        , new ListSource.ListSource_Factory<>(
                        Bank_Summary_SpendSet,
                        HalfTransfer_Value,
                        HalfTransfer_Currency)));
        // Spend =======================================================================================================
        fieldContainer.add(new DataField<>(Bank_Summary_Spend, Double.class));
        fieldContainer.get(Bank_Summary_Spend).getDisplayProperties().setDataType(CURRENCY);
        fieldContainer.<Double>get(Bank_Summary_Spend).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>(
                        (Derived_DataCore.Calculator<Double, Bank_Summary>) container ->
                                container.getSpendSum() / container.getCurrency().getToPrimary()
                        , new LocalSource.LocalSource_Factory<>((Bank_Summary_SpendSum))
                        , new ExternalSource.ExternalSource_Factory<>((Bank_Summary_Currency), Currency_ToPrimary)));
        // Order =======================================================================================================
        fieldContainer.add(new DataField<>(Bank_Summary_Order, Integer.class));
        fieldContainer.get(Bank_Summary_Order).getDisplayProperties().setVerbosityLevel(TRACE_DISPLAY);
        fieldContainer.<Integer>get(Bank_Summary_Order).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>(
                        (Derived_DataCore.Calculator<Integer, Bank_Summary>) container ->
                                container.getPeriod().getOrder() * 10000 + container.getPool().getOrder()
                        , new ExternalSource.ExternalSource_Factory<>((PoolSummary_Pool), Bank_Order)
                        , new ExternalSource.ExternalSource_Factory<>((PoolSummary_Period), ExistingPeriod_Order)));
        //==============================================================================================================
        // Parents
        // Children

        return fieldContainer.finaliseContainer(Bank_Summary.class);
    }

    /**
     * Create a new StatementEnd object
     */
    public static Bank_Summary make(Integer id, Period period, Pool pool) {
        return assembleDataObject(Bank_Summary.getFieldContainer(), new Bank_Summary()
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
