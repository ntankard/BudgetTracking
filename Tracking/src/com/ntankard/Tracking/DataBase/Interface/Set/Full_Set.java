package com.ntankard.Tracking.DataBase.Interface.Set;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;

import java.util.List;

public class Full_Set<T extends DataObject> implements ObjectSet<T> {

    /**
     * The DataObject to get from the database
     */
    protected Class<T> tClass;

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
