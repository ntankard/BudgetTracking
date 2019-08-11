package com.ntankard.Tracking.DataBase.Core;

import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.DynamicGUI.Components.Object.SetterProperties;

public class CategoryTransfer {

    // My parents
    private Statement idStatement;
    private Category source;
    private Category destination;

    // My values
    private String idCode;
    private String description;
    private Double value;

    public CategoryTransfer(Statement idStatement, String idCode, Category source, Category destination, String description, Double value) {
        this.idStatement = idStatement;
        this.source = source;
        this.destination = destination;
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

    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
    public String getId() {
        return idStatement.getId() + " " + idCode;
    }

    public Statement getIdStatement() {
        return idStatement;
    }

    public Category getSource() {
        return source;
    }

    public Category getDestination() {
        return destination;
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

    @SetterProperties(sourceMethod = "getCategories")
    public void setDestination(Category destination) {
        if (this.destination != null) {
            this.destination.notifyCategoriesTransferDestinationRemove(this);
        }
        this.destination = destination;
        this.destination.notifyCategoriesTransferDestinationLink(this);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
