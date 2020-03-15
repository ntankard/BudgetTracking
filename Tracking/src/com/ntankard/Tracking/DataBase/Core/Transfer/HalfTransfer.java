package com.ntankard.Tracking.DataBase.Core.Transfer;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.DataObject_Field;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Field;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.CurrencyBound;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import java.util.List;

import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY;
import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

/**
 * One half of the transaction
 */
@ClassExtensionProperties(includeParent = true)
public class HalfTransfer extends DataObject implements CurrencyBound {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static List<Field<?>> getFields(Integer id, Period period, Pool pool, Currency currency, Transfer transfer, DataObject container) {
        List<Field<?>> toReturn = DataObject.getFields(id, container);
        toReturn.add(new DataObject_Field<>("period", Period.class, period, container));
        toReturn.add(new DataObject_Field<>("pool", Pool.class, pool, container));
        toReturn.add(new DataObject_Field<>("currency", Currency.class, currency, container));
        toReturn.add(new DataObject_Field<>("transfer", Transfer.class, transfer, container));
        return toReturn;
    }

    /**
     * Constructor
     */
    @ParameterMap(shouldSave = false)
    public HalfTransfer(Integer id, Period period, Pool pool, Currency currency, Transfer transfer) {
        super();
        setFields(getFields(id, period, pool, currency, transfer, this));
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
        return get("period");
    }

    @DisplayProperties(order = 1200000)
    public Pool getPool() {
        return get("pool");
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
        return get("currency");
    }

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 1500000)
    public Transfer getTransfer() {
        return get("transfer");
    }

    // 2000000--getParents (Above)
    // 3000000--getChildren

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @SetterProperties(localSourceMethod = "sourceOptions", displaySet = false)
    public void setPeriod(Period period) {
        set("period", period);
    }

    @SetterProperties(localSourceMethod = "sourceOptions", displaySet = false)
    public void setPool(Pool pool) {
        set("pool", pool);
    }

    @SetterProperties(localSourceMethod = "sourceOptions", displaySet = false)
    public void setCurrency(Currency currency) {
        set("currency", currency);
    }
}
