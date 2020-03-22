package com.ntankard.Tracking.DataBase.Core;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Field;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.HasDefault;
import com.ntankard.Tracking.DataBase.Core.BaseObject.NamedDataObject;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;
import static com.ntankard.ClassExtension.MemberProperties.TRACE_DISPLAY;

@ClassExtensionProperties(includeParent = true)
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

    /**
     * Get all the fields for this object
     */
    public static List<Field<?>> getFields() {
        List<Field<?>> toReturn = NamedDataObject.getFields();
        toReturn.add(new Field<>("isDefault", Boolean.class));
        toReturn.add(new Field<>("getToPrimary", Double.class));
        toReturn.add(new Field<>("getLanguage", String.class));
        toReturn.add(new Field<>("getCountry", String.class));
        return toReturn;
    }

    /**
     * Get the appropriate formatter for this currency type
     *
     * @return The formatter for this currency
     */
    @MemberProperties(verbosityLevel = TRACE_DISPLAY)
    @DisplayProperties(order = 1150000)
    public NumberFormat getNumberFormat() {
        return NumberFormat.getCurrencyInstance(new Locale(getLanguage(), getCountry()));
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    // 1000000--getID
    // 1100000----getName

    @Override
    @DisplayProperties(order = 1110000)
    public Boolean isDefault() {
        return get("isDefault");
    }

    @DisplayProperties(order = 1120000)
    public Double getToPrimary() {
        return get("getToPrimary");
    }

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 1130000)
    public String getLanguage() {
        return get("getLanguage");
    }

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 1140000)
    public String getCountry() {
        return get("getCountry");
    }

    // 1150000------getNumberFormat (Above)
    // 2000000--getParents (Above)
    // 3000000--getChildren
}
