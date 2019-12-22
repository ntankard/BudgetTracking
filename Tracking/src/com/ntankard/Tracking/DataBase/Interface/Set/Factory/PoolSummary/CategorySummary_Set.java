package com.ntankard.Tracking.DataBase.Interface.Set.Factory.PoolSummary;

import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.Category_Summary;

public class CategorySummary_Set extends Summary_Set<Category_Summary, Category, Period> {

    public CategorySummary_Set(Category coreParent) {
        super(Period.class, Category.class, coreParent);
    }

    public CategorySummary_Set(Period corePeriod) {
        super(Period.class, corePeriod, Category.class);
    }

    public CategorySummary_Set(Period corePeriod, Category coreParent) {
        super(Period.class, corePeriod, Category.class, coreParent);
    }

    /**
     * {@inheritDoc
     */
    @Override
    protected Category_Summary getSummary(Period period, Category pool) {
        return new Category_Summary(period, pool);
    }
}