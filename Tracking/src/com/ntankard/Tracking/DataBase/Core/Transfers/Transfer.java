package com.ntankard.Tracking.DataBase.Core.Transfers;

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
import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public abstract class Transfer<SourceType extends DataObject, DestinationType extends DataObject> extends DataObject implements CurrencyBound {

    private Period period;

    // My parents
    private SourceType source;
    private DestinationType destination;
    private Currency currency;

    // My values
    private String description;
    private Double value;

    /**
     * Constructor
     */
    @ParameterMap(shouldSave = false)
    public Transfer(Integer id, String description, Double value, Period period, SourceType source, DestinationType destination, Currency currency) {
        super(id);
        this.description = description;
        this.value = value;
        this.period = period;
        this.source = source;
        this.destination = destination;
        this.currency = currency;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 21)
    public List<DataObject> getParents() {
        List<DataObject> toReturn = new ArrayList<>();
        toReturn.add(source);
        toReturn.add(destination);
        toReturn.add(period);
        if (currency != null) {
            toReturn.add(currency);
        }
        return toReturn;
    }

    /**
     * Is this combination of container and category the source of this transaction?
     *
     * @param sourceContainer The container to check
     * @return True if the params represent this transfers source
     */
    public Boolean isThisSource(DataObject sourceContainer) {
        return this.source.equals(sourceContainer);
    }

    /**
     * Is this combination of container and category the destination of this transaction?
     *
     * @param destinationContainer The container to check
     * @return True if the params represent this transfers destination
     */
    public Boolean isThisDestination(DataObject destinationContainer) {
        return this.destination.equals(destinationContainer);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 2)
    public Period getPeriod() {
        return period;
    }

    @DisplayProperties(order = 3)
    public String getDescription() {
        return description;
    }

    @DisplayProperties(order = 4, dataType = CURRENCY)
    public Double getValue() {
        return value;
    }

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 5, dataType = CURRENCY)
    public Double getSourceValue() {
        return -value;
    }

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 6, dataType = CURRENCY)
    public Double getDestinationValue() {
        return value;
    }

    @DisplayProperties(order = 7)
    public SourceType getSource() {
        return source;
    }

    @DisplayProperties(order = 8)
    public DestinationType getDestination() {
        return destination;
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

    @SetterProperties(sourceMethod = "getData")
    public void setSource(SourceType source) {
        this.source.notifyChildUnLink(this);
        this.source = source;
        this.source.notifyChildLink(this);
    }

    @SetterProperties(sourceMethod = "getData")
    public void setDestination(DestinationType destination) {
        this.destination.notifyChildUnLink(this);
        this.destination = destination;
        this.destination.notifyChildLink(this);
    }

    @SetterProperties(sourceMethod = "getData")
    public void setCurrency(Currency currency) {
        this.currency.notifyChildUnLink(this);
        this.currency = currency;
        this.currency.notifyChildLink(this);
    }
}
