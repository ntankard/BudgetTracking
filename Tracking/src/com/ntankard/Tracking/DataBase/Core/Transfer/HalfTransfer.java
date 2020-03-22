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
@ParameterMap(shouldSave = false)
@ClassExtensionProperties(includeParent = true)
public class HalfTransfer extends DataObject implements CurrencyBound {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static List<Field<?>> getFields() {
        List<Field<?>> toReturn = DataObject.getFields();
        toReturn.add(new DataObject_Field<>("getPeriod", Period.class));
        toReturn.add(new DataObject_Field<>("getPool", Pool.class));
        toReturn.add(new DataObject_Field<>("getCurrency", Currency.class));
        toReturn.add(new DataObject_Field<>("getTransfer", Transfer.class));
        return toReturn;
    }

    /**
     * Create a new HalfTransfer object
     */
    public static HalfTransfer make(Integer id, Period period, Pool pool, Currency currency, Transfer transfer) {
        return assembleDataObject(HalfTransfer.getFields(), new HalfTransfer()
                , "getId", id
                , "getPeriod", period
                , "getPool", pool
                , "getCurrency", currency
                , "getTransfer", transfer
        );
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
        return get("getPeriod");
    }

    @DisplayProperties(order = 1200000)
    public Pool getPool() {
        return get("getPool");
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
        return get("getCurrency");
    }

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 1500000)
    public Transfer getTransfer() {
        return get("getTransfer");
    }

    // 2000000--getParents (Above)
    // 3000000--getChildren

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @SetterProperties(localSourceMethod = "sourceOptions", displaySet = false)
    public void setPeriod(Period period) {
        set("getPeriod", period);
    }

    @SetterProperties(localSourceMethod = "sourceOptions", displaySet = false)
    public void setPool(Pool pool) {
        set("getPool", pool);
    }

    @SetterProperties(localSourceMethod = "sourceOptions", displaySet = false)
    public void setCurrency(Currency currency) {
        set("getCurrency", currency);
    }
}
