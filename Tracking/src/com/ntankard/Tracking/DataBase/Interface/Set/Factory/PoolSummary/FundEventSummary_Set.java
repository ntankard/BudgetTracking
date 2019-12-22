package com.ntankard.Tracking.DataBase.Interface.Set.Factory.PoolSummary;

import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.FundEvent_Summary;

public class FundEventSummary_Set extends Summary_Set<FundEvent_Summary, FundEvent, Period> {

    public FundEventSummary_Set(FundEvent coreParent) {
        super(Period.class, FundEvent.class, coreParent);
    }

    public FundEventSummary_Set(Period corePeriod) {
        super(Period.class, corePeriod, FundEvent.class);
    }

    public FundEventSummary_Set(Period corePeriod, FundEvent coreParent) {
        super(Period.class, corePeriod, FundEvent.class, coreParent);
    }

    /**
     * {@inheritDoc
     */
    @Override
    protected FundEvent_Summary getSummary(Period period, FundEvent pool) {
        if (!pool.isActiveThisPeriod(period)) {
            return null;
        }
        return new FundEvent_Summary(period, pool);
    }
}
