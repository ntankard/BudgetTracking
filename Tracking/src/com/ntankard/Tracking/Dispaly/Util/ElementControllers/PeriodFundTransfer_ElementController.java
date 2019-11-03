package com.ntankard.Tracking.Dispaly.Util.ElementControllers;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Fund;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.PeriodFundTransfer;
import com.ntankard.Tracking.DataBase.Core.MoneyCategory.Category;
import com.ntankard.Tracking.DataBase.Core.SupportObjects.Currency;
import com.ntankard.Tracking.DataBase.Core.MoneyCategory.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
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
        return new PeriodFundTransfer(TrackingDatabase.get().getNextId(PeriodFundTransfer.class),
                "",
                0.0,
                period,
                TrackingDatabase.get().getDefault(Category.class),
                TrackingDatabase.get().getDefault(Fund.class),
                TrackingDatabase.get().getDefault(Fund.class).getChildren(FundEvent.class).get(0),
                TrackingDatabase.get().getDefault(Currency.class));
    }
}
