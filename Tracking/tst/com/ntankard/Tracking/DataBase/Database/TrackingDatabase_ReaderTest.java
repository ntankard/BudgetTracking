package com.ntankard.Tracking.DataBase.Database;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.Util.FileUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ntankard.Tracking.DataBase.Database.TrackingDatabase_Reader.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TrackingDatabase_ReaderTest {

    @BeforeAll
    static void setUp() {
        TrackingDatabase.reset();
        String savePath = "C:\\Users\\Nicholas\\Google Drive\\BudgetTrackingData";
        read(TrackingDatabase.get(), savePath);
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

                List<ConstructorParameter> constructorParameters = getConstructorParameters(dataObject.getClass());
                if (getParameterMap(dataObject.getClass()).shouldSave()) {
                    List<String> first = dataObjectToString(dataObject, constructorParameters);

                    DataObject newObj = dataObjectFromString(dataObject.getClass(), first.toArray(new String[0]), constructorParameters, constructorParameters, allObjects);

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
        read(TrackingDatabase.get(), savePath);
        TrackingDatabase.get().finalizeCore();

        new File(testPath).mkdir();
        new File(testPath + DATA_PATH).mkdir();
        new File(testPath + IMAGE_PATH).mkdir();
        new File(testPath + NEW_IMAGE_PATH).mkdir();
        new File(testPath + FILE_PATH).mkdir();
        save(TrackingDatabase.get(), testPath);

        String saveDir = FileUtil.getLatestSaveDirectory(savePath + DATA_PATH);
        List<String> saveFiles = FileUtil.findFilesInDirectory(saveDir);

        String testDir = FileUtil.getLatestSaveDirectory(testPath + DATA_PATH);
        List<String> testFiles = FileUtil.findFilesInDirectory(testDir);

        assertEquals(saveFiles.size(), testFiles.size());

        for (int i = 0; i < testFiles.size(); i++) {
            String saveFile = testFiles.get(i);
            String testFile = saveFiles.get(i);
            assertEquals(saveFile, testFile);

            List<String[]> saveLines = FileUtil.readLines(saveDir + saveFile);
            List<String[]> testLines = FileUtil.readLines(testDir + testFile);

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
