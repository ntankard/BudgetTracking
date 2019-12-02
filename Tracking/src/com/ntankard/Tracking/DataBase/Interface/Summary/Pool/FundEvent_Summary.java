package com.ntankard.Tracking.DataBase.Interface.Summary.Pool;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Fund.Fund;
import com.ntankard.Tracking.DataBase.Core.Pool.Fund.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Core.Transfers.Transfer;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;
import com.ntankard.Tracking.DataBase.Interface.Set.MultiParent_Set;
import com.ntankard.Tracking.DataBase.Interface.Summary.TransferSet_Summary;

import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY;

@ClassExtensionProperties(includeParent = true)
public class FundEvent_Summary extends PoolSummary<Fund> {

    // My Parents
    private FundEvent fundEvent;

    @ParameterMap(shouldSave = false)
    public FundEvent_Summary(Period period, FundEvent fundEvent, Class<? extends Transfer> transferType) {
        super(period, fundEvent.getFund(), transferType);
        this.fundEvent = fundEvent;
    }

    @ParameterMap(shouldSave = false)
    public FundEvent_Summary(Period period, FundEvent fundEvent) {
        super(period, fundEvent.getFund());
        this.fundEvent = fundEvent;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @Override
    @DisplayProperties(order = 5, dataType = CURRENCY)
    public Double getStart() {
        if (getPeriod().getLast() == null) {
            return 0.0;
        }
        return new FundEvent_Summary(getPeriod().getLast(), fundEvent).getEnd();
    }

    @Override
    @DisplayProperties(order = 6, dataType = CURRENCY)
    public Double getEnd() {
        return getStart() + getTransferSum();
    }

    @Override
    @DisplayProperties(order = 8, dataType = CURRENCY)
    public Double getTransferSum() {
        return new TransferSet_Summary<>(new MultiParent_Set<>(transferType, getPeriod(), fundEvent), getPool()).getTotal() / getCurrency().getToPrimary();
    }
}
