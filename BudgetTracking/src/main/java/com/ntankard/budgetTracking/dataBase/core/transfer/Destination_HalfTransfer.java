package com.ntankard.budgetTracking.dataBase.core.transfer;

import com.ntankard.javaObjectDatabase.dataObject.factory.SingleParentFactory;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.ParameterMap;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.javaObjectDatabase.database.Database_Schema;

@ParameterMap(shouldSave = false)
public class Destination_HalfTransfer extends HalfTransfer {

    public static SingleParentFactory<?, ?> Factory = new SingleParentFactory<>(
            Destination_HalfTransfer.class,
            Transfer.class,
            Destination_HalfTransfer::new);

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = HalfTransfer.getDataObjectSchema();

        // Class behavior
        dataObjectSchema.setMyFactory(Factory);

        return dataObjectSchema.finaliseContainer(Destination_HalfTransfer.class);
    }

    /**
     * Constructor
     */
    public Destination_HalfTransfer(Database database) {
        super(database);
    }

    /**
     * Constructor
     */
    public Destination_HalfTransfer(Transfer transfer) {
        this(transfer.getTrackingDatabase());
        setAllValues(DataObject_Id, getTrackingDatabase().getNextId()
                , HalfTransfer_Source, false
                , HalfTransfer_Transfer, transfer
        );
    }
}
