package com.ntankard.Tracking.DataBase.Core.Transfers;

import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.DynamicGUI.Components.Object.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.Category;
import com.ntankard.Tracking.DataBase.Core.Period;

import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY_YEN;

public class CategoryTransfer {

    // My parents
    private Period idPeriod;
    private Category source;
    private Category destination;

    // My values
    private String idCode;
    private String description;
    private Double value;

    // My Children

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
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
    public String getId() {
        return getIdPeriod().getId() + " " + getIdCode();
    }

    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
    public Period getIdPeriod() {
        return idPeriod;
    }

    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
    public String getIdCode() {
        return idCode;
    }

    public Category getSource() {
        return source;
    }

    public Category getDestination() {
        return destination;
    }

    public String getDescription() {
        return description;
    }

    @DisplayProperties(dataType = CURRENCY_YEN)
    public Double getValue() {
        return value;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @SetterProperties(sourceMethod = "getCategories")
    public void setSource(Category destination) {
        this.source.notifyCategoriesTransferSourceLinkRemove(this);
        this.source = destination;
        this.source.notifyCategoriesTransferSourceLink(this);
    }

    @SetterProperties(sourceMethod = "getCategories")
    public void setDestination(Category destination) {
        this.destination.notifyCategoriesTransferDestinationRemove(this);
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
