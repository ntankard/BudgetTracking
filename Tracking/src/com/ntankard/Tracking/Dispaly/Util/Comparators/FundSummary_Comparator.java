package com.ntankard.Tracking.Dispaly.Util.Comparators;

import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.Fund_Summary;

import java.util.Comparator;

public class FundSummary_Comparator implements Comparator<Fund_Summary> {

    /**
     * {@inheritDoc
     */
    @Override
    public int compare(Fund_Summary o1, Fund_Summary o2) {
        return new Period_Comparator().compare(o1.getPeriod(), o2.getPeriod());
    }
}
