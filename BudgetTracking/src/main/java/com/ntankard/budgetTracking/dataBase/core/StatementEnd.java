package com.ntankard.budgetTracking.dataBase.core;

import com.ntankard.budgetTracking.dataBase.core.baseObject.interfaces.CurrencyBound;
import com.ntankard.budgetTracking.dataBase.core.period.ExistingPeriod;
import com.ntankard.budgetTracking.dataBase.core.pool.Bank;
import com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties;
import com.ntankard.dynamicGUI.javaObjectDatabase.Displayable_DataObject;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.DataCore_Factory;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.dataObject.factory.DoubleParentFactory;
import com.ntankard.javaObjectDatabase.dataObject.factory.ObjectFactory;
import com.ntankard.javaObjectDatabase.dataObject.interfaces.Ordered;
import com.ntankard.javaObjectDatabase.database.Database;

import java.util.List;

import static com.ntankard.budgetTracking.dataBase.core.period.ExistingPeriod.ExistingPeriod_Order;
import static com.ntankard.budgetTracking.dataBase.core.pool.Bank.Bank_Currency;
import static com.ntankard.budgetTracking.dataBase.core.pool.Bank.Bank_Order;
import static com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties.DataType.CURRENCY;
import static com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties.INFO_DISPLAY;
import static com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties.TRACE_DISPLAY;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Source_Factory.makeSourceChain;

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
            StatementEnd_Period, (generator, secondaryGenerator) -> new StatementEnd(secondaryGenerator, generator, 0.0),
            ObjectFactory.GeneratorMode.SINGLE);

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = Displayable_DataObject.getDataObjectSchema();

        // Class behavior
        dataObjectSchema.setMyFactory(Factory);

        // ID
        // Period ======================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(StatementEnd_Period, ExistingPeriod.class));
        dataObjectSchema.get(StatementEnd_Period).getProperty(Display_Properties.class).setVerbosityLevel(INFO_DISPLAY);
        // Bank ============================================================0============================================
        dataObjectSchema.add(new DataField_Schema<>(StatementEnd_Bank, Bank.class));
        // End =========================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(StatementEnd_End, Double.class));
        dataObjectSchema.get(StatementEnd_End).getProperty(Display_Properties.class).setDataType(CURRENCY);
        dataObjectSchema.get(StatementEnd_End).setManualCanEdit(true);
        // Currency ====================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(StatementEnd_Currency, Currency.class));
        dataObjectSchema.<Currency>get(StatementEnd_Currency).setDataCore_schema(DataCore_Factory.createDirectDerivedDataCore(StatementEnd_Bank, Bank_Currency));
        // Order =======================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(StatementEnd_Order, Integer.class));
        dataObjectSchema.get(StatementEnd_Order).getProperty(Display_Properties.class).setVerbosityLevel(TRACE_DISPLAY);
        dataObjectSchema.<Integer>get(StatementEnd_Order).setDataCore_schema(
                new Derived_DataCore_Schema<Integer, StatementEnd>
                        (dataObject -> dataObject.getBank().getOrder() + dataObject.getPeriod().getOrder() * 1000
                                , makeSourceChain(StatementEnd_Bank, Bank_Order)
                                , makeSourceChain(StatementEnd_Period, ExistingPeriod_Order)));
        //==============================================================================================================
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(StatementEnd.class);
    }

    /**
     * Constructor
     */
    public StatementEnd(Database database, Object... args) {
        super(database, args);
    }

    /**
     * Constructor
     */
    public StatementEnd(ExistingPeriod period, Bank bank, Double end) {
        super(period.getTrackingDatabase()
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
