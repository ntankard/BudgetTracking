package com.ntankard.Tracking.Dispaly.Util.ElementControllers;

import com.ntankard.DynamicGUI.Components.List.DynamicGUI_DisplayList.ElementController;
import com.ntankard.DynamicGUI.Util.Updatable;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.NonPeriodFundTransfer;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.NonPeriodFundEvent;
import com.ntankard.Tracking.DataBase.TrackingDatabase;

public class NonPeriodTransfer_ElementController implements ElementController<NonPeriodFundTransfer> {

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
    public NonPeriodTransfer_ElementController(Period period, Updatable master) {
        this.period = period;
        this.master = master;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public NonPeriodFundTransfer newElement() {
        String idCode = TrackingDatabase.get().getNextNonPeriodFundTransferId();
        return new NonPeriodFundTransfer(
                idCode,
                period,
                TrackingDatabase.get().getNonPeriodFunds().get(0),
                TrackingDatabase.get().getCategory("Unaccounted"),
                TrackingDatabase.get().getNonPeriodFunds().get(0).<NonPeriodFundEvent>getChildren(NonPeriodFundEvent.class).get(0),
                TrackingDatabase.get().getCurrency("YEN"),
                "",
                0.0);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void deleteElement(NonPeriodFundTransfer toDel) {
        TrackingDatabase.get().removeNonPeriodFundTransfer(toDel);
        master.notifyUpdate();
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void addElement(NonPeriodFundTransfer newObj) {
        TrackingDatabase.get().addNonPeriodFundTransfer(newObj);
        master.notifyUpdate();
    }
}
