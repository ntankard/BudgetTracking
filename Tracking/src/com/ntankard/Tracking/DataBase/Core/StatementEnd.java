package com.ntankard.Tracking.DataBase.Core;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.DataObject_Field;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Field;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.CurrencyBound;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.Ordered;
import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank;

import java.util.List;

import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY;
import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;
import static com.ntankard.ClassExtension.MemberProperties.TRACE_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class StatementEnd extends DataObject implements CurrencyBound, Ordered {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static List<Field<?>> getFields() {
        List<Field<?>> toReturn = DataObject.getFields();
        toReturn.add(new DataObject_Field<>("getPeriod", ExistingPeriod.class));
        toReturn.add(new DataObject_Field<>("getBank", Bank.class));
        toReturn.add(new Field<>("getEnd", Double.class));
        return toReturn;
    }

    /**
     * Create a new StatementEnd object
     */
    public static StatementEnd make(Integer id, ExistingPeriod period, Bank bank, Double end) {
        return assembleDataObject(StatementEnd.getFields(), new StatementEnd()
                , "getId", id
                , "getPeriod", period
                , "getBank", bank
                , "getEnd", end
        );
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    // 1000000--getID

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 1100000)
    public ExistingPeriod getPeriod() {
        return get("getPeriod");
    }

    @DisplayProperties(order = 1200000)
    public Bank getBank() {
        return get("getBank");
    }

    @DisplayProperties(order = 1300000, dataType = CURRENCY)
    public Double getEnd() {
        return get("getEnd");
    }

    @Override
    @DisplayProperties(order = 1400000)
    public Currency getCurrency() {
        return getBank().getCurrency();
    }

    @Override
    @MemberProperties(verbosityLevel = TRACE_DISPLAY)
    @DisplayProperties(order = 1500000)
    public Integer getOrder() {
        return getBank().getOrder() + getPeriod().getOrder() * 1000;
    }

    // 2000000--getParents (Above)
    // 3000000--getChildren

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public void setEnd(Double end) {
        set("getEnd", end);
    }
}
