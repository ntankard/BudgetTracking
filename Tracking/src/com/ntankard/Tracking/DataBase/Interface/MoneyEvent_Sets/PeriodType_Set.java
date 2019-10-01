package com.ntankard.Tracking.DataBase.Interface.MoneyEvent_Sets;

import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.MoneyEvent;

import java.util.ArrayList;
import java.util.List;

public class PeriodType_Set<T extends MoneyEvent> extends MoneyEvent_Set<T> {

    /**
     * The period to summarise
     */
    private Period period;

    /**
     * The type of object to group
     */
    private Class toGet;

    /**
     * Constructor
     */
    public PeriodType_Set(Period period, Class toGet) {
        this.period = period;
        this.toGet = toGet;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public List<T> getMoneyEvents() {
        List<T> toReturn = new ArrayList<>();
        for (T transaction : period.<T>getChildren(toGet)) {
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
    protected boolean isSource(MoneyEvent moneyEvent) {
        return moneyEvent.isThisSource(period);
    }

    /**
     * {@inheritDoc
     */
    @Override
    protected boolean isDestination(MoneyEvent moneyEvent) {
        return moneyEvent.isThisDestination(period);
    }
}