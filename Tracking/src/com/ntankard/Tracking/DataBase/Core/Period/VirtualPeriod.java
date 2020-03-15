package com.ntankard.Tracking.DataBase.Core.Period;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Field;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import java.util.List;

@ClassExtensionProperties(includeParent = true)
public class VirtualPeriod extends Period {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static List<Field<?>> getFields(Integer id, String name, Integer order, DataObject container) {
        List<Field<?>> toReturn = Period.getFields(id, container);
        toReturn.add(new Field<>("name", String.class, name, container));
        toReturn.add(new Field<>("order", Integer.class, order, container));
        return toReturn;
    }

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "toString", "getOrder"})
    public VirtualPeriod(Integer id, String name, Integer order) {
        super();
        setFields(getFields(id, name, order, this));
    }

    /**
     * {@inheritDoc
     */
    @Override
    public String toString() {
        return getName();
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    // 1000000--getID

    @DisplayProperties(order = 1010000)
    public String getName() {
        return get("name");
    }

    @Override
    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
    @DisplayProperties(order = 1020000)
    public Integer getOrder() {
        return get("order");
    }

    // 2000000--getParents (Above)
    // 3000000--getChildren
}
