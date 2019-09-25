package com.ntankard.Tracking.DataBase.Interface;

import com.ntankard.Tracking.DataBase.Core.Base.Transfer;
import com.ntankard.Tracking.DataBase.Core.Category;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;

import java.util.ArrayList;
import java.util.List;

public class Period_SummaryTransfer<T extends Transfer> extends Period_Summary<T> {

    /**
     * All the Transfers for this period (not yet filtered by category)
     */
    protected List<T> transfers;

    /**
     * Constructor
     *
     * @param period    The period to summarise
     * @param category  The category to filler on
     * @param transfers All the Transfers for this period (not yet filtered by category)
     */
    public Period_SummaryTransfer(Period period, Category category, List<T> transfers) {
        super(period, category);
        this.transfers = transfers;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public List<T> getEvents() {
        List<T> toReturn = new ArrayList<>();
        for (T periodTransfer : transfers) {
            if (periodTransfer.isThisDestination(period, category) || periodTransfer.isThisSource(period, category)) {
                toReturn.add(periodTransfer);
            }
        }
        return toReturn;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public double getTotal(Currency toSum) {
        double sum = 0;
        for (T nonPeriodFundTransfer : getEvents()) {
            if (nonPeriodFundTransfer.getCurrency().equals(toSum)) {
                if (nonPeriodFundTransfer.isThisSource(period, category)) {
                    sum -= nonPeriodFundTransfer.getValue();
                } else if (nonPeriodFundTransfer.isThisDestination(period, category)) {
                    sum += nonPeriodFundTransfer.getValue();
                }
            }
        }
        return sum;
    }
}
