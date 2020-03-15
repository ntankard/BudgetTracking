package com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Filter;

import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Field;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.Ordered;

public class Ordered_FieldFilter<T extends Ordered> extends FieldFilter<T> {

    /**
     * The type of order
     */
    public enum OrderSequence {ABOVE, BELOW}

    /**
     * The field to compare too
     */
    private Field<T> toCompare;

    /**
     * The type of order
     */
    private OrderSequence orderSequence;

    /**
     * Constructor
     */
    public Ordered_FieldFilter(Field<T> toCompare, OrderSequence orderSequence) {
        this.toCompare = toCompare;
        this.orderSequence = orderSequence;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void filter(T value) {
        if (value != null && toCompare.get() != null) {
            Integer order1 = value.getOrder();
            Integer order2 = toCompare.get().getOrder();

            if (orderSequence.equals(OrderSequence.BELOW)) {
                if ((order1 >= order2)) throw new IllegalArgumentException("Out of order below");
            } else if (orderSequence.equals(OrderSequence.ABOVE)) {
                if ((order1 <= order2)) throw new IllegalArgumentException("Out of order above");
            }
        }
    }
}
