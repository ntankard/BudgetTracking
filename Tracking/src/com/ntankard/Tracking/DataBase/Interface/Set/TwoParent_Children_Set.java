package com.ntankard.Tracking.DataBase.Interface.Set;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Interface.Set.Filter.SetFilter;

import java.util.ArrayList;
import java.util.List;

public class TwoParent_Children_Set<T extends DataObject, PrimaryParentType extends DataObject, SecondaryParentType extends DataObject> extends ObjectSet<T> {

    /**
     * The DataObject to get from the core
     */
    private final Class<T> tClass;

    /**
     * The core object to extract children from
     */
    private PrimaryParentType primaryParent;

    /**
     * The secondary object to extract children from
     */
    private SecondaryParentType secondaryParent;

    /**
     * Constructor
     */
    public TwoParent_Children_Set(Class<T> tClass, PrimaryParentType primaryParent, SecondaryParentType secondaryParent) {
        this(tClass, primaryParent, secondaryParent, null);
    }

    /**
     * Constructor
     */
    public TwoParent_Children_Set(Class<T> tClass, PrimaryParentType primaryParent, SecondaryParentType secondaryParent, SetFilter<T> filter) {
        super(filter);
        this.tClass = tClass;
        this.primaryParent = primaryParent;
        this.secondaryParent = secondaryParent;
    }

    /**
     * {@inheritDoc
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<T> get() {
        if (primaryParent == null || secondaryParent == null) {
            return new ArrayList<>();
        }

        List<T> toReturn = new ArrayList<>();
        List<T> primary = primaryParent.getChildren(tClass);
        List<T> secondary = secondaryParent.getChildren(tClass);
        for (T t : primary) {
            if (secondary.contains(t)) {
                if (shouldAdd(t)) {
                    toReturn.add(t);
                }
            }
        }

        return toReturn;
    }

    /**
     * Set the core object to extract children from
     *
     * @param primaryParent The core object to extract children from
     */
    public void setPrimaryParent(PrimaryParentType primaryParent) {
        this.primaryParent = primaryParent;
    }

    /**
     * Set the secondary object to extract children from
     *
     * @param secondaryParent The secondary object to extract children from
     */
    public void setSecondaryParent(SecondaryParentType secondaryParent) {
        this.secondaryParent = secondaryParent;
    }
}
