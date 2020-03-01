package com.ntankard.Tracking.DataBase.Core.Pool.Bank;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.CurrencyBound;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.HasDefault;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.Ordered;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Core.StatementEnd;
import com.ntankard.Tracking.DataBase.Database.ObjectFactory;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Set.TwoParent_Children_Set;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.*;

@ClassExtensionProperties(includeParent = true)
@ObjectFactory(builtObjects = {StatementEnd.class})
public class Bank extends Pool implements CurrencyBound, Ordered, HasDefault {

    // My parents
    private Currency currency;

    // My values
    private Double start;
    private Integer order;
    private Boolean isDefault;

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getName", "getOrder", "getCurrency", "getStart", "isDefault"})
    public Bank(Integer id, String name, Integer order, Currency currency, Double start, Boolean isDefault) {
        super(id, name);
        if (order == null) throw new IllegalArgumentException("Order is null");
        if (currency == null) throw new IllegalArgumentException("Currency is null");
        if (start == null) throw new IllegalArgumentException("Start is null");
        if (isDefault == null) throw new IllegalArgumentException("Default is null");
        this.order = order;
        this.currency = currency;
        this.start = start;
        this.isDefault = isDefault;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 2000000)
    public List<DataObject> getParents() {
        List<DataObject> toReturn = new ArrayList<>();
        toReturn.add(getCurrency());
        return toReturn;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void add() {
        super.add();

        for (ExistingPeriod period : TrackingDatabase.get().get(ExistingPeriod.class)) {
            if (new TwoParent_Children_Set<>(StatementEnd.class, this, period).get().size() > 1) {
                throw new RuntimeException("More than 1 statement end");
            }
            if (new TwoParent_Children_Set<>(StatementEnd.class, this, period).get().size() == 0) {
                new StatementEnd(TrackingDatabase.get().getNextId(), period, this, 0.0).add();
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    // 1000000--getID
    // 1100000----getName

    @DisplayProperties(order = 1101000)
    public Currency getCurrency() {
        return currency;
    }

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 1102000)
    public Double getStart() {
        return start;
    }

    @Override
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 1103000)
    public Boolean isDefault() {
        return isDefault;
    }

    @Override
    @MemberProperties(verbosityLevel = TRACE_DISPLAY)
    @DisplayProperties(order = 1104000)
    public Integer getOrder() {
        return order;
    }

    // 2000000--getParents (Above)
    // 3000000--getChildren
}
