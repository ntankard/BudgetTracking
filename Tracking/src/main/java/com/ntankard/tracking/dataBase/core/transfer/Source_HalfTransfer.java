package com.ntankard.tracking.dataBase.core.transfer;

import com.ntankard.javaObjectDatabase.coreObject.factory.SingleParentFactory;
import com.ntankard.javaObjectDatabase.coreObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.ParameterMap;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase;

@ParameterMap(shouldSave = false)
public class Source_HalfTransfer extends HalfTransfer {

    public static SingleParentFactory<?, ?> Factory = new SingleParentFactory<>(
            Source_HalfTransfer.class,
            Transfer.class,
            generator -> Source_HalfTransfer.make(TrackingDatabase.get().getNextId(), generator));

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getFieldContainer() {
        DataObject_Schema dataObjectSchema = HalfTransfer.getFieldContainer();

        // Class behavior
        dataObjectSchema.setMyFactory(Factory);

        return dataObjectSchema.finaliseContainer(Source_HalfTransfer.class);
    }

    /**
     * Create a new Source_HalfTransfer object
     */
    public static Source_HalfTransfer make(Integer id, Transfer transfer) {
        return assembleDataObject(Source_HalfTransfer.getFieldContainer(), new Source_HalfTransfer()
                , DataObject_Id, id
                , HalfTransfer_Source, true
                , HalfTransfer_Transfer, transfer
        );
    }
}
