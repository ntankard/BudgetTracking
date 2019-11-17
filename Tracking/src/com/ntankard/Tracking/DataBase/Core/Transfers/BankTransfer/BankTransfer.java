package com.ntankard.Tracking.DataBase.Core.Transfers.BankTransfer;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank;
import com.ntankard.Tracking.DataBase.Core.Transfers.Transfer;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY;
import static com.ntankard.ClassExtension.MemberProperties.*;

@ClassExtensionProperties(includeParent = true)
public class BankTransfer extends Transfer<Bank, Bank> {

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getDescription", "getValue", "getPeriod", "getSource", "getDestination"})
    public BankTransfer(Integer id, String description, Double value, Period period, Bank source, Bank destination) {
        super(id, description, value, period, source, destination, null);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @Override
    @MemberProperties(verbosityLevel = TRACE_DISPLAY)
    @DisplayProperties(order = 3)
    public String getDescription() {
        return super.getDescription();
    }

    @Override
    @DisplayProperties(order = 4, dataType = CURRENCY)
    public Double getValue() {
        return super.getValue();
    }

    @Override
    @DisplayProperties(order = 5)
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public Currency getCurrency() {
        return getSource().getCurrency();
    }

    @Override
    @DisplayProperties(order = 6)
    public Bank getSource() {
        return super.getSource();
    }

    @Override
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 8)
    public Currency getSourceCurrency() {
        return super.getSource().getCurrency();
    }

    @Override
    @DisplayProperties(order = 9)
    public Bank getDestination() {
        return super.getDestination();
    }

    @Override
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 11)
    public Currency getDestinationCurrency() {
        return getDestination().getCurrency();
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @Override
    @SetterProperties(sourceMethod = "getData")
    public void setSource(Bank source) {
        super.setSource(source);
    }

    @Override
    @SetterProperties(sourceMethod = "getData")
    public void setDestination(Bank destination) {
        super.setDestination(destination);
    }
}
