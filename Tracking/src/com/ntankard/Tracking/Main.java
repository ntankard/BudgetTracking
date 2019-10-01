package com.ntankard.Tracking;

import com.ntankard.Tracking.DataBase.TrackingDatabase_Reader;
import com.ntankard.Tracking.Dispaly.Frames.MainFrame.Master_Frame;

public class Main {

    public static void main(String args[]) {
        TrackingDatabase_Reader.read("C:\\Users\\Nicholas\\Documents\\Projects\\BudgetTrackingData");
        Master_Frame.open();
    }
}