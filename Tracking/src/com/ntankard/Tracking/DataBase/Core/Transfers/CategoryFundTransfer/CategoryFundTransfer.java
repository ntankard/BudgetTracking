package com.ntankard.Tracking.DataBase.Core.Transfers.CategoryFundTransfer;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Core.Pool.Fund.Fund;
import com.ntankard.Tracking.DataBase.Core.Pool.Fund.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Core.Transfers.Transfer;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.DEBUG_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public abstract class CategoryFundTransfer extends Transfer<Category, Fund> {

    // My parents
    private FundEvent fundEvent;

    /**
     * Constructor
     */
    @ParameterMap(shouldSave = false)
    public CategoryFundTransfer(Integer id, String description, Double value, Period period, FundEvent fundEvent, Currency currency) {
        super(id, description, value, period, fundEvent.getFund().getCategory(), fundEvent.getFund(), currency);
        if (period == null) throw new IllegalArgumentException("Period is null");
        if (currency == null) throw new IllegalArgumentException("Currency is null");
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
        toReturn.add(getFundEvent());
        return toReturn;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T extends DataObject> List<T> sourceOptions(Class<T> type, String fieldName) {
        if (fieldName.equals("FundEvent")) {
            return (List<T>) getDestination().getChildren(FundEvent.class);
        }
        return super.sourceOptions(type, fieldName);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @Override
    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 4)
    public Category getSource() {
        return super.getSource();
    }

    @Override
    @DisplayProperties(order = 7)
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

    @Override
    @SetterProperties(localSourceMethod = "sourceOptions")
    public void setDestination(Fund destination) {
        if (destination == null) throw new IllegalArgumentException("Fund is null");
        super.setDestination(destination);
        super.setSource(destination.getCategory());
        setFundEvent(destination.getDefaultFundEvent());
    }

    @SetterProperties(localSourceMethod = "sourceOptions")
    public void setFundEvent(FundEvent fundEvent) {
        if (fundEvent == null) throw new IllegalArgumentException("FundEvent is null");
        if (!fundEvent.getFund().equals(getDestination()))
            throw new IllegalArgumentException("Cant set a fund event that dose not match the fund");
        this.fundEvent.notifyChildUnLink(this);
        this.fundEvent = fundEvent;
        this.fundEvent.notifyChildLink(this);
    }
}
