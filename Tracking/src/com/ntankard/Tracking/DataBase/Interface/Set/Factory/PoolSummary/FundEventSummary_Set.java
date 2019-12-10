package com.ntankard.Tracking.DataBase.Interface.Set.Factory.PoolSummary;

import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.FundEvent_Summary;

public class FundEventSummary_Set extends Summary_Set<FundEvent_Summary, FundEvent> {

    public FundEventSummary_Set(FundEvent coreParent) {
        super(FundEvent.class, coreParent);
    }

    public FundEventSummary_Set(Period corePeriod) {
        super(FundEvent.class, corePeriod);
    }

    public FundEventSummary_Set(Period corePeriod, FundEvent coreParent) {
        super(FundEvent.class, corePeriod, coreParent);
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
