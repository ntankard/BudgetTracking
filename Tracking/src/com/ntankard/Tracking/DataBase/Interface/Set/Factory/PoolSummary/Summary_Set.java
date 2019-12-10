package com.ntankard.Tracking.DataBase.Interface.Set.Factory.PoolSummary;

import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Set.ObjectSet;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.PoolSummary;
import com.ntankard.Tracking.Dispaly.Util.Comparators.Ordered_Comparator;

import java.util.ArrayList;
import java.util.List;

public abstract class Summary_Set<SummaryType extends PoolSummary, PoolType extends Pool> implements ObjectSet<SummaryType> {

    // The objects used to generate the set
    private Class<PoolType> poolTypeClass;
    private Period corePeriod;
    private PoolType corePool;

    protected Summary_Set(Class<PoolType> poolTypeClass, PoolType corePool) {
        this(poolTypeClass, null, corePool);
    }

    protected Summary_Set(Class<PoolType> poolTypeClass, Period corePeriod) {
        this(poolTypeClass, corePeriod, null);
    }

    protected Summary_Set(Class<PoolType> poolTypeClass, Period corePeriod, PoolType corePool) {
        this.poolTypeClass = poolTypeClass;
        this.corePeriod = corePeriod;
        this.corePool = corePool;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public List<SummaryType> get() {
        List<SummaryType> toReturn = new ArrayList<>();

        if (corePool == null && corePeriod == null) {
            for (PoolType pool : getPools()) {
                for (Period period : TrackingDatabase.get().get(Period.class)) {
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
            for (Period period : TrackingDatabase.get().get(Period.class)) {
                SummaryType summary = getSummary(period, corePool);
                if (summary != null) {
                    toReturn.add(summary);
                }
            }
        }

        toReturn.sort(new Ordered_Comparator<>());
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
    protected abstract SummaryType getSummary(Period period, PoolType pool);
}
