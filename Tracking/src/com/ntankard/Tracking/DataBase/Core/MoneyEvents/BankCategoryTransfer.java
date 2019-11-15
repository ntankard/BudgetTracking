package com.ntankard.Tracking.DataBase.Core.MoneyEvents;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Core.SupportObjects.Currency;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class BankCategoryTransfer extends MoneyEvent<Bank, DataObject, Category, DataObject> {

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getDescription", "getValue", "getPeriod", "getSource", "getDestination"})
    public BankCategoryTransfer(Integer id, String description, Double value, Period period, Bank source, Category destination) {
        super(id, description, value, period, source, null, destination, null, null);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @Override
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 3)
    public String getDescription() {
        return super.getDescription();
    }

    @DisplayProperties(order = 5)
    public Bank getSource() {
        return super.getSourceContainer();
    }

    @DisplayProperties(order = 7)
    public Category getDestination() {
        return super.getDestinationContainer();
    }

    @Override
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 11)
    public Currency getCurrency() {
        return getSource().getCurrency();
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @SetterProperties(sourceMethod = "getData")
    public void setSource(Bank sourceContainer) {
        super.setSourceContainer(sourceContainer);
    }

    @SetterProperties(sourceMethod = "getData")
    public void setDestination(Category destinationContainer) {
        super.setDestinationContainer(destinationContainer);
    }
}
