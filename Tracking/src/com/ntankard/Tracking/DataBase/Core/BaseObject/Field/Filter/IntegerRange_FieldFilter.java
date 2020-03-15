package com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Filter;

public class IntegerRange_FieldFilter extends FieldFilter<Integer> {

    /**
     * The minimum value, null if not needed
     */
    private Integer min;

    /**
     * The maximum value, null if not needed
     */
    private Integer max;

    /**
     * Constructor
     */
    public IntegerRange_FieldFilter(Integer min, Integer max) {
        this.min = min;
        this.max = max;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void filter(Integer value) {
        if (min != null && value < min) throw new IllegalArgumentException("Below minimum");
        if (max != null && value > max) throw new IllegalArgumentException("Above maximum");
    }
}
