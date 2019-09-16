package com.ntankard.Tracking.DataBase.Core;

import com.ntankard.ClassExtension.MemberProperties;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;
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
     * {@inheritDoc
     */
    @Override
    public String toString() {
        return getId();
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################# Link Management ################################################
    //------------------------------------------------------------------------------------------------------------------

    // Statement Link --------------------------------------------------------------------------------------------------

    /**
     * Notify that a Statement has linked to this Bank
     *
     * @param added The Statement that linked
     */
    public void notifyStatementLink(Statement added) {
        statements.add(added);
    }

    /**
     * Notify that a Statement has removed there link to this Bank
     *
     * @param removed The Statement that was linked
     */
    public void notifyStatementLinkRemove(Statement removed) {
        statements.remove(removed);
    }

    /**
     * Get all the Statement that have linked to this Bank
     *
     * @return All the Statements that have linked to this Bank
     */
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public List<Statement> getStatements() {
        return statements;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @MemberProperties(verbosityLevel = TRACE_DISPLAY)
    public String getId() {
        return getIdBank() + "-" + getIdAccount();
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
}
