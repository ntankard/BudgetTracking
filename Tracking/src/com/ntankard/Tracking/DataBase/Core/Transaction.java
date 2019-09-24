package com.ntankard.Tracking.DataBase.Core;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.DynamicGUI.Components.Object.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.Base.DataObject;
import com.ntankard.Tracking.DataBase.Core.Base.MoneyEvent;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class Transaction extends MoneyEvent {

    // My parents
    private Statement idStatement;
    private Category category;

    // My values
    private String idCode;

    /**
     * Constructor
     */
    public Transaction(Statement idStatement, String idCode, String description, Double value, Category category) {
        super(description, value);
        this.idStatement = idStatement;
        this.idCode = idCode;
        this.category = category;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public List<DataObject> getParents() {
        List<DataObject> toReturn = new ArrayList<>();
        toReturn.add(idStatement);
        toReturn.add(category);
        return toReturn;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public String getId() {
        return getIdStatement().getId() + " " + getIdCode();
    }

    /**
     * {@inheritDoc
     */
    @Override
    public Currency getCurrency() {
        return idStatement.getIdBank().getCurrency();
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public Statement getIdStatement() {
        return idStatement;
    }

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public String getIdCode() {
        return idCode;
    }

    public Category getCategory() {
        return category;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @SetterProperties(sourceMethod = "getStatements")
    public void setIdStatement(Statement idStatement) {
        this.idStatement.notifyChildUnLink(this);
        this.idStatement = idStatement;
        this.idStatement.notifyChildLink(this);
    }

    @SetterProperties(sourceMethod = "getCategories")
    public void setCategory(Category category) {
        this.category.notifyChildUnLink(this);
        this.category = category;
        this.category.notifyChildLink(this);
    }
}
