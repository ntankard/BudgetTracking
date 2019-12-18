package com.ntankard.Tracking.DataBase.Core.Transfers.CategoryFundTransfer;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

@ClassExtensionProperties(includeParent = true)
public class UseCategoryFundTransfer extends CategoryFundTransfer {

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getDescription", "getDestinationValue", "getPeriod", "getDestination", "getDestinationCurrency"})
    public UseCategoryFundTransfer(Integer id, String description, Double value, Period period, FundEvent fundEvent, Currency currency) {
        super(id, description, value, period, fundEvent, currency);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @DisplayProperties(order = 9)
    public Currency getDestinationCurrency() {
        return super.getDestinationCurrency();
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @Override
    @SetterProperties(localSourceMethod = "sourceOptions")
    public void setDestination(FundEvent destination) {
        if (destination == null) throw new IllegalArgumentException("Fund is null");
        super.setDestination(destination);
        super.setSource(destination.getCategory());
    }

    @SetterProperties(localSourceMethod = "sourceOptions")
    public void setDestinationCurrency(Currency currency) {
        if (currency == null) throw new IllegalArgumentException("Currency was null");
        this.currency.notifyChildUnLink(this);
        this.currency = currency;
        this.currency.notifyChildLink(this);
    }
}
