package com.ntankard.Tracking.DataBase.Interface.Summary;

import com.ntankard.dynamicGUI.CoreObject.Field.DataCore.Method_DataCore;
import com.ntankard.dynamicGUI.CoreObject.FieldContainer;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.CurrencyBound;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.Ordered;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Tracking_DataField;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.SolidCategory;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.SavingsFundEvent;
import com.ntankard.Tracking.DataBase.Core.Transfer.Bank.BankTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfer.Fund.RePayFundTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfer.HalfTransfer;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Set.Array_Set;
import com.ntankard.Tracking.DataBase.Interface.Set.Extended.Sum.Transfer_SumSet;
import com.ntankard.Tracking.DataBase.Interface.Set.Factory.PoolSummary.BankSummary_Set;
import com.ntankard.Tracking.DataBase.Interface.Set.Factory.PoolSummary.CategorySummary_Set;
import com.ntankard.Tracking.DataBase.Interface.Set.Factory.PoolSummary.FundEventSummary_Set;
import com.ntankard.Tracking.DataBase.Interface.Set.Filter.TransferType_HalfTransfer_Filter;
import com.ntankard.Tracking.DataBase.Interface.Set.ObjectSet;
import com.ntankard.Tracking.DataBase.Interface.Set.OneParent_Children_Set;
import com.ntankard.Tracking.DataBase.Interface.Set.TwoParent_Children_Set;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.Bank_Summary;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.Category_Summary;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.FundEvent_Summary;

import java.util.List;

import static com.ntankard.dynamicGUI.CoreObject.Field.Properties.Display_Properties.DataContext.*;
import static com.ntankard.dynamicGUI.CoreObject.Field.Properties.Display_Properties.DataType.CURRENCY;
import static com.ntankard.dynamicGUI.CoreObject.Field.Properties.Display_Properties.DEBUG_DISPLAY;
import static com.ntankard.dynamicGUI.CoreObject.Field.Properties.Display_Properties.TRACE_DISPLAY;

@ParameterMap(shouldSave = false)
public class Period_Summary extends DataObject implements CurrencyBound, Ordered {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String Period_Summary_Period = "getPeriod";
    public static final String Period_Summary_BankStart = "getBankStart";
    public static final String Period_Summary_BankEnd = "getBankEnd";
    public static final String Period_Summary_BankDelta = "getBankDelta";
    public static final String Period_Summary_CategoryDelta = "getCategoryDelta";
    public static final String Period_Summary_FundEventStart = "getFundEventStart";
    public static final String Period_Summary_FundEventEnd = "getFundEventEnd";
    public static final String Period_Summary_FundEventDelta = "getFundEventDelta";
    public static final String Period_Summary_AllSummaryValid = "isAllSummaryValid";
    public static final String Period_Summary_CategoryClear = "isCategoryClear";
    public static final String Period_Summary_ValueConserved = "isValueConserved";
    public static final String Period_Summary_ExchangeRateAcceptable = "isExchangeRateAcceptable";
    public static final String Period_Summary_Valid = "isValid";
    public static final String Period_Summary_NonSaveCategoryDelta = "getNonSaveCategoryDelta";
    public static final String Period_Summary_TaxableIncome = "getTaxableIncome";
    public static final String Period_Summary_CurrencyValueLoss = "getCurrencyValueLoss";
    public static final String Period_Summary_Order = "getOrder";
    public static final String Period_Summary_Currency = "getCurrency";

    /**
     * Get all the fields for this object
     */
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = DataObject.getFieldContainer();

        // ID
        // Period ======================================================================================================
        fieldContainer.add(new Tracking_DataField<>(Period_Summary_Period, Period.class));
        // BankStart ===================================================================================================
        fieldContainer.add(new Tracking_DataField<>(Period_Summary_BankStart, Double.class));
        fieldContainer.get(Period_Summary_BankStart).setDataCore(new Method_DataCore<>(container -> ((Period_Summary) container).getBankStart_impl()));
        fieldContainer.get(Period_Summary_BankStart).getDisplayProperties().setVerbosityLevel(DEBUG_DISPLAY);
        fieldContainer.get(Period_Summary_BankStart).getDisplayProperties().setDataType(CURRENCY);
        // BankEnd =====================================================================================================
        fieldContainer.add(new Tracking_DataField<>(Period_Summary_BankEnd, DataObject.class));
        fieldContainer.get(Period_Summary_BankEnd).setDataCore(new Method_DataCore<>(container -> ((Period_Summary) container).getBankEnd_impl()));
        fieldContainer.get(Period_Summary_BankEnd).getDisplayProperties().setDataType(CURRENCY);
        // BankDelta ===================================================================================================
        fieldContainer.add(new Tracking_DataField<>(Period_Summary_BankDelta, Double.class));
        fieldContainer.get(Period_Summary_BankDelta).setDataCore(new Method_DataCore<>(container -> ((Period_Summary) container).getBankDelta_impl()));
        fieldContainer.get(Period_Summary_BankDelta).getDisplayProperties().setDataType(CURRENCY);
        fieldContainer.get(Period_Summary_BankDelta).getDisplayProperties().setDataContext(ZERO_SCALE);
        // CategoryDelta ===============================================================================================
        fieldContainer.add(new Tracking_DataField<>(Period_Summary_CategoryDelta, Double.class));
        fieldContainer.get(Period_Summary_CategoryDelta).setDataCore(new Method_DataCore<>(container -> ((Period_Summary) container).getCategoryDelta_impl()));
        fieldContainer.get(Period_Summary_CategoryDelta).getDisplayProperties().setVerbosityLevel(DEBUG_DISPLAY);
        fieldContainer.get(Period_Summary_CategoryDelta).getDisplayProperties().setDataType(CURRENCY);
        fieldContainer.get(Period_Summary_CategoryDelta).getDisplayProperties().setDataContext(ZERO_TARGET);
        // FundEventStart ==============================================================================================
        fieldContainer.add(new Tracking_DataField<>(Period_Summary_FundEventStart, Double.class));
        fieldContainer.get(Period_Summary_FundEventStart).setDataCore(new Method_DataCore<>(container -> ((Period_Summary) container).getFundEventStart_impl()));
        fieldContainer.get(Period_Summary_FundEventStart).getDisplayProperties().setVerbosityLevel(DEBUG_DISPLAY);
        fieldContainer.get(Period_Summary_FundEventStart).getDisplayProperties().setDataType(CURRENCY);
        // FundEventEnd ================================================================================================
        fieldContainer.add(new Tracking_DataField<>(Period_Summary_FundEventEnd, Double.class));
        fieldContainer.get(Period_Summary_FundEventEnd).setDataCore(new Method_DataCore<>(container -> ((Period_Summary) container).getFundEventEnd_impl()));
        fieldContainer.get(Period_Summary_FundEventEnd).getDisplayProperties().setVerbosityLevel(DEBUG_DISPLAY);
        fieldContainer.get(Period_Summary_FundEventEnd).getDisplayProperties().setDataType(CURRENCY);
        // FundEventDelta ==============================================================================================
        fieldContainer.add(new Tracking_DataField<>(Period_Summary_FundEventDelta, Double.class));
        fieldContainer.get(Period_Summary_FundEventDelta).setDataCore(new Method_DataCore<>(container -> ((Period_Summary) container).getFundEventDelta_impl()));
        fieldContainer.get(Period_Summary_FundEventDelta).getDisplayProperties().setDataType(CURRENCY);
        fieldContainer.get(Period_Summary_FundEventDelta).getDisplayProperties().setDataContext(ZERO_SCALE);
        // AllSummaryValid =============================================================================================
        fieldContainer.add(new Tracking_DataField<>(Period_Summary_AllSummaryValid, Boolean.class));
        fieldContainer.get(Period_Summary_AllSummaryValid).setDataCore(new Method_DataCore<>(container -> ((Period_Summary) container).isAllSummaryValid_impl()));
        fieldContainer.get(Period_Summary_AllSummaryValid).getDisplayProperties().setVerbosityLevel(DEBUG_DISPLAY);
        fieldContainer.get(Period_Summary_AllSummaryValid).getDisplayProperties().setDataContext(NOT_FALSE);
        // CategoryClear ===============================================================================================
        fieldContainer.add(new Tracking_DataField<>(Period_Summary_CategoryClear, Boolean.class));
        fieldContainer.get(Period_Summary_CategoryClear).setDataCore(new Method_DataCore<>(container -> ((Period_Summary) container).isCategoryClear_impl()));
        fieldContainer.get(Period_Summary_CategoryClear).getDisplayProperties().setVerbosityLevel(DEBUG_DISPLAY);
        fieldContainer.get(Period_Summary_CategoryClear).getDisplayProperties().setDataContext(NOT_FALSE);
        // ValueConserved ==============================================================================================
        fieldContainer.add(new Tracking_DataField<>(Period_Summary_ValueConserved, Boolean.class));
        fieldContainer.get(Period_Summary_ValueConserved).setDataCore(new Method_DataCore<>(container -> ((Period_Summary) container).isValueConserved_impl()));
        fieldContainer.get(Period_Summary_ValueConserved).getDisplayProperties().setVerbosityLevel(DEBUG_DISPLAY);
        fieldContainer.get(Period_Summary_ValueConserved).getDisplayProperties().setDataContext(NOT_FALSE);
        // ExchangeRateAcceptable ======================================================================================
        fieldContainer.add(new Tracking_DataField<>(Period_Summary_ExchangeRateAcceptable, Boolean.class));
        fieldContainer.get(Period_Summary_ExchangeRateAcceptable).setDataCore(new Method_DataCore<>(container -> ((Period_Summary) container).isExchangeRateAcceptable_impl()));
        fieldContainer.get(Period_Summary_ExchangeRateAcceptable).getDisplayProperties().setVerbosityLevel(DEBUG_DISPLAY);
        fieldContainer.get(Period_Summary_ExchangeRateAcceptable).getDisplayProperties().setDataContext(NOT_FALSE);
        // Valid =======================================================================================================
        fieldContainer.add(new Tracking_DataField<>(Period_Summary_Valid, Boolean.class));
        fieldContainer.get(Period_Summary_Valid).setDataCore(new Method_DataCore<>(container -> ((Period_Summary) container).isValid_impl()));
        fieldContainer.get(Period_Summary_Valid).getDisplayProperties().setDataContext(NOT_FALSE);
        // NonSaveCategoryDelta ========================================================================================
        fieldContainer.add(new Tracking_DataField<>(Period_Summary_NonSaveCategoryDelta, Double.class));
        fieldContainer.get(Period_Summary_NonSaveCategoryDelta).setDataCore(new Method_DataCore<>(container -> ((Period_Summary) container).getNonSaveCategoryDelta_impl()));
        fieldContainer.get(Period_Summary_NonSaveCategoryDelta).getDisplayProperties().setDataType(CURRENCY);
        // TaxableIncome ===============================================================================================
        fieldContainer.add(new Tracking_DataField<>(Period_Summary_TaxableIncome, Double.class));
        fieldContainer.get(Period_Summary_TaxableIncome).setDataCore(new Method_DataCore<>(container -> ((Period_Summary) container).getTaxableIncome_impl()));
        fieldContainer.get(Period_Summary_TaxableIncome).getDisplayProperties().setDataType(CURRENCY);
        // CurrencyValueLoss ===========================================================================================
        fieldContainer.add(new Tracking_DataField<>(Period_Summary_CurrencyValueLoss, Double.class));
        fieldContainer.get(Period_Summary_CurrencyValueLoss).setDataCore(new Method_DataCore<>(container -> ((Period_Summary) container).getCurrencyValueLoss_impl()));
        fieldContainer.get(Period_Summary_CurrencyValueLoss).getDisplayProperties().setVerbosityLevel(DEBUG_DISPLAY);
        fieldContainer.get(Period_Summary_CurrencyValueLoss).getDisplayProperties().setDataType(CURRENCY);
        // Order =======================================================================================================
        fieldContainer.add(new Tracking_DataField<>(Period_Summary_Order, Integer.class));
        fieldContainer.get(Period_Summary_Order).setDataCore(new Method_DataCore<>(container -> ((Period_Summary) container).getPeriod().getOrder()));
        fieldContainer.get(Period_Summary_CurrencyValueLoss).getDisplayProperties().setVerbosityLevel(TRACE_DISPLAY);
        // Currency ====================================================================================================
        fieldContainer.add(new Tracking_DataField<>(Period_Summary_Currency, Currency.class));
        fieldContainer.get(Period_Summary_Currency).getDisplayProperties().setVerbosityLevel(TRACE_DISPLAY);
        fieldContainer.get(Period_Summary_Currency).getDisplayProperties().setShouldDisplay(false);
        fieldContainer.get(Period_Summary_Currency).setDataCore(new Method_DataCore<>(container -> TrackingDatabase.get().getDefault(Currency.class)));
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
    //################################################## Pool Summary ##################################################
    //------------------------------------------------------------------------------------------------------------------

    private Double getBankStart_impl() {
        if (getPeriod() instanceof ExistingPeriod) {
            double value = 0.0;
            for (Bank_Summary bank_summary : new BankSummary_Set((ExistingPeriod) getPeriod()).get()) {
                value += bank_summary.getStart() * bank_summary.getCurrency().getToPrimary();
            }
            return Currency.round(value);
        }
        return -1.0;
    }

    private Double getBankEnd_impl() {
        if (getPeriod() instanceof ExistingPeriod) {
            double value = 0.0;
            for (Bank_Summary bank_summary : new BankSummary_Set((ExistingPeriod) getPeriod()).get()) {
                value += bank_summary.getEnd() * bank_summary.getCurrency().getToPrimary();
            }
            return Currency.round(value);
        }
        return -1.0;
    }

    private Double getBankDelta_impl() {
        return getBankEnd() - getBankStart();
    }

    private Double getCategoryDelta_impl() {
        double value = 0.0;
        for (Category_Summary category_summary : new CategorySummary_Set(getPeriod()).get()) {
            value += category_summary.getTransferSum();
        }
        return Currency.round(value);
    }

    private Double getFundEventStart_impl() {
        double value = 0.0;
        for (FundEvent_Summary fundEvent_summary : new FundEventSummary_Set(getPeriod()).get()) {
            value += fundEvent_summary.getStart() * fundEvent_summary.getCurrency().getToPrimary();
        }
        return Currency.round(value);
    }

    private Double getFundEventEnd_impl() {
        double value = 0.0;
        for (FundEvent_Summary fundEvent_summary : new FundEventSummary_Set(getPeriod()).get()) {
            value += fundEvent_summary.getEnd() * fundEvent_summary.getCurrency().getToPrimary();
        }
        return value;
    }

    private Double getFundEventDelta_impl() {
        return getFundEventEnd() - getFundEventStart();
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Validity ####################################################
    //------------------------------------------------------------------------------------------------------------------

    private Boolean isAllSummaryValid_impl() {
        if (getPeriod() instanceof ExistingPeriod) {
            for (Bank_Summary summary : new BankSummary_Set((ExistingPeriod) getPeriod()).get()) {
                if (!summary.isValid()) {
                    return false;
                }
            }
        }

        for (Category_Summary summary : new CategorySummary_Set(getPeriod()).get()) {
            if (!summary.isValid()) {
                return false;
            }
        }

        for (FundEvent_Summary summary : new FundEventSummary_Set(getPeriod()).get()) {
            if (!summary.isValid()) {
                return false;
            }
        }

        for (FundEvent_Summary summary : new FundEventSummary_Set(getPeriod()).get()) {
            if (!summary.isValid()) {
                return false;
            }
        }
        return true;
    }

    private Boolean isCategoryClear_impl() {
        return getCategoryDelta().equals(0.0);
    }

    private Boolean isValueConserved_impl() {
        return Math.abs(getBankDelta() + getFundEventDelta() - getCurrencyValueLoss()) < 0.01;
    }

    private Boolean isExchangeRateAcceptable_impl() {
        for (Currency currency1 : TrackingDatabase.get().get(Currency.class)) {
            for (Currency currency2 : TrackingDatabase.get().get(Currency.class)) {
                if (!currency1.equals(currency2)) {
                    double expectRate = currency2.getToPrimary() / currency1.getToPrimary();
                    Double rate = getExchangeRate(currency1, currency2);
                    if (rate == 0.0) {
                        continue;
                    }
                    double delta = Math.abs(expectRate - rate);
                    if (delta > expectRate * 0.1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private Boolean isValid_impl() {
        return isAllSummaryValid() && isCategoryClear() && isExchangeRateAcceptable(); //  && isValueConserved() Removed as we don't have a way to account for intra period
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################## Special Calculations ##############################################
    //------------------------------------------------------------------------------------------------------------------


    /**
     * Get the total of all transfers not including the savings transfer
     *
     * @return The total of all transfers not including the savings transfer
     */
    private Double getNonSaveCategoryDelta_impl() {
        double sum = 0.0;
        for (SolidCategory solidCategory : TrackingDatabase.get().get(SolidCategory.class)) {

            ObjectSet<HalfTransfer> set = new TwoParent_Children_Set<>(HalfTransfer.class, getPeriod(), solidCategory);
            List<HalfTransfer> data = set.get();
            Object toRemove = null;
            for (HalfTransfer transfer : data) {
                if (transfer.getTransfer() instanceof RePayFundTransfer) {
                    RePayFundTransfer rePayCategoryFundTransfer = (RePayFundTransfer) transfer.getTransfer();
                    if (rePayCategoryFundTransfer.getSource() instanceof SavingsFundEvent) {
                        if (toRemove != null) {
                            //toRemove.toString();
                            throw new RuntimeException("Duplicate savings");
                        }
                        toRemove = transfer;
                    }
                }
            }

            if (toRemove != null) {
                data.remove(toRemove);
            }

            sum += new Transfer_SumSet<>(new Array_Set<>(data), solidCategory).getTotal();
        }
        return -sum;
    }

    /**
     * Get the sum of BankCategoryTransfer into taxable income categories
     *
     * @return The sum of BankCategoryTransfer into taxable income categories
     */
    private Double getTaxableIncome_impl() {
        SolidCategory solidCategory = TrackingDatabase.get().getSpecialValue(SolidCategory.class, SolidCategory.TAXABLE);
        ObjectSet<HalfTransfer> objectSet = new TwoParent_Children_Set<>(HalfTransfer.class, getPeriod(), solidCategory, new TransferType_HalfTransfer_Filter(BankTransfer.class));
        return Currency.round(new Transfer_SumSet<>(objectSet, solidCategory).getTotal());
    }


    /**
     * Get the difference between the expected exchange rate and the used exchanged rate for IntraCurrencyBankTransfer
     *
     * @return THe value lost from the system to currency exchange
     */
    private Double getCurrencyValueLoss_impl() {
        double value = 0.0;
        for (BankTransfer bankTransfer : getPeriod().getChildren(BankTransfer.class)) {
            HalfTransfer halfSource = bankTransfer.toChaneGetSourceTransfer();
            HalfTransfer halfDestination = bankTransfer.toChangeGetDestinationTransfer();
            double source = halfSource.getValue() * halfSource.getCurrency().getToPrimary();
            double destination = halfDestination.getValue() * halfDestination.getCurrency().getToPrimary();
            value += destination + source;
        }
        return value;
    }

    /**
     * Get the average exchange rate between 2 currencies
     *
     * @param currency1 The first currency
     * @param currency2 The second currency
     * @return The average exchange rate between 2 currencies
     */
    public Double getExchangeRate(Currency currency1, Currency currency2) {
        double primarySum = 0.0;
        double secondarySum = 0.0;
        for (BankTransfer bankTransfer : getPeriod().getChildren(BankTransfer.class)) {
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
                Bank_Summary summary = Bank_Summary.make((ExistingPeriod) getPeriod(), bank);
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
        return get(Period_Summary_Currency);
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

    public Boolean isValueConserved() {
        return get(Period_Summary_ValueConserved);
    }

    public Boolean isExchangeRateAcceptable() {
        return get(Period_Summary_ExchangeRateAcceptable);
    }

    public Boolean isValid() {
        return get(Period_Summary_Valid);
    }

    public Double getNonSaveCategoryDelta() {
        return get(Period_Summary_NonSaveCategoryDelta);
    }

    public Double getTaxableIncome() {
        return get(Period_Summary_TaxableIncome);
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
}
