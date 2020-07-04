package com.ntankard.Tracking.DataBase.Interface.Summary.Pool;

import com.ntankard.CoreObject.Field.DataCore.Method_DataCore;
import com.ntankard.CoreObject.FieldContainer;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.Ordered;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Tracking_DataField;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.SolidCategory;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import static com.ntankard.CoreObject.Field.Properties.Display_Properties.TRACE_DISPLAY;

@ParameterMap(shouldSave = false)
public class Category_Summary extends PoolSummary<SolidCategory> implements Ordered {

    public static final String Category_Summary_Order = "getOrder";

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
        fieldContainer.get(PoolSummary_Start).setDataCore(new Method_DataCore<>(container -> -1.0));
        // End =========================================================================================================
        fieldContainer.get(PoolSummary_End).setDataCore(new Method_DataCore<>(container -> -1.0));
        // Net
        // TransferSum
        // Missing
        // Valid =========================================================================================================
        fieldContainer.get(PoolSummary_Valid).setDataCore(new Method_DataCore<>(container -> true));
        // Order =======================================================================================================
        fieldContainer.add(new Tracking_DataField<>(Category_Summary_Order, Integer.class));
        fieldContainer.get(Category_Summary_Order).getDisplayProperties().setVerbosityLevel(TRACE_DISPLAY);
        fieldContainer.get(Category_Summary_Order).setDataCore(new Method_DataCore<>(container -> ((Category_Summary) container).getPool().getOrder()));
        //==============================================================================================================
        // Parents
        // Children

        return fieldContainer.finaliseContainer(Category_Summary.class);
    }

    /**
     * Create a new StatementEnd object
     */
    public static Category_Summary make(Period period, Pool pool) {
        return assembleDataObject(Category_Summary.getFieldContainer(), new Category_Summary()
                , DataObject_Id, -1
                , PoolSummary_Period, period
                , PoolSummary_Pool, pool
        );
    }

    @Override
    public Integer getOrder() {
        return get(Category_Summary_Order);
    }
}
