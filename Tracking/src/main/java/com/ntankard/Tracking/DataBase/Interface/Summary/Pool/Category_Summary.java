package com.ntankard.Tracking.DataBase.Interface.Summary.Pool;

import com.ntankard.Tracking.DataBase.Core.Pool.Category.Category;
import com.ntankard.javaObjectDatabase.CoreObject.Factory.DoubleParentFactory;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.Derived_DataCore;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.Static_DataCore;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.source.ExternalSource;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.source.LocalSource;
import com.ntankard.javaObjectDatabase.CoreObject.FieldContainer;
import com.ntankard.javaObjectDatabase.CoreObject.Interface.Ordered;
import com.ntankard.javaObjectDatabase.CoreObject.Field.DataField;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.SolidCategory;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.javaObjectDatabase.Database.ParameterMap;
import com.ntankard.javaObjectDatabase.Database.TrackingDatabase;

import java.util.List;

import static com.ntankard.Tracking.DataBase.Core.Pool.Category.SolidCategory.SolidCategory_Order;

@ParameterMap(shouldSave = false)
public class Category_Summary extends PoolSummary<SolidCategory> implements Ordered {

    public interface Category_SummaryList extends List<Category_Summary> {
    }

    public static final String Category_Summary_Order = "getOrder";

    public static DoubleParentFactory<?, ?, ?> Factory = new DoubleParentFactory<>(
            Category_Summary.class,
            Period.class,
            PoolSummary_Period, SolidCategory.class,
            PoolSummary_Pool, (generator, secondaryGenerator) -> Category_Summary.make(TrackingDatabase.get().getNextId(), generator, secondaryGenerator)
    );

    /**
     * Get all the fields for this object
     */
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = PoolSummary.getFieldContainer();

        // Class behavior
        fieldContainer.setMyFactory(Factory);

        // ID
        // Period
        // Pool
        // Currency
        // Start =======================================================================================================
        fieldContainer.get(PoolSummary_Start).setDataCore(new Static_DataCore<>(-1.0));
        // End =========================================================================================================
        fieldContainer.get(PoolSummary_End).setDataCore(new Static_DataCore<>(-1.0));
        // =============================================================================================================
        // Net
        // TransferSum =================================================================================================
        fieldContainer.<Double>get(PoolSummary_TransferSum).setDataCore(
                new Derived_DataCore<>(
                        (Derived_DataCore.Calculator<Double, PoolSummary<Category>>) PoolSummary::getTransferSetSum
                        , new LocalSource<>(fieldContainer.get(PoolSummary_TransferSetSum))));
        // =============================================================================================================
        // Missing
        // Valid =======================================================================================================
        fieldContainer.get(PoolSummary_Valid).setDataCore(new Static_DataCore<>(true));
        // Order =======================================================================================================
        fieldContainer.add(new DataField<>(Category_Summary_Order, Integer.class));
        fieldContainer.<Integer>get(Category_Summary_Order).setDataCore(
                new Derived_DataCore<>(
                        (Derived_DataCore.Calculator<Integer, Category_Summary>) container ->
                                container.getPeriod().getOrder()
                        , new ExternalSource<>(fieldContainer.get(PoolSummary_Pool), SolidCategory_Order)));
        //==============================================================================================================
        // Parents
        // Children

        return fieldContainer.finaliseContainer(Category_Summary.class);
    }

    /**
     * Create a new StatementEnd object
     */
    public static Category_Summary make(Integer id, Period period, Pool pool) {
        return assembleDataObject(Category_Summary.getFieldContainer(), new Category_Summary()
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
