package com.ntankard.Tracking.DataBase;

import com.ntankard.Tracking.DataBase.Core.DataObject;
import com.ntankard.Tracking.DataBase.Core.IdDataObject;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Fund;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Statement;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.*;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Bank;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Category;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Currency;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.FundEvent;

import java.util.*;

public class TrackingDatabase {

    /**
     * Core data objects
     */
    private Map<Class, List<DataObject>> dataObjects = new HashMap<>();
    private Map<Class, Map<String, DataObject>> dataObjectsMap = new HashMap<>();
    private Map<String, Class> classMap = new HashMap<>();

    // Flag for database construction
    private boolean isFinalized = false;

    //------------------------------------------------------------------------------------------------------------------
    //############################################### Constructor ######################################################
    //------------------------------------------------------------------------------------------------------------------

    // Singleton constructor
    private static TrackingDatabase master;

    /**
     * Singleton access
     */
    public static TrackingDatabase get() {
        if (master == null) {
            master = new TrackingDatabase();
        }
        return master;
    }

    /**
     * Private Constructor
     */
    private TrackingDatabase() {
        addType(Currency.class);
        addType(Category.class);
        addType(Bank.class);
        addType(Period.class);
        addType(Statement.class);
        addType(Transaction.class);
        addType(CategoryTransfer.class);
        addType(PeriodTransfer.class);
        addType(Fund.class);
        addType(PeriodFundTransfer.class);
        addType(FundEvent.class);
        addType(FundChargeTransfer.class);
    }

    /**
     * Generate a container for a new object type
     *
     * @param aClass The type to add
     * @param <T>    Same as type
     */
    private <T extends DataObject> void addType(Class<T> aClass) {
        dataObjects.put(aClass, new ArrayList<>());
        dataObjectsMap.put(aClass, new HashMap<>());
        classMap.put(aClass.getSimpleName(), aClass);
    }

    /**
     * Repair any missing data
     */
    public void finalizeData() {
        for (Class aClass : dataObjects.keySet()) {
            for (DataObject dataObject : dataObjects.get(aClass)) {
                fix(dataObject);
            }
        }
        isFinalized = true;
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################ Data IO #########################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get the next free ID for the data type
     *
     * @param toGet The data type to search
     * @param <T>   Method restriction to limit data objects to ones with a individual integer ID
     * @return The next free ID
     */
    public <T extends IdDataObject> String getNextId(Class<T> toGet) {
        int max = 0;

        if (!dataObjects.containsKey(toGet)) {
            throw new RuntimeException("Impossible type");
        }

        for (DataObject t : dataObjects.get(toGet)) {
            int value = Integer.parseInt(t.getId());
            if (value > max) {
                max = value;
            }
        }
        return (max + 1) + "";
    }

    /**
     * Add a new element to the database. New elements are repaired if needed and all relevant parents are notified
     *
     * @param dataObject The object to add
     */
    public void add(DataObject dataObject) {
        if (!dataObjects.containsKey(dataObject.getClass()) || !dataObjectsMap.containsKey(dataObject.getClass())) {
            throw new RuntimeException("Impossible type");
        }

        this.dataObjects.get(dataObject.getClass()).add(dataObject);
        this.dataObjectsMap.get(dataObject.getClass()).put(dataObject.getId(), dataObject);
        if (isFinalized) {
            fix(dataObject);
        }
        dataObject.notifyParentLink();

        if (dataObjects.get(dataObject.getClass()).size() != dataObjectsMap.get(dataObject.getClass()).size()) {
            throw new RuntimeException("Duplicate ID");
        }
    }

    /**
     * Remove an element from the database as long as it has no children linking to it. All relevant parents are notified
     *
     * @param dataObject The object to remove
     */
    public void remove(DataObject dataObject) {
        if (!dataObjects.containsKey(dataObject.getClass()) || !dataObjectsMap.containsKey(dataObject.getClass())) {
            throw new RuntimeException("Impossible type");
        }
        if (dataObject.getChildren().size() != 0) {
            throw new RuntimeException("Deleting an object with dependencies");
        }

        dataObject.notifyParentUnLink();
        this.dataObjects.get(dataObject.getClass()).remove(dataObject);
        this.dataObjectsMap.get(dataObject.getClass()).remove(dataObject.getId());

        if (dataObjects.get(dataObject.getClass()).size() != dataObjectsMap.get(dataObject.getClass()).size()) {
            throw new RuntimeException("Error remove");
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################ Data Integrity ######################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * So whatever fixes are needed for the data object
     *
     * @param dataObject The object to fix
     */
    private void fix(DataObject dataObject) {
        if (dataObject instanceof Period) {
            fixPeriod((Period) dataObject);
        } else if (dataObject instanceof Fund) {
            fixFund((Fund) dataObject);
        }
    }

    /**
     * Ensure the fund has required defaults
     *
     * @param funds The fund to fix
     */
    private void fixFund(Fund funds) {
        boolean found = false;
        for (FundEvent fundEvent : funds.getChildren(FundEvent.class)) {
            if (fundEvent.getIdCode().equals("NONE")) {
                found = true;
                break;
            }
        }
        if (!found) {
            add(new FundEvent(funds, "NONE"));
        }
    }

    /**
     * Ensure that a period has all the data it should
     *
     * @param period The period to fix
     */
    private void fixPeriod(Period period) {
        Period last = period.getPreviousPeriod();
        for (Bank b : get(Bank.class)) {

            // Dose this period have a statement for this bank?
            Statement match = get(Statement.class, b.getId() + " " + period.getId());
            if (match == null) {

                // Find the statement from the previous month if it exists
                double end = 0.0;
                if (last != null) {
                    Statement lastStatement = get(Statement.class, b.getId() + " " + last.getId());
                    end = lastStatement.getEnd();
                }

                add(new Statement(b, period, end, 0.0, 0.0, 0.0));
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################ Accessors #######################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get an element of the data base based on its unique ID
     *
     * @param type The data type to get
     * @param id   The ID of the object to get
     * @param <T>  The data type to return (same as type)
     * @return The element of the Database
     */
    @SuppressWarnings("unchecked")
    public <T extends DataObject> T get(Class<T> type, String id) {
        return (T) dataObjectsMap.get(type).get(id);
    }

    /**
     * Get all elements of the database of a certain type
     *
     * @param type The data type to get
     * @param <T>  The data type to return (same as type)
     * @return A unmodifiableList of all elements of that type
     */
    @SuppressWarnings("unchecked")
    public <T extends DataObject> List<T> get(Class<T> type) {
        return Collections.unmodifiableList((List<T>) dataObjects.get(type));
    }

    /**
     * Get all elements of the database of a certain type name
     *
     * @param type The data type to get
     * @param <T>  The data type to return (same as type)
     * @return A unmodifiableList of all elements of that type
     */
    @SuppressWarnings("unchecked")
    public <T extends DataObject> List<T> getData(String type) {
        return get(classMap.get(type));
    }
}
