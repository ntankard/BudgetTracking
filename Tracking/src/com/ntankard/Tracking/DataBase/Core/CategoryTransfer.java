package com.ntankard.Tracking.DataBase.Core;

import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.DynamicGUI.Components.Object.SetterProperties;

public class CategoryTransfer {

    // My parents
    private Period idPeriod;
    private Category source;
    private Category destination;

    // My values
    private String idCode;
    private String description;
    private Double value;

    public CategoryTransfer(Period idPeriod, String idCode, Category source, Category destination, String description, Double value) {
        this.idPeriod = idPeriod;
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
        return idPeriod.getId() + " " + idCode;
    }

    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
    public Period getIdPeriod() {
        return idPeriod;
    }

    public Category getSource() {
        return source;
    }

    public Category getDestination() {
        return destination;
    }

    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
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

    @SetterProperties(sourceMethod = "getCategories")
    public void setSource(Category destination) {
        if (this.source != null) {
            this.source.notifyCategoriesTransferSourceLinkRemove(this);
        }
        this.source = destination;
        this.source.notifyCategoriesTransferSourceLink(this);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
