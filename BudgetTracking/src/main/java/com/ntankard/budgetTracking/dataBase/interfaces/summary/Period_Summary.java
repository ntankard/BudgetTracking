package com.ntankard.budgetTracking.dataBase.interfaces.summary;

import com.ntankard.dynamicGUI.javaObjectDatabase.Displayable_DataObject;
import com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema.Calculator;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.*;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.end.End_Source_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.step.Step_Source_Schema;
import com.ntankard.budgetTracking.dataBase.core.baseObject.interfaces.CurrencyBound;
import com.ntankard.budgetTracking.dataBase.core.Currency;
import com.ntankard.budgetTracking.dataBase.core.period.ExistingPeriod;
import com.ntankard.budgetTracking.dataBase.core.period.Period;
import com.ntankard.budgetTracking.dataBase.core.period.VirtualPeriod;
import com.ntankard.budgetTracking.dataBase.core.pool.Bank;
import com.ntankard.budgetTracking.dataBase.core.transfer.bank.BankTransfer;
import com.ntankard.budgetTracking.dataBase.core.transfer.HalfTransfer;
import com.ntankard.budgetTracking.dataBase.interfaces.summary.pool.Bank_Summary;
import com.ntankard.budgetTracking.dataBase.interfaces.summary.pool.Bank_Summary.Bank_SummaryList;
import com.ntankard.budgetTracking.dataBase.interfaces.summary.pool.Category_Summary;
import com.ntankard.budgetTracking.dataBase.interfaces.summary.pool.Category_Summary.Category_SummaryList;
import com.ntankard.budgetTracking.dataBase.interfaces.summary.pool.FundEvent_Summary;
import com.ntankard.budgetTracking.dataBase.interfaces.summary.pool.FundEvent_Summary.FundEvent_SummaryList;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.dataObject.factory.SingleParentFactory;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.ListDataField_Schema;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.dataObject.interfaces.Ordered;
import com.ntankard.javaObjectDatabase.database.ParameterMap;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.javaObjectDatabase.util.set.OneParent_Children_Set;
import com.ntankard.javaObjectDatabase.util.set.TwoParent_Children_Set;

import java.util.List;

import static com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties.DataContext.*;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.DataCore_Factory.createMultiParentList;
import static com.ntankard.budgetTracking.dataBase.core.period.ExistingPeriod.ExistingPeriod_Order;
import static com.ntankard.budgetTracking.dataBase.core.transfer.Transfer.*;
import static com.ntankard.budgetTracking.dataBase.interfaces.summary.pool.Bank_Summary.Bank_Summary_Currency;
import static com.ntankard.budgetTracking.dataBase.interfaces.summary.pool.PoolSummary.*;
import static com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties.DEBUG_DISPLAY;
import static com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties.DataType.CURRENCY;
import static com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties.TRACE_DISPLAY;

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

    public static SingleParentFactory<?, ?> Factory = new SingleParentFactory<>(Period_Summary.class, Period.class, Period_Summary::new);

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = Displayable_DataObject.getDataObjectSchema();

        // Class behavior
        dataObjectSchema.setMyFactory(Factory);

        // ID
        // Period ======================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Period_Summary_Period, Period.class));
        // BankSummarySet ==============================================================================================
        dataObjectSchema.add(new ListDataField_Schema<>(Period_Summary_BankSummarySet, Bank_SummaryList.class));
        dataObjectSchema.get(Period_Summary_BankSummarySet).getProperty(Display_Properties.class).setVerbosityLevel(TRACE_DISPLAY);
        dataObjectSchema.<List<Bank_Summary>>get(Period_Summary_BankSummarySet).setDataCore_schema(
                createMultiParentList(
                        Bank_Summary.class,
                        Period_Summary_Period));
        // FundEventSummarySet =========================================================================================
        dataObjectSchema.add(new ListDataField_Schema<>(Period_Summary_FundEventSummarySet, FundEvent_SummaryList.class));
        dataObjectSchema.get(Period_Summary_FundEventSummarySet).getProperty(Display_Properties.class).setVerbosityLevel(TRACE_DISPLAY);
        dataObjectSchema.<List<FundEvent_Summary>>get(Period_Summary_FundEventSummarySet).setDataCore_schema(
                createMultiParentList(
                        FundEvent_Summary.class,
                        Period_Summary_Period));
        // CategorySummarySet ==========================================================================================
        dataObjectSchema.add(new ListDataField_Schema<>(Period_Summary_CategorySummarySet, Category_SummaryList.class));
        dataObjectSchema.get(Period_Summary_CategorySummarySet).getProperty(Display_Properties.class).setVerbosityLevel(TRACE_DISPLAY);
        dataObjectSchema.<List<Category_Summary>>get(Period_Summary_CategorySummarySet).setDataCore_schema(
                createMultiParentList(
                        Category_Summary.class,
                        Period_Summary_Period));
        // BankStart ===================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Period_Summary_BankStart, Double.class, true));
        dataObjectSchema.get(Period_Summary_BankStart).getProperty(Display_Properties.class).setVerbosityLevel(DEBUG_DISPLAY);
        dataObjectSchema.get(Period_Summary_BankStart).getProperty(Display_Properties.class).setDataType(CURRENCY);
        dataObjectSchema.<Double>get(Period_Summary_BankStart).setDataCore_schema(
                new Derived_DataCore_Schema<Double, Period_Summary>(
                        container -> {
                            double sum = 0.0;
                            for (Bank_Summary bankSummary : container.getBankSummarySet()) {
                                sum += bankSummary.getStart() * bankSummary.getCurrency().getToPrimary();
                            }
                            return Currency.round(sum);
                        }
                        , Source_Factory.makeSharedStepSourceChain(
                        Period_Summary_BankSummarySet,
                        PoolSummary_Start,
                        Bank_Summary_Currency // TODO possible problem here, we have a 3 layer nested dependency. getToPrimary
                )));
        // BankEnd =====================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Period_Summary_BankEnd, Double.class, true));
        dataObjectSchema.get(Period_Summary_BankEnd).getProperty(Display_Properties.class).setDataType(CURRENCY);
        dataObjectSchema.<Double>get(Period_Summary_BankEnd).setDataCore_schema(
                new Derived_DataCore_Schema<Double, Period_Summary>(
                        container -> {
                            double sum = 0.0;
                            for (Bank_Summary bankSummary : container.getBankSummarySet()) {
                                sum += bankSummary.getEnd() * bankSummary.getCurrency().getToPrimary();
                            }
                            return Currency.round(sum);
                        }
                        , Source_Factory.makeSharedStepSourceChain(
                        Period_Summary_BankSummarySet,
                        PoolSummary_End,
                        Bank_Summary_Currency // TODO possible problem here, we have a 3 layer nested dependency. getToPrimary
                )));
        // BankDelta ===================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Period_Summary_BankDelta, Double.class));
        dataObjectSchema.get(Period_Summary_BankDelta).getProperty(Display_Properties.class).setDataType(CURRENCY);
        dataObjectSchema.get(Period_Summary_BankDelta).getProperty(Display_Properties.class).setDataContext(ZERO_SCALE);
        dataObjectSchema.<Double>get(Period_Summary_BankDelta).setDataCore_schema(
                new Derived_DataCore_Schema<>(
                        (Calculator<Double, Period_Summary>) container -> {
                            if (container.getPeriod() instanceof VirtualPeriod) {
                                return 0.0;
                            }
                            return container.getBankEnd() - container.getBankStart();
                        }
                        , new End_Source_Schema<>((Period_Summary_BankStart))
                        , new End_Source_Schema<>((Period_Summary_BankEnd))
                        , new End_Source_Schema<>((Period_Summary_Period))));
        // CategoryDelta ===============================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Period_Summary_CategoryDelta, Double.class));
        dataObjectSchema.get(Period_Summary_CategoryDelta).getProperty(Display_Properties.class).setVerbosityLevel(DEBUG_DISPLAY);
        dataObjectSchema.get(Period_Summary_CategoryDelta).getProperty(Display_Properties.class).setDataType(CURRENCY);
        dataObjectSchema.get(Period_Summary_CategoryDelta).getProperty(Display_Properties.class).setDataContext(ZERO_TARGET);
        dataObjectSchema.<Double>get(Period_Summary_CategoryDelta).setDataCore_schema(
                new Derived_DataCore_Schema<Double, Period_Summary>(
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
                        , Source_Factory.makeSharedStepSourceChain(
                        Period_Summary_CategorySummarySet,
                        PoolSummary_TransferSum
                )));
        // FundEventStart ==============================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Period_Summary_FundEventStart, Double.class));
        dataObjectSchema.get(Period_Summary_FundEventStart).getProperty(Display_Properties.class).setVerbosityLevel(DEBUG_DISPLAY);
        dataObjectSchema.get(Period_Summary_FundEventStart).getProperty(Display_Properties.class).setDataType(CURRENCY);
        dataObjectSchema.<Double>get(Period_Summary_FundEventStart).setDataCore_schema(
                new Derived_DataCore_Schema<Double, Period_Summary>(
                        container -> {
                            double sum = 0.0;
                            for (FundEvent_Summary fundEventSummary : container.getFundEventSummarySet()) {
                                sum += fundEventSummary.getTransferSum();
                            }
                            return Currency.round(sum);
                        }
                        , Source_Factory.makeSharedStepSourceChain(
                        Period_Summary_FundEventSummarySet,
                        PoolSummary_Start
                )));
        // FundEventEnd ================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Period_Summary_FundEventEnd, Double.class));
        dataObjectSchema.get(Period_Summary_FundEventEnd).getProperty(Display_Properties.class).setVerbosityLevel(DEBUG_DISPLAY);
        dataObjectSchema.get(Period_Summary_FundEventEnd).getProperty(Display_Properties.class).setDataType(CURRENCY);
        dataObjectSchema.<Double>get(Period_Summary_FundEventEnd).setDataCore_schema(
                new Derived_DataCore_Schema<Double, Period_Summary>(
                        container -> {
                            double sum = 0.0;
                            for (FundEvent_Summary fundEventSummary : container.getFundEventSummarySet()) {
                                sum += fundEventSummary.getEnd();
                            }
                            return Currency.round(sum);
                        }
                        , Source_Factory.makeSharedStepSourceChain(
                        Period_Summary_FundEventSummarySet,
                        PoolSummary_End
                )));
        // FundEventDelta ==============================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Period_Summary_FundEventDelta, Double.class));
        dataObjectSchema.get(Period_Summary_FundEventDelta).getProperty(Display_Properties.class).setDataType(CURRENCY);
        dataObjectSchema.get(Period_Summary_FundEventDelta).getProperty(Display_Properties.class).setDataContext(ZERO_SCALE);
        dataObjectSchema.<Double>get(Period_Summary_FundEventDelta).setDataCore_schema(
                new Derived_DataCore_Schema<>(
                        (Calculator<Double, Period_Summary>) container ->
                                container.getFundEventEnd() - container.getFundEventStart()
                        , new End_Source_Schema<>((Period_Summary_FundEventEnd))
                        , new End_Source_Schema<>((Period_Summary_FundEventStart))));
        // BankSummaryValid ============================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Period_Summary_BankSummaryValid, Boolean.class, true));
        dataObjectSchema.<Boolean>get(Period_Summary_BankSummaryValid).setDataCore_schema(
                new Derived_DataCore_Schema<Boolean, Period_Summary>(
                        container -> {
                            for (Bank_Summary bankSummary : container.getBankSummarySet()) {
                                if (!bankSummary.isValid()) {
                                    return false;
                                }
                            }
                            return true;
                        }
                        , Source_Factory.makeSharedStepSourceChain(
                        Period_Summary_BankSummarySet,
                        PoolSummary_Valid
                )));
        // FundEventSummaryValid =======================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Period_Summary_FundEventSummaryValid, Boolean.class, true));
        dataObjectSchema.<Boolean>get(Period_Summary_FundEventSummaryValid).setDataCore_schema(
                new Derived_DataCore_Schema<Boolean, Period_Summary>(
                        container -> {
                            for (FundEvent_Summary fundEventSummary : container.getFundEventSummarySet()) {
                                if (!fundEventSummary.isValid()) {
                                    return false;
                                }
                            }
                            return true;
                        }
                        , Source_Factory.makeSharedStepSourceChain(
                        Period_Summary_FundEventSummarySet,
                        PoolSummary_Valid
                )));
        // CategorySummaryValid ========================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Period_Summary_CategorySummaryValid, Boolean.class, true));
        dataObjectSchema.<Boolean>get(Period_Summary_CategorySummaryValid).setDataCore_schema(
                new Derived_DataCore_Schema<Boolean, Period_Summary>(
                        container -> {
                            for (Category_Summary categorySummary : container.getCategorySummarySet()) {
                                if (!categorySummary.isValid()) {
                                    return false;
                                }
                            }
                            return true;
                        }
                        , Source_Factory.makeSharedStepSourceChain(
                        Period_Summary_CategorySummarySet,
                        PoolSummary_Valid
                )));
        // AllSummaryValid =============================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Period_Summary_AllSummaryValid, Boolean.class));
        dataObjectSchema.get(Period_Summary_AllSummaryValid).getProperty(Display_Properties.class).setVerbosityLevel(DEBUG_DISPLAY);
        dataObjectSchema.get(Period_Summary_AllSummaryValid).getProperty(Display_Properties.class).setDataContext(NOT_FALSE);
        dataObjectSchema.<Boolean>get(Period_Summary_AllSummaryValid).setDataCore_schema(
                new Derived_DataCore_Schema<>(
                        (Calculator<Boolean, Period_Summary>) container ->
                                ((Boolean) container.get(Period_Summary_BankSummaryValid)) & ((Boolean) container.get(Period_Summary_FundEventSummaryValid)) & ((Boolean) container.get(Period_Summary_CategorySummaryValid))
                        , new End_Source_Schema<>((Period_Summary_BankSummaryValid))
                        , new End_Source_Schema<>((Period_Summary_FundEventSummaryValid))
                        , new End_Source_Schema<>((Period_Summary_CategorySummaryValid))));
        // CategoryClear ===============================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Period_Summary_CategoryClear, Boolean.class));
        dataObjectSchema.get(Period_Summary_CategoryClear).getProperty(Display_Properties.class).setVerbosityLevel(DEBUG_DISPLAY);
        dataObjectSchema.get(Period_Summary_CategoryClear).getProperty(Display_Properties.class).setDataContext(NOT_FALSE);
        dataObjectSchema.<Boolean>get(Period_Summary_CategoryClear).setDataCore_schema(
                new Derived_DataCore_Schema<>(
                        (Calculator<Boolean, Period_Summary>) container ->
                                !(Math.abs(container.getCategoryDelta()) > 1.0)
                        , new End_Source_Schema<>((Period_Summary_CategoryDelta))));
        // BankTransferSet =============================================================================================
        dataObjectSchema.add(new ListDataField_Schema<>(Period_Summary_BankTransferSet, BankTransfer.BankTransferList.class));
        dataObjectSchema.get(Period_Summary_BankTransferSet).getProperty(Display_Properties.class).setVerbosityLevel(TRACE_DISPLAY);
        dataObjectSchema.<List<BankTransfer>>get(Period_Summary_BankTransferSet).setDataCore_schema(
                createMultiParentList(
                        BankTransfer.class,
                        Period_Summary_Period));
        // CurrencyValueLoss ===========================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Period_Summary_CurrencyValueLoss, Double.class));
        dataObjectSchema.get(Period_Summary_CurrencyValueLoss).getProperty(Display_Properties.class).setVerbosityLevel(DEBUG_DISPLAY);
        dataObjectSchema.get(Period_Summary_CurrencyValueLoss).getProperty(Display_Properties.class).setDataType(CURRENCY);
        dataObjectSchema.<Double>get(Period_Summary_CurrencyValueLoss).setDataCore_schema(
                new Derived_DataCore_Schema<>(
                        (Calculator<Double, Period_Summary>) container -> {
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
                        , new End_Source_Schema<>((Period_Summary_Period))
                        , new Step_Source_Schema<>(Period_Summary_BankTransferSet, new End_Source_Schema<>(Transfer_Source))
                        , new Step_Source_Schema<>(Period_Summary_BankTransferSet, new End_Source_Schema<>(Transfer_Destination))
                        , new Step_Source_Schema<>(Period_Summary_BankTransferSet, new End_Source_Schema<>(Transfer_Currency))
                        , new Step_Source_Schema<>(Period_Summary_BankTransferSet, new End_Source_Schema<>(Transfer_Value))));
        // ExchangeRate ================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Period_Summary_ExchangeRate, Double.class));
        dataObjectSchema.<Double>get(Period_Summary_ExchangeRate).setDataCore_schema(
                new Derived_DataCore_Schema<>(
                        (Calculator<Double, Period_Summary>) container -> {
                            Currency currency1 = null;
                            Currency currency2 = null;

                            for (Currency currency : container.getTrackingDatabase().get(Currency.class)) {
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
                        , new End_Source_Schema<>((Period_Summary_Period))
                        , new Step_Source_Schema<>(Period_Summary_BankTransferSet, new End_Source_Schema<>(Transfer_Source))
                        , new Step_Source_Schema<>(Period_Summary_BankTransferSet, new End_Source_Schema<>(Transfer_Destination))
                        , new Step_Source_Schema<>(Period_Summary_BankTransferSet, new End_Source_Schema<>(Transfer_Currency))
                        , new Step_Source_Schema<>(Period_Summary_BankTransferSet, new End_Source_Schema<>(Transfer_Value))));
        // ExchangeRateAcceptable ======================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Period_Summary_ExchangeRateAcceptable, Boolean.class));
        dataObjectSchema.<Boolean>get(Period_Summary_ExchangeRateAcceptable).setDataCore_schema(
                new Derived_DataCore_Schema<>(
                        (Calculator<Boolean, Period_Summary>) container -> {
                            Currency currency1 = null;
                            Currency currency2 = null;
                            for (Currency currency : container.getTrackingDatabase().get(Currency.class)) {
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
                        , new End_Source_Schema<>((Period_Summary_Period))
                        , new End_Source_Schema<>((Period_Summary_ExchangeRate))));
        // Valid =======================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Period_Summary_Valid, Boolean.class));
        dataObjectSchema.get(Period_Summary_Valid).getProperty(Display_Properties.class).setDataContext(NOT_FALSE);
        dataObjectSchema.<Boolean>get(Period_Summary_Valid).setDataCore_schema(
                new Derived_DataCore_Schema<>(
                        (Calculator<Boolean, Period_Summary>) container ->
                                container.isAllSummaryValid() && container.isCategoryClear() && container.isExchangeRateAcceptable()
                        , new End_Source_Schema<>((Period_Summary_AllSummaryValid))
                        , new End_Source_Schema<>((Period_Summary_CategoryClear))
                        , new End_Source_Schema<>((Period_Summary_ExchangeRateAcceptable))));
        // Order =======================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Period_Summary_Order, Integer.class));
        dataObjectSchema.<Integer>get(Period_Summary_Order).setDataCore_schema(
                new Derived_DataCore_Schema<>(
                        (Calculator<Integer, Period_Summary>) container ->
                                container.getPeriod().getOrder()
                        , Source_Factory.makeSourceChain((Period_Summary_Period), ExistingPeriod_Order)));
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(Period_Summary.class);
    }

    /**
     * Constructor
     */
    public Period_Summary(Database database) {
        super(database);
    }

    /**
     * Constructor
     */
    public Period_Summary(Period period) {
        super(period.getTrackingDatabase());
        if (!period.getChildren(Period_Summary.class).isEmpty()) {
            throw new IllegalStateException("Making a second period summary");
        }
        setAllValues(DataObject_Id, getTrackingDatabase().getNextId()
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
        return getTrackingDatabase().getDefault(Currency.class);
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
