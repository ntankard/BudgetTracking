package com.ntankard.budgetTracking;

import com.ntankard.budgetTracking.dataBase.core.pool.fundEvent.SavingsFundEvent;
import com.ntankard.budgetTracking.dataBase.core.recurringPayment.FixedRecurringPayment;
import com.ntankard.budgetTracking.display.frames.mainFrame.Master_Frame;
import com.ntankard.budgetTracking.processing.BudgetTracking;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.javaObjectDatabase.database.Database_Schema;
import com.ntankard.javaObjectDatabase.database.io.Database_IO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static String databasePath = "com.ntankard.budgetTracking.dataBase";
    public static String savePath = "C:\\Users\\Nicholas\\Google Drive\\BudgetTrackingData";

    public static Map<Class<? extends DataObject>, List<Class<? extends DataObject>>> getForcedDependencies() {
        Map<Class<? extends DataObject>, List<Class<? extends DataObject>>> forcedDependencies = new HashMap<>();
        forcedDependencies.put(FixedRecurringPayment.class, new ArrayList<>());
        forcedDependencies.get(FixedRecurringPayment.class).add(SavingsFundEvent.class);
        return forcedDependencies;
    }

    public static Database createDataBase() {
        return createDataBase(getForcedDependencies());
    }

    public static Database createDataBase(Map<Class<? extends DataObject>, List<Class<? extends DataObject>>> forcedDependencies) {
        Map<String, String> nameMap = new HashMap<>();
        return Database_IO.read(Database_Schema.getSchemaFromPackage(databasePath, forcedDependencies), databasePath, savePath, nameMap);
    }

    public static Database createDataBase(List<Class<? extends DataObject>> solidClasses) {
        Map<String, String> nameMap = new HashMap<>();
        return Database_IO.read(Database_Schema.getSchemaFromPackage(databasePath, solidClasses, getForcedDependencies()), databasePath, savePath, nameMap);
    }

    public static void main(String[] args) {
        ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(true); // TODO remove
        Database database = createDataBase(getForcedDependencies());
        BudgetTracking.process(database);
        Master_Frame.open(database, savePath);
    }
}
