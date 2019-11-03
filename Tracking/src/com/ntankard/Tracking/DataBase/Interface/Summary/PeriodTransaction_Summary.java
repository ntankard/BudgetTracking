package com.ntankard.Tracking.DataBase.Interface.Summary;

import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyCategory.Category;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Statement;
import com.ntankard.Tracking.DataBase.Core.SupportObjects.Currency;
import com.ntankard.Tracking.DataBase.Interface.ClassExtension.ExtendedPeriod;
import com.ntankard.Tracking.DataBase.Interface.ClassExtension.ExtendedStatement;
import com.ntankard.Tracking.DataBase.Interface.Set.MoneyEvent_Sets.ContainerCategory_Set;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;

import java.awt.*;

import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY_YEN;

public class PeriodTransaction_Summary {

    /**
     * The core data object to calculate
     */
    private Period core;

    /**
     * Constructor
     */
    public PeriodTransaction_Summary(Period core) {
        this.core = core;
    }

    /**
     * Get the hex debut that needs to be paid for the income made
     *
     * @return The hex debut that needs to be paid for the income made
     */
    @DisplayProperties(dataType = CURRENCY_YEN)
    public Double getTax() {
        return new ContainerCategory_Set(core, TrackingDatabase.get().getSpecialValue(Category.class, Category.INCOME)).getTotal() * -TrackingDatabase.get().getTaxRate();
    }

    /**
     * Get the remaining money to put into savings
     *
     * @return The remaining money to put into savings
     */
    @DisplayProperties(dataType = CURRENCY_YEN)
    public Double getSavings() {
        Double sum = 0.0;
        sum += getNonCategory();
        sum -= getTax();
        return sum;
    }

    /**
     * Get the amount on money not in a category
     *
     * @return The amount on money not in a category
     */
    @DisplayProperties(dataType = CURRENCY_YEN)
    public Double getNonCategory() {
        double sum = 0.0;
        for (Category category : TrackingDatabase.get().get(Category.class)) {
            sum += new ContainerCategory_Set(core, category).getTotal();
        }
        return -sum;
    }

    /**
     * For all Statements in the period, do all there transactions match then balance change
     *
     * @return True if all the balance is accounted for
     */
    public boolean isValidSpend() {
        boolean missing = false;
        for (Statement statement : core.getChildren(Statement.class)) {
            if (new ExtendedStatement(statement).getMissingSpend() != 0) {
                missing = true;
            }
        }
        return !missing;
    }

    /**
     * If this the first statement for this bank?
     *
     * @return True if this the first statement for this bank?
     */
    public boolean isFirst() {
        return core.getLast() == null;
    }

    /**
     * Do the start and end values for the statement match up
     *
     * @return True if the start and end values for the statement match up
     */
    public boolean isValidStatementBalance() {
        if (isFirst()) {
            return true;
        }

        for (Statement statement : core.getChildren(Statement.class)) {
            boolean found = false;
            for (Statement lastStatement : core.getLast().getChildren(Statement.class)) {
                if (statement.getBank().equals(lastStatement.getBank())) {
                    if (!statement.getStart().equals(lastStatement.getEnd())) {
                        return false;
                    }
                    found = true;
                    break;
                }
            }

            if (!found) {
                throw new RuntimeException("Could not find the last statement");
            }
        }
        return true;
    }

    /**
     * Are all transfer accounted for
     *
     * @return True is all transfers are accounted for
     */
    public boolean isValidTransfer() {
        if (new ExtendedPeriod(core).getTransferRate() == 0.0) {
            for (Currency currency : TrackingDatabase.get().get(Currency.class)) {
                if (new ExtendedPeriod(core).getMissingTransfer(currency) != 0.0) {
                    return false;
                }
            }
        } else {
            if (new ExtendedPeriod(core).getTransferRate() != 0.0 && (new ExtendedPeriod(core).getTransferRate() < 60 || new ExtendedPeriod(core).getTransferRate() > 80)) {
                return false;
            }
        }
        return true;
    }
}
