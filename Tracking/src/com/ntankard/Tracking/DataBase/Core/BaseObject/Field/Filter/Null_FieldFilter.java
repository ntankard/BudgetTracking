package com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Filter;

public class Null_FieldFilter<T> extends FieldFilter<T> {

    /**
     * Can the field be set to null?
     */
    private Boolean canBeNull;

    /**
     * Constructor
     */
    public Null_FieldFilter(Boolean canBeNull) {
        this.canBeNull = canBeNull;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void filter(T value) {
        if (!canBeNull) {
            if (value == null) {
                throw new IllegalArgumentException("Field is null");
            }
        }
    }
}
