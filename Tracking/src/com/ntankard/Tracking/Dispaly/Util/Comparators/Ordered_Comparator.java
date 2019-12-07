package com.ntankard.Tracking.Dispaly.Util.Comparators;

import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.Ordered;

import java.util.Comparator;

public class Ordered_Comparator implements Comparator<Ordered> {

    /**
     * {@inheritDoc
     */
    @Override
    public int compare(Ordered o1, Ordered o2) {
        if (o1.getOrder().equals(o2.getOrder())) {
            return 0;
        } else if (o1.getOrder() > o2.getOrder()) {
            return 1;
        }
        return -1;
    }
}
