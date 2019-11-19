package com.ntankard.Tracking.DataBase.Core.Transfers.BankTransfer;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank;
import com.ntankard.Tracking.DataBase.Core.Transfers.Transfer;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY;
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
    @SetterProperties(localSourceMethod = "sourceOptions")
    public void setSource(Bank source) {
        this.source.notifyChildUnLink(this);
        this.source = source;

        List<Bank> options = sourceOptions(Bank.class, "Destination");
        if (getSource().equals(getDestination()) || !options.contains(getDestination())) {
            setDestination(options.get(0));
        }

        this.source.notifyChildLink(this);
    }

    @Override
    @SetterProperties(localSourceMethod = "sourceOptions")
    public void setDestination(Bank destination) {
        super.setDestination(destination);
    }
}
