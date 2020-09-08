package com.ntankard.Tracking.DataBase.Interface.Set.Extended.Sum;

import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Core.Transfer.HalfTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfer.Transfer;
import com.ntankard.Tracking.DataBase.Interface.Set.Filter.TransferDestination_HalfTransfer_Filter;
import com.ntankard.Tracking.DataBase.Interface.Set.Filter.TransferType_HalfTransfer_Filter;
import com.ntankard.Tracking.DataBase.Interface.Set.TwoParent_Children_Set;

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
        this(new TwoParent_Children_Set<>(HalfTransfer.class, period, pool, new TransferType_HalfTransfer_Filter(transferType, new TransferDestination_HalfTransfer_Filter(transferDestination))));
    }

    /**
     * Constructor
     */
    public PeriodPool_SumSet(TwoParent_Children_Set<HalfTransfer, Period, Pool> set) {
        super(set);
    }
}
