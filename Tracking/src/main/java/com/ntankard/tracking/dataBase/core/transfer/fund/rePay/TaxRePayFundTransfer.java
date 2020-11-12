package com.ntankard.tracking.dataBase.core.transfer.fund.rePay;

import com.ntankard.tracking.dataBase.core.Currency;
import com.ntankard.tracking.dataBase.core.period.ExistingPeriod;
import com.ntankard.tracking.dataBase.core.period.Period;
import com.ntankard.tracking.dataBase.core.pool.category.Category;
import com.ntankard.tracking.dataBase.core.pool.category.SolidCategory;
import com.ntankard.tracking.dataBase.core.pool.fundEvent.FundEvent;
import com.ntankard.tracking.dataBase.core.pool.fundEvent.TaxFundEvent;
import com.ntankard.tracking.dataBase.core.transfer.HalfTransfer;
import com.ntankard.javaObjectDatabase.coreObject.factory.DoubleParentFactory;
import com.ntankard.javaObjectDatabase.coreObject.field.DataField_Schema;
import com.ntankard.javaObjectDatabase.coreObject.field.ListDataField_Schema;
import com.ntankard.javaObjectDatabase.coreObject.field.dataCore.Children_ListDataCore;
import com.ntankard.javaObjectDatabase.coreObject.field.dataCore.derived.Derived_DataCore;
import com.ntankard.javaObjectDatabase.coreObject.field.dataCore.Static_DataCore;
import com.ntankard.javaObjectDatabase.coreObject.field.dataCore.derived.source.ExternalSource;
import com.ntankard.javaObjectDatabase.coreObject.field.dataCore.derived.source.ListSource;
import com.ntankard.javaObjectDatabase.coreObject.field.dataCore.derived.source.LocalSource;
import com.ntankard.javaObjectDatabase.coreObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.ParameterMap;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase;

import java.util.List;

import static com.ntankard.tracking.dataBase.core.pool.fundEvent.TaxFundEvent.TaxFundEvent_Percentage;
import static com.ntankard.tracking.dataBase.core.transfer.HalfTransfer.HalfTransfer_Currency;
import static com.ntankard.tracking.dataBase.core.transfer.HalfTransfer.HalfTransfer_Value;

@ParameterMap(shouldSave = false)
public class TaxRePayFundTransfer extends RePayFundTransfer {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String TaxRePayFundTransfer_TaxableCategory = "getTaxableCategory";
    public static final String TaxRePayFundTransfer_TaxableSet = "getTaxableSet";
    public static final String TaxRePayFundTransfer_TaxableAmount = "getTaxableAmount";

    public static DoubleParentFactory<?, ?, ?> Factory = new DoubleParentFactory<>(
            TaxRePayFundTransfer.class,
            ExistingPeriod.class,
            Transfer_Period, TaxFundEvent.class,
            Transfer_Source, (generator, secondaryGenerator) -> TaxRePayFundTransfer.make(
            TrackingDatabase.get().getNextId(),
            generator,
            secondaryGenerator,
            TrackingDatabase.get().getDefault(Currency.class))
    );

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getFieldContainer() {
        DataObject_Schema dataObjectSchema = RePayFundTransfer.getFieldContainer();

        // Class behavior
        dataObjectSchema.setMyFactory(Factory);

        // ID
        // Description
        // Period
        // Source
        // TaxableCategory =============================================================================================
        dataObjectSchema.add(new DataField_Schema<>(TaxRePayFundTransfer_TaxableCategory, Category.class));
        dataObjectSchema.<SolidCategory>get(TaxRePayFundTransfer_TaxableCategory).setDataCore_factory(
                new Static_DataCore.Static_DataCore_Factory<>(dataField ->
                        TrackingDatabase.get().getSpecialValue(SolidCategory.class, SolidCategory.TAXABLE)));
        // TaxableSet ==================================================================================================
        dataObjectSchema.add(new ListDataField_Schema<>(TaxRePayFundTransfer_TaxableSet, HalfTransfer.HalfTransferList.class));
        dataObjectSchema.<List<HalfTransfer>>get(TaxRePayFundTransfer_TaxableSet).setDataCore_factory(
                new Children_ListDataCore.Children_ListDataCore_Factory<>(
                        HalfTransfer.class,
                        new Children_ListDataCore.ParentAccess.ParentAccess_Factory<>(TaxRePayFundTransfer_TaxableCategory),
                        new Children_ListDataCore.ParentAccess.ParentAccess_Factory<>(Transfer_Period)));
        // TaxableAmount ===============================================================================================
        dataObjectSchema.add(new DataField_Schema<>(TaxRePayFundTransfer_TaxableAmount, Double.class));
        dataObjectSchema.<Double>get(TaxRePayFundTransfer_TaxableAmount).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<Double, TaxRePayFundTransfer>(
                        container -> {
                            double sum = 0.0;
                            for (HalfTransfer halfTransfer : container.<List<HalfTransfer>>get(TaxRePayFundTransfer_TaxableSet)) {
                                sum += halfTransfer.getValue() * halfTransfer.getCurrency().getToPrimary();
                            }
                            return -Currency.round(sum);
                        }
                        , new ListSource.ListSource_Factory<>(
                        TaxRePayFundTransfer_TaxableSet,
                        HalfTransfer_Value,
                        HalfTransfer_Currency
                )));
        // Value =======================================================================================================
        dataObjectSchema.<Double>get(Transfer_Value).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>(
                        (Derived_DataCore.Calculator<Double, TaxRePayFundTransfer>) container -> {
                            TaxFundEvent taxFundEvent = (TaxFundEvent) container.getSource();
                            return -Currency.round(container.getTaxableAmount() * taxFundEvent.getPercentage());
                        }
                        , new LocalSource.LocalSource_Factory<>(TaxRePayFundTransfer_TaxableAmount)
                        , new ExternalSource.ExternalSource_Factory<>(Transfer_Source, TaxFundEvent_Percentage)));
        //==============================================================================================================
        // Currency
        // Destination
        // SourceCurrencyGet
        // DestinationCurrencyGet
        // SourcePeriodGet
        // DestinationPeriodGet
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(TaxRePayFundTransfer.class);
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
