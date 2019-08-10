package com.ntankard.Tracking.DataBase.Core;

import com.ntankard.ClassExtension.MemberProperties;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Period {

    // My parents

    // My values
    public String id;
    public Calendar start;
    public Calendar end;

    // My Children
    private List<Statement> statements = new ArrayList<>();

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
     * {@inheritDoc
     */
    @Override
    public String toString() {
        return getId();
    }

    //------------------------------------------------------------------------------------------------------------------
    //########################################## Calculated accessors ##################################################
    //------------------------------------------------------------------------------------------------------------------

    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
    public Double getAUDMissingTransfer() {
        Double value = 0.0;
        for (Statement t : statements) {
            if (t.getIdBank().getCurrency().getId().equals("AUD")) {
                value += t.getNetTransfer();
            }
        }
        return value;
    }

    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
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

    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
    public Double getAudStart() {
        Double value = 0.0;
        for (Statement t : statements) {
            value += (t.getStart() * t.getIdBank().getCurrency().getToAUD());
        }
        return value;
    }

    public Double getAudBalance() {
        Double value = 0.0;
        for (Statement t : statements) {
            value += (t.getEnd() * t.getIdBank().getCurrency().getToAUD());
        }
        return value;
    }

    public Double getProfit() {
        return getAudBalance() - getAudStart();
    }

    //------------------------------------------------------------------------------------------------------------------
    //########################################### Standard accessors ###################################################
    //------------------------------------------------------------------------------------------------------------------

    public String getId() {
        return id;
    }

    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
    public Calendar getStart() {
        return start;
    }

    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
    public Calendar getEnd() {
        return end;
    }

    @MemberProperties(verbosityLevel = MemberProperties.TRACE_DISPLAY)
    public List<Statement> getStatements() {
        return statements;
    }
}
