package com.ntankard.Tracking.DataBase.Core.Pool;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.HasDefault;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.SpecialValues;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.DEBUG_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class Category extends Pool implements HasDefault, SpecialValues {

    public static Integer SAVINGS = 1;
    public static Integer TAXABLE = 2;

    // My values
    private Boolean isDefault;
    private Boolean isSavings;
    private Boolean isTaxable;

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getName", "getOrder", "isDefault", "isSavings", "isTaxable"})
    public Category(Integer id, String name, Integer order, Boolean isDefault, Boolean isSavings, Boolean isTaxable) {
        super(id, name, order);
        if (isDefault == null) throw new IllegalArgumentException("IsDefault is null");
        if (isSavings == null) throw new IllegalArgumentException("IsSaving is null");
        if (isTaxable == null) throw new IllegalArgumentException("IsTaxable is null");
        this.isDefault = isDefault;
        this.isSavings = isSavings;
        this.isTaxable = isTaxable;
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
    @DisplayProperties(order = 1111000)
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

    // 1110000------getOrder
    // 1111000--------getKeys (Above)
    // 2000000--getParents (Above)
    // 3000000--getChildren
}
