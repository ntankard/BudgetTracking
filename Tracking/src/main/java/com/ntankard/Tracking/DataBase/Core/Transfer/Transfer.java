package com.ntankard.Tracking.DataBase.Core.Transfer;

import com.ntankard.dynamicGUI.CoreObject.Field.DataCore.Derived_DataCore;
import com.ntankard.dynamicGUI.CoreObject.Field.DataCore.ValueRead_DataCore;
import com.ntankard.dynamicGUI.CoreObject.FieldContainer;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.CurrencyBound;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Tracking_DataField;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Core.Receipt;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;

import java.util.List;

import static com.ntankard.dynamicGUI.CoreObject.Field.Properties.Display_Properties.DataType.CURRENCY;
import static com.ntankard.dynamicGUI.CoreObject.Field.Properties.Display_Properties.INFO_DISPLAY;

public abstract class Transfer extends DataObject implements CurrencyBound {

    // Not parents on purpose
    private HalfTransfer sourceTransfer = null;
    private HalfTransfer destinationTransfer = null;

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String Transfer_Description = "getDescription";
    public static final String Transfer_Period = "getPeriod";
    public static final String Transfer_Source = "getSource";
    public static final String Transfer_Value = "getValue";
    public static final String Transfer_Currency = "getCurrency";
    public static final String Transfer_Destination = "getDestination";

    public static final String Transfer_SourceCurrencyGet = "getSourceCurrencyGet";
    public static final String Transfer_DestinationCurrencyGet = "getDestinationCurrencyGet";

    public static final String Transfer_SourcePeriodGet = "getSourcePeriodGet";
    public static final String Transfer_DestinationPeriodGet = "getDestinationPeriodGet";

    /**
     * Get all the fields for this object
     */
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = DataObject.getFieldContainer();

        // ID
        // Description =================================================================================================
        fieldContainer.add(new Tracking_DataField<>(Transfer_Description, String.class));
        fieldContainer.get(Transfer_Description).setDataCore(new ValueRead_DataCore<>(true));
        // Period ======================================================================================================
        fieldContainer.add(new Tracking_DataField<>(Transfer_Period, Period.class));
        fieldContainer.<Period>get(Transfer_Period).setDataCore(new ValueRead_DataCore<>(true));
        // Source ======================================================================================================
        //fieldContainer.add(new Tracking_DataField<>(Transfer_Source, Pool.class));
        // Value =======================================================================================================
        fieldContainer.add(new Tracking_DataField<>(Transfer_Value, Double.class));
        fieldContainer.get(Transfer_Value).getDisplayProperties().setDataType(CURRENCY);
        // Currency ====================================================================================================
        fieldContainer.add(new Tracking_DataField<>(Transfer_Currency, Currency.class));
        fieldContainer.get(Transfer_Currency).getDisplayProperties().setVerbosityLevel(INFO_DISPLAY);
        // Destination =================================================================================================
        fieldContainer.add(new Tracking_DataField<>(Transfer_Destination, Pool.class));
        // SourceCurrencyGet ===========================================================================================
        fieldContainer.add(new Tracking_DataField<>(Transfer_SourceCurrencyGet, Currency.class, false, false));
        fieldContainer.<Currency>get(Transfer_SourceCurrencyGet).setDataCore(
                new Derived_DataCore<>
                        (container -> ((Transfer) container).getCurrency()
                                , new Derived_DataCore.LocalSource<>(fieldContainer.get(Transfer_Currency))));
        // DestinationCurrencyGet  =====================================================================================
        fieldContainer.add(new Tracking_DataField<>(Transfer_DestinationCurrencyGet, Currency.class, false, false));
        fieldContainer.<Currency>get(Transfer_DestinationCurrencyGet).setDataCore(
                new Derived_DataCore<>
                        (container -> ((Transfer) container).getCurrency()
                                , new Derived_DataCore.LocalSource<>(fieldContainer.get(Transfer_Currency))));
        // SourcePeriodGet =================================================================================================
        fieldContainer.add(new Tracking_DataField<>(Transfer_SourcePeriodGet, Period.class, false, false));
        fieldContainer.<Period>get(Transfer_SourcePeriodGet).setDataCore(
                new Derived_DataCore<>
                        (container -> ((Transfer) container).getPeriod()
                                , new Derived_DataCore.LocalSource<>(fieldContainer.get(Transfer_Period))));
        // DestinationPeriodGet =================================================================================================
        fieldContainer.add(new Tracking_DataField<>(Transfer_DestinationPeriodGet, Period.class, false, false));
        fieldContainer.<Period>get(Transfer_DestinationPeriodGet).setDataCore(
                new Derived_DataCore<>
                        (container -> ((Transfer) container).getPeriod()
                                , new Derived_DataCore.LocalSource<>(fieldContainer.get(Transfer_Period))));
        //==============================================================================================================
        // Parents
        // Children

        return fieldContainer.endLayer(Transfer.class);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void add() {
        if (sourceTransfer != null) throw new IllegalStateException("This object has already been added");
        if (destinationTransfer != null) throw new IllegalStateException("This object has already been added");
        super.add();
        sourceTransfer = HalfTransfer.make(TrackingDatabase.get().getNextId(), this, true);
        sourceTransfer.add();
        destinationTransfer = HalfTransfer.make(TrackingDatabase.get().getNextId(), this, false);
        destinationTransfer.add();
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void remove() {
        if (sourceTransfer == null) throw new IllegalStateException("This object has already been removed");
        if (destinationTransfer == null) throw new IllegalStateException("This object has already been removed");
        sourceTransfer.remove();
        destinationTransfer.remove();
        if (getChildren(Receipt.class).size() != 0) {
            getChildren(Receipt.class).get(0).remove();
        }
        remove_impl();
    }

    /**
     * {@inheritDoc
     */
    @Override
    @SuppressWarnings("SuspiciousMethodCalls")
    public <T extends DataObject> List<T> sourceOptions(Class<T> type, String fieldName) {
        if (fieldName.equals("Source")) {
            if (getDestination() instanceof Bank) {
                List<T> toReturn = super.sourceOptions(type, fieldName);
                toReturn.remove(getDestination());
                return toReturn;
            }
        }
        return super.sourceOptions(type, fieldName);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public String getDescription() {
        return get(Transfer_Description);
    }

    public Period getPeriod() {
        return get(Transfer_Period);
    }

    public Pool getSource() {
        return get(Transfer_Source);
    }

    public Double getValue() {
        return get(Transfer_Value);
    }

    public Currency getCurrency() {
        return get(Transfer_Currency);
    }

    public Pool getDestination() {
        return get(Transfer_Destination);
    }

    public HalfTransfer toChaneGetSourceTransfer() {
        return sourceTransfer;
    }

    public HalfTransfer toChangeGetDestinationTransfer() {
        return destinationTransfer;
    }

    public Currency getDestinationCurrencyGet() {
        return get(Transfer_DestinationCurrencyGet);
    }

    public Currency getSourceCurrencyGet() {
        return get(Transfer_SourceCurrencyGet);
    }

    public Period getDestinationPeriodGet() {
        return get(Transfer_DestinationPeriodGet);
    }

    public Period getSourcePeriodGet() {
        return get(Transfer_SourcePeriodGet);
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################# HalfTransfer Interface #############################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get the value for this half of the transaction
     *
     * @param isSource Get the source? Otherwise get the destination
     * @return The value this side should use
     */
    protected Double getValue(boolean isSource) {
        if (isSource) {
            return -getValue();
        } else {
            return getValue();
        }
    }
}
