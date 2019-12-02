package com.ntankard.Tracking.DataBase.Core.Pool.Fund.FundEvent;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Fund.Fund;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

@ClassExtensionProperties(includeParent = true)
public class NoneFundEvent extends FundEvent {

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getName", "getFund"})
    public NoneFundEvent(Integer id, String name, Fund fund) {
        super(id, name, fund);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public Boolean isActiveThisPeriod(Period period) {
        return true;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public Boolean isChargeThisPeriod(Period period) {
        return false;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public Double getCharge(Period period) {
        throw new UnsupportedOperationException("Not relevant for this type");
    }
}
