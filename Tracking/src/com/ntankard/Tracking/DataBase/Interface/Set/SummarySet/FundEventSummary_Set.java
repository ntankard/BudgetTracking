package com.ntankard.Tracking.DataBase.Interface.Set.SummarySet;

import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Core.Transfers.Transfer;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.FundEvent_Summary;

import java.util.List;

public class FundEventSummary_Set extends Summary_Set<FundEvent_Summary, FundEvent> {

    public FundEventSummary_Set(FundEvent coreParent) {
        super(coreParent);
    }

    public FundEventSummary_Set(Period corePeriod) {
        super(corePeriod);
    }

    public FundEventSummary_Set(Period corePeriod, FundEvent coreParent, Class<? extends Transfer> transferType) {
        super(corePeriod, coreParent, transferType);
    }

    /**
     * {@inheritDoc
     */
    @Override
    protected List<FundEvent> getToSummarise() {
        return TrackingDatabase.get().get(FundEvent.class);
    }

    /**
     * {@inheritDoc
     */
    @Override
    protected FundEvent_Summary getSummary(Period period, FundEvent parent, Class<? extends Transfer> transferType) {
        if (!parent.isActiveThisPeriod(period)) {
            return null;
        }
        return new FundEvent_Summary(period, parent, transferType);
    }
}
