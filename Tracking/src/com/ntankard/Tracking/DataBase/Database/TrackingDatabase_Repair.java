package com.ntankard.Tracking.DataBase.Database;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Statement;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.FundChargeTransfer.FundChargeTransfer;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.FundChargeTransfer.SavingsChargeTransfer;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.FundChargeTransfer.TaxChargeTransfer;
import com.ntankard.Tracking.DataBase.Core.SupportObjects.Bank;

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
        }
    }

    /**
     * Repair a Period type object
     *
     * @param period The period to repair
     */
    private static void repairPeriod(Period period) {
        generateStatements(period);
        generateFundCharge(period);
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
}
