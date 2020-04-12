package com.ntankard.Tracking;

import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase_Reader;
import com.ntankard.Tracking.Dispaly.Frames.MainFrame.Master_Frame;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        String savePath = "C:\\Users\\Nicholas\\Google Drive\\BudgetTrackingData";
        Map<String, String> nameMap = new HashMap<>();
        //nameMap.put("com.ntankard.Tracking.DataBase.Core.Pool.Category", "com.ntankard.Tracking.DataBase.Core.Pool.SolidCategory");

        TrackingDatabase.reset();
        TrackingDatabase_Reader.read(savePath, nameMap);
        TrackingDatabase.get().finalizeCore();
        Master_Frame.open(savePath);
    }
}
