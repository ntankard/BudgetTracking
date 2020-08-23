package com.ntankard.Tracking.DataBase.Core.Period;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.Ordered;
import com.ntankard.Tracking.DataBase.Interface.Summary.Period_Summary;
import com.ntankard.dynamicGUI.CoreObject.Factory.Dummy_Factory;
import com.ntankard.dynamicGUI.CoreObject.FieldContainer;

public abstract class Period extends DataObject implements Ordered {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = DataObject.getFieldContainer();

        // Class behavior
        fieldContainer.addObjectFactory(new Dummy_Factory(Period_Summary.class));

        // ID
        // Parents
        // Children

        return fieldContainer.endLayer(Period.class);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void add() {
        super.add();
        Period_Summary.make(this).add();
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Speciality ###################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Dose this period exist within this range of time?
     *
     * @param start    The start of the range
     * @param duration The duration of the range (in months)
     * @return True if this period is withing the range
     */
    public boolean isWithin(Period start, Integer duration) {
        int diff = this.getOrder() - start.getOrder();
        return diff >= 0 && diff < duration;
    }

    /**
     * Get the Period Summary for this period, it is in the main database, stored here for convenience
     *
     * @return The period summary for this period
     */
    public Period_Summary getPeriodSummary() {
        return getChildren(Period_Summary.class).get(0);
    }
}
