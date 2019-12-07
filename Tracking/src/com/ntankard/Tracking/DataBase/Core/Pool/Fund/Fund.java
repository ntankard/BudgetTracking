package com.ntankard.Tracking.DataBase.Core.Pool.Fund;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.Ordered;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Core.Pool.Fund.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.*;

@ClassExtensionProperties(includeParent = true)
public class Fund extends Pool implements Ordered {

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
        if (category == null) throw new IllegalArgumentException("Category is null");
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
        toReturn.add(getCategory());
        return toReturn;
    }

    /**
     * {@inheritDoc
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T extends DataObject> List<T> sourceOptions(Class<T> type, String fieldName) {
        if (fieldName.equals("DefaultFundEvent")) {
            return (List<T>) getChildren(FundEvent.class);
        }
        return super.sourceOptions(type, fieldName);
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

    @Override
    @MemberProperties(verbosityLevel = TRACE_DISPLAY)
    @DisplayProperties(order = 5)
    public Integer getOrder() {
        return getCategory().getOrder();
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @SetterProperties(localSourceMethod = "sourceOptions", displaySet = false)
    public void setDefaultFundEvent(FundEvent defaultFundEvent) {
        if (defaultFundEvent == null) throw new IllegalArgumentException("Fund is null");
        if (!defaultFundEvent.getFund().equals(this)) {
            throw new IllegalArgumentException("Cant set a default fun event that dose not reference this fund");
        }
        this.defaultFundEvent = defaultFundEvent;
    }
}
