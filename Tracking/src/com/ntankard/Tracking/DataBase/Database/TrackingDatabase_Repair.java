package com.ntankard.Tracking.DataBase.Database;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank;
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
                TrackingDatabase.get().add(new SavingsFundEvent());
            } else {
                throw new RuntimeException("More than 1 savings event");
            }
        }
    }

    /**
     * Repair any DataObject
     *
     * @param dataObject The object to repair
     */
    public static void repair(DataObject dataObject) {
        if (dataObject instanceof Period) {
            repairPeriod((Period) dataObject);
        } else if (dataObject instanceof FundEvent) {
            repairFundEvent((FundEvent) dataObject);
        }
    }

    /**
     * Prepare and object for deletion
     *
     * @param dataObject The object to delete
     */
    public static void prepareForRemove(DataObject dataObject) {
        if(dataObject instanceof NoneFundEvent){
            if(dataObject.getChildren().size() != 0){
                throw new RuntimeException("Cant delete this kind of object. NoneFundEvent still has children");
            }
            return;
        }

        if (dataObject instanceof Period || dataObject instanceof Bank || dataObject instanceof FundEvent || dataObject instanceof Category) {
            throw new RuntimeException("Cant delete this kind of object");
        }
    }

    /**
     * Repair a Period type object
     *
     * @param period The period to repair
     */
    private static void repairPeriod(Period period) {
        List<Period> periods = TrackingDatabase.get().get(Period.class);
        for (int i = 0; i < periods.size(); i++) {
            if (i >= 1) {
                periods.get(i).setLast(periods.get(i - 1));
            }
            if (i < periods.size() - 1) {
                periods.get(i).setNext(periods.get(i + 1));
            }
        }

        for (FundEvent fundEvent : TrackingDatabase.get().get(FundEvent.class)) {
            setupRePay(fundEvent, period);
        }
    }

    /**
     * Repair a fund event
     *
     * @param fundEvent The event to repair
     */
    public static void repairFundEvent(FundEvent fundEvent) {
        for (Period period : TrackingDatabase.get().get(Period.class)) {
            setupRePay(fundEvent, period);
        }
    }

    /**
     * Create  a repayment object for a fund event
     *
     * @param fundEvent The fund event to charge
     * @param period    The period that the payment will occur in
     */
    private static void setupRePay(FundEvent fundEvent, Period period) {
        List<RePayCategoryFundTransfer> toRemove = new MultiParent_Set<>(RePayCategoryFundTransfer.class, fundEvent, period).get();
        for (RePayCategoryFundTransfer rePayCategoryFundTransfer : toRemove) {
            TrackingDatabase.get().remove(rePayCategoryFundTransfer);
        }

        if (fundEvent.isChargeThisPeriod(period)) {
            TrackingDatabase.get().add(new RePayCategoryFundTransfer(TrackingDatabase.get().getNextId(), period, fundEvent, TrackingDatabase.get().getDefault(Currency.class)));
        }
    }
}
