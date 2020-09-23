package com.ntankard.Tracking.DataBase.Interface.Summary.Pool;

import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.CurrencyBound;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Core.Transfer.HalfTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfer.Transfer;
import com.ntankard.Tracking.DataBase.Interface.Set.Filter.TransferDestination_HalfTransfer_Filter;
import com.ntankard.Tracking.DataBase.Interface.Set.Filter.TransferType_HalfTransfer_Filter;
import com.ntankard.javaObjectDatabase.CoreObject.DataObject;
import com.ntankard.javaObjectDatabase.CoreObject.Field.DataField;
import com.ntankard.javaObjectDatabase.CoreObject.Field.ListDataField;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.Children_ListDataCore;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.Derived_DataCore;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.source.ListSource;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.source.LocalSource;
import com.ntankard.javaObjectDatabase.CoreObject.FieldContainer;
import com.ntankard.javaObjectDatabase.Database.TrackingDatabase;

import java.util.List;

import static com.ntankard.Tracking.DataBase.Core.Transfer.HalfTransfer.HalfTransfer_Currency;
import static com.ntankard.Tracking.DataBase.Core.Transfer.HalfTransfer.HalfTransfer_Value;
import static com.ntankard.javaObjectDatabase.CoreObject.Field.Properties.Display_Properties.*;
import static com.ntankard.javaObjectDatabase.CoreObject.Field.Properties.Display_Properties.DataContext.ZERO_TARGET;
import static com.ntankard.javaObjectDatabase.CoreObject.Field.Properties.Display_Properties.DataType.CURRENCY;

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
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = DataObject.getFieldContainer();

        // ID
        // Period ======================================================================================================
        fieldContainer.add(new DataField<>(PoolSummary_Period, Period.class, false));
        fieldContainer.get(PoolSummary_Period).getDisplayProperties().setVerbosityLevel(DEBUG_DISPLAY);
        // Pool ========================================================================================================
        fieldContainer.add(new DataField<>(PoolSummary_Pool, Pool.class, false));
        // Start =======================================================================================================
        fieldContainer.add(new DataField<>(PoolSummary_Start, Double.class));
        fieldContainer.get(PoolSummary_Start).getDisplayProperties().setDataType(CURRENCY);
        // End =========================================================================================================
        fieldContainer.add(new DataField<>(PoolSummary_End, Double.class));
        fieldContainer.get(PoolSummary_End).getDisplayProperties().setDataType(CURRENCY);
        // Net =========================================================================================================
        fieldContainer.add(new DataField<>(PoolSummary_Net, Double.class, true));
        fieldContainer.get(PoolSummary_Net).getDisplayProperties().setDataType(CURRENCY);
        fieldContainer.<Double>get(PoolSummary_Net).setDataCore(
                new Derived_DataCore<>(
                        (Derived_DataCore.Calculator<Double, PoolSummary<?>>) container ->
                                container.getEnd() - container.getStart()
                        , new LocalSource<>(fieldContainer.get(PoolSummary_Start))
                        , new LocalSource<>(fieldContainer.get(PoolSummary_End))));
        // NetSet ======================================================================================================
        fieldContainer.add(new ListDataField<>(PoolSummary_TransferSet, HalfTransfer.HalfTransferList.class));
        fieldContainer.get(PoolSummary_TransferSet).getDisplayProperties().setVerbosityLevel(TRACE_DISPLAY);
        fieldContainer.<List<HalfTransfer>>get(PoolSummary_TransferSet).setDataCore(
                new Children_ListDataCore<>(
                        HalfTransfer.class,
                        new TransferType_HalfTransfer_Filter(Transfer.class,
                                new TransferDestination_HalfTransfer_Filter(Pool.class)),
                        new Children_ListDataCore.ParentAccess<>(fieldContainer.get(PoolSummary_Period)),
                        new Children_ListDataCore.ParentAccess<>(fieldContainer.get(PoolSummary_Pool))));
        // NetSum ======================================================================================================
        fieldContainer.add(new DataField<>(PoolSummary_TransferSetSum, Double.class));
        fieldContainer.<Double>get(PoolSummary_TransferSetSum).setDataCore(
                new Derived_DataCore<Double, PoolSummary<?>>(
                        container -> {
                            double sum = 0.0;
                            for (HalfTransfer halfTransfer : container.<List<HalfTransfer>>get(PoolSummary_TransferSet)) {
                                sum += halfTransfer.getValue() * halfTransfer.getCurrency().getToPrimary();
                            }
                            return Currency.round(sum);
                        }
                        , new ListSource<>(
                        (ListDataField<HalfTransfer>) fieldContainer.<List<HalfTransfer>>get(PoolSummary_TransferSet),
                        HalfTransfer_Value,
                        HalfTransfer_Currency)));
        // TransferSum =================================================================================================
        fieldContainer.add(new DataField<>(PoolSummary_TransferSum, Double.class));
        fieldContainer.get(PoolSummary_TransferSum).getDisplayProperties().setDataType(CURRENCY);
        // Missing =====================================================================================================
        fieldContainer.add(new DataField<>(PoolSummary_Missing, Double.class));
        fieldContainer.get(PoolSummary_Missing).getDisplayProperties().setDataType(CURRENCY);
        fieldContainer.get(PoolSummary_Missing).getDisplayProperties().setDataContext(ZERO_TARGET);
        fieldContainer.<Double>get(PoolSummary_Missing).setDataCore(
                new Derived_DataCore<>(
                        (Derived_DataCore.Calculator<Double, PoolSummary<?>>) container ->
                                Currency.round(container.getTransferSum() - container.getNet())
                        , new LocalSource<>(fieldContainer.get(PoolSummary_TransferSum))
                        , new LocalSource<>(fieldContainer.get(PoolSummary_Net))));
        // Valid =======================================================================================================
        fieldContainer.add(new DataField<>(PoolSummary_Valid, Boolean.class));
        fieldContainer.get(PoolSummary_Valid).getDisplayProperties().setVerbosityLevel(INFO_DISPLAY);
        // Parents
        // Children

        return fieldContainer.endLayer(PoolSummary.class);
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
        return TrackingDatabase.get().getDefault(Currency.class);
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
