package com.ntankard.Tracking.Dispaly.Util.Comparators;

import com.ntankard.Tracking.DataBase.Interface.Summary.Period_Summary;

import java.util.Comparator;

public class PeriodSummary_Comparator implements Comparator<Period_Summary> {

    /**
     * {@inheritDoc
     */
    @Override
    public int compare(Period_Summary o1, Period_Summary o2) {
        return new Period_Comparator().compare(o1.getPeriod(), o2.getPeriod());
    }
}
