package com.ntankard.Tracking.DataBase.Core.MoneyEvents;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Core.SupportObjects.Currency;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;


@ClassExtensionProperties(includeParent = true)
public class PeriodTransfer extends MoneyEvent<Period, Category, Period, Category> {

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getDescription", "getValue", "getSourceContainer", "getSourceCategory", "getDestinationContainer", "getCurrency"})
    public PeriodTransfer(Integer id, String description, Double value, Period sourceContainer, Category sourceCategory, Period destinationContainer, Currency currency) {
        super(id, description, value, sourceContainer, sourceContainer, sourceCategory, destinationContainer, null, currency);
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
