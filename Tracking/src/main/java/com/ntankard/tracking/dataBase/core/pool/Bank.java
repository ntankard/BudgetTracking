package com.ntankard.tracking.dataBase.core.pool;

import com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.tracking.dataBase.core.baseObject.interfaces.CurrencyBound;
import com.ntankard.tracking.dataBase.core.Currency;
import com.ntankard.tracking.dataBase.core.StatementEnd;
import com.ntankard.tracking.dataBase.interfaces.summary.pool.Bank_Summary;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.dataObject.interfaces.HasDefault;
import com.ntankard.javaObjectDatabase.dataObject.interfaces.Ordered;

import static com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties.INFO_DISPLAY;

public class Bank extends Pool implements CurrencyBound, Ordered, HasDefault {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String Bank_Currency = "getCurrency";
    public static final String Bank_Start = "getStart";
    public static final String Bank_Default = "isDefault";
    public static final String Bank_Order = "getOrder";

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = Pool.getDataObjectSchema();

        // Class behavior
        dataObjectSchema.addObjectFactory(StatementEnd.Factory);
        dataObjectSchema.addObjectFactory(Bank_Summary.Factory);

        // ID
        // Name
        // Currency ========================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Bank_Currency, Currency.class));
        // Start ========================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Bank_Start, Double.class));
        dataObjectSchema.get(Bank_Start).getProperty(Display_Properties.class).setVerbosityLevel(INFO_DISPLAY);
        // Default ========================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Bank_Default, Boolean.class));
        dataObjectSchema.get(Bank_Default).getProperty(Display_Properties.class).setVerbosityLevel(INFO_DISPLAY);
        // Order ========================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Bank_Order, Integer.class));
        dataObjectSchema.get(Bank_Order).getProperty(Display_Properties.class).setVerbosityLevel(INFO_DISPLAY);
        //==============================================================================================================
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(Bank.class);
    }

    /**
     * Constructor
     */
    public Bank(Database database) {
        super(database);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @Override
    public Currency getCurrency() {
        return get(Bank_Currency);
    }

    public Double getStart() {
        return get(Bank_Start);
    }

    @Override
    public Boolean isDefault() {
        return get(Bank_Default);
    }

    @Override
    public Integer getOrder() {
        return get(Bank_Order);
    }
}
