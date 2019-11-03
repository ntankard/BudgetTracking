package com.ntankard.Tracking.DataBase.Interface.ClassExtension;

import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Statement;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Currency;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;

import static com.ntankard.ClassExtension.DisplayProperties.DataContext.ZERO_BELOW_BAD;
import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY_YEN;
import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

public class ExtendedPeriod {

    /**
     * The core period object used for this ones calculations
     */
    private Period period;

    /**
     * Constructor
     */
    public ExtendedPeriod(Period period) {
        this.period = period;
    }

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 1)
    public Period getPeriod() {
        return period;
    }

    //------------------------------------------------------------------------------------------------------------------
    //###################################### Calculated accessors, from children #######################################
    //------------------------------------------------------------------------------------------------------------------

    // Inter currency transfers ----------------------------------------------------------------------------------------

    public Double getMissingTransfer(Currency currency) {
        Double value = 0.0;
        for (Statement t : period.getChildren(Statement.class)) {
            if (t.getBank().getCurrency().equals(currency)) {
                value += new ExtendedStatement(t).getNetTransfer();
            }
        }

        return Currency.round(value);
    }

    @DisplayProperties(order = 7)
    public Double getTransferRate() {
        int size = TrackingDatabase.get().get(Currency.class).size();
        if (size == 1) {
            return 0.0;
        } else if (size == 2) {
            double primarySum = 0.0;
            double otherSum = 0.0;
            for (Currency currency : TrackingDatabase.get().get(Currency.class)) {
                if (currency.isDefault()) {
                    primarySum = getMissingTransfer(currency);
                } else {
                    otherSum = getMissingTransfer(currency);
                }
            }
            double value = primarySum / otherSum;

            return -Currency.round(value);
        } else {
            throw new RuntimeException("3+ currencies not supported");
        }
    }

    // Period totals ---------------------------------------------------------------------------------------------------

    @DisplayProperties(order = 4, dataType = CURRENCY_YEN)
    public Double getStartBalance() {
        double value = 0.0;
        for (Statement t : period.getChildren(Statement.class)) {
            value += (t.getStart() * t.getBank().getCurrency().getToPrimary());
        }
        return value;
    }

    @DisplayProperties(order = 5, dataType = CURRENCY_YEN)
    public Double getEndBalance() {
        double value = 0.0;
        for (Statement t : period.getChildren(Statement.class)) {
            value += (t.getEnd() * t.getBank().getCurrency().getToPrimary());
        }
        return value;
    }

    @DisplayProperties(order = 6, dataType = CURRENCY_YEN, dataContext = ZERO_BELOW_BAD)
    public Double getProfit() {
        return getEndBalance() - getStartBalance();
    }

    public Double getEndBalance(Currency currency) {
        double value = 0.0;
        for (Statement t : period.getChildren(Statement.class)) {
            if (t.getBank().getCurrency().equals(currency)) {
                value += (t.getEnd() * t.getBank().getCurrency().getToPrimary());
            }
        }
        return value;
    }
}
