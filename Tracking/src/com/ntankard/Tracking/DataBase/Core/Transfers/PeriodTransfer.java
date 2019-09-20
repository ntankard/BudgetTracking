package com.ntankard.Tracking.DataBase.Core.Transfers;

import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.DynamicGUI.Components.Object.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.Category;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;

import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY;

public class PeriodTransfer {

    // My parents
    private Period source;
    private Period destination;
    private Currency currency;
    private Category category;

    // My values
    private String id;
    private String description;
    private Double value;

    // My Children

    /**
     * Constructor
     */
    public PeriodTransfer(String id, Period source, Period destination, Currency currency, Category category, String description, Double value) {
        this.id = id;
        this.source = source;
        this.destination = destination;
        this.currency = currency;
        this.category = category;
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
        return id;
    }

    @DisplayProperties(order = 2)
    public Period getSource() {
        return source;
    }

    @DisplayProperties(order = 3)
    public Period getDestination() {
        return destination;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Category getCategory() {
        return category;
    }

    @DisplayProperties(order = 0)
    public String getDescription() {
        return description;
    }

    @DisplayProperties(order = 1, dataType = CURRENCY)
    public Double getValue() {
        return value;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @SetterProperties(sourceMethod = "getPeriods")
    public void setSource(Period source) {
        this.source.notifyPeriodTransferSourceLinkRemove(this);
        this.source = source;
        this.source.notifyPeriodTransferSourceLink(this);
    }

    @SetterProperties(sourceMethod = "getPeriods")
    public void setDestination(Period destination) {
        this.destination.notifyPeriodTransferDestinationLinkRemove(this);
        this.destination = destination;
        this.destination.notifyPeriodTransferDestinationLink(this);
    }

    @SetterProperties(sourceMethod = "getCurrencies")
    public void setCurrency(Currency currency) {
        this.currency.notifyPeriodTransferLinkRemove(this);
        this.currency = currency;
        this.currency.notifyPeriodTransferLink(this);
    }

    @SetterProperties(sourceMethod = "getCategories")
    public void setCategory(Category category) {
        this.category.notifyPeriodTransferLinkRemove(this);
        this.category = category;
        this.category.notifyPeriodTransferLink(this);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
