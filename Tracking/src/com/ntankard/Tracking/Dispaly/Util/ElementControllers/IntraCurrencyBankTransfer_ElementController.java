package com.ntankard.Tracking.Dispaly.Util.ElementControllers;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Transfers.BankTransfer.IntraCurrencyBankTransfer;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.Dispaly.Util.Panels.TrackingDatabase_ElementController;

public class IntraCurrencyBankTransfer_ElementController extends TrackingDatabase_ElementController<IntraCurrencyBankTransfer> {

    /**
     * Data to use when creating a new object
     */
    private Period core;

    /**
     * Constructor
     */
    public IntraCurrencyBankTransfer_ElementController(Period core, Updatable master) {
        super(master);
        this.core = core;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public IntraCurrencyBankTransfer newElement() {
        return new IntraCurrencyBankTransfer(TrackingDatabase.get().getNextId(),
                "",
                0.0,
                0.0,
                core,
                TrackingDatabase.get().get(Bank.class).get(0),
                TrackingDatabase.get().get(Bank.class).get(1));
    }
}
