package com.ntankard.Tracking.DataBase.Interface.Summary;

import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.CurrencyBound;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Period.VirtualPeriod;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank;
import com.ntankard.Tracking.DataBase.Core.Transfer.Bank.BankTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfer.HalfTransfer;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.Bank_Summary;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.Bank_Summary.Bank_SummaryList;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.Category_Summary;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.Category_Summary.Category_SummaryList;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.FundEvent_Summary;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.FundEvent_Summary.FundEvent_SummaryList;
import com.ntankard.javaObjectDatabase.CoreObject.DataObject;
import com.ntankard.javaObjectDatabase.CoreObject.Factory.SingleParentFactory;
import com.ntankard.javaObjectDatabase.CoreObject.Field.DataField;
import com.ntankard.javaObjectDatabase.CoreObject.Field.ListDataField;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.Children_ListDataCore;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.Derived_DataCore;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.source.ExternalSource;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.source.ListSource;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.source.LocalSource;
import com.ntankard.javaObjectDatabase.CoreObject.FieldContainer;
import com.ntankard.javaObjectDatabase.CoreObject.Interface.Ordered;
import com.ntankard.javaObjectDatabase.Database.ParameterMap;
import com.ntankard.javaObjectDatabase.Database.TrackingDatabase;
import com.ntankard.javaObjectDatabase.util.OneParent_Children_Set;
import com.ntankard.javaObjectDatabase.util.TwoParent_Children_Set;

import java.util.List;

import static com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod.ExistingPeriod_Order;
import static com.ntankard.Tracking.DataBase.Core.Transfer.Transfer.*;
import static com.ntankard.Tracking.DataBase.Interface.Summary.Pool.Bank_Summary.Bank_Summary_Currency;
import static com.ntankard.Tracking.DataBase.Interface.Summary.Pool.PoolSummary.*;
import static com.ntankard.javaObjectDatabase.CoreObject.Field.Properties.Display_Properties.DEBUG_DISPLAY;
import static com.ntankard.javaObjectDatabase.CoreObject.Field.Properties.Display_Properties.DataContext.*;
import static com.ntankard.javaObjectDatabase.CoreObject.Field.Properties.Display_Properties.DataType.CURRENCY;
import static com.ntankard.javaObjectDatabase.CoreObject.Field.Properties.Display_Properties.TRACE_DISPLAY;

@ParameterMap(shouldSave = false)
public class Period_Summary extends DataObject implements CurrencyBound, Ordered {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String Period_Summary_Period = "getPeriod";
    public static final String Period_Summary_BankSummarySet = "getBankSummarySet";
    public static final String Period_Summary_FundEventSummarySet = "getFundEventSummarySet";
    public static final String Period_Summary_CategorySummarySet = "getCategorySummarySet";
    public static final String Period_Summary_BankStart = "getBankStart";
    public static final String Period_Summary_BankEnd = "getBankEnd";
    public static final String Period_Summary_BankDelta = "getBankDelta";
    public static final String Period_Summary_CategoryDelta = "getCategoryDelta";
    public static final String Period_Summary_FundEventStart = "getFundEventStart";
    public static final String Period_Summary_FundEventEnd = "getFundEventEnd";
    public static final String Period_Summary_FundEventDelta = "getFundEventDelta";
    public static final String Period_Summary_BankSummaryValid = "getBankSummaryValid";
    public static final String Period_Summary_FundEventSummaryValid = "getFundEventSummaryValid";
    public static final String Period_Summary_CategorySummaryValid = "getCategorySummaryValid";
    public static final String Period_Summary_AllSummaryValid = "isAllSummaryValid";
    public static final String Period_Summary_CategoryClear = "isCategoryClear";
    public static final String Period_Summary_BankTransferSet = "getBankTransferSet";
    public static final String Period_Summary_CurrencyValueLoss = "getCurrencyValueLoss";
    public static final String Period_Summary_ExchangeRate = "getExchangeRate";
    public static final String Period_Summary_ExchangeRateAcceptable = "isExchangeRateAcceptable";
    public static final String Period_Summary_Valid = "isValid";
    public static final String Period_Summary_Order = "getOrder";

    public static SingleParentFactory<?, ?> Factory = new SingleParentFactory<>(Period_Summary.class, Period.class, Period_Summary::make);

    /**
     * Get all the fields for this object
     */
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = DataObject.getFieldContainer();

        // Class behavior
        fieldContainer.setMyFactory(Factory);

        // ID
        // Period ======================================================================================================
        fieldContainer.add(new DataField<>(Period_Summary_Period, Period.class));
        // BankSummarySet ==============================================================================================
        fieldContainer.add(new ListDataField<>(Period_Summary_BankSummarySet, Bank_SummaryList.class));
        fieldContainer.get(Period_Summary_BankSummarySet).getDisplayProperties().setVerbosityLevel(TRACE_DISPLAY);
        fieldContainer.<List<Bank_Summary>>get(Period_Summary_BankSummarySet).setDataCore(
                new Children_ListDataCore<>(
                        Bank_Summary.class,
                        new Children_ListDataCore.ParentAccess<>(fieldContainer.get(Period_Summary_Period))));
        // FundEventSummarySet =========================================================================================
        fieldContainer.add(new ListDataField<>(Period_Summary_FundEventSummarySet, FundEvent_SummaryList.class));
        fieldContainer.get(Period_Summary_FundEventSummarySet).getDisplayProperties().setVerbosityLevel(TRACE_DISPLAY);
        fieldContainer.<List<FundEvent_Summary>>get(Period_Summary_FundEventSummarySet).setDataCore(
                new Children_ListDataCore<>(
                        FundEvent_Summary.class,
                        new Children_ListDataCore.ParentAccess<>(fieldContainer.get(Period_Summary_Period))));
        // CategorySummarySet ==========================================================================================
        fieldContainer.add(new ListDataField<>(Period_Summary_CategorySummarySet, Category_SummaryList.class));
        fieldContainer.get(Period_Summary_CategorySummarySet).getDisplayProperties().setVerbosityLevel(TRACE_DISPLAY);
        fieldContainer.<List<Category_Summary>>get(Period_Summary_CategorySummarySet).setDataCore(
                new Children_ListDataCore<>(
                        Category_Summary.class,
                        new Children_ListDataCore.ParentAccess<>(fieldContainer.get(Period_Summary_Period))));
        // BankStart ===================================================================================================
        fieldContainer.add(new DataField<>(Period_Summary_BankStart, Double.class, true));
        fieldContainer.get(Period_Summary_BankStart).getDisplayProperties().setVerbosityLevel(DEBUG_DISPLAY);
        fieldContainer.get(Period_Summary_BankStart).getDisplayProperties().setDataType(CURRENCY);
        fieldContainer.<Double>get(Period_Summary_BankStart).setDataCore(
                new Derived_DataCore<Double, Period_Summary>(
                        container -> {
                            double sum = 0.0;
                            for (Bank_Summary bankSummary : container.getBankSummarySet()) {
                                sum += bankSummary.getStart() * bankSummary.getCurrency().getToPrimary();
                            }
                            return Currency.round(sum);
                        }
                        , new ListSource<>(
                        (ListDataField<Bank_Summary>) fieldContainer.<List<Bank_Summary>>get(Period_Summary_BankSummarySet),
                        PoolSummary_Start,
                        Bank_Summary_Currency // TODO possible problem here, we have a 3 layer nested dependency. getToPrimary
                )));
        // BankEnd =====================================================================================================
        fieldContainer.add(new DataField<>(Period_Summary_BankEnd, Double.class, true));
        fieldContainer.get(Period_Summary_BankEnd).getDisplayProperties().setDataType(CURRENCY);
        fieldContainer.<Double>get(Period_Summary_BankEnd).setDataCore(
                new Derived_DataCore<Double, Period_Summary>(
                        container -> {
                            double sum = 0.0;
                            for (Bank_Summary bankSummary : container.getBankSummarySet()) {
                                sum += bankSummary.getEnd() * bankSummary.getCurrency().getToPrimary();
                            }
                            return Currency.round(sum);
                        }
                        , new ListSource<>(
                        (ListDataField<Bank_Summary>) fieldContainer.<List<Bank_Summary>>get(Period_Summary_BankSummarySet),
                        PoolSummary_End,
                        Bank_Summary_Currency // TODO possible problem here, we have a 3 layer nested dependency. getToPrimary
                )));
        // BankDelta ===================================================================================================
        fieldContainer.add(new DataField<>(Period_Summary_BankDelta, Double.class));
        fieldContainer.get(Period_Summary_BankDelta).getDisplayProperties().setDataType(CURRENCY);
        fieldContainer.get(Period_Summary_BankDelta).getDisplayProperties().setDataContext(ZERO_SCALE);
        fieldContainer.<Double>get(Period_Summary_BankDelta).setDataCore(
                new Derived_DataCore<>(
                        (Derived_DataCore.Calculator<Double, Period_Summary>) container -> {
                            if (container.getPeriod() instanceof VirtualPeriod) {
                                return 0.0;
                            }
                            return container.getBankEnd() - container.getBankStart();
                        }
                        , new LocalSource<>(fieldContainer.get(Period_Summary_BankStart))
                        , new LocalSource<>(fieldContainer.get(Period_Summary_BankEnd))
                        , new LocalSource<>(fieldContainer.get(Period_Summary_Period))));
        // CategoryDelta ===============================================================================================
        fieldContainer.add(new DataField<>(Period_Summary_CategoryDelta, Double.class));
        fieldContainer.get(Period_Summary_CategoryDelta).getDisplayProperties().setVerbosityLevel(DEBUG_DISPLAY);
        fieldContainer.get(Period_Summary_CategoryDelta).getDisplayProperties().setDataType(CURRENCY);
        fieldContainer.get(Period_Summary_CategoryDelta).getDisplayProperties().setDataContext(ZERO_TARGET);
        fieldContainer.<Double>get(Period_Summary_CategoryDelta).setDataCore(
                new Derived_DataCore<Double, Period_Summary>(
                        container -> {
                            double sum = 0.0;
                            for (Category_Summary categorySummary : container.getCategorySummarySet()) {
                                sum += categorySummary.getTransferSum();
                            }
                            if (Math.abs(sum) < 1) { // TODO fix this, it only works because default currently is YEN
                                return 0.0;
                            }
                            return Currency.round(sum);
                        }
                        , new ListSource<>(
                        (ListDataField<Bank_Summary>) fieldContainer.<List<Bank_Summary>>get(Period_Summary_CategorySummarySet),
                        PoolSummary_TransferSum
                )));
        // FundEventStart ==============================================================================================
        fieldContainer.add(new DataField<>(Period_Summary_FundEventStart, Double.class));
        fieldContainer.get(Period_Summary_FundEventStart).getDisplayProperties().setVerbosityLevel(DEBUG_DISPLAY);
        fieldContainer.get(Period_Summary_FundEventStart).getDisplayProperties().setDataType(CURRENCY);
        fieldContainer.<Double>get(Period_Summary_FundEventStart).setDataCore(
                new Derived_DataCore<Double, Period_Summary>(
                        container -> {
                            double sum = 0.0;
                            for (FundEvent_Summary fundEventSummary : container.getFundEventSummarySet()) {
                                sum += fundEventSummary.getTransferSum();
                            }
                            return Currency.round(sum);
                        }
                        , new ListSource<>(
                        (ListDataField<Bank_Summary>) fieldContainer.<List<Bank_Summary>>get(Period_Summary_FundEventSummarySet),
                        PoolSummary_Start
                )));
        // FundEventEnd ================================================================================================
        fieldContainer.add(new DataField<>(Period_Summary_FundEventEnd, Double.class));
        fieldContainer.get(Period_Summary_FundEventEnd).getDisplayProperties().setVerbosityLevel(DEBUG_DISPLAY);
        fieldContainer.get(Period_Summary_FundEventEnd).getDisplayProperties().setDataType(CURRENCY);
        fieldContainer.<Double>get(Period_Summary_FundEventEnd).setDataCore(
                new Derived_DataCore<Double, Period_Summary>(
                        container -> {
                            double sum = 0.0;
                            for (FundEvent_Summary fundEventSummary : container.getFundEventSummarySet()) {
                                sum += fundEventSummary.getEnd();
                            }
                            return Currency.round(sum);
                        }
                        , new ListSource<>(
                        (ListDataField<Bank_Summary>) fieldContainer.<List<Bank_Summary>>get(Period_Summary_FundEventSummarySet),
                        PoolSummary_End
                )));
        // FundEventDelta ==============================================================================================
        fieldContainer.add(new DataField<>(Period_Summary_FundEventDelta, Double.class));
        fieldContainer.get(Period_Summary_FundEventDelta).getDisplayProperties().setDataType(CURRENCY);
        fieldContainer.get(Period_Summary_FundEventDelta).getDisplayProperties().setDataContext(ZERO_SCALE);
        fieldContainer.<Double>get(Period_Summary_FundEventDelta).setDataCore(
                new Derived_DataCore<>(
                        (Derived_DataCore.Calculator<Double, Period_Summary>) container ->
                                container.getFundEventEnd() - container.getFundEventStart()
                        , new LocalSource<>(fieldContainer.get(Period_Summary_FundEventEnd))
                        , new LocalSource<>(fieldContainer.get(Period_Summary_FundEventStart))));
        // BankSummaryValid ============================================================================================
        fieldContainer.add(new DataField<>(Period_Summary_BankSummaryValid, Boolean.class, true));
        fieldContainer.<Boolean>get(Period_Summary_BankSummaryValid).setDataCore(
                new Derived_DataCore<Boolean, Period_Summary>(
                        container -> {
                            for (Bank_Summary bankSummary : container.getBankSummarySet()) {
                                if (!bankSummary.isValid()) {
                                    return false;
                                }
                            }
                            return true;
                        }
                        , new ListSource<>(
                        (ListDataField<Bank_Summary>) fieldContainer.<List<Bank_Summary>>get(Period_Summary_BankSummarySet),
                        PoolSummary_Valid
                )));
        // FundEventSummaryValid =======================================================================================
        fieldContainer.add(new DataField<>(Period_Summary_FundEventSummaryValid, Boolean.class, true));
        fieldContainer.<Boolean>get(Period_Summary_FundEventSummaryValid).setDataCore(
                new Derived_DataCore<Boolean, Period_Summary>(
                        container -> {
                            for (FundEvent_Summary fundEventSummary : container.getFundEventSummarySet()) {
                                if (!fundEventSummary.isValid()) {
                                    return false;
                                }
                            }
                            return true;
                        }
                        , new ListSource<>(
                        (ListDataField<FundEvent_Summary>) fieldContainer.<List<FundEvent_Summary>>get(Period_Summary_FundEventSummarySet),
                        PoolSummary_Valid
                )));
        // CategorySummaryValid ========================================================================================
        fieldContainer.add(new DataField<>(Period_Summary_CategorySummaryValid, Boolean.class, true));
        fieldContainer.<Boolean>get(Period_Summary_CategorySummaryValid).setDataCore(
                new Derived_DataCore<Boolean, Period_Summary>(
                        container -> {
                            for (Category_Summary categorySummary : container.getCategorySummarySet()) {
                                if (!categorySummary.isValid()) {
                                    return false;
                                }
                            }
                            return true;
                        }
                        , new ListSource<>(
                        (ListDataField<Category_Summary>) fieldContainer.<List<Category_Summary>>get(Period_Summary_CategorySummarySet),
                        PoolSummary_Valid
                )));
        // AllSummaryValid =============================================================================================
        fieldContainer.add(new DataField<>(Period_Summary_AllSummaryValid, Boolean.class));
        fieldContainer.get(Period_Summary_AllSummaryValid).getDisplayProperties().setVerbosityLevel(DEBUG_DISPLAY);
        fieldContainer.get(Period_Summary_AllSummaryValid).getDisplayProperties().setDataContext(NOT_FALSE);
        fieldContainer.<Boolean>get(Period_Summary_AllSummaryValid).setDataCore(
                new Derived_DataCore<>(
                        (Derived_DataCore.Calculator<Boolean, Period_Summary>) container ->
                                ((Boolean) container.get(Period_Summary_BankSummaryValid)) & ((Boolean) container.get(Period_Summary_FundEventSummaryValid)) & ((Boolean) container.get(Period_Summary_CategorySummaryValid))
                        , new LocalSource<>(fieldContainer.get(Period_Summary_BankSummaryValid))
                        , new LocalSource<>(fieldContainer.get(Period_Summary_FundEventSummaryValid))
                        , new LocalSource<>(fieldContainer.get(Period_Summary_CategorySummaryValid))));
        // CategoryClear ===============================================================================================
        fieldContainer.add(new DataField<>(Period_Summary_CategoryClear, Boolean.class));
        fieldContainer.get(Period_Summary_CategoryClear).getDisplayProperties().setVerbosityLevel(DEBUG_DISPLAY);
        fieldContainer.get(Period_Summary_CategoryClear).getDisplayProperties().setDataContext(NOT_FALSE);
        fieldContainer.<Boolean>get(Period_Summary_CategoryClear).setDataCore(
                new Derived_DataCore<>(
                        (Derived_DataCore.Calculator<Boolean, Period_Summary>) container ->
                                !(Math.abs(container.getCategoryDelta()) > 1.0)
                        , new LocalSource<>(fieldContainer.get(Period_Summary_CategoryDelta))));
        // BankTransferSet =============================================================================================
        fieldContainer.add(new ListDataField<>(Period_Summary_BankTransferSet, BankTransfer.BankTransferList.class));
        fieldContainer.get(Period_Summary_BankTransferSet).getDisplayProperties().setVerbosityLevel(TRACE_DISPLAY);
        fieldContainer.<List<BankTransfer>>get(Period_Summary_BankTransferSet).setDataCore(
                new Children_ListDataCore<>(
                        BankTransfer.class,
                        new Children_ListDataCore.ParentAccess<>(fieldContainer.get(Period_Summary_Period))));
        // CurrencyValueLoss ===========================================================================================
        fieldContainer.add(new DataField<>(Period_Summary_CurrencyValueLoss, Double.class));
        fieldContainer.get(Period_Summary_CurrencyValueLoss).getDisplayProperties().setVerbosityLevel(DEBUG_DISPLAY);
        fieldContainer.get(Period_Summary_CurrencyValueLoss).getDisplayProperties().setDataType(CURRENCY);
        fieldContainer.<Double>get(Period_Summary_CurrencyValueLoss).setDataCore(
                new Derived_DataCore<>(
                        (Derived_DataCore.Calculator<Double, Period_Summary>) container -> {
                            double value = 0.0;
                            for (BankTransfer bankTransfer : container.getBankTransferSet()) {
                                HalfTransfer halfSource = bankTransfer.toChaneGetSourceTransfer();
                                HalfTransfer halfDestination = bankTransfer.toChangeGetDestinationTransfer();
                                double source = halfSource.getValue() * halfSource.getCurrency().getToPrimary();
                                double destination = halfDestination.getValue() * halfDestination.getCurrency().getToPrimary();
                                value += destination + source;
                            }
                            return value;
                        }
                        , new LocalSource<>(fieldContainer.get(Period_Summary_Period))
                        , new ListSource<>(
                        (ListDataField<BankTransfer>) fieldContainer.<List<BankTransfer>>get(Period_Summary_BankTransferSet),
                        Transfer_Source,
                        Transfer_Destination, // TODO this is wrong, you are actually looking at the half transfer
                        Transfer_Currency,
                        Transfer_Value)));
        // ExchangeRate ================================================================================================
        fieldContainer.add(new DataField<>(Period_Summary_ExchangeRate, Double.class));
        fieldContainer.<Double>get(Period_Summary_ExchangeRate).setDataCore(
                new Derived_DataCore<>(
                        (Derived_DataCore.Calculator<Double, Period_Summary>) container -> {
                            Currency currency1 = null;
                            Currency currency2 = null;

                            for (Currency currency : TrackingDatabase.get().get(Currency.class)) {
                                if (currency.isDefault()) {
                                    currency1 = currency;
                                } else {
                                    currency2 = currency;
                                }
                            }

                            if (currency1 == null || currency2 == null) {
                                return -1.0;
                            }

                            double primarySum = 0.0;
                            double secondarySum = 0.0;
                            for (BankTransfer bankTransfer : container.getPeriod().getChildren(BankTransfer.class)) {
                                HalfTransfer halfSource = bankTransfer.toChaneGetSourceTransfer();
                                HalfTransfer halfDestination = bankTransfer.toChangeGetDestinationTransfer();

                                if (halfDestination.getCurrency().equals(currency1) && halfSource.getCurrency().equals(currency2)) {
                                    primarySum += halfDestination.getValue();
                                    secondarySum -= halfSource.getValue();
                                } else if (halfDestination.getCurrency().equals(currency2) && halfSource.getCurrency().equals(currency1)) {
                                    secondarySum += halfDestination.getValue();
                                    primarySum -= halfSource.getValue();
                                }
                            }

                            if (primarySum == 0.0 && secondarySum == 0.0) {
                                return 0.0;
                            }

                            return primarySum / secondarySum;
                        }
                        , new LocalSource<>(fieldContainer.get(Period_Summary_Period))
                        , new ListSource<>(
                        (ListDataField<BankTransfer>) fieldContainer.<List<BankTransfer>>get(Period_Summary_BankTransferSet),
                        Transfer_Source,
                        Transfer_Destination, // TODO this is wrong, you are actually looking at the half transfer
                        Transfer_Currency,    // TODO this is wrong, you are reading currencies from the tracking database
                        Transfer_Value)));
        // ExchangeRateAcceptable ======================================================================================
        fieldContainer.add(new DataField<>(Period_Summary_ExchangeRateAcceptable, Boolean.class));
        fieldContainer.<Boolean>get(Period_Summary_ExchangeRateAcceptable).setDataCore(
                new Derived_DataCore<>(
                        (Derived_DataCore.Calculator<Boolean, Period_Summary>) container -> {
                            Currency currency1 = null;
                            Currency currency2 = null;
                            for (Currency currency : TrackingDatabase.get().get(Currency.class)) {
                                if (currency.isDefault()) {
                                    currency1 = currency;
                                } else {
                                    currency2 = currency;
                                }
                            }
                            if (currency1 == null || currency2 == null) {
                                return false;
                            }
                            double expectRate = currency2.getToPrimary() / currency1.getToPrimary();
                            Double rate = container.get(Period_Summary_ExchangeRate);
                            if (rate == 0.0) {
                                return true;
                            }
                            double delta = Math.abs(expectRate - rate);
                            return !(delta > expectRate * 0.1);
                        }
                        , new LocalSource<>(fieldContainer.get(Period_Summary_Period))
                        , new LocalSource<>(fieldContainer.get(Period_Summary_ExchangeRate))));
        // Valid =======================================================================================================
        fieldContainer.add(new DataField<>(Period_Summary_Valid, Boolean.class));
        fieldContainer.get(Period_Summary_Valid).getDisplayProperties().setDataContext(NOT_FALSE);
        fieldContainer.<Boolean>get(Period_Summary_Valid).setDataCore(
                new Derived_DataCore<>(
                        (Derived_DataCore.Calculator<Boolean, Period_Summary>) container ->
                                container.isAllSummaryValid() && container.isCategoryClear() && container.isExchangeRateAcceptable()
                        , new LocalSource<>(fieldContainer.get(Period_Summary_AllSummaryValid))
                        , new LocalSource<>(fieldContainer.get(Period_Summary_CategoryClear))
                        , new LocalSource<>(fieldContainer.get(Period_Summary_ExchangeRateAcceptable))));
        // Order =======================================================================================================
        fieldContainer.add(new DataField<>(Period_Summary_Order, Integer.class));
        fieldContainer.<Integer>get(Period_Summary_Order).setDataCore(
                new Derived_DataCore<>(
                        (Derived_DataCore.Calculator<Integer, Period_Summary>) container ->
                                container.getPeriod().getOrder()
                        , new ExternalSource<>(fieldContainer.get(Period_Summary_Period), ExistingPeriod_Order)));
        // Parents
        // Children

        return fieldContainer.finaliseContainer(Period_Summary.class);
    }

    /**
     * Create a new StatementEnd object
     */
    public static Period_Summary make(Period period) {
        if (!period.getChildren(Period_Summary.class).isEmpty()) {
            throw new IllegalStateException("Making a second period summary");
        }

        return assembleDataObject(Period_Summary.getFieldContainer(), new Period_Summary()
                , DataObject_Id, TrackingDatabase.get().getNextId()
                , Period_Summary_Period, period
        );
    }


    //------------------------------------------------------------------------------------------------------------------
    //############################################## Special Calculations ##############################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get the end balance of all banks with the listed currency. Return value is in primary currency, not the source currency
     *
     * @param currency The currency to get
     * @return The end balance of all banks with the listed currency
     */
    public Double getBankEnd(Currency currency) {
        if (getPeriod() instanceof ExistingPeriod) {
            double value = 0.0;
            for (Bank bank : new OneParent_Children_Set<>(Bank.class, currency).get()) {
                Bank_Summary summary = new TwoParent_Children_Set<>(Bank_Summary.class, (ExistingPeriod) getPeriod(), bank).get().get(0);
                value += summary.getEnd() * summary.getCurrency().getToPrimary();
            }
            return value;
        }
        return -1.0;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @Override
    public Currency getCurrency() {
        return TrackingDatabase.get().getDefault(Currency.class);
    }

    public Double getBankStart() {
        return get(Period_Summary_BankStart);
    }

    public Double getBankEnd() {
        return get(Period_Summary_BankEnd);
    }

    public Double getBankDelta() {
        return get(Period_Summary_BankDelta);
    }

    public Double getCategoryDelta() {
        return get(Period_Summary_CategoryDelta);
    }

    public Double getFundEventStart() {
        return get(Period_Summary_FundEventStart);
    }

    public Double getFundEventEnd() {
        return get(Period_Summary_FundEventEnd);
    }

    public Double getFundEventDelta() {
        return get(Period_Summary_FundEventDelta);
    }

    public Boolean isAllSummaryValid() {
        return get(Period_Summary_AllSummaryValid);
    }

    public Boolean isCategoryClear() {
        return get(Period_Summary_CategoryClear);
    }

    public Boolean isExchangeRateAcceptable() {
        return get(Period_Summary_ExchangeRateAcceptable);
    }

    public Boolean isValid() {
        return get(Period_Summary_Valid);
    }

    public Double getCurrencyValueLoss() {
        return get(Period_Summary_CurrencyValueLoss);
    }

    public Period getPeriod() {
        return get(Period_Summary_Period);
    }

    @Override
    public Integer getOrder() {
        return get(Period_Summary_Order);
    }

    public List<Bank_Summary> getBankSummarySet() {
        return get(Period_Summary_BankSummarySet);
    }

    public List<FundEvent_Summary> getFundEventSummarySet() {
        return get(Period_Summary_FundEventSummarySet);
    }

    public List<Category_Summary> getCategorySummarySet() {
        return get(Period_Summary_CategorySummarySet);
    }

    public List<BankTransfer> getBankTransferSet() {
        return get(Period_Summary_BankTransferSet);
    }
}
