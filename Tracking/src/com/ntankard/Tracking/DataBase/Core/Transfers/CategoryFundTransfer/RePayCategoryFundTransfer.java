package com.ntankard.Tracking.DataBase.Core.Transfers.CategoryFundTransfer;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Fund.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

@ClassExtensionProperties(includeParent = true)
public class RePayCategoryFundTransfer extends CategoryFundTransfer {

    /**
     * Constructor
     */
    @ParameterMap(shouldSave = false)
    public RePayCategoryFundTransfer(Integer id, Period period, FundEvent fundEvent, Currency currency) {
        super(id, fundEvent.getName(), -1.0, period, fundEvent.getFund().getCategory(), fundEvent, currency);
    }

    @Override
    public Double getValue() {
        return getFundEvent().getCharge(getPeriod());
    }
}
