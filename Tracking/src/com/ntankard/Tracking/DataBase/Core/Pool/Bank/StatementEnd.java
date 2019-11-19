package com.ntankard.Tracking.DataBase.Core.Pool.Bank;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.CurrencyBound;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY;
import static com.ntankard.ClassExtension.MemberProperties.DEBUG_DISPLAY;
import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class StatementEnd extends DataObject implements CurrencyBound {

    // My parents
    private Period period;
    private Bank bank;
    private Currency currency;

    // My values
    private Double end;

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getPeriod", "getBank", "getCurrency", "getEnd"})
    public StatementEnd(Integer id, Period period, Bank bank, Currency currency, Double end) {
        super(id);
        this.period = period;
        this.bank = bank;
        this.currency = currency;
        this.end = end;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 21)
    public List<DataObject> getParents() {
        List<DataObject> toReturn = new ArrayList<>();
        toReturn.add(period);
        toReturn.add(bank);
        toReturn.add(currency);
        return toReturn;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 2)
    public Period getPeriod() {
        return period;
    }

    @DisplayProperties(order = 3)
    public Bank getBank() {
        return bank;
    }

    @DisplayProperties(order = 4, dataType = CURRENCY)
    public Double getEnd() {
        return end;
    }

    @Override
    @DisplayProperties(order = 5)
    public Currency getCurrency() {
        return currency;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public void setEnd(Double end) {
        this.end = end;
    }
}
