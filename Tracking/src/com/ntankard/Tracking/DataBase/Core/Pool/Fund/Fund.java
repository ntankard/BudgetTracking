package com.ntankard.Tracking.DataBase.Core.Pool.Fund;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.SpecialValues;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.DEBUG_DISPLAY;
import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class Fund extends Pool implements SpecialValues {

    public static Integer TAX = 1;
    public static Integer SAVINGS = 2;

    // My values
    private Boolean isSavings;
    private Boolean isTax;
    private FundEvent defaultFundEvent; // Not a parent to prevent a circular dependency

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getName", "isSavings", "isTax"})
    public Fund(Integer id, String name, Boolean isSavings, Boolean isTax) {
        super(id, name);
        this.isSavings = isSavings;
        this.isTax = isTax;
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

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 21)
    public List<DataObject> getParents() {
        return new ArrayList<>();
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 3)
    public Boolean isSavings() {
        return isSavings;
    }

    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 4)
    public Boolean isTax() {
        return isTax;
    }

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 5)
    public FundEvent getDefaultFundEvent() {
        return defaultFundEvent;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public void setDefaultFundEvent(FundEvent defaultFundEvent) {
        this.defaultFundEvent = defaultFundEvent;
    }
}
