package com.ntankard.Tracking.DataBase.Interface.Summary.Pool;

import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.CurrencyBound;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Core.Transfers.Transfer;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Set.Extended.Sum.PeriodPool_SumSet;

import java.util.List;

import static com.ntankard.ClassExtension.DisplayProperties.DataContext.ZERO_TARGET;
import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY;
import static com.ntankard.ClassExtension.MemberProperties.DEBUG_DISPLAY;
import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

public abstract class PoolSummary<PoolType extends Pool> extends DataObject implements CurrencyBound {

    private Period period;
    private PoolType pool;

    /**
     * Constructor
     */
    protected PoolSummary(Period period, PoolType pool) {
        super(-1);
        this.period = period;
        this.pool = pool;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = DEBUG_DISPLAY, shouldDisplay = false)
    @DisplayProperties(order = 21)
    public List<DataObject> getParents() {
        throw new UnsupportedOperationException();
    }

    // Transfer sum ----------------------------------------------------------------------------------------------------

    @Override
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 4)
    public Currency getCurrency() {
        return TrackingDatabase.get().getDefault(Currency.class);
    }

    @DisplayProperties(order = 8, dataType = CURRENCY)
    public Double getTransferSum() {
        return new PeriodPool_SumSet<>(Transfer.class, getPeriod(), getPool()).getTotal() / getCurrency().getToPrimary();
    }

    // Start End -------------------------------------------------------------------------------------------------------

    @DisplayProperties(order = 5, dataType = CURRENCY)
    public abstract Double getStart();

    @DisplayProperties(order = 6, dataType = CURRENCY)
    public abstract Double getEnd();

    @DisplayProperties(order = 7, dataType = CURRENCY)
    public Double getNet() {
        return getEnd() - getStart();
    }

    // Validity --------------------------------------------------------------------------------------------------------

    @DisplayProperties(order = 9, dataContext = ZERO_TARGET, dataType = CURRENCY)
    public Double getMissing() {
        return Currency.round(getTransferSum() - getNet());
    }

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 10)
    public Boolean isValid() {
        return getMissing().equals(0.00);
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
}
