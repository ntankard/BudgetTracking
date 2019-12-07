package com.ntankard.TestUtil;

import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase_Reader;

public class DataAccessUntil {

    /**
     * Load the read database
     */
    public static void loadDatabase() {
        TrackingDatabase.reset();
        String savePath = "C:\\Users\\Nicholas\\Google Drive\\BudgetTrackingData";
        TrackingDatabase_Reader.read(TrackingDatabase.get(), savePath);
        TrackingDatabase.get().finalizeCore(false);
    }
}
