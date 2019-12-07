package com.ntankard.Tracking.DataBase.Core.Pool.FundEvent;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.DEBUG_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public abstract class FundEvent extends Pool {

    // My parent
    private Category category;

    /**
     * Constructor
     */
    @ParameterMap(shouldSave = false)
    public FundEvent(Integer id, String name, Category category) {
        super(id, name);
        this.category = category;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 21)
    public List<DataObject> getParents() {
        List<DataObject> toReturn = new ArrayList<>();
        toReturn.add(getCategory());
        return toReturn;
    }

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

    @DisplayProperties(order = 3)
    public Category getCategory() {
        return category;
    }
}
