package com.ntankard.Tracking.DataBase.Interface.Set.SummarySet;

import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Transfers.Transfer;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Set.ObjectSet;

import java.util.ArrayList;
import java.util.List;

public abstract class Summary_Set<SummaryType, ParentType> implements ObjectSet<SummaryType> {

    // The objects used to generate the set
    private Period corePeriod;
    private ParentType coreParent;
    private Class<? extends Transfer> transferType;

    protected Summary_Set(ParentType coreParent) {
        this(null, coreParent, Transfer.class);
    }

    protected Summary_Set(Period corePeriod) {
        this(corePeriod, null, Transfer.class);
    }

    protected Summary_Set(Period corePeriod, ParentType coreParent, Class<? extends Transfer> transferType) {
        this.corePeriod = corePeriod;
        this.coreParent = coreParent;
        this.transferType = transferType;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public List<SummaryType> get() {
        List<SummaryType> toReturn = new ArrayList<>();

        if (coreParent == null && corePeriod == null) {
            for (ParentType parent : getToSummarise()) {
                for (Period period : TrackingDatabase.get().get(Period.class)) {
                    SummaryType summary = getSummary(period, parent, transferType);
                    if (summary != null) {
                        toReturn.add(summary);
                    }
                }
            }
        } else if (coreParent == null) {
            for (ParentType parent : getToSummarise()) {
                SummaryType summary = getSummary(corePeriod, parent, transferType);
                if (summary != null) {
                    toReturn.add(summary);
                }
            }
        } else {
            for (Period period : TrackingDatabase.get().get(Period.class)) {
                SummaryType summary = getSummary(period, coreParent, transferType);
                if (summary != null) {
                    toReturn.add(summary);
                }
            }
        }

        return toReturn;
    }

    /**
     * Get a list of parent objects used to generate a set
     *
     * @return The parent objects
     */
    protected abstract List<ParentType> getToSummarise();

    /**
     * Build the specific summary object
     *
     * @param period       The Period
     * @param parent       The Parent
     * @param transferType The type of object to summarise
     * @return A solid summary object
     */
    protected abstract SummaryType getSummary(Period period, ParentType parent, Class<? extends Transfer> transferType);
}
