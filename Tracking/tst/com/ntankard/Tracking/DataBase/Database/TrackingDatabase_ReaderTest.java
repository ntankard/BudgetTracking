package com.ntankard.Tracking.DataBase.Database;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.Util.FileUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TrackingDatabase_ReaderTest {

    @BeforeAll
    static void setUp() {
        TrackingDatabase.reset();
        String savePath = "C:\\Users\\Nicholas\\Google Drive\\BudgetTrackingData";
        TrackingDatabase_Reader.read(TrackingDatabase.get(), savePath);
    }

    @Test
    void testReadWrite() {
        for (Class<? extends DataObject> aClass : TrackingDatabase.get().getDataObjectTypes()) {
            for (Object dataObject : TrackingDatabase.get().get(aClass)) {

                TrackingDatabase_Reader.DataObjectSaver dataObjectSaver = TrackingDatabase_Reader.generateConstructorMap(dataObject.getClass());
                if (dataObjectSaver.shouldSave) {
                    List<String> first = TrackingDatabase_Reader.dataObjectToString((DataObject) dataObject, dataObjectSaver);

                    DataObject newObj = TrackingDatabase_Reader.dataObjectFromString(first.toArray(new String[0]), dataObjectSaver, dataObjectSaver, TrackingDatabase.get());

                    List<String> second = TrackingDatabase_Reader.dataObjectToString(newObj, dataObjectSaver);

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
        TrackingDatabase_Reader.read(TrackingDatabase.get(), savePath);

        new File(testPath).mkdir();
        TrackingDatabase_Reader.save(TrackingDatabase.get(), testPath);

        String saveDir = FileUtil.getLatestSaveDirectory(savePath);
        List<String> saveFiles = FileUtil.findFilesInDirectory(saveDir);

        String testDir = FileUtil.getLatestSaveDirectory(testPath);
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
