package com.ntankard.Tracking.DataBase.Core.Transfers.CategoryFundTransfer;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Fund.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

@ClassExtensionProperties(includeParent = true)
public class UseCategoryFundTransfer extends CategoryFundTransfer {

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getDescription", "getDestinationValue", "getPeriod", "getFundEvent", "getDestinationCurrency"})
    public UseCategoryFundTransfer(Integer id, String description, Double value, Period period, FundEvent fundEvent, Currency currency) {
        super(id, description, value, period, fundEvent, currency);
    }
}
