package com.ntankard.Tracking.DataBase.Interface.ClassExtension;

import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Statement;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.Transaction;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Bank;

import static com.ntankard.ClassExtension.DisplayProperties.DataContext.ZERO_TARGET;
import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY;
import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

public class ExtendedStatement {

    /**
     * The core statement object used for this ones calculations
     */
    private Statement statement;

    /**
     * Constructor
     */
    public ExtendedStatement(Statement statement) {
        this.statement = statement;
    }

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 1)
    public Statement getStatement() {
        return statement;
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################### Direct Access ####################################################
    //------------------------------------------------------------------------------------------------------------------

    @DisplayProperties(order = 2)
    public Bank getBank() {
        return statement.getBank();
    }

    @DisplayProperties(dataType = CURRENCY, order = 3)
    public Double getStart() {
        return statement.getStart();
    }

    @DisplayProperties(dataType = CURRENCY, order = 4)
    public Double getEnd() {
        return statement.getEnd();
    }

    @DisplayProperties(dataType = CURRENCY, order = 6)
    public Double getTransferIn() {
        return statement.getTransferIn();
    }

    @DisplayProperties(dataType = CURRENCY, order = 5)
    public Double getTransferOut() {
        return statement.getTransferOut();
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
        return -(statement.getEnd() - statement.getStart() - getNetTransfer());
    }

    /**
     * Get the net transfer in and out of the account
     *
     * @return The net transfer in and out of the account
     */
    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
    @DisplayProperties(dataType = CURRENCY, order = 10)
    public Double getNetTransfer() {
        return statement.getTransferIn() - statement.getTransferOut();
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
        for (Transaction t : statement.getChildren(Transaction.class)) {
            sum += t.getValue();
        }
        return sum;
    }
}
