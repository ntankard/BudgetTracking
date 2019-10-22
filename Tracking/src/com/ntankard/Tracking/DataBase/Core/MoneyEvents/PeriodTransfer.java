package com.ntankard.Tracking.DataBase.Core.MoneyEvents;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.DynamicGUI.Components.Object.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Category;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Currency;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;


@ClassExtensionProperties(includeParent = true)
public class PeriodTransfer extends MoneyEvent<Period, Category, Period, Category> {

    /**
     * Constructor
     */
    public PeriodTransfer(String id, Period source, Period destination, Currency currency, Category category, String description, Double value) {
        super(id, description, value, source, category, destination, category, currency);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @DisplayProperties(order = 2)
    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
    @Override
    public Period getSourceContainer() {
        return super.getSourceContainer();
    }

    @DisplayProperties(order = 3)
    @Override
    public Period getDestinationContainer() {
        return super.getDestinationContainer();
    }

    @Override
    public Category getSourceCategory() {
        return super.getSourceCategory();
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
    public void setDestinationContainer(Period destination) {
        super.setDestinationContainer(destination);
    }

    @SetterProperties(sourceMethod = "getData")
    @Override
    public void setSourceCategory(Category category) {
        super.setSourceCategory(category);
        super.setDestinationCategory(category);
    }
}
