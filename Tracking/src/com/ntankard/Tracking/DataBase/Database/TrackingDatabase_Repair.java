package com.ntankard.Tracking.DataBase.Database;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.MoneyCategory.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Core.MoneyCategory.FundEvent.PeriodBoundFundEvent;
import com.ntankard.Tracking.DataBase.Core.Pool.Fund;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Statement;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.PeriodFundTransfer.PeriodBoundFundEvent_Transfer;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.PeriodFundTransfer.PeriodFundTransfer;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank;
import com.ntankard.Tracking.DataBase.Core.SupportObjects.Currency;

public class TrackingDatabase_Repair {

    /**
     * Repair any DataObject
     *
     * @param dataObject The object to repair
     */
    public static void repair(DataObject dataObject) {
        if (dataObject instanceof Period) {
            repairPeriod((Period) dataObject);
        } else if (dataObject instanceof Bank) {
            repairBank((Bank) dataObject);
        } else if (dataObject instanceof PeriodBoundFundEvent) {
            repairPeriodBoundFundEvent((PeriodBoundFundEvent) dataObject);
        } else if (dataObject instanceof Fund) {
            System.out.println("Implement repair for Fund (generateManagersChargeTransfer)");
        }
    }

    /**
     * Repair a Period type object
     *
     * @param period The period to repair
     */
    private static void repairPeriod(Period period) {
        if (period.getLast() != null) {
            period.getLast().setNext(period);
        }
        generateStatements(period);
        for (FundEvent fundEvent : TrackingDatabase.get().get(FundEvent.class)) {
            if (fundEvent instanceof PeriodBoundFundEvent) {
                repairPeriodBoundFundEvent((PeriodBoundFundEvent) fundEvent);
            }
        }
    }

    /**
     * Repair a Bank type object
     *
     * @param added The bank object to repair
     */
    private static void repairBank(Bank added) {
        for (Period period : TrackingDatabase.get().get(Period.class)) {
            generateStatements(period);
        }
    }

    /**
     * Repair a PeriodBoundFundEvent type object
     *
     * @param periodBoundFundEvent The PeriodBoundFundEvent to repair
     */
    private static void repairPeriodBoundFundEvent(PeriodBoundFundEvent periodBoundFundEvent) {
        Period toAdd = periodBoundFundEvent.getStart();

        int addedCount = 0;
        while (addedCount < periodBoundFundEvent.getDuration()) {
            boolean found = false;
            for (PeriodFundTransfer periodFundTransfer : periodBoundFundEvent.getChildren(PeriodFundTransfer.class)) {
                if (periodFundTransfer instanceof PeriodBoundFundEvent_Transfer) {
                    if (periodFundTransfer.getSourceContainer().equals(toAdd)) {
                        found = true;
                        break;
                    }
                }
            }

            if (!found) {
                TrackingDatabase.get().add(new PeriodBoundFundEvent_Transfer(TrackingDatabase.get().getNextId(PeriodFundTransfer.class),
                        periodBoundFundEvent.getName() + " payment",
                        toAdd,
                        periodBoundFundEvent.getSource(),
                        periodBoundFundEvent.getFund(),
                        periodBoundFundEvent,
                        TrackingDatabase.get().getDefault(Currency.class)));
            }

            if (toAdd.getNext() == null) {
                break;
            }
            toAdd = toAdd.getNext();
            addedCount++;
        }
    }

    /**
     * Ensure that statements are generated for all pairs of Bank accounts and Periods
     *
     * @param period The Period to check and generated if needed
     */
    private static void generateStatements(Period period) {

        for (Bank b : TrackingDatabase.get().get(Bank.class)) {
            boolean found = false;
            for (Statement statement : period.getChildren(Statement.class)) {
                if (statement.getBank().equals(b)) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                Double lastEnd = 0.0;
                if (period.getLast() != null) {
                    for (Statement statement : period.getLast().getChildren(Statement.class)) {
                        if (statement.getBank().equals(b)) {
                            lastEnd = statement.getEnd();
                            break;
                        }
                    }
                }
                TrackingDatabase.get().add(new Statement(TrackingDatabase.get().getNextId(Statement.class), b, period, lastEnd, 0.0, 0.0, 0.0));
            }
        }
    }
}
