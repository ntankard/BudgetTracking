package com.ntankard.Tracking.DataBase.Interface.Set.MoneyEvent_Sets;

import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.MoneyEvent;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;

import java.util.ArrayList;
import java.util.List;

public class PeriodPoolType_Set<T extends MoneyEvent> extends MoneyEvent_Set<T> {

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
     * {@inheritDoc
     */
    @Override
    protected boolean isSource(T moneyEvent) {
        return moneyEvent.isThisSource(pool);
    }

    /**
     * {@inheritDoc
     */
    @Override
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
