package com.ntankard.Tracking.DataBase.Core.Transfers;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank;
import com.ntankard.Tracking.DataBase.Core.SupportObjects.Currency;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;
import static com.ntankard.ClassExtension.MemberProperties.TRACE_DISPLAY;

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
    @DisplayProperties(order = 7)
    public Bank getSource() {
        return super.getSource();
    }

    @Override
    @DisplayProperties(order = 8)
    public Bank getDestination() {
        return super.getDestination();
    }

    @Override
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 9)
    public Currency getCurrency() {
        return getSource().getCurrency();
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
