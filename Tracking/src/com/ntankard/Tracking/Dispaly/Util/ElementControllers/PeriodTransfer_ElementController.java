package com.ntankard.Tracking.Dispaly.Util.ElementControllers;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.PeriodTransfer;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Category;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Currency;
import com.ntankard.Tracking.DataBase.TrackingDatabase;
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
        String idCode = TrackingDatabase.get().getNextId(PeriodTransfer.class);
        return new PeriodTransfer(
                idCode,
                period,
                TrackingDatabase.get().get(Period.class).get(1),
                TrackingDatabase.get().get(Currency.class, "YEN"),
                TrackingDatabase.get().get(Category.class, "Unaccounted"),
                "",
                0.0);
    }
}
