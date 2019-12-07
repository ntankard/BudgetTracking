package com.ntankard.Tracking.DataBase.Core.Transfers.CategoryFundTransfer;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Fund.FundEvent.FundEvent;
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
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @Override
    @DisplayProperties(order = 5, dataType = CURRENCY)
    public Double getSourceValue() {
        return -getDestination().getCharge(getPeriod());
    }

    @Override
    @DisplayProperties(order = 8, dataType = CURRENCY)
    public Double getDestinationValue() {
        return getDestination().getCharge(getPeriod());
    }
}
