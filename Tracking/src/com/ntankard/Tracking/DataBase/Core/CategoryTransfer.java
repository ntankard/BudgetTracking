package com.ntankard.Tracking.DataBase.Core;

import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.DisplayProperties.DataType;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.DynamicGUI.Components.Object.SetterProperties;

import static com.ntankard.ClassExtension.DisplayProperties.DataType.*;

public class CategoryTransfer {

    // My parents
    private Period idPeriod;
    private Category source;
    private Category destination;
    private Currency currency;

    // My values
    private String idCode;
    private String description;
    private Double value;

    public CategoryTransfer(Period idPeriod, String idCode, Category source, Category destination, Currency currency, String description, Double value) {
        this.idPeriod = idPeriod;
        this.idCode = idCode;
        this.source = source;
        this.destination = destination;
        this.currency = currency;
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

    @DisplayProperties(dataType = CURRENCY_YEN)
    public Double getValue() {
        return value;
    }

    public Currency getCurrency() {
        return currency;
    }

    @SetterProperties(sourceMethod = "getCurrencies")
    public void setCurrency(Currency currency) {
        this.currency = currency;
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
