package com.ntankard.Tracking.DataBase.Core.Pool.Fund.FundEvent;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.OutCategory;
import com.ntankard.Tracking.DataBase.Core.Pool.Fund.Fund;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Summary.Period_Summary;

@ClassExtensionProperties(includeParent = true)
public class SavingsFundEvent extends FundEvent {

    /**
     * Constructor
     */
    public SavingsFundEvent() {
        super(-1, "Savings", TrackingDatabase.get().getSpecialValue(OutCategory.class, OutCategory.SAVINGS).getChildren(Fund.class).get(0));
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
