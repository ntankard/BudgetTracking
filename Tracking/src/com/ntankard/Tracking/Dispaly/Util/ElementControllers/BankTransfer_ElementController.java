package com.ntankard.Tracking.Dispaly.Util.ElementControllers;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.Transfers.BankTransfer;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.Dispaly.Util.Panels.TrackingDatabase_ElementController;

public class BankTransfer_ElementController extends TrackingDatabase_ElementController<BankTransfer> {

    /**
     * Data to use when creating a new object
     */
    private Period core;

    /**
     * Constructor
     */
    public BankTransfer_ElementController(Period core, Updatable master) {
        super(master);
        this.core = core;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public BankTransfer newElement() {
        return new BankTransfer(TrackingDatabase.get().getNextId(),
                "",
                0.0,
                core,
                TrackingDatabase.get().get(Bank.class).get(0),
                TrackingDatabase.get().get(Bank.class).get(1));
    }
}
