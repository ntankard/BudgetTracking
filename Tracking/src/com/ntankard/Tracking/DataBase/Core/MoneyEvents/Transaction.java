package com.ntankard.Tracking.DataBase.Core.MoneyEvents;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Statement;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Category;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class Transaction extends MoneyEvent<Statement, DataObject, Period, Category> {

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getDescription", "getValue", "getSourceContainer", "getDestinationCategory"})
    public Transaction(String id, String description, Double value, Statement sourceContainer, Category destinationCategory) {
        super(id, description, value, sourceContainer, null, sourceContainer.getPeriod(), destinationCategory, sourceContainer.getBank().getCurrency());
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @Override
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 5)
    public Statement getSourceContainer() {
        return super.getSourceContainer();
    }

    @Override
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 7)
    public Period getDestinationContainer() {
        return super.getDestinationContainer();
    }

    @Override
    @DisplayProperties(order = 8)
    public Category getDestinationCategory() {
        return super.getDestinationCategory();
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @SetterProperties(sourceMethod = "getData")
    public void setDestinationCategory(Category category) {
        super.setDestinationCategory(category);
    }
}
