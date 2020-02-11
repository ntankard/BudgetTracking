package com.ntankard.Tracking.DataBase.Core.Period;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.Ordered;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.DEBUG_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public abstract class Period extends DataObject implements Ordered {

    /**
     * Constructor
     */
    @ParameterMap(shouldSave = false)
    public Period(Integer id) {
        super(id);
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 2000000)
    public List<DataObject> getParents() {
        return new ArrayList<>();
    }

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

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    // 1000000--getID
    // 2000000--getParents (Above)
    // 3000000--getChildren
}
