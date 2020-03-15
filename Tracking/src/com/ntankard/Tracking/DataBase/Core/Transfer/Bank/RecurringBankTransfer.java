package com.ntankard.Tracking.DataBase.Core.Transfer.Bank;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.DataObject_Field;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Field;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Core.RecurringPayment.FixedRecurringPayment;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import java.util.List;

@ClassExtensionProperties(includeParent = true)
public class RecurringBankTransfer extends BankTransfer {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static List<Field<?>> getFields(Integer id,
                                           Period period, Bank source, Double value,
                                           Period destinationPeriod, Pool destination, Double destinationValue,
                                           FixedRecurringPayment parentPayment,
                                           DataObject container) {
        List<Field<?>> toReturn = BankTransfer.getFields(id, "NOT USED", period, source, value, destinationPeriod, destination, destinationValue, container);
        toReturn.add(new DataObject_Field<>("parentPayment", FixedRecurringPayment.class, parentPayment, container));
        return toReturn;
    }

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getPeriod", "getSource", "getValue", "getDestinationPeriod", "getDestination", "getDestinationValue", "getParentPayment"})
    public RecurringBankTransfer(Integer id,
                                 Period period, Bank source, Double value,
                                 Period destinationPeriod, Pool destination, Double destinationValue,
                                 FixedRecurringPayment parentPayment) {
        super();
        setFields(getFields(id, period, source, value, destinationPeriod, destination, destinationValue, parentPayment, this));
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    // 1000000--getID

    @Override
    @DisplayProperties(order = 1100000)
    public String getDescription() {
        return getParentPayment().getName();
    }

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

    @DisplayProperties(order = 1621000)
    public FixedRecurringPayment getParentPayment() {
        return get("parentPayment");
    }

    // 1700000----getSourceTransfer
    // 1800000----getDestinationTransfer
    // 2000000--getParents (Above)
    // 3000000--getChildren

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @SetterProperties(localSourceMethod = "sourceOptions")
    public void setSource(Pool source) {
        super.setSource(source);
    }
}
