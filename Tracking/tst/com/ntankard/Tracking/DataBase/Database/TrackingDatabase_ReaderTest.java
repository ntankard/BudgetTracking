package com.ntankard.Tracking.DataBase.Database;

import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.FundChargeTransfer.FundChargeTransfer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
            if (FundChargeTransfer.class.isAssignableFrom(aClass)) {
                continue;
            }
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
}
