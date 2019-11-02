package com.ntankard.Tracking.DataBase.Core.MoneyEvents.FundChargeTransfer;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.DataObject;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Fund;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.MoneyEvent;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Currency;

@ClassExtensionProperties(includeParent = true)
public class FundChargeTransfer extends MoneyEvent<Period, DataObject, Fund, DataObject> {

    /**
     * Constructor
     */
    public FundChargeTransfer(String id, String description, Double value, Period sourceContainer, Fund destinationContainer, Currency currency) {
        super(id, description, value, sourceContainer, null, destinationContainer, null, currency);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @Override
    @DisplayProperties(order = 5)
    public Period getSourceContainer() {
        return super.getSourceContainer();
    }

    @Override
    @DisplayProperties(order = 7)
    public Fund getDestinationContainer() {
        return super.getDestinationContainer();
    }


    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @SetterProperties(sourceMethod = "getData")
    @Override
    public void setSourceContainer(Period source) {
        super.setSourceContainer(source);
    }

    @SetterProperties(sourceMethod = "getData")
    @Override
    public void setDestinationContainer(Fund destination) {
        super.setDestinationContainer(destination);
    }
}
