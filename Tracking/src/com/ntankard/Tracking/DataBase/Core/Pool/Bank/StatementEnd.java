package com.ntankard.Tracking.DataBase.Core.Pool.Bank;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.CurrencyBound;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.Ordered;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY;
import static com.ntankard.ClassExtension.MemberProperties.*;

@ClassExtensionProperties(includeParent = true)
public class StatementEnd extends DataObject implements CurrencyBound, Ordered {

    // My parents
    private Period period;
    private Bank bank;

    // My values
    private Double end;

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getPeriod", "getBank", "getEnd"})
    public StatementEnd(Integer id, Period period, Bank bank, Double end) {
        super(id);
        if (period == null) throw new IllegalArgumentException("Period is null");
        if (bank == null) throw new IllegalArgumentException("Bank is null");
        this.period = period;
        this.bank = bank;
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
        toReturn.add(getPeriod());
        toReturn.add(getBank());
        toReturn.add(getCurrency());
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
        return getBank().getCurrency();
    }

    @Override
    @MemberProperties(verbosityLevel = TRACE_DISPLAY)
    @DisplayProperties(order = 6)
    public Integer getOrder() {
        return getBank().getOrder();
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public void setEnd(Double end) {
        this.end = end;
    }
}
