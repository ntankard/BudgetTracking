package com.ntankard.Tracking.Dispaly.Util.ElementControllers;

import com.ntankard.DynamicGUI.Components.List.DynamicGUI_DisplayList.ElementController;
import com.ntankard.DynamicGUI.Util.Updatable;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.PeriodFundTransfer;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.FundEvent;
import com.ntankard.Tracking.DataBase.TrackingDatabase;

public class PeriodFundTransfer_ElementController implements ElementController<PeriodFundTransfer> {

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
    public PeriodFundTransfer_ElementController(Period period, Updatable master) {
        this.period = period;
        this.master = master;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public PeriodFundTransfer newElement() {
        String idCode = TrackingDatabase.get().getNextPeriodFundTransferId();
        return new PeriodFundTransfer(
                idCode,
                period,
                TrackingDatabase.get().getFunds().get(0),
                TrackingDatabase.get().getCategory("Unaccounted"),
                TrackingDatabase.get().getFunds().get(0).<FundEvent>getChildren(FundEvent.class).get(0),
                TrackingDatabase.get().getCurrency("YEN"),
                "",
                0.0);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void deleteElement(PeriodFundTransfer toDel) {
        TrackingDatabase.get().removePeriodFundTransfer(toDel);
        master.notifyUpdate();
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void addElement(PeriodFundTransfer newObj) {
        TrackingDatabase.get().addPeriodFundTransfer(newObj);
        master.notifyUpdate();
    }
}
