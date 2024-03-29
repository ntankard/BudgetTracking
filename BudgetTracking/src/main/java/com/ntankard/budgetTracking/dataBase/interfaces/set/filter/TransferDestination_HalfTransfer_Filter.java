package com.ntankard.budgetTracking.dataBase.interfaces.set.filter;

import com.ntankard.budgetTracking.dataBase.core.pool.Pool;
import com.ntankard.budgetTracking.dataBase.core.transfer.HalfTransfer;
import com.ntankard.javaObjectDatabase.util.set.SetFilter;

public class TransferDestination_HalfTransfer_Filter extends SetFilter<HalfTransfer> {

    /**
     * The transfer type that the HalfTransfer should belong to
     */
    private Class<? extends Pool> destination;

    /**
     * Constructor
     * TransferDestination_HalfTransfer_Filter
     */
    public TransferDestination_HalfTransfer_Filter(Class<? extends Pool> destination, SetFilter<HalfTransfer> nestFilter) {
        super(nestFilter);
        this.destination = destination;
    }

    /**
     * Constructor
     */
    public TransferDestination_HalfTransfer_Filter(Class<? extends Pool> destination) {
        this(destination, null);
    }

    /**
     * @inheritDoc
     */
    @Override
    protected boolean shouldAdd_Impl(HalfTransfer halfTransfer) {
        return destination.isAssignableFrom(halfTransfer.getTransfer().getDestination().getClass());
    }
}
