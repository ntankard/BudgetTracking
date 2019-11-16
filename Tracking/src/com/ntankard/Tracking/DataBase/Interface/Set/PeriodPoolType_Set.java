package com.ntankard.Tracking.DataBase.Interface.Set;

import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Transfers.Transfer;

import java.util.ArrayList;
import java.util.List;

public class PeriodPoolType_Set<T extends Transfer>  implements ObjectSet<T> {

    // The objects to filter on
    private Period period;
    private Pool pool;

    /**
     * The type of object to group
     */
    private Class<T> toGet;

    /**
     * Constructor
     */
    public PeriodPoolType_Set(Period period, Pool pool, Class<T> toGet) {
        this.period = period;
        this.pool = pool;
        this.toGet = toGet;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public List<T> get() {
        List<T> toReturn = new ArrayList<>();
        for (T transaction : period.getChildren(toGet)) {
            if (isSource(transaction) || isDestination(transaction)) {
                toReturn.add(transaction);
            }
        }
        return toReturn;
    }

    /**
     * Get all the events in this set for a currency
     *
     * @param currency The currency to get
     * @return All the events in this set for a currency
     */
    public List<T> get(Currency currency) {
        List<T> toReturn = new ArrayList<>();
        for (T moneyEvent : get()) {
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
        for (T moneyEvent : get(toSum)) {
            if (isSource(moneyEvent)) {
                sum += moneyEvent.getSourceValue();
            } else if (isDestination(moneyEvent)) {
                sum += moneyEvent.getDestinationValue();
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
        for (T moneyEvent : get()) {
            if (isSource(moneyEvent)) {
                sum += moneyEvent.getSourceValue() * moneyEvent.getCurrency().getToPrimary();
            } else if (isDestination(moneyEvent)) {
                sum += moneyEvent.getDestinationValue() * moneyEvent.getCurrency().getToPrimary();
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
        for (T moneyEvent : get()) {
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
    protected boolean isSource(T moneyEvent) {
        return moneyEvent.isThisSource(pool);
    }

    /**
     * Dose this set treat the money event as a destination?
     *
     * @param moneyEvent The event to check
     * @return True if this set treat the money event as a destination
     */
    protected boolean isDestination(T moneyEvent) {
        return moneyEvent.isThisDestination(pool);
    }

    /**
     * Set the pool object to filter on
     *
     * @param pool The pool object to filter on
     */
    public void setPool(Pool pool) {
        this.pool = pool;
    }
}
