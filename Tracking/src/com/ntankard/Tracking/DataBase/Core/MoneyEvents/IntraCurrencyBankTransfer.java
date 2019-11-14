package com.ntankard.Tracking.DataBase.Core.MoneyEvents;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY;
import static com.ntankard.ClassExtension.MemberProperties.TRACE_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class IntraCurrencyBankTransfer extends BankTransfer {

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

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @Override
    @MemberProperties(verbosityLevel = TRACE_DISPLAY)
    @DisplayProperties(order = 4, dataType = CURRENCY)
    public Double getValue() {
        return super.getValue();
    }

    @Override
    @DisplayProperties(order = 5, dataType = CURRENCY)
    public Double getSourceValue() {
        return -sourceValue;
    }

    @Override
    @DisplayProperties(order = 6, dataType = CURRENCY)
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
