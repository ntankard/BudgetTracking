package com.ntankard.testUtil;

import com.ntankard.javaObjectDatabase.coreObject.DataObject;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase_Schema;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase_Reader;
import com.ntankard.tracking.Main;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DataAccessUntil {

    /**
     * Load the read database
     */
    public static void loadDatabase() {
        Main.createDataBase();
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
