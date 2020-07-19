package com.ntankard.Tracking.DataBase.Core.BaseObject;

import com.ntankard.CoreObject.Field.DataCore.DataCore;
import com.ntankard.CoreObject.Field.DataCore.Calculate_DataCore;
import com.ntankard.CoreObject.Field.DataField;
import com.ntankard.CoreObject.Field.Filter.Null_FieldFilter;
import com.ntankard.CoreObject.Field.Listener.FieldChangeListener;

public class Tracking_DataField<T> extends DataField<T> {

    /**
     * Can the field be null?
     */
    private final boolean canBeNull;

    /**
     * Should the parent be notified if this field links to it? @TODO this is a bad hack and should be removed
     */
    private boolean tellParent;

    /**
     * Listener to register children and parents
     */
    private final FieldChangeListener<T> listener = (field, oldValue, newValue) -> {
        if (field.getState().equals(NewFieldState.N_ACTIVE)) {
            if (DataObject.class.isAssignableFrom(field.getType())) {
                if (((Tracking_DataField<T>) field).tellParent) {
                    if (oldValue != null) {
                        ((DataObject) oldValue).notifyChildUnLink((DataObject) field.getContainer());
                    }
                    if (newValue != null) {
                        ((DataObject) newValue).notifyChildLink((DataObject) field.getContainer());
                    }
                    ((DataObject) field.getContainer()).validateParents();
                }
            }
        }
    };

    /**
     * {@inheritDoc
     */
    @Override
    public void add() {
        super.add();
        if (DataObject.class.isAssignableFrom(getType())) {
            if (tellParent) {
                if (get() != null) {
                    ((DataObject) get()).notifyChildLink((DataObject) getContainer());
                }
            }
        }
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void remove() {
        this.removeChangeListener(listener);
        if (DataObject.class.isAssignableFrom(getType())) {
            if (tellParent) {
                if (get() != null) {
                    ((DataObject) get()).notifyChildUnLink((DataObject) getContainer());
                }
            }
        }
        super.remove();
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void setDataCore(DataCore<T> dataCore) {
        if (Calculate_DataCore.class.isAssignableFrom(dataCore.getClass())) {
            removeChangeListener(listener);
            tellParent = false;
        }
        if (dataCore.canEdit() && DataObject.class.isAssignableFrom(getType())) {
            setSource(DataObject.getSourceOptionMethod());
        }
        super.setDataCore(dataCore);
        // TODO possible bug here, if the data core is set more than once, and goes from regular, to calculated, to regular we might lose the listener
    }

    /**
     * Constructor
     */
    public Tracking_DataField(String name, Class<T> type) {
        this(name, type, false, true);
    }

    /**
     * Constructor
     */
    public Tracking_DataField(String name, Class<T> type, Boolean canBeNull) {
        this(name, type, canBeNull, true);
    }

    /**
     * Constructor
     */
    public Tracking_DataField(String name, Class<T> type, Boolean canBeNull, boolean tellParent) {
        super(name, type);
        this.canBeNull = canBeNull;
        this.tellParent = tellParent;

        addFilter(new Null_FieldFilter<>(canBeNull));
        addChangeListener(listener);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public boolean isCanBeNull() {
        return canBeNull;
    }

    public boolean isTellParent() {
        return tellParent;
    }
}
