package com.ntankard.Tracking.DataBase.Core;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Currency;

@ClassExtensionProperties(includeParent = true)
public interface CurrencyBound {

    /**
     * Get the currency related to this object
     *
     * @return The Currency related to this object
     */
    Currency getCurrency();
}
