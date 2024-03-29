package com.ntankard.budgetTracking.dataBase.interfaces.summary.pool;

import com.ntankard.budgetTracking.dataBase.core.Currency;
import com.ntankard.budgetTracking.dataBase.core.baseObject.interfaces.CurrencyBound;
import com.ntankard.budgetTracking.dataBase.core.period.Period;
import com.ntankard.budgetTracking.dataBase.core.pool.Pool;
import com.ntankard.budgetTracking.dataBase.core.transfer.HalfTransfer;
import com.ntankard.budgetTracking.dataBase.core.transfer.Transfer;
import com.ntankard.budgetTracking.dataBase.interfaces.set.filter.TransferDestination_HalfTransfer_Filter;
import com.ntankard.budgetTracking.dataBase.interfaces.set.filter.TransferType_HalfTransfer_Filter;
import com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties;
import com.ntankard.dynamicGUI.javaObjectDatabase.Displayable_DataObject;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.ListDataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema.Calculator;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.Database;

import java.util.List;

import static com.ntankard.budgetTracking.dataBase.core.transfer.HalfTransfer.HalfTransfer_Currency;
import static com.ntankard.budgetTracking.dataBase.core.transfer.HalfTransfer.HalfTransfer_Value;
import static com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties.DataContext.ZERO_TARGET;
import static com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties.DataType.CURRENCY;
import static com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties.INFO_DISPLAY;
import static com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties.TRACE_DISPLAY;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.DataCore_Factory.createMultiParentList;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Source_Factory.makeSharedStepSourceChain;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Source_Factory.makeSourceChain;

public abstract class PoolSummary<PoolType extends Pool> extends DataObject implements CurrencyBound {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String PoolSummary_Period = "getPeriod";
    public static final String PoolSummary_Pool = "getPool";
    public static final String PoolSummary_Start = "getStart";
    public static final String PoolSummary_End = "getEnd";
    public static final String PoolSummary_Net = "getNet";
    public static final String PoolSummary_TransferSet = "getTransferSet";
    public static final String PoolSummary_TransferSetSum = "getTransferSetSum"; // In primary currency
    public static final String PoolSummary_TransferSum = "getTransferSum";
    public static final String PoolSummary_Missing = "getMissing";
    public static final String PoolSummary_Valid = "isValid";

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = Displayable_DataObject.getDataObjectSchema();

        // ID
        // Period ======================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(PoolSummary_Period, Period.class, false));
        // Pool ========================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(PoolSummary_Pool, Pool.class, false));
        // Start =======================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(PoolSummary_Start, Double.class));
        dataObjectSchema.get(PoolSummary_Start).getProperty(Display_Properties.class).setDataType(CURRENCY);
        // End =========================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(PoolSummary_End, Double.class));
        dataObjectSchema.get(PoolSummary_End).getProperty(Display_Properties.class).setDataType(CURRENCY);
        // Net =========================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(PoolSummary_Net, Double.class, true));
        dataObjectSchema.get(PoolSummary_Net).getProperty(Display_Properties.class).setDataType(CURRENCY);
        dataObjectSchema.<Double>get(PoolSummary_Net).setDataCore_schema(
                new Derived_DataCore_Schema<>(
                        (Calculator<Double, PoolSummary<?>>) container ->
                                container.getEnd() - container.getStart()
                        , makeSourceChain(PoolSummary_Start)
                        , makeSourceChain(PoolSummary_End)));
        // NetSet ======================================================================================================
        dataObjectSchema.add(new ListDataField_Schema<>(PoolSummary_TransferSet, HalfTransfer.HalfTransferList.class));
        dataObjectSchema.get(PoolSummary_TransferSet).getProperty(Display_Properties.class).setVerbosityLevel(TRACE_DISPLAY);
        dataObjectSchema.<List<HalfTransfer>>get(PoolSummary_TransferSet).setDataCore_schema(
                createMultiParentList(
                        HalfTransfer.class,
                        new TransferType_HalfTransfer_Filter(Transfer.class,
                                new TransferDestination_HalfTransfer_Filter(Pool.class)), PoolSummary_Period,
                        PoolSummary_Pool
                ));
        // NetSum ======================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(PoolSummary_TransferSetSum, Double.class));
        dataObjectSchema.<Double>get(PoolSummary_TransferSetSum).setDataCore_schema(
                new Derived_DataCore_Schema<Double, PoolSummary<?>>(
                        container -> {
                            double sum = 0.0;
                            for (HalfTransfer halfTransfer : container.<List<HalfTransfer>>get(PoolSummary_TransferSet)) {
                                sum += halfTransfer.getValue() * halfTransfer.getCurrency().getToPrimary();
                            }
                            return Currency.round(sum);
                        }
                        , makeSharedStepSourceChain(
                        PoolSummary_TransferSet,
                        HalfTransfer_Value,
                        HalfTransfer_Currency)));
        // TransferSum =================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(PoolSummary_TransferSum, Double.class));
        dataObjectSchema.get(PoolSummary_TransferSum).getProperty(Display_Properties.class).setDataType(CURRENCY);
        // Missing =====================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(PoolSummary_Missing, Double.class));
        dataObjectSchema.get(PoolSummary_Missing).getProperty(Display_Properties.class).setDataType(CURRENCY);
        dataObjectSchema.get(PoolSummary_Missing).getProperty(Display_Properties.class).setDataContext(ZERO_TARGET);
        dataObjectSchema.<Double>get(PoolSummary_Missing).setDataCore_schema(
                new Derived_DataCore_Schema<>(
                        (Calculator<Double, PoolSummary<?>>) container ->
                                Currency.round(container.getTransferSum() - container.getNet())
                        , makeSourceChain(PoolSummary_TransferSum)
                        , makeSourceChain(PoolSummary_Net)));
        // Valid =======================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(PoolSummary_Valid, Boolean.class));
        dataObjectSchema.get(PoolSummary_Valid).getProperty(Display_Properties.class).setVerbosityLevel(INFO_DISPLAY);
        // Parents
        // Children

        return dataObjectSchema.endLayer(PoolSummary.class);
    }

    /**
     * Constructor
     */
    public PoolSummary(Database database, Object... args) {
        super(database, args);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public Period getPeriod() {
        return get(PoolSummary_Period);
    }

    public PoolType getPool() {
        return get(PoolSummary_Pool);
    }

    @Override
    public Currency getCurrency() {
        return getTrackingDatabase().getDefault(Currency.class);
    }

    public Double getStart() {
        return get(PoolSummary_Start);
    }

    public Double getEnd() {
        return get(PoolSummary_End);
    }

    public Double getNet() {
        return get(PoolSummary_Net);
    }

    public Double getTransferSum() {
        return get(PoolSummary_TransferSum);
    }

    public Double getMissing() {
        return get(PoolSummary_Missing);
    }

    public Boolean isValid() {
        return get(PoolSummary_Valid);
    }

    public Double getTransferSetSum() {
        return get(PoolSummary_TransferSetSum);
    }
}
