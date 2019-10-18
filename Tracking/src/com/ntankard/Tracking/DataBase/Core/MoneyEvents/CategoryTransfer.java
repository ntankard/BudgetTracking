package com.ntankard.Tracking.DataBase.Core.MoneyEvents;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.DynamicGUI.Components.Object.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Category;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Currency;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;

@ClassExtensionProperties(includeParent = true)
public class CategoryTransfer extends MoneyEvent<Period, Category, Period, Category> {

    // My values
    private String idCode;

    /**
     * Constructor
     */
    public CategoryTransfer(Period idPeriod, String idCode, Category source, Category destination, Currency currency, String description, Double value) {
        super(description, value, idPeriod, source, idPeriod, destination, currency);
        this.idCode = idCode;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
    public String getId() {
        return getSourceContainer().getId() + " " + getIdCode();
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
    public String getIdCode() {
        return idCode;
    }

    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
    @DisplayProperties(order = 2)
    @Override
    public Period getSourceContainer() {
        return super.getSourceContainer();
    }

    @Override
    public Category getSourceCategory() {
        return super.getSourceCategory();
    }

    @DisplayProperties(order = 3)
    @Override
    public Category getDestinationCategory() {
        return super.getDestinationCategory();
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @SetterProperties(sourceMethod = "getCategories")
    @Override
    public void setDestinationCategory(Category destination) {
        super.setDestinationCategory(destination);
    }

    @SetterProperties(sourceMethod = "getCategories")
    @Override
    public void setSourceCategory(Category source) {
        super.setSourceCategory(source);
    }
}
