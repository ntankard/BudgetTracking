package com.ntankard.Tracking.DataBase.Interface.Summary.Pool;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.DataObject_Field;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Field;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;

import java.util.List;

import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY;

@ParameterMap(shouldSave = false)
@ClassExtensionProperties(includeParent = true)
public class FundEvent_Summary extends PoolSummary<FundEvent> {

    /**
     * Get all the fields for this object
     */
    public static List<Field<?>> getFields() {
        List<Field<?>> toReturn = PoolSummary.getFields();
        toReturn.add(new DataObject_Field<>("getPeriod", Period.class));
        toReturn.add(new DataObject_Field<>("getPool", Pool.class));
        return toReturn;
    }

    /**
     * Create a new StatementEnd object
     */
    public static FundEvent_Summary make(Period period, Pool pool) {
        return assembleDataObject(FundEvent_Summary.getFields(), new FundEvent_Summary()
                , "getId", -1
                , "getPeriod", period
                , "getPool", pool
        );
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
        return FundEvent_Summary.make(last, getPool()).getEnd();
    }

    @Override
    @DisplayProperties(order = 6, dataType = CURRENCY)
    public Double getEnd() {
        return getStart() + getTransferSum();
    }
}
