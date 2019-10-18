package com.ntankard.Tracking.DataBase.Core.MoneyEvents;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.DynamicGUI.Components.Object.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.DataObject;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Statement;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Category;

import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class Transaction extends MoneyEvent<Statement, Category, Period, Category> {

    // My values
    private String idCode;

    /**
     * Constructor
     */
    public Transaction(Statement idStatement, String idCode, String description, Double value, Category category) {
        super(description, value, idStatement, null, idStatement.getIdPeriod(), category, idStatement.getIdBank().getCurrency());
        this.idCode = idCode;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public String getId() {
        return getDestinationContainer().getId() + " " + getIdCode();
    }

    /**
     * {@inheritDoc
     */
    @Override
    public boolean isThisSource(DataObject sourceContainer, Category category) {
        return false;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public Period getDestinationContainer() {
        return super.getDestinationContainer();
    }

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public String getIdCode() {
        return idCode;
    }

    public Category getDestinationCategory() {
        return super.getDestinationCategory();
    }

    @Override
    public Statement getSourceContainer() {
        return super.getSourceContainer();
    }


    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @SetterProperties(sourceMethod = "getCategories")
    public void setDestinationCategory(Category category) {
        super.setDestinationCategory(category);
    }
}
