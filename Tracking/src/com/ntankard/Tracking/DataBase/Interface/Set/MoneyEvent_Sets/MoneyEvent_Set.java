package com.ntankard.Tracking.DataBase.Interface.Set.MoneyEvent_Sets;

import com.ntankard.Tracking.DataBase.Core.MoneyEvents.MoneyEvent;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Currency;
import com.ntankard.Tracking.DataBase.Interface.Set.DataObjectSet;

import java.util.ArrayList;
import java.util.List;

public abstract class MoneyEvent_Set<M extends MoneyEvent> implements DataObjectSet<M> {

    /**
     * Get all the events in this set
     *
     * @return All the events in this set
     */
    public abstract List<M> get();

    /**
     * Get all the events in this set for a currency
     *
     * @param currency The currency to get
     * @return All the events in this set for a currency
     */
    public List<M> get(Currency currency) {
        List<M> toReturn = new ArrayList<>();
        for (M moneyEvent : get()) {
            if (moneyEvent.getCurrency().equals(currency)) {
                toReturn.add(moneyEvent);
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
        for (M moneyEvent : get(toSum)) {
            if (isSource(moneyEvent)) {
                sum -= moneyEvent.getValue();
            } else if (isDestination(moneyEvent)) {
                sum += moneyEvent.getValue();
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
        for (M moneyEvent : get()) {
            if (isSource(moneyEvent)) {
                sum -= moneyEvent.getValue() * moneyEvent.getCurrency().getToPrimary();
            } else if (isDestination(moneyEvent)) {
                sum += moneyEvent.getValue() * moneyEvent.getCurrency().getToPrimary();
            }

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
        for (M moneyEvent : get()) {
            Currency currency = moneyEvent.getCurrency();
            if (!toReturn.contains(currency)) {
                toReturn.add(currency);
            }
        }
        return toReturn;
    }

    /**
     * Dose this set treat the money event as a source?
     *
     * @param moneyEvent The event to check
     * @return True if this set treat the money event as a source
     */
    protected abstract boolean isSource(M moneyEvent);

    /**
     * Dose this set treat the money event as a destination?
     *
     * @param moneyEvent The event to check
     * @return True if this set treat the money event as a destination
     */
    protected abstract boolean isDestination(M moneyEvent);
}
