package com.ntankard.Tracking.DataBase.Interface.Summary.Pool;

import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.CurrencyBound;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Core.Transfers.Transfer;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Summary.TransferSet_Summary;

import static com.ntankard.ClassExtension.DisplayProperties.DataContext.ZERO_TARGET;
import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY;
import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

public abstract class PoolSummary<PoolType extends Pool> implements CurrencyBound {

    // My parents
    private Period period;
    private PoolType pool;

    // My Values
    protected Class<? extends Transfer> transferType;

    /**
     * Constructor
     */
    protected PoolSummary(Period period, PoolType pool) {
        this(period, pool, Transfer.class);
    }

    /**
     * Constructor
     */
    protected PoolSummary(Period period, PoolType pool, Class<? extends Transfer> transferType) {
        this.period = period;
        this.pool = pool;
        this.transferType = transferType;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 2)
    public Period getPeriod() {
        return period;
    }

    @DisplayProperties(order = 3)
    public PoolType getPool() {
        return pool;
    }

    @Override
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 4)
    public Currency getCurrency() {
        return TrackingDatabase.get().getDefault(Currency.class);
    }

    @DisplayProperties(order = 5, dataType = CURRENCY)
    public abstract Double getStart();

    @DisplayProperties(order = 6, dataType = CURRENCY)
    public abstract Double getEnd();

    @DisplayProperties(order = 7, dataType = CURRENCY)
    public Double getExpected() {
        return getEnd() - getStart();
    }

    @DisplayProperties(order = 8, dataType = CURRENCY)
    public Double getTransferSum() {
        return new TransferSet_Summary<>(transferType, getPeriod(), getPool()).getTotal() / getCurrency().getToPrimary();
    }

    @DisplayProperties(order = 9, dataContext = ZERO_TARGET, dataType = CURRENCY)
    public Double getMissing() {
        return Currency.round(getTransferSum() - getExpected());
    }

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 10)
    public Boolean isValid() {
        return getMissing().equals(0.00);
    }
}
