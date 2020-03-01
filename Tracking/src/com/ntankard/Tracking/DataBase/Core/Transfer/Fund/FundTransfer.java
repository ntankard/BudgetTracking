package com.ntankard.Tracking.DataBase.Core.Transfer.Fund;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Core.Transfer.Transfer;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.DEBUG_DISPLAY;
import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public abstract class FundTransfer extends Transfer<FundEvent> {

    // My parents
    private Pool destination;
    private Currency currency;

    /**
     * Constructor
     */
    @ParameterMap(shouldSave = false)
    public FundTransfer(Integer id, String description,
                        Period period, FundEvent source, Currency currency) {
        super(id, description, period, source);
        if (currency == null) throw new IllegalArgumentException("Currency is null");
        this.destination = source.getCategory();
        this.currency = currency;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 2000000)
    public List<DataObject> getParents() {
        List<DataObject> toReturn = super.getParents();
        toReturn.add(getCurrency());
        toReturn.add(getDestination());
        return toReturn;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    // 1000000--getID
    // 1100000----getDescription
    // 1200000----getPeriod

    @Override
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 1300000)
    public FundEvent getSource() {
        return super.getSource();
    }

    // 1400000----getValue

    @Override
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 1500000)
    public Currency getCurrency() {
        return currency;
    }

    @Override
    @DisplayProperties(order = 1600000)
    public Pool getDestination() {
        return destination;
    }

    // 1700000----getSourceTransfer
    // 1800000----getDestinationTransfer
    // 2000000--getParents (Above)
    // 3000000--getChildren

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public void setDestination() {
        Pool destination = getSource().getCategory();
        if (destination == null) throw new IllegalArgumentException("Destination is null");
        this.destination.notifyChildUnLink(this);
        this.destination = destination;
        this.destination.notifyChildLink(this);
        getDestinationTransfer().setPool(getPool(false));
        validateParents();
    }

    @SetterProperties(localSourceMethod = "sourceOptions")
    public void setCurrency(Currency currency) {
        if (currency == null) throw new IllegalArgumentException("Currency is null");
        this.currency.notifyChildUnLink(this);
        this.currency = currency;
        this.currency.notifyChildLink(this);
        getSourceTransfer().setCurrency(getCurrency(true));
        getDestinationTransfer().setCurrency(getCurrency(false));
        validateParents();
    }
}
