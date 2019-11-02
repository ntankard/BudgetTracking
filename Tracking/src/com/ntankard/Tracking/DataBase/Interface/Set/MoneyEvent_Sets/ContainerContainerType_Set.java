package com.ntankard.Tracking.DataBase.Interface.Set.MoneyEvent_Sets;

import com.ntankard.Tracking.DataBase.Core.DataObject;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.MoneyEvent;

import java.util.ArrayList;
import java.util.List;

public class ContainerContainerType_Set<T extends MoneyEvent> extends MoneyEvent_Set<T> {

    /**
     * One side of the container
     */
    private DataObject source;

    /**
     * One side of the container
     */
    private DataObject destination;

    /**
     * The type of object to group
     */
    private Class<T> toGet;

    /**
     * Constructor
     */
    public ContainerContainerType_Set(DataObject source, DataObject destination, Class<T> toGet) {
        this.source = source;
        this.destination = destination;
        this.toGet = toGet;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public List<T> get() {
        List<T> toReturn = new ArrayList<>();

        for (T event : source.getChildren(toGet)) {
            if (destination.getChildren(toGet).contains(event)) {
                toReturn.add(event);
            }
        }

        return toReturn;
    }

    /**
     * {@inheritDoc
     */
    @Override
    protected boolean isSource(T moneyEvent) {
        return moneyEvent.isThisSource(source);
    }

    /**
     * {@inheritDoc
     */
    @Override
    protected boolean isDestination(T moneyEvent) {
        return moneyEvent.isThisDestination(destination);
    }
}
