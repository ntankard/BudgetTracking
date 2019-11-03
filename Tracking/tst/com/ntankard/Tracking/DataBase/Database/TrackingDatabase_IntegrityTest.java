package com.ntankard.Tracking.DataBase.Database;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TrackingDatabase_IntegrityTest {

    @BeforeAll
    static void setUp() {
        TrackingDatabase.reset();
        String savePath = "C:\\Users\\Nicholas\\Google Drive\\BudgetTrackingData";
        TrackingDatabase_Reader.read(TrackingDatabase.get(), savePath);
    }

    @Test
    void validateCore() {
        TrackingDatabase_Integrity.validateCore();
    }

    @Test
    void validateChild() {
        TrackingDatabase_Integrity.validateChild();
    }

    @Test
    void validateParent() {
        TrackingDatabase_Integrity.validateParent();
    }

    @Test
    void validateDefault() {
        TrackingDatabase_Integrity.validateDefault();
    }

    @Test
    void validateSpecial() {
        TrackingDatabase_Integrity.validateSpecial();
    }

    @Test
    void validateId() {
        TrackingDatabase_Integrity.validateId();
    }
}
