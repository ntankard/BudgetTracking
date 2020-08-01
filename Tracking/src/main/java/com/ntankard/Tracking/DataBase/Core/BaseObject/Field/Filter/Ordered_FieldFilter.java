package com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Filter;

import com.ntankard.dynamicGUI.CoreObject.CoreObject;
import com.ntankard.dynamicGUI.CoreObject.Field.Filter.FieldFilter;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.Ordered;

public class Ordered_FieldFilter<T extends Ordered, ContainerType extends CoreObject> extends FieldFilter<T, ContainerType> {

    /**
     * The type of order
     */
    public enum OrderSequence {ABOVE, BELOW}

    /**
     * The field to compare too
     */
    private final String toCompare;

    /**
     * The type of order
     */
    private final OrderSequence orderSequence;

    /**
     * Constructor
     */
    public Ordered_FieldFilter(String toCompare, OrderSequence orderSequence) {
        this.toCompare = toCompare;
        this.orderSequence = orderSequence;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public boolean isValid(T value, ContainerType container) {
        if (value != null && container.getField(toCompare).get() != null) {
            Integer order1 = value.getOrder();
            Integer order2 = ((Ordered) container.getField(toCompare).get()).getOrder();

            if (orderSequence.equals(OrderSequence.BELOW)) {
                return order1 < order2;
            } else if (orderSequence.equals(OrderSequence.ABOVE)) {
                return order1 > order2;
            }
        }
        return true;
    }
}
