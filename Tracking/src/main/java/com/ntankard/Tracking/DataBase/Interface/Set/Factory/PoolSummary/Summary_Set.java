package com.ntankard.Tracking.DataBase.Interface.Set.Factory.PoolSummary;

import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Set.ObjectSet;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.PoolSummary;

import java.util.ArrayList;
import java.util.List;

public abstract class Summary_Set<SummaryType extends PoolSummary, PoolType extends Pool, PeriodType extends Period> extends ObjectSet<SummaryType> {

    // The objects used to generate the set
    private Class<PeriodType> periodTypeClass;
    private PeriodType corePeriod;
    private Class<PoolType> poolTypeClass;
    private PoolType corePool;

    protected Summary_Set(Class<PeriodType> periodTypeClass, Class<PoolType> poolTypeClass, PoolType corePool) {
        this(periodTypeClass, null, poolTypeClass, corePool);
    }

    protected Summary_Set(Class<PeriodType> periodTypeClass, PeriodType corePeriod, Class<PoolType> poolTypeClass) {
        this(periodTypeClass, corePeriod, poolTypeClass, null);
    }

    protected Summary_Set(Class<PeriodType> periodTypeClass, PeriodType corePeriod, Class<PoolType> poolTypeClass, PoolType corePool) {
        super(null);
        this.poolTypeClass = poolTypeClass;
        this.corePool = corePool;
        this.corePeriod = corePeriod;
        this.periodTypeClass = periodTypeClass;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public List<SummaryType> get() {
        List<SummaryType> toReturn = new ArrayList<>();

        if (corePool == null && corePeriod == null) {
            for (PoolType pool : getPools()) {
                for (PeriodType period : TrackingDatabase.get().get(periodTypeClass)) {
                    SummaryType summary = getSummary(period, pool);
                    if (summary != null) {
                        toReturn.add(summary);
                    }
                }
            }
        } else if (corePool == null) {
            for (PoolType pool : getPools()) {
                SummaryType summary = getSummary(corePeriod, pool);
                if (summary != null) {
                    toReturn.add(summary);
                }
            }
        } else {
            for (PeriodType period : TrackingDatabase.get().get(periodTypeClass)) {
                SummaryType summary = getSummary(period, corePool);
                if (summary != null) {
                    toReturn.add(summary);
                }
            }
        }

        return toReturn;
    }

    /**
     * Get a list of parent objects used to generate a set
     *
     * @return The parent objects
     */
    private List<PoolType> getPools() {
        return TrackingDatabase.get().get(poolTypeClass);
    }

    /**
     * Build the specific summary object
     *
     * @param period The Period
     * @param pool   The Parent
     * @return A solid summary object
     */
    protected abstract SummaryType getSummary(PeriodType period, PoolType pool);
}
