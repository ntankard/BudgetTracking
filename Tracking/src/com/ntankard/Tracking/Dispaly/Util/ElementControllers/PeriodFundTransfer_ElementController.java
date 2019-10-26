package com.ntankard.Tracking.Dispaly.Util.ElementControllers;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Fund;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.PeriodFundTransfer;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Category;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Currency;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.FundEvent;
import com.ntankard.Tracking.DataBase.TrackingDatabase;
import com.ntankard.Tracking.Dispaly.Util.Panels.TrackingDatabase_ElementController;

public class PeriodFundTransfer_ElementController extends TrackingDatabase_ElementController<PeriodFundTransfer> {

    /**
     * Data to use when creating a new object
     */
    private Period period;

    /**
     * Constructor
     */
    public PeriodFundTransfer_ElementController(Period period, Updatable master) {
        super(master);
        this.period = period;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public PeriodFundTransfer newElement() {
        String idCode = TrackingDatabase.get().getNextId(PeriodFundTransfer.class);
        return new PeriodFundTransfer(
                idCode,
                period,
                TrackingDatabase.get().get(Fund.class).get(0),
                TrackingDatabase.get().get(Category.class, "Unaccounted"),
                TrackingDatabase.get().get(Fund.class).get(0).<FundEvent>getChildren(FundEvent.class).get(0),
                TrackingDatabase.get().get(Currency.class, "YEN"),
                "",
                0.0);
    }
}
