package com.ntankard.Tracking.Dispaly.Util.Comparators;

import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.Bank_Summary;

import java.util.Comparator;

public class BankSummary_Comparator implements Comparator<Bank_Summary> {

    /**
     * {@inheritDoc
     */
    @Override
    public int compare(Bank_Summary o1, Bank_Summary o2) {
        int periodCompare = new Period_Comparator().compare(o1.getPeriod(), o2.getPeriod());
        if(periodCompare == 0){
            if (o1.getPool().getOrder().equals(o2.getPool().getOrder())) {
                return 0;
            } else if (o1.getPool().getOrder() > o2.getPool().getOrder()) {
                return 1;
            }
            return -1;
        }
        return periodCompare;
    }
}
