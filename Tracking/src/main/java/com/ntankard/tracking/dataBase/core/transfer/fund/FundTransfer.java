package com.ntankard.tracking.dataBase.core.transfer.fund;

import com.ntankard.tracking.dataBase.core.Currency;
import com.ntankard.tracking.dataBase.core.period.Period;
import com.ntankard.javaObjectDatabase.coreObject.field.dataCore.derived.Derived_DataCore;
import com.ntankard.javaObjectDatabase.coreObject.field.dataCore.derived.source.LocalSource;
import com.ntankard.javaObjectDatabase.coreObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.coreObject.DataObject;
import com.ntankard.javaObjectDatabase.coreObject.field.DataField_Schema;
import com.ntankard.tracking.dataBase.core.pool.fundEvent.FundEvent;
import com.ntankard.tracking.dataBase.core.transfer.Transfer;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.javaObjectDatabase.coreObject.field.properties.Display_Properties.ALWAYS_DISPLAY;

public abstract class FundTransfer extends Transfer {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------


    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getFieldContainer() {
        DataObject_Schema dataObjectSchema = Transfer.getFieldContainer();

        // ID
        // Description
        // Period
        // Source ======================================================================================================
        dataObjectSchema.add(Transfer_Period, new DataField_Schema<>(Transfer_Source, FundEvent.class));
        // Value
        // Currency ====================================================================================================
        dataObjectSchema.get(Transfer_Currency).getDisplayProperties().setVerbosityLevel(ALWAYS_DISPLAY);
        // Destination
        // SourceCurrencyGet
        // DestinationCurrencyGet ======================================================================================
        dataObjectSchema.<Currency>get(Transfer_DestinationCurrencyGet).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>
                        (container -> ((Transfer) container).getCurrency()
                                , new LocalSource.LocalSource_Factory<>(Transfer_Currency)));
        // SourcePeriodGet
        // DestinationPeriodGet ========================================================================================
        dataObjectSchema.<Period>get(Transfer_DestinationPeriodGet).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>
                        (container -> ((Transfer) container).getPeriod()
                                , new LocalSource.LocalSource_Factory<>(Transfer_Period)));
        // Parents
        // Children

        return dataObjectSchema.endLayer(FundTransfer.class);
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