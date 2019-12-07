package com.ntankard.Tracking.DataBase.Core.Pool.Fund.FundEvent;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Summary.Period_Summary;

@ClassExtensionProperties(includeParent = true)
public class SavingsFundEvent extends FundEvent {

    /**
     * Constructor
     */
    @ParameterMap(shouldSave = false)
    public SavingsFundEvent() {
        super(TrackingDatabase.get().getNextId(), "Savings", TrackingDatabase.get().getSpecialValue(Category.class, Category.SAVINGS));
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
        return true;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public Double getCharge(Period period) {
        return -new Period_Summary(period).getNonSaveTotal();
    }
}
