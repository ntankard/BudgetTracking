package com.ntankard.Tracking.DataBase.Core.Pool.FundEvent;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

@ClassExtensionProperties(includeParent = true)
public class NoneFundEvent extends FundEvent {

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getName", "getCategory"})
    public NoneFundEvent(Integer id, String name, Category category) {
        super(id, name, category);
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

    /**
     * {@inheritDoc
     */
    @Override
    public String toString() {
        return super.toString() + " " + getCategory().toString();
    }
}
