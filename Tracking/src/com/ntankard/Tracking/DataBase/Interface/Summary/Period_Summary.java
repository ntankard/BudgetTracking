package com.ntankard.Tracking.DataBase.Interface.Summary;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.CurrencyBound;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Core.Transfers.Transfer;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Set.Children_Set;
import com.ntankard.Tracking.DataBase.Interface.Set.MultiParent_Set;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.Bank_Summary;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.PoolSummary;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.DisplayProperties.DataContext.ZERO_SCALE;
import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY;
import static com.ntankard.ClassExtension.MemberProperties.DEBUG_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class Period_Summary extends DataObject implements CurrencyBound {

    /**
     * The period to summarise
     */
    private Period period;

    /**
     * Constructor
     */
    @ParameterMap(shouldSave = false)
    public Period_Summary(Integer id, Period period) {
        super(id);
        this.period = period;
    }

    /**
     * @inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 21)
    public List<DataObject> getParents() {
        List<DataObject> toReturn = new ArrayList<>();
        toReturn.add(period);
        return toReturn;
    }

    @DisplayProperties(order = 2)
    public Period getPeriod() {
        return period;
    }

    /**
     * Is the period valid? Are all funds accounted for?
     *
     * @return True it the period is valid
     */
    @DisplayProperties(order = 3)
    public Boolean isValid() {
        for (PoolSummary poolSummary : new Children_Set<>(PoolSummary.class, period).get()) {
            if (!poolSummary.isValid()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get the full start balance of this period
     *
     * @return The full start balance of this period
     */
    @DisplayProperties(order = 4, dataType = CURRENCY)
    public Double getStartBalance() {
        double value = 0.0;
        for (Bank_Summary bank_summary : new Children_Set<>(Bank_Summary.class, period).get()) {
            value += bank_summary.getStart() * bank_summary.getCurrency().getToPrimary();
        }
        return value;
    }

    /**
     * Get the full end balance of this period
     *
     * @return The full end balance of this period
     */
    @DisplayProperties(order = 5, dataType = CURRENCY)
    public Double getEndBalance() {
        double value = 0.0;
        for (Bank_Summary bank_summary : new Children_Set<>(Bank_Summary.class, period).get()) {
            value += bank_summary.getEnd() * bank_summary.getCurrency().getToPrimary();
        }
        return value;
    }

    /**
     * Get the profit of this period
     *
     * @return The profit of this period
     */
    @DisplayProperties(order = 6, dataType = CURRENCY, dataContext = ZERO_SCALE)
    public Double getProfit() {
        return getEndBalance() - getStartBalance();
    }

    /**
     * Get the end balance of all banks with the listed currency. Return value is in primary currency, not the source currency
     *
     * @param currency The currency to get
     * @return The end balance of all banks with the listed currency
     */
    public Double getEndBalance(Currency currency) {
        double value = 0.0;
        for (Bank bank : new Children_Set<>(Bank.class, currency).get()) {
            Bank_Summary bank_summary = new MultiParent_Set<>(Bank_Summary.class, period, bank).get().get(0);
            value += bank_summary.getEnd() * bank_summary.getCurrency().getToPrimary();
        }

        return value;
    }

    /**
     * Get the remaining money to put into savings
     *
     * @return The remaining money to put into savings
     */
    @DisplayProperties(order = 7, dataType = CURRENCY, dataContext = ZERO_SCALE)
    public Double getSavings() {
        Double sum = 0.0;
        sum += getNonCategory();
        sum -= getTax();
        return sum;
    }

    /**
     * Get the hex debut that needs to be paid for the income made
     *
     * @return The hex debut that needs to be paid for the income made
     */
    @DisplayProperties(order = 8, dataType = CURRENCY)
    public Double getTax() {
        return new PeriodPoolSet_Summary<>(period, TrackingDatabase.get().getSpecialValue(Category.class, Category.INCOME), Transfer.class).getTotal() * -TrackingDatabase.get().getTaxRate();
    }


    /**
     * Get the amount on money not in a category
     *
     * @return The amount on money not in a category
     */
    @DisplayProperties(order = 9, dataType = CURRENCY)
    public Double getNonCategory() {
        double sum = 0.0;
        for (Category category : TrackingDatabase.get().get(Category.class)) {
            sum += new PeriodPoolSet_Summary<>(period, category, Transfer.class).getTotal();
        }
        return -sum;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @DisplayProperties(order = 10)
    public Currency getCurrency() {
        return TrackingDatabase.get().getDefault(Currency.class);
    }
}
