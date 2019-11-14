package com.ntankard.Tracking.DataBase.Core.MoneyEvents;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.CurrencyBound;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.SupportObjects.Currency;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY;
import static com.ntankard.ClassExtension.MemberProperties.DEBUG_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public abstract class MoneyEvent<SourceType extends DataObject, SourceCategory extends DataObject, DestinationType extends DataObject, DestinationCategory extends DataObject> extends DataObject implements CurrencyBound {

    private Period period;

    // My parents
    private SourceType sourceContainer;
    private SourceCategory sourceCategory;
    private DestinationType destinationContainer;
    private DestinationCategory destinationCategory;
    private Currency currency;

    // My values
    private String description;
    private double value;

    /**
     * Constructor
     */
    @ParameterMap(shouldSave = false)
    public MoneyEvent(Integer id, String description, Double value, Period period, SourceType sourceContainer, SourceCategory sourceCategory, DestinationType destinationContainer, DestinationCategory destinationCategory, Currency currency) {
        super(id);
        this.description = description;
        this.value = value;
        this.period = period;
        this.sourceContainer = sourceContainer;
        this.sourceCategory = sourceCategory;
        this.destinationContainer = destinationContainer;
        this.destinationCategory = destinationCategory;
        this.currency = currency;
    }

    public Period getPeriod() {
        return period;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 21)
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
    public Boolean isThisSource(DataObject sourceContainer, DataObject category) {
        return this.sourceContainer.equals(sourceContainer) && this.sourceCategory.equals(category);
    }

    /**
     * Is this combination of container and category the source of this transaction?
     *
     * @param sourceContainer The container to check
     * @return True if the params represent this transfers source
     */
    public Boolean isThisSource(DataObject sourceContainer) {
        return this.sourceContainer.equals(sourceContainer);
    }

    /**
     * Is this combination of container and category the destination of this transaction?
     *
     * @param destinationContainer The container to check
     * @param category             The category to check
     * @return True if the params represent this transfers destination
     */
    public Boolean isThisDestination(DataObject destinationContainer, DataObject category) {
        if (category == null || this.destinationCategory == null) {
            return isThisDestination(destinationContainer);
        }
        return this.destinationContainer.equals(destinationContainer) && this.destinationCategory.equals(category);
    }

    /**
     * Is this combination of container and category the destination of this transaction?
     *
     * @param destinationContainer The container to check
     * @return True if the params represent this transfers destination
     */
    public Boolean isThisDestination(DataObject destinationContainer) {
        return this.destinationContainer.equals(destinationContainer);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @DisplayProperties(order = 3)
    public String getDescription() {
        return description;
    }

    @DisplayProperties(order = 4, dataType = CURRENCY)
    public Double getValue() {
        return value;
    }

    @DisplayProperties(order = 5)
    protected SourceType getSourceContainer() {
        return sourceContainer;
    }

    @DisplayProperties(order = 6)
    protected SourceCategory getSourceCategory() {
        return sourceCategory;
    }

    @DisplayProperties(order = 7)
    protected DestinationType getDestinationContainer() {
        return destinationContainer;
    }

    @DisplayProperties(order = 8)
    protected DestinationCategory getDestinationCategory() {
        return destinationCategory;
    }

    @Override
    @DisplayProperties(order = 9)
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
