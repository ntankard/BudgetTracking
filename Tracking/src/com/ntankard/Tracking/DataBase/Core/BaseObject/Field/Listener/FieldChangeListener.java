package com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Listener;

import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Field;

public interface FieldChangeListener<T> {

    /**
     * Called when the value is changed
     *
     * @param field    The field that changed
     * @param oldValue The past value
     * @param newValue The new value
     */
    void valueChanged(Field<T> field, T oldValue, T newValue);
}
