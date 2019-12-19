package com.ntankard.Tracking.DataBase.Interface.Set.Factory.PoolSummary;

import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.Category_Summary;

public class CategorySummary_Set extends Summary_Set<Category_Summary, Category> {

    public CategorySummary_Set(Category coreParent) {
        super(Category.class, coreParent);
    }

    public CategorySummary_Set(Period corePeriod) {
        super(Category.class, corePeriod);
    }

    public CategorySummary_Set(Period corePeriod, Category coreParent) {
        super(Category.class, corePeriod, coreParent);
    }

    /**
     * {@inheritDoc
     */
    @Override
    protected Category_Summary getSummary(Period period, Category pool) {
        return new Category_Summary(period, pool);
    }
}