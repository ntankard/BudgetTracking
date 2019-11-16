package com.ntankard.Tracking.DataBase.Core.Pool.Bank;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.CurrencyBound;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.DEBUG_DISPLAY;
import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class Bank extends Pool implements CurrencyBound {

    // My parents
    private Currency currency;

    // My values
    private Integer order;
    private Double start = 0.0;

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getName", "getCurrency", "getStart", "getOrder"})
    public Bank(Integer id, String name, Currency currency, Double start, Integer order) {
        super(id, name);
        this.currency = currency;
        this.start = start;
        this.order = order;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 21)
    public List<DataObject> getParents() {
        List<DataObject> toReturn = new ArrayList<>();
        toReturn.add(currency);
        return toReturn;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @DisplayProperties(order = 3)
    public Currency getCurrency() {
        return currency;
    }

    @DisplayProperties(order = 4)
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public Integer getOrder() {
        return order;
    }

    @DisplayProperties(order = 5)
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public Double getStart() {
        return start;
    }
}
