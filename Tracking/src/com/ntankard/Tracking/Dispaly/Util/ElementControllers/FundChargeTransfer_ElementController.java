package com.ntankard.Tracking.Dispaly.Util.ElementControllers;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Fund;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.FundChargeTransfer;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.PeriodFundTransfer;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Currency;
import com.ntankard.Tracking.DataBase.TrackingDatabase;
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
        String idCode = TrackingDatabase.get().getNextId(PeriodFundTransfer.class);
        return new FundChargeTransfer(
                idCode,
                period,
                TrackingDatabase.get().get(Fund.class).get(0),
                TrackingDatabase.get().get(Currency.class, "YEN"),
                "",
                0.0);
    }
}
