package com.ntankard.Tracking.DataBase.Core.Transfers.CategoryFundTransfer;

import com.ntankard.TestUtil.DataAccessUntil;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UseCategoryFundTransferTest {

    /**
     * Load the database
     */
    @BeforeEach
    void setUp() {
        DataAccessUntil.loadDatabase(); //TODO make this not needed by building the test objects directly for Unit Tests
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################### Unit Tests (any instance of an object) #######################################
    //------------------------------------------------------------------------------------------------------------------

    @Test
    void getDestinationCurrency() {
        assertNotEquals(0, TrackingDatabase.get().get(Period.class).size());
        assertNotEquals(0, TrackingDatabase.get().get(FundEvent.class).size());
        assertNotEquals(0, TrackingDatabase.get().get(Currency.class).size());

        Period period = TrackingDatabase.get().get(Period.class).get(0);
        FundEvent fundEvent = TrackingDatabase.get().get(FundEvent.class).get(0);
        Currency currency = TrackingDatabase.get().get(Currency.class).get(0);

        UseCategoryFundTransfer useCategoryFundTransfer = new UseCategoryFundTransfer(-2, "", 0.0, period, fundEvent, currency);
        useCategoryFundTransfer.notifyParentLink();
        assertThrows(IllegalArgumentException.class, () -> useCategoryFundTransfer.setDestinationCurrency(null));
        assertDoesNotThrow(() -> useCategoryFundTransfer.setDestinationCurrency(currency));
    }
}
