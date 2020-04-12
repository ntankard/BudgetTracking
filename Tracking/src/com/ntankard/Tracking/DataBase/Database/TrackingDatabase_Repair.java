package com.ntankard.Tracking.DataBase.Database;

import com.ntankard.Tracking.DataBase.Core.Period.VirtualPeriod;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.SolidCategory;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.SavingsFundEvent;

public class TrackingDatabase_Repair {

    /**
     * Repair the global database
     */
    public static void repair() {
        if (TrackingDatabase.get().get(SavingsFundEvent.class).size() != 1) {
            if (TrackingDatabase.get().get(SavingsFundEvent.class).size() == 0) {
                SavingsFundEvent.make(TrackingDatabase.get().getNextId(), TrackingDatabase.get().getSpecialValue(SolidCategory.class, SolidCategory.SAVINGS)).add();
            } else {
                throw new RuntimeException("More than 1 savings event");
            }
        }

        boolean beforeFound = false;
        boolean afterFound = false;
        for (VirtualPeriod virtualPeriod : TrackingDatabase.get().get(VirtualPeriod.class)) {
            if (virtualPeriod.getOrder() == 0) {
                beforeFound = true;
            }

            if (virtualPeriod.getOrder() == Integer.MAX_VALUE) {
                afterFound = true;
            }
        }

        if (!beforeFound) {
            VirtualPeriod.make(TrackingDatabase.get().getNextId(), "Before", 0).add();
        }

        if (!afterFound) {
            VirtualPeriod.make(TrackingDatabase.get().getNextId(), "After", Integer.MAX_VALUE).add();
        }
    }
}
