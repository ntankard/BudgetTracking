package com.ntankard.Tracking.DataBase.Core.Transfers;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.DynamicGUI.Components.Object.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.Base.DataObject;
import com.ntankard.Tracking.DataBase.Core.Base.MoneyEvent;
import com.ntankard.Tracking.DataBase.Core.Category;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class CategoryTransfer extends MoneyEvent {

    // My parents
    private Period idPeriod;
    private Category source;
    private Category destination;
    private Currency currency;

    // My values
    private String idCode;

    /**
     * Constructor
     */
    public CategoryTransfer(Period idPeriod, String idCode, Category source, Category destination, Currency currency, String description, Double value) {
        super(description, value);
        this.idPeriod = idPeriod;
        this.source = source;
        this.destination = destination;
        this.idCode = idCode;
        this.currency = currency;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public List<DataObject> getParents() {
        List<DataObject> toReturn = new ArrayList<>();
        toReturn.add(idPeriod);
        toReturn.add(source);
        toReturn.add(destination);
        toReturn.add(currency);
        return toReturn;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
    public String getId() {
        return getIdPeriod().getId() + " " + getIdCode();
    }

    /**
     * {@inheritDoc
     */
    @Override
    public Currency getCurrency() {
        return currency;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
    public Period getIdPeriod() {
        return idPeriod;
    }

    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
    public String getIdCode() {
        return idCode;
    }

    @DisplayProperties(order = 2)
    public Category getSource() {
        return source;
    }

    @DisplayProperties(order = 3)
    public Category getDestination() {
        return destination;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @SetterProperties(sourceMethod = "getCategories")
    public void setDestination(Category destination) {
        this.destination.notifyChildUnLink(this);
        this.destination = destination;
        this.destination.notifyChildLink(this);
    }

    @SetterProperties(sourceMethod = "getCategories")
    public void setSource(Category source) {
        this.source.notifyChildUnLink(this);
        this.source = source;
        this.source.notifyChildLink(this);
    }

    @SetterProperties(sourceMethod = "getCurrencies")
    public void setCurrency(Currency currency) {
        this.currency.notifyChildUnLink(this);
        this.currency = currency;
        this.currency.notifyChildLink(this);
    }
}
