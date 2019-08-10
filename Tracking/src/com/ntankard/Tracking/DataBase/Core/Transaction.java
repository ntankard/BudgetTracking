package com.ntankard.Tracking.DataBase.Core;

import com.ntankard.DynamicGUI.Components.Object.SetterProperties;

public class Transaction {

    // My parents
    private Statement idStatement;

    // My values
    private String idCode;
    private String description;
    private Double value;

    // My Children

    /**
     * Constructor
     */
    public Transaction(Statement idStatement, String idCode, String description, Double value) {
        this.idStatement = idStatement;
        this.idCode = idCode;
        this.description = description;
        this.value = value;
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

    public String getId() {
        return idStatement.getId() + " " + idCode;
    }

    public Statement getIdStatement() {
        return idStatement;
    }

    public String getIdCode() {
        return idCode;
    }

    public String getDescription() {
        return description;
    }

    public Double getValue() {
        return value;
    }

    @SetterProperties(sourceMethod = "getStatements")
    public void setIdStatement(Statement idStatement) {
        this.idStatement.notifyTransactionLinkRemove(this);
        this.idStatement = idStatement;
        this.idStatement.notifyTransactionLink(this);
    }

    public void setIdCode(String idCode) {
        this.idCode = idCode;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
