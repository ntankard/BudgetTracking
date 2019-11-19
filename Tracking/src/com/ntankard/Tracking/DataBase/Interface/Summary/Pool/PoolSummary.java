package com.ntankard.Tracking.DataBase.Interface.Summary.Pool;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.CurrencyBound;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Core.Transfers.Transfer;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Summary.PeriodPoolSet_Summary;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.DisplayProperties.DataContext.ZERO_TARGET;
import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY;
import static com.ntankard.ClassExtension.MemberProperties.DEBUG_DISPLAY;
import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public abstract class PoolSummary<PoolType extends Pool> extends DataObject implements CurrencyBound {

    // My parents
    private Period period;
    private PoolType pool;

    /**
     * Constructor
     */
    @ParameterMap(shouldSave = false)
    protected PoolSummary(Integer id, Period period, PoolType pool) {
        super(id);
        this.period = period;
        this.pool = pool;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 21)
    public List<DataObject> getParents() {
        List<DataObject> toReturn = new ArrayList<>();
        toReturn.add(period);
        toReturn.add(pool);
        return toReturn;
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
    public Double getReal() {
        return new PeriodPoolSet_Summary<>(getPeriod(), getPool(), Transfer.class).getTotal() / getCurrency().getToPrimary();
    }

    @DisplayProperties(order = 9, dataContext = ZERO_TARGET, dataType = CURRENCY)
    public Double getMissing() {
        return Currency.round(getReal() - getExpected());
    }

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 10)
    public Boolean isValid() {
        return getMissing().equals(0.00);
    }
}
