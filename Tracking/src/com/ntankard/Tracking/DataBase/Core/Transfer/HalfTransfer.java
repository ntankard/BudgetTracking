package com.ntankard.Tracking.DataBase.Core.Transfer;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.CurrencyBound;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY;
import static com.ntankard.ClassExtension.MemberProperties.DEBUG_DISPLAY;
import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

/**
 * One half of the transaction
 */
@ClassExtensionProperties(includeParent = true)
public class HalfTransfer extends DataObject implements CurrencyBound {

    // My parents
    private Period period;
    private Pool pool;
    private Currency currency;
    private Transfer transfer;

    /**
     * Constructor
     */
    @ParameterMap(shouldSave = false)
    public HalfTransfer(Integer id, Period period, Pool pool, Currency currency, Transfer transfer) {
        super(id);
        this.period = period;
        this.pool = pool;
        this.currency = currency;
        this.transfer = transfer;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 2000000)
    public List<DataObject> getParents() {
        List<DataObject> toReturn = new ArrayList<>();
        toReturn.add(getPeriod());
        toReturn.add(getPool());
        toReturn.add(getCurrency());
        toReturn.add(getTransfer());
        return toReturn;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void remove() {
        remove_impl();
    }

    /**
     * {@inheritDoc
     */
    @Override
    public String toString() {
        return getPeriod().toString() + " " + getPool().toString() + " " + getCurrency().getNumberFormat().format(getValue());
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    // 1000000--getID

    @DisplayProperties(order = 1100000)
    public Period getPeriod() {
        return period;
    }

    @DisplayProperties(order = 1200000)
    public Pool getPool() {
        return pool;
    }

    @DisplayProperties(order = 1300000, dataType = CURRENCY)
    public Double getValue() {
        if (getTransfer().getSourceTransfer() == this) {
            return getTransfer().getValue(true);
        } else {
            return getTransfer().getValue(false);
        }
    }

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 1400000)
    public Currency getCurrency() {
        return currency;
    }

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 1500000)
    public Transfer getTransfer() {
        return transfer;
    }

    // 2000000--getParents (Above)
    // 3000000--getChildren

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @SetterProperties(localSourceMethod = "sourceOptions", displaySet = false)
    public void setPeriod(Period period) {
        if (period == null) throw new IllegalArgumentException("Period is null");
        this.period.notifyChildUnLink(this);
        this.period = period;
        this.period.notifyChildLink(this);
        validateParents();
    }

    @SetterProperties(localSourceMethod = "sourceOptions", displaySet = false)
    public void setPool(Pool pool) {
        if (pool == null) throw new IllegalArgumentException("Pool is null");
        this.pool.notifyChildUnLink(this);
        this.pool = pool;
        this.pool.notifyChildLink(this);
        validateParents();
    }

    @SetterProperties(localSourceMethod = "sourceOptions", displaySet = false)
    public void setCurrency(Currency currency) {
        if (currency == null) throw new IllegalArgumentException("Currency is null");
        this.currency.notifyChildUnLink(this);
        this.currency = currency;
        this.currency.notifyChildLink(this);
        validateParents();
    }
}
