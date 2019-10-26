package com.ntankard.Tracking.Dispaly.Util.Set;

import com.ntankard.Tracking.DataBase.Core.DataObject;
import com.ntankard.Tracking.DataBase.TrackingDatabase;

import java.util.List;

public class Full_Set<T extends DataObject> implements DataObjectSet<T> {

    /**
     * The DataObject to get from the database
     */
    private Class<T> tClass;

    /**
     * Constructor
     */
    public Full_Set(Class<T> tClass) {
        this.tClass = tClass;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public List<T> get() {
        return TrackingDatabase.get().get(tClass);
    }
}
