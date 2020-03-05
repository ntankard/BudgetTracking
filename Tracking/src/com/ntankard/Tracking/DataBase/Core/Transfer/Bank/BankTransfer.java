package com.ntankard.Tracking.DataBase.Core.Transfer.Bank;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.ClassExtension.SetterProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Core.Transfer.Transfer;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY;
import static com.ntankard.ClassExtension.MemberProperties.DEBUG_DISPLAY;
import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public abstract class BankTransfer extends Transfer {

    // My parents
    private Pool destination;
    private Period destinationPeriod;

    // My values
    private Double value;
    private Double destinationValue;

    /**
     * Constructor
     */
    @ParameterMap(shouldSave = false)
    public BankTransfer(Integer id, String description,
                        Period period, Bank source, Double value,
                        Period destinationPeriod, Pool destination, Double destinationValue) {
        super(id, description, period, source);
        if (value == null) throw new IllegalArgumentException("Value is null");
        if (destination == null) throw new IllegalArgumentException("Destination is null");
        this.destination = destination;
        this.value = value;
        this.destinationPeriod = destinationPeriod;
        this.destinationValue = destinationValue;

        // Check that custom destination values can only be set if the bank is another currency
        if (destination instanceof Bank) {
            if (((Bank) getSource()).getCurrency().equals(((Bank) getDestination()).getCurrency())) {
                if (destinationValue != null) {
                    throw new IllegalArgumentException("Trying to set a destination value when not supported");
                }
            }
        } else {
            if (!(destination instanceof Category)) {
                // Check that any other type of transfer cannot have a destination period
                if (destinationPeriod != null) {
                    throw new RuntimeException("Trying to set a destination period when its not supported");
                }
            }
            // Check that any other type of transfer cannot have a destination value
            if (destinationValue != null) {
                throw new IllegalArgumentException("Trying to set a destination value when not supported");
            }
        }
    }

    /**
     * {@inheritDoc
     */
    @Override
    @MemberProperties(verbosityLevel = DEBUG_DISPLAY)
    @DisplayProperties(order = 2000000)
    public List<DataObject> getParents() {
        List<DataObject> toReturn = super.getParents();
        toReturn.add(getDestination());
        if (getDestinationPeriod() != null) {
            toReturn.add(getDestinationPeriod());
        }
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
                if (getDestination() instanceof Bank || getDestination() instanceof Category) {
                    List<T> toReturn = TrackingDatabase.get().get(type);
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
                List<T> toReturn = TrackingDatabase.get().get(type);
                toReturn.remove(getSource());
                return toReturn;
            }
            case "Source": {
                List<T> toReturn = new ArrayList<>();
                for (Bank bank : TrackingDatabase.get().get(Bank.class)) {
                    toReturn.add((T) bank);
                }
                return toReturn;
            }
        }
        return super.sourceOptions(type, fieldName);
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
        return value;
    }

    @Override
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 1500000)
    public Currency getCurrency() {
        return ((Bank) getSource()).getCurrency();
    }

    @DisplayProperties(order = 1510000)
    public Period getDestinationPeriod() {
        return destinationPeriod;
    }

    // 1520000----getCategory   (Below)
    // 1530000----getBank       (Below)
    // 1540000----getFundEvent  (Below)

    @Override
    @DisplayProperties(order = 1600000)
    public Pool getDestination() {
        return destination;
    }

    @DisplayProperties(order = 1610000, dataType = CURRENCY)
    public Double getDestinationValue() {
        return destinationValue;
    }

    @DisplayProperties(order = 1620000)
    public Currency getDestinationCurrency() {
        if (getDestination() instanceof Bank) {
            if (!((Bank) getSource()).getCurrency().equals(((Bank) getDestination()).getCurrency())) {
                return ((Bank) getDestination()).getCurrency();
            }
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
        if (value == null) throw new IllegalArgumentException("Value is null");
        this.value = value;
    }

    @SetterProperties(localSourceMethod = "sourceOptions")
    public void setDestinationPeriod(Period destinationPeriod) {
        if (!(getDestination() instanceof Bank || getDestination() instanceof Category)) {
            if (destinationPeriod != null) {
                throw new IllegalArgumentException("Destination period can only be set in a bank to bank transfer");
            }
        }
        if (destinationPeriod != null && destinationPeriod.equals(getPeriod()))
            throw new IllegalArgumentException("Can not set the destination period to be the same as the source (this is the default)");
        if (this.destinationPeriod != null) {
            this.destinationPeriod.notifyChildUnLink(this);
        }
        this.destinationPeriod = destinationPeriod;
        if (this.destinationPeriod != null) {
            this.destinationPeriod.notifyChildLink(this);
        }
        getDestinationTransfer().setPeriod(getPeriod(false));
        validateParents();
    }

    @SetterProperties(localSourceMethod = "sourceOptions")
    public void setDestination(Pool destination) {
        if (destination == null) throw new IllegalArgumentException("Destination is null");
        if (getSource().equals(destination)) throw new IllegalArgumentException("Destination equals source");
        this.destination.notifyChildUnLink(this);
        this.destination = destination;
        this.destination.notifyChildLink(this);

        // Destination Period can only be maintained if its a Bank to Bank transfer
        if (!(destination instanceof Bank || destination instanceof Category)) {
            setDestinationPeriod(null);
        }

        // Destination value can only be maintained if its a bank to bank transfer with different currencies
        if (!(destination instanceof Bank && getDestinationCurrency() != null)) {
            setDestinationValue(null);
        }

        getDestinationTransfer().setPeriod(getPeriod(false));
        getDestinationTransfer().setPool(getPool(false));
        getDestinationTransfer().setCurrency(getCurrency(false));
        validateParents();
    }

    public void setDestinationValue(Double destinationValue) {
        Double destinationValue_toSet = destinationValue;

        // Destination value can only be maintained if its a bank to bank transfer with different currencies
        if (!(getDestination() instanceof Bank && getDestinationCurrency() != null)) {
            destinationValue_toSet = null;
        }
        if (destinationValue != null && destinationValue.equals(0.0)) {
            destinationValue_toSet = null;
        }
        this.destinationValue = destinationValue_toSet;
        validateParents();
    }

    @Override
    protected void setSource(Pool bank) {
        if (!(bank instanceof Bank)) throw new IllegalArgumentException("Source is not a bank");
        super.setSource(bank);

        // Destination value can only be maintained if its a bank to bank transfer with different currencies
        if (!(destination instanceof Bank && getDestinationCurrency() != null)) {
            setDestinationValue(null);
        }
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
            return getValue();
        } else {
            if (getDestinationValue() != null) {
                return getDestinationValue();
            } else {
                return -getValue();
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
