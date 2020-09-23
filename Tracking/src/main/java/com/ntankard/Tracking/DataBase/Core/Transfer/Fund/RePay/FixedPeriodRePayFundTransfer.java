package com.ntankard.Tracking.DataBase.Core.Transfer.Fund.RePay;

import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.FixedPeriodFundEvent;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.FundEvent;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.source.ExternalSource;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.source.LocalSource;
import com.ntankard.javaObjectDatabase.Database.ParameterMap;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.Derived_DataCore;
import com.ntankard.javaObjectDatabase.CoreObject.FieldContainer;

import static com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.FixedPeriodFundEvent.*;

@ParameterMap(shouldSave = false)
public class FixedPeriodRePayFundTransfer extends RePayFundTransfer {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = RePayFundTransfer.getFieldContainer();

        // ID
        // Description
        // Period
        // Source
        // Value =======================================================================================================
        fieldContainer.<Double>get(Transfer_Value).setDataCore(
                new Derived_DataCore<>(
                        (Derived_DataCore.Calculator<Double, FixedPeriodRePayFundTransfer>) container -> {
                            FixedPeriodFundEvent fixedPeriodFundEvent = (FixedPeriodFundEvent) container.getSource();
                            if (!container.getPeriod().isWithin(fixedPeriodFundEvent.getStart(), fixedPeriodFundEvent.getDuration())) {
                                return -0.0;
                            }
                            return fixedPeriodFundEvent.getRepayAmount();
                        }
                        , new LocalSource<>(fieldContainer.get(Transfer_Period))
                        , new ExternalSource<>(fieldContainer.get(Transfer_Source), FixedPeriodFundEvent_Start)
                        , new ExternalSource<>(fieldContainer.get(Transfer_Source), FixedPeriodFundEvent_Duration)
                        , new ExternalSource<>(fieldContainer.get(Transfer_Source), FixedPeriodFundEvent_RepayAmount)));
        // =============================================================================================================
        // Currency
        // Destination
        // SourceCurrencyGet
        // DestinationCurrencyGet
        // SourcePeriodGet
        // DestinationPeriodGet
        // Parents
        // Children

        return fieldContainer.finaliseContainer(FixedPeriodRePayFundTransfer.class);
    }

    /**
     * Create a new RePayFundTransfer object
     */
    public static FixedPeriodRePayFundTransfer make(Integer id, Period period, FundEvent source, Currency currency) {
        return assembleDataObject(FixedPeriodRePayFundTransfer.getFieldContainer(), new FixedPeriodRePayFundTransfer()
                , DataObject_Id, id
                , Transfer_Period, period
                , Transfer_Source, source
                , Transfer_Currency, currency
        );
    }
}
