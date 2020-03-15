package com.ntankard.Tracking.DataBase.Core.Pool;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.DataObject_Field;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Field;
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

import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;
import static com.ntankard.ClassExtension.MemberProperties.TRACE_DISPLAY;

@ClassExtensionProperties(includeParent = true)
@ObjectFactory(builtObjects = {StatementEnd.class})
public class Bank extends Pool implements CurrencyBound, Ordered, HasDefault {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static List<Field<?>> getFields(Integer id, String name, Integer order, Currency currency, Double start, Boolean isDefault, DataObject container) {
        List<Field<?>> toReturn = Pool.getFields(id, name, container);
        toReturn.add(new Field<>("order", Integer.class, order, container));
        toReturn.add(new DataObject_Field<>("currency", Currency.class, currency, container));
        toReturn.add(new Field<>("start", Double.class, start, container));
        toReturn.add(new Field<>("isDefault", Boolean.class, isDefault, container));
        return toReturn;
    }

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getName", "getOrder", "getCurrency", "getStart", "isDefault"})
    public Bank(Integer id, String name, Integer order, Currency currency, Double start, Boolean isDefault) {
        super();
        setFields(getFields(id, name, order, currency, start, isDefault, this));
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
        return get("currency");
    }

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 1102000)
    public Double getStart() {
        return get("start");
    }

    @Override
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 1103000)
    public Boolean isDefault() {
        return get("isDefault");
    }

    @Override
    @MemberProperties(verbosityLevel = TRACE_DISPLAY)
    @DisplayProperties(order = 1104000)
    public Integer getOrder() {
        return get("order");
    }

    // 2000000--getParents (Above)
    // 3000000--getChildren
}
