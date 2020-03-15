package com.ntankard.Tracking.DataBase.Interface.Set.Factory.PoolSummary;

import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.Bank_Summary;

public class BankSummary_Set extends Summary_Set<Bank_Summary, Bank, ExistingPeriod> {

    public BankSummary_Set(Bank coreParent) {
        super(ExistingPeriod.class, Bank.class, coreParent);
    }

    public BankSummary_Set(ExistingPeriod corePeriod) {
        super(ExistingPeriod.class, corePeriod, Bank.class);
    }

    public BankSummary_Set(ExistingPeriod corePeriod, Bank coreParent) {
        super(ExistingPeriod.class, corePeriod, Bank.class, coreParent);
    }

    /**
     * {@inheritDoc
     */
    @Override
    protected Bank_Summary getSummary(ExistingPeriod period, Bank pool) {
        return new Bank_Summary(period, pool);
    }
}
