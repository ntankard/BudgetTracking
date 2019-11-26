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
    void validateRepaired() {
        TrackingDatabase_Integrity.validateRepaired();
    }

    @Test
    void validateId() {
        TrackingDatabase_Integrity.validateId();
    }

    @Test
    void validateParent() {
        TrackingDatabase_Integrity.validateParent();
    }

    @Test
    void validateChild() {
        TrackingDatabase_Integrity.validateChild();
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
    void validateCurrencyBankTransfer() {
        TrackingDatabase_Integrity.validateCurrencyBankTransfer();
    }

    @Test
    void validateIntraCurrencyBankTransfer() {
        TrackingDatabase_Integrity.validateIntraCurrencyBankTransfer();
    }

    @Test
    void validateCategoryFundTransfer() {
        TrackingDatabase_Integrity.validateCategoryFundTransfer();
    }

    @Test
    void validatePeriodSequence() {
        TrackingDatabase_Integrity.validatePeriodSequence();
    }

    @Test
    void validateCategoryFund() {
        TrackingDatabase_Integrity.validateCategoryFund();
    }

    @Test
    void validateFundFundEvent() {
        TrackingDatabase_Integrity.validateFundFundEvent();
    }
}
