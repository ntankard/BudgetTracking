package com.ntankard.Tracking.DataBase.Core.Transfer;

import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.Derived_DataCore;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.source.LocalSource;
import com.ntankard.javaObjectDatabase.CoreObject.FieldContainer;
import com.ntankard.javaObjectDatabase.CoreObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.CurrencyBound;
import com.ntankard.javaObjectDatabase.CoreObject.Field.DataField;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Core.Receipt;
import com.ntankard.javaObjectDatabase.Database.TrackingDatabase;

import java.util.List;

import static com.ntankard.javaObjectDatabase.CoreObject.Field.Properties.Display_Properties.DataType.CURRENCY;
import static com.ntankard.javaObjectDatabase.CoreObject.Field.Properties.Display_Properties.INFO_DISPLAY;

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
        fieldContainer.add(new DataField<>(Transfer_Description, String.class));
        fieldContainer.get(Transfer_Description).setCanEdit(true);
        // Period ======================================================================================================
        fieldContainer.add(new DataField<>(Transfer_Period, Period.class));
        //==============================================================================================================
        // Source
        // Value =======================================================================================================
        fieldContainer.add(new DataField<>(Transfer_Value, Double.class));
        fieldContainer.get(Transfer_Value).getDisplayProperties().setDataType(CURRENCY);
        // Currency ====================================================================================================
        fieldContainer.add(new DataField<>(Transfer_Currency, Currency.class));
        fieldContainer.get(Transfer_Currency).getDisplayProperties().setVerbosityLevel(INFO_DISPLAY);
        // Destination =================================================================================================
        fieldContainer.add(new DataField<>(Transfer_Destination, Pool.class));
        // SourceCurrencyGet ===========================================================================================
        fieldContainer.add(new DataField<>(Transfer_SourceCurrencyGet, Currency.class));
        fieldContainer.get(Transfer_SourceCurrencyGet).setTellParent(false);
        fieldContainer.<Currency>get(Transfer_SourceCurrencyGet).setDataCore(
                new Derived_DataCore<>
                        (container -> ((Transfer) container).getCurrency()
                                , new LocalSource<>(fieldContainer.get(Transfer_Currency))));
        // DestinationCurrencyGet  =====================================================================================
        fieldContainer.add(new DataField<>(Transfer_DestinationCurrencyGet, Currency.class));
        fieldContainer.get(Transfer_DestinationCurrencyGet).setTellParent(false);
        // SourcePeriodGet =================================================================================================
        fieldContainer.add(new DataField<>(Transfer_SourcePeriodGet, Period.class));
        fieldContainer.get(Transfer_SourcePeriodGet).setTellParent(false);
        fieldContainer.<Period>get(Transfer_SourcePeriodGet).setDataCore(
                new Derived_DataCore<>
                        (container -> ((Transfer) container).getPeriod()
                                , new LocalSource<>(fieldContainer.get(Transfer_Period))));
        // DestinationPeriodGet =================================================================================================
        fieldContainer.add(new DataField<>(Transfer_DestinationPeriodGet, Period.class));
        fieldContainer.get(Transfer_DestinationPeriodGet).setTellParent(false);
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
