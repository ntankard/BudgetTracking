package com.ntankard.Tracking.Dispaly.Util.ElementControllers;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Fund;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.FundChargeTransfer.FundChargeTransfer;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.PeriodFundTransfer.PeriodFundTransfer;
import com.ntankard.Tracking.DataBase.Core.SupportObjects.Currency;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.Dispaly.Util.Panels.TrackingDatabase_ElementController;

public class FundChargeTransfer_ElementController extends TrackingDatabase_ElementController<FundChargeTransfer> {

    /**
     * Data to use when creating a new object
     */
    private Period period;

    /**
     * Constructor
     */
    public FundChargeTransfer_ElementController(Period period, Updatable master) {
        super(master);
        this.period = period;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public FundChargeTransfer newElement() {
        return new FundChargeTransfer(TrackingDatabase.get().getNextId(PeriodFundTransfer.class),
                "",
                0.0,
                period,
                TrackingDatabase.get().get(Fund.class).get(0),
                TrackingDatabase.get().getDefault(Currency.class));
    }
}
