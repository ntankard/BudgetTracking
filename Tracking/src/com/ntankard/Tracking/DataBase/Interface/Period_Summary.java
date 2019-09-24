package com.ntankard.Tracking.DataBase.Interface;

import com.ntankard.Tracking.DataBase.Core.Base.MoneyEvent;
import com.ntankard.Tracking.DataBase.Core.Category;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Transfers.CategoryTransfer;

import java.util.ArrayList;
import java.util.List;

public abstract class Period_Summary<T extends MoneyEvent> {

    /**
     * The period to summarise
     */
    protected Period period;

    /**
     * The category to filler on
     */
    protected Category category;

    /**
     * Constructor
     *
     * @param period   The period to summarise
     * @param category The category to filler on
     */
    public Period_Summary(Period period, Category category) {
        this.period = period;
        this.category = category;
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

    /**
     * Get all the events tied to the this period category pair
     *
     * @return All the events tied to the this period category pair
     */
    public abstract List<T> getEvents();

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
     * Sum all the transfers for this category, in this period that are in the specified currency
     *
     * @param toSum The currency to sum
     * @return All the transfers for this category, in this period that are in the specified currency
     */
    public abstract double getTotal(Currency toSum);
}
