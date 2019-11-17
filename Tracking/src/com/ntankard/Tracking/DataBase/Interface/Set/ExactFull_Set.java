package com.ntankard.Tracking.DataBase.Interface.Set;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;

import java.util.ArrayList;
import java.util.List;

public class ExactFull_Set<T extends DataObject> implements ObjectSet<T> {

    /**
     * The DataObject to get from the database
     */
    private Class<T> tClass;

    /**
     * Constructor
     */
    public ExactFull_Set(Class<T> tClass) {
        this.tClass = tClass;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public List<T> get() {
        List<T> toReturn = new ArrayList<>();
        for (T t : TrackingDatabase.get().get(tClass)) {
            if (t.getClass().equals(tClass)) {
                toReturn.add(t);
            }
        }
        return toReturn;
    }
}
