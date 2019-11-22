package com.ntankard.Tracking.DataBase.Database;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.HasDefault;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.SpecialValues;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Fund.Fund;
import com.ntankard.Tracking.DataBase.Core.Pool.Fund.FundEvent;
import com.ntankard.Tracking.DataBase.Core.Transfers.BankTransfer.BankTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfers.BankTransfer.IntraCurrencyBankTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfers.CategoryFundTransfer;
import com.ntankard.Tracking.DataBase.Interface.Set.ExactFull_Set;

import java.util.ArrayList;
import java.util.List;

public class TrackingDatabase_Integrity {

    /**
     * Validate the core of the database. All things involved with central data, not calculated values.
     */
    public static void validateCore() {
        // Are individual objects ok?
        validateId();

        // Are objects linked correctly?
        validateParent();
        validateChild();

        // Is the database exposing the correct special objects?
        validateDefault();
        validateSpecial();

        // Validate the transfer objects
        validateBankTransfer();
        validateIntraCurrencyBankTransfer();
    }

    public static void validateRepaired() {
        validateCategoryFundTransfer();
        validatePeriodSequence();
        validateFundFundEvent();
    }

    //------------------------------------------------------------------------------------------------------------------
    //########################################## Individual object Integrity ###########################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Confirm the ID of all object. Check they are unique
     */
    static void validateId() {
        for (DataObject dataObject : TrackingDatabase.get().getAll()) {
            for (DataObject toTest : TrackingDatabase.get().getAll()) {
                if (!dataObject.equals(toTest)) {
                    if (dataObject.getId().equals(toTest.getId())) {
                        throw new RuntimeException("Core Database error. Duplicate ID found");
                    }
                }
            }
        }
    }


    //------------------------------------------------------------------------------------------------------------------
    //########################################## Object interlink Integrity ############################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Confirm that all parent objects are present and have been linked
     */
    static void validateParent() {
        for (DataObject dataObject : TrackingDatabase.get().getAll()) {
            for (DataObject parent : dataObject.getParents()) {
                if (parent == null) {
                    throw new RuntimeException("Core Database error. Null parent detected");
                }
                if (!parent.getChildren().contains(dataObject)) {
                    throw new RuntimeException("Core Database error. Parent has not been notified");
                }
            }
        }
    }

    /**
     * Confirm that all children that the object knows about are present and connected to the parent
     */
    static void validateChild() {
        for (DataObject dataObject : TrackingDatabase.get().getAll()) {
            for (DataObject child : dataObject.getChildren()) {
                if (child == null) {
                    throw new RuntimeException("Core Database error. Null child detected");
                }
                if (!child.getParents().contains(dataObject)) {
                    throw new RuntimeException("Core Database error. Object registers as a child that dose not list this object as a parent");
                }
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //########################################### Database content Integrity ###########################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Confirm that all default values are set and correct
     */
    static void validateDefault() {
        for (Class<? extends DataObject> aClass : TrackingDatabase.get().getDataObjectTypes()) {
            if (HasDefault.class.isAssignableFrom(aClass)) {
                if (TrackingDatabase.get().getDefault(aClass) == null) {
                    throw new RuntimeException("Core Database error. An object with a default value has no default set");
                }
                if (!((HasDefault) TrackingDatabase.get().getDefault(aClass)).isDefault()) {
                    throw new RuntimeException("Core Database error. A non default object has been set as the default");
                }
            }
        }
    }

    /**
     * Confirm that all special values are present in the database, and are the correct ones
     */
    static void validateSpecial() {
        for (Class<? extends DataObject> aClass : TrackingDatabase.get().getDataObjectTypes()) {
            if (TrackingDatabase.get().get(aClass).size() != 0) {
                if (SpecialValues.class.isAssignableFrom(aClass)) {
                    for (int key : ((SpecialValues) TrackingDatabase.get().get(aClass).get(0)).getKeys()) {
                        if (TrackingDatabase.get().getSpecialValue(aClass, key) == null) {
                            throw new RuntimeException("Core Database error. An object with a special value has no special value set");
                        }
                    }
                }
            }
        }
    }

    /**
     * Validate that the Bank Transfer is in the correct state
     */
    static void validateBankTransfer() {
        for (BankTransfer bankTransfer : new ExactFull_Set<>(BankTransfer.class).get()) {
            if (bankTransfer.getSource().equals(bankTransfer.getDestination())) {
                throw new RuntimeException("Transferring to itself");
            }

            if (!bankTransfer.getSource().getCurrency().equals(bankTransfer.getDestination().getCurrency())) {
                throw new RuntimeException("Transferring between banks with different currencies");
            }

            if (!bankTransfer.getValue().equals(bankTransfer.getDestinationValue()) || !bankTransfer.getDestinationValue().equals(-bankTransfer.getSourceValue())) {
                throw new RuntimeException("Fund source and destination values do not match when they should");
            }

            if (!bankTransfer.getCurrency().equals(bankTransfer.getDestinationCurrency()) || !bankTransfer.getDestinationCurrency().equals(bankTransfer.getSourceCurrency())) {
                throw new RuntimeException("Fund source and destination currencies do not match when they should");
            }
        }
    }

    /**
     * Validate that the IntraCurrencyBankTransfers are in the correct state
     */
    static void validateIntraCurrencyBankTransfer() {
        for (IntraCurrencyBankTransfer intraCurrencyBankTransfer : TrackingDatabase.get().get(IntraCurrencyBankTransfer.class)) {
            if (intraCurrencyBankTransfer.getSource().equals(intraCurrencyBankTransfer.getDestination())) {
                throw new RuntimeException("Transferring to itself");
            }

            if (intraCurrencyBankTransfer.getSource().getCurrency().equals(intraCurrencyBankTransfer.getDestination().getCurrency())) {
                throw new RuntimeException("Transferring between banks with same currencies");
            }
        }
    }

    /**
     * Validate that the CategoryFundTransfers are in the correct state
     */
    static void validateCategoryFundTransfer() {
        for (CategoryFundTransfer categoryFundTransfer : TrackingDatabase.get().get(CategoryFundTransfer.class)) {
            if (!categoryFundTransfer.getDestination().getChildren(FundEvent.class).contains(categoryFundTransfer.getFundEvent())) {
                throw new RuntimeException("Fund and Fund event don't match");
            }

            if (!categoryFundTransfer.getValue().equals(categoryFundTransfer.getDestinationValue()) || !categoryFundTransfer.getDestinationValue().equals(-categoryFundTransfer.getSourceValue())) {
                throw new RuntimeException("Fund source and destination values do not match when they should");
            }

            if (!categoryFundTransfer.getCurrency().equals(categoryFundTransfer.getDestinationCurrency()) || !categoryFundTransfer.getDestinationCurrency().equals(categoryFundTransfer.getSourceCurrency())) {
                throw new RuntimeException("Fund source and destination currencies do not match when they should");
            }
        }
    }

    /**
     * Validate that all periods are in a row with none missing. Also validate that they are connected properly
     */
    static void validatePeriodSequence() {
        List<Period> periods = new ArrayList<>(TrackingDatabase.get().get(Period.class));

        // Find the first period
        Period start = periods.get(0);
        while (start.getLast() != null) {
            start = start.getLast();
        }

        // Iterate through all periods
        periods.remove(start);
        while (start.getNext() != null) {
            if (!start.getNext().getLast().equals(start)) {
                throw new RuntimeException("Core Database error. Periods are not sequential");
            }
            if (start.getMonth() == 12) {
                if (start.getNext().getMonth() != 1 || start.getYear() != start.getNext().getYear() - 1) {
                    throw new RuntimeException("Core Database error. Periods dates did not roll over");
                }
            } else {
                if (start.getMonth() + 1 != start.getNext().getMonth()) {
                    throw new RuntimeException("Core Database error. Months out of over");
                }
            }

            start = start.getNext();
            periods.remove(start);
        }

        if (periods.size() != 0) {
            throw new RuntimeException("Core Database error. Not all periods are in order");
        }
    }

    /**
     * Validate that all funds are fund events that are setup properly
     */
    static void validateFundFundEvent() {
        for (Fund fund : TrackingDatabase.get().get(Fund.class)) {
            if (fund.getDefaultFundEvent() == null) {
                throw new RuntimeException("Fund dose not have a default set");
            }

            if (fund.getChildren(FundEvent.class).size() == 0) {
                throw new RuntimeException("A fund has no Fund events available");
            }
        }
    }
}
