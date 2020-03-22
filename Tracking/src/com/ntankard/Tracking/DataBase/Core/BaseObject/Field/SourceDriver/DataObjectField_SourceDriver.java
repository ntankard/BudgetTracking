package com.ntankard.Tracking.DataBase.Core.BaseObject.Field.SourceDriver;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Field;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Listener.FieldChangeListener;

public class DataObjectField_SourceDriver<DrivingType, SourceObjectType extends DataObject> extends SourceDriver<DrivingType> implements FieldChangeListener<DrivingType> {

    /**
     * The field holding the source object
     */
    private Field<SourceObjectType> sourceObjectField;

    /**
     * The listener to determine is the source object changes
     */
    private FieldChangeListener<SourceObjectType> sourceObjectChangeListener;

    /**
     * The last know source object
     */
    private SourceObjectType sourceObject;

    /**
     * The field inside the source object used to get the final value
     */
    private String fieldName;

    /**
     * Constructor
     */
    @SuppressWarnings("unchecked")
    public DataObjectField_SourceDriver(Field<SourceObjectType> sourceObjectField, String fieldName) {
        this.sourceObjectField = sourceObjectField;
        this.fieldName = fieldName;

        this.sourceObjectChangeListener = (field, oldValue, newValue) -> {
            if (oldValue != null) {
                ((Field<DrivingType>) oldValue.getField(fieldName)).removeChangeListener(DataObjectField_SourceDriver.this);
            }
            if (newValue != null) {
                ((Field<DrivingType>) newValue.getField(fieldName)).addChangeListener(DataObjectField_SourceDriver.this);
                driving.sourceSet(((Field<DrivingType>) newValue.getField(fieldName)).get(), DataObjectField_SourceDriver.this);
            } else {
                driving.sourceSet(null, DataObjectField_SourceDriver.this);
            }
            sourceObject = newValue;
        };
        this.sourceObjectField.addChangeListener(sourceObjectChangeListener);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void valueChanged(Field<DrivingType> field, DrivingType oldValue, DrivingType newValue) {
        driving.sourceSet(newValue, this);
    }

    /**
     * {@inheritDoc
     */
    @Override
    @SuppressWarnings("unchecked")
    public void remove() {
        super.remove();
        if (sourceObject != null) {
            ((Field<DrivingType>) sourceObject.getField(fieldName)).removeChangeListener(this);
        }
        sourceObjectField.removeChangeListener(sourceObjectChangeListener);
    }
}
