package com.ntankard.budgetTracking.dataBase.interfaces.summary.pool;

import com.ntankard.budgetTracking.dataBase.core.period.Period;
import com.ntankard.budgetTracking.dataBase.core.pool.Pool;
import com.ntankard.budgetTracking.dataBase.core.pool.fundEvent.FundEvent;
import com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.ListDataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema.Calculator;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.dataObject.factory.DoubleParentFactory;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.javaObjectDatabase.database.ParameterMap;

import java.util.List;

import static com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties.TRACE_DISPLAY;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.DataCore_Factory.createDirectDerivedDataCore;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.DataCore_Factory.createMultiParentList;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Source_Factory.makeSourceChain;

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
            PoolSummary_Pool, FundEvent_Summary::new
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
        // BankSummarySet ==============================================================================================
        dataObjectSchema.add(new ListDataField_Schema<>(FundEvent_Summary_FundEventSummarySet, FundEvent_SummaryList.class));
        dataObjectSchema.get(FundEvent_Summary_FundEventSummarySet).getProperty(Display_Properties.class).setVerbosityLevel(TRACE_DISPLAY);
        dataObjectSchema.<List<FundEvent_Summary>>get(FundEvent_Summary_FundEventSummarySet).setDataCore_schema(
                createMultiParentList(
                        FundEvent_Summary.class,
                        PoolSummary_Pool));
        // PreviousBankSummary =========================================================================================
        dataObjectSchema.add(new DataField_Schema<>(FundEvent_Summary_PreviousFundEventSummary, FundEvent_Summary.class, true));
        dataObjectSchema.get(FundEvent_Summary_PreviousFundEventSummary).getProperty(Display_Properties.class).setVerbosityLevel(TRACE_DISPLAY);
        dataObjectSchema.<FundEvent_Summary>get(FundEvent_Summary_PreviousFundEventSummary).setDataCore_schema(
                new Derived_DataCore_Schema<>(
                        (Calculator<FundEvent_Summary, FundEvent_Summary>) container -> {
                            for (FundEvent_Summary fundEvent_summary : container.getFundEventSummarySet()) {
                                if (fundEvent_summary.getPeriod().getOrder() == container.getPeriod().getOrder() - 1) {
                                    return fundEvent_summary;
                                }
                            }
                            return null;
                        }
                        , makeSourceChain(FundEvent_Summary_FundEventSummarySet)));
        // Start =======================================================================================================
        dataObjectSchema.<Double>get(PoolSummary_Start).setDataCore_schema(
                new Derived_DataCore_Schema<>(
                        (Calculator<Double, FundEvent_Summary>) container ->
                                container.getPreviousFundEventSummary() == null ? 0.0 : container.getPreviousFundEventSummary().getEnd()
                        , makeSourceChain(FundEvent_Summary_PreviousFundEventSummary, PoolSummary_End)));
        // End =========================================================================================================
        dataObjectSchema.<Double>get(PoolSummary_End).setDataCore_schema(
                new Derived_DataCore_Schema<>(
                        (Calculator<Double, FundEvent_Summary>) container -> container.getStart() + container.getTransferSum()
                        , makeSourceChain(PoolSummary_Start)
                        , makeSourceChain(PoolSummary_TransferSum)));
        //==============================================================================================================
        // Net
        // TransferSum =================================================================================================
        dataObjectSchema.<Double>get(PoolSummary_TransferSum).setDataCore_schema(createDirectDerivedDataCore(PoolSummary_TransferSetSum));
        //==============================================================================================================
        // Missing
        // Valid =======================================================================================================
        dataObjectSchema.<Boolean>get(PoolSummary_Valid).setDataCore_schema(
                new Derived_DataCore_Schema<>(
                        (Calculator<Boolean, PoolSummary<FundEvent>>) container -> container.getMissing().equals(0.00)
                        , makeSourceChain(PoolSummary_Missing)));
        //==============================================================================================================
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(FundEvent_Summary.class);
    }

    /**
     * Constructor
     */
    public FundEvent_Summary(Database database, Object... args) {
        super(database, args);
    }

    /**
     * Constructor
     */
    public FundEvent_Summary(Period period, Pool pool) {
        super(period.getTrackingDatabase()
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
