package com.ntankard.Tracking.Dispaly.Util.ElementControllers;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.PeriodTransfer;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Category;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Currency;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.Dispaly.Util.Panels.TrackingDatabase_ElementController;

public class PeriodTransfer_ElementController extends TrackingDatabase_ElementController<PeriodTransfer> {

    /**
     * Data to use when creating a new object
     */
    private Period period;

    /**
     * Constructor
     */
    public PeriodTransfer_ElementController(Period period, Updatable master) {
        super(master);
        this.period = period;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public PeriodTransfer newElement() {
        return new PeriodTransfer(TrackingDatabase.get().getNextId(PeriodTransfer.class),
                "",
                0.0,
                period,
                TrackingDatabase.get().getDefault(Category.class),
                TrackingDatabase.get().getDefault(Period.class),
                TrackingDatabase.get().getDefault(Currency.class));
    }
}
