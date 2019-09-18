package com.ntankard.Tracking.DataBase.Interface;

import com.ntankard.Tracking.DataBase.Core.Category;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Transfers.NonPeriodFundTransfer;

import java.util.ArrayList;
import java.util.List;

public class Period_SummaryNonPeriodFundTransfer extends Period_Summary<NonPeriodFundTransfer> {

    /**
     * All the NonPeriodFundTransfers for this period (not yet filtered by category)
     */
    private List<NonPeriodFundTransfer> nonPeriodFundTransfers;

    /**
     * Constructor
     *
     * @param period   The period to summarise
     * @param category The category to filler on
     */
    public Period_SummaryNonPeriodFundTransfer(Period period, Category category, List<NonPeriodFundTransfer> nonPeriodFundTransfers) {
        super(period, category);
        this.nonPeriodFundTransfers = nonPeriodFundTransfers;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public List<Currency> getCurrencies() {
        List<Currency> toReturn = new ArrayList<>();
        for (NonPeriodFundTransfer nonPeriodFundTransfer : getEvents()) {
            Currency currency = nonPeriodFundTransfer.getCurrency();
            if (!toReturn.contains(currency)) {
                toReturn.add(currency);
            }
        }
        return toReturn;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public List<NonPeriodFundTransfer> getEvents() {
        List<NonPeriodFundTransfer> toReturn = new ArrayList<>();
        for (NonPeriodFundTransfer nonPeriodFundTransfer : nonPeriodFundTransfers) {
            if (nonPeriodFundTransfer.getSourceCategory().equals(category)) {
                toReturn.add(nonPeriodFundTransfer);
            }
        }
        return toReturn;
    }

    /**
     * Sum all the NonPeriodFundTransfer for this category, in this period that are in the specified currency
     *
     * @param toSum The currency to sum
     * @return All the NonPeriodFundTransfer for this category, in this period that are in the specified currency
     */
    public double getTotal(Currency toSum) {
        double sum = 0;
        for (NonPeriodFundTransfer nonPeriodFundTransfer : getEvents()) {
            if (nonPeriodFundTransfer.getCurrency().equals(toSum)) {
                if (nonPeriodFundTransfer.getSource().equals(period)) {
                    sum -= nonPeriodFundTransfer.getValue();
                } else {
                    sum += nonPeriodFundTransfer.getValue();
                }
            }
        }
        return sum;
    }

    /**
     * Sum all the NonPeriodFundTransfer for this category, in this period. Return in the primary currency
     *
     * @return All the NonPeriodFundTransfer for this category, in this period. Return in the primary currency
     */
    public double getTotal() {
        double sum = 0;
        for (Currency currency : getCurrencies()) {
            sum += getTotal(currency) * currency.getToPrimary();
        }
        return sum;
    }
}
