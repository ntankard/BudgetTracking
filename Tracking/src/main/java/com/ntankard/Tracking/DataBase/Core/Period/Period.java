package com.ntankard.Tracking.DataBase.Core.Period;

import com.ntankard.Tracking.DataBase.Interface.Summary.Period_Summary;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.Category_Summary;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.FundEvent_Summary;
import com.ntankard.javaObjectDatabase.CoreObject.DataObject;
import com.ntankard.javaObjectDatabase.CoreObject.FieldContainer;
import com.ntankard.javaObjectDatabase.CoreObject.Interface.Ordered;

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
        fieldContainer.addObjectFactory(Period_Summary.Factory);
        fieldContainer.addObjectFactory(Category_Summary.Factory);
        fieldContainer.addObjectFactory(FundEvent_Summary.Factory);

        // ID
        // Parents
        // Children

        return fieldContainer.endLayer(Period.class);
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
}
