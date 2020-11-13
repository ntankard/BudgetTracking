package com.ntankard.tracking;

import com.ntankard.tracking.dispaly.frames.mainFrame.Master_Frame;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase_Schema;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase_Reader;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public static String databasePath = "com.ntankard.tracking.dataBase.core";
    public static String savePath = "C:\\Users\\Nicholas\\Google Drive\\BudgetTrackingData";

    public static void createDataBase() {
        Map<String, String> nameMap = new HashMap<>();

        if (!TrackingDatabase_Schema.get().isInitialized()) {
            TrackingDatabase_Schema.get().init(databasePath, nameMap);
        }

        TrackingDatabase_Reader.read(TrackingDatabase_Schema.get(), savePath, nameMap);
    }

    public static void main(String[] args) {
        createDataBase();
        Master_Frame.open(savePath);
    }
}
