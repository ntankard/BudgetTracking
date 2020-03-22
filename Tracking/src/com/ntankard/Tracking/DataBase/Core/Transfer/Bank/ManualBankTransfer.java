package com.ntankard.Tracking.DataBase.Core.Transfer.Bank;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Field;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;

import java.util.List;

@ClassExtensionProperties(includeParent = true)
public class ManualBankTransfer extends BankTransfer {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static List<Field<?>> getFields() {
        return BankTransfer.getFields();
    }

    /**
     * Create a new RePayFundTransfer object
     */
    public static ManualBankTransfer make(Integer id, String description,
                                          Period period, Bank source, Double value,
                                          Period destinationPeriod, Pool destination, Double destinationValue) {
        return assembleDataObject(ManualBankTransfer.getFields(), new ManualBankTransfer()
                , "getId", id
                , "getDescription", description
                , "getPeriod", period
                , "getSource", source
                , "getValue", value
                , "getDestinationPeriod", destinationPeriod
                , "getDestination", destination
                , "getDestinationValue", destinationValue
        );
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    // 1000000--getID
    // 1100000----getDescription
    // 1200000----getPeriod
    // 1300000----getSource
    // 1400000----getValue
    // 1500000----getCurrency
    // 1510000------getDestinationPeriod
    // 1520000------getCategory
    // 1530000------getBank
    // 1540000------getFundEvent
    // 1600000----getDestination
    // 1610000------getDestinationValue
    // 1620000------getDestinationCurrency
    // 1700000----getSourceTransfer
    // 1800000----getDestinationTransfer
    // 2000000--getParents (Above)
    // 3000000--getChildren
}
