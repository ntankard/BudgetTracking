package com.ntankard.Tracking.DataBase.Database;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Fund;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Statement;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.*;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.FundChargeTransfer.FundChargeTransfer;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.FundChargeTransfer.TaxChargeTransfer;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.FundChargeTransfer.SavingsChargeTransfer;
import com.ntankard.Tracking.DataBase.Core.SupportObjects.Bank;
import com.ntankard.Tracking.DataBase.Core.MoneyCategory.Category;
import com.ntankard.Tracking.DataBase.Core.SupportObjects.Currency;
import com.ntankard.Tracking.DataBase.Core.MoneyCategory.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Database.SubContainers.*;

import java.util.*;

public class TrackingDatabase {

    // Core data objects
    private List<Container> containers = new ArrayList<>();
    private TypeMap typeMap = new TypeMap();
    private TypeIDMap typeIDMap = new TypeIDMap();
    private DefaultObjectMap defaultObjectMap = new DefaultObjectMap();
    private ClassMap classMap = new ClassMap();
    private SpecialValuesMap specialValuesMap = new SpecialValuesMap();

    // Special values
    private static Double taxRate = 0.06;

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
    TrackingDatabase() {
        containers.add(typeIDMap);
        containers.add(typeMap);
        containers.add(defaultObjectMap);
        containers.add(classMap);
        containers.add(specialValuesMap);

        containers.forEach(container -> container.addType(Currency.class));
        containers.forEach(container -> container.addType(Category.class));
        containers.forEach(container -> container.addType(Bank.class));
        containers.forEach(container -> container.addType(Period.class));
        containers.forEach(container -> container.addType(Statement.class));
        containers.forEach(container -> container.addType(Transaction.class));
        containers.forEach(container -> container.addType(CategoryTransfer.class));
        containers.forEach(container -> container.addType(PeriodTransfer.class));
        containers.forEach(container -> container.addType(Fund.class));
        containers.forEach(container -> container.addType(PeriodFundTransfer.class));
        containers.forEach(container -> container.addType(FundEvent.class));
        containers.forEach(container -> container.addType(FundChargeTransfer.class));
    }

    /**
     * Get the tax rate to use
     *
     * @return The tax rate to use
     */
    public Double getTaxRate() {
        return taxRate;
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
    public <T extends DataObject> String getNextId(Class<T> toGet) {
        int max = 0;

        if (!typeMap.containsKey(toGet)) {
            throw new RuntimeException("Impossible type");
        }

        for (DataObject t : typeMap.get(toGet)) {
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
        add(dataObject.getTypeClass(), dataObject);
    }

    /**
     * Add a new element to the database. New elements are repaired if needed and all relevant parents are notified
     *
     * @param tClass     The object to add
     * @param dataObject The type to store it under
     */
    public void add(Class tClass, DataObject dataObject) {
        fix(dataObject);
        containers.forEach(container -> container.add(tClass, dataObject));
        dataObject.notifyParentLink();
    }

    public void remove(DataObject dataObject) {
        remove(dataObject.getTypeClass(), dataObject);
    }

    /**
     * Remove an element from the database as long as it has no children linking to it. All relevant parents are notified
     *
     * @param dataObject The object to remove
     */
    public void remove(Class tClass, DataObject dataObject) {
        dataObject.notifyParentUnLink();
        containers.forEach(container -> container.remove(tClass, dataObject));
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
        }
    }

    /**
     * Ensure that a period has all the data it should
     *
     * @param period The period to fix
     */
    private void fixPeriod(Period period) {

        for (Bank b : get(Bank.class)) {
            boolean found = false;
            for (Statement statement : period.getChildren(Statement.class)) {
                if (statement.getBank().equals(b)) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                Double lastEnd = 0.0;
                for (Statement statement : period.getLast().getChildren(Statement.class)) {
                    if (statement.getBank().equals(b)) {
                        lastEnd = statement.getEnd();
                        break;
                    }
                }
                add(new Statement(TrackingDatabase.get().getNextId(Statement.class), b, period, lastEnd, 0.0, 0.0, 0.0));
            }
        }

        boolean hexFound = false;
        boolean saveFound = false;
        for (FundChargeTransfer fundChargeTransfer : period.getChildren(FundChargeTransfer.class)) {
            if (fundChargeTransfer instanceof TaxChargeTransfer) {
                hexFound = true;
            }
            if (fundChargeTransfer instanceof SavingsChargeTransfer) {
                saveFound = true;
            }
        }
        if (!hexFound) {
            add(FundChargeTransfer.class, new TaxChargeTransfer(period));
        }
        if (!saveFound) {
            add(FundChargeTransfer.class, new SavingsChargeTransfer(period));
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
        return (T) typeIDMap.get(type).get(id);
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
        return Collections.unmodifiableList((List<T>) typeMap.get(type));
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

    /**
     * Get the default object of a type (specified or the 0th element)
     *
     * @param aClass The class to get
     * @param <T>    The data type to return (same as aClass)
     * @return The default object
     */
    public <T extends DataObject> T getDefault(Class<T> aClass) {
        return defaultObjectMap.getDefault(aClass);
    }

    /**
     * Get the object that is the special value
     *
     * @param aClass The type of object to search
     * @param key    The key to search
     * @return The value of type aClass that is the special value for the key
     */
    public <T extends DataObject> T getSpecialValue(Class<T> aClass, Integer key) {
        return specialValuesMap.get(aClass, key);
    }

    /**
     * Get all the DataObject types added to this database
     *
     * @return All the DataObject types added to this database
     */
    public Set<Class> getDataObjectTypes() {
        return typeMap.keySet();
    }
}
