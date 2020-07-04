package com.ntankard.Tracking.DataBase.Core.BaseObject.Interface;

import com.ntankard.Tracking.DataBase.Core.Currency;

public interface CurrencyBound {

    /**
     * Get the currency related to this object
     *
     * @return The Currency related to this object
     */
    Currency getCurrency();
}
