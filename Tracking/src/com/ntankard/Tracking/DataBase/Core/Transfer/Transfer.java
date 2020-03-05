package com.ntankard.Tracking.DataBase.Core.Transfer;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.CurrencyBound;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY;
import static com.ntankard.ClassExtension.MemberProperties.*;

@ClassExtensionProperties(includeParent = true)
public abstract class Transfer extends DataObject implements CurrencyBound {

    // My parents
    private Period period;
    private Pool source;

    // My values
    private String description;

    // Not parents on purpose
    private HalfTransfer sourceTransfer;
    private HalfTransfer destinationTransfer;

    /**
     * Constructor
     */
    @ParameterMap(shouldSave = false)
    public Transfer(Integer id, String description,
                    Period period, Pool source) {
        super(id);
        if (description == null) throw new IllegalArgumentException("Description is null");
        if (period == null) throw new IllegalArgumentException("Period is null");
        if (source == null) throw new IllegalArgumentException("Source is null");
        this.description = description;
        this.source = source;
        this.period = period;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 2000000)
    public List<DataObject> getParents() {
        List<DataObject> toReturn = new ArrayList<>();
        toReturn.add(getPeriod());
        toReturn.add(getSource());
        return toReturn;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void add() {
        super.add();
        sourceTransfer = new HalfTransfer(TrackingDatabase.get().getNextId(), getPeriod(true), getPool(true), getCurrency(true), this);
        sourceTransfer.add();
        destinationTransfer = new HalfTransfer(TrackingDatabase.get().getNextId(), getPeriod(false), getPool(false), getCurrency(false), this);
        destinationTransfer.add();
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void remove() {
        sourceTransfer.remove();
        destinationTransfer.remove();
        super.remove_impl();
    }

    /**
     * {@inheritDoc
     */
    @Override
    @SuppressWarnings("SuspiciousMethodCalls")
    public <T extends DataObject> List<T> sourceOptions(Class<T> type, String fieldName) {
        if (fieldName.equals("Source")) {
            if (getDestination() instanceof Bank || getDestination() instanceof Category) {
                List<T> toReturn = TrackingDatabase.get().get(type);
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
        return description;
    }

    @DisplayProperties(order = 1200000)
    public Period getPeriod() {
        return period;
    }

    @DisplayProperties(order = 1300000)
    public Pool getSource() {
        return source;
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
        if (description == null) throw new IllegalArgumentException("Description is null");
        this.description = description;
    }

    @SetterProperties(localSourceMethod = "sourceOptions")
    protected void setSource(Pool source) {
        if (source == null) throw new IllegalArgumentException("Source is null");
        if (source.equals(getDestination())) throw new IllegalArgumentException("Destination equals source");
        this.source.notifyChildUnLink(this);
        this.source = source;
        this.source.notifyChildLink(this);

        getSourceTransfer().setPool(getSource());
        validateParents();
    }

    @SetterProperties(localSourceMethod = "sourceOptions")
    public void setPeriod(Period period) {
        if (period == null) throw new IllegalArgumentException("Source is null");
        this.period.notifyChildUnLink(this);
        this.period = period;
        this.period.notifyChildLink(this);

        getSourceTransfer().setPeriod(getPeriod());
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
            return getValue();
        } else {
            return -getValue();
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
