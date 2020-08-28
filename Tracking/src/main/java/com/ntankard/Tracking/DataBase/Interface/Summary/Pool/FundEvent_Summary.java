package com.ntankard.Tracking.DataBase.Interface.Summary.Pool;

import com.ntankard.Tracking.DataBase.Interface.Set.Single_TwoParent_Children_Set;
import com.ntankard.Tracking.DataBase.Interface.Set.TwoParent_Children_Set;
import com.ntankard.dynamicGUI.CoreObject.Field.DataCore.Method_DataCore;
import com.ntankard.dynamicGUI.CoreObject.Field.DataField;
import com.ntankard.dynamicGUI.CoreObject.FieldContainer;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;

import java.util.Map;

@ParameterMap(shouldSave = false)
public class FundEvent_Summary extends PoolSummary<FundEvent> {

    /**
     * Get all the fields for this object
     */
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = PoolSummary.getFieldContainer();

        // ID
        // Period
        // Pool
        // Currency
        // Start =======================================================================================================
        fieldContainer.get(PoolSummary_Start).setDataCore(new Method_DataCore<>(container -> ((FundEvent_Summary) container).getStart_impl()));
        // End =========================================================================================================
        fieldContainer.get(PoolSummary_End).setDataCore(new Method_DataCore<>(container -> ((FundEvent_Summary) container).getEnd_impl()));
        //==============================================================================================================
        // Net
        // TransferSum
        // Missing
        // Valid
        // Parents
        // Children

        return fieldContainer.finaliseContainer(FundEvent_Summary.class);
    }

    /**
     * Create a new StatementEnd object
     */
    public static FundEvent_Summary make(Integer id, Period period, Pool pool) {
        return assembleDataObject(FundEvent_Summary.getFieldContainer(), new FundEvent_Summary()
                , DataObject_Id, id
                , PoolSummary_Period, period
                , PoolSummary_Pool, pool
        );
    }

    // Start End -------------------------------------------------------------------------------------------------------

    private Double getStart_impl() {
        int index = TrackingDatabase.get().get(Period.class).indexOf(getPeriod());
        if (index == 0) {
            return 0.0;
        }
        Period last = TrackingDatabase.get().get(Period.class).get(index - 1);
        return new Single_TwoParent_Children_Set<>(FundEvent_Summary.class, last, getPool()).getItem().getEnd();
    }

    private Double getEnd_impl() {
        return getStart() + getTransferSum();
    }
}
