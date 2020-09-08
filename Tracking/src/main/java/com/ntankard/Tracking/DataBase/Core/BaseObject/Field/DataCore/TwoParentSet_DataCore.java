package com.ntankard.Tracking.DataBase.Core.BaseObject.Field.DataCore;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Set.Filter.SetFilter;
import com.ntankard.Tracking.DataBase.Interface.Set.TwoParent_Children_Set;
import com.ntankard.dynamicGUI.CoreObject.Field.DataCore.DataCore;
import com.ntankard.dynamicGUI.CoreObject.Field.DataField;
import com.ntankard.dynamicGUI.CoreObject.Field.Listener.FieldChangeListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TwoParentSet_DataCore<T extends DataObject, PrimaryParentType extends DataObject, SecondaryParentType extends DataObject> extends DataCore<List<T>> implements DataObject.ChildrenListener<T> {

    /**
     * The type of object stored in this set
     */
    private final Class<T> tClass;

    // Access for the primary parent
    private final DataField<PrimaryParentType> primaryParentField;
    private FieldChangeListener<PrimaryParentType> primaryParentListener;
    private PrimaryParentType primaryParent;

    // Access for the secondary parent
    private final DataField<SecondaryParentType> secondaryParentField;
    private FieldChangeListener<SecondaryParentType> secondaryParentListener;
    protected SecondaryParentType secondaryParent;

    /**
     * A filter to apply, can be null
     */
    private final SetFilter<T> filter;

    /**
     * The master list of objects
     */
    private List<T> list = new ArrayList<>();

    /**
     * Constructor
     */
    public TwoParentSet_DataCore(Class<T> tClass, DataField<PrimaryParentType> primaryParentField, DataField<SecondaryParentType> secondaryParentField) {
        this(tClass, primaryParentField, secondaryParentField, null);
    }

    /**
     * Constructor
     */
    public TwoParentSet_DataCore(Class<T> tClass, DataField<PrimaryParentType> primaryParentField, DataField<SecondaryParentType> secondaryParentField, SetFilter<T> filter) {
        this.tClass = tClass;
        this.primaryParentField = primaryParentField;
        this.secondaryParentField = secondaryParentField;
        this.filter = filter;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void attachToField(DataField<List<T>> dataField) {
        super.attachToField(dataField);

        primaryParentListener = (field, oldValue, newValue) -> {
            if (newValue == null) {
                throw new UnsupportedOperationException(); // Dose not currently support a change in the parent
            }
            if (primaryParent != null) {
                throw new UnsupportedOperationException(); // Dose not currently support a change in the parent
            }
            primaryParent = newValue;
            if (secondaryParent != null) {
                linkToParents();
            }
        };
        primaryParentField.addChangeListener(primaryParentListener);

        secondaryParentListener = (field, oldValue, newValue) -> {
            if (newValue == null) {
                throw new UnsupportedOperationException(); // Dose not currently support a change in the parent
            }
            if (secondaryParent != null) {
                throw new UnsupportedOperationException(); // Dose not currently support a change in the parent
            }
            secondaryParent = newValue;
            if (primaryParent != null) {
                linkToParents();
            }
        };
        secondaryParentField.addChangeListener(secondaryParentListener);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void detachFromField(DataField<List<T>> field) {
        if (primaryParent != null || secondaryParent != null) {
            throw new UnsupportedOperationException(); // Dose not currently deleting the object this core is attached to
        }

        primaryParentField.removeChangeListener(primaryParentListener);
        primaryParentListener = null;

        secondaryParentField.removeChangeListener(secondaryParentListener);
        secondaryParentListener = null;

        super.detachFromField(field);
    }

    /**
     * Once both parents have been received, link to them and start maintaining the list
     */
    private void linkToParents() {
        if (primaryParent == null || secondaryParent == null) {
            throw new IllegalStateException();
        }

        primaryParent.addChildrenListener(this);
        secondaryParent.addChildrenListener(this);

        list = new ArrayList<>();
        for (T dataObject : manualGet()) {
            childAdded(dataObject);
        }
    }

    /**
     * Recalculate the list from scratch
     *
     * @return The recalculated list
     */
    private List<T> manualGet() {
        return new TwoParent_Children_Set<>(tClass, primaryParent, secondaryParent, filter).get();
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void childAdded(T dataObject) {
        if (tClass.isAssignableFrom(dataObject.getClass())) {
            if (list.contains(dataObject)) {
                throw new IllegalArgumentException();
            }
            if (primaryParent.getChildren(tClass).contains(dataObject)) {
                if (secondaryParent.getChildren(tClass).contains(dataObject)) {
                    if (shouldAdd(dataObject)) {
                        list.add(dataObject);
                        for (FieldChangeListener<List<T>> fieldChangeListener : getDataField().getFieldChangeListeners()) {
                            fieldChangeListener.valueChanged(getDataField(), null, Collections.singletonList(dataObject));
                        }
                    }
                }
            }
        }
    }

    /**
     * {@inheritDoc
     */
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void childRemoved(T dataObject) {
        if (tClass.isAssignableFrom(dataObject.getClass())) {
            if (primaryParent.getChildren(tClass).contains(dataObject)) {
                if (secondaryParent.getChildren(tClass).contains(dataObject)) {
                    if (shouldAdd(dataObject)) {
                        if (!list.contains(dataObject)) {
                            throw new IllegalArgumentException();
                        }
                        list.remove(dataObject);
                        for (FieldChangeListener fieldChangeListener : getDataField().getFieldChangeListeners()) {
                            fieldChangeListener.valueChanged(getDataField(), Collections.singletonList(dataObject), null);
                        }
                    }
                }
            }
        }
    }

    /**
     * Should this object be added to the set based on any available filters?
     *
     * @param t The object to test
     * @return True if it should be added to the set
     */
    public boolean shouldAdd(T t) {
        if (filter != null) {
            return filter.shouldAdd(t);
        }
        return true;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public List<T> get() {
        if (TrackingDatabase.get().shouldVerifyCalculations()) {
            List<T> reCalculated = manualGet();

            if (reCalculated.size() != list.size()) {
                throw new IllegalStateException();
            }

            for (T toCheck : list) {
                if (!reCalculated.contains(toCheck)) {
                    throw new IllegalStateException();
                }
            }
        }
        return list;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void set(List<T> toSet) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void initialSet(List<T> toSet) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc
     */
    @Override
    public boolean canEdit() {
        return false;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public boolean canInitialSet() {
        return false;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public boolean doseSupportChangeListeners() {
        return true;
    }
}
