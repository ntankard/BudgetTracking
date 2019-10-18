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

    // My values
    private String id;

    /**
     * Constructor
     */
    public PeriodTransfer(String id, Period source, Period destination, Currency currency, Category category, String description, Double value) {
        super(description, value, source, category, destination, category, currency);
        this.id = id;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
    public String getId() {
        return id;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @DisplayProperties(order = 2)
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

    @SetterProperties(sourceMethod = "getPeriods")
    @Override
    public void setSourceContainer(Period source) {
        super.setSourceContainer(source);
    }

    @SetterProperties(sourceMethod = "getPeriods")
    @Override
    public void setDestinationContainer(Period destination) {
        super.setDestinationContainer(destination);
    }

    @SetterProperties(sourceMethod = "getCategories")
    @Override
    public void setSourceCategory(Category category) {
        super.setSourceCategory(category);
        super.setDestinationCategory(category);
    }
}
