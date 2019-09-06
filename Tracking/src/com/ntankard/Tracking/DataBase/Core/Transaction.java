package com.ntankard.Tracking.DataBase.Core;

import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.DynamicGUI.Components.Object.SetterProperties;

import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY;
import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

public class Transaction {

    // My parents
    private Statement idStatement;
    private Category category;

    // My values
    private String idCode;
    private String description;
    private Double value;

    // My Children

    /**
     * Constructor
     */
    public Transaction(Statement idStatement, String idCode, String description, Double value, Category category) {
        this.idStatement = idStatement;
        this.idCode = idCode;
        this.description = description;
        this.value = value;
        this.category = category;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public String toString() {
        return getId();
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public String getId() {
        return getIdStatement().getId() + " " + getIdCode();
    }

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public Statement getIdStatement() {
        return idStatement;
    }

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public String getIdCode() {
        return idCode;
    }

    public String getDescription() {
        return description;
    }

    @DisplayProperties(dataType = CURRENCY)
    public Double getValue() {
        return value;
    }

    public Category getCategory() {
        return category;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public void setIdCode(String idCode) {
        this.idCode = idCode;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @SetterProperties(sourceMethod = "getStatements")
    public void setIdStatement(Statement idStatement) {
        this.idStatement.notifyTransactionLinkRemove(this);
        this.idStatement = idStatement;
        this.idStatement.notifyTransactionLink(this);
    }

    @SetterProperties(sourceMethod = "getCategories")
    public void setCategory(Category category) {
        this.category.notifyTransactionLinkRemove(this);
        this.category = category;
        this.category.notifyTransactionLink(this);
    }
}
