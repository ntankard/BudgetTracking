package com.ntankard.Tracking.DataBase.Interface.Summary.Pool;

import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.javaObjectDatabase.CoreObject.Factory.DoubleParentFactory;
import com.ntankard.javaObjectDatabase.CoreObject.Field.DataField_Schema;
import com.ntankard.javaObjectDatabase.CoreObject.Field.ListDataField_Schema;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.Children_ListDataCore;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.Derived_DataCore;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.source.ExternalSource;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.source.LocalSource;
import com.ntankard.javaObjectDatabase.CoreObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.Database.ParameterMap;
import com.ntankard.javaObjectDatabase.Database.TrackingDatabase;

import java.util.List;

import static com.ntankard.javaObjectDatabase.CoreObject.Field.Properties.Display_Properties.TRACE_DISPLAY;

@ParameterMap(shouldSave = false)
public class FundEvent_Summary extends PoolSummary<FundEvent> {

    public interface FundEvent_SummaryList extends List<FundEvent_Summary> {
    }

    public static final String FundEvent_Summary_FundEventSummarySet = "getFundEventSummarySet";
    public static final String FundEvent_Summary_PreviousFundEventSummary = "getPreviousFundEventSummary";

    public static DoubleParentFactory<?, ?, ?> Factory = new DoubleParentFactory<>(
            FundEvent_Summary.class,
            Period.class,
            PoolSummary_Period, FundEvent.class,
            PoolSummary_Pool, (generator, secondaryGenerator) -> FundEvent_Summary.make(TrackingDatabase.get().getNextId(), generator, secondaryGenerator)
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
        // BankSummarySet ==============================================================================================
        dataObjectSchema.add(new ListDataField_Schema<>(FundEvent_Summary_FundEventSummarySet, FundEvent_SummaryList.class));
        dataObjectSchema.get(FundEvent_Summary_FundEventSummarySet).getDisplayProperties().setVerbosityLevel(TRACE_DISPLAY);
        dataObjectSchema.<List<FundEvent_Summary>>get(FundEvent_Summary_FundEventSummarySet).setDataCore_factory(
                new Children_ListDataCore.Children_ListDataCore_Factory<>(
                        FundEvent_Summary.class,
                        new Children_ListDataCore.ParentAccess.ParentAccess_Factory<>(PoolSummary_Pool)));
        // PreviousBankSummary =========================================================================================
        dataObjectSchema.add(new DataField_Schema<>(FundEvent_Summary_PreviousFundEventSummary, FundEvent_Summary.class, true));
        dataObjectSchema.get(FundEvent_Summary_PreviousFundEventSummary).getDisplayProperties().setVerbosityLevel(TRACE_DISPLAY);
        dataObjectSchema.<FundEvent_Summary>get(FundEvent_Summary_PreviousFundEventSummary).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>(
                        (Derived_DataCore.Calculator<FundEvent_Summary, FundEvent_Summary>) container -> {
                            for (FundEvent_Summary fundEvent_summary : container.getFundEventSummarySet()) {
                                if (fundEvent_summary.getPeriod().getOrder() == container.getPeriod().getOrder() - 1) {
                                    return fundEvent_summary;
                                }
                            }
                            return null;
                        }
                        , new LocalSource.LocalSource_Factory<>(FundEvent_Summary_FundEventSummarySet)));
        // Start =======================================================================================================
        dataObjectSchema.<Double>get(PoolSummary_Start).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>(
                        (Derived_DataCore.Calculator<Double, FundEvent_Summary>) container ->
                                container.getPreviousFundEventSummary() == null ? 0.0 : container.getPreviousFundEventSummary().getEnd()
                        , new ExternalSource.ExternalSource_Factory<>(FundEvent_Summary_PreviousFundEventSummary, PoolSummary_End)));
        // End =========================================================================================================
        dataObjectSchema.<Double>get(PoolSummary_End).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>(
                        (Derived_DataCore.Calculator<Double, FundEvent_Summary>) container -> container.getStart() + container.getTransferSum()
                        , new LocalSource.LocalSource_Factory<>(PoolSummary_Start)
                        , new LocalSource.LocalSource_Factory<>(PoolSummary_TransferSum)));
        //==============================================================================================================
        // Net
        // TransferSum =================================================================================================
        dataObjectSchema.<Double>get(PoolSummary_TransferSum).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>(
                        (Derived_DataCore.Calculator<Double, PoolSummary<FundEvent>>) PoolSummary::getTransferSetSum
                        , new LocalSource.LocalSource_Factory<>(PoolSummary_TransferSetSum)));
        //==============================================================================================================
        // Missing
        // Valid =======================================================================================================
        dataObjectSchema.<Boolean>get(PoolSummary_Valid).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>(
                        (Derived_DataCore.Calculator<Boolean, PoolSummary<FundEvent>>) container -> container.getMissing().equals(0.00)
                        , new LocalSource.LocalSource_Factory<>(PoolSummary_Missing)));
        //==============================================================================================================
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(FundEvent_Summary.class);
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

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public List<FundEvent_Summary> getFundEventSummarySet() {
        return get(FundEvent_Summary_FundEventSummarySet);
    }

    public FundEvent_Summary getPreviousFundEventSummary() {
        return get(FundEvent_Summary_PreviousFundEventSummary);
    }
}
