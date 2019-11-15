package com.ntankard.Tracking.DataBase.Interface.ClassExtension;

import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.CurrencyBound;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Statement;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.BankCategoryTransfer;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.BankTransfer;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank;
import com.ntankard.Tracking.DataBase.Core.StatementEnd;
import com.ntankard.Tracking.DataBase.Core.SupportObjects.Currency;
import com.ntankard.Tracking.DataBase.Interface.Set.MoneyEvent_Sets.PeriodPoolType_Set;

import static com.ntankard.ClassExtension.DisplayProperties.DataContext.ZERO_TARGET;
import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY;
import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

public class ExtendedStatement implements CurrencyBound {

    /**
     * The core statement object used for this ones calculations
     */
    private Statement statement;

    private Period period;
    private Bank bank;

    /**
     * Constructor
     */
    public ExtendedStatement(Period period, Bank bank, Statement statement) {
        this.period = period;
        this.bank = bank;
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
        if (period.getLast() == null) {
            return bank.getStart();
        }
        for (StatementEnd statementEnd : period.getLast().getChildren(StatementEnd.class)) {
            if (statementEnd.getBank().equals(bank)) {
                return statementEnd.getEnd();
            }
        }
        return -1.0;
    }

    @DisplayProperties(dataType = CURRENCY, order = 4)
    public Double getEnd() {
        for (StatementEnd statementEnd : period.getChildren(StatementEnd.class)) {
            if (statementEnd.getBank().equals(bank)) {
                return statementEnd.getEnd();
            }
        }
        return -1.0;
    }

    @DisplayProperties(dataType = CURRENCY, order = 6)
    public Double getTransferIn() {
        Double total = 0.00;
        for (BankTransfer bankTransfer : new PeriodPoolType_Set<>(period, bank, BankTransfer.class).get()) {
            if (bankTransfer.getDestination().equals(bank)) {
                total += bankTransfer.getDestinationValue();
            }
        }
        return total;
    }

    @DisplayProperties(dataType = CURRENCY, order = 5)
    public Double getTransferOut() {
        Double total = 0.00;
        for (BankTransfer bankTransfer : new PeriodPoolType_Set<>(period, bank, BankTransfer.class).get()) {
            if (bankTransfer.getSource().equals(bank)) {
                total -= bankTransfer.getSourceValue();
            }
        }
        return total;
    }

    @Override
    @DisplayProperties(order = 7)
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
        return -(getEnd() - getStart() - getNetTransfer());
    }

    /**
     * Get the net transfer in and out of the account
     *
     * @return The net transfer in and out of the account
     */
    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
    @DisplayProperties(dataType = CURRENCY, order = 10)
    public Double getNetTransfer() {
        return getTransferIn() - getTransferOut();
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
        for (BankCategoryTransfer t : new PeriodPoolType_Set<>(period, bank, BankCategoryTransfer.class).get()) {
            sum += t.getValue();
        }
        return sum;
    }
}
