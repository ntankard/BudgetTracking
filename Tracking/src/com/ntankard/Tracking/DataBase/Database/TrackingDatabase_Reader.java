package com.ntankard.Tracking.DataBase.Database;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Fund;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Statement;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.CategoryTransfer;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.PeriodFundTransfer;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.PeriodTransfer;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.Transaction;
import com.ntankard.Tracking.DataBase.Core.SupportObjects.Bank;
import com.ntankard.Tracking.DataBase.Core.MoneyCategory.Category;
import com.ntankard.Tracking.DataBase.Core.SupportObjects.Currency;
import com.ntankard.Tracking.DataBase.Core.MoneyCategory.FundEvent;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TrackingDatabase_Reader {

    /**
     * Save the database to a new directory
     *
     * @param data     The data to save
     * @param corePath The directory to put the folder
     */
    public static void save(TrackingDatabase data, String corePath) {
        String csvFile = newSaveDirectory(corePath);

        // Save the data
        saveDataObjectSet(csvFile, Currency.class, data);
        saveDataObjectSet(csvFile, Category.class, data);
        saveDataObjectSet(csvFile, Bank.class, data);
        saveDataObjectSet(csvFile, Period.class, data);
        saveDataObjectSet(csvFile, Statement.class, data);
        saveDataObjectSet(csvFile, Transaction.class, data);
        saveDataObjectSet(csvFile, CategoryTransfer.class, data);
        saveDataObjectSet(csvFile, PeriodTransfer.class, data);
        saveDataObjectSet(csvFile, Fund.class, data);
        saveDataObjectSet(csvFile, FundEvent.class, data);
        saveDataObjectSet(csvFile, PeriodFundTransfer.class, data);
        //saveDataObjectSet(csvFile, FundChargeTransfer.class);
    }

    /**
     * Read all files for the database from the latest save folder
     *
     * @param data     The data to load into
     * @param corePath The path that files are located in
     */
    public static void read(TrackingDatabase data, String corePath) {
        String csvFile = getLatestSaveDirectory(corePath);

        // Read the data
        readDataObjectSet(csvFile, Currency.class, data);
        readDataObjectSet(csvFile, Category.class, data);
        readDataObjectSet(csvFile, Fund.class, data);
        readDataObjectSet(csvFile, Period.class, data);
        readDataObjectSet(csvFile, Bank.class, data);
        readDataObjectSet(csvFile, Statement.class, data);
        readDataObjectSet(csvFile, FundEvent.class, data);
        readDataObjectSet(csvFile, CategoryTransfer.class, data);
        readDataObjectSet(csvFile, PeriodTransfer.class, data);
        readDataObjectSet(csvFile, Transaction.class, data);
        readDataObjectSet(csvFile, PeriodFundTransfer.class, data);
        //readDataObjectSet(csvFile, FundChargeTransfer.class, data);
    }


    //------------------------------------------------------------------------------------------------------------------
    //################################################## Save ##########################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Save all objects of certain type from the database
     *
     * @param saveDir          The location to save the file
     * @param aClass           The object type to save
     * @param trackingDatabase The database save the files to
     * @param <T>              Type, same as aClass
     */
    private static <T extends DataObject> void saveDataObjectSet(String saveDir, Class<T> aClass, TrackingDatabase trackingDatabase) {
        ArrayList<List<String>> lines = new ArrayList<>();
        for (DataObject t : trackingDatabase.get(aClass)) {
            lines.add(dataObjectToString(t));
        }

        writeLines(saveDir + aClass.getSimpleName() + ".csv", lines);
    }

    /**
     * Create a list of constructor parameters from a DataObject
     *
     * @param dataObject the DataObject to convert
     * @return The list of parameters that can be used to recreate this object
     */
    static List<String> dataObjectToString(DataObject dataObject) {

        // Find the constructors parameters
        Class aClass = dataObject.getTypeClass();
        Constructor[] constructors = aClass.getConstructors();
        if (constructors.length != 1) {
            throw new RuntimeException("More than one constructor detected");
        }
        Class<?>[] paramTypes = constructors[0].getParameterTypes();

        // Find the getters that map to the each constructor parameter
        ParameterMap parameterMap = (ParameterMap) constructors[0].getAnnotation(ParameterMap.class);
        if (parameterMap == null) {
            throw new RuntimeException("dataObject dose not have ParameterMap on its constructor");
        }
        String[] paramGetters = parameterMap.parameterGetters();
        if (paramGetters.length != paramTypes.length) {
            throw new RuntimeException("dataObjects ParameterMap annotation dose not match is constructor. Could save but would not be able to load. Aborting save");
        }

        List<String> paramStrings = new ArrayList<>();
        for (int i = 0; i < paramGetters.length; i++) {
            String paramGetter = paramGetters[i];
            Class paramType = paramTypes[i];

            // Find the method
            Method getter;
            try {
                getter = dataObject.getTypeClass().getMethod(paramGetter);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            if (!getter.getReturnType().equals(paramType)) {
                throw new RuntimeException("Getter provided by ParameterMap dose not match the parameter in the constructor. Could save but would not be able to load. Aborting save");
            }

            // Execute the getter
            Object getterValue;
            try {
                getterValue = getter.invoke(dataObject);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }

            // Convert to String
            if (getterValue == null) {
                paramStrings.add(" ");
            } else if (getterValue instanceof DataObject) {
                paramStrings.add(((DataObject) getterValue).getId());
            } else {
                paramStrings.add(getterValue.toString());
            }
        }

        return paramStrings;
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################## Read ##########################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Read all objects of a certain type into the database
     *
     * @param saveDir          The directory where the same file can be found
     * @param aClass           The object type to read
     * @param trackingDatabase The database save the files to
     */
    private static void readDataObjectSet(String saveDir, Class<?> aClass, TrackingDatabase trackingDatabase) {
        ArrayList<String[]> allLines = readLines(saveDir + aClass.getSimpleName() + ".csv");

        for (String[] lines : allLines) {
            DataObject built = dataObjectFromString(lines, aClass, trackingDatabase);
            trackingDatabase.add(built);
        }
    }

    /**
     * Create a DataObject from a list of constructor parts
     *
     * @param paramStrings     The string of constructor parts
     * @param target           The type of object to create
     * @param trackingDatabase The database used to get reference objects
     * @return THe newly constructed (but not added) DataObject
     */
    @SuppressWarnings("unchecked")
    static DataObject dataObjectFromString(String[] paramStrings, Class<?> target, TrackingDatabase trackingDatabase) {

        // Find the constructors parameters
        Constructor[] constructors = target.getConstructors();
        if (constructors.length != 1) {
            throw new RuntimeException("More than one constructor detected");
        }
        Class<?>[] paramTypes = constructors[0].getParameterTypes();
        if (paramTypes.length != paramStrings.length) {
            throw new RuntimeException("Line parts dose not match the available params");
        }

        // Build up the parameters from the strings
        List<Object> params = new ArrayList<>();
        for (int i = 0; i < paramTypes.length; i++) {
            String paramString = paramStrings[i];
            Class paramType = paramTypes[i];

            if (DataObject.class.isAssignableFrom(paramType)) {
                params.add(trackingDatabase.get(paramType, paramString));
            } else if (String.class.isAssignableFrom(paramType)) {
                params.add(paramString);
            } else if (Boolean.class.isAssignableFrom(paramType) || boolean.class.isAssignableFrom(paramType)) {
                params.add(Boolean.parseBoolean(paramString));
            } else if (Double.class.isAssignableFrom(paramType) || double.class.isAssignableFrom(paramType)) {
                params.add(Double.parseDouble(paramString));
            } else if (Integer.class.isAssignableFrom(paramType) || int.class.isAssignableFrom(paramType)) {
                params.add(Integer.parseInt(paramString));
            } else {
                throw new RuntimeException("Unknown data type");
            }
        }

        // Construct the object
        try {
            return (DataObject) constructors[0].newInstance(params.toArray());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################## Util ##########################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Read lines from a csv file
     *
     * @param csvFile The path to the file to read
     * @return ALl lines read from the file
     */
    private static ArrayList<String[]> readLines(String csvFile) {
        BufferedReader br = null;
        String line;
        String cvsSplitBy = ",";
        ArrayList<String[]> allLines = new ArrayList<>();

        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                String[] lines = line.split(cvsSplitBy);
                allLines.add(lines);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return allLines;
    }

    /**
     * Write lines to a csv file
     *
     * @param path  The path to write the files to
     * @param lines The lines to write
     */
    private static void writeLines(String path, List<List<String>> lines) {
        try {
            FileWriter fw = new FileWriter(path);
            for (List<String> line : lines) {
                for (String s : line) {
                    fw.write(s);
                    fw.write(",");
                }
                fw.write('\n');
            }
            fw.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Find the latest save directory
     *
     * @param corePath The core directory
     * @return The path of the latest save directory
     */
    private static String getLatestSaveDirectory(String corePath) {
        int max = 0;
        List<String> folders = findFoldersInDirectory(corePath);
        for (String s : folders) {
            int value = Integer.parseInt(s);
            if (value > max) {
                max = value;
            }
        }
        return corePath + "\\" + max + "\\";
    }

    /**
     * Create an empty save directory in the core directory
     *
     * @param corePath The core directory
     * @return The new save path
     */
    private static String newSaveDirectory(String corePath) {
        // Find the next save dir
        int max = 0;
        List<String> folders = findFoldersInDirectory(corePath);
        for (String s : folders) {
            int value = Integer.parseInt(s);
            if (value > max) {
                max = value;
            }
        }
        String csvFile = corePath + "\\" + (max + 1) + "\\";

        // Make the folder
        new File(csvFile).mkdir();

        return csvFile;
    }

    /**
     * Find the folders in a directory
     *
     * @param directoryPath The path to search
     * @return A list of folders in the directory
     */
    private static List<String> findFoldersInDirectory(String directoryPath) {
        File directory = new File(directoryPath);

        FileFilter directoryFileFilter = File::isDirectory;

        File[] directoryListAsFile = directory.listFiles(directoryFileFilter);
        assert directoryListAsFile != null;
        List<String> foldersInDirectory = new ArrayList<>(directoryListAsFile.length);
        for (File directoryAsFile : directoryListAsFile) {
            foldersInDirectory.add(directoryAsFile.getName());
        }

        return foldersInDirectory;
    }
}
