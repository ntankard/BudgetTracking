package com.ntankard.Tracking.DataBase.Core;

import com.ntankard.javaObjectDatabase.CoreObject.Factory.DoubleParentFactory;
import com.ntankard.javaObjectDatabase.CoreObject.Factory.ObjectFactory;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.Derived_DataCore;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.source.DirectExternalSource;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.source.ExternalSource;
import com.ntankard.javaObjectDatabase.CoreObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.CoreObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.CurrencyBound;
import com.ntankard.javaObjectDatabase.CoreObject.Interface.Ordered;
import com.ntankard.javaObjectDatabase.CoreObject.Field.DataField_Schema;
import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank;
import com.ntankard.javaObjectDatabase.Database.TrackingDatabase;

import java.util.List;

import static com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod.ExistingPeriod_Order;
import static com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank_Order;
import static com.ntankard.javaObjectDatabase.CoreObject.Field.Properties.Display_Properties.DataType.CURRENCY;
import static com.ntankard.javaObjectDatabase.CoreObject.Field.Properties.Display_Properties.INFO_DISPLAY;
import static com.ntankard.javaObjectDatabase.CoreObject.Field.Properties.Display_Properties.TRACE_DISPLAY;
import static com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank_Currency;

public class StatementEnd extends DataObject implements CurrencyBound, Ordered {

    public interface StatementEndList extends List<StatementEnd> {
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String StatementEnd_Period = "getPeriod";
    public static final String StatementEnd_Bank = "getBank";
    public static final String StatementEnd_End = "getEnd";
    public static final String StatementEnd_Currency = "getCurrency";
    public static final String StatementEnd_Order = "getOrder";

    public static DoubleParentFactory<?, ?, ?> Factory = new DoubleParentFactory<>(
            StatementEnd.class,
            Bank.class,
            StatementEnd_Bank, ExistingPeriod.class,
            StatementEnd_Period, (generator, secondaryGenerator) -> StatementEnd.make(TrackingDatabase.get().getNextId(), secondaryGenerator, generator, 0.0),
            ObjectFactory.GeneratorMode.SINGLE);

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getFieldContainer() {
        DataObject_Schema dataObjectSchema = DataObject.getFieldContainer();

        // Class behavior
        dataObjectSchema.setMyFactory(Factory);

        // ID
        // Period ======================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(StatementEnd_Period, ExistingPeriod.class));
        dataObjectSchema.get(StatementEnd_Period).getDisplayProperties().setVerbosityLevel(INFO_DISPLAY);
        // Bank ============================================================0============================================
        dataObjectSchema.add(new DataField_Schema<>(StatementEnd_Bank, Bank.class));
        // End =========================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(StatementEnd_End, Double.class));
        dataObjectSchema.get(StatementEnd_End).getDisplayProperties().setDataType(CURRENCY);
        dataObjectSchema.get(StatementEnd_End).setManualCanEdit(true);
        // Currency ====================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(StatementEnd_Currency, Currency.class));
        dataObjectSchema.get(StatementEnd_Currency).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<>(
                        new DirectExternalSource.DirectExternalSource_Factory<>((StatementEnd_Bank), Bank_Currency)));
        // Order =======================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(StatementEnd_Order, Integer.class));
        dataObjectSchema.get(StatementEnd_Order).getDisplayProperties().setVerbosityLevel(TRACE_DISPLAY);
        dataObjectSchema.<Integer>get(StatementEnd_Order).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<Integer, StatementEnd>
                        (dataObject -> dataObject.getBank().getOrder() + dataObject.getPeriod().getOrder() * 1000
                                , new ExternalSource.ExternalSource_Factory<>((StatementEnd_Bank), Bank_Order)
                                , new ExternalSource.ExternalSource_Factory<>((StatementEnd_Period), ExistingPeriod_Order)));
        //==============================================================================================================
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(StatementEnd.class);
    }

    /**
     * Create a new StatementEnd object
     */
    public static StatementEnd make(Integer id, ExistingPeriod period, Bank bank, Double end) {
        return assembleDataObject(StatementEnd.getFieldContainer(), new StatementEnd()
                , DataObject_Id, id
                , StatementEnd_Period, period
                , StatementEnd_Bank, bank
                , StatementEnd_End, end
        );
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public ExistingPeriod getPeriod() {
        return get(StatementEnd_Period);
    }

    public Bank getBank() {
        return get(StatementEnd_Bank);
    }

    public Double getEnd() {
        return get(StatementEnd_End);
    }

    @Override
    public Currency getCurrency() {
        return get(StatementEnd_Currency);
    }

    @Override
    public Integer getOrder() {
        return get(StatementEnd_Order);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public void setEnd(Double value) {
        set(StatementEnd_End, value);
    }
}
