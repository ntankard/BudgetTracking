package com.ntankard.Tracking.DataBase.Core;

import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.DisplayProperties.DataContext;
import com.ntankard.ClassExtension.MemberProperties;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.ntankard.ClassExtension.DisplayProperties.*;
import static com.ntankard.ClassExtension.DisplayProperties.DataContext.*;
import static com.ntankard.ClassExtension.DisplayProperties.DataType.*;
import static com.ntankard.ClassExtension.MemberProperties.*;

public class Period {

    // My parents

    // My values
    public String id;
    public Calendar start;
    public Calendar end;

    // My Children
    private List<Statement> statements = new ArrayList<>();
    private List<CategoryTransfer> categoryTransfers = new ArrayList<>();

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

    /**
     * Private constructor
     */
    private Period(String id, Calendar start, Calendar end) {
        this.id = id;
        this.start = start;
        this.end = end;
    }

    /**
     * Notify that another object has linked to this one
     *
     * @param statement The object that linked
     */
    public void notifyStatementLink(Statement statement) {
        statements.add(statement);
    }

    /**
     * Notify that another object has linked to this one
     *
     * @param transfer The object that linked
     */
    public void notifyCategoryTransferLink(CategoryTransfer transfer) {
        categoryTransfers.add(transfer);
    }

    /**
     * Notify that another object has removed there link to this one
     *
     * @param transfer The object was linked
     */
    public void notifyCategoryTransferLinkRemove(CategoryTransfer transfer) {
        categoryTransfers.remove(transfer);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public String toString() {
        return getId();
    }

    //------------------------------------------------------------------------------------------------------------------
    //########################################## Calculated accessors ##################################################
    //------------------------------------------------------------------------------------------------------------------

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public Double getAUDMissingTransfer() {
        Double value = 0.0;
        for (Statement t : statements) {
            if (t.getIdBank().getCurrency().getId().equals("AUD")) {
                value += t.getNetTransfer();
            }
        }
        return value;
    }

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public Double getYENMissingTransfer() {
        Double value = 0.0;
        for (Statement t : statements) {
            if (t.getIdBank().getCurrency().getId().equals("YEN")) {
                value += t.getNetTransfer();
            }
        }
        return value;
    }

    public Double getTransferRate() {
        Double aud = getAUDMissingTransfer();
        Double yen = getYENMissingTransfer();
        if (aud != 0.0 && yen != 0.0) {
            return -yen / aud;
        }
        return 0.0;
    }

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public Double getStartBalance() {
        Double value = 0.0;
        for (Statement t : statements) {
            value += (t.getStart() * t.getIdBank().getCurrency().getToPrimary());
        }
        return value;
    }

    @DisplayProperties(name = "Balance", order = 2, dataType = CURRENCY_YEN)
    public Double getEndBalance() {
        Double value = 0.0;
        for (Statement t : statements) {
            value += (t.getEnd() * t.getIdBank().getCurrency().getToPrimary());
        }
        return value;
    }

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public Double getStartBalanceSecondary() {
        Double value = 0.0;
        for (Statement t : statements) {
            value += (t.getStart() * t.getIdBank().getCurrency().getToSecondary());
        }
        return value;
    }

    @DisplayProperties(name = "Balance", order = 4, dataType = CURRENCY_AUD)
    public Double getEndBalanceSecondary() {
        Double value = 0.0;
        for (Statement t : statements) {
            value += (t.getEnd() * t.getIdBank().getCurrency().getToSecondary());
        }
        return value;
    }

    @DisplayProperties(order = 3, dataType = CURRENCY_YEN, dataContext = ZERO_BELOW_BAD)
    public Double getProfit() {
        return getEndBalance() - getStartBalance();
    }

    @DisplayProperties(name = "Profit", order = 5, dataType = CURRENCY_AUD, dataContext = ZERO_BELOW_BAD)
    public Double getProfitSecondary() {
        return getEndBalanceSecondary() - getStartBalanceSecondary();
    }

    public double getCategoryTotal(Category member, boolean sumChildren) {
        double total = 0;
        for (Statement s : statements) {
            total += s.getCategoryTotal(member, sumChildren) * s.getIdBank().getCurrency().getToPrimary();
        }

        return total;
    }

    //------------------------------------------------------------------------------------------------------------------
    //########################################### Standard accessors ###################################################
    //------------------------------------------------------------------------------------------------------------------

    @DisplayProperties(order = 1, name = "Period")
    public String getId() {
        return id;
    }

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public Calendar getStart() {
        return start;
    }

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public Calendar getEnd() {
        return end;
    }

    @MemberProperties(verbosityLevel = TRACE_DISPLAY)
    public List<Statement> getStatements() {
        return statements;
    }

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public List<CategoryTransfer> getCategoryTransfers() {
        return categoryTransfers;
    }
}
