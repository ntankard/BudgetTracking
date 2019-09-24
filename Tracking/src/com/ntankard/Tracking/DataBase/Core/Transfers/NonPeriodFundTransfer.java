package com.ntankard.Tracking.DataBase.Core.Transfers;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.DynamicGUI.Components.Object.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.Base.DataObject;
import com.ntankard.Tracking.DataBase.Core.Base.MoneyEvent;
import com.ntankard.Tracking.DataBase.Core.Category;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.NonPeriodFund;
import com.ntankard.Tracking.DataBase.Core.Period;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class NonPeriodFundTransfer extends MoneyEvent {

    // My parents
    private Period source;
    private NonPeriodFund destination;
    private Category sourceCategory;
    private Currency currency;

    // My values
    private String id;

    /**
     * Constructor
     */
    public NonPeriodFundTransfer(String id, Period source, NonPeriodFund destination, Category sourceCategory, Currency currency, String description, Double value) {
        super(description, value);
        this.id = id;
        this.source = source;
        this.destination = destination;
        this.sourceCategory = sourceCategory;
        this.currency = currency;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public List<DataObject> getParents() {
        List<DataObject> toReturn = new ArrayList<>();
        toReturn.add(source);
        toReturn.add(destination);
        toReturn.add(sourceCategory);
        toReturn.add(currency);
        return toReturn;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
    public String getId() {
        return id;
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

    @DisplayProperties(order = 2)
    public Period getSource() {
        return source;
    }

    @DisplayProperties(order = 4)
    public NonPeriodFund getDestination() {
        return destination;
    }

    @DisplayProperties(order = 3)
    public Category getSourceCategory() {
        return sourceCategory;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @SetterProperties(sourceMethod = "getPeriods")
    public void setSource(Period source) {
        this.source.notifyChildUnLink(this);
        this.source = source;
        this.source.notifyChildLink(this);
    }

    @SetterProperties(sourceMethod = "getNonPeriodFunds")
    public void setDestination(NonPeriodFund destination) {
        this.destination.notifyChildUnLink(this);
        this.destination = destination;
        this.destination.notifyChildLink(this);
    }

    @SetterProperties(sourceMethod = "getCategories")
    public void setSourceCategory(Category sourceCategory) {
        this.sourceCategory.notifyChildUnLink(this);
        this.sourceCategory = sourceCategory;
        this.sourceCategory.notifyChildLink(this);
    }

    @SetterProperties(sourceMethod = "getCurrencies")
    public void setCurrency(Currency currency) {
        this.currency.notifyChildUnLink(this);
        this.currency = currency;
        this.currency.notifyChildLink(this);
    }
}
