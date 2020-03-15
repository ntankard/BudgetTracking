package com.ntankard.Tracking.DataBase.Core.BaseObject.Field;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;

public class DataObject_Field<T extends DataObject> extends Field<T> {

    /**
     * Constructor
     *
     * @param name      The name of the Field
     * @param type      The data type of the field (same as T)
     * @param value     The current Value of the Field
     * @param canBeNull Is null accepted?
     * @param container The class that contains this field
     */
    public DataObject_Field(String name, Class<T> type, T value, Boolean canBeNull, DataObject container) {
        super(name, type, value, canBeNull, container);
    }

    /**
     * Constructor
     *
     * @param name      The name of the Field
     * @param type      The data type of the field (same as T)
     * @param value     The current Value of the Field
     * @param container The class that contains this field
     */
    public DataObject_Field(String name, Class<T> type, T value, DataObject container) {
        super(name, type, value, container);
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################# Implementation #################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc
     */
    @Override
    protected void set_preSet() {
        if (this.value != null) {
            this.value.notifyChildUnLink(container);
        }
        super.set_preSet();
    }

    /**
     * {@inheritDoc
     */
    @Override
    protected void set_postSet() {
        if (this.value != null) {
            this.value.notifyChildLink(container);
        }
        super.set_postSet();
    }
}
