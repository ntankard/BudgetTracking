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
        this.order = order;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @DisplayProperties(order = 23)
    @MemberProperties(verbosityLevel = TRACE_DISPLAY)
    public Integer getOrder() {
        return order;
    }
}
