package com.ntankard.Tracking.DataBase.Core.Transfers.CategoryFundTransfer;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY;

@ClassExtensionProperties(includeParent = true)
public class RePayCategoryFundTransfer extends CategoryFundTransfer {

    /**
     * Constructor
     */
    @ParameterMap(shouldSave = false)
    public RePayCategoryFundTransfer(Integer id, Period period, FundEvent fundEvent, Currency currency) {
        super(id, fundEvent.getName(), -1.0, period, fundEvent, currency);
        if (currency == null) throw new IllegalArgumentException("Currency is null");
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void remove() {
        super.remove_impl();
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    // 1000000--getID
    // 1100000----getPeriod

    @Override
    @DisplayProperties(order = 1200000)
    public String getDescription() {
        return "RP " + super.getDescription();
    }

    // 1300000----getSource

    @Override
    @DisplayProperties(order = 1400000, dataType = CURRENCY)
    public Double getSourceValue() {
        return -getDestination().getCharge(getPeriod());
    }

    // 1500000----getSourceCurrency
    // 1600000----getDestination

    @Override
    @DisplayProperties(order = 1700000, dataType = CURRENCY)
    public Double getDestinationValue() {
        return getDestination().getCharge(getPeriod());
    }

    // 1800000----getDestinationCurrency
    // 2000000--getParents
    // 3000000--getChildren
}
