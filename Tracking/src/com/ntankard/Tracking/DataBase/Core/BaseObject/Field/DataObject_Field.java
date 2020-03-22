package com.ntankard.Tracking.DataBase.Core.BaseObject.Field;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;

public class DataObject_Field<T extends DataObject> extends Field<T> {

    /**
     * Constructor
     *
     * @param name      The name of the Field
     * @param type      The data type of the field (same as T)
     * @param canBeNull Is null accepted?
     */
    public DataObject_Field(String name, Class<T> type, Boolean canBeNull) {
        super(name, type, canBeNull);
    }

    /**
     * Constructor
     *
     * @param name The name of the Field
     * @param type The data type of the field (same as T)
     */
    public DataObject_Field(String name, Class<T> type) {
        super(name, type);
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################# Implementation #################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc
     */
    @Override
    protected void set_preSet() {
        if (fieldState.equals(FieldState.ADDED)) {
            if (this.value != null) {
                this.value.notifyChildUnLink(container);
            }
        }
        super.set_preSet();
    }

    /**
     * {@inheritDoc
     */
    @Override
    protected void set_postSet() {
        if (fieldState.equals(FieldState.ADDED)) {
            if (this.value != null) {
                this.value.notifyChildLink(container);
            }
        }
        super.set_postSet();
    }

    /**
     * {@inheritDoc
     */
    @Override
    protected void set_postCheck() {
        super.set_postCheck();
        if (fieldState.equals(FieldState.ADDED)) {
            container.validateParents();
        }
    }
}
