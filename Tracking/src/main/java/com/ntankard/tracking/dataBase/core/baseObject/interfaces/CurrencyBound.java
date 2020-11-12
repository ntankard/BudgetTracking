package com.ntankard.tracking.dataBase.core.baseObject.interfaces;

import com.ntankard.tracking.dataBase.core.Currency;

public interface CurrencyBound {

    /**
     * Get the currency related to this object
     *
     * @return The Currency related to this object
     */
    Currency getCurrency();
}
