package com.ntankard.Tracking;

import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase_Reader;
import com.ntankard.Tracking.Dispaly.Frames.MainFrame.Master_Frame;

public class Main {

    public static void main(String[] args) {
        TrackingDatabase.reset();
        String savePath = "C:\\Users\\Nicholas\\Google Drive\\BudgetTrackingData";
        TrackingDatabase_Reader.read(savePath);
        TrackingDatabase.get().finalizeCore();
        Master_Frame.open(savePath);
    }
}
