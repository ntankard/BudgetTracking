package com.ntankard.Tracking.DataBase.Interface.Set.Factory.PoolSummary;

import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.Bank_Summary;

public class BankSummary_Set extends Summary_Set<Bank_Summary, Bank> {

    public BankSummary_Set(Bank coreParent) {
        super(Bank.class, coreParent);
    }

    public BankSummary_Set(Period corePeriod) {
        super(Bank.class, corePeriod);
    }

    public BankSummary_Set(Period corePeriod, Bank coreParent) {
        super(Bank.class, corePeriod, coreParent);
    }

    /**
     * {@inheritDoc
     */
    @Override
    protected Bank_Summary getSummary(Period period, Bank pool) {
        return new Bank_Summary(period, pool);
    }
}
