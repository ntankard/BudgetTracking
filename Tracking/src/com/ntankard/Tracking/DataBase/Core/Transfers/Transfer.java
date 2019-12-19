package com.ntankard.Tracking.DataBase.Core.Transfers;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY;
import static com.ntankard.ClassExtension.MemberProperties.*;

@ClassExtensionProperties(includeParent = true)
public abstract class Transfer<SourceType extends DataObject, DestinationType extends DataObject> extends DataObject {

    // My parents
    protected Period period;
    protected SourceType source;
    protected DestinationType destination;
    protected Currency currency;

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
        toReturn.add(getSource());
        toReturn.add(getDestination());
        toReturn.add(getPeriod());
        toReturn.add(getDestinationCurrency());
        if (!getDestinationCurrency().equals(getSourceCurrency())) {
            toReturn.add(getSourceCurrency());
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
        return this.getSource().equals(sourceContainer);
    }

    /**
     * Is this combination of container and category the destination of this transaction?
     *
     * @param destinationContainer The container to check
     * @return True if the params represent this transfers destination
     */
    public Boolean isThisDestination(DataObject destinationContainer) {
        return this.getDestination().equals(destinationContainer);
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

    @DisplayProperties(order = 4)
    public SourceType getSource() {
        return source;
    }

    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 5, dataType = CURRENCY)
    public Double getSourceValue() {
        return -value;
    }

    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 6)
    public Currency getSourceCurrency() {
        return currency;
    }

    @DisplayProperties(order = 7)
    public DestinationType getDestination() {
        return destination;
    }

    @DisplayProperties(order = 8, dataType = CURRENCY)
    public Double getDestinationValue() {
        return value;
    }

    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 9)
    public Currency getDestinationCurrency() {
        return currency;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDestinationValue(Double value) {
        this.value = value;
    }

    @SetterProperties(localSourceMethod = "sourceOptions")
    protected void setSource(SourceType source) {
        if (source == null) throw new IllegalArgumentException("Source is null");
        this.source.notifyChildUnLink(this);
        this.source = source;
        this.source.notifyChildLink(this);
    }

    @SetterProperties(localSourceMethod = "sourceOptions")
    protected void setDestination(DestinationType destination) {
        if (destination == null) throw new IllegalArgumentException("Destination is null");
        this.destination.notifyChildUnLink(this);
        this.destination = destination;
        this.destination.notifyChildLink(this);
    }
}
