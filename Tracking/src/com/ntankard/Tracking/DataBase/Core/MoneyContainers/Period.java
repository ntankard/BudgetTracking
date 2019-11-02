package com.ntankard.Tracking.DataBase.Core.MoneyContainers;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.DataObject;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Currency;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.DisplayProperties.DataContext.ZERO_BELOW_BAD;
import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY_YEN;
import static com.ntankard.ClassExtension.MemberProperties.DEBUG_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class Period extends DataObject {

    // My parents
    private Period last;

    // My values
    private int month;
    private int year;

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getMonth", "getYear", "getLast"})
    public Period(String id, int month, int year, Period last) {
        super(id);
        this.month = month;
        this.year = year;
        this.last = last;
    }

    /**
     * Generate a new period that comes after this one
     *
     * @return A new period that comes after this one
     */
    public Period generateNext() {
        int nextMonth = month;
        int nextYear = year;
        nextMonth++;
        if (nextMonth > 12) {
            nextMonth -= 12;
            nextYear++;
        }

        return new Period(TrackingDatabase.get().getNextId(Period.class), nextMonth, nextYear, this);
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
    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 21)
    public List<DataObject> getParents() {
        return new ArrayList<>();
    }

    //------------------------------------------------------------------------------------------------------------------
    //###################################### Calculated accessors, from children #######################################
    //------------------------------------------------------------------------------------------------------------------

    // Inter currency transfers ----------------------------------------------------------------------------------------

    public Double getMissingTransfer(Currency currency) {
        Double value = 0.0;
        for (Statement t : this.getChildren(Statement.class)) {
            if (t.getBank().getCurrency().equals(currency)) {
                value += t.getNetTransfer();
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
        for (Statement t : this.getChildren(Statement.class)) {
            value += (t.getStart() * t.getBank().getCurrency().getToPrimary());
        }
        return value;
    }

    @DisplayProperties(order = 5, dataType = CURRENCY_YEN)
    public Double getEndBalance() {
        double value = 0.0;
        for (Statement t : this.getChildren(Statement.class)) {
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
        for (Statement t : this.getChildren(Statement.class)) {
            if (t.getBank().getCurrency().equals(currency)) {
                value += (t.getEnd() * t.getBank().getCurrency().getToPrimary());
            }
        }
        return value;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @DisplayProperties(order = 2)
    public int getMonth() {
        return month;
    }

    @DisplayProperties(order = 3)
    public int getYear() {
        return year;
    }

    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
    @DisplayProperties(order = 8)
    public Period getLast() {
        return last;
    }
}
