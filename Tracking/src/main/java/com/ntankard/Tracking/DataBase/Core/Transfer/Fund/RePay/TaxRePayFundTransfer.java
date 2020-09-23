package com.ntankard.Tracking.DataBase.Core.Transfer.Fund.RePay;

import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.Category;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.SolidCategory;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.TaxFundEvent;
import com.ntankard.Tracking.DataBase.Core.Transfer.HalfTransfer;
import com.ntankard.javaObjectDatabase.CoreObject.Field.DataField;
import com.ntankard.javaObjectDatabase.CoreObject.Field.ListDataField;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.Children_ListDataCore;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.Derived_DataCore;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.Static_DataCore;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.source.ExternalSource;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.source.ListSource;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.source.LocalSource;
import com.ntankard.javaObjectDatabase.CoreObject.FieldContainer;
import com.ntankard.javaObjectDatabase.Database.ParameterMap;
import com.ntankard.javaObjectDatabase.Database.TrackingDatabase;

import java.util.List;

import static com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.TaxFundEvent.TaxFundEvent_Percentage;
import static com.ntankard.Tracking.DataBase.Core.Transfer.HalfTransfer.HalfTransfer_Currency;
import static com.ntankard.Tracking.DataBase.Core.Transfer.HalfTransfer.HalfTransfer_Value;

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
        fieldContainer.<SolidCategory>get(TaxRePayFundTransfer_TaxableCategory).setDataCore(
                new Static_DataCore<>(dataField ->
                        TrackingDatabase.get().getSpecialValue(SolidCategory.class, SolidCategory.TAXABLE)));
        // TaxableSet ==================================================================================================
        fieldContainer.add(new ListDataField<>(TaxRePayFundTransfer_TaxableSet, HalfTransfer.HalfTransferList.class));
        fieldContainer.<List<HalfTransfer>>get(TaxRePayFundTransfer_TaxableSet).setDataCore(
                new Children_ListDataCore<>(
                        HalfTransfer.class,
                        new Children_ListDataCore.ParentAccess<>(fieldContainer.get(TaxRePayFundTransfer_TaxableCategory)),
                        new Children_ListDataCore.ParentAccess<>(fieldContainer.get(Transfer_Period))));
        // TaxableAmount ===============================================================================================
        fieldContainer.add(new DataField<>(TaxRePayFundTransfer_TaxableAmount, Double.class));
        fieldContainer.<Double>get(TaxRePayFundTransfer_TaxableAmount).setDataCore(
                new Derived_DataCore<Double, TaxRePayFundTransfer>(
                        container -> {
                            double sum = 0.0;
                            for (HalfTransfer halfTransfer : container.<List<HalfTransfer>>get(TaxRePayFundTransfer_TaxableSet)) {
                                sum += halfTransfer.getValue() * halfTransfer.getCurrency().getToPrimary();
                            }
                            return -Currency.round(sum);
                        }
                        , new ListSource<>(
                        (ListDataField<HalfTransfer>) fieldContainer.<List<HalfTransfer>>get(TaxRePayFundTransfer_TaxableSet),
                        HalfTransfer_Value,
                        HalfTransfer_Currency
                )));
        // Value =======================================================================================================
        fieldContainer.<Double>get(Transfer_Value).setDataCore(
                new Derived_DataCore<>(
                        (Derived_DataCore.Calculator<Double, TaxRePayFundTransfer>) container -> {
                            TaxFundEvent taxFundEvent = (TaxFundEvent) container.getSource();
                            return -Currency.round(container.getTaxableAmount() * taxFundEvent.getPercentage());
                        }
                        , new LocalSource<>(fieldContainer.get(TaxRePayFundTransfer_TaxableAmount))
                        , new ExternalSource<>(fieldContainer.get(Transfer_Source), TaxFundEvent_Percentage)));
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
