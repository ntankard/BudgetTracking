package com.ntankard.Tracking.DataBase.Core.Pool.Fund;

import com.ntankard.TestUtil.DataAccessUntil;
import com.ntankard.TestUtil.DataObjectTestUtil;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Core.Pool.Fund.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Core.Pool.Fund.FundEvent.NoneFundEvent;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase_Integrity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FundTest {

    /**
     * Load the database
     */
    @BeforeEach
    void setUp() {
        DataAccessUntil.loadDatabase();
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################### Unit Tests (any instance of an object) #######################################
    //------------------------------------------------------------------------------------------------------------------

    @Test
    void constructor() {
        assertNotEquals(0, TrackingDatabase.get().get(Category.class).size());

        Category category = TrackingDatabase.get().get(Category.class).get(0);

        assertThrows(IllegalArgumentException.class, () -> new Fund(-1, null));
        assertDoesNotThrow(() -> new Fund(-1, category));
    }

    /**
     * Test that setDefaultFundEvent validates properly
     */
    @Test
    void setDefaultFundEvent() {
        assertNotEquals(0, TrackingDatabase.get().get(Category.class).size());
        Category category = TrackingDatabase.get().get(Category.class).get(0);

        Fund fund1 = new Fund(-1, category);
        Fund fund2 = new Fund(-1, category);

        FundEvent event1 = new NoneFundEvent(-1, "", fund1);
        FundEvent event2 = new NoneFundEvent(-1, "", fund2);

        assertDoesNotThrow(() -> fund1.setDefaultFundEvent(event1));
        assertThrows(IllegalArgumentException.class, () -> fund1.setDefaultFundEvent(event2));

        assertDoesNotThrow(() -> fund2.setDefaultFundEvent(event2));
        assertThrows(IllegalArgumentException.class, () -> fund2.setDefaultFundEvent(event1));
    }

    //------------------------------------------------------------------------------------------------------------------
    //########################## Implementation Tests (all declared objects in isolation) ##############################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Check that the correct parents are presenting
     */
    @Test
    void getParents() {
        List<String> exclude = new ArrayList<>();
        exclude.add("DefaultFundEvent");
        DataObjectTestUtil.testStandardParents(Fund.class, exclude);
    }

    /**
     * Check that anything returning a data object is valid
     */
    @Test
    void getDataObject() {
        DataObjectTestUtil.checkDataObjectNotNull(Fund.class);
    }

    //------------------------------------------------------------------------------------------------------------------
    //######################### Database Test (all declared objects considers as a group) ##############################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Check that the order is correct
     */
    @Test
    void getOrder() {
        DataObjectTestUtil.testStandardOrder(Fund.class);
    }

    /**
     * Run the validator
     */
    @Test
    void validateCategoryFund() {
        assertDoesNotThrow(TrackingDatabase_Integrity::validateCategoryFund);
    }
}
