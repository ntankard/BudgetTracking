package com.ntankard.Tracking.DataBase.Core.MoneyEvents;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.MoneyCategory.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Core.Pool.Fund;
import com.ntankard.Tracking.DataBase.Core.SupportObjects.Currency;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.DEBUG_DISPLAY;
import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class CategoryFundTransfer extends MoneyEvent<Category, DataObject, Fund, DataObject> {

    // My parents
    private FundEvent fundEvent;

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getDescription", "getValue", "getPeriod", "getSource", "getDestination", "getFundEvent", "getCurrency"})
    public CategoryFundTransfer(Integer id, String description, Double value, Period period, Category source, Fund destination, FundEvent fundEvent, Currency currency) {
        super(id, description, value, period, source, null, destination, null, currency);
        this.fundEvent = fundEvent;
    }

    @Override
    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 21)
    public List<DataObject> getParents() {
        List<DataObject> toReturn = super.getParents();
        if (fundEvent != null) {
            toReturn.add(fundEvent);
        }
        return toReturn;
    }


    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @Override
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 3)
    public String getDescription() {
        return super.getDescription();
    }

    @DisplayProperties(order = 5)
    public Category getSource() {
        return super.getSourceContainer();
    }

    @DisplayProperties(order = 7)
    public Fund getDestination() {
        return super.getDestinationContainer();
    }

    @DisplayProperties(order = 10)
    public FundEvent getFundEvent() {
        return fundEvent;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @SetterProperties(sourceMethod = "getData")
    public void setSource(Category sourceContainer) {
        super.setSourceContainer(sourceContainer);
    }

    @SetterProperties(sourceMethod = "getData")
    public void setDestination(Fund destinationContainer) {
        super.setDestinationContainer(destinationContainer);
    }

    @SetterProperties(sourceMethod = "getData")
    public void setFundEvent(FundEvent fundEvent) {
        this.fundEvent = fundEvent;
    }
}
