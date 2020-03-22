package com.ntankard.Tracking.DataBase.Core.Pool;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Field;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.HasDefault;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.Ordered;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.SpecialValues;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.*;

@ClassExtensionProperties(includeParent = true)
public class Category extends Pool implements HasDefault, SpecialValues, Ordered {

    public static Integer SAVINGS = 1;
    public static Integer TAXABLE = 2;

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static List<Field<?>> getFields() {
        List<Field<?>> toReturn = Pool.getFields();
        toReturn.add(new Field<>("getOrder", Integer.class));
        toReturn.add(new Field<>("isDefault", Boolean.class));
        toReturn.add(new Field<>("isSavings", Boolean.class));
        toReturn.add(new Field<>("isTaxable", Boolean.class));
        toReturn.add(new Field<>("getSet", Integer.class));
        toReturn.add(new Field<>("getSetName", String.class));
        return toReturn;
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################# Implementations ################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc
     */
    @Override
    public Boolean isValue(Integer key) {
        if (key.equals(SAVINGS)) {
            return isSavings();
        }
        if (key.equals(TAXABLE)) {
            return isTaxable();
        }
        throw new IllegalArgumentException("Unexpected key: " + key);
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 1107000)
    public List<Integer> getKeys() {
        List<Integer> keys = new ArrayList<>();
        keys.add(TAXABLE);
        keys.add(SAVINGS);
        return keys;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    // 1000000--getID
    // 1100000----getName

    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 1101000)
    public Boolean isDefault() {
        return get("isDefault");
    }

    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 1102000)
    public Boolean isSavings() {
        return get("isSavings");
    }

    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 1103000)
    public Boolean isTaxable() {
        return get("isTaxable");
    }

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 1104000)
    public Integer getSet() {
        return get("getSet");
    }

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 1105000)
    public String getSetName() {
        return get("getSetName");
    }

    @Override
    @MemberProperties(verbosityLevel = TRACE_DISPLAY)
    @DisplayProperties(order = 1106000)
    public Integer getOrder() {
        return get("getOrder");
    }

    // 1107000--------getKeys (Above)
    // 2000000--getParents (Above)
    // 3000000--getChildren
}
