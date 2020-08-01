package com.ntankard.TestUtil;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase_Reader;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DataAccessUntil {

    /**
     * Load the read database
     */
    public static void loadDatabase() {
        TrackingDatabase.reset();
        String savePath = "C:\\Users\\Nicholas\\Google Drive\\BudgetTrackingData";
        TrackingDatabase_Reader.read(savePath, new HashMap<>());
        TrackingDatabase.get().finalizeCore();
    }

    /**
     * Get a object to use for a test from the database
     *
     * @param toGet The type of object to get
     * @param index The index of the object to get
     * @param <T>   ToGet
     * @return An object from the database
     */
    public static <T extends DataObject> T getObject(Class<T> toGet, int index) {
        assertTrue(TrackingDatabase.get().get(toGet).size() > index);
        return TrackingDatabase.get().get(toGet).get(index);
    }
}
