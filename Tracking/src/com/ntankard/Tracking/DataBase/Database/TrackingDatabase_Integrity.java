package com.ntankard.Tracking.DataBase.Database;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.HasDefault;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.SpecialValues;
import com.ntankard.Tracking.DataBase.Core.Transfers.BankTransfer.CurrencyBankTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfers.BankTransfer.IntraCurrencyBankTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfers.CategoryFundTransfer.CategoryFundTransfer;
import com.ntankard.Tracking.DataBase.Interface.Set.Full_Set;

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
        validateCurrencyBankTransfer();
        validateIntraCurrencyBankTransfer();
    }

    public static void validateRepaired() {
        validateCategoryFundTransfer();
    }

    //------------------------------------------------------------------------------------------------------------------
    //########################################## Individual object Integrity ###########################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Confirm the ID of all object. Check they are unique
     */
    public static void validateId() {
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
    public static void validateParent() {
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
    public static void validateChild() {
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
    public static void validateDefault() {
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
    public static void validateSpecial() {
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
    public static void validateCurrencyBankTransfer() {
        for (CurrencyBankTransfer currencyBankTransfer : new Full_Set<>(CurrencyBankTransfer.class).get()) {
            if (currencyBankTransfer.getSource().equals(currencyBankTransfer.getDestination())) {
                throw new RuntimeException("Transferring to itself");
            }

            if (!currencyBankTransfer.getSource().getCurrency().equals(currencyBankTransfer.getDestination().getCurrency())) {
                throw new RuntimeException("Transferring between banks with different currencies");
            }

            if (!currencyBankTransfer.getDestinationValue().equals(-currencyBankTransfer.getSourceValue())) {
                throw new RuntimeException("Fund source and destination values do not match when they should");
            }

            if (!currencyBankTransfer.getDestinationCurrency().equals(currencyBankTransfer.getSourceCurrency())) {
                throw new RuntimeException("Fund source and destination currencies do not match when they should");
            }
        }
    }

    /**
     * Validate that the IntraCurrencyBankTransfers are in the correct state
     */
    public static void validateIntraCurrencyBankTransfer() {
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
    public static void validateCategoryFundTransfer() {
        for (CategoryFundTransfer categoryFundTransfer : TrackingDatabase.get().get(CategoryFundTransfer.class)) {
            if (!categoryFundTransfer.getDestinationValue().equals(-categoryFundTransfer.getSourceValue())) {
                throw new RuntimeException("Fund source and destination values do not match when they should");
            }

            if (!categoryFundTransfer.getDestinationCurrency().equals(categoryFundTransfer.getSourceCurrency())) {
                throw new RuntimeException("Fund source and destination currencies do not match when they should");
            }
        }
    }
}
