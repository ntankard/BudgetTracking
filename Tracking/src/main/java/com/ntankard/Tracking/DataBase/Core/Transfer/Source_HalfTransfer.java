package com.ntankard.Tracking.DataBase.Core.Transfer;

import com.ntankard.javaObjectDatabase.CoreObject.Factory.SingleParentFactory;
import com.ntankard.javaObjectDatabase.CoreObject.FieldContainer;
import com.ntankard.javaObjectDatabase.Database.ParameterMap;
import com.ntankard.javaObjectDatabase.Database.TrackingDatabase;

@ParameterMap(shouldSave = false)
public class Source_HalfTransfer extends HalfTransfer {

    public static SingleParentFactory<?, ?> Factory = new SingleParentFactory<>(
            Source_HalfTransfer.class,
            Transfer.class,
            generator -> Source_HalfTransfer.make(TrackingDatabase.get().getNextId(), generator));

    /**
     * Get all the fields for this object
     */
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = HalfTransfer.getFieldContainer();

        // Class behavior
        fieldContainer.setMyFactory(Factory);

        return fieldContainer.finaliseContainer(Source_HalfTransfer.class);
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
