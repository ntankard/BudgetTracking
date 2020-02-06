package com.ntankard.Tracking.Dispaly.Util.ElementControllers;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Core.Transfers.ManualBankCategoryTransfer;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.Dispaly.Util.Panels.TrackingDatabase_ElementController;

public class ManualBankCategoryTransfer_ElementController extends TrackingDatabase_ElementController<ManualBankCategoryTransfer> {

    /**
     * Data to use when creating a new object
     */
    private Period core;
    private Bank bank;

    /**
     * Constructor
     */
    public ManualBankCategoryTransfer_ElementController(Period core, Bank bank, Updatable master) {
        super(master);
        this.core = core;
        this.bank = bank;
    }

    /**
     * Constructor
     */
    public ManualBankCategoryTransfer_ElementController(Period core, Updatable master) {
        super(master);
        this.core = core;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public ManualBankCategoryTransfer newElement() {
        return new ManualBankCategoryTransfer(TrackingDatabase.get().getNextId(),
                "",
                0.0,
                core,
                bank,
                TrackingDatabase.get().getDefault(Category.class));
    }

    /**
     * Set the data to use when creating a new object
     *
     * @param bank The data to use when creating a new object
     */
    public void setBank(Bank bank) {
        this.bank = bank;
    }
}
