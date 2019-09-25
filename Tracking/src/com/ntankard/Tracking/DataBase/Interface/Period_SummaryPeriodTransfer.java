package com.ntankard.Tracking.DataBase.Interface;

import com.ntankard.Tracking.DataBase.Core.Category;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Transfers.PeriodTransfer;

import java.util.ArrayList;
import java.util.List;

public class Period_SummaryPeriodTransfer extends Period_Summary<PeriodTransfer> {

    /**
     * All the PeriodTransfer for this period (not yet filtered by category)
     */
    private List<PeriodTransfer> periodTransfers;

    /**
     * Constructor
     *
     * @param period          The period to summarise
     * @param category        The category to filler on
     * @param periodTransfers All the PeriodTransfers for this period (not yet filtered by category)
     */
    public Period_SummaryPeriodTransfer(Period period, Category category, List<PeriodTransfer> periodTransfers) {
        super(period, category);
        this.periodTransfers = periodTransfers;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public List<PeriodTransfer> getEvents() {
        List<PeriodTransfer> toReturn = new ArrayList<>();
        for (PeriodTransfer periodTransfer : periodTransfers) {
            if (periodTransfer.getSourceCategory().equals(category)) {
                if (periodTransfer.getSourceContainer().equals(period) || periodTransfer.getDestinationContainer().equals(period)) {
                    toReturn.add(periodTransfer);
                }
            }
        }
        return toReturn;
    }

    /**
     * Sum all the PeriodTransfer for this category, in this period that are in the specified currency
     *
     * @param toSum The currency to sum
     * @return All the PeriodTransfer for this category, in this period that are in the specified currency
     */
    @Override
    public double getTotal(Currency toSum) {
        double sum = 0;
        for (PeriodTransfer periodTransfer : getEvents()) {
            if (periodTransfer.getCurrency().equals(toSum)) {
                if (periodTransfer.getSourceContainer().equals(period)) {
                    sum -= periodTransfer.getValue();
                } else if (periodTransfer.getDestinationContainer().equals(period)) {
                    sum += periodTransfer.getValue();
                }
            }
        }
        return sum;
    }
}
