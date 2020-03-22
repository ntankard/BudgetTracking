package com.ntankard.Tracking.DataBase.Core.Period;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Field;

import java.util.List;

@ClassExtensionProperties(includeParent = true)
public class VirtualPeriod extends Period {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static List<Field<?>> getFields() {
        List<Field<?>> toReturn = Period.getFields();
        toReturn.add(new Field<>("getName", String.class));
        toReturn.add(new Field<>("getOrder", Integer.class));
        return toReturn;
    }

    /**
     * Create a new VirtualPeriod object
     */
    public static VirtualPeriod make(Integer id, String name, Integer order) {
        return assembleDataObject(VirtualPeriod.getFields(), new VirtualPeriod()
                , "getId", id
                , "getName", name
                , "getOrder", order
        );
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
        return get("getName");
    }

    @Override
    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
    @DisplayProperties(order = 1020000)
    public Integer getOrder() {
        return get("getOrder");
    }

    // 2000000--getParents (Above)
    // 3000000--getChildren
}
