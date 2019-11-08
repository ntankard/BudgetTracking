package com.ntankard.Tracking.DataBase.Core.Pool;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.HasDefault;
import com.ntankard.Tracking.DataBase.Core.BaseObject.NamedDataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.SpecialValues;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.DEBUG_DISPLAY;
import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class Category extends NamedDataObject implements HasDefault, SpecialValues {

    public static Integer INCOME = 1;

    // My values
    private int order;
    private boolean isDefault;
    private boolean isIncome;

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getName", "getOrder", "isDefault", "isIncome"})
    public Category(String id, String name, int order, boolean isDefault, boolean isIncome) {
        super(id, name);
        this.order = order;
        this.isDefault = isDefault;
        this.isIncome = isIncome;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public boolean isValue(Integer key) {
        if (key.equals(INCOME)) {
            return isIncome();
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
        keys.add(INCOME);
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

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 3)
    public int getOrder() {
        return order;
    }

    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 4)
    public boolean isDefault() {
        return isDefault;
    }

    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 5)
    public boolean isIncome() {
        return isIncome;
    }
}
