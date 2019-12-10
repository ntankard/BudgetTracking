package com.ntankard.Tracking.DataBase.Interface.Set.Extended.Sum;

import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Core.Transfers.Transfer;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Set.ObjectSet;

import java.util.ArrayList;
import java.util.List;

public class Transfer_SumSet<T extends Transfer, S extends ObjectSet<T>> implements ObjectSet<T> {

    /**
     * The source of data
     */
    protected S coreSet;

    /**
     * What side of the transaction is this for calculations
     */
    protected Pool pool;

    /**
     * Constructor
     */
    public Transfer_SumSet(S coreSet, Pool pool) {
        this.coreSet = coreSet;
        this.pool = pool;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public List<T> get() {
        return coreSet.get();
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
            if (moneyEvent.isThisSource(pool)) {
                if (moneyEvent.getSourceCurrency().equals(currency)) {
                    toReturn.add(moneyEvent);
                }
            } else if (moneyEvent.isThisDestination(pool)) {
                if (moneyEvent.getDestinationCurrency().equals(currency)) {
                    toReturn.add(moneyEvent);
                }
            } else {
                throw new RuntimeException("Irrelevant transaction in the set");
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
            if (moneyEvent.isThisSource(pool)) {
                sum += moneyEvent.getSourceValue();
            } else if (moneyEvent.isThisDestination(pool)) {
                sum += moneyEvent.getDestinationValue();
            } else {
                throw new RuntimeException("Irrelevant transaction in the set");
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
            if (moneyEvent.isThisSource(pool)) {
                sum += moneyEvent.getSourceValue() * moneyEvent.getSourceCurrency().getToPrimary();
            } else if (moneyEvent.isThisDestination(pool)) {
                sum += moneyEvent.getDestinationValue() * moneyEvent.getDestinationCurrency().getToPrimary();
            } else {
                throw new RuntimeException("Irrelevant transaction in the set");
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
        for (Currency currency : TrackingDatabase.get().get(Currency.class)) {
            if (get(currency).size() != 0) {
                toReturn.add(currency);
            }
        }
        return toReturn;
    }
}