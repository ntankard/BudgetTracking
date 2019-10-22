package com.ntankard.Tracking.DataBase.Core.MoneyEvents;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Category;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Currency;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;

@ClassExtensionProperties(includeParent = true)
public class CategoryTransfer extends MoneyEvent<Period, Category, Period, Category> {

    /**
     * Constructor
     */
    public CategoryTransfer(Period idPeriod, String id, Category source, Category destination, Currency currency, String description, Double value) {
        super(id, description, value, idPeriod, source, idPeriod, destination, currency);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

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
