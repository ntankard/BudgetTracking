package com.ntankard.budgetTracking.dataBase.core.transfer;

import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.dataObject.factory.ObjectFactory;
import com.ntankard.javaObjectDatabase.dataObject.factory.SingleParentFactory;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.javaObjectDatabase.database.ParameterMap;

@ParameterMap(shouldSave = false)
public class Source_HalfTransfer extends HalfTransfer {

    public static ObjectFactory<?> Factory = new SingleParentFactory<>(
            Source_HalfTransfer.class,
            Transfer.class,
            Source_HalfTransfer::new).setCanDelete(true);

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = HalfTransfer.getDataObjectSchema();

        // Class behavior
        dataObjectSchema.setMyFactory(Factory);

        return dataObjectSchema.finaliseContainer(Source_HalfTransfer.class);
    }

    /**
     * Constructor
     */
    public Source_HalfTransfer(Database database, Object... args) {
        super(database, args);
    }

    /**
     * Constructor
     */
    public Source_HalfTransfer(Transfer transfer) {
        super(transfer.getTrackingDatabase()
                , HalfTransfer_Source, true
                , HalfTransfer_Transfer, transfer
        );
    }
}
