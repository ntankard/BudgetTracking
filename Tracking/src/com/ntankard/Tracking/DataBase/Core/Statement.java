package com.ntankard.Tracking.DataBase.Core;

import java.util.ArrayList;
import java.util.List;

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
    public Double getExpectedSpend() {
        return end - start;
    }

    /**
     * Get the amount that differs between the Total Spend and the Expected spend. If this is not 0 money is missing
     *
     * @return The amount that differs between the Total Spend and the Expected spend
     */
    public Double getMissingSpend() {
        return getExpectedSpend() - getTotalSpend();
    }

    //------------------------------------------------------------------------------------------------------------------
    //########################################### Standard accessors ###################################################
    //------------------------------------------------------------------------------------------------------------------

    public String getId() {
        return idBank.getId() + " " + idPeriod.getId();
    }

    public Bank getIdBank() {
        return idBank;
    }

    public Period getIdPeriod() {
        return idPeriod;
    }

    public Double getStart() {
        return start;
    }

    public Double getEnd() {
        return end;
    }

    public Double getTransferIn() {
        return transferIn;
    }

    public Double getTransferOut() {
        return transferOut;
    }

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