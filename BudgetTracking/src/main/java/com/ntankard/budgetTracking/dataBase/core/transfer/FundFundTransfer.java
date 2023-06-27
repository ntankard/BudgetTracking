package com.ntankard.budgetTracking.dataBase.core.transfer;

import com.ntankard.budgetTracking.dataBase.core.Currency;
import com.ntankard.budgetTracking.dataBase.core.period.Period;
import com.ntankard.budgetTracking.dataBase.core.pool.Pool;
import com.ntankard.budgetTracking.dataBase.core.pool.fundEvent.FundEvent;
import com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.Database;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties.ALWAYS_DISPLAY;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.DataCore_Factory.createDirectDerivedDataCore;

public class FundFundTransfer extends Transfer {
    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------


    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = Transfer.getDataObjectSchema();

        // ID
        // Description
        // Period
        // Source ======================================================================================================
        dataObjectSchema.add(Transfer_Period, new DataField_Schema<>(Transfer_Source, FundEvent.class));
        dataObjectSchema.get(Transfer_Source).setManualCanEdit(true);
        // Value =======================================================================================================
        dataObjectSchema.get(Transfer_Value).setManualCanEdit(true);
        // Currency ====================================================================================================
        dataObjectSchema.get(Transfer_Currency).getProperty(Display_Properties.class).setVerbosityLevel(ALWAYS_DISPLAY);
        dataObjectSchema.get(Transfer_Currency).setManualCanEdit(true);
        // Destination =================================================================================================
        dataObjectSchema.<Pool>get(Transfer_Destination).setManualCanEdit(true);
        // SourceCurrencyGet
        // DestinationCurrencyGet ======================================================================================
        dataObjectSchema.<Currency>get(Transfer_DestinationCurrencyGet).setDataCore_schema(createDirectDerivedDataCore(Transfer_Currency));
        // SourcePeriodGet
        // DestinationPeriodGet ========================================================================================
        dataObjectSchema.<Period>get(Transfer_DestinationPeriodGet).setDataCore_schema(createDirectDerivedDataCore(Transfer_Period));
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(FundFundTransfer.class);
    }

    /**
     * Constructor
     */
    public FundFundTransfer(Database database, Object... args) {
        super(database, args);
    }

    /**
     * Constructor
     */
    public FundFundTransfer(String description, Period period, FundEvent source, Double value, Currency currency, FundEvent destination) {
        super(period.getTrackingDatabase()
                , Transfer_Description, description
                , Transfer_Period, period
                , Transfer_Source, source
                , Transfer_Value, value
                , Transfer_Currency, currency
                , Transfer_Destination, destination
        );
    }

    /**
     * @inheritDoc
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
        if (fieldName.equals("Destination")) {
            List<T> toReturn = new ArrayList<>();
            for (FundEvent fundEvent : super.sourceOptions(FundEvent.class, fieldName)) {
                toReturn.add((T) fundEvent);
            }
            return toReturn;
        }
        return super.sourceOptions(type, fieldName);
    }
}
