package com.ntankard.Tracking.DataBase.Database;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.Category;
import com.ntankard.Tracking.DataBase.Core.Pool.Fund.Fund;
import com.ntankard.Tracking.DataBase.Core.Pool.Fund.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Core.Pool.Fund.FundEvent.NoneFundEvent;
import com.ntankard.Tracking.DataBase.Core.Transfers.CategoryFundTransfer.CategoryFundTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfers.CategoryFundTransfer.RePayCategoryFundTransfer;
import com.ntankard.Tracking.DataBase.Interface.Set.MultiParent_Set;

import java.util.List;

public class TrackingDatabase_Repair {

    /**
     * Repair any DataObject
     *
     * @param dataObject The object to repair
     */
    public static void repair(DataObject dataObject) {
        if (dataObject instanceof Period) {
            repairPeriod((Period) dataObject);
        } else if (dataObject instanceof Fund) {
            repairFund((Fund) dataObject);
        } else if (dataObject instanceof Category) {
            repairCategory((Category) dataObject);
        } else if (dataObject instanceof CategoryFundTransfer) {
            repairCategoryFundTransfer((CategoryFundTransfer) dataObject);
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
        if (dataObject instanceof Period || dataObject instanceof Bank || dataObject instanceof Fund || dataObject instanceof Category) {
            throw new RuntimeException("Cant delete this kind of object");
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

        for (FundEvent fundEvent : TrackingDatabase.get().get(FundEvent.class)) {
            setupRePay(fundEvent, period);
        }
    }

    /**
     * Repair a Category object
     *
     * @param category The object to repair
     */
    private static void repairCategory(Category category) {
        Fund child = category.getChildren(Fund.class).get(0);
        if (child == null) {
            TrackingDatabase.get().add(new Fund(TrackingDatabase.get().getNextId(), category));
        }
    }

    /**
     * Repair a Fund object
     *
     * @param fund The object to repair
     */
    private static void repairFund(Fund fund) {
        for (FundEvent fundEvent : fund.getChildren(FundEvent.class)) {
            if (fundEvent instanceof NoneFundEvent) {
                if (!fundEvent.equals(fund.getDefaultFundEvent())) {
                    if (fund.getDefaultFundEvent() != null && !fund.getDefaultFundEvent().equals(fundEvent)) {
                        throw new RuntimeException("Multiple fund defaults");
                    }
                    fund.setDefaultFundEvent(fundEvent);
                }
            }
        }

        if (fund.getDefaultFundEvent() == null) {
            TrackingDatabase.get().add(new NoneFundEvent(TrackingDatabase.get().getNextId(), "None", fund));
        }
    }

    /**
     * Repair a CategoryFundTransfer object
     *
     * @param categoryFundTransfer The object to repair
     */
    private static void repairCategoryFundTransfer(CategoryFundTransfer categoryFundTransfer) {
        if (categoryFundTransfer.getFundEvent() == null) {
            if (categoryFundTransfer.getDestination().getDefaultFundEvent() == null) {
                repairFund(categoryFundTransfer.getDestination());
            }
            categoryFundTransfer.setFundEvent(categoryFundTransfer.getDestination().getDefaultFundEvent());
        }
    }

    /**
     * Repair a fund event
     *
     * @param fundEvent The event to repair
     */
    private static void repairFundEvent(FundEvent fundEvent) {
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
