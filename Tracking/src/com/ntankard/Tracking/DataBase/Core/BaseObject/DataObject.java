package com.ntankard.Tracking.DataBase.Core.BaseObject;

import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.DataObject_Field;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Field;
import com.ntankard.Tracking.DataBase.Database.SubContainers.DataObjectContainer;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ntankard.ClassExtension.MemberProperties.DEBUG_DISPLAY;
import static com.ntankard.ClassExtension.MemberProperties.TRACE_DISPLAY;

public abstract class DataObject {

    /**
     * The fields for this DataObject
     */
    private Map<String, Field<?>> fieldMap = new HashMap<>();

    /**
     * All my children
     */
    private DataObjectContainer children = new DataObjectContainer();

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static List<Field<?>> getFields() {
        List<Field<?>> toReturn = new ArrayList<>();
        toReturn.add(new Field<>("getId", Integer.class));
        return toReturn;
    }

    /**
     * Get a map of the fields
     *
     * @param fields The Fields to map
     * @return A Map of the Fields
     */
    public static Map<String, Field<?>> makeFieldMap(List<Field<?>> fields) {
        Map<String, Field<?>> fieldMap = new HashMap<>();
        for (Field<?> field : fields) {
            fieldMap.put(field.getName(), field);
        }
        return fieldMap;
    }

    /**
     * Construct an object with initialised values
     *
     * @param fields      The Fields of the object
     * @param blackObject The constructed object without the fields attached yet
     * @param args        The values for the fields
     * @param <T>         The object type
     * @return The assembled object
     */
    @SuppressWarnings({"rawtypes", "unchecked", "SuspiciousMethodCalls"})
    public static <T extends DataObject> T assembleDataObject(List<Field<?>> fields, T blackObject, Object... args) {
        if (args.length / 2 * 2 != args.length) throw new IllegalArgumentException("Wrong amount of arguments");
        int amount = args.length / 2;
        fields.forEach(field -> field.setContainer(blackObject));
        Map<String, Field> fieldMap = new HashMap<>();
        fields.forEach(field -> fieldMap.put(field.getName(), field));

        for (int i = 0; i < amount; i++) {
            fieldMap.get(args[i * 2]).initialSet(args[i * 2 + 1]);
        }
        blackObject.setFields(fields);
        return blackObject;
    }

    /**
     * Set all the fields for this object, should be called by a solid object constructor
     *
     * @param fields The fields to set
     */
    public void setFields(List<Field<?>> fields) {
        for (Field<?> field : fields) {
            fieldMap.put(field.getName(), field);
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### General #####################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get the unique identifier for this data object
     *
     * @return The unique identifier for this data object
     */
    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
    @DisplayProperties(order = 1000000)
    public Integer getId() {
        return get("getId");
    }

    /**
     * {@inheritDoc
     */
    @Override
    public String toString() {
        return getId().toString();
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################ Database access  ################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Add this object to the database. Notify everyone required and create or add supporting objects if needed
     */
    public void add() {
        TrackingDatabase.get().add(this);
        this.notifyParentLink();
        // @TODO check that this has not been double added
    }

    /**
     * Safely remove this object from the database. Disabled by default
     */
    public void remove() {
        throw new UnsupportedOperationException("Not cleared for removal");
    }

    /**
     * Safely remove this object from the database.
     */
    protected void remove_impl() {
        if (this.getChildren().size() != 0) {
            throw new RuntimeException("Cant delete this kind of object. NoneFundEvent still has children");
        }
        for (Map.Entry<String, Field<?>> field : fieldMap.entrySet()) {
            field.getValue().remove();
        }
        this.notifyParentUnLink();
        TrackingDatabase.get().remove(this);
        // @TODO check that this has not been double removed
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################### Parental Hierarchy  ##############################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Notify all the objects parents that this object has linked to them
     */
    public void notifyParentLink() {
        for (DataObject dataObject : getParents()) {
            dataObject.notifyChildLink(this);
        }
        for (Map.Entry<String, Field<?>> field : fieldMap.entrySet()) {
            field.getValue().add();
        }
    }

    /**
     * Notify all this object's parent that this object is removing the link to them. Call before deleting this object
     */
    public void notifyParentUnLink() {
        for (DataObject dataObject : getParents()) {
            dataObject.notifyChildUnLink(this);
        }
    }

    /**
     * Get all the parents of this object
     *
     * @return All the parents of this object
     */
    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 2000000)
    public List<DataObject> getParents() {
        List<DataObject> toReturn = new ArrayList<>();
        for (Map.Entry<String, Field<?>> field : fieldMap.entrySet()) {
            if (field.getValue().get() != null) {
                if (DataObject_Field.class.isAssignableFrom(field.getValue().getClass())) {
                    try {
                        toReturn.add((DataObject) field.getValue().get());
                    } catch (Exception e) {
                        toReturn.add((DataObject) field.getValue().get());
                    }
                }
            }
        }
        return toReturn;
    }

    /**
     * Notify that a child has linked to this object as its parent
     *
     * @param linkObject The child
     */
    public void notifyChildLink(DataObject linkObject) {
        children.add(linkObject);
    }

    /**
     * Notify that a child has un linked this object as its parent
     *
     * @param linkObject The child
     */
    public void notifyChildUnLink(DataObject linkObject) {
        children.remove(linkObject);
    }

    /**
     * Get the list of children for a a specific class type
     *
     * @param type The class type to get
     * @param <T>  The Object type
     * @return The list of children for a a specific class type
     */
    public <T extends DataObject> List<T> getChildren(Class<T> type) {
        return children.get(type);
    }

    /**
     * Get the list of children for a a specific class type
     *
     * @param type The class type to get
     * @param key  The ID of the object to get
     * @param <T>  The Object type
     * @return The list of children for a a specific class type
     */
    public <T extends DataObject> T getChildren(Class<T> type, Integer key) {
        return children.get(type, key);
    }

    /**
     * Get the list of all children
     *
     * @return The list of all children
     */
    @MemberProperties(verbosityLevel = TRACE_DISPLAY)
    @DisplayProperties(order = 3000000)
    public List<DataObject> getChildren() {
        return children.get();
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get a specific field
     *
     * @param field The Field to get
     * @param <T>   THe type of the field
     * @return The field
     */
    @SuppressWarnings("unchecked")
    public <T> Field<T> getField(String field) {
        return (Field<T>) fieldMap.get(field);
    }

    /**
     * Get the value from a specific field
     *
     * @param field The Field to get
     * @param <T>   The type of the Field
     * @return The value of the field
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String field) {
        return (T) getField(field).get();
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Set the value from a specific field
     *
     * @param field The field to set
     * @param value THe value to set
     * @param <T>   The type of the Field
     */
    @SuppressWarnings("unchecked")
    public <T> void set(String field, T value) {
        ((Field<T>) fieldMap.get(field)).set(value);
    }


    /**
     * Get possible options that a field will accept
     *
     * @param type      The type of object expected
     * @param fieldName The field name
     * @return A list of objects the the field will accept
     */
    public <T extends DataObject> List<T> sourceOptions(Class<T> type, String fieldName) {
        return TrackingDatabase.get().get(type);
    }

    /**
     * Check that all parents are linked properly
     */
    public void validateParents() {
        try {
            for (DataObject dataObject : getParents()) {
                if (!dataObject.getChildren().contains(this)) {
                    throw new RuntimeException("Not registered with a parent");
                }
            }
        } catch (UnsupportedOperationException ignored) {

        }
    }
}
