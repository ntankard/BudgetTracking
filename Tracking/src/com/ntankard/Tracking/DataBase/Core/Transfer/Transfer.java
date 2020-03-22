package com.ntankard.Tracking.DataBase.Core.Transfer;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.DataObject_Field;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Field;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.CurrencyBound;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Core.Receipt;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;

import java.util.List;

import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY;
import static com.ntankard.ClassExtension.MemberProperties.DEBUG_DISPLAY;
import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public abstract class Transfer extends DataObject implements CurrencyBound {

    // Not parents on purpose
    private HalfTransfer sourceTransfer = null;
    private HalfTransfer destinationTransfer = null;

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static List<Field<?>> getFields() {
        List<Field<?>> toReturn = DataObject.getFields();
        toReturn.add(new Field<>("getDescription", String.class));
        toReturn.add(new DataObject_Field<>("getPeriod", Period.class));
        toReturn.add(new DataObject_Field<>("getSource", Pool.class));
        return toReturn;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void add() {
        if (sourceTransfer != null) throw new IllegalStateException("This object has already been added");
        if (destinationTransfer != null) throw new IllegalStateException("This object has already been added");
        super.add();
        sourceTransfer = HalfTransfer.make(TrackingDatabase.get().getNextId(), getPeriod(true), getPool(true), getCurrency(true), this);
        sourceTransfer.add();
        destinationTransfer = HalfTransfer.make(TrackingDatabase.get().getNextId(), getPeriod(false), getPool(false), getCurrency(false), this);
        destinationTransfer.add();
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void remove() {
        if (sourceTransfer == null) throw new IllegalStateException("This object has already been removed");
        if (destinationTransfer == null) throw new IllegalStateException("This object has already been removed");
        sourceTransfer.remove();
        destinationTransfer.remove();
        if (getChildren(Receipt.class).size() != 0) {
            getChildren(Receipt.class).get(0).remove();
        }
        super.remove_impl();
    }

    /**
     * Update the values of the 2 half transfers, call in any set
     */
    protected void updateHalfTransfer() {
        getSourceTransfer().setPeriod(getPeriod(true));
        getSourceTransfer().setPool(getPool(true));
        getSourceTransfer().setCurrency(getCurrency(true));

        getDestinationTransfer().setPeriod(getPeriod(false));
        getDestinationTransfer().setPool(getPool(false));
        getDestinationTransfer().setCurrency(getCurrency(false));
    }

    /**
     * {@inheritDoc
     */
    @Override
    @SuppressWarnings("SuspiciousMethodCalls")
    public <T extends DataObject> List<T> sourceOptions(Class<T> type, String fieldName) {
        if (fieldName.equals("Source")) {
            if (getDestination() instanceof Bank) {
                List<T> toReturn = super.sourceOptions(type, fieldName);
                toReturn.remove(getDestination());
                return toReturn;
            }
        }
        return super.sourceOptions(type, fieldName);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    // 1000000--getID

    @DisplayProperties(order = 1100000)
    public String getDescription() {
        return get("getDescription");
    }

    @DisplayProperties(order = 1200000)
    public Period getPeriod() {
        return get("getPeriod");
    }

    @DisplayProperties(order = 1300000)
    public Pool getSource() {
        return get("getSource");
    }

    @DisplayProperties(order = 1400000, dataType = CURRENCY)
    public abstract Double getValue();

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 1500000)
    public abstract Currency getCurrency();

    @DisplayProperties(order = 1600000)
    public abstract Pool getDestination();

    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 1700000)
    public HalfTransfer getSourceTransfer() {
        return sourceTransfer;
    }

    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 1800000)
    public HalfTransfer getDestinationTransfer() {
        return destinationTransfer;
    }

    // 2000000--getParents (Above)
    // 3000000--getChildren

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public void setDescription(String description) {
        set("getDescription", description);
        updateHalfTransfer();
    }

    @SetterProperties(localSourceMethod = "sourceOptions")
    protected void setSource(Pool source) {
        set("getSource", source);
        updateHalfTransfer();
        validateParents();
    }

    @SetterProperties(localSourceMethod = "sourceOptions")
    public void setPeriod(Period period) {
        set("getPeriod", period);
        updateHalfTransfer();
        validateParents();
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################# HalfTransfer Interface #############################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get the period for this half of the transaction
     *
     * @param isSource Get the source? Otherwise get the destination
     * @return The period this side should use
     */
    protected Period getPeriod(boolean isSource) {
        return getPeriod();
    }

    /**
     * Get the pool for this half of the transaction
     *
     * @param isSource Get the source? Otherwise get the destination
     * @return The pool this side should use
     */
    protected Pool getPool(boolean isSource) {
        if (isSource) {
            return getSource();
        } else {
            return getDestination();
        }
    }

    /**
     * Get the value for this half of the transaction
     *
     * @param isSource Get the source? Otherwise get the destination
     * @return The value this side should use
     */
    protected Double getValue(boolean isSource) {
        if (isSource) {
            return -getValue();
        } else {
            return getValue();
        }
    }

    /**
     * Get the currency for this half of the transaction
     *
     * @param isSource Get the source? Otherwise get the destination
     * @return The currency this side should use
     */
    protected Currency getCurrency(boolean isSource) {
        return getCurrency();
    }
}

// 1000000--getID
// 1100000----getDescription
// 1200000----getPeriod
// 1300000----getSource
// 1400000----getValue
// 1500000----getCurrency
// 1600000----getDestination
// 1700000----getSourceTransfer
// 1800000----getDestinationTransfer
// 2000000--getParents (Above)
// 3000000--getChildren
