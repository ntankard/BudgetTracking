package com.ntankard.Tracking;

import com.ntankard.Tracking.DataBase.TrackingDatabase;
import com.ntankard.Tracking.DataBase.TrackingDatabase_Reader;
import com.ntankard.Tracking.Frames.TrackingDatabase_Frame;

public class Main {

    public static void main(String args[]){
        TrackingDatabase database = TrackingDatabase_Reader.read();
        TrackingDatabase_Frame.open(database);

       /* TrackingDatabase database = new TrackingDatabase();

        Currency AUD = new Currency("AUS",1);
        Currency YEN = new Currency("YEN",1);
        database.addCurrency(AUD);
        database.addCurrency(YEN);

        Bank com = new Bank("Com", AUD);
        Bank shinzen = new Bank("Shinzen", YEN);
        database.addBank(com);
        database.addBank(shinzen);

        Period dec19 = Period.Month(12,2018);
        Period jan19 = Period.Month(1,2019);
        database.addPeriod(dec19);
        database.addPeriod(jan19);*/
    }
}