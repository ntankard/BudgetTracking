package com.ntankard.Tracking.DataBase.Interface;

import com.ntankard.Tracking.DataBase.Core.Base.MoneyEvent;
import com.ntankard.Tracking.DataBase.Core.Category;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;

import java.util.ArrayList;
import java.util.List;

public class Period_SummaryTransfer<T extends MoneyEvent> {

    /**
     * All the MoneyEvents for this period (not yet filtered by category)
     */
    private List<T> transfers;

    /**
     * The period to summarise
     */
    private Period period;

    /**
     * The category to filler on
     */
    private Category category;

    /**
     * Constructor
     *
     * @param period    The period to summarise
     * @param category  The category to filler on
     * @param transfers All the MoneyEvents for this period (not yet filtered by category)
     */
    public Period_SummaryTransfer(Period period, Category category, List<T> transfers) {
        this.period = period;
        this.category = category;
        this.transfers = transfers;
    }

    /**
     * Get all the events tied to the this period category pair
     *
     * @return All the events tied to the this period category pair
     */
    public List<T> getEvents() {
        List<T> toReturn = new ArrayList<>();
        for (T periodTransfer : transfers) {
            if (periodTransfer.isThisDestination(period, category) || periodTransfer.isThisSource(period, category)) {
                toReturn.add(periodTransfer);
            }
        }
        return toReturn;
    }

    /**
     * Sum all the transfers for this category, in this period that are in the specified currency
     *
     * @param toSum The currency to sum
     * @return All the transfers for this category, in this period that are in the specified currency
     */
    public double getTotal(Currency toSum) {
        double sum = 0;
        for (T nonPeriodFundTransfer : getEvents()) {
            if (nonPeriodFundTransfer.getCurrency().equals(toSum)) {
                if (nonPeriodFundTransfer.isThisSource(period, category)) {
                    sum -= nonPeriodFundTransfer.getValue();
                } else if (nonPeriodFundTransfer.isThisDestination(period, category)) {
                    sum += nonPeriodFundTransfer.getValue();
                }
            }
        }
        return sum;
    }

    /**
     * Sum all the transfers for this category, in this period. Return in the primary currency
     *
     * @return All the transfers for this category, in this period. Return in the primary currency
     */
    public double getTotal() {
        double sum = 0;
        for (Currency currency : getCurrencies()) {
            sum += getTotal(currency) * currency.getToPrimary();
        }
        return sum;
    }

    /**
     * Get a list of all the currencies used in the events
     *
     * @return A list of all the currencies used in the events
     */
    public List<Currency> getCurrencies() {
        List<Currency> toReturn = new ArrayList<>();
        for (T moneyEvent : getEvents()) {
            Currency currency = moneyEvent.getCurrency();
            if (!toReturn.contains(currency)) {
                toReturn.add(currency);
            }
        }
        return toReturn;
    }
}
