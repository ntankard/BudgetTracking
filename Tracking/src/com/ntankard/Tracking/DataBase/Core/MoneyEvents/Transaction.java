package com.ntankard.Tracking.DataBase.Core.MoneyEvents;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.DynamicGUI.Components.Object.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.Base.DataObject;
import com.ntankard.Tracking.DataBase.Core.Base.MoneyEvent;
import com.ntankard.Tracking.DataBase.Core.Category;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Statement;

import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class Transaction extends MoneyEvent<Period, Period> {

    // My parents

    // My values
    private String idCode;
    private Statement idStatement;

    /**
     * Constructor
     */
    public Transaction(Statement idStatement, String idCode, String description, Double value, Category category) {
        super(description, value, null, null, idStatement.getIdPeriod(), category, idStatement.getIdBank().getCurrency());
        this.idStatement = idStatement;
        this.idCode = idCode;

    }

    /**
     * {@inheritDoc
     */
    @Override
    public List<DataObject> getParents() {
        List<DataObject> toReturn = super.getParents();
        toReturn.add(idStatement);
        return toReturn;

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

    public Statement getIdStatement() {
        return idStatement;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @SetterProperties(sourceMethod = "getCategories")
    public void setDestinationCategory(Category category) {
        super.setDestinationCategory(category);
    }
}
