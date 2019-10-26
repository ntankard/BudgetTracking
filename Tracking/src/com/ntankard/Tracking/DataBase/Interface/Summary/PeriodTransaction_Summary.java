package com.ntankard.Tracking.DataBase.Interface.Summary;

import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.Transaction;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Category;
import com.ntankard.Tracking.Dispaly.Util.Set.MoneyEvent_Sets.PeriodCategoryType_Set;
import com.ntankard.Tracking.Dispaly.Util.Set.MoneyEvent_Sets.PeriodCategory_Set;
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
        return new PeriodCategoryType_Set<>(core, TrackingDatabase.get().get(Category.class, "Income"), Transaction.class).getTotal() * -0.06;
    }

    /**
     * Get the remaining money to put into savings
     *
     * @return The remaining money to put into savings
     */
    @DisplayProperties(dataType = CURRENCY_YEN)
    public Double getSavings() {
        Double sum = 0.0;
        for (Category category : TrackingDatabase.get().get(Category.class)) {
            sum += new PeriodCategory_Set(core, category).getTotal();
        }
        sum += getHex();
        return -sum;
    }
}
