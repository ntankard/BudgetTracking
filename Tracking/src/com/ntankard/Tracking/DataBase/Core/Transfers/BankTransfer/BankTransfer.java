package com.ntankard.Tracking.DataBase.Core.Transfers.BankTransfer;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank;
import com.ntankard.Tracking.DataBase.Core.Transfers.Transfer;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;

import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;
import static com.ntankard.ClassExtension.MemberProperties.TRACE_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public abstract class BankTransfer extends Transfer<Bank, Bank> {

    /**
     * Constructor
     */
    @ParameterMap(shouldSave = false)
    public BankTransfer(Integer id, String description, Double value, Period period, Bank source, Bank destination) {
        super(id, description, value, period, source, destination, null);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    // 1000000--getID
    // 1100000----getPeriod

    @Override
    @MemberProperties(verbosityLevel = TRACE_DISPLAY)
    @DisplayProperties(order = 1200000)
    public String getDescription() {
        return super.getDescription();
    }

    @Override
    @DisplayProperties(order = 1300000)
    public Bank getSource() {
        return super.getSource();
    }

    // 1400000----getSourceValue

    @Override
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 1500000)
    public Currency getSourceCurrency() {
        return super.getSource().getCurrency();
    }

    @Override
    @DisplayProperties(order = 1600000)
    public Bank getDestination() {
        return super.getDestination();
    }

    // 1700000----getDestinationValue

    @Override
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 1800000)
    public Currency getDestinationCurrency() {
        return getDestination().getCurrency();
    }

    // 2000000--getParents
    // 3000000--getChildren

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @Override
    @SetterProperties(localSourceMethod = "sourceOptions")
    public void setSource(Bank source) {
        if (source == null) throw new IllegalArgumentException("Source is null");

        this.source.notifyChildUnLink(this);
        this.source = source;

        List<Bank> options = sourceOptions(Bank.class, "Destination");
        if (!options.contains(getDestination())) {
            setDestination(options.get(0));
        }

        this.source.notifyChildLink(this);
    }
}
