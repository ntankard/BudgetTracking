package com.ntankard.Tracking.DataBase.Core.MoneyContainers;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.DataObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class Period extends MoneyContainer {

    /**
     * Build a period over an entire month
     *
     * @param month The month (1-12)
     * @param year  The year
     * @return The Period for the month
     */
    public static Period Month(int month, int year) {
        Calendar start = Calendar.getInstance();
        start.clear();
        start.set(Calendar.YEAR, year);
        start.set(Calendar.MONTH, month - 1);
        start.set(Calendar.DAY_OF_MONTH, 1);

        Calendar end = Calendar.getInstance();
        end.clear();
        end.set(Calendar.YEAR, year);
        end.set(Calendar.MONTH, month);
        end.set(Calendar.DAY_OF_MONTH, 1);
        end.add(Calendar.SECOND, -1);

        if (start.get(Calendar.YEAR) != end.get(Calendar.YEAR)) {
            throw new RuntimeException("Year mismatch");
        }

        if (start.get(Calendar.MONTH) != end.get(Calendar.MONTH)) {
            throw new RuntimeException("Month mismatch");
        }

        SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");
        return new Period(monthFormat.format(start.getTime()), start, end);
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################## Core Object ###################################################
    //------------------------------------------------------------------------------------------------------------------

    // My values
    private String id;
    private Calendar start;
    private Calendar end;

    /**
     * Private constructor
     */
    private Period(String id, Calendar start, Calendar end) {
        this.id = id;
        this.start = start;
        this.end = end;
    }

    /**
     * Get the start time of the next period
     *
     * @return The next period
     */
    public Calendar getNextPeriodTime() {
        Calendar toReturn = (Calendar) end.clone();
        toReturn.add(Calendar.SECOND, 1);
        return toReturn;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @DisplayProperties(order = 1, name = "Period")
    public String getId() {
        return id;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public List<DataObject> getParents() {
        return new ArrayList<>();
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public Calendar getStart() {
        return start;
    }

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public Calendar getEnd() {
        return end;
    }
}


//    // Inter currency transfers ----------------------------------------------------------------------------------------
//
//    @MemberProperties(verbosityLevel = INFO_DISPLAY)
//    public Double getAUDMissingTransfer() {
//        Double value = 0.0;
//        for (Statement t : this.<Statement>getChildren(Statement.class)) {
//            if (t.getIdBank().getCurrency().getId().equals("AUD")) {
//                value += t.getNetTransfer();
//            }
//        }
//        return value;
//    }
//
//    @MemberProperties(verbosityLevel = INFO_DISPLAY)
//    public Double getYENMissingTransfer() {
//        Double value = 0.0;
//        for (Statement t : this.<Statement>getChildren(Statement.class)) {
//            if (t.getIdBank().getCurrency().getId().equals("YEN")) {
//                value += t.getNetTransfer();
//            }
//        }
//        return value;
//    }
//
//    public Double getTransferRate() {
//        Double aud = getAUDMissingTransfer();
//        Double yen = getYENMissingTransfer();
//        if (aud != 0.0 && yen != 0.0) {
//            return -yen / aud;
//        }
//        return 0.0;
//    }
//
//    // Period totals ---------------------------------------------------------------------------------------------------
//
//    @MemberProperties(verbosityLevel = INFO_DISPLAY)
//    public Double getStartBalance() {
//        Double value = 0.0;
//        for (Statement t : this.<Statement>getChildren(Statement.class)) {
//            value += (t.getStart() * t.getIdBank().getCurrency().getToPrimary());
//        }
//        return value;
//    }
//
//    @DisplayProperties(name = "Balance", order = 2, dataType = CURRENCY_YEN)
//    public Double getEndBalance() {
//        Double value = 0.0;
//        for (Statement t : this.<Statement>getChildren(Statement.class)) {
//            value += (t.getEnd() * t.getIdBank().getCurrency().getToPrimary());
//        }
//        return value;
//    }
//
//    @MemberProperties(verbosityLevel = INFO_DISPLAY)
//    public Double getStartBalanceSecondary() {
//        double value = 0.0;
//        for (Statement t : this.<Statement>getChildren(Statement.class)) {
//            value += (t.getStart() * t.getIdBank().getCurrency().getToSecondary());
//        }
//        return value;
//    }
//
//    @DisplayProperties(name = "Balance", order = 4, dataType = CURRENCY_AUD)
//    public Double getEndBalanceSecondary() {
//        double value = 0.0;
//        for (Statement t : this.<Statement>getChildren(Statement.class)) {
//            value += (t.getEnd() * t.getIdBank().getCurrency().getToSecondary());
//        }
//        return value;
//    }
//
//    @DisplayProperties(order = 3, dataType = CURRENCY_YEN, dataContext = ZERO_BELOW_BAD)
//    public Double getProfit() {
//        return getEndBalance() - getStartBalance();
//    }
//
//    @DisplayProperties(name = "Profit", order = 5, dataType = CURRENCY_AUD, dataContext = ZERO_BELOW_BAD)
//    public Double getProfitSecondary() {
//        return getEndBalanceSecondary() - getStartBalanceSecondary();
//    }
//
//    // Filtered lists --------------------------------------------------------------------------------------------------
//
//    public double getCategoryTotal(Category member) {
//        double total = 0;
//        for (Statement s : this.<Statement>getChildren(Statement.class)) {
//            total += s.getCategoryTotal(member) * s.getIdBank().getCurrency().getToPrimary();
//        }
//
//        return total;
//    }
