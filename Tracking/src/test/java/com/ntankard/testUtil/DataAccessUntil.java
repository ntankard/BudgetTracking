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
     * Get a object to use for a test from the database
     *
     * @param toGet The type of object to get
     * @param index The index of the object to get
     * @param <T>   ToGet
     * @return An object from the database
     */
    public static <T extends DataObject> T getObject(TrackingDatabase trackingDatabase, Class<T> toGet, int index) {
        assertTrue(trackingDatabase.get(toGet).size() > index);
        return trackingDatabase.get(toGet).get(index);
    }

    /**
     * Get the user database
     *
     * @return The user database
     */
    public static TrackingDatabase getDataBase() {
        return Main.createDataBase();
    }

    /**
     * Get the tracking schema
     *
     * @return The tracking schema
     */
    public static TrackingDatabase_Schema getSchema() {
        return TrackingDatabase_Schema.getSchemaFromPackage(Main.databasePath);
    }
}
