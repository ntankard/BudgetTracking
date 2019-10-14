package com.ntankard.Tracking.DataBase.Core.MoneyContainers;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.DataObject;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.Transaction;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Bank;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.DisplayProperties.DataContext.ZERO_TARGET;
import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY;
import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class Statement extends MoneyContainer {

    // My parents
    private Bank idBank;
    private Period idPeriod;

    // My values
    private Double start;
    private Double end;
    private Double transferIn;
    private Double transferOut;

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
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
    public String getId() {
        return getIdBank().getId() + " " + getIdPeriod().getId();
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public List<DataObject> getParents() {
        List<DataObject> toReturn = new ArrayList<>();
        toReturn.add(idBank);
        toReturn.add(idPeriod);
        return toReturn;
    }

    //------------------------------------------------------------------------------------------------------------------
    //##################################### Calculated accessors, from base data #######################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get the real spend based on the difference between the starting and ending balance
     *
     * @return The real spend based on the difference between the starting and ending balance
     */
    @DisplayProperties(dataType = CURRENCY, order = 5)
    public Double getExpectedSpend() {
        return -(end - start - getNetTransfer());
    }

    /**
     * Get the net transfer in and out of the account
     *
     * @return The net transfer in and out of the account
     */
    @DisplayProperties(dataType = CURRENCY)
    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
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
    @DisplayProperties(dataType = CURRENCY, dataContext = ZERO_TARGET, order = 7)
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
    @DisplayProperties(dataType = CURRENCY, order = 6)
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
//    /**
//     * Get all values for a given category
//     *
//     * @param category The category to check
//     * @return The total values
//     */
//    public double getCategoryTotal(Category category) {
//        double total = 0;
//        for (Transaction t : this.<Transaction>getChildren(Transaction.class)) {
//            if (t.getDestinationCategory().equals(category)) {
//                total += t.getValue();
//            }
//        }
//        return total;
//    }
