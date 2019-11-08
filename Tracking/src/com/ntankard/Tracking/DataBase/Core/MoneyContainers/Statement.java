package com.ntankard.Tracking.DataBase.Core.MoneyContainers;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.CurrencyBound;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank;
import com.ntankard.Tracking.DataBase.Core.SupportObjects.Currency;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY;
import static com.ntankard.ClassExtension.MemberProperties.DEBUG_DISPLAY;
import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class Statement extends DataObject implements CurrencyBound {

    // My parents
    private Bank bank;
    private Period period;

    // My values
    private Double start;
    private Double end;
    private Double transferIn;
    private Double transferOut;

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getBank", "getPeriod", "getStart", "getEnd", "getTransferIn", "getTransferOut"})
    public Statement(String id, Bank bank, Period period, Double start, Double end, Double transferIn, Double transferOut) {
        super(id);
        this.bank = bank;
        this.period = period;
        this.start = start;
        this.end = end;
        this.transferIn = transferIn;
        this.transferOut = transferOut;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public String toString() {
        return bank.toString();
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 21)
    public List<DataObject> getParents() {
        List<DataObject> toReturn = new ArrayList<>();
        toReturn.add(bank);
        toReturn.add(period);
        return toReturn;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public Currency getCurrency() {
        return bank.getCurrency();
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @DisplayProperties(order = 2)
    public Bank getBank() {
        return bank;
    }

    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
    @DisplayProperties(order = 11)
    public Period getPeriod() {
        return period;
    }

    @DisplayProperties(dataType = CURRENCY, order = 3)
    public Double getStart() {
        return start;
    }

    @DisplayProperties(dataType = CURRENCY, order = 4)
    public Double getEnd() {
        return end;
    }

    @DisplayProperties(dataType = CURRENCY, order = 6)
    public Double getTransferIn() {
        return transferIn;
    }

    @DisplayProperties(dataType = CURRENCY, order = 5)
    public Double getTransferOut() {
        return transferOut;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public void setStart(Double start) {
        this.start = start;
    }

    public void setEnd(Double end) {
        this.end = end;
    }

    public void setTransferIn(Double transferIn) {
        this.transferIn = transferIn;
    }

    public void setTransferOut(Double transferOut) {
        this.transferOut = transferOut;
    }
}
