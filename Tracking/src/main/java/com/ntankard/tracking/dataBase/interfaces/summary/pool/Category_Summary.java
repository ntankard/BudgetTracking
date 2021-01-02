package com.ntankard.tracking.dataBase.interfaces.summary.pool;

import com.ntankard.javaObjectDatabase.dataField.dataCore.Static_DataCore_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema.Calculator;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.end.End_Source_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Source_Factory;
import com.ntankard.tracking.dataBase.core.pool.category.Category;
import com.ntankard.javaObjectDatabase.dataObject.factory.DoubleParentFactory;
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
        dataObjectSchema.<Double>get(PoolSummary_TransferSum).setDataCore_schema(
                new Derived_DataCore_Schema<>(
                        (Calculator<Double, PoolSummary<Category>>) PoolSummary::getTransferSetSum
                        , new End_Source_Schema<>(PoolSummary_TransferSetSum)));
        // =============================================================================================================
        // Missing
        // Valid =======================================================================================================
        dataObjectSchema.get(PoolSummary_Valid).setDataCore_schema(new Static_DataCore_Schema<>(true));
        // Order =======================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Category_Summary_Order, Integer.class));
        dataObjectSchema.<Integer>get(Category_Summary_Order).setDataCore_schema(
                new Derived_DataCore_Schema<>(
                        (Calculator<Integer, Category_Summary>) container ->
                                container.getPeriod().getOrder()
                        , Source_Factory.makeSourceChain(PoolSummary_Pool, SolidCategory_Order)));
        //==============================================================================================================
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(Category_Summary.class);
    }

    /**
     * Constructor
     */
    public Category_Summary(Database database) {
        super(database);
    }

    /**
     * Constructor
     */
    public Category_Summary(Period period, Pool pool) {
        this(period.getTrackingDatabase());
        setAllValues(DataObject_Id, getTrackingDatabase().getNextId()
                , PoolSummary_Period, period
                , PoolSummary_Pool, pool
        );
    }

    @Override
    public Integer getOrder() {
        return get(Category_Summary_Order);
    }
}
