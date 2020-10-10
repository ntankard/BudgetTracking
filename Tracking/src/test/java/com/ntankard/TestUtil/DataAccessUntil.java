package com.ntankard.TestUtil;

import com.ntankard.javaObjectDatabase.CoreObject.DataObject;
import com.ntankard.javaObjectDatabase.CoreObject.TrackingDatabase_Schema;
import com.ntankard.javaObjectDatabase.Database.TrackingDatabase;
import com.ntankard.javaObjectDatabase.Database.TrackingDatabase_Reader;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DataAccessUntil {

    /**
     * Load the read database
     */
    public static void loadDatabase() {
        String databasePath = "com.ntankard.Tracking.DataBase.Core";
        String savePath = "C:\\Users\\Nicholas\\Google Drive\\BudgetTrackingData";
        Map<String, String> nameMap = new HashMap<>();

        if (!TrackingDatabase_Schema.get().isInitialized()) {
            TrackingDatabase_Schema.get().init(databasePath, nameMap);
        }

        TrackingDatabase.reset();
        TrackingDatabase_Reader.read(savePath, new HashMap<>());
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
