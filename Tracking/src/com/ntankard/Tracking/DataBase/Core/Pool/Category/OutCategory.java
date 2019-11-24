package com.ntankard.Tracking.DataBase.Core.Pool.Category;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.HasDefault;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.SpecialValues;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.DEBUG_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class OutCategory extends Category implements HasDefault, SpecialValues {

    public static Integer TAX = 1;
    public static Integer SAVINGS = 2;

    // My values
    private Boolean isDefault;
    private Boolean isTax;
    private Boolean isSavings;

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getName", "getOrder", "isDefault", "isTax", "isSavings"})
    public OutCategory(Integer id, String name, Integer order, Boolean isDefault, Boolean isTax, Boolean isSavings) {
        super(id, name, order);
        this.isDefault = isDefault;
        this.isTax = isTax;
        this.isSavings = isSavings;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public Boolean isValue(Integer key) {
        if (key.equals(TAX)) {
            return isTax();
        }
        if (key.equals(SAVINGS)) {
            return isSavings();
        }
        throw new IllegalStateException("Unexpected value: " + key);
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 23)
    public List<Integer> getKeys() {
        List<Integer> keys = new ArrayList<>();
        keys.add(TAX);
        keys.add(SAVINGS);
        return keys;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 4)
    public Boolean isDefault() {
        return isDefault;
    }

    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 5)
    public Boolean isTax() {
        return isTax;
    }

    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 6)
    public Boolean isSavings() {
        return isSavings;
    }
}
