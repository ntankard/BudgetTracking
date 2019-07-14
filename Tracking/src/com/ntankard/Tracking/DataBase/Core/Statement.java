package com.ntankard.Tracking.DataBase.Core;

import java.util.ArrayList;
import java.util.List;

public class Statement {

    // My parents
    private Bank idBank;
    private Period idPeriod;

    // My values

    // My Children
    private List<Transaction> transactions = new ArrayList<>();

    /**
     * Constructor
     */
    public Statement(Bank bank, Period period) {
        this.idBank = bank;
        this.idPeriod = period;

        this.idBank.notifyStatementLink(this);
        this.idPeriod.notifyStatementLink(this);
    }

    /**
     * Notify that another object has linked to this one
     * @param transaction The object that linked
     */
    public void notifyTransactionLink(Transaction transaction) {
        transactions.add(transaction);
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
        return idBank.getId() + " " + idPeriod.getId();
    }
    public Bank getIdBank() {
        return idBank;
    }
    public Period getIdPeriod() {
        return idPeriod;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}