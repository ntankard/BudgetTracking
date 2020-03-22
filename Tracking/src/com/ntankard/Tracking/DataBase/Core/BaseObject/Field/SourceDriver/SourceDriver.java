package com.ntankard.Tracking.DataBase.Core.BaseObject.Field.SourceDriver;

import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Field;

public class SourceDriver<DrivingType> {

    /**
     * The field this is driving
     */
    protected Field<DrivingType> driving;

    /**
     * Set the field this is driving
     *
     * @param driving The field this is driving
     */
    public void setDriving(Field<DrivingType> driving) {
        this.driving = driving;
    }

    /**
     * Remove this object from the database
     */
    public void remove() {

    }
}
