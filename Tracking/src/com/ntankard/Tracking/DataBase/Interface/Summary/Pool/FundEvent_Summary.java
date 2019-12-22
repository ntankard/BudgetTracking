package com.ntankard.Tracking.DataBase.Interface.Summary.Pool;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;

import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY;

@ClassExtensionProperties(includeParent = true)
public class FundEvent_Summary extends PoolSummary<FundEvent> {

    @ParameterMap(shouldSave = false)
    public FundEvent_Summary(Period period, FundEvent fundEvent) {
        super(period, fundEvent);
    }

    // Start End -------------------------------------------------------------------------------------------------------

    @Override
    @DisplayProperties(order = 5, dataType = CURRENCY)
    public Double getStart() {
        int index = TrackingDatabase.get().get(Period.class).indexOf(getPeriod());
        if (index == 0) {
            return 0.0;
        }
        Period last = TrackingDatabase.get().get(Period.class).get(index - 1);
        return new FundEvent_Summary(last, getPool()).getEnd();
    }

    @Override
    @DisplayProperties(order = 6, dataType = CURRENCY)
    public Double getEnd() {
        return getStart() + getTransferSum();
    }
}
