package com.ntankard.Tracking.DataBase.Interface.Set.Factory.PeriodSummary;

import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Set.ObjectSet;
import com.ntankard.Tracking.DataBase.Interface.Summary.Period_Summary;

import java.util.ArrayList;
import java.util.List;

public class PeriodSummary_Set extends ObjectSet<Period_Summary> {

    /**
     * Constructor
     */
    public PeriodSummary_Set() {
        super(null);
    }

    /**
     * Get the set of objects
     *
     * @return A set of daa objects
     */
    @Override
    public List<Period_Summary> get() {
        List<Period_Summary> toReturn = new ArrayList<>();
        for (Period period : TrackingDatabase.get().get(Period.class)) {
            toReturn.add(Period_Summary.make(period));
        }
        return toReturn;
    }
}
