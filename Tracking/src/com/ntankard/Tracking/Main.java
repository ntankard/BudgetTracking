package com.ntankard.Tracking;

import com.ntankard.Tracking.DataBase.TrackingDatabase;
import com.ntankard.Tracking.DataBase.TrackingDatabase_Reader;
import com.ntankard.Tracking.Frames.TrackingDatabase_Frame;

public class Main {

    public static void main(String args[]){
        TrackingDatabase database = TrackingDatabase_Reader.read("C:\\Users\\Nicholas\\Documents\\Projects\\BudgetTrackingData");
        TrackingDatabase_Frame.open(database);
    }
}