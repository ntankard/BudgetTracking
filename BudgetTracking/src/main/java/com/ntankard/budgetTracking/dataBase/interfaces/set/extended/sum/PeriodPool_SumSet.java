package com.ntankard.budgetTracking.dataBase.interfaces.set.extended.sum;

import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.budgetTracking.dataBase.core.period.Period;
import com.ntankard.budgetTracking.dataBase.core.pool.Pool;
import com.ntankard.budgetTracking.dataBase.core.transfer.HalfTransfer;
import com.ntankard.budgetTracking.dataBase.core.transfer.Transfer;
import com.ntankard.budgetTracking.dataBase.interfaces.set.filter.TransferDestination_HalfTransfer_Filter;
import com.ntankard.budgetTracking.dataBase.interfaces.set.filter.TransferType_HalfTransfer_Filter;
import com.ntankard.javaObjectDatabase.util.set.TwoParent_Children_Set;

public class PeriodPool_SumSet extends Transfer_SumSet<TwoParent_Children_Set<HalfTransfer, Period, Pool>> {

    /**
     * Constructor
     */
    public PeriodPool_SumSet(Period period, Pool pool) {
        this(Transfer.class, Pool.class, period, pool);
    }

    /**
     * Constructor
     */
    public PeriodPool_SumSet(Class<? extends Transfer> transferType, Class<? extends Pool> transferDestination, Period period, Pool pool) {
        this(period.getTrackingDatabase(), new TwoParent_Children_Set<>(HalfTransfer.class, period, pool, new TransferType_HalfTransfer_Filter(transferType, new TransferDestination_HalfTransfer_Filter(transferDestination))));
    }

    /**
     * Constructor
     */
    public PeriodPool_SumSet(Database database, TwoParent_Children_Set<HalfTransfer, Period, Pool> set) {
        super(database, set);
    }
}
