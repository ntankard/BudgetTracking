package com.ntankard.tracking.dataBase.core;

import com.ntankard.javaObjectDatabase.dataObject.factory.DoubleParentFactory;
import com.ntankard.javaObjectDatabase.dataObject.factory.ObjectFactory;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.DirectExternal_Source;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.External_Source;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.database.Database_Schema;
import com.ntankard.tracking.dataBase.core.baseObject.interfaces.CurrencyBound;
import com.ntankard.javaObjectDatabase.dataObject.interfaces.Ordered;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.tracking.dataBase.core.period.ExistingPeriod;
import com.ntankard.tracking.dataBase.core.pool.Bank;
import com.ntankard.javaObjectDatabase.database.Database;

import java.util.List;

import static com.ntankard.tracking.dataBase.core.period.ExistingPeriod.ExistingPeriod_Order;
import static com.ntankard.tracking.dataBase.core.pool.Bank.Bank_Order;
import static com.ntankard.javaObjectDatabase.dataField.properties.Display_Properties.DataType.CURRENCY;
import static com.ntankard.javaObjectDatabase.dataField.properties.Display_Properties.INFO_DISPLAY;
import static com.ntankard.javaObjectDatabase.dataField.properties.Display_Properties.TRACE_DISPLAY;
import static com.ntankard.tracking.dataBase.core.pool.Bank.Bank_Currency;

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
            StatementEnd_Period, (generator, secondaryGenerator) -> StatementEnd.make(generator.getTrackingDatabase().getNextId(), secondaryGenerator, generator, 0.0),
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
                        new DirectExternal_Source.DirectExternalSource_Factory<>((StatementEnd_Bank), Bank_Currency)));
        // Order =======================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(StatementEnd_Order, Integer.class));
        dataObjectSchema.get(StatementEnd_Order).getDisplayProperties().setVerbosityLevel(TRACE_DISPLAY);
        dataObjectSchema.<Integer>get(StatementEnd_Order).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<Integer, StatementEnd>
                        (dataObject -> dataObject.getBank().getOrder() + dataObject.getPeriod().getOrder() * 1000
                                , new External_Source.ExternalSource_Factory<>((StatementEnd_Bank), Bank_Order)
                                , new External_Source.ExternalSource_Factory<>((StatementEnd_Period), ExistingPeriod_Order)));
        //==============================================================================================================
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(StatementEnd.class);
    }

    /**
     * Create a new StatementEnd object
     */
    public static StatementEnd make(Integer id, ExistingPeriod period, Bank bank, Double end) {
        Database database = period.getTrackingDatabase();
        Database_Schema database_schema = database.getSchema();
        return assembleDataObject(database, database_schema.getClassSchema(StatementEnd.class), new StatementEnd()
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
