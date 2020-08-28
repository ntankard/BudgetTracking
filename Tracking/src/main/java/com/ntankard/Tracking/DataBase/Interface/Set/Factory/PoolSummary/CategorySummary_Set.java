package com.ntankard.Tracking.DataBase.Interface.Set.Factory.PoolSummary;

import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.SolidCategory;
import com.ntankard.Tracking.DataBase.Interface.Set.TwoParent_Children_Set;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.Bank_Summary;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.Category_Summary;

public class CategorySummary_Set extends Summary_Set<Category_Summary, SolidCategory, Period> {

    public CategorySummary_Set(SolidCategory coreParent) {
        super(Period.class, SolidCategory.class, coreParent);
    }

    public CategorySummary_Set(Period corePeriod) {
        super(Period.class, corePeriod, SolidCategory.class);
    }

    public CategorySummary_Set(Period corePeriod, SolidCategory coreParent) {
        super(Period.class, corePeriod, SolidCategory.class, coreParent);
    }

    /**
     * {@inheritDoc
     */
    @Override
    protected Category_Summary getSummary(Period period, SolidCategory pool) {
        return new TwoParent_Children_Set<>(Category_Summary.class, period, pool).get().get(0);
    }
}