package com.ntankard.Tracking.DataBase.Interface.Set.Extended.Sum;

import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Core.Transfers.Transfer;
import com.ntankard.Tracking.DataBase.Interface.Set.MultiParent_Set;

public class PeriodPool_SumSet<T extends Transfer> extends Transfer_SumSet<T, MultiParent_Set<T, Period, Pool>> {

    /**
     * Constructor
     */
    public PeriodPool_SumSet(Class<T> toGet, Period period, Pool pool) {
        super(new MultiParent_Set<>(toGet, period, pool), pool);
    }

    /**
     * Set the pool object to filter on
     *
     * @param pool The pool object to filter on
     */
    public void setPool(Pool pool) {
        this.coreSet.setSecondaryParent(pool);
        this.pool = pool;
    }
}
