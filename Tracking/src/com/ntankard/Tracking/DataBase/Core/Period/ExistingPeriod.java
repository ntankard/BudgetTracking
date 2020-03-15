package com.ntankard.Tracking.DataBase.Core.Period;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Field;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Core.RecurringPayment.FixedRecurringPayment;
import com.ntankard.Tracking.DataBase.Core.StatementEnd;
import com.ntankard.Tracking.DataBase.Core.Transfer.Bank.RecurringBankTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfer.Fund.RePayFundTransfer;
import com.ntankard.Tracking.DataBase.Database.ObjectFactory;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Set.TwoParent_Children_Set;

import java.util.List;

@ClassExtensionProperties(includeParent = true)
@ObjectFactory(builtObjects = {StatementEnd.class, RePayFundTransfer.class, RecurringBankTransfer.class})
public class ExistingPeriod extends Period {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static List<Field<?>> getFields(Integer id, Integer month, Integer year, DataObject container) {
        List<Field<?>> toReturn = Period.getFields(id, container);
        toReturn.add(new Field<>("month", Integer.class, month, container));
        toReturn.add(new Field<>("year", Integer.class, year, container));
        return toReturn;
    }

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getMonth", "getYear"})
    public ExistingPeriod(Integer id, Integer month, Integer year) {
        super();
        setFields(getFields(id, month, year, this));

    }

    /**
     * {@inheritDoc
     */
    @Override
    public void add() {
        super.add();

        for (FundEvent fundEvent : TrackingDatabase.get().get(FundEvent.class)) {
            if (new TwoParent_Children_Set<>(RePayFundTransfer.class, fundEvent, this).get().size() != 0) {
                throw new RuntimeException("Repay exists before it should");
            }

            if (fundEvent.isChargeThisPeriod(this)) {
                new RePayFundTransfer(TrackingDatabase.get().getNextId(), this, fundEvent, TrackingDatabase.get().getDefault(Currency.class)).add();
            }
        }

        for (Bank bank : TrackingDatabase.get().get(Bank.class)) {
            if (new TwoParent_Children_Set<>(StatementEnd.class, bank, this).get().size() > 1) {
                throw new RuntimeException("More than 1 statement end");
            }
            if (new TwoParent_Children_Set<>(StatementEnd.class, bank, this).get().size() == 0) {
                new StatementEnd(TrackingDatabase.get().getNextId(), this, bank, 0.0).add();
            }
        }

        for (FixedRecurringPayment fixedRecurringPayment : TrackingDatabase.get().get(FixedRecurringPayment.class)) {
            fixedRecurringPayment.regenerateChildren();
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Speciality ###################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Generate a new period that comes after this one
     *
     * @return A new period that comes after this one
     */
    public ExistingPeriod generateNext() {
        int nextMonth = getMonth();
        int nextYear = getYear();
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
        return getYear() + "-" + getMonth();
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    // 1000000--getID

    @DisplayProperties(order = 1010000)
    public Integer getMonth() {
        return get("month");
    }

    @DisplayProperties(order = 1020000)
    public Integer getYear() {
        return get("year");
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
