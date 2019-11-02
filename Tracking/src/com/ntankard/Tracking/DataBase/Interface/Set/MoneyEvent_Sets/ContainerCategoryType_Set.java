package com.ntankard.Tracking.DataBase.Interface.Set.MoneyEvent_Sets;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.MoneyEvent;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.Transaction;

import java.util.ArrayList;
import java.util.List;

public class ContainerCategoryType_Set<T extends MoneyEvent> extends MoneyEvent_Set<T> {

    /**
     * The period to summarise
     */
    private DataObject period;

    /**
     * The category to filler on
     */
    private DataObject category;

    /**
     * The type of object to group
     */
    private Class<T> toGet;

    /**
     * Constructor
     */
    public ContainerCategoryType_Set(DataObject period, DataObject category, Class<T> toGet) {
        this.period = period;
        this.category = category;
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

        if (toGet.equals(Transaction.class)) {
            toReturn.sort((o1, o2) -> {
                Transaction t1 = (Transaction) o1;
                Transaction t2 = (Transaction) o2;
                if (t1.getSourceContainer().getBank().getOrder() == t2.getSourceContainer().getBank().getOrder()) {
                    return 0;
                } else if (t1.getSourceContainer().getBank().getOrder() > t2.getSourceContainer().getBank().getOrder()) {
                    return 1;
                }
                return -1;
            });
        }

        return toReturn;
    }

    /**
     * {@inheritDoc
     */
    @Override
    protected boolean isSource(MoneyEvent moneyEvent) {
        return moneyEvent.isThisSource(period, category);
    }

    /**
     * {@inheritDoc
     */
    @Override
    protected boolean isDestination(MoneyEvent moneyEvent) {
        return moneyEvent.isThisDestination(period, category);
    }
}
