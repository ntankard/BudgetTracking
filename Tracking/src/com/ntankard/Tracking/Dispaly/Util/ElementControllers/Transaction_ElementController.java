package com.ntankard.Tracking.Dispaly.Util.ElementControllers;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Statement;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.Transaction;
import com.ntankard.Tracking.DataBase.Core.MoneyCategory.Category;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.Dispaly.Util.Panels.TrackingDatabase_ElementController;

public class Transaction_ElementController extends TrackingDatabase_ElementController<Transaction> {

    /**
     * Data to use when creating a new object
     */
    private Statement statement;

    /**
     * Constructor
     */
    public Transaction_ElementController(Statement statement, Updatable master) {
        super(master);
        this.statement = statement;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public Transaction newElement() {
        return new Transaction(TrackingDatabase.get().getNextId(Transaction.class),
                "",
                0.0,
                statement,
                TrackingDatabase.get().getDefault(Category.class));
    }

    /**
     * Set the data to use when creating a new object
     *
     * @param statement The data to use when creating a new object
     */
    public void setStatement(Statement statement) {
        this.statement = statement;
    }
}
