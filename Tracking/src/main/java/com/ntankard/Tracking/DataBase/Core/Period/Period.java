package com.ntankard.Tracking.DataBase.Core.Period;

import com.ntankard.javaObjectDatabase.CoreObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Factory.DoubleParentFactory;
import com.ntankard.javaObjectDatabase.CoreObject.Interface.Ordered;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.SolidCategory;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.FundEvent;
import com.ntankard.javaObjectDatabase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Summary.Period_Summary;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Factory.SingleParentFactory;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.Category_Summary;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.FundEvent_Summary;
import com.ntankard.javaObjectDatabase.CoreObject.FieldContainer;

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
        fieldContainer.addObjectFactory(new SingleParentFactory<>(Period_Summary.class, Period_Summary::make));
        fieldContainer.addObjectFactory(new DoubleParentFactory<Category_Summary, Period, SolidCategory>(
                Category_Summary.class,
                SolidCategory.class,
                (generator, secondaryGenerator) -> Category_Summary.make(TrackingDatabase.get().getNextId(), generator, secondaryGenerator)));
        fieldContainer.addObjectFactory(new DoubleParentFactory<FundEvent_Summary, Period, FundEvent>(
                FundEvent_Summary.class,
                FundEvent.class,
                (generator, secondaryGenerator) -> FundEvent_Summary.make(TrackingDatabase.get().getNextId(), generator, secondaryGenerator)));

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
