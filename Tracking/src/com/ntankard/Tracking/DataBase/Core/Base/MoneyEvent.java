package com.ntankard.Tracking.DataBase.Core.Base;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.Tracking.DataBase.Core.Currency;

import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY;

@ClassExtensionProperties(includeParent = true)
public abstract class MoneyEvent extends DataObject {

    /**
     * A summary of the event
     */
    private String description;

    /**
     * The amount of the event
     */
    private double value;

    /**
     * Constructor
     */
    public MoneyEvent(String description, Double value) {
        this.description = description;
        this.value = value;
    }

    /**
     * Get the summary of the event
     *
     * @return A summary of the event
     */
    @DisplayProperties(order = 0)
    public String getDescription() {
        return description;
    }

    /**
     * Get the amount of the event
     *
     * @return The amount of the event
     */
    @DisplayProperties(order = 1, dataType = CURRENCY)
    public Double getValue() {
        return value;
    }

    /**
     * Get the currency that that value is in
     *
     * @return The currency that the value is in
     */
    public abstract Currency getCurrency();

    /**
     * Set the amount of the event
     *
     * @param value The amount of the event
     */
    public void setValue(Double value) {
        this.value = value;
    }

    /**
     * Set the summary of the event
     *
     * @param description A summary of the event
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
