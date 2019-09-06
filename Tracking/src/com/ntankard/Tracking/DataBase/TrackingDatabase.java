package com.ntankard.Tracking.DataBase;

import com.ntankard.Tracking.DataBase.Core.*;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Transfers.CategoryTransfer;
import com.ntankard.Tracking.DataBase.Interface.PeriodCategory;

import java.util.*;

public class TrackingDatabase {

    // Core Containers
    private List<Currency> currencies = new ArrayList<>();
    private List<Category> categories = new ArrayList<>();
    private List<Bank> banks = new ArrayList<>();
    private List<Period> periods = new ArrayList<>();
    private List<Statement> statements = new ArrayList<>();
    private List<Transaction> transactions = new ArrayList<>();
    private List<CategoryTransfer> categoryTransfer = new ArrayList<>();

    // Special Containers
    private List<PeriodCategory> periodCategory = new ArrayList<>();

    // Container accessors
    private Map<String, Currency> currencyMap = new HashMap<>();
    private Map<String, Category> categoryMap = new HashMap<>();
    private Map<String, Bank> bankMap = new HashMap<>();
    private Map<String, Period> periodMap = new HashMap<>();
    private Map<Period, Map<Bank, Statement>> statementMap = new HashMap<>();
    private Map<String, CategoryTransfer> categoryTransferMap = new HashMap<>();

    /**
     * Repair any missing data
     */
    public void finalizeData() {
        for (Period p : periods) { // TODO this needs to be sorted based on data to work properly
            Period last = getPreviousPeriod(p);
            for (Bank b : banks) {

                // Dose this period have a statement for this bank?
                Statement match = getStatement(b, p);
                if (match == null) {

                    // Find the statement from the previous month if it exists
                    double end = 0.0;
                    if (last != null) {
                        Statement lastStatement = getStatement(last, b);
                        end = lastStatement.getEnd();
                    }

                    addStatement(new Statement(b, p, end, 0.0, 0.0, 0.0));
                }
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
     * @param s The statement to containing the transactions
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
     * Find the next available categoryTransfer code
     *
     * @param core The statement to containing the transactions
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

    //------------------------------------------------------------------------------------------------------------------
    //############################################# Populate Data ######################################################
    //------------------------------------------------------------------------------------------------------------------

    public void addCurrency(Currency currency) {
        this.currencies.add(currency);
        this.currencyMap.put(currency.getId(), currency);
    }

    public void addCategory(Category category) {
        if (category.getIdCategory() != null) {
            category.getIdCategory().notifyCategoryLink(category);
        }
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
        this.periodCategory.add(new PeriodCategory(period, this));
    }

    public void addStatement(Statement statement) {
        this.statements.add(statement);
        this.statementMap.get(statement.getIdPeriod()).put(statement.getIdBank(), statement);
        statement.getIdBank().notifyStatementLink(statement);
        statement.getIdPeriod().notifyStatementLink(statement);
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        transaction.getIdStatement().notifyTransactionLink(transaction);
        transaction.getCategory().notifyTransactionLink(transaction);
    }

    public void addCategoryTransfer(CategoryTransfer categoryTransfer) {
        this.categoryTransfer.add(categoryTransfer);
        this.categoryTransferMap.put(categoryTransfer.getId(), categoryTransfer);
        categoryTransfer.getIdPeriod().notifyCategoryTransferLink(categoryTransfer);
        categoryTransfer.getDestination().notifyCategoriesTransferDestinationLink(categoryTransfer);
        categoryTransfer.getSource().notifyCategoriesTransferSourceLink(categoryTransfer);
        categoryTransfer.getCurrency().notifyCategoryTransferLink(categoryTransfer);
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################## Remove Data #######################################################
    //------------------------------------------------------------------------------------------------------------------

    public void removeTransaction(Transaction transaction) {
        transaction.getIdStatement().notifyTransactionLinkRemove(transaction);
        transaction.getCategory().notifyTransactionLinkRemove(transaction);
        this.transactions.remove(transaction);
    }

    public void removeCategoryTransfer(CategoryTransfer categoryTransfer) {
        categoryTransfer.getDestination().notifyCategoriesTransferDestinationRemove(categoryTransfer);
        categoryTransfer.getSource().notifyCategoriesTransferSourceLinkRemove(categoryTransfer);
        categoryTransfer.getIdPeriod().notifyCategoryTransferLinkRemove(categoryTransfer);
        categoryTransfer.getCurrency().notifyCategoryTransferLinkRemove(categoryTransfer);
        this.categoryTransfer.remove(categoryTransfer);
        this.categoryTransferMap.remove(categoryTransfer.getId());
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
        return statementMap.get(periodID).get(bankID);
    }

    public CategoryTransfer getCategoryTransfer(String categoryTransferId) {
        return categoryTransferMap.get(categoryTransferId);
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

    public List<CategoryTransfer> getCategoryTransfer() {
        return Collections.unmodifiableList(categoryTransfer);
    }

    public List<PeriodCategory> getPeriodCategory() {
        return Collections.unmodifiableList(periodCategory);
    }
}
