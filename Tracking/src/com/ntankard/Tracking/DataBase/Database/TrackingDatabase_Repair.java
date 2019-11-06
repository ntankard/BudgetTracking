package com.ntankard.Tracking.DataBase.Database;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.MoneyCategory.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Core.MoneyCategory.FundEvent.PeriodBoundFundEvent;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Fund;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Statement;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.FundChargeTransfer.FundChargeTransfer;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.FundChargeTransfer.ManagersChargeTransfer;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.FundChargeTransfer.SavingsChargeTransfer;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.FundChargeTransfer.TaxChargeTransfer;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.PeriodFundTransfer.PeriodBoundFundEvent_Transfer;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.PeriodFundTransfer.PeriodFundTransfer;
import com.ntankard.Tracking.DataBase.Core.SupportObjects.Bank;
import com.ntankard.Tracking.DataBase.Core.SupportObjects.Currency;
import com.ntankard.Tracking.DataBase.Core.SupportObjects.FundController;

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
        generateFundCharge(period);
        for (FundEvent fundEvent : TrackingDatabase.get().get(FundEvent.class)) {
            if (fundEvent instanceof PeriodBoundFundEvent) {
                repairPeriodBoundFundEvent((PeriodBoundFundEvent) fundEvent);
            }
        }
        generateFundController(period);
        generateManagersChargeTransfer(period);
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

    /**
     * Generate all required Fund charges for a period if they don't already exist
     *
     * @param period The period to check and generate for
     */
    private static void generateFundCharge(Period period) {
        boolean hexFound = false;
        boolean saveFound = false;
        for (FundChargeTransfer fundChargeTransfer : period.getChildren(FundChargeTransfer.class)) {
            if (fundChargeTransfer instanceof TaxChargeTransfer) {
                hexFound = true;
            }
            if (fundChargeTransfer instanceof SavingsChargeTransfer) {
                saveFound = true;
            }
        }
        if (!hexFound) {
            TrackingDatabase.get().add(FundChargeTransfer.class, new TaxChargeTransfer(period));
        }
        if (!saveFound) {
            TrackingDatabase.get().add(FundChargeTransfer.class, new SavingsChargeTransfer(period));
        }
    }

    /**
     * Add the fund controller  object to a period
     *
     * @param period The period to add to
     */
    private static void generateFundController(Period period) {
        if (period.getChildren(FundController.class).size() == 0) {
            TrackingDatabase.get().add(new FundController(TrackingDatabase.get().getNextId(FundController.class), period));
        }
    }

    /**
     * Add a ManagersChargeTransfer for each fund without a charge
     *
     * @param period The period its from
     */
    private static void generateManagersChargeTransfer(Period period) {
        for (Fund fund : TrackingDatabase.get().get(Fund.class)) {
            boolean found = false;
            for (FundChargeTransfer fundChargeTransfer : period.getChildren(FundChargeTransfer.class)) {
                if (fundChargeTransfer.getDestinationContainer().equals(fund)) {
                    found = true;
                }
            }

            if (!found) {
                TrackingDatabase.get().add(new ManagersChargeTransfer(period, fund));
            }
        }
    }
}
