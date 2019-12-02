package com.ntankard.Tracking.DataBase.Interface.Set.SummarySet;

import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Fund.Fund;
import com.ntankard.Tracking.DataBase.Core.Transfers.Transfer;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.Fund_Summary;

import java.util.List;

public class FundSummary_Set extends Summary_Set<Fund_Summary, Fund> {

    public FundSummary_Set() {
    }

    public FundSummary_Set(Class<? extends Transfer> transferType) {
        super(transferType);
    }

    public FundSummary_Set(Fund coreParent) {
        super(coreParent);
    }

    public FundSummary_Set(Fund coreParent, Class<? extends Transfer> transferType) {
        super(coreParent, transferType);
    }

    public FundSummary_Set(Period corePeriod) {
        super(corePeriod);
    }

    public FundSummary_Set(Period corePeriod, Class<? extends Transfer> transferType) {
        super(corePeriod, transferType);
    }

    public FundSummary_Set(Period corePeriod, Fund coreParent, Class<? extends Transfer> transferType) {
        super(corePeriod, coreParent, transferType);
    }

    /**
     * {@inheritDoc
     */
    @Override
    protected List<Fund> getParents() {
        return TrackingDatabase.get().get(Fund.class);
    }

    /**
     * {@inheritDoc
     */
    @Override
    protected Fund_Summary getSummary(Period period, Fund parent, Class<? extends Transfer> transferType) {
        return new Fund_Summary(period, parent, transferType);
    }
}
