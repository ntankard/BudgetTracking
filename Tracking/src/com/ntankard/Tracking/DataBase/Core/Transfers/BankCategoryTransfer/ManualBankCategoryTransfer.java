package com.ntankard.Tracking.DataBase.Core.Transfers.BankCategoryTransfer;

import com.ntankard.ClassExtension.ClassExtensionProperties;
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
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    // 1000000--getID
    // 1100000----getPeriod
    // 1200000----getDescription
    // 1300000----getSource
    // 1400000----getSourceValue
    // 1500000----getSourceCurrency
    // 1600000----getDestination
    // 1700000----getDestinationValue
    // 1800000----getDestinationCurrency
    // 1810000------getOrder
    // 2000000--getParents
    // 3000000--getChildren

}
