package com.ntankard.Tracking.DataBase.Core.BaseObject.Field;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Filter.FieldFilter;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Filter.Null_FieldFilter;

import java.util.ArrayList;
import java.util.List;

public class Field<T> {

    /**
     * The name of the Field
     */
    private String name;

    /**
     * The data type of the field (same as T)
     */
    private Class<T> type;

    /**
     * The current Value of the Field
     */
    protected T value;

    /**
     * The class that contains this field
     */
    protected DataObject container;

    /**
     * The fillers used to check the data
     */
    private List<FieldFilter<T>> filters = new ArrayList<>();

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Constructor
     *
     * @param name      The name of the Field
     * @param type      The data type of the field (same as T)
     * @param value     The current Value of the Field
     * @param container The class that contains this field
     */
    public Field(String name, Class<T> type, T value, DataObject container) {
        this(name, type, value, false, container);
    }

    /**
     * Constructor
     *
     * @param name      The name of the Field
     * @param type      The data type of the field (same as T)
     * @param value     The current Value of the Field
     * @param canBeNull Is null accepted?
     * @param container The class that contains this field
     */
    public Field(String name, Class<T> type, T value, Boolean canBeNull, DataObject container) {
        this.name = name;
        this.type = type;
        set_set(value);
        this.container = container;

        addFilter(new Null_FieldFilter<>(canBeNull));
    }

    /**
     * Add a new filter, and run it on the current data
     *
     * @param filter TRhe filter to add
     * @return This
     */
    public Field<T> addFilter(FieldFilter<T> filter) {
        filter.filter(this.value);
        filters.add(filter);
        return this;
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
        set_preCheck(value);
        set_preSet();
        set_set(value);
        set_postSet();
        set_postCheck();
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################# Implementation #################################################
    //------------------------------------------------------------------------------------------------------------------

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
        this.value = value;
    }

    /**
     * Perform what ever actions are required after setting a new value (register, notify ect)
     */
    protected void set_postSet() {

    }

    /**
     * Validate the field with the new value
     */
    protected void set_postCheck() {
        container.validateParents();
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
}
