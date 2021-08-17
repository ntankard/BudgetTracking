package com.ntankard.budgetTracking.dataBase.core.baseObject.interfaces;

import com.ntankard.budgetTracking.dataBase.core.Currency;

public interface CurrencyBound {

    /**
     * Get the currency related to this object
     *
     * @return The Currency related to this object
     */
    Currency getCurrency();
}
