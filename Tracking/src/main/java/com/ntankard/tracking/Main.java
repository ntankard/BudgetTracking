package com.ntankard.tracking;

import com.ntankard.tracking.dispaly.frames.mainFrame.Master_Frame;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase_Schema;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase_Reader;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        String databasePath = "com.ntankard.Tracking.DataBase.Core";
        String savePath = "C:\\Users\\Nicholas\\Pictures\\BudgetTrackingData";
        Map<String, String> nameMap = new HashMap<>();

        TrackingDatabase_Schema.get().init(databasePath, nameMap);

        TrackingDatabase.reset();
        TrackingDatabase_Reader.read(savePath, nameMap);

        Master_Frame.open(savePath);
    }
}
