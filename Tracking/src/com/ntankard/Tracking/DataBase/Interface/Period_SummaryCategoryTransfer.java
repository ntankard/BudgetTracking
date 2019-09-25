package com.ntankard.Tracking.DataBase.Interface;

import com.ntankard.Tracking.DataBase.Core.*;
import com.ntankard.Tracking.DataBase.Core.Transfers.CategoryTransfer;

import java.util.ArrayList;
import java.util.List;

public class Period_SummaryCategoryTransfer extends Period_Summary<CategoryTransfer> {

    /**
     * All the CategoryTransfers for this period (not yet filtered by category)
     */
    private List<CategoryTransfer> categoryTransfers;

    /**
     * Constructor
     *
     * @param period            The period to summarise
     * @param category          The category to filler on
     * @param categoryTransfers All the CategoryTransfers for this period (not yet filtered by category)
     */
    public Period_SummaryCategoryTransfer(Period period, Category category, List<CategoryTransfer> categoryTransfers) {
        super(period, category);
        this.categoryTransfers = categoryTransfers;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public List<CategoryTransfer> getEvents() {
        List<CategoryTransfer> toReturn = new ArrayList<>();
        for (CategoryTransfer categoryTransfer : categoryTransfers) {
            if (categoryTransfer.getSourceCategory().equals(category) || categoryTransfer.getDestinationCategory().equals(category)) {
                toReturn.add(categoryTransfer);
            }
        }
        return toReturn;
    }

    /**
     * Sum all the CategoryTransfers for this category, in this period that are in the specified currency
     *
     * @param toSum The currency to sum
     * @return All the CategoryTransfers for this category, in this period that are in the specified currency
     */
    @Override
    public double getTotal(Currency toSum) {
        double sum = 0;
        for (CategoryTransfer categoryTransfer : categoryTransfers) {
            if (categoryTransfer.getCurrency().equals(toSum)) {
                if (categoryTransfer.getSourceCategory().equals(category)) {
                    sum -= categoryTransfer.getValue();
                } else if (categoryTransfer.getDestinationCategory().equals(category)) {
                    sum += categoryTransfer.getValue();
                }
            }
        }
        return sum;
    }
}
