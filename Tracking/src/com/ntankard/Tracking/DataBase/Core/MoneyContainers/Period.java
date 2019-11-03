package com.ntankard.Tracking.DataBase.Core.MoneyContainers;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.DEBUG_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class Period extends DataObject {

    // My parents
    private Period last;

    // My values
    private int month;
    private int year;

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getMonth", "getYear", "getLast"})
    public Period(String id, int month, int year, Period last) {
        super(id);
        this.month = month;
        this.year = year;
        this.last = last;
    }

    /**
     * Generate a new period that comes after this one
     *
     * @return A new period that comes after this one
     */
    public Period generateNext() {
        int nextMonth = month;
        int nextYear = year;
        nextMonth++;
        if (nextMonth > 12) {
            nextMonth -= 12;
            nextYear++;
        }

        return new Period(TrackingDatabase.get().getNextId(Period.class), nextMonth, nextYear, this);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public String toString() {
        return year + "-" + month;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 21)
    public List<DataObject> getParents() {
        return new ArrayList<>();
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @DisplayProperties(order = 2)
    public int getMonth() {
        return month;
    }

    @DisplayProperties(order = 3)
    public int getYear() {
        return year;
    }

    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
    @DisplayProperties(order = 8)
    public Period getLast() {
        return last;
    }
}
