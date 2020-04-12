package com.ntankard.Tracking.DataBase.Interface.Summary.Pool;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.DataObject_Field;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Field;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.Ordered;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.SolidCategory;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import java.util.List;

import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY;
import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;
import static com.ntankard.ClassExtension.MemberProperties.TRACE_DISPLAY;

@ParameterMap(shouldSave = false)
@ClassExtensionProperties(includeParent = true)
public class Category_Summary extends PoolSummary<SolidCategory> implements Ordered {

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
    public static Category_Summary make(Period period, Pool pool) {
        return assembleDataObject(Category_Summary.getFields(), new Category_Summary()
                , "getId", -1
                , "getPeriod", period
                , "getPool", pool
        );
    }

    // Start End -------------------------------------------------------------------------------------------------------

    @Override
    @DisplayProperties(order = 5, dataType = CURRENCY)
    public Double getStart() {
        return -1.0;
    }

    @Override
    @DisplayProperties(order = 6, dataType = CURRENCY)
    public Double getEnd() {
        return -1.0;
    }

    // Validity --------------------------------------------------------------------------------------------------------

    @Override
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 10)
    public Boolean isValid() {
        return true;
    }

    @MemberProperties(verbosityLevel = TRACE_DISPLAY)
    @DisplayProperties(order = 23)
    public Integer getOrder() {
        return getPool().getOrder();
    }
}
