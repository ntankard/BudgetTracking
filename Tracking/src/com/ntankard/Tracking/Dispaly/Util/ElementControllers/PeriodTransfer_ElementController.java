package com.ntankard.Tracking.Dispaly.Util.ElementControllers;

import com.ntankard.DynamicGUI.Components.List.DynamicGUI_DisplayList.ElementController;
import com.ntankard.DynamicGUI.Util.Updatable;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.PeriodTransfer;
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
        String idCode = TrackingDatabase.get().getNextPeriodTransferId();
        return new PeriodTransfer(
                idCode,
                period,
                TrackingDatabase.get().getPeriods().get(1),
                TrackingDatabase.get().getCurrency("YEN"),
                TrackingDatabase.get().getCategory("Unaccounted"),
                "",
                0.0);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void deleteElement(PeriodTransfer toDel) {
        TrackingDatabase.get().removePeriodTransfer(toDel);
        master.notifyUpdate();
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void addElement(PeriodTransfer newObj) {
        TrackingDatabase.get().addPeriodTransfer(newObj);
        master.notifyUpdate();
    }
}
