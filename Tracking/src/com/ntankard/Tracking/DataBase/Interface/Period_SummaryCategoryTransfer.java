package com.ntankard.Tracking.DataBase.Interface;

import com.ntankard.Tracking.DataBase.Core.Category;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;
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
    public List<Currency> getCurrencies() {
        return new ArrayList<>();
    }

    /**
     * {@inheritDoc
     */
    @Override
    public List<CategoryTransfer> getEvents() {
        return new ArrayList<>();
    }
}
