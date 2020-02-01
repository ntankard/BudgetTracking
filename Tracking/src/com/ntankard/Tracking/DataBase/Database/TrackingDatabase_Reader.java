package com.ntankard.Tracking.DataBase.Database;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.Receipt;
import com.ntankard.Tracking.Util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.util.*;

import static com.ntankard.ClassExtension.Util.classForName;

public class TrackingDatabase_Reader {

    // Root paths
    static String ROOT_DATA_PATH = "\\Data";
    static String ROOT_NEW_IMAGE_PATH = "\\NewImage\\";
    static String ROOT_FILE_PATH = "\\BudgetTracking.txt";

    // Save instance paths
    static String INSTANCE_CLASSES_PATH = "\\Classes\\";
    static String INSTANCE_IMAGE_PATH = "\\Images\\";

    /**
     * Read all files for the database from the latest save folder
     *
     * @param data     The data to load into
     * @param corePath The path that files are located in
     */
    @SuppressWarnings("unchecked")
    public static void read(TrackingDatabase data, String corePath) {
        if (!checkSavePath(corePath)) {
            throw new RuntimeException("Save path is invalid");
        }

        String savePath = FileUtil.getLatestSaveDirectory(corePath + ROOT_DATA_PATH);
        List<String> files = FileUtil.findFilesInDirectory(savePath + INSTANCE_CLASSES_PATH);

        List<Class<? extends DataObject>> savedClasses = new ArrayList<>();
        Map<Class<? extends DataObject>, List<String[]>> savedLines = new HashMap<>();
        Map<Class<? extends DataObject>, List<ConstructorParameter>> savedConstructorParameters = new HashMap<>();

        // Find all saved files, load the object types but not the data
        for (String file : files) {

            // Read the lines
            List<String[]> allLines = FileUtil.readLines(savePath + INSTANCE_CLASSES_PATH + file);

            // Check that the file has the class type and the parameter map in its header
            if (allLines.size() < 2 || allLines.get(0).length != 1 || allLines.get(1).length % 2 != 0) {
                throw new RuntimeException("File is in the wrong format");
            }

            // Parse the object
            Class<? extends DataObject> fileClass = (Class<? extends DataObject>) classForName(allLines.get(0)[0], "com.ntankard.Tracking.DataBase.Core");

            // Save the meta data
            savedClasses.add(fileClass);
            savedLines.put(fileClass, allLines);
            savedConstructorParameters.put(fileClass, getConstructorParameters(allLines.get(1)));
        }

        // Sort the objects so they are read correctly
        List<Class<? extends DataObject>> readOrder = sortByDependency(getConstructorDependencies(savedClasses));

        // Read each object type
        Map<Class, List<DataObject>> readObjects = new HashMap<>();
        Map<Integer, DataObject> objectIDMap = new HashMap<>();
        for (Class<? extends DataObject> toRead : readOrder) {
            List<ConstructorParameter> pastConstructorParameters = savedConstructorParameters.get(toRead);
            List<ConstructorParameter> currentConstructorParameters = getConstructorParameters(toRead);
            List<String[]> toReadSavedLines = savedLines.get(toRead);

            // Load each object
            List<DataObject> loadedObjects = new ArrayList<>();
            for (int i = 2; i < toReadSavedLines.size(); i++) {
                if (currentConstructorParameters.size() != toReadSavedLines.get(i).length)
                    throw new RuntimeException("Line parts dose not match the available params");

                // Generate
                DataObject toAdd = dataObjectFromString(toRead, toReadSavedLines.get(i), currentConstructorParameters, pastConstructorParameters, objectIDMap);

                // Store
                loadedObjects.add(toAdd);
                objectIDMap.put(toAdd.getId(), toAdd);
            }
            readObjects.put(toRead, loadedObjects);
        }

        // Sort the objects so they can be added correctly
        List<Class<? extends DataObject>> loadOrder = sortByDependency(generateMangedDependencies(savedClasses));

        // Load into the database
        for (Class toLoad : loadOrder) {
            if (readObjects.containsKey(toLoad)) {
                for (DataObject toAdd : readObjects.get(toLoad)) {
                    toAdd.add();
                }
            }
        }

        // Load the images and paths into the database
        TrackingDatabase.get().setNewImagePath(corePath + ROOT_NEW_IMAGE_PATH);
        TrackingDatabase.get().setSavedImagePath(savePath + INSTANCE_IMAGE_PATH);
        TrackingDatabase.get().setPossibleImages(FileUtil.findFilesInDirectory(corePath + ROOT_NEW_IMAGE_PATH));
    }

    /**
     * Save the database to a new directory
     *
     * @param data     The data to save
     * @param corePath The directory to put the folder
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void save(TrackingDatabase data, String corePath) {
        if (!checkSavePath(corePath)) {
            throw new RuntimeException("Save path is invalid");
        }

        Map<Class<? extends DataObject>, List<List<String>>> classLinesToSave = new HashMap<>();
        Map<Class<? extends DataObject>, List<ConstructorParameter>> classConstructorParameters = new HashMap<>();

        // Generate the headers
        for (Class<? extends DataObject> aClass : data.getDataObjectTypes()) {

            // Only save solid objects
            if (Modifier.isAbstract(aClass.getModifiers())) {
                continue;
            }

            // Should we save?
            if (getParameterMap(aClass).shouldSave()) {

                // Create entry
                classLinesToSave.put(aClass, new ArrayList<>());

                // Write the object type
                classLinesToSave.get(aClass).add(new ArrayList<>(Collections.singletonList(aClass.getName())));

                // Write the object parameters to the header of the file
                classConstructorParameters.put(aClass, getConstructorParameters(aClass));
                List<String> types = new ArrayList<>();
                for (ConstructorParameter constructorParameter : classConstructorParameters.get(aClass)) {
                    types.add(constructorParameter.name);
                    types.add(constructorParameter.type.getName());
                }
                classLinesToSave.get(aClass).add(types);
            }
        }

        // Add each individual object
        for (DataObject dataObject : data.getAll()) {
            List<List<String>> lines = classLinesToSave.get(dataObject.getClass());
            List<ConstructorParameter> constructorParameters = classConstructorParameters.get(dataObject.getClass());

            if (lines == null || constructorParameters == null) {
                if (getParameterMap(dataObject.getClass()).shouldSave()) {
                    throw new RuntimeException("Trying to save and object that is not setup for saving");
                } else {
                    continue;
                }
            }

            lines.add(dataObjectToString(dataObject, constructorParameters));
        }

        // Write to file
        String saveDir = FileUtil.newSaveDirectory(corePath + ROOT_DATA_PATH);
        new File(saveDir + INSTANCE_CLASSES_PATH).mkdir();
        new File(saveDir + INSTANCE_IMAGE_PATH).mkdir();

        // Save the classes
        for (Map.Entry<Class<? extends DataObject>, List<List<String>>> entry : classLinesToSave.entrySet()) {
            if (getParameterMap(entry.getKey()).shouldSave()) {
                FileUtil.writeLines(saveDir + INSTANCE_CLASSES_PATH + entry.getKey().getSimpleName() + ".csv", entry.getValue());
            }
        }

        // Save the images
        for (Receipt receipt : TrackingDatabase.get().get(Receipt.class)) {
            if (receipt.isFirstFile()) {
                String source = TrackingDatabase.get().getNewImagePath() + receipt.getFileName();
                String destination = saveDir + INSTANCE_IMAGE_PATH + receipt.getFileName();
                File file = new File(source);
                file.renameTo(new File(destination));
            } else {
                String source = TrackingDatabase.get().getSavedImagePath() + receipt.getFileName();
                String destination = saveDir + INSTANCE_IMAGE_PATH + receipt.getFileName();
                try {
                    Files.copy(new File(source).toPath(), new File(destination).toPath());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * Check that the save path is in the expected format
     *
     * @param path The path to check
     * @return True if the path is valid
     */
    private static boolean checkSavePath(String path) {
        File coreDir = new File(path);
        if (!coreDir.exists()) {
            return false;
        }

        File dataDir = new File(path + ROOT_DATA_PATH);
        if (!dataDir.exists()) {
            return false;
        }

        File newImageDir = new File(path + ROOT_NEW_IMAGE_PATH);
        if (!newImageDir.exists()) {
            return false;
        }

        File filePath = new File(path + ROOT_FILE_PATH);
        if (!filePath.exists()) {
            return false;
        }

        return true;
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################## Dependency management #############################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get a class order that ensures all dependencies come before a class
     *
     * @param dependencyMap The list of dependencies
     * @return The order that will ensure all dependencies come first
     */
    private static List<Class<? extends DataObject>> sortByDependency(Map<Class<? extends DataObject>, List<Class<? extends DataObject>>> dependencyMap) {
        List<Class<? extends DataObject>> toReturn = new ArrayList<>(dependencyMap.keySet());

        // Sort the list
        boolean allSorted;
        int attempts = 0;
        do {

            // Infinite loop catch
            if (attempts++ > toReturn.size() * toReturn.size()) {
                throw new RuntimeException("Failed to sort dependencies, infinite loop detected");
            }

            allSorted = true;
            for (int i = 0; i < toReturn.size(); i++) {

                Class<? extends DataObject> toTest = toReturn.get(i);
                List<Class<? extends DataObject>> toTestDependencies = dependencyMap.get(toTest);

                // Check that all dependencies are earlier in the list
                boolean allFound = true;
                for (Class<?> dependency : toTestDependencies) {

                    // Look for a dependency earlier in the list
                    boolean found = false;
                    for (int j = 0; j < i; j++) {
                        if (dependency.isAssignableFrom(toReturn.get(j))) {
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        allFound = false;
                        break;
                    }
                }

                // If one of more dependencies is not before the test object, move it to the end of the list
                if (!allFound) {
                    toReturn.remove(toTest);
                    toReturn.add(toTest);
                    allSorted = false;
                    break;
                }
            }
        } while (!allSorted);

        return toReturn;
    }

    /**
     * Find all dependencies to load all objects
     *
     * @param loadingObjects The objects to load
     * @return The dependency map
     */
    @SuppressWarnings("unchecked")
    private static Map<Class<? extends DataObject>, List<Class<? extends DataObject>>> generateMangedDependencies(List<Class<? extends DataObject>> loadingObjects) {

        // Find all the objects that manage other objects, add the objects they make to there own dependency
        Map<Class<? extends DataObject>, List<Class<? extends DataObject>>> managedObjectDependencies = new HashMap<>();
        for (Class<? extends DataObject> loadedObject : loadingObjects) {
            ObjectFactory objectFactory = loadedObject.getAnnotation(ObjectFactory.class);
            if (objectFactory != null) {
                managedObjectDependencies.put(loadedObject, new ArrayList<>(Arrays.asList(objectFactory.builtObjects())));
            }
        }

        // For each managed object, add dependencies for its constructors
        Map<Class<? extends DataObject>, List<Class<? extends DataObject>>> managedObjectConstructorDependencies = new HashMap<>();
        for (Class<? extends DataObject> objectManager : managedObjectDependencies.keySet()) {                          // For each ObjectManager (Class with ObjectFactory attached)
            for (Class<? extends DataObject> managedObject : managedObjectDependencies.get(objectManager)) {            // For each ObjectManagers, ManagedObjects
                if (!managedObjectConstructorDependencies.containsKey(managedObject)) {                                 // If not seen before

                    // Get the non conflicting dependencies for the constructor
                    List<Class<? extends DataObject>> constructorParameters = new ArrayList<>();
                    for (Class<?> constructorParameterType : getConstructor(managedObject).getParameterTypes()) {       // For each ObjectManagers, ManagedObjects, Constructor parameters
                        if (DataObject.class.isAssignableFrom(constructorParameterType)) {                              // If its a DataObject

                            // Check that the constructor parameter cant be used to build the managed object
                            ObjectFactory constructorParameterTypeFactory = constructorParameterType.getAnnotation(ObjectFactory.class);
                            boolean found = false;
                            if (constructorParameterTypeFactory != null) {                                              // If the constructor parameter manages objects
                                for (Class<?> toTest : constructorParameterTypeFactory.builtObjects()) {
                                    if (toTest.isAssignableFrom(managedObject)) {                                       // Check that is dose not manage this object
                                        found = true;
                                        break;
                                    }
                                }
                            }
                            if (!found && !constructorParameters.contains(constructorParameterType)) {                  // If it dose not manage objects, or if it dost no managed this Managed object
                                constructorParameters.add((Class<? extends DataObject>) constructorParameterType);      // Add to the list of dependencies
                            }
                        }
                    }

                    // Add them all
                    managedObjectConstructorDependencies.put(managedObject, constructorParameters);
                }
            }
        }

        // Create one master dependency map
        Map<Class<? extends DataObject>, List<Class<? extends DataObject>>> dependencyMap = new HashMap<>();

        // Add all the ObjectManagers dependencies on the object they manage
        for (Class<? extends DataObject> objectManager : managedObjectDependencies.keySet())
            if (!dependencyMap.containsKey(objectManager))
                dependencyMap.put(objectManager, managedObjectDependencies.get(objectManager));

        // Add all the ManagedObjects dependencies on there contractors
        for (Class<? extends DataObject> managedObject : managedObjectConstructorDependencies.keySet())
            if (!dependencyMap.containsKey(managedObject))
                dependencyMap.put(managedObject, managedObjectConstructorDependencies.get(managedObject));

        // Add all missing objects
        for (Class<? extends DataObject> loadingObject : loadingObjects)
            if (!dependencyMap.containsKey(loadingObject))
                dependencyMap.put(loadingObject, new ArrayList<>());

        return dependencyMap;
    }

    /**
     * Find all dependencies needed to build all object
     *
     * @param loadingObjects The objects to load
     * @return A list of all objects that needed to be loaded to load for each class
     */
    private static Map<Class<? extends DataObject>, List<Class<? extends DataObject>>> getConstructorDependencies(List<Class<? extends DataObject>> loadingObjects) {
        Map<Class<? extends DataObject>, List<Class<? extends DataObject>>> dependencyMap = new HashMap<>();
        for (Class<? extends DataObject> dataObjectClass : loadingObjects) {
            dependencyMap.put(dataObjectClass, getConstructorDependencies(dataObjectClass, loadingObjects));
        }
        return dependencyMap;
    }

    /**
     * Find all dependencies needed to build an object
     *
     * @param dataObjectClass The object to inspect
     * @param loadingObjects  All other know objects (used to find inherited dependencies)
     * @return A list of all objects that needed to be loaded to load this class
     */
    @SuppressWarnings("unchecked")
    private static List<Class<? extends DataObject>> getConstructorDependencies(Class<? extends DataObject> dataObjectClass, List<Class<? extends DataObject>> loadingObjects) {
        List<Class<? extends DataObject>> dependencies = new ArrayList<>();
        for (ConstructorParameter constructorParameter : getConstructorParameters(dataObjectClass)) {

            // Look for the direct dependency
            if (DataObject.class.isAssignableFrom(constructorParameter.type)) {
                Class<? extends DataObject> primeDependencies = constructorParameter.type;
                dependencies.add(primeDependencies);

                // Is this dependency a solid object?
                if (!loadingObjects.contains(primeDependencies)) {
                    boolean found = false;

                    // Find an object down the tree that we can read
                    for (Class<? extends DataObject> toTest : loadingObjects) {
                        if (primeDependencies.isAssignableFrom(toTest)) {
                            dependencies.add(toTest);
                            found = true;
                        }
                    }
                    if (!found) {
                        throw new RuntimeException("Dependency detected on an object that dose not exist");
                    }
                }
            }
        }
        return dependencies;
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################ Object to String ################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Create an object based on a string of its parameters
     *
     * @param aClass                       The type of class to build
     * @param paramStrings                 The values as a string
     * @param currentConstructorParameters The current state of the objects constructor
     * @param savedConstructorParameters   The state of the constructor when the object was saved
     * @param loadedObjects                All past loaded objects
     * @return The newly constructed object
     */
    static DataObject dataObjectFromString(Class<? extends DataObject> aClass, String[] paramStrings, List<ConstructorParameter> currentConstructorParameters, List<ConstructorParameter> savedConstructorParameters, Map<Integer, DataObject> loadedObjects) {

        // Find out how the saved field map the the current objects construction parameters
        List<Integer> parameterMapping = getParameterMapping(aClass, currentConstructorParameters, savedConstructorParameters);

        // Build up the parameters from the strings
        List<Object> params = new ArrayList<>();
        for (int i = 0; i < currentConstructorParameters.size(); i++) {

            int parameterMap = parameterMapping.get(i);
            if (parameterMap == -1) {
                params.add(null);
                System.out.println("Warning, the object " + aClass.getSimpleName() + ". Has a new parameter that was not saved before. Setting to null");
            } else {
                String paramString = paramStrings[parameterMap];
                Class paramType = currentConstructorParameters.get(i).type;

                if (DataObject.class.isAssignableFrom(paramType)) {
                    if (paramString.equals(" ")) {
                        params.add(null);
                    } else {
                        DataObject dataObject = loadedObjects.get(Integer.parseInt(paramString));
                        if (dataObject == null) {
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
            return (DataObject) getConstructor(aClass).newInstance(params.toArray());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create a string of all teh values needed to rebuild the object
     *
     * @param dataObject            the DataObject to convert
     * @param constructorParameters the parameters to save for loading in the future
     * @return All objects needed to construct the object as a string
     */
    static List<String> dataObjectToString(DataObject dataObject, List<ConstructorParameter> constructorParameters) {
        List<String> paramStrings = new ArrayList<>();
        for (ConstructorParameter constructorParameter : constructorParameters) {

            // Find the method
            Method getter;
            try {
                getter = dataObject.getClass().getMethod(constructorParameter.name);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("\n" + "Class: " + dataObject.getClass().getSimpleName() + " Method:" + constructorParameter.name + "\n" + e);
            }
            if (!getter.getReturnType().equals(constructorParameter.type))
                throw new RuntimeException("Class:" + dataObject.getClass().getSimpleName() + " Method:" + constructorParameter.name + " Getter provided by ParameterMap dose not match the parameter in the constructor. Could save but would not be able to load. Aborting save");

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
    //############################################### Constructor access ###############################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Find out how the old constructor parameters map top the new constructor parameters
     *
     * @param aClass                       The Object to map
     * @param currentConstructorParameters The current parameters
     * @param savedConstructorParameters   The past parameters
     * @return A orders lists of the mapping
     */
    private static List<Integer> getParameterMapping(Class<? extends DataObject> aClass, List<ConstructorParameter> currentConstructorParameters, List<ConstructorParameter> savedConstructorParameters) {
        List<Integer> mappedOrder = new ArrayList<>();

        // Check for duplicates parameters in current object
        for (int i = 0; i < currentConstructorParameters.size(); i++)
            for (int j = 0; j < currentConstructorParameters.size(); j++)
                if (i != j)
                    if (currentConstructorParameters.get(i).name.equals(currentConstructorParameters.get(j).name))
                        throw new RuntimeException("Error, the object " + aClass.getSimpleName() + ". Has a duplicate parameter name");

        // Check for duplicate parameters in the saved object
        for (int i = 0; i < savedConstructorParameters.size(); i++)
            for (int j = 0; j < savedConstructorParameters.size(); j++)
                if (i != j)
                    if (savedConstructorParameters.get(i).name.equals(savedConstructorParameters.get(j).name))
                        throw new RuntimeException("Error, the object " + aClass.getSimpleName() + ". Was saved with a duplicate parameter name");

        // Find the past entry that matches the constructor parameter
        for (ConstructorParameter currentParameter : currentConstructorParameters) {
            int foundIndex = 0;
            boolean found = false;
            for (ConstructorParameter savedParameter : savedConstructorParameters) {
                if (currentParameter.name.equals(savedParameter.name)) {
                    if (!currentParameter.type.equals(savedParameter.type))
                        throw new RuntimeException("Error, the object " + aClass.getSimpleName() + ". Name matched, type did not");
                    found = true;
                    break;
                }
                foundIndex++;
            }

            // Save the match
            if (!found) {
                mappedOrder.add(-1);
                System.out.println("Warning, the object " + aClass.getSimpleName() + ". Has a new parameter that was not saved before. Making it to be set to null");
            } else {
                mappedOrder.add(foundIndex);
            }
        }

        return mappedOrder;
    }

    /**
     * Create a ConstructorMap object for its save line
     *
     * @param lines The lines to generate from
     * @return A ConstructorMap
     */
    private static List<ConstructorParameter> getConstructorParameters(String[] lines) {
        List<ConstructorParameter> constructorParameters = new ArrayList<>();
        for (int i = 0; i < lines.length / 2; i++) {
            constructorParameters.add(new ConstructorParameter(lines[i * 2], classForName(lines[i * 2 + 1], "com.ntankard.Tracking.DataBase.Core")));
        }
        return constructorParameters;
    }

    /**
     * Create a ConstructorMap object for a specific class
     *
     * @param aClass The class to generate from
     * @return its ConstructorMap or null if its not supported
     */
    static List<ConstructorParameter> getConstructorParameters(Class<? extends DataObject> aClass) {
        if (aClass.isPrimitive()) throw new RuntimeException("Primate object type detected");

        // Find the save settings
        ParameterMap parameterMap = getParameterMap(aClass);

        // Should this object be ignored?
        String[] paramGetters = parameterMap.parameterGetters();
        if (!parameterMap.shouldSave()) {
            return new ArrayList<>();
        }

        // Find the getters that map to the each constructor parameter
        Class<?>[] paramTypes = getConstructor(aClass).getParameterTypes();
        if (paramGetters.length != paramTypes.length)
            throw new RuntimeException("dataObjects ParameterMap annotation dose not match is constructor. Could save but would not be able to load. Aborting save");

        // Build the rest of the object
        List<ConstructorParameter> constructorParameters = new ArrayList<>();
        for (int i = 0; i < paramGetters.length; i++) {
            constructorParameters.add(new ConstructorParameter(paramGetters[i], paramTypes[i]));
        }
        return constructorParameters;
    }

    /**
     * Get the ParameterMap assigned to a class
     *
     * @param aClass The class to inspect
     * @return The ParameterMap
     */
    static ParameterMap getParameterMap(Class<? extends DataObject> aClass) {
        ParameterMap parameterMap = (ParameterMap) getConstructor(aClass).getAnnotation(ParameterMap.class);
        if (parameterMap == null)
            throw new RuntimeException("Error: " + aClass.getSimpleName() + " dose not support saving");
        return parameterMap;
    }

    /**
     * Get the single constructor
     *
     * @param aClass The class to get the constructor from
     * @return The constructor
     */
    private static Constructor getConstructor(Class<? extends DataObject> aClass) {
        Constructor[] constructors = aClass.getConstructors();
        if (constructors.length != 1)
            throw new RuntimeException("Error: " + aClass.getSimpleName() + ", More than one constructor detected");
        return constructors[0];
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################ Class containers ################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * A constructor parameter, its name and type
     */
    static class ConstructorParameter {
        Class type;
        String name;

        ConstructorParameter(String name, Class type) {
            this.type = type;
            this.name = name;
        }

        @Override
        public String toString() {
            return name + " " + type.toString();
        }
    }
}
