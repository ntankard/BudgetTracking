package com.ntankard.Tracking.DataBase.Core;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.CurrencyBound;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.Ordered;
import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY;
import static com.ntankard.ClassExtension.MemberProperties.*;

@ClassExtensionProperties(includeParent = true)
public class StatementEnd extends DataObject implements CurrencyBound, Ordered {

    // My parents
    private ExistingPeriod period;
    private Bank bank;

    // My values
    private Double end;

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getPeriod", "getBank", "getEnd"})
    public StatementEnd(Integer id, ExistingPeriod period, Bank bank, Double end) {
        super(id);
        if (period == null) throw new IllegalArgumentException("Period is null");
        if (bank == null) throw new IllegalArgumentException("Bank is null");
        if (end == null) throw new IllegalArgumentException("End is null");
        this.period = period;
        this.bank = bank;
        this.end = end;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 2000000)
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

    // 1000000--getID

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 1100000)
    public ExistingPeriod getPeriod() {
        return period;
    }

    @DisplayProperties(order = 1200000)
    public Bank getBank() {
        return bank;
    }

    @DisplayProperties(order = 1300000, dataType = CURRENCY)
    public Double getEnd() {
        return end;
    }

    @Override
    @DisplayProperties(order = 1400000)
    public Currency getCurrency() {
        return getBank().getCurrency();
    }

    @Override
    @MemberProperties(verbosityLevel = TRACE_DISPLAY)
    @DisplayProperties(order = 1500000)
    public Integer getOrder() {
        return getBank().getOrder() + getPeriod().getOrder() * 1000;
    }

    // 2000000--getParents (Above)
    // 3000000--getChildren

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public void setEnd(Double end) {
        if (end == null) throw new IllegalArgumentException("Bank is null");
        this.end = end;
    }
}
