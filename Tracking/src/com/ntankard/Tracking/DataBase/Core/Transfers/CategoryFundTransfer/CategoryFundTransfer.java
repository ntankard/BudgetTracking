package com.ntankard.Tracking.DataBase.Core.Transfers.CategoryFundTransfer;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.Category;
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
    public CategoryFundTransfer(Integer id, String description, Double value, Period period, Category source, FundEvent fundEvent, Currency currency) {
        super(id, description, value, period, source, source.getChildren(Fund.class).get(0), currency);
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
        toReturn.add(fundEvent);
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
    @DisplayProperties(order = 6)
    public Category getSource() {
        return super.getSource();
    }

    @Override
    @DisplayProperties(order = 9)
    public Fund getDestination() {
        return super.getDestination();
    }

    @DisplayProperties(order = 12)
    public FundEvent getFundEvent() {
        return fundEvent;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @SetterProperties(localSourceMethod = "sourceOptions")
    public void setFundEvent(FundEvent fundEvent) {
        this.fundEvent.notifyChildUnLink(this);
        this.fundEvent = fundEvent;
        this.fundEvent.notifyChildLink(this);
    }

    @Override
    @SetterProperties(localSourceMethod = "sourceOptions")
    public void setSource(Category source) {
        super.setSource(source);
        super.setDestination(source.getChildren(Fund.class).get(0));
        setFundEvent(getDestination().getDefaultFundEvent());
    }
}
