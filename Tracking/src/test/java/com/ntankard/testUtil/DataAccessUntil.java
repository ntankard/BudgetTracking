package com.ntankard.testUtil;

import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.database.Database_Schema;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.tracking.Main;

import java.util.List;

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
    public static <T extends DataObject> T getObject(Database database, Class<T> toGet, int index) {
        assertTrue(database.get(toGet).size() > index);
        return database.get(toGet).get(index);
    }

    /**
     * Get the user database
     *
     * @return The user database
     */
    public static Database getDataBase() {
        return Main.createDataBase();
    }

    public static Database getDataBase(List<Class<? extends DataObject>> solidClasses) {
        return Main.createDataBase(solidClasses);
    }

    /**
     * Get the tracking schema
     *
     * @return The tracking schema
     */
    public static Database_Schema getSchema() {
        return Database_Schema.getSchemaFromPackage(Main.databasePath);
    }
}
