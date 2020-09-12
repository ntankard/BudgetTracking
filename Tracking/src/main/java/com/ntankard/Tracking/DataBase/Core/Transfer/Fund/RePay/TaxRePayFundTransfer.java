package com.ntankard.Tracking.DataBase.Core.Transfer.Fund.RePay;

import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.DataCore.HalfTransferSetSum_DataCore;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.DataCore.TwoParentSet_DataCore;
import com.ntankard.javaObjectDatabase.CoreObject.Field.DataField;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.Category;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.SolidCategory;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.TaxFundEvent;
import com.ntankard.Tracking.DataBase.Core.Transfer.HalfTransfer;
import com.ntankard.javaObjectDatabase.Database.ParameterMap;
import com.ntankard.javaObjectDatabase.Database.TrackingDatabase;
import com.ntankard.javaObjectDatabase.CoreObject.Field.DataCore.Derived_DataCore;
import com.ntankard.javaObjectDatabase.CoreObject.Field.DataCore.Static_DataCore;
import com.ntankard.javaObjectDatabase.CoreObject.FieldContainer;

import java.util.List;

import static com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.TaxFundEvent.TaxFundEvent_Percentage;

@ParameterMap(shouldSave = false)
public class TaxRePayFundTransfer extends RePayFundTransfer {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String TaxRePayFundTransfer_TaxableCategory = "getTaxableCategory";
    public static final String TaxRePayFundTransfer_TaxableSet = "getTaxableSet";
    public static final String TaxRePayFundTransfer_TaxableAmount = "getTaxableAmount";

    /**
     * Get all the fields for this object
     */
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = RePayFundTransfer.getFieldContainer();

        // ID
        // Description
        // Period
        // Source
        // TaxableCategory =============================================================================================
        fieldContainer.add(new DataField<>(TaxRePayFundTransfer_TaxableCategory, Category.class));
        fieldContainer.<SolidCategory>get(TaxRePayFundTransfer_TaxableCategory).setDataCore(new Static_DataCore<SolidCategory>(null) {
            @Override
            public SolidCategory get() {
                return TrackingDatabase.get().getSpecialValue(SolidCategory.class, SolidCategory.TAXABLE);
            }
        });
        // TaxableSet ==================================================================================================
        fieldContainer.add(new DataField<>(TaxRePayFundTransfer_TaxableSet, List.class));
        fieldContainer.<List<HalfTransfer>>get(TaxRePayFundTransfer_TaxableSet).setDataCore(
                new TwoParentSet_DataCore<>(
                        HalfTransfer.class,
                        fieldContainer.get(TaxRePayFundTransfer_TaxableCategory),
                        fieldContainer.get(Transfer_Period)));
        // TaxableAmount ===============================================================================================
        fieldContainer.add(new DataField<>(TaxRePayFundTransfer_TaxableAmount, Double.class));
        fieldContainer.<Double>get(TaxRePayFundTransfer_TaxableAmount).setDataCore(
                new HalfTransferSetSum_DataCore(
                        fieldContainer.get(TaxRePayFundTransfer_TaxableSet)));
        // Value =======================================================================================================
        fieldContainer.<Double>get(Transfer_Value).setDataCore(
                new Derived_DataCore<>(
                        (Derived_DataCore.Converter<Double, TaxRePayFundTransfer>) container -> {
                            TaxFundEvent taxFundEvent = (TaxFundEvent) container.getSource();
                            return -Currency.round(container.getTaxableAmount() * taxFundEvent.getPercentage());
                        }
                        , new Derived_DataCore.LocalSource<>(fieldContainer.get(TaxRePayFundTransfer_TaxableAmount))
                        , new Derived_DataCore.ExternalSource<>(fieldContainer.get(Transfer_Source), TaxFundEvent_Percentage)));
        //==============================================================================================================
        // Currency
        // Destination
        // SourceCurrencyGet
        // DestinationCurrencyGet
        // SourcePeriodGet
        // DestinationPeriodGet
        // Parents
        // Children

        return fieldContainer.finaliseContainer(TaxRePayFundTransfer.class);
    }

    /**
     * Create a new RePayFundTransfer object
     */
    public static TaxRePayFundTransfer make(Integer id, Period period, FundEvent source, Currency currency) {
        return assembleDataObject(TaxRePayFundTransfer.getFieldContainer(), new TaxRePayFundTransfer()
                , DataObject_Id, id
                , Transfer_Period, period
                , Transfer_Source, source
                , Transfer_Currency, currency
        );
    }

    public Double getTaxableAmount() {
        return this.<Double>get(TaxRePayFundTransfer_TaxableAmount);
    }
}
