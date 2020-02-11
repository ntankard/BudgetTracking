package com.ntankard.Tracking.DataBase.Core.Pool;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.Ordered;
import com.ntankard.Tracking.DataBase.Core.BaseObject.NamedDataObject;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import static com.ntankard.ClassExtension.MemberProperties.TRACE_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public abstract class Pool extends NamedDataObject implements Ordered {

    // My values
    private Integer order;

    /**
     * Constructor
     */
    @ParameterMap(shouldSave = false)
    public Pool(Integer id, String name, Integer order) {
        super(id, name);
        if (order == null) throw new IllegalArgumentException("Order is null");
        this.order = order;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    // 1000000--getID
    // 1100000----getName

    @Override
    @MemberProperties(verbosityLevel = TRACE_DISPLAY)
    @DisplayProperties(order = 1110000)
    public Integer getOrder() {
        return order;
    }

    // 2000000--getParents
    // 3000000--getChildren
}
