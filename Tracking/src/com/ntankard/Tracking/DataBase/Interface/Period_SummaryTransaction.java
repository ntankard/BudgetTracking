package com.ntankard.Tracking.DataBase.Interface;

import com.ntankard.Tracking.DataBase.Core.*;

import java.util.ArrayList;
import java.util.List;

public class Period_SummaryTransaction extends Period_Summary<Transaction> {

    /**
     * All the Statements for this period (not yet filtered by category)
     */
    private List<Statement> statements;

    /**
     * Constructor
     *
     * @param period     The period to summarise
     * @param category   The category to filler on
     * @param statements All the Statements for this period (not yet filtered by category)
     */
    public Period_SummaryTransaction(Period period, Category category, List<Statement> statements) {
        super(period, category);
        this.statements = statements;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public List<Currency> getCurrencies() {
        List<Currency> toReturn = new ArrayList<>();

        for (Statement statement : statements) {
            if (getTransactions(statement).size() != 0) {
                Currency currency = statement.getIdBank().getCurrency();
                if (!toReturn.contains(currency)) {
                    toReturn.add(currency);
                }
            }
        }

        return toReturn;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public List<Transaction> getEvents() {
        List<Transaction> transactions = new ArrayList<>();
        for (Statement statement : statements) {
            transactions.addAll(getTransactions(statement));
        }
        return transactions;
    }

    /**
     * Get all transactions from a specific statement
     *
     * @param statement The statment to read from
     * @return All transactions from a specific statement
     */
    public List<Transaction> getTransactions(Statement statement) {
        List<Transaction> transactions = new ArrayList<>();
        for (Transaction transaction : statement.getTransactions()) {
            if (transaction.getCategory().equals(category)) {
                transactions.add(transaction);
            }
        }
        return transactions;
    }
}
