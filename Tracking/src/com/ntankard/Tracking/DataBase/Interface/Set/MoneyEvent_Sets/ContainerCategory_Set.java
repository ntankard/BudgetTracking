package com.ntankard.Tracking.DataBase.Interface.Set.MoneyEvent_Sets;

import com.ntankard.Tracking.DataBase.Core.DataObject;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.*;

import java.util.ArrayList;
import java.util.List;

public class ContainerCategory_Set extends MoneyEvent_Set<MoneyEvent> {

    /**
     * The period to summarise
     */
    private DataObject period;

    /**
     * The category to filler on
     */
    private DataObject category;

    /**
     * Constructor
     */
    public ContainerCategory_Set(DataObject period, DataObject category) {
        this.period = period;
        this.category = category;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public List<MoneyEvent> get() {
        List<MoneyEvent> toReturn = new ArrayList<>();
        toReturn.addAll(new ContainerCategoryType_Set<>(period, category, Transaction.class).get());
        toReturn.addAll(new ContainerCategoryType_Set<>(period, category, CategoryTransfer.class).get());
        toReturn.addAll(new ContainerCategoryType_Set<>(period, category, PeriodTransfer.class).get());
        toReturn.addAll(new ContainerCategoryType_Set<>(period, category, PeriodFundTransfer.class).get());
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
