package com.ntankard.Tracking.DataBase.Interface.Summary;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.DataObject_Field;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Field;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.CurrencyBound;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.Ordered;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
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

import static com.ntankard.ClassExtension.DisplayProperties.DataContext.*;
import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY;
import static com.ntankard.ClassExtension.MemberProperties.DEBUG_DISPLAY;
import static com.ntankard.ClassExtension.MemberProperties.TRACE_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class Period_Summary extends DataObject implements CurrencyBound, Ordered {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static List<Field<?>> getFields(Period period, DataObject container) {
        List<Field<?>> toReturn = DataObject.getFields(-1, container);
        toReturn.add(new DataObject_Field<>("period", Period.class, period, container));
        return toReturn;
    }

    /**
     * Constructor
     */
    @ParameterMap(shouldSave = false)
    public Period_Summary(Period period) {
        super();
        setFields(getFields(period, this));
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
        if (getPeriod() instanceof ExistingPeriod) {
            double value = 0.0;
            for (Bank_Summary bank_summary : new BankSummary_Set((ExistingPeriod) getPeriod()).get()) {
                value += bank_summary.getStart() * bank_summary.getCurrency().getToPrimary();
            }
            return Currency.round(value);
        }
        return -1.0;
    }

    @DisplayProperties(order = 4, dataType = CURRENCY)
    public Double getBankEnd() {
        if (getPeriod() instanceof ExistingPeriod) {
            double value = 0.0;
            for (Bank_Summary bank_summary : new BankSummary_Set((ExistingPeriod) getPeriod()).get()) {
                value += bank_summary.getEnd() * bank_summary.getCurrency().getToPrimary();
            }
            return Currency.round(value);
        }
        return -1.0;
    }

    @DisplayProperties(order = 5, dataType = CURRENCY, dataContext = ZERO_SCALE)
    public Double getBankDelta() {
        return getBankEnd() - getBankStart();
    }

    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 6, dataType = CURRENCY, dataContext = ZERO_TARGET)
    public Double getCategoryDelta() {
        double value = 0.0;
        for (Category_Summary category_summary : new CategorySummary_Set(getPeriod()).get()) {
            value += category_summary.getTransferSum();
        }
        return Currency.round(value);
    }

    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 7, dataType = CURRENCY)
    public Double getFundEventStart() {
        double value = 0.0;
        for (FundEvent_Summary fundEvent_summary : new FundEventSummary_Set(getPeriod()).get()) {
            value += fundEvent_summary.getStart() * fundEvent_summary.getCurrency().getToPrimary();
        }
        return Currency.round(value);
    }

    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 8, dataType = CURRENCY)
    public Double getFundEventEnd() {
        double value = 0.0;
        for (FundEvent_Summary fundEvent_summary : new FundEventSummary_Set(getPeriod()).get()) {
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

    @DisplayProperties(order = 14, dataContext = NOT_FALSE)
    public Boolean isValid() {
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
    @DisplayProperties(order = 15, dataType = CURRENCY)
    public Double getNonSaveCategoryDelta() {
        double sum = 0.0;
        for (Category category : TrackingDatabase.get().get(Category.class)) {

            ObjectSet<HalfTransfer> set = new TwoParent_Children_Set<>(HalfTransfer.class, getPeriod(), category);
            List<HalfTransfer> data = set.get();
            Object toRemove = null;
            for (HalfTransfer transfer : data) {
                if (transfer.getTransfer() instanceof RePayFundTransfer) {
                    RePayFundTransfer rePayCategoryFundTransfer = (RePayFundTransfer) transfer.getTransfer();
                    if (rePayCategoryFundTransfer.getSource() instanceof SavingsFundEvent) {
                        if (toRemove != null) {
                            throw new RuntimeException("Duplicate savings");
                        }
                        toRemove = transfer;
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
        ObjectSet<HalfTransfer> objectSet = new TwoParent_Children_Set<>(HalfTransfer.class, getPeriod(), category, new TransferType_HalfTransfer_Filter(BankTransfer.class));
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
        for (BankTransfer bankTransfer : getPeriod().getChildren(BankTransfer.class)) {
            HalfTransfer halfSource = bankTransfer.getSourceTransfer();
            HalfTransfer halfDestination = bankTransfer.getDestinationTransfer();
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
            HalfTransfer halfSource = bankTransfer.getSourceTransfer();
            HalfTransfer halfDestination = bankTransfer.getDestinationTransfer();

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
                Bank_Summary summary = new Bank_Summary((ExistingPeriod) getPeriod(), bank);
                value += summary.getEnd() * summary.getCurrency().getToPrimary();
            }
            return value;
        }
        return -1.0;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @DisplayProperties(order = 2)
    public Period getPeriod() {
        return get("period");
    }

    @MemberProperties(verbosityLevel = TRACE_DISPLAY)
    @DisplayProperties(order = 23)
    public Integer getOrder() {
        return getPeriod().getOrder();
    }
}
