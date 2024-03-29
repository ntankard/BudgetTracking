package com.ntankard.budgetTracking.dataBase.interfaces.set.extended.sum;

import com.ntankard.budgetTracking.dataBase.core.Currency;
import com.ntankard.budgetTracking.dataBase.core.transfer.HalfTransfer;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.javaObjectDatabase.util.set.ObjectSet;

import java.util.ArrayList;
import java.util.List;

public class Transfer_SumSet<S extends ObjectSet<HalfTransfer>> {

    /**
     * The source of data
     */
    protected S coreSet;

    /**
     * Core database
     */
    private final Database database;

    /**
     * Constructor
     */
    public Transfer_SumSet(Database database, S coreSet) {
        this.database = database;
        this.coreSet = coreSet;
    }

    /**
     * Get the set of objects
     *
     * @return A set of daa objects
     */
    public List<HalfTransfer> get() {
        return coreSet.get();
    }

    /**
     * Get all the events in this set for a currency
     *
     * @param currency The currency to get
     * @return All the events in this set for a currency
     */
    public List<HalfTransfer> get(Currency currency) {
        List<HalfTransfer> toReturn = new ArrayList<>();
        for (HalfTransfer moneyEvent : get()) {
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
        for (HalfTransfer moneyEvent : get(toSum)) {
            sum += moneyEvent.getValue();
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
        for (HalfTransfer moneyEvent : get()) {
            sum += moneyEvent.getValue() * moneyEvent.getCurrency().getToPrimary();
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
        for (Currency currency : database.get(Currency.class)) {
            if (get(currency).size() != 0) {
                toReturn.add(currency);
            }
        }
        return toReturn;
    }
}
