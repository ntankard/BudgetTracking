package com.ntankard.Tracking.DataBase.Core.MoneyEvents.PeriodFundTransfer;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.Pool.Fund;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.MoneyEvent;
import com.ntankard.Tracking.DataBase.Core.SupportObjects.Currency;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyCategory.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

@ClassExtensionProperties(includeParent = true)
public class PeriodFundTransfer extends MoneyEvent<Period, Category, Fund, FundEvent> {

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getDescription", "getValue", "getSourceContainer", "getSourceCategory", "getDestinationContainer", "getDestinationCategory", "getCurrency"})
    public PeriodFundTransfer(Integer id, String description, Double value, Period sourceContainer, Category sourceCategory, Fund destinationContainer, FundEvent destinationCategory, Currency currency) {
        super(id, description, value, sourceContainer, sourceContainer, sourceCategory, destinationContainer, destinationCategory, currency);
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
    public Fund getDestinationContainer() {
        return super.getDestinationContainer();
    }

    @Override
    @DisplayProperties(order = 8)
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
    public void setSourceCategory(Category sourceCategory) {
        super.setSourceCategory(sourceCategory);
    }

    @SetterProperties(sourceMethod = "getData")
    @Override
    public void setDestinationContainer(Fund destination) {
        super.setDestinationContainer(destination);
    }

    @SetterProperties(sourceMethod = "getData")
    @Override
    public void setDestinationCategory(FundEvent destination) {
        super.setDestinationCategory(destination);
    }
}
