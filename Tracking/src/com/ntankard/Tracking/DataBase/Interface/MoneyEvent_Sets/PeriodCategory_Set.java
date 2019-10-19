package com.ntankard.Tracking.DataBase.Interface.MoneyEvent_Sets;

import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.*;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Category;

import java.util.ArrayList;
import java.util.List;

public class PeriodCategory_Set extends MoneyEvent_Set<MoneyEvent> {

    /**
     * The period to summarise
     */
    private Period period;

    /**
     * The category to filler on
     */
    private Category category;

    /**
     * Constructor
     */
    public PeriodCategory_Set(Period period, Category category) {
        this.period = period;
        this.category = category;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public List<MoneyEvent> getMoneyEvents() {
        List<MoneyEvent> toReturn = new ArrayList<>();
        toReturn.addAll(new PeriodCategoryType_Set<>(period, category, Transaction.class).getMoneyEvents());
        toReturn.addAll(new PeriodCategoryType_Set<>(period, category, CategoryTransfer.class).getMoneyEvents());
        toReturn.addAll(new PeriodCategoryType_Set<>(period, category, PeriodTransfer.class).getMoneyEvents());
        toReturn.addAll(new PeriodCategoryType_Set<>(period, category, PeriodFundTransfer.class).getMoneyEvents());
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
