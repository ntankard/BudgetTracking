package com.ntankard.Tracking.DataBase.Interface.Summary.Pool;

import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.javaObjectDatabase.CoreObject.Factory.DoubleParentFactory;
import com.ntankard.javaObjectDatabase.CoreObject.Field.DataField;
import com.ntankard.javaObjectDatabase.CoreObject.Field.ListDataField;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.Children_ListDataCore;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.Derived_DataCore;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.source.ExternalSource;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.source.LocalSource;
import com.ntankard.javaObjectDatabase.CoreObject.FieldContainer;
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
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = PoolSummary.getFieldContainer();

        // Class behavior
        fieldContainer.setMyFactory(Factory);

        // ID
        // Period
        // Pool
        // Currency
        // BankSummarySet ==============================================================================================
        fieldContainer.add(new ListDataField<>(FundEvent_Summary_FundEventSummarySet, FundEvent_SummaryList.class));
        fieldContainer.get(FundEvent_Summary_FundEventSummarySet).getDisplayProperties().setVerbosityLevel(TRACE_DISPLAY);
        fieldContainer.<List<FundEvent_Summary>>get(FundEvent_Summary_FundEventSummarySet).setDataCore(
                new Children_ListDataCore<>(
                        FundEvent_Summary.class,
                        new Children_ListDataCore.ParentAccess<>(fieldContainer.get(PoolSummary_Pool))));
        // PreviousBankSummary =========================================================================================
        fieldContainer.add(new DataField<>(FundEvent_Summary_PreviousFundEventSummary, FundEvent_Summary.class, true));
        fieldContainer.get(FundEvent_Summary_PreviousFundEventSummary).getDisplayProperties().setVerbosityLevel(TRACE_DISPLAY);
        fieldContainer.<FundEvent_Summary>get(FundEvent_Summary_PreviousFundEventSummary).setDataCore(
                new Derived_DataCore<>(
                        (Derived_DataCore.Calculator<FundEvent_Summary, FundEvent_Summary>) container -> {
                            for (FundEvent_Summary fundEvent_summary : container.getFundEventSummarySet()) {
                                if (fundEvent_summary.getPeriod().getOrder() == container.getPeriod().getOrder() - 1) {
                                    return fundEvent_summary;
                                }
                            }
                            return null;
                        }
                        , new LocalSource<>(fieldContainer.get(FundEvent_Summary_FundEventSummarySet))));
        // Start =======================================================================================================
        fieldContainer.<Double>get(PoolSummary_Start).setDataCore(
                new Derived_DataCore<>(
                        (Derived_DataCore.Calculator<Double, FundEvent_Summary>) container ->
                                container.getPreviousFundEventSummary() == null ? 0.0 : container.getPreviousFundEventSummary().getEnd()
                        , new ExternalSource<>(fieldContainer.get(FundEvent_Summary_PreviousFundEventSummary), PoolSummary_End)));
        // End =========================================================================================================
        fieldContainer.<Double>get(PoolSummary_End).setDataCore(
                new Derived_DataCore<>(
                        (Derived_DataCore.Calculator<Double, FundEvent_Summary>) container -> container.getStart() + container.getTransferSum()
                        , new LocalSource<>(fieldContainer.get(PoolSummary_Start))
                        , new LocalSource<>(fieldContainer.get(PoolSummary_TransferSum))));
        //==============================================================================================================
        // Net
        // TransferSum =================================================================================================
        fieldContainer.<Double>get(PoolSummary_TransferSum).setDataCore(
                new Derived_DataCore<>(
                        (Derived_DataCore.Calculator<Double, PoolSummary<FundEvent>>) PoolSummary::getTransferSetSum
                        , new LocalSource<>(fieldContainer.get(PoolSummary_TransferSetSum))));
        //==============================================================================================================
        // Missing
        // Valid =======================================================================================================
        fieldContainer.<Boolean>get(PoolSummary_Valid).setDataCore(
                new Derived_DataCore<>(
                        (Derived_DataCore.Calculator<Boolean, PoolSummary<FundEvent>>) container -> container.getMissing().equals(0.00)
                        , new LocalSource<>(fieldContainer.get(PoolSummary_Missing))));
        //==============================================================================================================
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
