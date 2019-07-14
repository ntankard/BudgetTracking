package com.ntankard.Tracking.DataBase.Core;

import java.util.ArrayList;
import java.util.List;

public class Bank {

    // My parents
    private Currency currency;

    // My values
    private String id;

    // My Children
    private List<Statement> statements = new ArrayList<>();

    /**
     * Constructor
     */
    public Bank(String id, Currency currency) {
        this.id = id;
        this.currency = currency;

        this.currency.notifyBankLink(this);
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
    public Currency getCurrency() {
        return currency;
    }

    public List<Statement> getStatements() {
        return statements;
    }
}