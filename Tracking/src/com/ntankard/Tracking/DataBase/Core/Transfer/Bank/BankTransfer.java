package com.ntankard.Tracking.DataBase.Core.Transfer.Bank;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.DataObject_Field;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Field;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Core.Transfer.Transfer;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY;
import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public abstract class BankTransfer extends Transfer {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all the fields for this object
     */
    public static List<Field<?>> getFields(Integer id, String description,
                                           Period period, Bank source, Double value,
                                           Period destinationPeriod, Pool destination, Double destinationValue,
                                           DataObject container) {
        List<Field<?>> toReturn = Transfer.getFields(id, description, period, source, container);
        toReturn.add(new Field<>("value", Double.class, value, container));
        toReturn.add(new DataObject_Field<>("destinationPeriod", Period.class, destinationPeriod, true, container));
        toReturn.add(new DataObject_Field<>("destination", Pool.class, destination, container));
        toReturn.add(new Field<>("destinationValue", Double.class, destinationValue, true, container));
        return toReturn;
    }

    /**
     * {@inheritDoc
     */
    @Override
    @SuppressWarnings({"SuspiciousMethodCalls", "unchecked"})
    public <T extends DataObject> List<T> sourceOptions(Class<T> type, String fieldName) {
        switch (fieldName) {
            case "DestinationPeriod":
                if (doseSupportDestinationPeriod()) {
                    List<T> toReturn = super.sourceOptions(type, fieldName);
                    toReturn.add(null);
                    toReturn.remove(getPeriod());
                    return toReturn;
                } else {
                    List<T> toReturn = new ArrayList<>();
                    toReturn.add(null);
                    return toReturn;
                }
            case "Destination":
            case "Bank": {
                List<T> toReturn = super.sourceOptions(type, fieldName);
                toReturn.remove(getSource());
                return toReturn;
            }
            case "Source": {
                List<T> toReturn = new ArrayList<>();
                for (Bank bank : super.sourceOptions(Bank.class, fieldName)) {
                    toReturn.add((T) bank);
                }
                return toReturn;
            }
        }
        return super.sourceOptions(type, fieldName);
    }

    /**
     * Dose this object, in its current state support setting a destination value?
     *
     * @return True if it supports setting a destination value
     */
    boolean doseSupportDestinationValue() {
        return doseSupportDestinationCurrency();
    }

    /**
     * Dose this object, in its current state support setting a destination currency?
     *
     * @return True if it supports setting a destination currency
     */
    boolean doseSupportDestinationCurrency() {
        if (getDestination() instanceof Bank) {
            Currency sourceCurrency = ((Bank) getSource()).getCurrency();
            Currency destinationCurrency = ((Bank) getDestination()).getCurrency();
            return !sourceCurrency.equals(destinationCurrency);
        }
        return false;
    }

    /**
     * Dose this object, in its current state support setting a destination period?
     *
     * @return True if it supports setting a destination period
     */
    boolean doseSupportDestinationPeriod() {
        return getDestination() instanceof Bank || getDestination() instanceof Category;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    // 1000000--getID
    // 1100000----getDescription
    // 1200000----getPeriod

    @Override
    @DisplayProperties(order = 1300000)
    public Pool getSource() {
        return super.getSource();
    }

    @Override
    @DisplayProperties(order = 1400000, dataType = CURRENCY)
    public Double getValue() {
        return get("value");
    }

    @Override
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 1500000)
    public Currency getCurrency() {
        return ((Bank) getSource()).getCurrency();
    }

    @DisplayProperties(order = 1510000)
    public Period getDestinationPeriod() {
        return get("destinationPeriod");
    }

    // 1520000----getCategory   (Below)
    // 1530000----getBank       (Below)
    // 1540000----getFundEvent  (Below)

    @Override
    @DisplayProperties(order = 1600000)
    public Pool getDestination() {
        return get("destination");
    }

    @DisplayProperties(order = 1610000, dataType = CURRENCY)
    public Double getDestinationValue() {
        return get("destinationValue");
    }

    @DisplayProperties(order = 1620000)
    public Currency getDestinationCurrency() {
        if (doseSupportDestinationCurrency()) {
            return ((Bank) getDestination()).getCurrency();
        }
        return null;
    }

    // 1700000----getSourceTransfer
    // 1800000----getDestinationTransfer
    // 2000000--getParents (Above)
    // 3000000--getChildren

    //------------------------------------------------------------------------------------------------------------------
    //############################################### Specialty Getters ################################################
    //------------------------------------------------------------------------------------------------------------------

    @DisplayProperties(order = 1520000)
    public Category getCategory() {
        if (getDestination() instanceof Category) {
            return (Category) getDestination();
        }
        return null;
    }

    @DisplayProperties(order = 1530000)
    public Bank getBank() {
        if (getDestination() instanceof Bank) {
            return (Bank) getDestination();
        }
        return null;
    }

    @DisplayProperties(order = 1540000)
    public FundEvent getFundEvent() {
        if (getDestination() instanceof FundEvent) {
            return (FundEvent) getDestination();
        }
        return null;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public void setValue(Double value) {
        set("value", value);
        updateHalfTransfer();
    }

    @SetterProperties(localSourceMethod = "sourceOptions")
    public void setDestinationPeriod(Period destinationPeriod) {
        if (destinationPeriod != null && !doseSupportDestinationPeriod())
            throw new IllegalArgumentException("Destination period can only be set in a bank to bank transfer");
        if (getPeriod().equals(destinationPeriod))
            throw new IllegalArgumentException("Can not set the destination period to be the same as the source (this is the default)");

        set("destinationPeriod", destinationPeriod);
        updateHalfTransfer();
        validateParents();
    }

    @SetterProperties(localSourceMethod = "sourceOptions")
    public void setDestination(Pool destination) {
        if (getSource().equals(destination)) throw new IllegalArgumentException("Source equals Destination");

        set("destination", destination);

        // Destination Period can only be maintained if its a Bank to Bank transfer
        if (!doseSupportDestinationPeriod()) {
            setDestinationPeriod(null);
        }

        // Destination value can only be maintained if its a bank to bank transfer with different currencies
        if (!doseSupportDestinationValue()) {
            setDestinationValue(null);
        }

        updateHalfTransfer();
        validateParents();
    }

    public void setDestinationValue(Double destinationValue) {
        Double destinationValue_toSet = destinationValue;

        // Destination value can only be maintained if its a bank to bank transfer with different currencies
        if (!doseSupportDestinationValue()) {
            destinationValue_toSet = null;
        }
        if (destinationValue != null && destinationValue.equals(0.0)) {
            destinationValue_toSet = null;
        }
        set("destinationValue", destinationValue_toSet);

        updateHalfTransfer();
        validateParents();
    }

    @Override
    protected void setSource(Pool bank) {
        if (!(bank instanceof Bank)) throw new IllegalArgumentException("Source is not a bank");
        super.setSource(bank);

        // Destination value can only be maintained if its a bank to bank transfer with different currencies
        if (!(getDestination() instanceof Bank && getDestinationCurrency() != null)) {
            setDestinationValue(null);
        }

        updateHalfTransfer();
        validateParents();
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################ Specialty Setters ###############################################
    //------------------------------------------------------------------------------------------------------------------

    @SetterProperties(localSourceMethod = "sourceOptions")
    public void setCategory(Category category) {
        setDestination(category);
    }

    @SetterProperties(localSourceMethod = "sourceOptions")
    public void setBank(Bank bank) {
        setDestination(bank);
    }

    @SetterProperties(localSourceMethod = "sourceOptions")
    public void setFundEvent(FundEvent fundEvent) {
        setDestination(fundEvent);
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################# HalfTransfer Interface #############################################
    //------------------------------------------------------------------------------------------------------------------

    @Override
    protected Period getPeriod(boolean isSource) {
        if (isSource) {
            return getPeriod();
        } else {
            if (getDestinationPeriod() != null) {
                return getDestinationPeriod();
            } else {
                return getPeriod();
            }
        }
    }

    @Override
    protected Double getValue(boolean isSource) {
        if (isSource) {
            return -getValue();
        } else {
            if (getDestinationValue() != null) {
                return getDestinationValue();
            } else {
                return getValue();
            }
        }
    }

    @Override
    protected Currency getCurrency(boolean isSource) {
        if (isSource) {
            return getCurrency();
        } else {
            if (getDestinationCurrency() != null) {
                return getDestinationCurrency();
            } else {
                return getCurrency();
            }
        }
    }
}

// 1000000--getID
// 1100000----getDescription
// 1200000----getPeriod
// 1300000----getSource
// 1400000----getValue
// 1500000----getCurrency
// 1510000------getDestinationPeriod
// 1520000------getCategory
// 1530000------getBank
// 1540000------getFundEvent
// 1600000----getDestination
// 1610000------getDestinationValue
// 1620000------getDestinationCurrency
// 1700000----getSourceTransfer
// 1800000----getDestinationTransfer
// 2000000--getParents (Above)
// 3000000--getChildren
