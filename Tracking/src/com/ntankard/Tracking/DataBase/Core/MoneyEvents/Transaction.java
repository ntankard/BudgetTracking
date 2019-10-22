package com.ntankard.Tracking.DataBase.Core.MoneyEvents;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.DataObject;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Statement;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Category;

import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class Transaction extends MoneyEvent<Statement, Category, Period, Category> {

    /**
     * Constructor
     */
    public Transaction(Statement idStatement, String id, String description, Double value, Category category) {
        super(id, description, value, idStatement, null, idStatement.getIdPeriod(), category, idStatement.getIdBank().getCurrency());
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
    @Override
    public Period getDestinationContainer() {
        return super.getDestinationContainer();
    }

    @Override
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

    @SetterProperties(sourceMethod = "getData")
    public void setDestinationCategory(Category category) {
        super.setDestinationCategory(category);
    }
}
