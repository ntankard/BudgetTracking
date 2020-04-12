package com.ntankard.Tracking.DataBase.Database;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Field.Field;
import com.ntankard.Tracking.Util.FileUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ntankard.Tracking.DataBase.Database.TrackingDatabase_Reader.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TrackingDatabase_ReaderTest {

    @BeforeAll
    static void setUp() {
        TrackingDatabase.reset();
        String savePath = "C:\\Users\\Nicholas\\Google Drive\\BudgetTrackingData";
        read(savePath, new HashMap<>());
        TrackingDatabase.get().finalizeCore();
    }

    @Test
    void testReadWrite() {

        // Generate a list of all objects in the database linked to there ID
        Map<Integer, DataObject> allObjects = new HashMap<>();
        for (DataObject toAdd : TrackingDatabase.get().getAll()) {
            allObjects.put(toAdd.getId(), toAdd);
        }

        for (Class<? extends DataObject> aClass : TrackingDatabase.get().getDataObjectTypes()) {
            for (DataObject dataObject : TrackingDatabase.get().get(aClass)) {

                List<Field<?>> constructorParameters = getSaveFields(dataObject.getClass());
                if (shouldSave(dataObject.getClass())) {
                    List<String> first = dataObjectToString(dataObject, constructorParameters);

                    List<Integer> mapping = new ArrayList<>();
                    for (int i = 0; i < constructorParameters.size(); i++) {
                        mapping.add(i);
                    }
                    DataObject newObj = dataObjectFromString(dataObject.getClass(), first.toArray(new String[0]), mapping, allObjects);

                    List<String> second = dataObjectToString(newObj, constructorParameters);

                    assertEquals(first.size(), second.size());
                    for (int i = 0; i < second.size(); i++) {
                        assertEquals(first.get(i), second.get(i));
                    }
                }
            }
        }
    }

    @Test
    void testFullIO() {
        String savePath = "C:\\Users\\Nicholas\\Google Drive\\BudgetTrackingData";
        String testPath = "testFiles\\";

        TrackingDatabase.reset();
        read(savePath, new HashMap<>());
        TrackingDatabase.get().finalizeCore();

        new File(testPath).mkdir();
        new File(testPath + ROOT_DATA_PATH).mkdir();
        new File(testPath + ROOT_FILE_PATH).mkdir();
        save(testPath);

        String saveDir = FileUtil.getLatestSaveDirectory(savePath + ROOT_DATA_PATH);
        List<String> saveFiles = FileUtil.findFilesInDirectory(saveDir + INSTANCE_CLASSES_PATH);

        String testDir = FileUtil.getLatestSaveDirectory(testPath + ROOT_DATA_PATH);
        List<String> testFiles = FileUtil.findFilesInDirectory(testDir + INSTANCE_CLASSES_PATH);

        assertEquals(saveFiles.size(), testFiles.size());
        assertNotEquals(saveFiles.size(), 0);

        for (int i = 0; i < testFiles.size(); i++) {
            String saveFile = testFiles.get(i);
            String testFile = saveFiles.get(i);
            assertEquals(saveFile, testFile);

            List<String[]> saveLines = FileUtil.readLines(saveDir + INSTANCE_CLASSES_PATH + saveFile);
            List<String[]> testLines = FileUtil.readLines(testDir + INSTANCE_CLASSES_PATH + testFile);

            assertEquals(saveLines.size(), testLines.size());
            for (int j = 0; j < saveLines.size(); j++) {
                String[] saveLine = saveLines.get(j);
                String[] testLine = testLines.get(j);

                assertEquals(saveLine.length, testLine.length);
                for (int k = 0; k < saveLine.length; k++) {
                    assertEquals(saveLine[k], testLine[k]);
                }
            }
        }
    }
}
