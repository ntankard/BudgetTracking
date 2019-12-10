package com.ntankard.Tracking.DataBase.Interface.Summary;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.CurrencyBound;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.Ordered;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.SavingsFundEvent;
import com.ntankard.Tracking.DataBase.Core.Transfers.BankCategoryTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfers.BankTransfer.IntraCurrencyBankTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfers.CategoryFundTransfer.RePayCategoryFundTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfers.Transfer;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Set.Array_Set;
import com.ntankard.Tracking.DataBase.Interface.Set.Children_Set;
import com.ntankard.Tracking.DataBase.Interface.Set.Extended.Sum.Transfer_SumSet;
import com.ntankard.Tracking.DataBase.Interface.Set.Factory.PoolSummary.BankSummary_Set;
import com.ntankard.Tracking.DataBase.Interface.Set.Factory.PoolSummary.CategorySummary_Set;
import com.ntankard.Tracking.DataBase.Interface.Set.Factory.PoolSummary.FundEventSummary_Set;
import com.ntankard.Tracking.DataBase.Interface.Set.MultiParent_Set;
import com.ntankard.Tracking.DataBase.Interface.Set.ObjectSet;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.Bank_Summary;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.Category_Summary;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.FundEvent_Summary;

import java.util.List;

import static com.ntankard.ClassExtension.DisplayProperties.DataContext.*;
import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY;
import static com.ntankard.ClassExtension.MemberProperties.DEBUG_DISPLAY;
import static com.ntankard.ClassExtension.MemberProperties.TRACE_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class Period_Summary extends DataObject implements CurrencyBound, Ordered {

    /**
     * The period to summarise
     */
    private Period period;

    /**
     * Constructor
     */
    @ParameterMap(shouldSave = false)
    public Period_Summary(Period period) {
        super(-1);
        this.period = period;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = TRACE_DISPLAY, shouldDisplay = false)
    @DisplayProperties(order = 21)
    public List<DataObject> getParents() {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = TRACE_DISPLAY, shouldDisplay = false)
    @DisplayProperties(order = 22)
    public Currency getCurrency() {
        return TrackingDatabase.get().getDefault(Currency.class);
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################## Pool Summary ##################################################
    //------------------------------------------------------------------------------------------------------------------

    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 3, dataType = CURRENCY)
    public Double getBankStart() {
        double value = 0.0;
        for (Bank_Summary bank_summary : new BankSummary_Set(period).get()) {
            value += bank_summary.getStart() * bank_summary.getCurrency().getToPrimary();
        }
        return Currency.round(value);
    }

    @DisplayProperties(order = 4, dataType = CURRENCY)
    public Double getBankEnd() {
        double value = 0.0;
        for (Bank_Summary bank_summary : new BankSummary_Set(period).get()) {
            value += bank_summary.getEnd() * bank_summary.getCurrency().getToPrimary();
        }
        return Currency.round(value);
    }

    @DisplayProperties(order = 5, dataType = CURRENCY, dataContext = ZERO_SCALE)
    public Double getBankDelta() {
        return getBankEnd() - getBankStart();
    }

    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 6, dataType = CURRENCY, dataContext = ZERO_TARGET)
    public Double getCategoryDelta() {
        double value = 0.0;
        for (Category_Summary category_summary : new CategorySummary_Set(period).get()) {
            value += category_summary.getTransferSum();
        }
        return Currency.round(value);
    }

    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 7, dataType = CURRENCY)
    public Double getFundEventStart() {
        double value = 0.0;
        for (FundEvent_Summary fundEvent_summary : new FundEventSummary_Set(period).get()) {
            value += fundEvent_summary.getStart() * fundEvent_summary.getCurrency().getToPrimary();
        }
        return Currency.round(value);
    }

    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 8, dataType = CURRENCY)
    public Double getFundEventEnd() {
        double value = 0.0;
        for (FundEvent_Summary fundEvent_summary : new FundEventSummary_Set(period).get()) {
            value += fundEvent_summary.getEnd() * fundEvent_summary.getCurrency().getToPrimary();
        }
        return value;
    }

    @DisplayProperties(order = 9, dataType = CURRENCY, dataContext = ZERO_SCALE)
    public Double getFundEventDelta() {
        return getFundEventEnd() - getFundEventStart();
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Validity ####################################################
    //------------------------------------------------------------------------------------------------------------------

    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 10, dataContext = NOT_FALSE)
    public Boolean isAllSummaryValid() {
        for (Bank_Summary summary : new BankSummary_Set(period).get()) {
            if (!summary.isValid()) {
                return false;
            }
        }

        for (Category_Summary summary : new CategorySummary_Set(period).get()) {
            if (!summary.isValid()) {
                return false;
            }
        }

        for (FundEvent_Summary summary : new FundEventSummary_Set(period).get()) {
            if (!summary.isValid()) {
                return false;
            }
        }

        for (FundEvent_Summary summary : new FundEventSummary_Set(period).get()) {
            if (!summary.isValid()) {
                return false;
            }
        }
        return true;
    }

    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 11, dataContext = NOT_FALSE)
    public Boolean isCategoryClear() {
        return getCategoryDelta().equals(0.0);
    }

    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 12, dataContext = NOT_FALSE)
    public Boolean isValueConserved() {
        return Math.abs(getBankDelta() + getFundEventDelta() - getCurrencyValueLoss()) < 0.01;
    }

    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 13, dataContext = NOT_FALSE)
    public Boolean isExchangeRateAcceptable() {
        for (Currency currency1 : TrackingDatabase.get().get(Currency.class)) {
            for (Currency currency2 : TrackingDatabase.get().get(Currency.class)) {
                if (!currency1.equals(currency2)) {
                    Double expectRate = currency2.getToPrimary() / currency1.getToPrimary();
                    Double rate = getExchangeRate(currency1, currency2);
                    if (rate == 0.0) {
                        continue;
                    }
                    Double delta = Math.abs(expectRate - rate);
                    if (delta > expectRate * 0.1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @DisplayProperties(order = 14, dataContext = NOT_FALSE)
    public Boolean isValid() {
        return isAllSummaryValid() && isCategoryClear() && isValueConserved() && isExchangeRateAcceptable();
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################## Special Calculations ##############################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get the total of all transfers not including the savings transfer
     *
     * @return The total of all transfers not including the savings transfer
     */
    @DisplayProperties(order = 15, dataType = CURRENCY)
    public Double getNonSaveCategoryDelta() {
        double sum = 0.0;
        for (Category category : TrackingDatabase.get().get(Category.class)) {

            ObjectSet<Transfer> set = new MultiParent_Set<>(Transfer.class, period, category);
            List<Transfer> data = set.get();
            Object toRemove = null;
            for (Transfer transfer : data) {
                if (transfer instanceof RePayCategoryFundTransfer) {
                    RePayCategoryFundTransfer rePayCategoryFundTransfer = (RePayCategoryFundTransfer) transfer;
                    if (rePayCategoryFundTransfer.getDestination() instanceof SavingsFundEvent) {
                        toRemove = transfer;
                        break;
                    }
                }
            }

            if (toRemove != null) {
                data.remove(toRemove);
            }

            sum += new Transfer_SumSet<>(new Array_Set<>(data), category).getTotal();
        }
        return -sum;
    }

    /**
     * Get the sum of BankCategoryTransfer into taxable income categories
     *
     * @return The sum of BankCategoryTransfer into taxable income categories
     */
    @DisplayProperties(order = 16, dataType = CURRENCY)
    public Double getTaxableIncome() {
        Category category = TrackingDatabase.get().getSpecialValue(Category.class, Category.TAXABLE);
        ObjectSet<BankCategoryTransfer> objectSet = new MultiParent_Set<>(BankCategoryTransfer.class, period, category);
        return Currency.round(new Transfer_SumSet<>(objectSet, category).getTotal());
    }

    /**
     * Get the difference between the expected exchange rate and the used exchanged rate for IntraCurrencyBankTransfer
     *
     * @return THe value lost from the system to currency exchange
     */
    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 17, dataType = CURRENCY)
    public Double getCurrencyValueLoss() {
        double value = 0.0;
        for (IntraCurrencyBankTransfer intraCurrencyBankTransfer : period.getChildren(IntraCurrencyBankTransfer.class)) {
            double source = intraCurrencyBankTransfer.getSourceValue() * intraCurrencyBankTransfer.getSourceCurrency().getToPrimary();
            double destination = intraCurrencyBankTransfer.getDestinationValue() * intraCurrencyBankTransfer.getDestinationCurrency().getToPrimary();
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
        for (IntraCurrencyBankTransfer intraCurrencyBankTransfer : period.getChildren(IntraCurrencyBankTransfer.class)) {
            if (intraCurrencyBankTransfer.getDestinationCurrency().equals(currency1) && intraCurrencyBankTransfer.getSourceCurrency().equals(currency2)) {
                primarySum += intraCurrencyBankTransfer.getDestinationValue();
                secondarySum -= intraCurrencyBankTransfer.getSourceValue();
            } else if (intraCurrencyBankTransfer.getDestinationCurrency().equals(currency2) && intraCurrencyBankTransfer.getSourceCurrency().equals(currency1)) {
                secondarySum += intraCurrencyBankTransfer.getDestinationValue();
                primarySum -= intraCurrencyBankTransfer.getSourceValue();
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
        double value = 0.0;
        for (Bank bank : new Children_Set<>(Bank.class, currency).get()) {
            Bank_Summary summary = new Bank_Summary(period, bank);
            value += summary.getEnd() * summary.getCurrency().getToPrimary();
        }
        return value;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @DisplayProperties(order = 2)
    public Period getPeriod() {
        return period;
    }

    @MemberProperties(verbosityLevel = TRACE_DISPLAY)
    @DisplayProperties(order = 23)
    public Integer getOrder() {
        return getPeriod().getOrder();
    }
}
