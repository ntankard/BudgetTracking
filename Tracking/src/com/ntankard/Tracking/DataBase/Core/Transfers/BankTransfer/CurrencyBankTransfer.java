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
        if (period == null) throw new IllegalArgumentException("Period is null");
        if (source == null) throw new IllegalArgumentException("Source is null");
        if (destination == null) throw new IllegalArgumentException("Destination is null");
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
