package com.ntankard.Tracking.DataBase.Interface.Set.Factory.PoolSummary;

import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Core.Transfers.Transfer;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Set.ObjectSet;

import java.util.ArrayList;
import java.util.List;

public abstract class Summary_Set<SummaryType, PoolType extends Pool> implements ObjectSet<SummaryType> {

    // The objects used to generate the set
    private Period corePeriod;
    private PoolType corePool;
    // Class<? extends Transfer> transferType;

    protected Summary_Set(PoolType corePool) {
        this(null, corePool, Transfer.class);
    }

    protected Summary_Set(Period corePeriod) {
        this(corePeriod, null, Transfer.class);
    }

    protected Summary_Set(Period corePeriod, PoolType corePool, Class<? extends Transfer> transferType) {
        this.corePeriod = corePeriod;
        this.corePool = corePool;
        //   this.transferType = transferType;
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

        return toReturn;
    }

    /**
     * Get a list of parent objects used to generate a set
     *
     * @return The parent objects
     */
    protected abstract List<PoolType> getPools();

    /**
     * Build the specific summary object
     *
     * @param period The Period
     * @param pool   The Parent
     * @return A solid summary object
     */
    protected abstract SummaryType getSummary(Period period, PoolType pool);
}
