package com.ntankard.Tracking.DataBase.Core;

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

    // My values
    private Integer month;
    private Integer year;
    private Period last;  // Not a parent to prevent a circular dependency
    private Period next;  // Not a parent to prevent a circular dependency

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getMonth", "getYear", "getLast", "getNext"})
    public Period(Integer id, Integer month, Integer year, Period last, Period next) {
        super(id);
        this.month = month;
        this.year = year;
        this.last = last;
        this.next = next;
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

        return new Period(TrackingDatabase.get().getNextId(), nextMonth, nextYear, this, null);
    }

    /**
     * Get the first Period know about
     *
     * @return The first Period know about
     */
    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
    @DisplayProperties(order = 6)
    public Period getFirst() {
        Period first = this;
        while (true) {
            if (first.getLast() == null) {
                return first;
            }
            first = first.getLast();
        }
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

    /**
     * {@inheritDoc
     */
    @Override
    public String toString() {
        return year + "-" + month;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @DisplayProperties(order = 2)
    public Integer getMonth() {
        return month;
    }

    @DisplayProperties(order = 3)
    public Integer getYear() {
        return year;
    }

    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
    @DisplayProperties(order = 4)
    public Period getLast() {
        return last;
    }

    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
    @DisplayProperties(order = 5)
    public Period getNext() {
        return next;
    }

    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
    @DisplayProperties(order = 6)
    public Integer getOrder() {
        return getYear() * 12 + getMonth();
    }

    //------------------------------------------------------------------------------------------------------------------
    //##################################################### Setter #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public void setNext(Period next) {
        this.next = next;
    }
}
