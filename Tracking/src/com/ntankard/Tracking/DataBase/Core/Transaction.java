package com.ntankard.Tracking.DataBase.Core;

import com.ntankard.DynamicGUI.Components.Object.SetterProperties;

public class Transaction {

    // My parents
    private Statement idStatement;

    // My values
    private String idCode;
    private String description;
    private double value;

    // My Children

    /**
     * Constructor
     */
    public Transaction(Statement idStatement, String idCode, String description, double value) {
        this.idStatement = idStatement;
        this.idCode = idCode;
        this.description = description;
        this.value = value;

        this.idStatement.notifyTransactionLink(this);
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
    public double getValue() {
        return value;
    }

    @SetterProperties(sourceMethod = "getStatements")
    public void setIdStatement(Statement idStatement) {
        this.idStatement = idStatement;
    }

    public void setIdCode(String idCode) {
        this.idCode = idCode;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setValue(double value) {
        this.value = value;
    }
}