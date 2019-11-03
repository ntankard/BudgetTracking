package com.ntankard.Tracking.DataBase.Core.MoneyEvents;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.MoneyCategory.Category;
import com.ntankard.Tracking.DataBase.Core.SupportObjects.Currency;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

@ClassExtensionProperties(includeParent = true)
public class CategoryTransfer extends MoneyEvent<Period, Category, Period, Category> {

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getDescription", "getValue", "getSourceContainer", "getSourceCategory", "getDestinationCategory", "getCurrency"})
    public CategoryTransfer(String id, String description, Double value, Period sourceContainer, Category sourceCategory, Category destinationCategory, Currency currency) {
        super(id, description, value, sourceContainer, sourceCategory, sourceContainer, destinationCategory, currency);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @Override
    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
    @DisplayProperties(order = 5)
    public Period getSourceContainer() {
        return super.getSourceContainer();
    }

    @Override
    @DisplayProperties(order = 6)
    public Category getSourceCategory() {
        return super.getSourceCategory();
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
    @Override
    public void setDestinationCategory(Category destination) {
        super.setDestinationCategory(destination);
    }

    @SetterProperties(sourceMethod = "getData")
    @Override
    public void setSourceCategory(Category source) {
        super.setSourceCategory(source);
    }
}
