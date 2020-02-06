package com.ntankard.Tracking.DataBase.Core.Transfers.RecurringPayment;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.NamedDataObject;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

@ClassExtensionProperties(includeParent = true)
public abstract class RecurringPayment extends NamedDataObject {

    /**
     * Constructor
     */
    @ParameterMap(shouldSave = false)
    public RecurringPayment(Integer id, String name) {
        super(id, name);
    }
}
