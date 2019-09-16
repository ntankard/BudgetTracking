package com.ntankard.Tracking.DataBase.Core.Transfers;

import com.ntankard.DynamicGUI.Components.Object.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.Category;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;

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

    public String getId() {
        return id;
    }

    public Period getSource() {
        return source;
    }

    public Period getDestination() {
        return destination;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Category getCategory() {
        return category;
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
        this.source = source;
    }

    @SetterProperties(sourceMethod = "getPeriods")
    public void setDestination(Period destination) {
        this.destination = destination;
    }

    @SetterProperties(sourceMethod = "getCurrencies")
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @SetterProperties(sourceMethod = "getCategories")
    public void setCategory(Category category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
