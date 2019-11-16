package com.ntankard.Tracking.DataBase.Core.Transfers;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.Pool.Fund.FundEvent;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Core.Pool.Fund.Fund;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.DEBUG_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class CategoryFundTransfer extends Transfer<Category, Fund> {

    // My parents
    private FundEvent fundEvent;

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getDescription", "getValue", "getPeriod", "getSource", "getDestination", "getFundEvent", "getCurrency"})
    public CategoryFundTransfer(Integer id, String description, Double value, Period period, Category source, Fund destination, FundEvent fundEvent, Currency currency) {
        super(id, description, value, period, source, destination, currency);
        this.fundEvent = fundEvent;
    }

    /**
     * {@inheritDoc
     */
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
    @DisplayProperties(order = 7)
    public Category getSource() {
        return super.getSource();
    }

    @Override
    @DisplayProperties(order = 8)
    public Fund getDestination() {
        return super.getDestination();
    }

    @DisplayProperties(order = 10)
    public FundEvent getFundEvent() {
        return fundEvent;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @SetterProperties(sourceMethod = "getData")
    public void setFundEvent(FundEvent fundEvent) {
        this.fundEvent = fundEvent;
    }

    @Override
    @SetterProperties(sourceMethod = "getData")
    public void setSource(Category source) {
        super.setSource(source);
    }

    @Override
    @SetterProperties(sourceMethod = "getData")
    public void setDestination(Fund destination) {
        super.setDestination(destination);
    }
}
