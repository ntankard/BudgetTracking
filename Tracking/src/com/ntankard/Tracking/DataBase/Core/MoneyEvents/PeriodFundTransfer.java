package com.ntankard.Tracking.DataBase.Core.MoneyEvents;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Fund;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Category;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Currency;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.FundEvent;

@ClassExtensionProperties(includeParent = true)
public class PeriodFundTransfer extends MoneyEvent<Period, Category, Fund, FundEvent> {

    /**
     * Constructor
     */
    public PeriodFundTransfer(String id, Period source, Fund destination, Category sourceCategory, FundEvent destinationCategory, Currency currency, String description, Double value) {
        super(id, description, value, source, sourceCategory, destination, destinationCategory, currency);
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

    @DisplayProperties(order = 4)
    @Override
    public Fund getDestinationContainer() {
        return super.getDestinationContainer();
    }

    @DisplayProperties(order = 3)
    @Override
    public Category getSourceCategory() {
        return super.getSourceCategory();
    }

    @Override
    public FundEvent getDestinationCategory() {
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
    public void setDestinationContainer(Fund destination) {
        super.setDestinationContainer(destination);
    }

    @SetterProperties(sourceMethod = "getData")
    @Override
    public void setSourceCategory(Category sourceCategory) {
        super.setSourceCategory(sourceCategory);
    }

    @SetterProperties(sourceMethod = "getData")
    @Override
    public void setDestinationCategory(FundEvent destination) {
        super.setDestinationCategory(destination);
    }
}
