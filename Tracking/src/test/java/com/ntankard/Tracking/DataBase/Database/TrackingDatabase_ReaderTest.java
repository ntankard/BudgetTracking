package com.ntankard.Tracking.DataBase.Database;

import com.ntankard.Tracking.DataBase.Core.Links.CategoryToCategorySet;
import com.ntankard.Tracking.DataBase.Core.Links.CategoryToVirtualCategory;
import com.ntankard.javaObjectDatabase.CoreObject.DataObject;
import com.ntankard.javaObjectDatabase.CoreObject.Field.DataField;
import com.ntankard.javaObjectDatabase.Database.TrackingDatabase;
import com.ntankard.javaObjectDatabase.Database.TrackingDatabase_Reader_Util;
import com.ntankard.javaObjectDatabase.util.FileUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ntankard.javaObjectDatabase.Database.TrackingDatabase_Reader.*;
import static com.ntankard.javaObjectDatabase.Database.TrackingDatabase_Reader_Read.dataObjectFromString;
import static com.ntankard.javaObjectDatabase.Database.TrackingDatabase_Reader_Save.dataObjectToString;
import static com.ntankard.javaObjectDatabase.Database.TrackingDatabase_Reader_Util.*;
import static org.junit.jupiter.api.Assertions.*;

class TrackingDatabase_ReaderTest {

    @BeforeAll
    static void setUp() {
        TrackingDatabase.reset();
        String savePath = "C:\\Users\\Nicholas\\Google Drive\\BudgetTrackingData";
        read(savePath, new HashMap<>());
    }

    @Test
    void testReadWrite() {
        for (Class<? extends DataObject> aClass : TrackingDatabase.get().getDataObjectTypes()) {
            if (Modifier.isAbstract(aClass.getModifiers())) {
                continue;
            }
            if (aClass.equals(CategoryToCategorySet.class) || aClass.equals(CategoryToVirtualCategory.class)) { // This wont work because new object can not exist because all the categories are already filled
                continue; // TODO might need a better solution here
            }
            for (DataObject dataObject : TrackingDatabase.get().get(aClass)) {

                List<DataField<?>> constructorParameters = getSaveFields(dataObject.getClass());

                if (shouldSave(dataObject.getClass())) {
                    List<String> first = dataObjectToString(dataObject, constructorParameters);

                    DataObject newObj = dataObjectFromString(dataObject.getClass(), first.toArray(new String[0]), null);

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

        new File(testPath).mkdir();
        new File(testPath + ROOT_DATA_PATH).mkdir();
        new File(testPath + ROOT_FILE_PATH).mkdir();
        save(testPath);

        String saveDir = TrackingDatabase_Reader_Util.getLatestSaveDirectory(savePath + ROOT_DATA_PATH);
        List<String> saveFiles = FileUtil.findFilesInDirectory(saveDir + INSTANCE_CLASSES_PATH);

        String testDir = TrackingDatabase_Reader_Util.getLatestSaveDirectory(testPath + ROOT_DATA_PATH);
        List<String> testFiles = FileUtil.findFilesInDirectory(testDir + INSTANCE_CLASSES_PATH);

        assertEquals(saveFiles.size(), testFiles.size());
        assertNotEquals(saveFiles.size(), 0);

        for (int i = 0; i < testFiles.size(); i++) {
            String saveFile = testFiles.get(i);
            String testFile = saveFiles.get(i);
            assertEquals(saveFile, testFile);

            List<String[]> saveLines = FileUtil.readLines(saveDir + INSTANCE_CLASSES_PATH + saveFile);
            List<String[]> testLines = FileUtil.readLines(testDir + INSTANCE_CLASSES_PATH + testFile);

            assertEquals(saveLines.size(), testLines.size(), "There are more entities of this class type. File:" + saveFile);
            assertEquals(1, saveLines.get(0).length, "Save file first line dose not contain the class type. File:" + saveFile);
            assertEquals(1, testLines.get(0).length, "Test file first line dose not contain the class type. File:" + saveFile);
            assertEquals(saveLines.get(0)[0], testLines.get(0)[0], "Class type dose not match. File:" + saveFile);

            List<Integer> map = new ArrayList<>();
            String[] saveParamLine = saveLines.get(1);
            String[] testParamLine = testLines.get(1);
            assertEquals(saveParamLine.length, testParamLine.length, "Different number of parameters. File:" + saveFile);
            for (int j = 0; j < saveParamLine.length / 2; j++) {
                String saveName = saveParamLine[j * 2];
                String saveType = saveParamLine[j * 2 + 1];
                int found = -1;
                for (int k = 0; k < saveParamLine.length / 2; k++) {
                    String testName = testParamLine[k * 2];
                    String testType = testParamLine[k * 2 + 1];
                    if (saveName.equals(testName)) {
                        assertEquals(testType, saveType, "Different types for the same param. File:" + saveFile + " Param:" + saveName);
                        found = k;
                        break;
                    }
                }
                assertNotEquals(-1, found, "The param could not be found once saved. File:" + saveFile + " Param:" + saveName);
                assertFalse(map.contains(found), "Match found more than once. File:" + saveFile + " Param:" + saveName);
                map.add(found);
            }

            for (int j = 2; j < saveLines.size(); j++) {
                String[] saveLine = saveLines.get(j);
                String[] testLine = testLines.get(j);

                assertEquals(saveLine.length, testLine.length);
                for (int k = 0; k < saveLine.length; k++) {
                    assertEquals(saveLine[k], testLine[map.get(k)]);
                }
            }
        }
    }
}
