package com.ntankard.Tracking.DataBase.Core.BaseObject.Field;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Filter.FieldFilter;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Filter.Null_FieldFilter;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Listener.FieldChangeListener;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.SourceDriver.SourceDriver;

import java.util.ArrayList;
import java.util.List;

public class Field<T> {

    protected enum FieldState {STARTED, INITIALISED, INITIAL_VALUE, ADDED}

    // Core Data -------------------------------------------------------------------------------------------------------

    /**
     * The name of the Field
     */
    private String name;

    /**
     * The data type of the field (same as T)
     */
    private Class<T> type;

    /**
     * The current state of the field
     */
    protected FieldState fieldState;

    // Core Data To Build (can be added after object is instantiated) --------------------------------------------------

    /**
     * The class that contains this field
     */
    protected DataObject container;

    /**
     * The fillers used to check the data
     */
    private List<FieldFilter<T>> filters = new ArrayList<>();

    /**
     * Objects to be notified when data changes
     */
    private List<FieldChangeListener<T>> fieldChangeListeners = new ArrayList<>();

    /**
     * Should the field be saved when the object is saved?
     */
    private boolean shouldSave = true;

    /**
     * The source driver that sets the value of this field
     */
    private SourceDriver<T> sourceDriver = null;

    // Dynamic data ----------------------------------------------------------------------------------------------------

    /**
     * The current Value of the Field
     */
    protected T value;

    /**
     * The most recent last Value of the Field
     */
    protected T oldValue;

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Constructor
     *
     * @param name The name of the Field
     * @param type The data type of the field (same as T)
     */
    public Field(String name, Class<T> type) {
        this(name, type, false);
        if (!DataObject_Field.class.isAssignableFrom(this.getClass())) {
            if (DataObject.class.isAssignableFrom(type)) {
                throw new IllegalArgumentException("Using the wrong field for a dataObject");
            }
        }
    }

    /**
     * Constructor
     *
     * @param name      The name of the Field
     * @param type      The data type of the field (same as T)
     * @param canBeNull Is null accepted?
     */
    public Field(String name, Class<T> type, Boolean canBeNull) {
        this.name = name;
        this.type = type;
        this.fieldState = FieldState.STARTED;
        addFilter(new Null_FieldFilter<>(canBeNull));
    }

    /**
     * Add a new change listener to get called when a value changes
     *
     * @param fieldChangeListener The FieldChangeListener to add
     * @return this
     */
    @SuppressWarnings("UnusedReturnValue")
    public Field<T> addChangeListener(FieldChangeListener<T> fieldChangeListener) {
        this.fieldChangeListeners.add(fieldChangeListener);
        return this;
    }

    /**
     * Remove a change listener
     *
     * @param fieldChangeListener The FieldChangeListener to remove
     */
    @SuppressWarnings("unused")
    public void removeChangeListener(FieldChangeListener<T> fieldChangeListener) {
        this.fieldChangeListeners.remove(fieldChangeListener);
    }

    /**
     * Add a new filter, and run it on the current data
     *
     * @param filter TRhe filter to add
     * @return This
     */
    public Field<T> addFilter(FieldFilter<T> filter) {
        if (!fieldState.equals(FieldState.STARTED)) {
            throw new RuntimeException("Fillers can only be added before the container and value is set");
        }
        this.filters.add(filter);
        return this;
    }

    /**
     * Link a source driver that will set the value of this field
     *
     * @param sourceDriver The source drive
     * @return This
     */
    public Field<T> addSourceDriver(SourceDriver<T> sourceDriver) {
        this.sourceDriver = sourceDriver;
        sourceDriver.setDriving(this);
        this.shouldSave = false;
        return this;
    }

    /**
     * Link the field to a container
     *
     * @param container The container to link it too
     * @return This
     */
    public Field<T> setContainer(DataObject container) {
        if (fieldState != FieldState.STARTED) {
            throw new RuntimeException("The container has already been set");
        }
        this.container = container;
        this.fieldState = FieldState.INITIALISED;
        return this;
    }

    /**
     * Set the initial value of the field
     *
     * @param value The value to set
     * @return THis
     */
    public Field<T> initialSet(T value) {
        if (fieldState != FieldState.INITIALISED) {
            throw new RuntimeException("The containers has not been set of the initial value has already been provided");
        }
        set_impl(value);
        fieldState = FieldState.INITIAL_VALUE;
        return this;
    }

    /**
     * Add this field to the database along with its container object
     *
     * @return This
     */
    public Field<T> add() {
        if (fieldState != FieldState.INITIAL_VALUE) {
            throw new RuntimeException("The field has not been fully constructed yet");
        }
        fieldState = FieldState.ADDED;
        return this;
    }

    /**
     * Remove this field from the database
     */
    public void remove() {
        if (fieldState != FieldState.ADDED) {
            throw new RuntimeException("Removing a field that was never added");
        }
        if (sourceDriver != null) {
            sourceDriver.remove();
        }
    }


    //------------------------------------------------------------------------------------------------------------------
    //################################################## Field Access ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get the current value
     *
     * @return The current value
     */
    public T get() {
        return value;
    }

    /**
     * Set the field value and perform what ever actions are required
     *
     * @param value The value to set
     */
    public void set(T value) {
        if (!fieldState.equals(FieldState.ADDED)) {
            throw new RuntimeException("Can't set a value before the object is fully setup");
        }
        set_impl(value);
    }

    /**
     * Set from the source driver
     *
     * @param value  The value to set
     * @param source The source of the value
     */
    public void sourceSet(T value, SourceDriver<T> source) {
        if (this.sourceDriver == null || this.sourceDriver != source) {
            throw new RuntimeException("This source dose not have permission to set values");
        }
        if (fieldState.equals(FieldState.ADDED)) {
            set(value);
        }
        if (fieldState.equals(FieldState.INITIALISED)) {
            initialSet(value);
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################# Implementation #################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Set the field value and perform what ever actions are required
     *
     * @param value The value to set
     */
    public void set_impl(T value) {
        set_preCheck(value);
        set_preSet();
        set_set(value);
        set_postSet();
        set_postCheck();
    }

    /**
     * Validate the value to set
     *
     * @param value Teh value to set
     */
    protected void set_preCheck(T value) {
        for (FieldFilter<T> filter : filters) {
            filter.filter(value);
        }
    }

    /**
     * Perform what ever actions are required before setting a new value (remove old value)
     */
    protected void set_preSet() {
    }

    /**
     * Set the value
     *
     * @param value The value to set
     */
    protected void set_set(T value) {
        this.oldValue = this.value;
        this.value = value;
    }

    /**
     * Perform what ever actions are required after setting a new value (register, notify ect)
     */
    protected void set_postSet() {
        for (FieldChangeListener<T> fieldChangeListener : fieldChangeListeners) {
            fieldChangeListener.valueChanged(this, oldValue, value);
        }
    }

    /**
     * Validate the field with the new value
     */
    protected void set_postCheck() {
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public String getName() {
        return name;
    }

    public Class<T> getType() {
        return type;
    }

    public T getValue() {
        return value;
    }

    public DataObject getContainer() {
        return container;
    }

    public FieldState getFieldState() {
        return fieldState;
    }

    public boolean isShouldSave() {
        return shouldSave;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public void setShouldSave(boolean shouldSave) {
        this.shouldSave = shouldSave;
    }
}
