package com.ntankard.Tracking.DataBase.Database;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.Category;
import com.ntankard.Tracking.DataBase.Core.Pool.Fund.Fund;
import com.ntankard.Tracking.DataBase.Core.Pool.Fund.FundEvent;
import com.ntankard.Tracking.DataBase.Core.Transfers.CategoryFundTransfer;
import com.ntankard.Tracking.DataBase.Interface.Set.Children_Set;
import com.ntankard.Tracking.DataBase.Interface.Set.MultiParent_Set;
import com.ntankard.Tracking.DataBase.Interface.Summary.Period_Summary;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.Bank_Summary;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.Category_Summary;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.Fund_Summary;

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
        } else if (dataObject instanceof Bank) {
            repairBank((Bank) dataObject);
        } else if (dataObject instanceof CategoryFundTransfer) {
            repairCategoryFundTransfer((CategoryFundTransfer) dataObject);
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

        int size = new Children_Set<>(Period_Summary.class, period).get().size();
        if (size >= 2) {
            throw new RuntimeException("Multiple single objects exist");
        } else if (size == 0) {
            TrackingDatabase.get().add(new Period_Summary(TrackingDatabase.get().getNextId(), period));
        }

        for (Bank bank : TrackingDatabase.get().get(Bank.class)) {
            setupBankSummary(period, bank);
        }

        for (Category category : TrackingDatabase.get().get(Category.class)) {
            setupCategorySummary(period, category);
        }

        for (Fund fund : TrackingDatabase.get().get(Fund.class)) {
            setupFundSummary(period, fund);
        }
    }

    /**
     * Repair a Bank object
     *
     * @param bank The object to repair
     */
    private static void repairBank(Bank bank) {
        for (Period period : TrackingDatabase.get().get(Period.class)) {
            setupBankSummary(period, bank);
        }
    }

    /**
     * Setup a BankSummary object if it dose not exist
     *
     * @param period The period to summarise
     * @param bank   The bank to summarise
     */
    private static void setupBankSummary(Period period, Bank bank) {
        int size = new MultiParent_Set<>(Bank_Summary.class, period, bank).get().size();
        if (size >= 2) {
            throw new RuntimeException("Multiple single objects exist");
        } else if (size == 0) {
            TrackingDatabase.get().add(new Bank_Summary(TrackingDatabase.get().getNextId(), period, bank));
        }
    }

    /**
     * Repair a Category object
     *
     * @param category The object to repair
     */
    private static void repairCategory(Category category) {
        for (Period period : TrackingDatabase.get().get(Period.class)) {
            setupCategorySummary(period, category);
        }

        Fund child = category.getChildren(Fund.class).get(0);
        if (child == null) {
            TrackingDatabase.get().add(new Fund(TrackingDatabase.get().getNextId(), category));
        }
    }

    /**
     * Setup a BankSummary object if it dose not exist
     *
     * @param period   The period to summarise
     * @param category The category to summarise
     */
    private static void setupCategorySummary(Period period, Category category) {
        int size = new MultiParent_Set<>(Category_Summary.class, period, category).get().size();
        if (size >= 2) {
            throw new RuntimeException("Multiple single objects exist");
        } else if (size == 0) {
            TrackingDatabase.get().add(new Category_Summary(TrackingDatabase.get().getNextId(), period, category));
        }
    }

    /**
     * Repair a Fund object
     *
     * @param fund The object to repair
     */
    private static void repairFund(Fund fund) {
        for (Period period : TrackingDatabase.get().get(Period.class)) {
            setupFundSummary(period, fund);
        }

        for (FundEvent fundEvent : fund.getChildren(FundEvent.class)) {
            if (fundEvent.isFundDefault()) {
                if (!fundEvent.equals(fund.getDefaultFundEvent())) {
                    if (fund.getDefaultFundEvent() != null && !fund.getDefaultFundEvent().equals(fundEvent)) {
                        throw new RuntimeException("Multiple fund defaults");
                    }
                    fund.setDefaultFundEvent(fundEvent);
                }
            }
        }

        if (fund.getDefaultFundEvent() == null) {
            TrackingDatabase.get().add(new FundEvent(TrackingDatabase.get().getNextId(), "None", fund, true));
        }
    }

    /**
     * Setup a BankSummary object if it dose not exist
     *
     * @param period The period to summarise
     * @param fund   The fund to summarise
     */
    private static void setupFundSummary(Period period, Fund fund) {
        int size = new MultiParent_Set<>(Fund_Summary.class, period, fund).get().size();
        if (size >= 2) {
            throw new RuntimeException("Multiple single objects exist");
        } else if (size == 0) {
            TrackingDatabase.get().add(new Fund_Summary(TrackingDatabase.get().getNextId(), period, fund));
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
}
