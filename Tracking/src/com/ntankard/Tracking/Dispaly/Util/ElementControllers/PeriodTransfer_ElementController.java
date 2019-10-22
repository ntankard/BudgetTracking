package com.ntankard.Tracking.Dispaly.Util.ElementControllers;

import com.ntankard.DynamicGUI.Containers.DynamicGUI_DisplayList.ElementController;
import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.PeriodTransfer;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Category;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Currency;
import com.ntankard.Tracking.DataBase.TrackingDatabase;

public class PeriodTransfer_ElementController implements ElementController<PeriodTransfer> {

    /**
     * Data to use when creating a new object
     */
    private Period period;

    /**
     * The container to notify if the buttons make a database change
     */
    private Updatable master;

    /**
     * Constructor
     */
    public PeriodTransfer_ElementController(Period period, Updatable master) {
        this.period = period;
        this.master = master;
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

    /**
     * {@inheritDoc
     */
    @Override
    public void deleteElement(PeriodTransfer toDel) {
        TrackingDatabase.get().remove(toDel);
        master.notifyUpdate();
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void addElement(PeriodTransfer newObj) {
        TrackingDatabase.get().add(newObj);
        master.notifyUpdate();
    }
}
