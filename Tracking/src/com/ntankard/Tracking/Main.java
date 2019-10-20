package com.ntankard.Tracking;

import com.ntankard.Tracking.DataBase.TrackingDatabase_Reader;
import com.ntankard.Tracking.Dispaly.Frames.MainFrame.Master_Frame;

public class Main {

    public static void main(String[] args) {
        String savePath = "C:\\Users\\Nicholas\\Google Drive\\BudgetTrackingData";
        TrackingDatabase_Reader.read(savePath);
        Master_Frame.open(savePath);
    }
}
