package com.ntankard.Tracking.DataBase.Core.Transfer.Fund;

import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.Derived_DataCore;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.source.LocalSource;
import com.ntankard.javaObjectDatabase.CoreObject.FieldContainer;
import com.ntankard.javaObjectDatabase.CoreObject.DataObject;
import com.ntankard.javaObjectDatabase.CoreObject.Field.DataField;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Core.Transfer.Transfer;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.javaObjectDatabase.CoreObject.Field.Properties.Display_Properties.ALWAYS_DISPLAY;

public abstract class FundTransfer extends Transfer {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------


    /**
     * Get all the fields for this object
     */
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = Transfer.getFieldContainer();

        // ID
        // Description
        // Period
        // Source ======================================================================================================
        fieldContainer.add(Transfer_Period, new DataField<>(Transfer_Source, FundEvent.class));
        // Value
        // Currency ====================================================================================================
        fieldContainer.get(Transfer_Currency).getDisplayProperties().setVerbosityLevel(ALWAYS_DISPLAY);
        // Destination
        // SourceCurrencyGet
        // DestinationCurrencyGet ======================================================================================
        fieldContainer.<Currency>get(Transfer_DestinationCurrencyGet).setDataCore(
                new Derived_DataCore<>
                        (container -> ((Transfer) container).getCurrency()
                                , new LocalSource<>(fieldContainer.get(Transfer_Currency))));
        // SourcePeriodGet
        // DestinationPeriodGet ========================================================================================
        fieldContainer.<Period>get(Transfer_DestinationPeriodGet).setDataCore(
                new Derived_DataCore<>
                        (container -> ((Transfer) container).getPeriod()
                                , new LocalSource<>(fieldContainer.get(Transfer_Period))));
        // Parents
        // Children

        return fieldContainer.endLayer(FundTransfer.class);
    }

    /**
     * {@inheritDoc
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T extends DataObject> List<T> sourceOptions(Class<T> type, String fieldName) {
        if (fieldName.equals("Source")) {
            List<T> toReturn = new ArrayList<>();
            for (FundEvent fundEvent : super.sourceOptions(FundEvent.class, fieldName)) {
                toReturn.add((T) fundEvent);
            }
            return toReturn;
        }
        return super.sourceOptions(type, fieldName);
    }
}
