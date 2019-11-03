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
public class PeriodTransfer extends MoneyEvent<Period, Category, Period, Category> {

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getDescription", "getValue", "getSourceContainer", "getSourceCategory", "getDestinationContainer", "getCurrency"})
    public PeriodTransfer(String id, String description, Double value, Period sourceContainer, Category sourceCategory, Period destinationContainer, Currency currency) {
        super(id, description, value, sourceContainer, sourceCategory, destinationContainer, sourceCategory, currency);
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
    @DisplayProperties(order = 7)
    public Period getDestinationContainer() {
        return super.getDestinationContainer();
    }

    @Override
    @MemberProperties(verbosityLevel = MemberProperties.DEBUG_DISPLAY)
    @DisplayProperties(order = 8)
    public Category getDestinationCategory() {
        return super.getDestinationCategory();
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @SetterProperties(sourceMethod = "getData")
    @Override
    public void setSourceContainer(Period source) {
        super.setSourceContainer(source);
    }

    @SetterProperties(sourceMethod = "getData")
    @Override
    public void setSourceCategory(Category category) {
        super.setSourceCategory(category);
        super.setDestinationCategory(category);
    }

    @SetterProperties(sourceMethod = "getData")
    @Override
    public void setDestinationContainer(Period destination) {
        super.setDestinationContainer(destination);
    }
}
