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
        this.name = name;
        this.order = order;
    }

    @Override
    public String toString() {
        return getName();
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @DisplayProperties(order = 2)
    public String getName() {
        return name;
    }

    @Override
    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
    @DisplayProperties(order = 3)
    public Integer getOrder() {
        return order;
    }
}
