package com.ntankard.budgetTracking.dataBase.core.transfer.fund.rePay;

import com.ntankard.budgetTracking.dataBase.core.Currency;
import com.ntankard.budgetTracking.dataBase.core.period.ExistingPeriod;
import com.ntankard.budgetTracking.dataBase.core.period.Period;
import com.ntankard.budgetTracking.dataBase.core.pool.category.Category;
import com.ntankard.budgetTracking.dataBase.core.pool.category.SolidCategory;
import com.ntankard.budgetTracking.dataBase.core.pool.fundEvent.FundEvent;
import com.ntankard.budgetTracking.dataBase.core.pool.fundEvent.TaxFundEvent;
import com.ntankard.budgetTracking.dataBase.core.transfer.HalfTransfer;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.ListDataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema.Calculator;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.dataObject.factory.DoubleParentFactory;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.javaObjectDatabase.database.ParameterMap;

import java.util.List;

import static com.ntankard.budgetTracking.dataBase.core.pool.category.SolidCategory.SolidCategory_Taxable;
import static com.ntankard.budgetTracking.dataBase.core.pool.fundEvent.TaxFundEvent.TaxFundEvent_Percentage;
import static com.ntankard.budgetTracking.dataBase.core.transfer.HalfTransfer.HalfTransfer_Currency;
import static com.ntankard.budgetTracking.dataBase.core.transfer.HalfTransfer.HalfTransfer_Value;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.DataCore_Factory.createMultiParentList;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.DataCore_Factory.createStaticObjectDataCore;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Source_Factory.makeSharedStepSourceChain;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Source_Factory.makeSourceChain;

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
            Transfer_Source, (generator, secondaryGenerator) -> new TaxRePayFundTransfer(
            generator,
            secondaryGenerator,
            generator.getTrackingDatabase().getDefault(Currency.class))
    );

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = RePayFundTransfer.getDataObjectSchema();

        // Class behavior
        dataObjectSchema.setMyFactory(Factory);

        // ID
        // Description
        // Period
        // Source
        // TaxableCategory =============================================================================================
        dataObjectSchema.add(new DataField_Schema<>(TaxRePayFundTransfer_TaxableCategory, Category.class));
        dataObjectSchema.<SolidCategory>get(TaxRePayFundTransfer_TaxableCategory).setDataCore_schema(
                createStaticObjectDataCore(SolidCategory.class, SolidCategory_Taxable));
        // TaxableSet ==================================================================================================
        dataObjectSchema.add(new ListDataField_Schema<>(TaxRePayFundTransfer_TaxableSet, HalfTransfer.HalfTransferList.class));
        dataObjectSchema.<List<HalfTransfer>>get(TaxRePayFundTransfer_TaxableSet).setDataCore_schema(
                createMultiParentList(
                        HalfTransfer.class,
                        TaxRePayFundTransfer_TaxableCategory,
                        Transfer_Period));
        // TaxableAmount ===============================================================================================
        dataObjectSchema.add(new DataField_Schema<>(TaxRePayFundTransfer_TaxableAmount, Double.class));
        dataObjectSchema.<Double>get(TaxRePayFundTransfer_TaxableAmount).setDataCore_schema(
                new Derived_DataCore_Schema<Double, TaxRePayFundTransfer>(
                        container -> {
                            double sum = 0.0;
                            for (HalfTransfer halfTransfer : container.<List<HalfTransfer>>get(TaxRePayFundTransfer_TaxableSet)) {
                                sum += halfTransfer.getValue() * halfTransfer.getCurrency().getToPrimary();
                            }
                            return -Currency.round(sum);
                        }
                        , makeSharedStepSourceChain(
                        TaxRePayFundTransfer_TaxableSet,
                        HalfTransfer_Value,
                        HalfTransfer_Currency
                )));
        // Value =======================================================================================================
        dataObjectSchema.<Double>get(Transfer_Value).setDataCore_schema(
                new Derived_DataCore_Schema<>(
                        (Calculator<Double, TaxRePayFundTransfer>) container -> {
                            TaxFundEvent taxFundEvent = (TaxFundEvent) container.getSource();
                            if (!container.getPeriod().isWithin(taxFundEvent.getStart(), taxFundEvent.getDuration())) {
                                return -0.0;
                            }
                            return Currency.round(container.getTaxableAmount() * taxFundEvent.getPercentage());
                        }
                        , makeSourceChain(TaxRePayFundTransfer_TaxableAmount)
                        , makeSourceChain(Transfer_Source, TaxFundEvent_Percentage)));
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
     * Constructor
     */
    public TaxRePayFundTransfer(Database database, Object... args) {
        super(database, args);
    }

    /**
     * Constructor
     */
    public TaxRePayFundTransfer(Period period, FundEvent source, Currency currency) {
        super(period.getTrackingDatabase()
                , Transfer_Period, period
                , Transfer_Source, source
                , Transfer_Currency, currency
        );
    }

    public Double getTaxableAmount() {
        return this.<Double>get(TaxRePayFundTransfer_TaxableAmount);
    }
}
