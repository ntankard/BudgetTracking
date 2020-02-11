package com.ntankard.Tracking.DataBase.Core.Transfers.CategoryFundTransfer;

import com.ntankard.TestUtil.DataAccessUntil;
import com.ntankard.TestUtil.DataObjectTestUtil;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
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
        assertNotEquals(0, TrackingDatabase.get().get(FundEvent.class).size());
        assertNotEquals(1, TrackingDatabase.get().get(FundEvent.class).size());
        assertNotEquals(0, TrackingDatabase.get().get(Currency.class).size());

        Period period = TrackingDatabase.get().get(Period.class).get(0);
        FundEvent fundEvent_fund1 = TrackingDatabase.get().get(FundEvent.class).get(0);
        FundEvent fundEvent_fund2 = TrackingDatabase.get().get(FundEvent.class).get(1);
        assertNotEquals(fundEvent_fund1.getCategory(), fundEvent_fund2.getCategory());
        Currency currency = TrackingDatabase.get().get(Currency.class).get(0);

        RePayCategoryFundTransfer rePayCategoryFundTransfer = new RePayCategoryFundTransfer(-1, period, fundEvent_fund1, currency);
        rePayCategoryFundTransfer.notifyParentLink();
        assertEquals(fundEvent_fund1.getCategory(), rePayCategoryFundTransfer.getSource());

        UseCategoryFundTransfer useCategoryFundTransfer = new UseCategoryFundTransfer(-2, "", 0.0, period, fundEvent_fund1, currency);
        useCategoryFundTransfer.notifyParentLink();
        assertThrows(IllegalArgumentException.class, () -> useCategoryFundTransfer.setDestination(null));
        assertEquals(fundEvent_fund1.getCategory(), useCategoryFundTransfer.getSource());

        useCategoryFundTransfer.setDestination(fundEvent_fund2);
        assertEquals(fundEvent_fund2.getCategory(), useCategoryFundTransfer.getSource());
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
     * Validate that the CategoryFundTransfers are in the correct state
     */
    @Test
    void validateCategoryFundTransfer() {
        for (CategoryFundTransfer categoryFundTransfer : TrackingDatabase.get().get(CategoryFundTransfer.class)) {
            assertEquals(categoryFundTransfer.getDestinationValue(), -categoryFundTransfer.getSourceValue(), "Fund source and destination values do not match when they should");
            assertEquals(categoryFundTransfer.getDestinationCurrency(), categoryFundTransfer.getSourceCurrency(), "Fund source and destination currencies do not match when they should");
        }
    }
}
