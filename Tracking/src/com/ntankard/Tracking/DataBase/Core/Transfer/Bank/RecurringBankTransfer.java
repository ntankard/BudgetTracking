package com.ntankard.Tracking.DataBase.Core.Transfer.Bank;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.DataObject_Field;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Dependant_Field;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Field;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Core.RecurringPayment.FixedRecurringPayment;

import java.util.List;

@ClassExtensionProperties(includeParent = true)
public class RecurringBankTransfer extends BankTransfer {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static List<Field<?>> getFields() {
        List<Field<?>> toReturn = BankTransfer.getFields();

        Field<?> parentPaymentField = new DataObject_Field<>("getParentPayment", FixedRecurringPayment.class);
        toReturn.add(parentPaymentField);

        // TODO THIS IS BROKEN, it will work if you change what recuring payment its linked to but NOT if the value of that field changes. So if you change the name it dose not work, you need to add aditional subscriptions
        toReturn.remove(makeFieldMap(toReturn).get("getDescription"));
        toReturn.add(new Dependant_Field<>("getDescription", String.class, parentPaymentField, new Dependant_Field.Extractor<String, FixedRecurringPayment>() {
            @Override
            public String extract(FixedRecurringPayment value) {
                return value.getName();
            }
        }));


        return toReturn;
    }

    /**
     * Create a new RePayFundTransfer object
     */
    public static RecurringBankTransfer make(Integer id,
                                             Period period, Bank source, Double value,
                                             Period destinationPeriod, Pool destination, Double destinationValue,
                                             FixedRecurringPayment parentPayment) {
        return assembleDataObject(RecurringBankTransfer.getFields(), new RecurringBankTransfer()
                , "getId", id
                , "getPeriod", period
                , "getSource", source
                , "getValue", value
                , "getDestinationPeriod", destinationPeriod
                , "getDestination", destination
                , "getDestinationValue", destinationValue
                , "getParentPayment", parentPayment
        );
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    // 1000000--getID

    @Override
    @DisplayProperties(order = 1100000)
    public String getDescription() {
        return get("getDescription");
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
        return get("getParentPayment");
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
