package com.ntankard.Tracking.DataBase.Core;

import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.Transfers.CategoryTransfer;
import com.ntankard.Tracking.DataBase.Interface.Period_SummaryCategoryTransfer;
import com.ntankard.Tracking.DataBase.Interface.Period_SummaryTransaction;
import com.ntankard.Tracking.DataBase.TrackingDatabase;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.ntankard.ClassExtension.DisplayProperties.DataContext.ZERO_BELOW_BAD;
import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY_AUD;
import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY_YEN;
import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;
import static com.ntankard.ClassExtension.MemberProperties.TRACE_DISPLAY;

public class Period {

    /**
     * Build a period over an entire month
     *
     * @param month    The month (1-12)
     * @param year     The year
     * @param database The main database, used to get categories
     * @return The Period for the month
     */
    public static Period Month(int month, int year, TrackingDatabase database) {
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
        return new Period(monthFormat.format(start.getTime()), start, end, database);
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################## Core Object ###################################################
    //------------------------------------------------------------------------------------------------------------------

    // My parents

    // My values
    private String id;
    private Calendar start;
    private Calendar end;

    // My Children
    private List<Statement> statements = new ArrayList<>();
    private List<CategoryTransfer> categoryTransfers = new ArrayList<>();

    // Special Access
    private Map<Category, Period_SummaryTransaction> transactionSummaries = new HashMap<>();
    private Map<Category, Period_SummaryCategoryTransfer> categoryTransferSummaries = new HashMap<>();

    /**
     * Private constructor
     */
    private Period(String id, Calendar start, Calendar end, TrackingDatabase database) {
        this.id = id;
        this.start = start;
        this.end = end;

        for (Category category : database.getCategories()) {
            transactionSummaries.put(category, new Period_SummaryTransaction(this, category, statements));
            categoryTransferSummaries.put(category, new Period_SummaryCategoryTransfer(this, category, categoryTransfers));
        }
    }

    /**
     * {@inheritDoc
     */
    @Override
    public String toString() {
        return getId();
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################# Calculated accessors ###############################################
    //------------------------------------------------------------------------------------------------------------------

    // Inter currency transfers ----------------------------------------------------------------------------------------

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

    // Period totals ---------------------------------------------------------------------------------------------------

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
        double value = 0.0;
        for (Statement t : statements) {
            value += (t.getStart() * t.getIdBank().getCurrency().getToSecondary());
        }
        return value;
    }

    @DisplayProperties(name = "Balance", order = 4, dataType = CURRENCY_AUD)
    public Double getEndBalanceSecondary() {
        double value = 0.0;
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

    // Filtered lists --------------------------------------------------------------------------------------------------

    public double getCategoryTotal(Category member) {
        double total = 0;
        for (Statement s : statements) {
            total += s.getCategoryTotal(member) * s.getIdBank().getCurrency().getToPrimary();
        }

        return total;
    }

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public List<Transaction> getTransactions() {
        List<Transaction> toReturn = new ArrayList<>();
        for (Statement statement : statements) {
            toReturn.addAll(statement.getTransactions());
        }
        return toReturn;
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################# Link Management ################################################
    //------------------------------------------------------------------------------------------------------------------

    // Statement Link --------------------------------------------------------------------------------------------------

    /**
     * Notify that a Statement has linked to this Period
     *
     * @param added The Statement that linked
     */
    public void notifyStatementLink(Statement added) {
        statements.add(added);
    }

    /**
     * Notify that a Statement has removed there link to this Period
     *
     * @param removed The Statement that was linked
     */
    public void notifyStatementLinkRemove(Statement removed) {
        statements.remove(removed);
    }

    /**
     * Get all the Statements that have linked to this Period
     *
     * @return All the Statements that have linked to this Period
     */
    @MemberProperties(verbosityLevel = TRACE_DISPLAY)
    public List<Statement> getStatements() {
        return statements;
    }

    // CategoryTransfer Link -------------------------------------------------------------------------------------------

    /**
     * Notify that a CategoryTransfer has linked to this Period
     *
     * @param added The CategoryTransfer that linked
     */
    public void notifyCategoryTransferLink(CategoryTransfer added) {
        categoryTransfers.add(added);
    }

    /**
     * Notify that a CategoryTransfer has removed there link to this Period
     *
     * @param removed The CategoryTransfer that was linked
     */
    public void notifyCategoryTransferLinkRemove(CategoryTransfer removed) {
        categoryTransfers.remove(removed);
    }

    /**
     * Get all the CategoryTransfers that have linked to this Period
     *
     * @return All the CategoryTransfers that have linked to this Period
     */
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public List<CategoryTransfer> getCategoryTransfers() {
        return categoryTransfers;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
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

    public Map<Category, Period_SummaryTransaction> getTransactionSummaries() {
        return transactionSummaries;
    }

    public Map<Category, Period_SummaryCategoryTransfer> getCategoryTransferSummaries() {
        return categoryTransferSummaries;
    }
}
