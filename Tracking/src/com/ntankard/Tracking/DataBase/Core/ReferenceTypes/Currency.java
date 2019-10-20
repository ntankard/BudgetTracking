package com.ntankard.Tracking.DataBase.Core.ReferenceTypes;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.DataObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class Currency extends DataObject {

    // My values
    private String id;
    private double toSecondary;
    private double toPrimary;
    private boolean isPrimary;
    private String language;
    private String country;

    // Generated value
    private NumberFormat numberFormat;

    /**
     * Constructor
     */
    public Currency(String id, double toSecondary, double toPrimary, boolean isPrimary, String language, String country) {
        this.id = id;
        this.toSecondary = toSecondary;
        this.toPrimary = toPrimary;
        this.isPrimary = isPrimary;
        this.language = language;
        this.country = country;

        this.numberFormat = NumberFormat.getCurrencyInstance(new Locale(language, country));
    }

    /**
     * {@inheritDoc
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    public List<DataObject> getParents() {
        return new ArrayList<>();
    }

    /**
     * Get the appropriate formatter for this currency type
     *
     * @return The formatter for this currency
     */
    public NumberFormat getNumberFormat() {
        return numberFormat;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public double getToSecondary() {
        return toSecondary;
    }

    public double getToPrimary() {
        return toPrimary;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public String getLanguage() {
        return language;
    }

    public String getCountry() {
        return country;
    }
}
