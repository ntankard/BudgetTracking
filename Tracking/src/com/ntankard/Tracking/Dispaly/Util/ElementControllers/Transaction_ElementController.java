package com.ntankard.Tracking.Dispaly.Util.ElementControllers;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Statement;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.Transaction;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Category;
import com.ntankard.Tracking.DataBase.TrackingDatabase;
import com.ntankard.Tracking.Dispaly.Util.Panels.TrackingDatabase_ElementController;

public class Transaction_ElementController extends TrackingDatabase_ElementController<Transaction> {

    /**
     * Data to use when creating a new object
     */
    private Statement core;

    /**
     * Constructor
     */
    public Transaction_ElementController(Statement core, Updatable master) {
        super(master);
        this.core = core;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public Transaction newElement() {
        String idCode = TrackingDatabase.get().getNextId(Transaction.class);
        return new Transaction(core, idCode, "", 0.0, TrackingDatabase.get().get(Category.class, "Unaccounted"));
    }

    /**
     * Set the data to use when creating a new object
     *
     * @param core The data to use when creating a new object
     */
    public void setCore(Statement core) {
        this.core = core;
    }
}
