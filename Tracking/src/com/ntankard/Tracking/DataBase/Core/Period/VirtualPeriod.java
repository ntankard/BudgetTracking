package com.ntankard.Tracking.DataBase.Core.Period;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

@ClassExtensionProperties(includeParent = true)
public class VirtualPeriod extends Period {

    // My values
    private String name;
    private Integer order;

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "toString", "getOrder"})
    public VirtualPeriod(Integer id, String name, Integer order) {
        super(id);
        if (name == null) throw new IllegalArgumentException("Name is null");
        if (order == null) throw new IllegalArgumentException("Order is null");
        this.name = name;
        this.order = order;
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
        return name;
    }

    @Override
    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
    @DisplayProperties(order = 1020000)
    public Integer getOrder() {
        return order;
    }

    // 2000000--getParents (Above)
    // 3000000--getChildren
}
