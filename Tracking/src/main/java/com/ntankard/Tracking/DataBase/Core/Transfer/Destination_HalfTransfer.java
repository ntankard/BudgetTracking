package com.ntankard.Tracking.DataBase.Core.Transfer;

import com.ntankard.javaObjectDatabase.CoreObject.Factory.SingleParentFactory;
import com.ntankard.javaObjectDatabase.CoreObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.Database.ParameterMap;
import com.ntankard.javaObjectDatabase.Database.TrackingDatabase;

@ParameterMap(shouldSave = false)
public class Destination_HalfTransfer extends HalfTransfer {

    public static SingleParentFactory<?, ?> Factory = new SingleParentFactory<>(
            Destination_HalfTransfer.class,
            Transfer.class,
            generator -> Destination_HalfTransfer.make(TrackingDatabase.get().getNextId(), generator));

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
        return assembleDataObject(Destination_HalfTransfer.getFieldContainer(), new Destination_HalfTransfer()
                , DataObject_Id, id
                , HalfTransfer_Source, false
                , HalfTransfer_Transfer, transfer
        );
    }
}
