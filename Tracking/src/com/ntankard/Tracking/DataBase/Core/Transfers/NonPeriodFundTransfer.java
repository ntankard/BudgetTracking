package com.ntankard.Tracking.DataBase.Core.Transfers;

import com.ntankard.DynamicGUI.Components.Object.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.Category;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.NonPeriodFund;
import com.ntankard.Tracking.DataBase.Core.Period;

public class NonPeriodFundTransfer {

    // My parents
    private Period source;
    private NonPeriodFund destination;
    private Category sourceCategory;
    private Currency currency;

    // My values
    private String id;
    private String description;
    private Double value;

    // My Children

    /**
     * Constructor
     */
    public NonPeriodFundTransfer(String id, Period source, NonPeriodFund destination, Category sourceCategory, Currency currency, String description, Double value) {
        this.id = id;
        this.source = source;
        this.destination = destination;
        this.sourceCategory = sourceCategory;
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
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public String getId() {
        return id;
    }

    public Period getSource() {
        return source;
    }

    public NonPeriodFund getDestination() {
        return destination;
    }

    public Category getSourceCategory() {
        return sourceCategory;
    }

    public Currency getCurrency() {
        return currency;
    }

    public String getDescription() {
        return description;
    }

    public Double getValue() {
        return value;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @SetterProperties(sourceMethod = "getPeriods")
    public void setSource(Period source) {
        this.source.notifyNonPeriodFundTransferLinkRemove(this);
        this.source = source;
        this.source.notifyNonPeriodFundTransferLink(this);
    }

    @SetterProperties(sourceMethod = "getNonPeriodFunds")
    public void setDestination(NonPeriodFund destination) {
        this.destination.notifyNonPeriodFundTransferLinkRemove(this);
        this.destination = destination;
        this.destination.notifyNonPeriodFundTransferLink(this);
    }

    @SetterProperties(sourceMethod = "getCategories")
    public void setSourceCategory(Category sourceCategory) {
        this.sourceCategory.notifyNonPeriodFundTransferLinkRemove(this);
        this.sourceCategory = sourceCategory;
        this.sourceCategory.notifyNonPeriodFundTransferLink(this);
    }

    @SetterProperties(sourceMethod = "getCurrencies")
    public void setCurrency(Currency currency) {
        this.currency.notifyNonPeriodFundTransferLinkRemove(this);
        this.currency = currency;
        this.currency.notifyNonPeriodFundTransferLink(this);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
