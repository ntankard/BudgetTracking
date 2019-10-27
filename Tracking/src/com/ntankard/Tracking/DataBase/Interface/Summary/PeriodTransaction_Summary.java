package com.ntankard.Tracking.DataBase.Interface.Summary;

import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Category;
import com.ntankard.Tracking.Dispaly.Util.Set.MoneyEvent_Sets.ContainerCategory_Set;
import com.ntankard.Tracking.DataBase.TrackingDatabase;

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
    public Double getHex() {
        return new ContainerCategory_Set(core, TrackingDatabase.get().get(Category.class, "Income")).getTotal() * -0.06;
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
        sum -= getHex();
        return sum;
    }

    /**
     * Get the amount on money not in a category
     *
     * @return The amount on money not in a category
     */
    @DisplayProperties(dataType = CURRENCY_YEN)
    public Double getNonCategory() {
        Double sum = 0.0;
        for (Category category : TrackingDatabase.get().get(Category.class)) {
            sum += new ContainerCategory_Set(core, category).getTotal();
        }
        return -sum;
    }
}
