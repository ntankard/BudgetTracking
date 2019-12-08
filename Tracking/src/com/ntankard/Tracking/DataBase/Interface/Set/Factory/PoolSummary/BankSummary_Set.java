package com.ntankard.Tracking.DataBase.Interface.Set.Factory.PoolSummary;

import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank;
import com.ntankard.Tracking.DataBase.Core.Transfers.Transfer;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.Bank_Summary;

import java.util.List;

public class BankSummary_Set extends Summary_Set<Bank_Summary, Bank> {

    public BankSummary_Set(Bank coreParent) {
        super(coreParent);
    }

    public BankSummary_Set(Period corePeriod) {
        super(corePeriod);
    }

    public BankSummary_Set(Period corePeriod, Bank coreParent, Class<? extends Transfer> transferType) {
        super(corePeriod, coreParent, transferType);
    }

    /**
     * {@inheritDoc
     */
    @Override
    protected List<Bank> getPools() {
        return TrackingDatabase.get().get(Bank.class);
    }

    /**
     * {@inheritDoc
     */
    @Override
    protected Bank_Summary getSummary(Period period, Bank pool) {
        return new Bank_Summary(period, pool);
    }
}
