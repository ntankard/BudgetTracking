package com.ntankard.Tracking.DataBase.Core.Transfers.BankTransfer;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;
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
        this.sourceValue = sourceValue;
        this.destinationValue = destinationValue;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @SuppressWarnings({"SuspiciousMethodCalls", "unchecked"})
    public <T extends DataObject> List<T> sourceOptions(Class<T> type, String fieldName) {
        if (fieldName.equals("Destination")) {
            List<T> toReturn = new ArrayList<>();
            for (Bank bank : super.sourceOptions((Class<Bank>) type, fieldName)) {
                if (!getSource().getCurrency().equals(bank.getCurrency())) {
                    toReturn.add((T) bank);
                }
            }
            return toReturn;
        }
        if (fieldName.equals("Source")) {
            List<T> toReturn = new ArrayList<>(super.sourceOptions(type, fieldName));
            toReturn.remove(getDestination());
            return toReturn;
        }
        return super.sourceOptions(type, fieldName);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @Override
    @MemberProperties(shouldDisplay = false)
    @DisplayProperties(order = 4, dataType = CURRENCY)
    public Double getValue() {
        throw new RuntimeException("Direct value access not supported");
    }

    @Override
    @MemberProperties(shouldDisplay = false)
    @DisplayProperties(order = 5)
    public Currency getCurrency() {
        throw new RuntimeException("Direct value access not supported");
    }

    @Override
    @DisplayProperties(order = 7, dataType = CURRENCY)
    public Double getSourceValue() {
        return sourceValue;
    }

    @Override
    @DisplayProperties(order = 10, dataType = CURRENCY)
    public Double getDestinationValue() {
        return destinationValue;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public void setSourceValue(Double sourceValue) {
        this.sourceValue = sourceValue;
    }

    public void setDestinationValue(Double destinationValue) {
        this.destinationValue = destinationValue;
    }
}
