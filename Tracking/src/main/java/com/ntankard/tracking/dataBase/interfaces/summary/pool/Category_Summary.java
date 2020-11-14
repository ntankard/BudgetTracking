package com.ntankard.tracking.dataBase.interfaces.summary.pool;

import com.ntankard.javaObjectDatabase.database.Database_Schema;
import com.ntankard.tracking.dataBase.core.pool.category.Category;
import com.ntankard.javaObjectDatabase.dataObject.factory.DoubleParentFactory;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore;
import com.ntankard.javaObjectDatabase.dataField.dataCore.Static_DataCore;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.External_Source;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Local_Source;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.dataObject.interfaces.Ordered;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.tracking.dataBase.core.period.Period;
import com.ntankard.tracking.dataBase.core.pool.category.SolidCategory;
import com.ntankard.tracking.dataBase.core.pool.Pool;
import com.ntankard.javaObjectDatabase.database.ParameterMap;
import com.ntankard.javaObjectDatabase.database.Database;

import java.util.List;

import static com.ntankard.tracking.dataBase.core.pool.category.SolidCategory.SolidCategory_Order;

@ParameterMap(shouldSave = false)
public class Category_Summary extends PoolSummary<SolidCategory> implements Ordered {

    public interface Category_SummaryList extends List<Category_Summary> {
    }

    public static final String Category_Summary_Order = "getOrder";

    public static DoubleParentFactory<?, ?, ?> Factory = new DoubleParentFactory<>(
            Category_Summary.class,
            Period.class,
            PoolSummary_Period, SolidCategory.class,
            PoolSummary_Pool, (generator, secondaryGenerator) -> Category_Summary.make(generator.getTrackingDatabase().getNextId(), generator, secondaryGenerator)
    );

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getFieldContainer() {
        DataObject_Schema dataObjectSchema = PoolSummary.getFieldContainer();

        // Class behavior
        dataObjectSchema.setMyFactory(Factory);

        // ID
        // Period
        // Pool
        // Currency
        // Start =======================================================================================================
        dataObjectSchema.get(PoolSummary_Start).setDataCore_factory(new Static_DataCore.Static_DataCore_Factory<>(-1.0));
        // End =========================================================================================================
        dataObjectSchema.get(PoolSummary_End).setDataCore_factory(new Static_DataCore.Static_DataCore_Factory<>(-1.0));
        // =============================================================================================================
        // Net
        // TransferSum =================================================================================================
        dataObjectSchema.<Double>get(PoolSummary_TransferSum).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>(
                        (Derived_DataCore.Calculator<Double, PoolSummary<Category>>) PoolSummary::getTransferSetSum
                        , new Local_Source.LocalSource_Factory<>(PoolSummary_TransferSetSum)));
        // =============================================================================================================
        // Missing
        // Valid =======================================================================================================
        dataObjectSchema.get(PoolSummary_Valid).setDataCore_factory(new Static_DataCore.Static_DataCore_Factory<>(true));
        // Order =======================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Category_Summary_Order, Integer.class));
        dataObjectSchema.<Integer>get(Category_Summary_Order).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>(
                        (Derived_DataCore.Calculator<Integer, Category_Summary>) container ->
                                container.getPeriod().getOrder()
                        , new External_Source.ExternalSource_Factory<>(PoolSummary_Pool, SolidCategory_Order)));
        //==============================================================================================================
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(Category_Summary.class);
    }

    /**
     * Create a new StatementEnd object
     */
    public static Category_Summary make(Integer id, Period period, Pool pool) {
        Database database = period.getTrackingDatabase();
        Database_Schema database_schema = database.getSchema();
        return assembleDataObject(database, database_schema.getClassSchema(Category_Summary.class), new Category_Summary()
                , DataObject_Id, id
                , PoolSummary_Period, period
                , PoolSummary_Pool, pool
        );
    }

    @Override
    public Integer getOrder() {
        return get(Category_Summary_Order);
    }
}
