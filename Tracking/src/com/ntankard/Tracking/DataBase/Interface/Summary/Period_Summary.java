package com.ntankard.Tracking.DataBase.Interface.Summary;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.CurrencyBound;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Core.Pool.Fund.FundEvent.SavingsFundEvent;
import com.ntankard.Tracking.DataBase.Core.Transfers.CategoryFundTransfer.RePayCategoryFundTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfers.Transfer;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Set.Array_Set;
import com.ntankard.Tracking.DataBase.Interface.Set.Children_Set;
import com.ntankard.Tracking.DataBase.Interface.Set.MultiParent_Set;
import com.ntankard.Tracking.DataBase.Interface.Set.ObjectSet;
import com.ntankard.Tracking.DataBase.Interface.Set.SummarySet.BankSummary_Set;
import com.ntankard.Tracking.DataBase.Interface.Set.SummarySet.CategorySummary_Set;
import com.ntankard.Tracking.DataBase.Interface.Set.SummarySet.FundEventSummary_Set;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.Bank_Summary;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.Category_Summary;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.FundEvent_Summary;

import java.util.List;

import static com.ntankard.ClassExtension.DisplayProperties.DataContext.ZERO_SCALE;
import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY;

@ClassExtensionProperties(includeParent = true)
public class Period_Summary implements CurrencyBound {

    /**
     * The period to summarise
     */
    private Period period;

    /**
     * Constructor
     */
    @ParameterMap(shouldSave = false)
    public Period_Summary(Period period) {
        this.period = period;
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

    /**
     * Get the full start balance of this period
     *
     * @return The full start balance of this period
     */
    @DisplayProperties(order = 4, dataType = CURRENCY)
    public Double getStartBalance() {
        double value = 0.0;
        for (Bank_Summary bank_summary : new BankSummary_Set(period).get()) {
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
        for (Bank_Summary bank_summary : new BankSummary_Set(period).get()) {
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
            Bank_Summary summary = new Bank_Summary(period, bank);
            value += summary.getEnd() * summary.getCurrency().getToPrimary();
        }
        return value;
    }

    /**
     * Get the total of all transfers
     *
     * @return The total of all transfers
     */
    @DisplayProperties(order = 7, dataType = CURRENCY, dataContext = ZERO_SCALE)
    public Double getTotal() {
        double sum = 0.0;
        for (Category category : TrackingDatabase.get().get(Category.class)) {
            sum += new TransferSet_Summary<>(Transfer.class, period, category).getTotal();
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

    /**
     * Get the total of all transfers not including the savings transfer
     *
     * @return The total of all transfers not including the savings transfer
     */
    public Double getNonSaveTotal() {
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

            sum += new TransferSet_Summary<>(new Array_Set<>(data), category).getTotal();
        }
        return -sum;
    }
}
