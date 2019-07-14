package com.ntankard.Tracking.DataBase.Core;

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
     * @param month The month (1-12)
     * @param year The year
     * @return The Period for the month
     */
    public static Period Month (int month, int year){
        Calendar start = Calendar.getInstance();
        start.clear();
        start.set(Calendar.YEAR,year);
        start.set(Calendar.MONTH,month-1);
        start.set(Calendar.DAY_OF_MONTH, 1);

        Calendar end = Calendar.getInstance();
        end.clear();
        end.set(Calendar.YEAR,year);
        end.set(Calendar.MONTH,month);
        end.set(Calendar.DAY_OF_MONTH, 1);
        end.add(Calendar.SECOND,-1);

        if(start.get(Calendar.YEAR) != end.get(Calendar.YEAR)){
            throw new RuntimeException("Year mismatch");
        }

        if(start.get(Calendar.MONTH) != end.get(Calendar.MONTH)){
            throw new RuntimeException("Month mismatch");
        }

        SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");
        return new Period(monthFormat.format(start.getTime()),start,end);
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
     * @param statement The object that linked
     */
    public void notifyStatementLink(Statement statement){
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
    //########################################### Standard accessors ###################################################
    //------------------------------------------------------------------------------------------------------------------

    public String getId(){
        return id;
    }
    public Calendar getStart() {
        return start;
    }
    public Calendar getEnd() {
        return end;
    }

    public List<Statement> getStatements() {
        return statements;
    }
}