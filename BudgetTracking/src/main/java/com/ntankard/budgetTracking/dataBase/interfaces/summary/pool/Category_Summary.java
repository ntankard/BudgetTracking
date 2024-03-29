package com.ntankard.budgetTracking.dataBase.interfaces.summary.pool;

import com.ntankard.budgetTracking.dataBase.core.period.Period;
import com.ntankard.budgetTracking.dataBase.core.pool.Pool;
import com.ntankard.budgetTracking.dataBase.core.pool.category.SolidCategory;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.Static_DataCore_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema.Calculator;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.dataObject.factory.DoubleParentFactory;
import com.ntankard.javaObjectDatabase.dataObject.interfaces.Ordered;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.javaObjectDatabase.database.ParameterMap;

import java.util.List;

import static com.ntankard.budgetTracking.dataBase.core.pool.category.SolidCategory.SolidCategory_Order;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.DataCore_Factory.createDirectDerivedDataCore;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Source_Factory.makeSourceChain;

@ParameterMap(shouldSave = false)
public class Category_Summary extends PoolSummary<SolidCategory> implements Ordered {

    public interface Category_SummaryList extends List<Category_Summary> {
    }

    public static final String Category_Summary_Order = "getOrder";

    public static DoubleParentFactory<?, ?, ?> Factory = new DoubleParentFactory<>(
            Category_Summary.class,
            Period.class,
            PoolSummary_Period, SolidCategory.class,
            PoolSummary_Pool, Category_Summary::new
    );

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = PoolSummary.getDataObjectSchema();

        // Class behavior
        dataObjectSchema.setMyFactory(Factory);

        // ID
        // Period
        // Pool
        // Currency
        // Start =======================================================================================================
        dataObjectSchema.get(PoolSummary_Start).setDataCore_schema(new Static_DataCore_Schema<>(-1.0));
        // End =========================================================================================================
        dataObjectSchema.get(PoolSummary_End).setDataCore_schema(new Static_DataCore_Schema<>(-1.0));
        // =============================================================================================================
        // Net
        // TransferSum =================================================================================================
        dataObjectSchema.<Double>get(PoolSummary_TransferSum).setDataCore_schema(createDirectDerivedDataCore(PoolSummary_TransferSetSum));
        // =============================================================================================================
        // Missing
        // Valid =======================================================================================================
        dataObjectSchema.get(PoolSummary_Valid).setDataCore_schema(new Static_DataCore_Schema<>(true));
        // Order =======================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Category_Summary_Order, Integer.class));
        dataObjectSchema.<Integer>get(Category_Summary_Order).setDataCore_schema(
                new Derived_DataCore_Schema<>(
                        (Calculator<Integer, Category_Summary>) container ->
                                container.getPeriod().getOrder() * 1000 + container.getPool().getOrder()
                        , makeSourceChain(PoolSummary_Pool, SolidCategory_Order)));
        //==============================================================================================================
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(Category_Summary.class);
    }

    /**
     * Constructor
     */
    public Category_Summary(Database database, Object... args) {
        super(database, args);
    }

    /**
     * Constructor
     */
    public Category_Summary(Period period, Pool pool) {
        super(period.getTrackingDatabase()
                , PoolSummary_Period, period
                , PoolSummary_Pool, pool
        );
    }

    @Override
    public Integer getOrder() {
        return get(Category_Summary_Order);
    }
}
