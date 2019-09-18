package com.ntankard.Tracking.DataBase;

import com.ntankard.Tracking.DataBase.Core.*;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Transfers.CategoryTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfers.NonPeriodFundTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfers.PeriodTransfer;
import com.ntankard.Tracking.DataBase.Interface.PeriodCategory;

import java.text.NumberFormat;
import java.util.*;

public class TrackingDatabase {

    // Core Containers
    private List<Currency> currencies = new ArrayList<>();
    private List<Category> categories = new ArrayList<>();
    private List<Bank> banks = new ArrayList<>();
    private List<Period> periods = new ArrayList<>();
    private List<Statement> statements = new ArrayList<>();
    private List<Transaction> transactions = new ArrayList<>();
    private List<CategoryTransfer> categoryTransfers = new ArrayList<>();
    private List<PeriodTransfer> periodTransfers = new ArrayList<>();
    private List<NonPeriodFund> nonPeriodFunds = new ArrayList<>();
    private List<NonPeriodFundTransfer> nonPeriodFundTransfers = new ArrayList<>();

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
    private Map<String, NonPeriodFund> nonPeriodFundMap = new HashMap<>();

    private boolean isFinalized = false;

    /**
     * Repair any missing data
     */
    public void finalizeData() {
        for (Period p : periods) { // TODO this needs to be sorted based on data to work properly
            fixPeriod(p);
        }

        currencyFormat.put(getCurrency("AUD"), NumberFormat.getCurrencyInstance(Locale.US));
        currencyFormat.put(getCurrency("YEN"), NumberFormat.getCurrencyInstance(Locale.JAPAN));

        isFinalized = true;
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
        for (Statement s : period.getStatements()) {
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
        for (Transaction t : s.getTransactions()) {
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
        for (CategoryTransfer t : core.getCategoryTransfers()) {
            int value = Integer.parseInt(t.getIdCode());
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

    public String getNextNonPeriodFundTransferId() {
        int max = 0;
        for (NonPeriodFundTransfer t : getNonPeriodFundTransfers()) {
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
    }

    public void addCategory(Category category) {
        this.categories.add(category);
        this.categoryMap.put(category.getId(), category);
    }

    public void addBank(Bank bank) {
        this.banks.add(bank);
        this.bankMap.put(bank.getId(), bank);
        bank.getCurrency().notifyBankLink(bank);
    }

    public void addPeriod(Period period) {
        this.periods.add(period);
        this.statementMap.put(period, new HashMap<>());
        this.periodMap.put(period.getId(), period);
        if (isFinalized) {
            fixPeriod(period);
        }
        this.periodCategory.add(new PeriodCategory(period, this));
    }

    public void addStatement(Statement statement) {
        this.statements.add(statement);
        this.statementMap.get(statement.getIdPeriod()).put(statement.getIdBank(), statement);

        statement.getIdBank().notifyStatementLinkRemove(statement);
        statement.getIdPeriod().notifyStatementLinkRemove(statement);

        statement.getIdBank().notifyStatementLink(statement);
        statement.getIdPeriod().notifyStatementLink(statement);
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);

        transaction.getIdStatement().notifyTransactionLinkRemove(transaction);
        transaction.getCategory().notifyTransactionLinkRemove(transaction);

        transaction.getIdStatement().notifyTransactionLink(transaction);
        transaction.getCategory().notifyTransactionLink(transaction);
    }

    public void addCategoryTransfer(CategoryTransfer categoryTransfer) {
        this.categoryTransfers.add(categoryTransfer);
        this.categoryTransferMap.put(categoryTransfer.getId(), categoryTransfer);

        categoryTransfer.getIdPeriod().notifyCategoryTransferLinkRemove(categoryTransfer);
        categoryTransfer.getDestination().notifyCategoriesTransferDestinationLinkRemove(categoryTransfer);
        categoryTransfer.getSource().notifyCategoriesTransferSourceLinkRemove(categoryTransfer);
        categoryTransfer.getCurrency().notifyCategoryTransferLinkRemove(categoryTransfer);

        categoryTransfer.getIdPeriod().notifyCategoryTransferLink(categoryTransfer);
        categoryTransfer.getDestination().notifyCategoriesTransferDestinationLink(categoryTransfer);
        categoryTransfer.getSource().notifyCategoriesTransferSourceLink(categoryTransfer);
        categoryTransfer.getCurrency().notifyCategoryTransferLink(categoryTransfer);
    }

    public void addPeriodTransfer(PeriodTransfer periodTransfer) {
        this.periodTransfers.add(periodTransfer);

        periodTransfer.getCategory().notifyPeriodTransferLinkRemove(periodTransfer);
        periodTransfer.getCurrency().notifyPeriodTransferLinkRemove(periodTransfer);
        periodTransfer.getDestination().notifyPeriodTransferDestinationLinkRemove(periodTransfer);
        periodTransfer.getSource().notifyPeriodTransferSourceLinkRemove(periodTransfer);

        periodTransfer.getCategory().notifyPeriodTransferLink(periodTransfer);
        periodTransfer.getCurrency().notifyPeriodTransferLink(periodTransfer);
        periodTransfer.getDestination().notifyPeriodTransferDestinationLink(periodTransfer);
        periodTransfer.getSource().notifyPeriodTransferSourceLink(periodTransfer);
    }

    public void addNonPeriodFund(NonPeriodFund nonPeriodFund) {
        this.nonPeriodFunds.add(nonPeriodFund);
        this.nonPeriodFundMap.put(nonPeriodFund.getId(), nonPeriodFund);
    }

    public void addNonPeriodFundTransfer(NonPeriodFundTransfer nonPeriodFundTransfer) {
        this.nonPeriodFundTransfers.add(nonPeriodFundTransfer);

        nonPeriodFundTransfer.getCurrency().notifyNonPeriodFundTransferLinkRemove(nonPeriodFundTransfer);
        nonPeriodFundTransfer.getDestination().notifyNonPeriodFundTransferLinkRemove(nonPeriodFundTransfer);
        nonPeriodFundTransfer.getSource().notifyNonPeriodFundTransferLinkRemove(nonPeriodFundTransfer);
        nonPeriodFundTransfer.getSourceCategory().notifyNonPeriodFundTransferLinkRemove(nonPeriodFundTransfer);

        nonPeriodFundTransfer.getCurrency().notifyNonPeriodFundTransferLink(nonPeriodFundTransfer);
        nonPeriodFundTransfer.getDestination().notifyNonPeriodFundTransferLink(nonPeriodFundTransfer);
        nonPeriodFundTransfer.getSource().notifyNonPeriodFundTransferLink(nonPeriodFundTransfer);
        nonPeriodFundTransfer.getSourceCategory().notifyNonPeriodFundTransferLink(nonPeriodFundTransfer);
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################## Remove Rows #######################################################
    //------------------------------------------------------------------------------------------------------------------

    public void removeTransaction(Transaction transaction) {
        transaction.getIdStatement().notifyTransactionLinkRemove(transaction);
        transaction.getCategory().notifyTransactionLinkRemove(transaction);
        this.transactions.remove(transaction);
    }

    public void removeCategoryTransfer(CategoryTransfer categoryTransfer) {
        categoryTransfer.getDestination().notifyCategoriesTransferDestinationLinkRemove(categoryTransfer);
        categoryTransfer.getSource().notifyCategoriesTransferSourceLinkRemove(categoryTransfer);
        categoryTransfer.getIdPeriod().notifyCategoryTransferLinkRemove(categoryTransfer);
        categoryTransfer.getCurrency().notifyCategoryTransferLinkRemove(categoryTransfer);
        this.categoryTransfers.remove(categoryTransfer);
        this.categoryTransferMap.remove(categoryTransfer.getId());
    }

    public void removePeriodTransfer(PeriodTransfer periodTransfer) {
        periodTransfer.getCategory().notifyPeriodTransferLinkRemove(periodTransfer);
        periodTransfer.getCurrency().notifyPeriodTransferLinkRemove(periodTransfer);
        periodTransfer.getDestination().notifyPeriodTransferDestinationLinkRemove(periodTransfer);
        periodTransfer.getSource().notifyPeriodTransferSourceLinkRemove(periodTransfer);
        this.periodTransfers.remove(periodTransfer);
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

    public NonPeriodFund getNonPeriodFund(String nonPeriodFundsId) {
        return nonPeriodFundMap.get(nonPeriodFundsId);
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

    public List<NonPeriodFund> getNonPeriodFunds() {
        return Collections.unmodifiableList(nonPeriodFunds);
    }

    public List<NonPeriodFundTransfer> getNonPeriodFundTransfers() {
        return Collections.unmodifiableList(nonPeriodFundTransfers);
    }
}
