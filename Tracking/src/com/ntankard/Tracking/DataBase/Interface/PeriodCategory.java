package com.ntankard.Tracking.DataBase.Interface;

import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
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
        return period.getCategoryTotal(database.getCategory("Income"));
    }

    @DisplayProperties(order = 2, dataType = CURRENCY_YEN, dataContext = ZERO_SCALE)
    public Double getMembership() {
        return period.getCategoryTotal(database.getCategory("Membership"));
    }

    @DisplayProperties(order = 3, dataType = CURRENCY_YEN, dataContext = ZERO_SCALE)
    public Double getBills() {
        return period.getCategoryTotal(database.getCategory("Bills"));
    }

    @DisplayProperties(order = 4, dataType = CURRENCY_YEN, dataContext = ZERO_SCALE)
    public Double getShopping() {
        return period.getCategoryTotal(database.getCategory("Shopping"));
    }

    @DisplayProperties(order = 5, dataType = CURRENCY_YEN, dataContext = ZERO_SCALE)
    public Double getEntertainment() {
        return period.getCategoryTotal(database.getCategory("Entertainment"));
    }

    @DisplayProperties(order = 6, dataType = CURRENCY_YEN, dataContext = ZERO_SCALE)
    public Double getFood() {
        return period.getCategoryTotal(database.getCategory("Food"));
    }

    @DisplayProperties(order = 7, dataType = CURRENCY_YEN, dataContext = ZERO_SCALE)
    public Double getRent() {
        return period.getCategoryTotal(database.getCategory("Rent"));
    }

    @DisplayProperties(order = 8, dataType = CURRENCY_YEN, dataContext = ZERO_SCALE)
    public Double getTravel() {
        return period.getCategoryTotal(database.getCategory("Travel"));
    }

    @DisplayProperties(order = 9, dataType = CURRENCY_YEN, dataContext = ZERO_SCALE)
    public Double getUnaccounted() {
        return period.getCategoryTotal(database.getCategory("Unaccounted"));
    }
}
