package com.ntankard.Tracking.DataBase.Interface.Set.SummarySet;

import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank;
import com.ntankard.Tracking.DataBase.Core.Transfers.Transfer;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.Bank_Summary;

import java.util.List;

public class BankSummary_Set extends Summary_Set<Bank_Summary, Bank> {

    public BankSummary_Set() {
    }

    public BankSummary_Set(Class<? extends Transfer> transferType) {
        super(transferType);
    }

    public BankSummary_Set(Bank coreParent) {
        super(coreParent);
    }

    public BankSummary_Set(Bank coreParent, Class<? extends Transfer> transferType) {
        super(coreParent, transferType);
    }

    public BankSummary_Set(Period corePeriod) {
        super(corePeriod);
    }

    public BankSummary_Set(Period corePeriod, Class<? extends Transfer> transferType) {
        super(corePeriod, transferType);
    }

    public BankSummary_Set(Period corePeriod, Bank coreParent, Class<? extends Transfer> transferType) {
        super(corePeriod, coreParent, transferType);
    }

    /**
     * {@inheritDoc
     */
    @Override
    protected List<Bank> getParents() {
        return TrackingDatabase.get().get(Bank.class);
    }

    /**
     * {@inheritDoc
     */
    @Override
    protected Bank_Summary getSummary(Period period, Bank parent, Class<? extends Transfer> transferType) {
        return new Bank_Summary(period, parent, transferType);
    }
}
