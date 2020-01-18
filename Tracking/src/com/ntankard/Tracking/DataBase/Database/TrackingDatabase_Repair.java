package com.ntankard.Tracking.DataBase.Database;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Period.VirtualPeriod;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank.StatementEnd;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.NoneFundEvent;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.SavingsFundEvent;
import com.ntankard.Tracking.DataBase.Core.Transfers.CategoryFundTransfer.RePayCategoryFundTransfer;
import com.ntankard.Tracking.DataBase.Interface.Set.MultiParent_Set;

import java.util.List;

public class TrackingDatabase_Repair {

    /**
     * Repair the global database
     */
    public static void repair() {
        if (TrackingDatabase.get().get(SavingsFundEvent.class).size() != 1) {
            if (TrackingDatabase.get().get(SavingsFundEvent.class).size() == 0) {
                new SavingsFundEvent(TrackingDatabase.get().getNextId(), TrackingDatabase.get().getSpecialValue(Category.class, Category.SAVINGS)).add();
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
            new VirtualPeriod(TrackingDatabase.get().getNextId(), "Before", 0).add();
        }

        if (!afterFound) {
            new VirtualPeriod(TrackingDatabase.get().getNextId(), "After", Integer.MAX_VALUE).add();
        }
    }

    /**
     * Repair any DataObject
     *
     * @param dataObject The object to repair
     */
    public static void repair(DataObject dataObject) {
        if (dataObject instanceof ExistingPeriod) {
            repairExistingPeriod((ExistingPeriod) dataObject);
        } else if (dataObject instanceof FundEvent) {
            repairFundEvent((FundEvent) dataObject);
        } else if (dataObject instanceof Bank) {
            repairBank((Bank) dataObject);
        }
    }

    /**
     * Prepare and object for deletion
     *
     * @param dataObject The object to delete
     */
    public static void prepareForRemove(DataObject dataObject) {
        if (dataObject instanceof NoneFundEvent) {
            if (dataObject.getChildren().size() != 0) {
                throw new RuntimeException("Cant delete this kind of object. NoneFundEvent still has children");
            }
            return;
        }

        if (dataObject instanceof Period || dataObject instanceof Bank || dataObject instanceof FundEvent || dataObject instanceof Category) {
            throw new RuntimeException("Cant delete this kind of object");
        }
    }

    /**
     * Repair a ExistingPeriod type object
     *
     * @param period The period to repair
     */
    private static void repairExistingPeriod(ExistingPeriod period) {
        for (FundEvent fundEvent : TrackingDatabase.get().get(FundEvent.class)) {
            setupRePay(fundEvent, period);
        }

        for (Bank bank : TrackingDatabase.get().get(Bank.class)) {
            setupStatementEnd(bank, period);
        }
    }

    /**
     * Repair a fund event
     *
     * @param fundEvent The event to repair
     */
    public static void repairFundEvent(FundEvent fundEvent) {
        for (ExistingPeriod period : TrackingDatabase.get().get(ExistingPeriod.class)) {
            setupRePay(fundEvent, period);
        }
    }

    /**
     * Repair a Bank event
     *
     * @param bank The event to repair
     */
    private static void repairBank(Bank bank) {
        for (ExistingPeriod period : TrackingDatabase.get().get(ExistingPeriod.class)) {
            setupStatementEnd(bank, period);
        }
    }

    /**
     * Ensure that there is a StatementEnd object for this pair
     *
     * @param bank   The bank
     * @param period The period
     */
    private static void setupStatementEnd(Bank bank, ExistingPeriod period) {
        if (new MultiParent_Set<>(StatementEnd.class, bank, period).get().size() > 1) {
            throw new RuntimeException("More than 1 statement end");
        }
        if (new MultiParent_Set<>(StatementEnd.class, bank, period).get().size() == 0) {
            new StatementEnd(TrackingDatabase.get().getNextId(), period, bank, 0.0).add();
        }
    }

    /**
     * Create  a repayment object for a fund event
     *
     * @param fundEvent The fund event to charge
     * @param period    The period that the payment will occur in
     */
    private static void setupRePay(FundEvent fundEvent, ExistingPeriod period) {
        List<RePayCategoryFundTransfer> toRemove = new MultiParent_Set<>(RePayCategoryFundTransfer.class, fundEvent, period).get();
        for (RePayCategoryFundTransfer rePayCategoryFundTransfer : toRemove) {
            rePayCategoryFundTransfer.remove();
        }

        if (fundEvent.isChargeThisPeriod(period)) {
            new RePayCategoryFundTransfer(TrackingDatabase.get().getNextId(), period, fundEvent, TrackingDatabase.get().getDefault(Currency.class)).add();
        }
    }
}
