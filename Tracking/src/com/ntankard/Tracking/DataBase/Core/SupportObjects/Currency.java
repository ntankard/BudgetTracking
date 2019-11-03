package com.ntankard.Tracking.DataBase.Core.SupportObjects;

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
    private boolean isDefault;
    private double toPrimary;
    private String language;
    private String country;

    // Generated value
    private NumberFormat numberFormat;

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getName", "isDefault", "getToPrimary", "getLanguage", "getCountry"})
    public Currency(String id, String name, boolean isDefault, double toPrimary, String language, String country) {
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
    @DisplayProperties(order = 21)
    public List<DataObject> getParents() {
        return new ArrayList<>();
    }

    /**
     * Get the appropriate formatter for this currency type
     *
     * @return The formatter for this currency
     */
    @MemberProperties(verbosityLevel = TRACE_DISPLAY)
    @DisplayProperties(order = 7)
    public NumberFormat getNumberFormat() {
        return numberFormat;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @Override
    @DisplayProperties(order = 3)
    public boolean isDefault() {
        return isDefault;
    }

    @DisplayProperties(order = 4)
    public double getToPrimary() {
        return toPrimary;
    }

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 5)
    public String getLanguage() {
        return language;
    }

    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 6)
    public String getCountry() {
        return country;
    }
}