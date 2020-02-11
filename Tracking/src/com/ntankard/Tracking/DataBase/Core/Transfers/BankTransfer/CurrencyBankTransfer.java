package com.ntankard.Tracking.DataBase.Core.Transfers.BankTransfer;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import java.util.List;

@ClassExtensionProperties(includeParent = true)
public class CurrencyBankTransfer extends BankTransfer {

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getDescription", "getDestinationValue", "getPeriod", "getSource", "getDestination"})
    public CurrencyBankTransfer(Integer id, String description, Double value, Period period, Bank source, Bank destination) {
        super(id, description, value, period, source, destination);
        if (source.equals(destination)) throw new IllegalArgumentException("Source and destination are the same");
        if (!source.getCurrency().equals(destination.getCurrency()))
            throw new IllegalArgumentException("Currencies are not the same");
    }

    /**
     * {@inheritDoc
     */
    @Override
    @SuppressWarnings("SuspiciousMethodCalls")
    public <T extends DataObject> List<T> sourceOptions(Class<T> type, String fieldName) {
        if (fieldName.equals("Destination")) {
            List<T> toReturn = getSource().getCurrency().getChildren(type);
            toReturn.remove(getSource());
            return toReturn;
        }

        return super.sourceOptions(type, fieldName);
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
    // 2000000--getParents
    // 3000000--getChildren

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @Override
    @SetterProperties(localSourceMethod = "sourceOptions")
    public void setDestination(Bank destination) {
        if (destination == null) throw new IllegalArgumentException("Destination is null");
        if (getSource().equals(destination))
            throw new IllegalArgumentException("Can not set the source as the destination");
        if (!getSource().getCurrency().equals(destination.getCurrency()))
            throw new IllegalArgumentException("Can not set a destination with a different currency to source");
        super.setDestination(destination);
    }
}
