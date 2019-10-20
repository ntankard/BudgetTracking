package com.ntankard.Tracking.DataBase.Core.MoneyEvents;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.DynamicGUI.Components.Object.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Fund;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Category;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Currency;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.FundEvent;

@ClassExtensionProperties(includeParent = true)
public class FundChargeTransfer extends MoneyEvent<Period, Category, Fund, FundEvent> {

    /**
     * Constructor
     */
    public FundChargeTransfer(String id, Period source, Fund destination, Currency currency, String description, Double value) {
        super(id, description, value, source, null, destination, null, currency);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @DisplayProperties(order = 2)
    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
    @Override
    public Period getSourceContainer() {
        return super.getSourceContainer();
    }

    @DisplayProperties(order = 4)
    @Override
    public Fund getDestinationContainer() {
        return super.getDestinationContainer();
    }


    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @SetterProperties(sourceMethod = "getPeriods")
    @Override
    public void setSourceContainer(Period source) {
        super.setSourceContainer(source);
    }

    @SetterProperties(sourceMethod = "getFunds")
    @Override
    public void setDestinationContainer(Fund destination) {
        super.setDestinationContainer(destination);
    }
}
