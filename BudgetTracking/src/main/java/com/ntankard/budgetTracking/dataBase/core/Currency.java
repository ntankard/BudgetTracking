package com.ntankard.budgetTracking.dataBase.core;

import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.Derived_DataCore_Schema;
import com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.end.End_Source_Schema;
import com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.dataObject.interfaces.HasDefault;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.budgetTracking.dataBase.core.baseObject.NamedDataObject;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;

import java.text.NumberFormat;
import java.util.Locale;

import static com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties.INFO_DISPLAY;
import static com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties.TRACE_DISPLAY;
import static com.ntankard.javaObjectDatabase.dataField.dataCore.derived.source.Source_Factory.makeSourceChain;

public class Currency extends NamedDataObject implements HasDefault {

    /**
     * Round a number to a sensible value for a currency value
     *
     * @param value The value to round
     * @return The rounded value
     */
    public static Double round(Double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String Currency_Default = "isDefault";
    public static final String Currency_ToPrimary = "getToPrimary";
    public static final String Currency_Language = "getLanguage";
    public static final String Currency_Country = "getCountry";
    public static final String Currency_NumberFormat = "getNumberFormat";

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = NamedDataObject.getDataObjectSchema();

        // ID
        // Name
        // Default =====================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Currency_Default, Boolean.class));
        // ToPrimary ===================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Currency_ToPrimary, Double.class));
        // Language ====================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Currency_Language, String.class));
        dataObjectSchema.get(Currency_Language).getProperty(Display_Properties.class).setVerbosityLevel(INFO_DISPLAY);
        // Country =====================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Currency_Country, String.class));
        dataObjectSchema.get(Currency_Country).getProperty(Display_Properties.class).setVerbosityLevel(INFO_DISPLAY);
        // NumberFormat ================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Currency_NumberFormat, NumberFormat.class));
        dataObjectSchema.get(Currency_NumberFormat).getProperty(Display_Properties.class).setVerbosityLevel(TRACE_DISPLAY);
        dataObjectSchema.<NumberFormat>get(Currency_NumberFormat).setDataCore_schema(
                new Derived_DataCore_Schema<NumberFormat, Currency>
                        (dataObject -> NumberFormat.getCurrencyInstance(new Locale(dataObject.getLanguage(), dataObject.getCountry()))
                                , makeSourceChain(Currency_Country)
                                , makeSourceChain(Currency_Language)));
        //==============================================================================================================
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(Currency.class);
    }

    /**
     * Constructor
     */
    public Currency(Database database) {
        super(database);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @Override
    public Boolean isDefault() {
        return get(Currency_Default);
    }

    public Double getToPrimary() {
        return get(Currency_ToPrimary);
    }

    public String getLanguage() {
        return get(Currency_Language);
    }

    public String getCountry() {
        return get(Currency_Country);
    }

    public NumberFormat getNumberFormat() {
        return get(Currency_NumberFormat);
    }
}
