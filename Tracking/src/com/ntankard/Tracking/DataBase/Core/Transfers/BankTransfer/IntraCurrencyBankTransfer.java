package com.ntankard.Tracking.DataBase.Core.Transfers.BankTransfer;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY;

@ClassExtensionProperties(includeParent = true)
public class IntraCurrencyBankTransfer extends BankTransfer {

    // My values
    private Double sourceValue;
    private Double destinationValue;

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getDescription", "getSourceValue", "getDestinationValue", "getPeriod", "getSource", "getDestination"})
    public IntraCurrencyBankTransfer(Integer id, String description, Double sourceValue, Double destinationValue, Period period, Bank source, Bank destination) {
        super(id, description, -1.0, period, source, destination);
        if (source.equals(destination)) throw new IllegalArgumentException("Source and destination are the same");
        if (source.getCurrency().equals(destination.getCurrency()))
            throw new IllegalArgumentException("Currencies are not different");

        this.sourceValue = sourceValue;
        this.destinationValue = destinationValue;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @SuppressWarnings("SuspiciousMethodCalls")
    public <T extends DataObject> List<T> sourceOptions(Class<T> type, String fieldName) {
        if (fieldName.equals("Destination")) {
            List<T> toReturn = new ArrayList<>(super.sourceOptions(type, fieldName));
            toReturn.removeAll(getSource().getCurrency().getChildren(Bank.class));
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

    @Override
    @DisplayProperties(order = 1400000, dataType = CURRENCY)
    public Double getSourceValue() {
        return sourceValue;
    }

    // 1500000----getSourceCurrency
    // 1600000----getDestination

    @Override
    @DisplayProperties(order = 1700000, dataType = CURRENCY)
    public Double getDestinationValue() {
        return destinationValue;
    }

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
        if (getSource().getCurrency().equals(destination.getCurrency()))
            throw new IllegalArgumentException("Can not set a destination with a the same currency to source");
        super.setDestination(destination);
    }

    public void setSourceValue(Double sourceValue) {
        if (sourceValue == null) throw new IllegalArgumentException("SourceValue is null");
        this.sourceValue = sourceValue;
    }

    public void setDestinationValue(Double destinationValue) {
        if (destinationValue == null) throw new IllegalArgumentException("DestinationValue is null");
        this.destinationValue = destinationValue;
    }
}
