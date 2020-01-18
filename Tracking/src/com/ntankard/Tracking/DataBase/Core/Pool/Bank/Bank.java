package com.ntankard.Tracking.DataBase.Core.Pool.Bank;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.CurrencyBound;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Database.ObjectFactory;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.DEBUG_DISPLAY;
import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

@ClassExtensionProperties(includeParent = true)
@ObjectFactory(builtObjects = {StatementEnd.class})
public class Bank extends Pool implements CurrencyBound {

    // My parents
    private Currency currency;

    // My values
    private Double start;

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getName", "getOrder", "getCurrency", "getStart"})
    public Bank(Integer id, String name, Integer order, Currency currency, Double start) {
        super(id, name, order);
        if (currency == null) throw new IllegalArgumentException("Currency is null");
        this.currency = currency;
        this.start = start;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 21)
    public List<DataObject> getParents() {
        List<DataObject> toReturn = new ArrayList<>();
        toReturn.add(getCurrency());
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
    public Double getStart() {
        return start;
    }
}
