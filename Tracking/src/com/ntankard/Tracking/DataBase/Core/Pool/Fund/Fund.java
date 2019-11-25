package com.ntankard.Tracking.DataBase.Core.Pool.Fund;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.Category;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.DEBUG_DISPLAY;
import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class Fund extends Pool {

    // My Parents
    private Category category;

    // My values
    private FundEvent defaultFundEvent; // Not a parent to prevent a circular dependency

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getCategory"})
    public Fund(Integer id, Category category) {
        super(id, "ERROR, THIS SHOULD NOT BE USED");
        this.category = category;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 21)
    public List<DataObject> getParents() {
        List<DataObject> toReturn = new ArrayList<>();
        toReturn.add(category);
        return toReturn;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @Override
    @DisplayProperties(order = 2)
    public String getName() {
        return getCategory().getName();
    }

    @DisplayProperties(order = 3)
    public Category getCategory() {
        return category;
    }

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 4)
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
