package com.ntankard.Tracking.DataBase.Core.MoneyEvents;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.CurrencyBound;
import com.ntankard.Tracking.DataBase.Core.DataObject;
import com.ntankard.Tracking.DataBase.Core.IdDataObject;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Currency;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY;
import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public abstract class MoneyEvent<SourceType extends DataObject, SourceCategory extends DataObject, DestinationType extends DataObject, DestinationCategory extends DataObject> extends IdDataObject implements CurrencyBound {

    /**
     * A summary of the event
     */
    private String description;

    /**
     * The amount of the event
     */
    private double value;

    // My parents
    private SourceType sourceContainer;
    private SourceCategory sourceCategory;
    private DestinationType destinationContainer;
    private DestinationCategory destinationCategory;
    private Currency currency;

    /**
     * Constructor
     */
    public MoneyEvent(String id, String description, Double value, SourceType sourceContainer, SourceCategory sourceCategory, DestinationType destinationContainer, DestinationCategory destinationCategory, Currency currency) {
        super(id);
        this.description = description;
        this.value = value;
        this.sourceContainer = sourceContainer;
        this.sourceCategory = sourceCategory;
        this.destinationContainer = destinationContainer;
        this.destinationCategory = destinationCategory;
        this.currency = currency;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public List<DataObject> getParents() {
        List<DataObject> toReturn = new ArrayList<>();
        if (sourceContainer != null) {
            toReturn.add(sourceContainer);
        }
        if (sourceCategory != null) {
            toReturn.add(sourceCategory);
        }
        if (destinationContainer != null) {
            toReturn.add(destinationContainer);
        }
        if (destinationCategory != null) {
            toReturn.add(destinationCategory);
        }
        toReturn.add(currency);
        return toReturn;
    }

    /**
     * Is this combination of container and category the source of this transaction?
     *
     * @param sourceContainer The container to check
     * @param category        The category to check
     * @return True if the params represent this transfers source
     */
    public boolean isThisSource(DataObject sourceContainer, DataObject category) {
        return this.sourceContainer.equals(sourceContainer) && this.sourceCategory.equals(category);
    }

    /**
     * Is this combination of container and category the source of this transaction?
     *
     * @param sourceContainer The container to check
     * @return True if the params represent this transfers source
     */
    public boolean isThisSource(DataObject sourceContainer) {
        return this.sourceContainer.equals(sourceContainer);
    }

    /**
     * Is this combination of container and category the destination of this transaction?
     *
     * @param destinationContainer The container to check
     * @param category             The category to check
     * @return True if the params represent this transfers destination
     */
    public boolean isThisDestination(DataObject destinationContainer, DataObject category) {
        return this.destinationContainer.equals(destinationContainer) && this.destinationCategory.equals(category);
    }

    /**
     * Is this combination of container and category the destination of this transaction?
     *
     * @param destinationContainer The container to check
     * @return True if the params represent this transfers destination
     */
    public boolean isThisDestination(DataObject destinationContainer) {
        return this.destinationContainer.equals(destinationContainer);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @DisplayProperties(order = 0)
    public String getDescription() {
        return description;
    }

    @DisplayProperties(order = 1, dataType = CURRENCY)
    public Double getValue() {
        return value;
    }

    protected SourceType getSourceContainer() {
        return sourceContainer;
    }

    protected SourceCategory getSourceCategory() {
        return sourceCategory;
    }

    protected DestinationType getDestinationContainer() {
        return destinationContainer;
    }

    protected DestinationCategory getDestinationCategory() {
        return destinationCategory;
    }

    public Currency getCurrency() {
        return currency;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public void setDescription(String description) {
        this.description = description;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    protected void setSourceCategory(SourceCategory source) {
        this.sourceCategory.notifyChildUnLink(this);
        this.sourceCategory = source;
        this.sourceCategory.notifyChildLink(this);
    }

    protected void setSourceContainer(SourceType sourceContainer) {
        this.sourceContainer.notifyChildUnLink(this);
        this.sourceContainer = sourceContainer;
        this.sourceContainer.notifyChildLink(this);
    }

    protected void setDestinationCategory(DestinationCategory destination) {
        this.destinationCategory.notifyChildUnLink(this);
        this.destinationCategory = destination;
        this.destinationCategory.notifyChildLink(this);
    }

    protected void setDestinationContainer(DestinationType destinationContainer) {
        this.destinationContainer.notifyChildUnLink(this);
        this.destinationContainer = destinationContainer;
        this.destinationContainer.notifyChildLink(this);
    }

    @SetterProperties(sourceMethod = "getData")
    public void setCurrency(Currency currency) {
        this.currency.notifyChildUnLink(this);
        this.currency = currency;
        this.currency.notifyChildLink(this);
    }
}
