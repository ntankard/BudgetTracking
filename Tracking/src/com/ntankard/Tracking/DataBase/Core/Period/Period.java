package com.ntankard.Tracking.DataBase.Core.Period;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.Ordered;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.DEBUG_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class Period extends DataObject implements Ordered {

    // My parents

    // My values
    private Integer month;
    private Integer year;
    private Period last = null;  // Not a parent to prevent a circular dependency
    private Period next = null;  // Not a parent to prevent a circular dependency

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getMonth", "getYear"})
    public Period(Integer id, Integer month, Integer year) {
        super(id);
        this.month = month;
        this.year = year;
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
     * {@inheritDoc
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T extends DataObject> List<T> sourceOptions(Class<T> type, String fieldName) {
        if (fieldName.equals("Next")) {
            List<T> options = new ArrayList<>();
            for (Period period : TrackingDatabase.get().get(Period.class)) {
                if (getOrder() == period.getOrder() - 1) {
                    options.add((T) period);
                }
            }
            return options;
        }
        if (fieldName.equals("Last")) {
            List<T> options = new ArrayList<>();
            for (Period period : TrackingDatabase.get().get(Period.class)) {
                if (getOrder() == period.getOrder() + 1) {
                    options.add((T) period);
                }
            }
            return options;
        }
        return super.sourceOptions(type, fieldName);
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

        Period newPeriod = new Period(TrackingDatabase.get().getNextId(), nextMonth, nextYear);
        newPeriod.setLast(this);
        return newPeriod;
    }

    /**
     * Get the first Period know about
     *
     * @return The first Period know about
     */
    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
    @DisplayProperties(order = 7)
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

    @SetterProperties(localSourceMethod = "sourceOptions", displaySet = false)
    public void setNext(Period next) {
        if (getOrder() != next.getOrder() - 1) {
            throw new IllegalArgumentException("Not the next month");
        }
        this.next = next;
    }

    @SetterProperties(localSourceMethod = "sourceOptions", displaySet = false)
    public void setLast(Period last) {
        if (getOrder() != last.getOrder() + 1) {
            throw new IllegalArgumentException("Not the last month");
        }
        this.last = last;
    }
}
