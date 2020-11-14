package com.ntankard.tracking.dataBase.core.transfer.fund.rePay;

import com.ntankard.javaObjectDatabase.database.Database_Schema;
import com.ntankard.tracking.dataBase.core.Currency;
import com.ntankard.tracking.dataBase.core.period.ExistingPeriod;
import com.ntankard.tracking.dataBase.core.period.Period;
import com.ntankard.tracking.dataBase.core.pool.category.Category;
import com.ntankard.tracking.dataBase.core.pool.category.SolidCategory;
import com.ntankard.tracking.dataBase.core.pool.fundEvent.FundEvent;
import com.ntankard.tracking.dataBase.core.pool.fundEvent.TaxFundEvent;
import com.ntankard.tracking.dataBase.core.transfer.HalfTransfer;
import com.ntankard.javaObjectDatabase.dataObject.factory.DoubleParentFactory;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.ListDataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.Children_ListDataCore;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore;
import com.ntankard.javaObjectDatabase.dataField.dataCore.Static_DataCore;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.External_Source;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.List_Source;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Local_Source;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.ParameterMap;
import com.ntankard.javaObjectDatabase.database.Database;

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
            Transfer_Source, (generator, secondaryGenerator) -> TaxRePayFundTransfer.make(generator.getTrackingDatabase().getNextId(),
            generator,
            secondaryGenerator,
            generator.getTrackingDatabase().getDefault(Currency.class))
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
                        dataField.getContainer().getTrackingDatabase().getSpecialValue(SolidCategory.class, SolidCategory.TAXABLE)));
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
                        , new List_Source.ListSource_Factory<>(
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
                        , new Local_Source.LocalSource_Factory<>(TaxRePayFundTransfer_TaxableAmount)
                        , new External_Source.ExternalSource_Factory<>(Transfer_Source, TaxFundEvent_Percentage)));
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
        Database database = period.getTrackingDatabase();
        Database_Schema database_schema = database.getSchema();
        return assembleDataObject(database, database_schema.getClassSchema(TaxRePayFundTransfer.class), new TaxRePayFundTransfer()
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
