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
     * @param statement The statement to read from
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

    /**
     * Sum all the transaction for this category, in this period that are in the specified currency
     *
     * @param toSum The currency to sum
     * @return All the transaction for this category, in this period that are in the specified currency
     */
    public double getTotal(Currency toSum) {
        double sum = 0;
        for (Statement statement : statements) {
            if (statement.getIdBank().getCurrency().equals(toSum)) {
                for (Transaction transaction : statement.getTransactions()) {
                    if (transaction.getCategory().equals(category)) {
                        sum += transaction.getValue();
                    }
                }
            }
        }
        return sum;
    }

    /**
     * Sum all the transaction for this category, in this period. Return in the primary currency
     *
     * @return All the transaction for this category, in this period. Return in the primary currency
     */
    public double getTotal() {
        double sum = 0;
        for (Currency currency : getCurrencies()) {
            sum += getTotal(currency) * currency.getToPrimary();
        }
        return sum;
    }
}
