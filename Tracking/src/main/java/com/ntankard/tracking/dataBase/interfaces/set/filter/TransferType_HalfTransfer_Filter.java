package com.ntankard.tracking.dataBase.interfaces.set.filter;

import com.ntankard.tracking.dataBase.core.transfer.HalfTransfer;
import com.ntankard.tracking.dataBase.core.transfer.Transfer;
import com.ntankard.javaObjectDatabase.util.set.SetFilter;

@SuppressWarnings("rawtypes")
public class TransferType_HalfTransfer_Filter extends SetFilter<HalfTransfer> {

    /**
     * The transfer type that the HalfTransfer should belong to
     */
    private Class<? extends Transfer> transferType;

    /**
     * Constructor
     */
    public TransferType_HalfTransfer_Filter(Class<? extends Transfer> transferType, SetFilter<HalfTransfer> nestFilter) {
        super(nestFilter);
        this.transferType = transferType;
    }

    /**
     * Constructor
     */
    public TransferType_HalfTransfer_Filter(Class<? extends Transfer> transferType) {
        this(transferType, null);
    }

    /**
     * @inheritDoc
     */
    @Override
    protected boolean shouldAdd_Impl(HalfTransfer halfTransfer) {
        return transferType.isAssignableFrom(halfTransfer.getTransfer().getClass());
    }
}
