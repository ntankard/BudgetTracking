package com.ntankard.Tracking.DataBase.Core.Base;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.DynamicGUI.Components.Object.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.Category;
import com.ntankard.Tracking.DataBase.Core.Currency;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public abstract class Transfer<SourceType extends DataObject, DestinationType extends DataObject> extends MoneyEvent {

    // My parents
    private SourceType sourceContainer;
    private Category sourceCategory;
    private DestinationType destinationContainer;
    private Category destinationCategory;
    private Currency currency;

    /**
     * Constructor
     */
    public Transfer(String description, Double value, SourceType sourceContainer, Category sourceCategory, DestinationType destinationContainer, Category destinationCategory, Currency currency) {
        super(description, value);
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
        toReturn.add(sourceContainer);
        toReturn.add(sourceCategory);
        toReturn.add(destinationContainer);
        toReturn.add(destinationCategory);
        toReturn.add(currency);
        return toReturn;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public Currency getCurrency() {
        return currency;
    }

    /**
     * Is this combination of container and category the source of this transaction?
     *
     * @param sourceContainer The container to check
     * @param category        The category to check
     * @return True if the params represent this transfers source
     */
    public boolean isThisSource(DataObject sourceContainer, Category category) {
        return this.sourceContainer.equals(sourceContainer) && this.sourceCategory.equals(category);
    }

    /**
     * Is this combination of container and category the destination of this transaction?
     *
     * @param destinationContainer The container to check
     * @param category             The category to check
     * @return True if the params represent this transfers destination
     */
    public boolean isThisDestination(DataObject destinationContainer, Category category) {
        return this.destinationContainer.equals(destinationContainer) && this.destinationCategory.equals(category);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    protected SourceType getSourceContainer() {
        return sourceContainer;
    }

    protected Category getSourceCategory() {
        return sourceCategory;
    }

    protected DestinationType getDestinationContainer() {
        return destinationContainer;
    }

    protected Category getDestinationCategory() {
        return destinationCategory;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc
     */
    @SetterProperties(sourceMethod = "getCurrencies")
    public void setCurrency(Currency currency) {
        this.currency.notifyChildUnLink(this);
        this.currency = currency;
        this.currency.notifyChildLink(this);
    }

    protected void setSourceCategory(Category source) {
        this.sourceCategory.notifyChildUnLink(this);
        this.sourceCategory = source;
        this.sourceCategory.notifyChildLink(this);
    }

    protected void setSourceContainer(SourceType sourceContainer) {
        this.sourceContainer.notifyChildUnLink(this);
        this.sourceContainer = sourceContainer;
        this.sourceContainer.notifyChildLink(this);
    }

    protected void setDestinationCategory(Category destination) {
        this.destinationCategory.notifyChildUnLink(this);
        this.destinationCategory = destination;
        this.destinationCategory.notifyChildLink(this);
    }

    protected void setDestinationContainer(DestinationType destinationContainer) {
        this.destinationContainer.notifyChildUnLink(this);
        this.destinationContainer = destinationContainer;
        this.destinationContainer.notifyChildLink(this);
    }
}
