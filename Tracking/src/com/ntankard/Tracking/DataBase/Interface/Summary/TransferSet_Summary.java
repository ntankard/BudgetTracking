package com.ntankard.Tracking.DataBase.Interface.Summary;

import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Core.Transfers.Transfer;
import com.ntankard.Tracking.DataBase.Interface.Set.MultiParent_Set;
import com.ntankard.Tracking.DataBase.Interface.Set.ObjectSet;

import java.util.ArrayList;
import java.util.List;

public class TransferSet_Summary<T extends Transfer> implements ObjectSet<T> {

    // The source of data
    private ObjectSet<T> coreSet;

    // What side of the transaction is this for calculations
    private Pool pool;

    /**
     * Constructor
     */
    public TransferSet_Summary(Class<T> toGet, Period period, Pool pool) {
        this(new MultiParent_Set<>(toGet, period, pool), pool);
    }

    /**
     * Constructor
     */
    public TransferSet_Summary(ObjectSet<T> coreSet, Pool pool) {
        this.coreSet = coreSet;
        this.pool = pool;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public List<T> get() {
        List<T> toReturn = new ArrayList<>();
        for (T transaction : coreSet.get()) {
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
            if (!moneyEvent.getSourceCurrency().equals(moneyEvent.getDestinationCurrency())) {
                throw new RuntimeException("Not supported");
            }
            if (moneyEvent.getSourceCurrency().equals(currency)) {
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
                sum += moneyEvent.getSourceValue() * moneyEvent.getSourceCurrency().getToPrimary();
            } else if (isDestination(moneyEvent)) {
                sum += moneyEvent.getDestinationValue() * moneyEvent.getDestinationCurrency().getToPrimary();
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
            if (!moneyEvent.getSourceCurrency().equals(moneyEvent.getDestinationCurrency())) {
                throw new RuntimeException("Not supported");
            }
            Currency currency = moneyEvent.getSourceCurrency();
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
    private boolean isSource(T moneyEvent) {
        return moneyEvent.isThisSource(pool);
    }

    /**
     * Dose this set treat the money event as a destination?
     *
     * @param moneyEvent The event to check
     * @return True if this set treat the money event as a destination
     */
    private boolean isDestination(T moneyEvent) {
        return moneyEvent.isThisDestination(pool);
    }

    /**
     * Set the pool object to filter on
     *
     * @param pool The pool object to filter on
     */
    public void setPool(Pool pool) {
        ((MultiParent_Set<T, Period, Pool>) this.coreSet).setSecondaryParent(pool);
        this.pool = pool;
    }
}
