package com.ntankard.Tracking.DataBase.Core.MoneyEvents;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.DynamicGUI.Components.Object.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Category;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Currency;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.NonPeriodFund;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.NonPeriodFundEvent;

@ClassExtensionProperties(includeParent = true)
public class NonPeriodFundChargeTransfer extends MoneyEvent<Period, Category, NonPeriodFund, NonPeriodFundEvent> {

    // My values
    private String id;

    /**
     * Constructor
     */
    public NonPeriodFundChargeTransfer(String id, Period source, Category sourceCategory, NonPeriodFund destination, NonPeriodFundEvent destinationCategory, Currency currency, String description, Double value) {
        super(description, value, source, sourceCategory, destination, destinationCategory, currency);
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

    @DisplayProperties(order = 4)
    @Override
    public NonPeriodFund getDestinationContainer() {
        return super.getDestinationContainer();
    }

    @DisplayProperties(order = 3)
    @Override
    public Category getSourceCategory() {
        return super.getSourceCategory();
    }

    @DisplayProperties(order = 5)
    @Override
    public NonPeriodFundEvent getDestinationCategory() {
        return super.getDestinationCategory();
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @SetterProperties(sourceMethod = "getPeriods")
    @Override
    public void setSourceContainer(Period source) {
        super.setSourceContainer(source);
    }

    @SetterProperties(sourceMethod = "getNonPeriodFunds")
    @Override
    public void setDestinationContainer(NonPeriodFund destination) {
        super.setDestinationContainer(destination);
    }

    @SetterProperties(sourceMethod = "getCategories")
    @Override
    public void setSourceCategory(Category sourceCategory) {
        super.setSourceCategory(sourceCategory);
    }

    @SetterProperties(sourceMethod = "getNonPeriodFundEvents")
    @Override
    public void setDestinationCategory(NonPeriodFundEvent destination) {
        super.setDestinationCategory(destination);
    }
}
