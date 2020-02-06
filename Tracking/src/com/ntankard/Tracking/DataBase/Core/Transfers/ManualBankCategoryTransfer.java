package com.ntankard.Tracking.DataBase.Core.Transfers;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

@ClassExtensionProperties(includeParent = true)
public class ManualBankCategoryTransfer extends BankCategoryTransfer {

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getDescription", "getDestinationValue", "getPeriod", "getSource", "getDestination"})
    public ManualBankCategoryTransfer(Integer id, String description, Double value, Period period, Bank source, Category destination) {
        super(id, description, value, period, source, destination);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @Override
    @SetterProperties(localSourceMethod = "sourceOptions")
    public void setSource(Bank source) {
        super.setSource(source);
    }

    @Override
    @SetterProperties(localSourceMethod = "sourceOptions")
    public void setDestination(Category destination) {
        super.setDestination(destination);
    }
}
