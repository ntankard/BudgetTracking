package com.ntankard.Tracking.DataBase.Interface.Set.SummarySet;

import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Core.Transfers.Transfer;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.Category_Summary;

import java.util.List;

public class CategorySummary_Set extends Summary_Set<Category_Summary, Category> {

    public CategorySummary_Set(Category coreParent) {
        super(coreParent);
    }

    public CategorySummary_Set(Period corePeriod) {
        super(corePeriod);
    }

    public CategorySummary_Set(Period corePeriod, Category coreParent, Class<? extends Transfer> transferType) {
        super(corePeriod, coreParent, transferType);
    }

    /**
     * {@inheritDoc
     */
    @Override
    protected List<Category> getPools() {
        return TrackingDatabase.get().get(Category.class);
    }

    /**
     * {@inheritDoc
     */
    @Override
    protected Category_Summary getSummary(Period period, Category pool, Class<? extends Transfer> transferType) {
        return new Category_Summary(period, pool, transferType);
    }
}