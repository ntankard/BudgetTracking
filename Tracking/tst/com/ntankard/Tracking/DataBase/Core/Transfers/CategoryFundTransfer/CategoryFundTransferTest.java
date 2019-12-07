package com.ntankard.Tracking.DataBase.Core.Transfers.CategoryFundTransfer;

import com.ntankard.TestUtil.DataAccessUntil;
import com.ntankard.TestUtil.DataObjectTestUtil;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Fund.Fund;
import com.ntankard.Tracking.DataBase.Core.Pool.Fund.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase_Integrity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryFundTransferTest {

    /**
     * Load the database
     */
    @BeforeEach
    void setUp() {
        DataAccessUntil.loadDatabase(); // TODO make this not needed by building the test objects directly for Unit Tests
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################### Unit Tests (any instance of an object) #######################################
    //------------------------------------------------------------------------------------------------------------------

    @Test
    void constructor() {
        assertNotEquals(0, TrackingDatabase.get().get(Period.class).size());
        assertNotEquals(0, TrackingDatabase.get().get(FundEvent.class).size());
        assertNotEquals(0, TrackingDatabase.get().get(Currency.class).size());

        Period period = TrackingDatabase.get().get(Period.class).get(0);
        FundEvent fundEvent = TrackingDatabase.get().get(FundEvent.class).get(0);
        Currency currency = TrackingDatabase.get().get(Currency.class).get(0);

        assertThrows(IllegalArgumentException.class, () -> new UseCategoryFundTransfer(-1, "", 0.0, null, fundEvent, currency));
        assertThrows(NullPointerException.class, () -> new UseCategoryFundTransfer(-1, "", 0.0, period, null, currency));
        assertThrows(IllegalArgumentException.class, () -> new UseCategoryFundTransfer(-1, "", 0.0, period, fundEvent, null));
        assertDoesNotThrow(() -> new UseCategoryFundTransfer(-1, "", 0.0, period, fundEvent, currency));

        assertThrows(IllegalArgumentException.class, () -> new RePayCategoryFundTransfer(-1, null, fundEvent, currency));
        assertThrows(NullPointerException.class, () -> new RePayCategoryFundTransfer(-1, period, null, currency));
        assertThrows(IllegalArgumentException.class, () -> new RePayCategoryFundTransfer(-1, period, fundEvent, null));
        assertDoesNotThrow(() -> new RePayCategoryFundTransfer(-1, period, fundEvent, currency));
    }

    @Test
    void setDestination() {
        assertNotEquals(0, TrackingDatabase.get().get(Period.class).size());
        assertNotEquals(0, TrackingDatabase.get().get(Fund.class).size());
        assertNotEquals(1, TrackingDatabase.get().get(Fund.class).size());
        assertNotEquals(0, TrackingDatabase.get().get(Currency.class).size());

        Period period = TrackingDatabase.get().get(Period.class).get(0);
        Fund fund1 = TrackingDatabase.get().get(Fund.class).get(0);
        FundEvent fundEvent_fund1 = fund1.getDefaultFundEvent();
        Fund fund2 = TrackingDatabase.get().get(Fund.class).get(1);
        FundEvent fundEvent_fund2 = fund2.getDefaultFundEvent();
        Currency currency = TrackingDatabase.get().get(Currency.class).get(0);

        RePayCategoryFundTransfer rePayCategoryFundTransfer = new RePayCategoryFundTransfer(-1, period, fundEvent_fund1, currency);
        rePayCategoryFundTransfer.notifyParentLink();
        assertThrows(IllegalArgumentException.class, () -> rePayCategoryFundTransfer.setDestination(null));
        assertEquals(fund1.getCategory(), rePayCategoryFundTransfer.getSource());
        assertEquals(fund1, rePayCategoryFundTransfer.getDestination());
        assertEquals(fundEvent_fund1, rePayCategoryFundTransfer.getFundEvent());
        assertEquals(fund1, rePayCategoryFundTransfer.getFundEvent().getFund());

        rePayCategoryFundTransfer.setDestination(fund2);
        assertEquals(fund2.getCategory(), rePayCategoryFundTransfer.getSource());
        assertEquals(fund2, rePayCategoryFundTransfer.getDestination());
        assertEquals(fundEvent_fund2, rePayCategoryFundTransfer.getFundEvent());
        assertEquals(fund2, rePayCategoryFundTransfer.getFundEvent().getFund());

        UseCategoryFundTransfer useCategoryFundTransfer = new UseCategoryFundTransfer(-2, "", 0.0, period, fundEvent_fund1, currency);
        useCategoryFundTransfer.notifyParentLink();
        assertThrows(IllegalArgumentException.class, () -> useCategoryFundTransfer.setDestination(null));
        assertEquals(fund1.getCategory(), useCategoryFundTransfer.getSource());
        assertEquals(fund1, useCategoryFundTransfer.getDestination());
        assertEquals(fundEvent_fund1, useCategoryFundTransfer.getFundEvent());
        assertEquals(fund1, useCategoryFundTransfer.getFundEvent().getFund());

        useCategoryFundTransfer.setDestination(fund2);
        assertEquals(fund2.getCategory(), useCategoryFundTransfer.getSource());
        assertEquals(fund2, useCategoryFundTransfer.getDestination());
        assertEquals(fundEvent_fund2, useCategoryFundTransfer.getFundEvent());
        assertEquals(fund2, useCategoryFundTransfer.getFundEvent().getFund());
    }

    @Test
    void setFundEvent() {
        assertNotEquals(0, TrackingDatabase.get().get(Period.class).size());
        assertNotEquals(0, TrackingDatabase.get().get(Fund.class).size());
        assertNotEquals(1, TrackingDatabase.get().get(Fund.class).size());
        assertNotEquals(0, TrackingDatabase.get().get(Currency.class).size());

        Period period = TrackingDatabase.get().get(Period.class).get(0);
        Fund fund1 = TrackingDatabase.get().get(Fund.class).get(0);
        FundEvent fundEvent_fund1 = fund1.getChildren(FundEvent.class).get(0);
        Fund fund2 = TrackingDatabase.get().get(Fund.class).get(1);
        FundEvent fundEvent_fund2 = fund2.getChildren(FundEvent.class).get(0);
        Currency currency = TrackingDatabase.get().get(Currency.class).get(0);

        RePayCategoryFundTransfer rePayCategoryFundTransfer = new RePayCategoryFundTransfer(-1, period, fundEvent_fund1, currency);
        rePayCategoryFundTransfer.notifyParentLink();
        assertThrows(IllegalArgumentException.class, () -> rePayCategoryFundTransfer.setFundEvent(null));
        assertThrows(IllegalArgumentException.class, () -> rePayCategoryFundTransfer.setFundEvent(fundEvent_fund2));
        assertDoesNotThrow(() -> rePayCategoryFundTransfer.setFundEvent(fundEvent_fund1));

        UseCategoryFundTransfer useCategoryFundTransfer = new UseCategoryFundTransfer(-2, "", 0.0, period, fundEvent_fund1, currency);
        useCategoryFundTransfer.notifyParentLink();
        assertThrows(IllegalArgumentException.class, () -> useCategoryFundTransfer.setFundEvent(null));
        assertThrows(IllegalArgumentException.class, () -> useCategoryFundTransfer.setFundEvent(fundEvent_fund2));
        assertDoesNotThrow(() -> useCategoryFundTransfer.setFundEvent(fundEvent_fund1));
    }

    //------------------------------------------------------------------------------------------------------------------
    //########################## Implementation Tests (all declared objects in isolation) ##############################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Check that the correct parents are presenting
     */
    @Test
    void getParents() {
        DataObjectTestUtil.testStandardParents(CategoryFundTransfer.class);
    }

    /**
     * Check that anything returning a data object is valid
     */
    @Test
    void getDataObject() {
        DataObjectTestUtil.checkDataObjectNotNull(CategoryFundTransfer.class);
    }

    //------------------------------------------------------------------------------------------------------------------
    //######################### Database Test (all declared objects considers as a group) ##############################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Run the validator
     */
    @Test
    void validateCategoryFundTransfer() {
        assertDoesNotThrow(TrackingDatabase_Integrity::validateCategoryFundTransfer);
    }
}
