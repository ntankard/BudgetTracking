package com.ntankard.Tracking.DataBase.Core.MoneyContainers;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.DataObject;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.CategoryTransfer;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.NonPeriodFundTransfer;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.PeriodTransfer;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.Transaction;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Category;
import com.ntankard.Tracking.DataBase.Interface.Period_SummaryTransfer;
import com.ntankard.Tracking.DataBase.TrackingDatabase;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.ntankard.ClassExtension.DisplayProperties.DataContext.ZERO_BELOW_BAD;
import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY_AUD;
import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY_YEN;
import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class Period extends MoneyContainer {

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

    // Special Access
    private Map<Class, Map<Category, Period_SummaryTransfer>> transferSummaries = new HashMap<>();

    // Master database
    private TrackingDatabase trackingDatabase;

    /**
     * Private constructor
     */
    private Period(String id, Calendar start, Calendar end, TrackingDatabase trackingDatabase) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.trackingDatabase = trackingDatabase;

        updateSummaries();
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void notifyChildLink(DataObject linkObject) {
        super.notifyChildLink(linkObject);
        updateSummaries();
    }

    /**
     * Update the summary map
     */
    private void updateSummaries() {
        transferSummaries.clear();

        transferSummaries.put(CategoryTransfer.class, new HashMap<>());
        transferSummaries.put(PeriodTransfer.class, new HashMap<>());
        transferSummaries.put(NonPeriodFundTransfer.class, new HashMap<>());
        transferSummaries.put(Transaction.class, new HashMap<>());

        for (Category category : trackingDatabase.getCategories()) {
            transferSummaries.get(CategoryTransfer.class).put(category, new Period_SummaryTransfer<>(this, category, getChildren(CategoryTransfer.class)));
            transferSummaries.get(PeriodTransfer.class).put(category, new Period_SummaryTransfer<>(this, category, getChildren(PeriodTransfer.class)));
            transferSummaries.get(NonPeriodFundTransfer.class).put(category, new Period_SummaryTransfer<>(this, category, getChildren(NonPeriodFundTransfer.class)));
            transferSummaries.get(Transaction.class).put(category, new Period_SummaryTransfer<>(this, category, getChildren(Transaction.class)));
        }
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
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public List<DataObject> getParents() {
        return new ArrayList<>();
    }

    /**
     * {@inheritDoc
     */
    @Override
    @DisplayProperties(order = 1, name = "Period")
    public String getId() {
        return id;
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################# Calculated accessors ###############################################
    //------------------------------------------------------------------------------------------------------------------

    // Inter currency transfers ----------------------------------------------------------------------------------------

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public Double getAUDMissingTransfer() {
        Double value = 0.0;
        for (Statement t : getStatements()) {
            if (t.getIdBank().getCurrency().getId().equals("AUD")) {
                value += t.getNetTransfer();
            }
        }
        return value;
    }

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public Double getYENMissingTransfer() {
        Double value = 0.0;
        for (Statement t : getStatements()) {
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
        for (Statement t : getStatements()) {
            value += (t.getStart() * t.getIdBank().getCurrency().getToPrimary());
        }
        return value;
    }

    @DisplayProperties(name = "Balance", order = 2, dataType = CURRENCY_YEN)
    public Double getEndBalance() {
        Double value = 0.0;
        for (Statement t : getStatements()) {
            value += (t.getEnd() * t.getIdBank().getCurrency().getToPrimary());
        }
        return value;
    }

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public Double getStartBalanceSecondary() {
        double value = 0.0;
        for (Statement t : getStatements()) {
            value += (t.getStart() * t.getIdBank().getCurrency().getToSecondary());
        }
        return value;
    }

    @DisplayProperties(name = "Balance", order = 4, dataType = CURRENCY_AUD)
    public Double getEndBalanceSecondary() {
        double value = 0.0;
        for (Statement t : getStatements()) {
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
        for (Statement s : getStatements()) {
            total += s.getCategoryTotal(member) * s.getIdBank().getCurrency().getToPrimary();
        }

        return total;
    }

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public List<Transaction> getTransactions() {
        List<Transaction> toReturn = new ArrayList<>();
        for (DataObject statement : getChildren(Statement.class)) {
            toReturn.addAll(((Statement) statement).getTransactions());
        }
        return toReturn;
    }

    public List<Statement> getStatements() {
        List<Statement> toReturn = new ArrayList<>();

        toReturn.addAll(getChildren(Statement.class));

        return toReturn;
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################# Link Management ################################################
    //------------------------------------------------------------------------------------------------------------------

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

    public Map<Category, Period_SummaryTransfer> getTransactionSummaries() {
        return transferSummaries.get(Transaction.class);
    }

    public Map<Category, Period_SummaryTransfer> getCategoryTransferSummaries() {
        return transferSummaries.get(CategoryTransfer.class);
    }

    public Map<Category, Period_SummaryTransfer> getPeriodTransferSummaries() {
        return transferSummaries.get(PeriodTransfer.class);
    }

    public Map<Category, Period_SummaryTransfer> getNonPeriodFundTransferSummaries() {
        return transferSummaries.get(NonPeriodFundTransfer.class);
    }

    public Period_SummaryTransfer getTransferSummary(Class transferType, Category category) {
        return transferSummaries.get(transferType).get(category);
    }
}
