package com.ntankard.Tracking.DataBase.Interface.Summary.Pool;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Fund.Fund;
import com.ntankard.Tracking.DataBase.Core.Transfers.Transfer;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY;

@ClassExtensionProperties(includeParent = true)
public class Fund_Summary extends PoolSummary<Fund> {

    @ParameterMap(shouldSave = false)
    public Fund_Summary(Period period, Fund pool, Class<? extends Transfer> transferType) {
        super(period, pool, transferType);
    }

    @ParameterMap(shouldSave = false)
    public Fund_Summary(Period period, Fund pool) {
        super(period, pool);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @Override
    @DisplayProperties(order = 5, dataType = CURRENCY)
    public Double getStart() {
        if (getPeriod().getLast() == null)
            return 0.0;
        return new Fund_Summary(getPeriod().getLast(), getPool()).getEnd();
    }

    @Override
    @DisplayProperties(order = 6, dataType = CURRENCY)
    public Double getEnd() {
        return getStart() + getTransferSum();
    }
}
