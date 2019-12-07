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

import static com.ntankard.ClassExtension.MemberProperties.DEBUG_DISPLAY;
import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class Category extends Pool implements HasDefault, SpecialValues, Ordered {

    public static Integer SAVINGS = 1;
    public static Integer TAXABLE = 2;

    // My values
    private Integer order;
    private Boolean isDefault;
    private Boolean isSavings;
    private Boolean isTaxable;

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getName", "getOrder", "isDefault", "isSavings", "isTaxable"})
    public Category(Integer id, String name, Integer order, Boolean isDefault, Boolean isSavings, Boolean isTaxable) {
        super(id, name);
        this.order = order;
        this.isDefault = isDefault;
        this.isSavings = isSavings;
        this.isTaxable = isTaxable;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 21)
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
    @DisplayProperties(order = 23)
    public List<Integer> getKeys() {
        List<Integer> keys = new ArrayList<>();
        keys.add(TAXABLE);
        keys.add(SAVINGS);
        return keys;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 3)
    public Integer getOrder() {
        return order;
    }

    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 4)
    public Boolean isDefault() {
        return isDefault;
    }

    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 5)
    public Boolean isSavings() {
        return isSavings;
    }

    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 6)
    public Boolean isTaxable() {
        return isTaxable;
    }
}
