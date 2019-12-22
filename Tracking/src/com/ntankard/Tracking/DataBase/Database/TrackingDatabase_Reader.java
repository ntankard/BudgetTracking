package com.ntankard.Tracking.DataBase.Database;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.Util.FileUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

import static com.ntankard.ClassExtension.Util.classForName;

public class TrackingDatabase_Reader {

    /**
     * Read all files for the database from the latest save folder
     *
     * @param data     The data to load into
     * @param corePath The path that files are located in
     */
    public static void read(TrackingDatabase data, String corePath) {
        String csvFile = FileUtil.getLatestSaveDirectory(corePath);
        List<String> files = FileUtil.findFilesInDirectory(csvFile);

        Map<DataObjectSaver, List<String[]>> readLines = new HashMap<>();
        List<DataObjectSaver> pastDataObjectSavers = new ArrayList<>();

        // Find all saved files
        for (String file : files) {

            // Read the lines
            List<String[]> allLines = FileUtil.readLines(csvFile + file);
            if (allLines.size() < 2 || allLines.get(0).length != 1 || allLines.get(1).length % 2 != 0) {
                throw new RuntimeException("File is in the wrong format");
            }

            // Parse the object
            DataObjectSaver dataObjectSaver = extractConstructorMap(allLines);

            readLines.put(dataObjectSaver, allLines);
            pastDataObjectSavers.add(dataObjectSaver);
        }

        // Sort the objects so they load correctly
        sortByDependency(pastDataObjectSavers);

        // Load each object type
        for (DataObjectSaver pastDataObjectSaver : pastDataObjectSavers) {
            DataObjectSaver currentObjectMap = generateConstructorMap(pastDataObjectSaver.aClass);
            List<String[]> allLines = readLines.get(pastDataObjectSaver);

            // Load each object
            for (int i = 2; i < allLines.size(); i++) {
                if (currentObjectMap.nameTypePairs.size() != allLines.get(i).length)
                    throw new RuntimeException("Line parts dose not match the available params");

                DataObject toAdd = dataObjectFromString(allLines.get(i), currentObjectMap, pastDataObjectSaver, data);
                data.add(toAdd);
            }
        }
    }

    /**
     * Save the database to a new directory
     *
     * @param data     The data to save
     * @param corePath The directory to put the folder
     */
    public static void save(TrackingDatabase data, String corePath) {
        Map<Class, List<List<String>>> linesToSave = new HashMap<>();
        Map<Class, DataObjectSaver> dataObjectSavers = new HashMap<>();

        // Generate the headers
        for (Class aClass : data.getDataObjectTypes()) {
            if (Modifier.isAbstract(aClass.getModifiers())) {
                continue;
            }

            DataObjectSaver dataObjectSaver = generateConstructorMap(aClass);
            if (dataObjectSaver.shouldSave) {

                // Create entry
                dataObjectSavers.put(aClass, dataObjectSaver);
                linesToSave.put(aClass, new ArrayList<>());

                // Write the object type
                linesToSave.get(aClass).add(new ArrayList<>(Collections.singletonList(aClass.getName())));

                // Write the object parameters to the header of the file
                List<String> types = new ArrayList<>();
                for (NameTypePair nameTypePair : dataObjectSaver.nameTypePairs) {
                    types.add(nameTypePair.name);
                    types.add(nameTypePair.type.getName());
                }
                linesToSave.get(aClass).add(types);
            }
        }

        // Add each individual object
        for (DataObject dataObject : data.getAll()) {
            List<List<String>> lines = linesToSave.get(dataObject.getClass());
            DataObjectSaver dataObjectSaver = dataObjectSavers.get(dataObject.getClass());

            if (lines == null || dataObjectSaver == null) {
                DataObjectSaver generated = generateConstructorMap(dataObject.getClass());
                if (generated.shouldSave) {
                    throw new RuntimeException("Trying to save and object that is not setup for saving");
                } else {
                    continue;
                }
            }

            if (dataObjectSaver.shouldSave) {
                lines.add(dataObjectToString(dataObject, dataObjectSaver));
            }
        }

        // Write to file
        String csvFile = FileUtil.newSaveDirectory(corePath);
        for (Map.Entry<Class, List<List<String>>> entry : linesToSave.entrySet()) {
            if (dataObjectSavers.get(entry.getKey()).shouldSave) {
                FileUtil.writeLines(csvFile + entry.getKey().getSimpleName() + ".csv", entry.getValue());
            }
        }
    }

    /**
     * Sort a list of Data Objects based on other objects they depends on. Is objects are loaded in this order all dependencies can be guaranteed
     *
     * @param pastDataObjectSavers The list to sort
     */
    private static void sortByDependency(List<DataObjectSaver> pastDataObjectSavers) {
        Map<DataObjectSaver, List<Class>> dependencyMap = new HashMap<>();
        List<Class> allObjects = new ArrayList<>();

        // Generate the dependencies
        for (DataObjectSaver dataObjectSaver : pastDataObjectSavers) {
            List<Class> dependency = new ArrayList<>();
            for (NameTypePair nameTypePair : dataObjectSaver.nameTypePairs) {
                if (DataObject.class.isAssignableFrom(nameTypePair.type)) {
                    if (!nameTypePair.type.equals(dataObjectSaver.aClass)) {
                        dependency.add(nameTypePair.type);
                    }
                }
            }

            dependencyMap.put(dataObjectSaver, dependency);
            allObjects.add(dataObjectSaver.aClass);
        }

        // Check that all dependencies are possible
        for (DataObjectSaver dataObjectSaver : pastDataObjectSavers) {
            List<Class> toAdd = new ArrayList<>();
            for (Class<?> aClass : dependencyMap.get(dataObjectSaver)) {
                if (!allObjects.contains(aClass)) {
                    boolean found = false;
                    for (Class<?> toTest : allObjects) {
                        if (aClass.equals(toTest)) {
                            if (found) {
                                throw new RuntimeException("Found both an inherited dependency and a direct one. This means a class has inherited from a non abstract");
                            }
                            found = true;
                            break;
                        } else if (aClass.isAssignableFrom(toTest)) {
                            toAdd.add(toTest);
                            found = true;
                        }
                    }
                    if (!found) {
                        throw new RuntimeException("Dependency detected on an object that dose not exist");
                    }
                }
            }
            dependencyMap.get(dataObjectSaver).addAll(toAdd);
        }

        // Sort the list
        boolean sorted;
        int attempts = 0;
        do {

            // Infinite loop catch
            if (attempts++ > pastDataObjectSavers.size() * pastDataObjectSavers.size()) {
                throw new RuntimeException("Failed to sort dependencies, infinite loop detected");
            }

            sorted = true;
            for (int i = 0; i < pastDataObjectSavers.size(); i++) {
                DataObjectSaver dataObjectSaver = pastDataObjectSavers.get(i);
                List<Class> dependency = dependencyMap.get(dataObjectSaver);

                // Check that all dependencies are earlier in the list
                boolean allFound = true;
                for (Class<?> aClass : dependency) {

                    // Look for a dependency earlier in the list
                    boolean found = false;
                    for (int j = 0; j < i; j++) {
                        if (aClass.isAssignableFrom(pastDataObjectSavers.get(j).aClass)) {
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        allFound = false;
                        break;
                    }
                }

                // If one of more dependencies is not before the test object more it to the end of the list
                if (!allFound) {
                    pastDataObjectSavers.remove(dataObjectSaver);
                    pastDataObjectSavers.add(dataObjectSaver);
                    sorted = false;
                    break;
                }
            }
        } while (!sorted);
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################ Object to String ################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Create a DataObject from a list of constructor parts
     *
     * @param paramStrings     The string of constructor parts
     * @param trackingDatabase The database used to get reference objects
     * @return THe newly constructed (but not added) DataObject
     */
    @SuppressWarnings("unchecked")
    static DataObject dataObjectFromString(String[] paramStrings, DataObjectSaver currentObjectMap, DataObjectSaver pastObjectMap, TrackingDatabase trackingDatabase) {

        // Build up the parameters from the strings
        List<Object> params = new ArrayList<>();
        for (int i = 0; i < currentObjectMap.nameTypePairs.size(); i++) {
            NameTypePair current = currentObjectMap.nameTypePairs.get(i);

            // Find the past entry that matches the constructor parameter
            int order = 0;
            boolean found = false;
            for (NameTypePair savedMethodPair : pastObjectMap.nameTypePairs) {
                if (current.name.equals(savedMethodPair.name)) {
                    if (!current.type.equals(savedMethodPair.type))
                        throw new RuntimeException("Name matched, type did not");
                    found = true;
                    break;
                }
                order++;
            }

            // Parse the object type
            if (!found) {
                params.add(null);
                System.out.println("Warning, the object " + currentObjectMap.aClass.getSimpleName() + ". Has a new parameter that was not saved before. Setting to null");
            } else {
                String paramString = paramStrings[order];
                Class paramType = currentObjectMap.nameTypePairs.get(i).type;

                if (DataObject.class.isAssignableFrom(paramType)) {
                    if (paramString.equals(" ")) {
                        params.add(null);
                    } else {
                        DataObject dataObject = trackingDatabase.get(paramType, Integer.parseInt(paramString));
                        if (dataObject == null && !Period.class.isAssignableFrom(paramType)) { // TODO fix period so this isn't required
                            throw new RuntimeException("Trying to load an object that is not yet in the database");
                        }
                        params.add(dataObject);
                    }
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
        }

        // Construct the object
        try {
            return (DataObject) currentObjectMap.constructor.newInstance(params.toArray());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create a list of constructor parameters from a DataObject
     *
     * @param dataObject      the DataObject to convert
     * @param dataObjectSaver The description of the object to save
     * @return The list of parameters that can be used to recreate this object
     */
    static List<String> dataObjectToString(DataObject dataObject, DataObjectSaver dataObjectSaver) {
        List<String> paramStrings = new ArrayList<>();

        for (NameTypePair nameTypePair : dataObjectSaver.nameTypePairs) {

            // Find the method
            Method getter;
            try {
                getter = dataObjectSaver.aClass.getMethod(nameTypePair.name);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("\n" + "Class: " + dataObjectSaver.aClass.getSimpleName() + " Method:" + nameTypePair.name + "\n" + e);
            }
            if (!getter.getReturnType().equals(nameTypePair.type))
                throw new RuntimeException("Class:" + dataObject.getClass().getSimpleName() + " Method:" + nameTypePair.name + " Getter provided by ParameterMap dose not match the parameter in the constructor. Could save but would not be able to load. Aborting save");

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
                paramStrings.add(((DataObject) getterValue).getId().toString());
            } else {
                paramStrings.add(getterValue.toString());
            }
        }

        return paramStrings;
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################ Class extraction ################################################
    //------------------------------------------------------------------------------------------------------------------

    private static DataObjectSaver extractConstructorMap(List<String[]> allLines) {
        Class aClass = classForName(allLines.get(0)[0], "com.ntankard.Tracking.DataBase.Core");
        return generateConstructorMap(aClass, allLines.get(1));
    }

    /**
     * Create a ConstructorMap object for its save line
     *
     * @param aClass The class to generate
     * @param lines  The lines to generate from
     * @return A ConstructorMap
     */
    private static DataObjectSaver generateConstructorMap(Class aClass, String[] lines) {
        DataObjectSaver dataObjectSaver = new DataObjectSaver(aClass);

        // Find the constructor
        Constructor[] constructors = aClass.getConstructors();
        if (constructors.length != 1) throw new RuntimeException("More than one constructor detected");
        dataObjectSaver.constructor = constructors[0];

        for (int i = 0; i < lines.length / 2; i++) {
            dataObjectSaver.nameTypePairs.add(new NameTypePair(lines[i * 2], classForName(lines[i * 2 + 1], "com.ntankard.Tracking.DataBase.Core")));
        }

        return dataObjectSaver;
    }

    /**
     * Create a ConstructorMap object for a specific class
     *
     * @param aClass The class to generate from
     * @return its ConstructorMap or null if its not supported
     */
    public static DataObjectSaver generateConstructorMap(Class aClass) {
        // Build the base object
        if (aClass.isPrimitive()) throw new RuntimeException("Primate object type detected");
        DataObjectSaver dataObjectSaver = new DataObjectSaver(aClass);

        // Find the constructor
        Constructor[] constructors = aClass.getConstructors();
        if (constructors.length != 1) throw new RuntimeException("More than one constructor detected");
        dataObjectSaver.constructor = constructors[0];

        // Find the save settings
        ParameterMap parameterMap = (ParameterMap) constructors[0].getAnnotation(ParameterMap.class);
        if (parameterMap == null)
            throw new RuntimeException("This class dose not support saving");

        // Should this object be ignored?
        String[] paramGetters = parameterMap.parameterGetters();
        if (!parameterMap.shouldSave()) {
            dataObjectSaver.shouldSave = false;
            return dataObjectSaver;
        }

        // Find the getters that map to the each constructor parameter
        Class<?>[] paramTypes = constructors[0].getParameterTypes();
        if (paramGetters.length != paramTypes.length)
            throw new RuntimeException("dataObjects ParameterMap annotation dose not match is constructor. Could save but would not be able to load. Aborting save");

        // Build the rest of the object
        for (int i = 0; i < paramGetters.length; i++) {
            dataObjectSaver.nameTypePairs.add(new NameTypePair(paramGetters[i], paramTypes[i]));
        }
        return dataObjectSaver;
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################ Class containers ################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Description of the object needed to construct an object
     */
    public static class DataObjectSaver {
        public Class<?> aClass;
        public List<NameTypePair> nameTypePairs = new ArrayList<>();
        public Boolean shouldSave = true;
        public Constructor constructor;

        DataObjectSaver(Class aClass) {
            this.aClass = aClass;
        }

        @Override
        public String toString() {
            return aClass.toString();
        }
    }

    /**
     * Each field (getter) and its type
     */
    private static class NameTypePair {
        Class type;
        String name;

        NameTypePair(String name, Class type) {
            this.type = type;
            this.name = name;
        }

        @Override
        public String toString() {
            return name + " " + type.toString();
        }
    }
}
