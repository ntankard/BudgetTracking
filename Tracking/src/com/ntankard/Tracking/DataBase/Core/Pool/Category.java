package com.ntankard.Tracking.DataBase.Core.Pool;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.HasDefault;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.Ordered;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.SpecialValues;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.*;
import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class Category extends Pool implements HasDefault, SpecialValues, Ordered {

    public static Integer SAVINGS = 1;
    public static Integer TAXABLE = 2;

    // My values
    private Boolean isDefault;
    private Boolean isSavings;
    private Boolean isTaxable;
    private Integer set;
    private String setName;
    private Integer order;

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getName", "getOrder", "isDefault", "isSavings", "isTaxable", "getSet", "getSetName"})
    public Category(Integer id, String name, Integer order, Boolean isDefault, Boolean isSavings, Boolean isTaxable, Integer set, String setName) {
        super(id, name);
        if (isDefault == null) throw new IllegalArgumentException("IsDefault is null");
        if (isSavings == null) throw new IllegalArgumentException("IsSaving is null");
        if (isTaxable == null) throw new IllegalArgumentException("IsTaxable is null");
        if (order == null) throw new IllegalArgumentException("Order is null");
        if (set == null) throw new IllegalArgumentException("Set is null");
        if (setName == null) throw new IllegalArgumentException("SetLeader is null");
        this.isDefault = isDefault;
        this.isSavings = isSavings;
        this.isTaxable = isTaxable;
        this.set = set;
        this.setName = setName;
        this.order = order;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 2000000)
    public List<DataObject> getParents() {
        return new ArrayList<>();
    }

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
    @DisplayProperties(order = 1105000)
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
        return isDefault;
    }

    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 1102000)
    public Boolean isSavings() {
        return isSavings;
    }

    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 1103000)
    public Boolean isTaxable() {
        return isTaxable;
    }

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 1104000)
    public Integer getSet() {
        return set;
    }

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 1105000)
    public String getSetName() {
        return setName;
    }

    @Override
    @MemberProperties(verbosityLevel = TRACE_DISPLAY)
    @DisplayProperties(order = 1106000)
    public Integer getOrder() {
        return order;
    }

    // 1111000--------getKeys (Above)
    // 2000000--getParents (Above)
    // 3000000--getChildren
}
