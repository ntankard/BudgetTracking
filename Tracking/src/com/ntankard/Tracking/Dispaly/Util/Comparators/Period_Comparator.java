package com.ntankard.Tracking.Dispaly.Util.Comparators;

import com.ntankard.Tracking.DataBase.Core.Period;

import java.util.Comparator;

public class Period_Comparator implements Comparator<Period> {

    /**
     * {@inheritDoc
     */
    @Override
    public int compare(Period o1, Period o2) {
        if (o1.getYear() > o2.getYear()) {
            return 1;
        }
        if (o1.getYear() < o2.getYear()) {
            return -1;
        }
        return Integer.compare(o1.getMonth(), o2.getMonth());
    }
}
