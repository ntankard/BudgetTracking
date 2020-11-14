package com.ntankard.tracking;

import com.ntankard.tracking.dispaly.frames.mainFrame.Master_Frame;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase_Schema;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase_Reader;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public static String databasePath = "com.ntankard.tracking.dataBase";
    public static String savePath = "C:\\Users\\Nicholas\\Google Drive\\BudgetTrackingData";

    public static TrackingDatabase createDataBase() {
        Map<String, String> nameMap = new HashMap<>();
        return TrackingDatabase_Reader.read(TrackingDatabase_Schema.getSchemaFromPackage(databasePath), savePath, nameMap);
    }

    public static void main(String[] args) {
        Master_Frame.open(createDataBase(), savePath);
    }
}
