package com.ntankard.Tracking.Dispaly.Util.Comparators;

import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.FundEvent_Summary;

import java.util.Comparator;

public class FundEventSummary_Comparator implements Comparator<FundEvent_Summary> {

    /**
     * {@inheritDoc
     */
    @Override
    public int compare(FundEvent_Summary o1, FundEvent_Summary o2) {
        return new Period_Comparator().compare(o1.getPeriod(), o2.getPeriod());
    }
}
