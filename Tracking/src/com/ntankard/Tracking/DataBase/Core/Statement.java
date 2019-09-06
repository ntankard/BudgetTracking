package com.ntankard.Tracking.DataBase.Core;

import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.ntankard.ClassExtension.DisplayProperties.*;
import static com.ntankard.ClassExtension.DisplayProperties.DataContext.*;
import static com.ntankard.ClassExtension.DisplayProperties.DataType.*;

public class Statement {

    // My parents
    private Bank idBank;
    private Period idPeriod;

    // My values
    private Double start;
    private Double end;
    private Double transferIn;
    private Double transferOut;

    // My Children
    private List<Transaction> transactions = new ArrayList<>();

    /**
     * Constructor
     */
    public Statement(Bank idBank, Period idPeriod, Double start, Double end, Double transferIn, Double transferOut) {
        this.idBank = idBank;
        this.idPeriod = idPeriod;
        this.start = start;
        this.end = end;
        this.transferIn = transferIn;
        this.transferOut = transferOut;
    }

    /**
     * Notify that another object has linked to this one
     *
     * @param transaction The object that linked
     */
    public void notifyTransactionLink(Transaction transaction) {
        transactions.add(transaction);
    }

    /**
     * Notify that another object has removed there link to this one
     *
     * @param transaction The object was linked
     */
    public void notifyTransactionLinkRemove(Transaction transaction) {
        transactions.remove(transaction);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public String toString() {
        return getId();
    }

    //------------------------------------------------------------------------------------------------------------------
    //########################################## Calculated accessors ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get the sum of all transactions for this statement
     *
     * @return The sum of all transactions for this statement
     */
    @DisplayProperties(dataType = CURRENCY, order = 6)
    public Double getTotalSpend() {
        Double sum = 0.0;
        for (Transaction t : transactions) {
            sum += t.getValue();
        }
        return sum;
    }

    /**
     * Get the real spend based on the difference between the starting and ending balance
     *
     * @return The real spend based on the difference between the starting and ending balance
     */
    @DisplayProperties(dataType = CURRENCY)
    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
    public Double getExpectedSpend() {
        return end - start - getNetTransfer();
    }

    /**
     * Get the net transfer in and out of the account
     *
     * @return The net transfer in and out of the account
     */
    @DisplayProperties(dataType = CURRENCY, order = 5)
    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
    public Double getNetTransfer() {
        return transferIn - transferOut;
    }

    /**
     * Get the amount that differs between the Total Spend and the Expected spend. If this is not 0 money is missing
     *
     * @return The amount that differs between the Total Spend and the Expected spend
     */
    @DisplayProperties(dataType = CURRENCY, dataContext = ZERO_TARGET)
    public Double getMissingSpend() {
        Double val = getExpectedSpend() + getTotalSpend();
        if (Math.abs(val) < 0.001) {
            val = 0.0;
        }
        return val;
    }

    /**
     * Get all values for a given category
     *
     * @param category    The category to check
     * @param sumChildren Should you sum the children as well?
     * @return The total values
     */
    public double getCategoryTotal(Category category, boolean sumChildren) {
        double total = 0;
        if (sumChildren) {
            for (Category child : category.getCategories()) {
                total += getCategoryTotal(child, true);
            }
        }
        for (Transaction t : transactions) {
            if (t.getCategory().equals(category)) {
                total += t.getValue();
            }
        }
        return total;
    }

    //------------------------------------------------------------------------------------------------------------------
    //########################################### Standard accessors ###################################################
    //------------------------------------------------------------------------------------------------------------------

    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
    public String getId() {
        return idBank.getId() + " " + idPeriod.getId();
    }

    @DisplayProperties(order = 0)
    public Bank getIdBank() {
        return idBank;
    }

    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
    public Period getIdPeriod() {
        return idPeriod;
    }

    @DisplayProperties(dataType = CURRENCY, order = 1)
    public Double getStart() {
        return start;
    }

    @DisplayProperties(dataType = CURRENCY, order = 2)
    public Double getEnd() {
        return end;
    }

    @DisplayProperties(dataType = CURRENCY, order = 4)
    public Double getTransferIn() {
        return transferIn;
    }

    @DisplayProperties(dataType = CURRENCY, order = 3)
    public Double getTransferOut() {
        return transferOut;
    }

    @MemberProperties(verbosityLevel = MemberProperties.TRACE_DISPLAY)
    public List<Transaction> getTransactions() {
        return transactions;
    }

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