package com.ntankard.budgetTracking.dataBase.database;

import com.ntankard.testUtil.DataAccessUntil;
import com.ntankard.budgetTracking.Main;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.javaObjectDatabase.database.io.Database_IO_Util;
import com.ntankard.javaObjectDatabase.util.FileUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.ntankard.javaObjectDatabase.database.io.Database_IO.*;
import static com.ntankard.javaObjectDatabase.database.io.Database_IO_Util.*;
import static org.junit.jupiter.api.Assertions.*;

@Execution(ExecutionMode.CONCURRENT)
class Database_ReaderTest {

    /**
     * The database instance to use
     */
    private static Database database;

    /**
     * Load the database
     */
    @BeforeEach
    void setUp() {
        database = DataAccessUntil.getDataBase(new ArrayList<>());
    }

    @Test
    void testReadWrite() {
        assertTrue(false);
//        for (Class<? extends DataObject> aClass : trackingDatabase.getDataObjectTypes()) {
//            if (Modifier.isAbstract(aClass.getModifiers())) {
//                continue;
//            }
//            if (aClass.equals(CategoryToCategorySet.class) || aClass.equals(CategoryToVirtualCategory.class)) { // This wont work because new object can not exist because all the categories are already filled
//                continue; // TODO might need a better solution here
//            }
//            for (DataObject dataObject : trackingDatabase.get(aClass)) {
//
//                List<DataField_Schema<?>> constructorParameters = getSaveFields(dataObject.getClass());
//
//                if (shouldSave(dataObject.getClass())) {
//                    List<String> first = dataObjectToString(dataObject, constructorParameters);
//
//                    DataObject newObj = dataObjectFromString(dataObject.getClass(), first.toArray(new String[0]), null);
//
//                    List<String> second = dataObjectToString(newObj, constructorParameters);
//
//                    assertEquals(first.size(), second.size());
//                    for (int i = 0; i < second.size(); i++) {
//                        assertEquals(first.get(i), second.get(i));
//                    }
//                }
//            }
//        }
    }

    @Test
    void testFullIO() {
        String testPath = "testFiles\\";

        new File(testPath).mkdir();
        new File(testPath + ROOT_DATA_PATH).mkdir();
        new File(testPath + ROOT_FILE_PATH).mkdir();
        new File(testPath + ROOT_FILES_PATH).mkdir();
        save(database, testPath);

        String saveDir = Database_IO_Util.getLatestSaveDirectory(Main.savePath + ROOT_DATA_PATH);
        List<String> saveFiles = FileUtil.findFilesInDirectory(saveDir + INSTANCE_CLASSES_PATH);

        String testDir = Database_IO_Util.getLatestSaveDirectory(testPath + ROOT_DATA_PATH);
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

            List<String[]> dataOnlySave = new ArrayList<>();
            List<String[]> dataOnlyTest = new ArrayList<>();

            dataOnlySave.addAll(saveLines);
            dataOnlySave.remove(saveLines.get(0));
            dataOnlySave.remove(saveLines.get(1));
            dataOnlyTest.addAll(testLines);
            dataOnlyTest.remove(testLines.get(0));
            dataOnlyTest.remove(testLines.get(1));

            Collections.sort(dataOnlySave, Comparator.comparingInt(o -> Integer.parseInt(o[0])));
            Collections.sort(dataOnlyTest, Comparator.comparingInt(o -> Integer.parseInt(o[0])));

            for (int j = 0; j < dataOnlySave.size(); j++) {
                String[] saveLine = dataOnlySave.get(j);
                String[] testLine = dataOnlyTest.get(j);

                assertEquals(saveLine.length, testLine.length);
                for (int k = 0; k < saveLine.length; k++) {
                    assertEquals(saveLine[k], testLine[map.get(k)]);
                }
            }
        }
    }
}
