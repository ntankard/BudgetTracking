package com.ntankard.tracking.dataBase.core.transfer;

import com.ntankard.javaObjectDatabase.coreObject.factory.SingleParentFactory;
import com.ntankard.javaObjectDatabase.coreObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.ParameterMap;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase_Schema;

@ParameterMap(shouldSave = false)
public class Destination_HalfTransfer extends HalfTransfer {

    public static SingleParentFactory<?, ?> Factory = new SingleParentFactory<>(
            Destination_HalfTransfer.class,
            Transfer.class,
            generator -> Destination_HalfTransfer.make(generator.getTrackingDatabase().getNextId(), generator));

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getFieldContainer() {
        DataObject_Schema dataObjectSchema = HalfTransfer.getFieldContainer();

        // Class behavior
        dataObjectSchema.setMyFactory(Factory);

        return dataObjectSchema.finaliseContainer(Destination_HalfTransfer.class);
    }

    /**
     * Create a new Destination_HalfTransfer object
     */
    public static Destination_HalfTransfer make(Integer id, Transfer transfer) {
        TrackingDatabase trackingDatabase = transfer.getTrackingDatabase();
        TrackingDatabase_Schema trackingDatabase_schema = trackingDatabase.getSchema();
        return assembleDataObject(trackingDatabase, trackingDatabase_schema.getClassSchema(Destination_HalfTransfer.class), new Destination_HalfTransfer()
                , DataObject_Id, id
                , HalfTransfer_Source, false
                , HalfTransfer_Transfer, transfer
        );
    }
}
