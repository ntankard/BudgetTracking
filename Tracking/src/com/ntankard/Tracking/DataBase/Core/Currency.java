package com.ntankard.Tracking.DataBase.Core;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.HasDefault;
import com.ntankard.Tracking.DataBase.Core.BaseObject.NamedDataObject;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.ntankard.ClassExtension.MemberProperties.*;

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

    // My values
    private Boolean isDefault;
    private Double toPrimary;
    private String language;
    private String country;

    // Generated value
    private NumberFormat numberFormat;

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getName", "isDefault", "getToPrimary", "getLanguage", "getCountry"})
    public Currency(Integer id, String name, Boolean isDefault, Double toPrimary, String language, String country) {
        super(id, name);
        this.isDefault = isDefault;
        this.toPrimary = toPrimary;
        this.language = language;
        this.country = country;

        this.numberFormat = NumberFormat.getCurrencyInstance(new Locale(language, country));
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 2000000)
    public List<DataObject> getParents() {
        return new ArrayList<>();
    }

    /**
     * Get the appropriate formatter for this currency type
     *
     * @return The formatter for this currency
     */
    @MemberProperties(verbosityLevel = TRACE_DISPLAY)
    @DisplayProperties(order = 1150000)
    public NumberFormat getNumberFormat() {
        return numberFormat;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    // 1000000--getID
    // 1100000----getName

    @Override
    @DisplayProperties(order = 1110000)
    public Boolean isDefault() {
        return isDefault;
    }

    @DisplayProperties(order = 1120000)
    public Double getToPrimary() {
        return toPrimary;
    }

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 1130000)
    public String getLanguage() {
        return language;
    }

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 1140000)
    public String getCountry() {
        return country;
    }

    // 1150000------getNumberFormat (Above)
    // 2000000--getParents (Above)
    // 3000000--getChildren
}
