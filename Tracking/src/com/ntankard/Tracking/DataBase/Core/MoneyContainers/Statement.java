package com.ntankard.Tracking.DataBase.Core.MoneyContainers;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.CurrencyBound;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.Transaction;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Bank;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Currency;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.DisplayProperties.DataContext.ZERO_TARGET;
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
    //##################################### Calculated accessors, from base data #######################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get the real spend based on the difference between the starting and ending balance
     *
     * @return The real spend based on the difference between the starting and ending balance
     */
    @DisplayProperties(dataType = CURRENCY, order = 7)
    public Double getExpectedSpend() {
        return -(end - start - getNetTransfer());
    }

    /**
     * Get the net transfer in and out of the account
     *
     * @return The net transfer in and out of the account
     */
    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
    @DisplayProperties(dataType = CURRENCY, order = 10)
    public Double getNetTransfer() {
        return transferIn - transferOut;
    }

    //------------------------------------------------------------------------------------------------------------------
    //###################################### Calculated accessors, from children #######################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get the amount that differs between the Total Spend and the Expected spend. If this is not 0 money is missing
     *
     * @return The amount that differs between the Total Spend and the Expected spend
     */
    @DisplayProperties(dataType = CURRENCY, dataContext = ZERO_TARGET, order = 9)
    public Double getMissingSpend() {
        double val = getExpectedSpend() - getTotalSpend();
        if (Math.abs(val) < 0.001) {
            val = 0.0;
        }
        return val;
    }

    /**
     * Get the sum of all transactions for this statement
     *
     * @return The sum of all transactions for this statement
     */
    @DisplayProperties(dataType = CURRENCY, order = 8)
    public Double getTotalSpend() {
        double sum = 0.0;
        for (Transaction t : this.<Transaction>getChildren(Transaction.class)) {
            sum += t.getValue();
        }
        return sum;
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
