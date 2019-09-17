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
    public List<Currency> getCurrencies() {
        List<Currency> toReturn = new ArrayList<>();
        for (PeriodTransfer periodTransfer : getEvents()) {
            Currency currency = periodTransfer.getCurrency();
            if (!toReturn.contains(currency)) {
                toReturn.add(currency);
            }
        }
        return toReturn;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public List<PeriodTransfer> getEvents() {
        List<PeriodTransfer> toReturn = new ArrayList<>();
        for (PeriodTransfer periodTransfer : periodTransfers) {
            if (periodTransfer.getCategory().equals(category)) {
                if (periodTransfer.getSource().equals(period) || periodTransfer.getDestination().equals(period)) {
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
    public double getTotal(Currency toSum) {
        double sum = 0;
        for (PeriodTransfer periodTransfer : getEvents()) {
            if (periodTransfer.getCurrency().equals(toSum)) {
                if (periodTransfer.getSource().equals(period)) {
                    sum -= periodTransfer.getValue();
                } else if (periodTransfer.getDestination().equals(period)) {
                    sum += periodTransfer.getValue();
                }
            }
        }
        return sum;
    }

    /**
     * Sum all the PeriodTransfer for this category, in this period. Return in the primary currency
     *
     * @return All the PeriodTransfer for this category, in this period. Return in the primary currency
     */
    public double getTotal() {
        double sum = 0;
        for (Currency currency : getCurrencies()) {
            sum += getTotal(currency) * currency.getToPrimary();
        }
        return sum;
    }
}
