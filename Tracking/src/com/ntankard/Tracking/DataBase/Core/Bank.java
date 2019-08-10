package com.ntankard.Tracking.DataBase.Core;

import com.ntankard.ClassExtension.MemberProperties;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.TRACE_DISPLAY;

public class Bank {

    // My parents
    private Currency currency;

    // My values
    private String idBank;
    private String idAccount;

    // My Children
    private List<Statement> statements = new ArrayList<>();

    /**
     * Constructor
     */
    public Bank(String idBank, String idAccount, Currency currency) {
        this.idBank = idBank;
        this.idAccount = idAccount;
        this.currency = currency;
    }

    /**
     * Notify that another object has linked to this one
     *
     * @param statement The object that linked
     */
    public void notifyStatementLink(Statement statement) {
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

    @MemberProperties(verbosityLevel = TRACE_DISPLAY)
    public String getId() {
        return idBank + "-" + idAccount;
    }

    public String getIdBank() {
        return idBank;
    }

    public String getIdAccount() {
        return idAccount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public List<Statement> getStatements() {
        return statements;
    }
}
