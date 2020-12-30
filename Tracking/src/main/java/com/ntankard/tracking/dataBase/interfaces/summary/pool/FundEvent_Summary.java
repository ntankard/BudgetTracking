package com.ntankard.tracking.dataBase.interfaces.summary.pool;

import com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.end.EndSource_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Source_Factory;
import com.ntankard.tracking.dataBase.core.period.Period;
import com.ntankard.tracking.dataBase.core.pool.fundEvent.FundEvent;
import com.ntankard.tracking.dataBase.core.pool.Pool;
import com.ntankard.javaObjectDatabase.dataObject.factory.DoubleParentFactory;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.ListDataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.ParameterMap;
import com.ntankard.javaObjectDatabase.database.Database;

import java.util.List;

import static com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties.TRACE_DISPLAY;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.derived.DerivedDataCore_Factory.createMultiParentList;

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
        dataObjectSchema.<List<FundEvent_Summary>>get(FundEvent_Summary_FundEventSummarySet).setDataCore_factory(
                createMultiParentList(
                        FundEvent_Summary.class,
                        PoolSummary_Pool));
        // PreviousBankSummary =========================================================================================
        dataObjectSchema.add(new DataField_Schema<>(FundEvent_Summary_PreviousFundEventSummary, FundEvent_Summary.class, true));
        dataObjectSchema.get(FundEvent_Summary_PreviousFundEventSummary).getProperty(Display_Properties.class).setVerbosityLevel(TRACE_DISPLAY);
        dataObjectSchema.<FundEvent_Summary>get(FundEvent_Summary_PreviousFundEventSummary).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Schema<>(
                        (Derived_DataCore.Calculator<FundEvent_Summary, FundEvent_Summary>) container -> {
                            for (FundEvent_Summary fundEvent_summary : container.getFundEventSummarySet()) {
                                if (fundEvent_summary.getPeriod().getOrder() == container.getPeriod().getOrder() - 1) {
                                    return fundEvent_summary;
                                }
                            }
                            return null;
                        }
                        , new EndSource_Schema<>(FundEvent_Summary_FundEventSummarySet)));
        // Start =======================================================================================================
        dataObjectSchema.<Double>get(PoolSummary_Start).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Schema<>(
                        (Derived_DataCore.Calculator<Double, FundEvent_Summary>) container ->
                                container.getPreviousFundEventSummary() == null ? 0.0 : container.getPreviousFundEventSummary().getEnd()
                        , Source_Factory.makeSourceChain(FundEvent_Summary_PreviousFundEventSummary, PoolSummary_End)));
        // End =========================================================================================================
        dataObjectSchema.<Double>get(PoolSummary_End).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Schema<>(
                        (Derived_DataCore.Calculator<Double, FundEvent_Summary>) container -> container.getStart() + container.getTransferSum()
                        , new EndSource_Schema<>(PoolSummary_Start)
                        , new EndSource_Schema<>(PoolSummary_TransferSum)));
        //==============================================================================================================
        // Net
        // TransferSum =================================================================================================
        dataObjectSchema.<Double>get(PoolSummary_TransferSum).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Schema<>(
                        (Derived_DataCore.Calculator<Double, PoolSummary<FundEvent>>) PoolSummary::getTransferSetSum
                        , new EndSource_Schema<>(PoolSummary_TransferSetSum)));
        //==============================================================================================================
        // Missing
        // Valid =======================================================================================================
        dataObjectSchema.<Boolean>get(PoolSummary_Valid).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Schema<>(
                        (Derived_DataCore.Calculator<Boolean, PoolSummary<FundEvent>>) container -> container.getMissing().equals(0.00)
                        , new EndSource_Schema<>(PoolSummary_Missing)));
        //==============================================================================================================
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(FundEvent_Summary.class);
    }

    /**
     * Constructor
     */
    public FundEvent_Summary(Database database) {
        super(database);
    }

    /**
     * Constructor
     */
    public FundEvent_Summary(Period period, Pool pool) {
        this(period.getTrackingDatabase());
        setAllValues(DataObject_Id, getTrackingDatabase().getNextId()
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
