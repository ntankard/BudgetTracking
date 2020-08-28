package com.ntankard.Tracking.DataBase.Core.Pool.FundEvent;

import com.ntankard.Tracking.DataBase.Core.BaseObject.Factory.DoubleParentFactory;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Tracking_DataField;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.SolidCategory;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.FundEvent_Summary;
import com.ntankard.dynamicGUI.CoreObject.FieldContainer;

public abstract class FundEvent extends Pool {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String FundEvent_Category = "getCategory";

    /**
     * Get all the fields for this object
     */
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = Pool.getFieldContainer();

        // Class behavior
        fieldContainer.addObjectFactory(new DoubleParentFactory<FundEvent_Summary, FundEvent, Period>(
                FundEvent_Summary.class,
                Period.class,
                (generator, secondaryGenerator) -> FundEvent_Summary.make(TrackingDatabase.get().getNextId(), secondaryGenerator, generator)));

        // ID
        // Name
        // Category ====================================================================================================
        fieldContainer.add(new Tracking_DataField<>(FundEvent_Category, SolidCategory.class));
        //==============================================================================================================
        // Parents
        // Children

        return fieldContainer.endLayer(FundEvent.class);
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Speciality ###################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Is this fund event active in this period?
     *
     * @param period The period to check
     * @return True if its active at this time
     */
    public abstract Boolean isActiveThisPeriod(Period period);

    /**
     * Will there be a charge this period?
     *
     * @param period The period to test
     * @return True if there needs to be a charge for this period
     */
    public abstract Boolean isChargeThisPeriod(Period period);

    /**
     * Get the required charge amount for a specific period
     *
     * @param period The period to get
     * @return The required charge
     */
    public abstract Double getCharge(Period period);

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public SolidCategory getCategory() {
        return get(FundEvent_Category);
    }
}
