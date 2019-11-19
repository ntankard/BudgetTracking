package com.ntankard.Tracking.Dispaly.Util.Comparators;

import com.ntankard.Tracking.DataBase.Core.Pool.Bank.StatementEnd;

import java.util.Comparator;

public class StatementEnd_Comparator implements Comparator<StatementEnd> {

    /**
     * {@inheritDoc
     */
    @Override
    public int compare(StatementEnd o1, StatementEnd o2) {
        if (o1.getBank().getOrder().equals(o2.getBank().getOrder())) {
            return 0;
        } else if (o1.getBank().getOrder() > o2.getBank().getOrder()) {
            return 1;
        }
        return -1;
    }
}
