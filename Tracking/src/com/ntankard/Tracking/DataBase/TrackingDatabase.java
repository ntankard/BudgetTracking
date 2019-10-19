package com.ntankard.Tracking.DataBase;

import com.ntankard.Tracking.DataBase.Core.DataObject;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Fund;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Statement;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.*;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Bank;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Category;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Currency;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.FundEvent;
import com.ntankard.Tracking.Old.PeriodCategory;

import java.text.NumberFormat;
import java.util.*;

public class TrackingDatabase {

    // Singleton constructor
    private static TrackingDatabase master;

    public static TrackingDatabase get() {
        if (master == null) {
            master = new TrackingDatabase();
        }
        return master;
    }

    // Core Containers
    private List<Currency> currencies = new ArrayList<>();
    private List<Category> categories = new ArrayList<>();
    private List<Bank> banks = new ArrayList<>();
    private List<Period> periods = new ArrayList<>();
    private List<Statement> statements = new ArrayList<>();
    private List<Transaction> transactions = new ArrayList<>();
    private List<CategoryTransfer> categoryTransfers = new ArrayList<>();
    private List<PeriodTransfer> periodTransfers = new ArrayList<>();
    private List<Fund> funds = new ArrayList<>();
    private List<PeriodFundTransfer> periodFundTransfers = new ArrayList<>();
    private List<FundEvent> fundEvents = new ArrayList<>();
    private List<FundChargeTransfer> fundChargeTransfers = new ArrayList<>();

    // Special Containers
    private List<PeriodCategory> periodCategory = new ArrayList<>();
    private Map<Currency, NumberFormat> currencyFormat = new HashMap<>();

    // Container accessors
    private Map<String, Currency> currencyMap = new HashMap<>();
    private Map<String, Category> categoryMap = new HashMap<>();
    private Map<String, Bank> bankMap = new HashMap<>();
    private Map<String, Period> periodMap = new HashMap<>();
    private Map<Period, Map<Bank, Statement>> statementMap = new HashMap<>();
    private Map<String, CategoryTransfer> categoryTransferMap = new HashMap<>();
    private Map<String, Fund> fundMap = new HashMap<>();
    private Map<String, FundEvent> fundEventMap = new HashMap<>();

    // Flag for database construction
    private boolean isFinalized = false;

    /**
     * Private Constructor
     */
    private TrackingDatabase() {
    }

    /**
     * Repair any missing data
     */
    public void finalizeData() {
        for (Period p : periods) { // TODO this needs to be sorted based on data to work properly
            fixPeriod(p);
        }

        for (Fund fund : funds) {
            fixFundEvent(fund);
        }

        currencyFormat.put(getCurrency("AUD"), NumberFormat.getCurrencyInstance(Locale.US));
        currencyFormat.put(getCurrency("YEN"), NumberFormat.getCurrencyInstance(Locale.JAPAN));

        isFinalized = true;
    }

    /**
     * Create default for each
     */
    private void fixFundEvent(Fund funds) {
        boolean found = false;
        for (FundEvent fundEvent : funds.<FundEvent>getChildren(FundEvent.class)) {
            if (fundEvent.getIdCode().equals("NONE")) {
                found = true;
                break;
            }
        }
        if (!found) {
            addFundEvent(new FundEvent(funds, "NONE"));
        }
    }

    /**
     * Ensure that a period has all the data it should
     *
     * @param period The period to fix
     */
    public void fixPeriod(Period period) {
        Period last = getPreviousPeriod(period);
        for (Bank b : banks) {

            // Dose this period have a statement for this bank?
            Statement match = getStatement(b, period);
            if (match == null) {

                // Find the statement from the previous month if it exists
                double end = 0.0;
                if (last != null) {
                    Statement lastStatement = getStatement(last, b);
                    end = lastStatement.getEnd();
                }

                addStatement(new Statement(b, period, end, 0.0, 0.0, 0.0));
            }
        }
    }

    /**
     * Get the statement in a period for a specific bank account
     *
     * @param period The period to search
     * @param bank   The bank account to finds
     * @return The matching statement
     */
    private Statement getStatement(Period period, Bank bank) {
        for (Statement s : period.<Statement>getChildren(Statement.class)) {
            if (s.getIdBank() == bank) {
                return s;
            }
        }
        return null;
    }

    /**
     * Find the Period before this one
     *
     * @param current The current period
     * @return THe last period or null
     */
    private Period getPreviousPeriod(Period current) {
        for (Period p : periods) {
            Calendar lastEnd = p.getEnd();
            Calendar nextStart = current.getStart();

            lastEnd.add(Calendar.SECOND, +1);
            if (lastEnd.get(Calendar.YEAR) == nextStart.get(Calendar.YEAR)) {
                if (lastEnd.get(Calendar.MONTH) == nextStart.get(Calendar.MONTH)) {
                    return p;
                }
            }
            lastEnd.add(Calendar.SECOND, -1);
        }

        return null;
    }

    /**
     * Find the next available transaction code
     *
     * @param s The statement to containing the rows
     * @return The next available code
     */
    public String getNextTransactionId(Statement s) {
        int max = 0;
        for (Transaction t : s.<Transaction>getChildren(Transaction.class)) {
            int value = Integer.parseInt(t.getIdCode());
            if (value > max) {
                max = value;
            }
        }
        return (max + 1) + "";
    }

    /**
     * Find the next available categoryTransfers code
     *
     * @param core The statement to containing the rows
     * @return The next available code
     */
    public String getNextCategoryTransferId(Period core) {
        int max = 0;
        for (DataObject t : core.getChildren(CategoryTransfer.class)) {//.getCategoryTransfers()) {
            int value = Integer.parseInt(((CategoryTransfer) t).getIdCode());
            if (value > max) {
                max = value;
            }
        }
        return (max + 1) + "";
    }

    /**
     * Find the next available PeriodTransfer code
     *
     * @return The next available PeriodTransfer code
     */
    public String getNextPeriodTransferId() {
        int max = 0;
        for (PeriodTransfer t : getPeriodTransfers()) {
            int value = Integer.parseInt(t.getId());
            if (value > max) {
                max = value;
            }
        }
        return (max + 1) + "";
    }

    public String getNextPeriodFundTransferId() {
        int max = 0;
        for (PeriodFundTransfer t : getPeriodFundTransfers()) {
            int value = Integer.parseInt(t.getId());
            if (value > max) {
                max = value;
            }
        }
        return (max + 1) + "";
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################# Populate Rows ######################################################
    //------------------------------------------------------------------------------------------------------------------

    public void addCurrency(Currency currency) {
        this.currencies.add(currency);
        this.currencyMap.put(currency.getId(), currency);
        currency.notifyParentLink();
    }

    public void addCategory(Category category) {
        this.categories.add(category);
        this.categoryMap.put(category.getId(), category);
        category.notifyParentLink();
    }

    public void addBank(Bank bank) {
        this.banks.add(bank);
        this.bankMap.put(bank.getId(), bank);
        bank.notifyParentLink();
    }

    public void addPeriod(Period period) {
        this.periods.add(period);
        this.statementMap.put(period, new HashMap<>());
        this.periodMap.put(period.getId(), period);
        if (isFinalized) {
            fixPeriod(period);
        }
        this.periodCategory.add(new PeriodCategory(period, this));
        period.notifyParentLink();
    }

    public void addStatement(Statement statement) {
        this.statements.add(statement);
        this.statementMap.get(statement.getIdPeriod()).put(statement.getIdBank(), statement);
        statement.notifyParentLink();
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        transaction.notifyParentLink();
    }

    public void addCategoryTransfer(CategoryTransfer categoryTransfer) {
        this.categoryTransfers.add(categoryTransfer);
        this.categoryTransferMap.put(categoryTransfer.getId(), categoryTransfer);
        categoryTransfer.notifyParentLink();
    }

    public void addPeriodTransfer(PeriodTransfer periodTransfer) {
        this.periodTransfers.add(periodTransfer);
        periodTransfer.notifyParentLink();
    }

    public void addFund(Fund fund) {
        this.funds.add(fund);
        this.fundMap.put(fund.getId(), fund);
        fund.notifyParentLink();
    }

    public void addPeriodFundTransfer(PeriodFundTransfer periodFundTransfer) {
        this.periodFundTransfers.add(periodFundTransfer);
        periodFundTransfer.notifyParentLink();
    }

    public void addFundEvent(FundEvent fundEvent) {
        this.fundEvents.add(fundEvent);
        fundEventMap.put(fundEvent.getId(), fundEvent);
        fundEvent.notifyParentLink();
    }

    public void addFundChargeTransfer(FundChargeTransfer fundChargeTransfer) {
        this.fundChargeTransfers.add(fundChargeTransfer);
        fundChargeTransfer.notifyParentLink();
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################## Remove Rows #######################################################
    //------------------------------------------------------------------------------------------------------------------

    public void removeTransaction(Transaction transaction) {
        transaction.notifyParentUnLink();
        this.transactions.remove(transaction);
    }

    public void removeCategoryTransfer(CategoryTransfer categoryTransfer) {
        categoryTransfer.notifyParentUnLink();
        this.categoryTransfers.remove(categoryTransfer);
        this.categoryTransferMap.remove(categoryTransfer.getId());
    }

    public void removePeriodTransfer(PeriodTransfer periodTransfer) {
        periodTransfer.notifyParentUnLink();
        this.periodTransfers.remove(periodTransfer);
    }

    public void removePeriodFundTransfer(PeriodFundTransfer periodFundTransfer) {
        periodFundTransfer.notifyParentUnLink();
        this.periodFundTransfers.remove(periodFundTransfer);
    }

    public void removeFundEvent(FundEvent fundEvent) {
        fundEvent.notifyParentUnLink();
        this.periodFundTransfers.remove(fundEvent);
    }

    public void removeFundChargeTransfer(FundChargeTransfer fundChargeTransfer) {
        fundChargeTransfer.notifyParentUnLink();
        this.fundChargeTransfers.remove(fundChargeTransfer);
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################# Map accessors ######################################################
    //------------------------------------------------------------------------------------------------------------------

    public Currency getCurrency(String currencyID) {
        return currencyMap.get(currencyID);
    }

    public Category getCategory(String categoryID) {
        return categoryMap.get(categoryID);
    }

    public Bank getBank(String bankId) {
        return bankMap.get(bankId);
    }

    public Period getPeriod(String periodId) {
        return periodMap.get(periodId);
    }

    public Statement getStatement(Bank bankID, Period periodID) {
        if (statementMap.get(periodID) != null) {
            return statementMap.get(periodID).get(bankID);
        }
        return null;
    }

    public CategoryTransfer getCategoryTransfer(String categoryTransferId) {
        return categoryTransferMap.get(categoryTransferId);
    }

    public FundEvent getFundEvent(String fundEventId) {
        return fundEventMap.get(fundEventId);
    }


    public Fund getFund(String fundId) {
        return fundMap.get(fundId);
    }

    public NumberFormat getCurrencyFormat(Currency currency) {
        return currencyFormat.get(currency);
    }

    public NumberFormat getCurrencyFormat(String currency) {
        return currencyFormat.get(getCurrency(currency));
    }

    //------------------------------------------------------------------------------------------------------------------
    //########################################### Standard accessors ###################################################
    //------------------------------------------------------------------------------------------------------------------

    public List<Currency> getCurrencies() {
        return Collections.unmodifiableList(currencies);
    }

    public List<Category> getCategories() {
        return Collections.unmodifiableList(categories);
    }

    public List<Bank> getBanks() {
        return Collections.unmodifiableList(banks);
    }

    public List<Period> getPeriods() {
        return Collections.unmodifiableList(periods);
    }

    public List<Statement> getStatements() {
        return Collections.unmodifiableList(statements);
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    public List<CategoryTransfer> getCategoryTransfers() {
        return Collections.unmodifiableList(categoryTransfers);
    }

    public List<PeriodCategory> getPeriodCategory() {
        return Collections.unmodifiableList(periodCategory);
    }

    public List<PeriodTransfer> getPeriodTransfers() {
        return Collections.unmodifiableList(periodTransfers);
    }

    public List<Fund> getFunds() {
        return Collections.unmodifiableList(funds);
    }

    public List<PeriodFundTransfer> getPeriodFundTransfers() {
        return Collections.unmodifiableList(periodFundTransfers);
    }

    public List<FundEvent> getFundEvents() {
        return Collections.unmodifiableList(fundEvents);
    }

    public List<FundChargeTransfer> getFundChargeTransfers() {
        return Collections.unmodifiableList(fundChargeTransfers);
    }
}
