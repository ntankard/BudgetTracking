package com.ntankard.tracking;

import com.ntankard.tracking.dispaly.frames.mainFrame.Master_Frame;
import com.ntankard.javaObjectDatabase.database.Database_Schema;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.javaObjectDatabase.database.io.Database_IO;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public static String databasePath = "com.ntankard.tracking.dataBase";
    public static String savePath = "C:\\Users\\Nicholas\\Google Drive\\BudgetTrackingData";

    public static Database createDataBase() {
        Map<String, String> nameMap = new HashMap<>();
        return Database_IO.read(Database_Schema.getSchemaFromPackage(databasePath), savePath, nameMap);
    }

    public static void main(String[] args) {
        ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(true); // TODO remove
        Master_Frame.open(createDataBase(), savePath);
    }
}
