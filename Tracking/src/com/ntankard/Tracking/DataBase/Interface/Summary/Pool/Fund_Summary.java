package com.ntankard.Tracking.DataBase.Interface.Summary.Pool;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Fund.Fund;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;
import com.ntankard.Tracking.DataBase.Interface.Set.MultiParent_Set;

import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY;
import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class Fund_Summary extends PoolSummary<Fund> {

    @ParameterMap(shouldSave = false)
    public Fund_Summary(Integer id, Period period, Fund pool) {
        super(id, period, pool);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @Override
    @DisplayProperties(order = 5, dataType = CURRENCY)
    public Double getStart() {
        if (getPeriod().getLast() == null)
            return 0.0;
        return new MultiParent_Set<>(Fund_Summary.class, getPeriod().getLast(), getPool()).get().get(0).getEnd();
    }

    @Override
    @DisplayProperties(order = 6, dataType = CURRENCY)
    public Double getEnd() {
        return getStart() + getReal();
    }

    @Override
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 10)
    public Boolean isValid() {
        return true;
    }
}
