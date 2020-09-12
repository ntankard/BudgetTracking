package com.ntankard.Tracking.DataBase.Core;

import com.ntankard.javaObjectDatabase.CoreObject.Field.DataCore.Derived_DataCore;
import com.ntankard.javaObjectDatabase.CoreObject.FieldContainer;
import com.ntankard.javaObjectDatabase.CoreObject.Interface.HasDefault;
import com.ntankard.Tracking.DataBase.Core.BaseObject.NamedDataObject;
import com.ntankard.javaObjectDatabase.CoreObject.Field.DataField;

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
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = NamedDataObject.getFieldContainer();

        // ID
        // Name
        // Default =====================================================================================================
        fieldContainer.add(new DataField<>(Currency_Default, Boolean.class));
        // ToPrimary ===================================================================================================
        fieldContainer.add(new DataField<>(Currency_ToPrimary, Double.class));
        // Language ====================================================================================================
        fieldContainer.add(new DataField<>(Currency_Language, String.class));
        fieldContainer.get(Currency_Language).getDisplayProperties().setVerbosityLevel(INFO_DISPLAY);
        // Country =====================================================================================================
        fieldContainer.add(new DataField<>(Currency_Country, String.class));
        fieldContainer.get(Currency_Country).getDisplayProperties().setVerbosityLevel(INFO_DISPLAY);
        // NumberFormat ================================================================================================
        fieldContainer.add(new DataField<>(Currency_NumberFormat, NumberFormat.class));
        fieldContainer.get(Currency_NumberFormat).getDisplayProperties().setVerbosityLevel(TRACE_DISPLAY);
        fieldContainer.<NumberFormat>get(Currency_NumberFormat).setDataCore(
                new Derived_DataCore<NumberFormat, Currency>
                        (dataObject -> NumberFormat.getCurrencyInstance(new Locale(dataObject.getLanguage(), dataObject.getCountry()))
                                , new Derived_DataCore.LocalSource<>(fieldContainer.get(Currency_Country))
                                , new Derived_DataCore.LocalSource<>(fieldContainer.get(Currency_Language))));
        //==============================================================================================================
        // Parents
        // Children

        return fieldContainer.finaliseContainer(Currency.class);
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
