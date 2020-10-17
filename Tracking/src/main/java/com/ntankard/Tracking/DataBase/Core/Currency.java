package com.ntankard.Tracking.DataBase.Core;

import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.Derived_DataCore;
import com.ntankard.javaObjectDatabase.CoreObject.Field.dataCore.derived.source.LocalSource;
import com.ntankard.javaObjectDatabase.CoreObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.CoreObject.Interface.HasDefault;
import com.ntankard.Tracking.DataBase.Core.BaseObject.NamedDataObject;
import com.ntankard.javaObjectDatabase.CoreObject.Field.DataField_Schema;

import java.text.NumberFormat;
import java.util.Locale;

import static com.ntankard.javaObjectDatabase.CoreObject.Field.Properties.Display_Properties.INFO_DISPLAY;
import static com.ntankard.javaObjectDatabase.CoreObject.Field.Properties.Display_Properties.TRACE_DISPLAY;

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
    public static DataObject_Schema getFieldContainer() {
        DataObject_Schema dataObjectSchema = NamedDataObject.getFieldContainer();

        // ID
        // Name
        // Default =====================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Currency_Default, Boolean.class));
        // ToPrimary ===================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Currency_ToPrimary, Double.class));
        // Language ====================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Currency_Language, String.class));
        dataObjectSchema.get(Currency_Language).getDisplayProperties().setVerbosityLevel(INFO_DISPLAY);
        // Country =====================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Currency_Country, String.class));
        dataObjectSchema.get(Currency_Country).getDisplayProperties().setVerbosityLevel(INFO_DISPLAY);
        // NumberFormat ================================================================================================
        dataObjectSchema.add(new DataField_Schema<>(Currency_NumberFormat, NumberFormat.class));
        dataObjectSchema.get(Currency_NumberFormat).getDisplayProperties().setVerbosityLevel(TRACE_DISPLAY);
        dataObjectSchema.<NumberFormat>get(Currency_NumberFormat).setDataCore_factory(
                new Derived_DataCore.Derived_DataCore_Factory<NumberFormat, Currency>
                        (dataObject -> NumberFormat.getCurrencyInstance(new Locale(dataObject.getLanguage(), dataObject.getCountry()))
                                , new LocalSource.LocalSource_Factory<>(Currency_Country)
                                , new LocalSource.LocalSource_Factory<>(Currency_Language)));
        //==============================================================================================================
        // Parents
        // Children

        return dataObjectSchema.finaliseContainer(Currency.class);
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
