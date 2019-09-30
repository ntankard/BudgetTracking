package com.ntankard.Tracking.DataBase.Interface;

import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Interface.MoneyEvent_Sets.PeriodCategory_Set;
import com.ntankard.Tracking.DataBase.TrackingDatabase;

import static com.ntankard.ClassExtension.DisplayProperties.DataContext.ZERO_SCALE;
import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY_YEN;

public class PeriodCategory {

    private Period period;
    private TrackingDatabase database;

    public PeriodCategory(Period period, TrackingDatabase database) {
        this.period = period;
        this.database = database;
    }

    @DisplayProperties(order = 0, name = "Period")
    public String getId() {
        return period.getId();
    }

    @DisplayProperties(order = 1, dataType = CURRENCY_YEN, dataContext = ZERO_SCALE)
    public Double getIncome() {
        return new PeriodCategory_Set(period, database.getCategory("Income")).getTotal();
    }

    @DisplayProperties(order = 2, dataType = CURRENCY_YEN, dataContext = ZERO_SCALE)
    public Double getMembership() {
        return new PeriodCategory_Set(period, database.getCategory("Membership")).getTotal();
    }

    @DisplayProperties(order = 3, dataType = CURRENCY_YEN, dataContext = ZERO_SCALE)
    public Double getBills() {
        return new PeriodCategory_Set(period, database.getCategory("Bills")).getTotal();
    }

    @DisplayProperties(order = 4, dataType = CURRENCY_YEN, dataContext = ZERO_SCALE)
    public Double getShopping() {
        return new PeriodCategory_Set(period, database.getCategory("Shopping")).getTotal();
    }

    @DisplayProperties(order = 5, dataType = CURRENCY_YEN, dataContext = ZERO_SCALE)
    public Double getEntertainment() {
        return new PeriodCategory_Set(period, database.getCategory("Entertainment")).getTotal();
    }

    @DisplayProperties(order = 6, dataType = CURRENCY_YEN, dataContext = ZERO_SCALE)
    public Double getFood() {
        return new PeriodCategory_Set(period, database.getCategory("Food")).getTotal();
    }

    @DisplayProperties(order = 7, dataType = CURRENCY_YEN, dataContext = ZERO_SCALE)
    public Double getRent() {
        return new PeriodCategory_Set(period, database.getCategory("Rent")).getTotal();
    }

    @DisplayProperties(order = 8, dataType = CURRENCY_YEN, dataContext = ZERO_SCALE)
    public Double getTravel() {
        return new PeriodCategory_Set(period, database.getCategory("Travel")).getTotal();
    }

    @DisplayProperties(order = 9, dataType = CURRENCY_YEN, dataContext = ZERO_SCALE)
    public Double getUnaccounted() {
        return new PeriodCategory_Set(period, database.getCategory("Unaccounted")).getTotal();
    }
}
