package com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Filter;

public abstract class FieldFilter<T> {

    /**
     * Check that the value is valid, throw an exception if not
     *
     * @param value The value to check
     */
    public abstract void filter(T value);
}
