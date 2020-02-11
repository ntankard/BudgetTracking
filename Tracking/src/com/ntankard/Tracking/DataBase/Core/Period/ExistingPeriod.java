package com.ntankard.Tracking.DataBase.Core.Period;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank.StatementEnd;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Core.Transfers.CategoryFundTransfer.RePayCategoryFundTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfers.BankCategoryTransfer.RecurringPayment.Fixed.FixedRecurringPayment;
import com.ntankard.Tracking.DataBase.Core.Transfers.BankCategoryTransfer.RecurringPayment.Fixed.FixedRecurringTransfer;
import com.ntankard.Tracking.DataBase.Database.ObjectFactory;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Set.MultiParent_Set;

@ClassExtensionProperties(includeParent = true)
@ObjectFactory(builtObjects = {StatementEnd.class, RePayCategoryFundTransfer.class, FixedRecurringTransfer.class})
public class ExistingPeriod extends Period {

    // My values
    private Integer month;
    private Integer year;

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getMonth", "getYear"})
    public ExistingPeriod(Integer id, Integer month, Integer year) {
        super(id);
        if (month == null) throw new IllegalArgumentException("Month is null");
        if (year == null) throw new IllegalArgumentException("Yesr is null");
        this.month = month;
        this.year = year;
    }

    /**
     * Generate a new period that comes after this one
     *
     * @return A new period that comes after this one
     */
    public ExistingPeriod generateNext() {
        int nextMonth = month;
        int nextYear = year;
        nextMonth++;
        if (nextMonth > 12) {
            nextMonth -= 12;
            nextYear++;
        }

        return new ExistingPeriod(TrackingDatabase.get().getNextId(), nextMonth, nextYear);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public String toString() {
        return year + "-" + month;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void add() {
        super.add();

        for (FundEvent fundEvent : TrackingDatabase.get().get(FundEvent.class)) {
            if (new MultiParent_Set<>(RePayCategoryFundTransfer.class, fundEvent, this).get().size() != 0) {
                throw new RuntimeException("Repay exists before it should");
            }

            if (fundEvent.isChargeThisPeriod(this)) {
                new RePayCategoryFundTransfer(TrackingDatabase.get().getNextId(), this, fundEvent, TrackingDatabase.get().getDefault(Currency.class)).add();
            }
        }

        for (Bank bank : TrackingDatabase.get().get(Bank.class)) {
            if (new MultiParent_Set<>(StatementEnd.class, bank, this).get().size() > 1) {
                throw new RuntimeException("More than 1 statement end");
            }
            if (new MultiParent_Set<>(StatementEnd.class, bank, this).get().size() == 0) {
                new StatementEnd(TrackingDatabase.get().getNextId(), this, bank, 0.0).add();
            }
        }

        for (FixedRecurringPayment fixedRecurringPayment : TrackingDatabase.get().get(FixedRecurringPayment.class)) {
            fixedRecurringPayment.regenerateChildren();
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    // 1000000--getID

    @DisplayProperties(order = 1010000)
    public Integer getMonth() {
        return month;
    }

    @DisplayProperties(order = 1020000)
    public Integer getYear() {
        return year;
    }

    @Override
    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
    @DisplayProperties(order = 1030000)
    public Integer getOrder() {
        return getYear() * 12 + getMonth();
    }

    // 2000000--getParents (Above)
    // 3000000--getChildren
}
