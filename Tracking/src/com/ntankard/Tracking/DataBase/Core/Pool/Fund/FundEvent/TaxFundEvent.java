package com.ntankard.Tracking.DataBase.Core.Pool.Fund.FundEvent;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Core.Pool.Fund.Fund;
import com.ntankard.Tracking.DataBase.Core.Transfers.BankCategoryTransfer;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Set.MultiParent_Set;
import com.ntankard.Tracking.DataBase.Interface.Set.ObjectSet;
import com.ntankard.Tracking.DataBase.Interface.Summary.TransferSet_Summary;

@ClassExtensionProperties(includeParent = true)
public class TaxFundEvent extends FundEvent {

    // My Values
    private Double percentage;

    /**
     * Constructor
     */
    @ParameterMap(parameterGetters = {"getId", "getName", "getFund", "getPercentage"})
    public TaxFundEvent(Integer id, String name, Fund fund, Double percentage) {
        super(id, name, fund);
        this.percentage = percentage;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public Boolean isActiveThisPeriod(Period period) {
        return isChargeThisPeriod(period);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public Boolean isChargeThisPeriod(Period period) {
        return true;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public Double getCharge(Period period) {
        Category category = TrackingDatabase.get().getSpecialValue(Category.class, Category.TAXABLE);
        ObjectSet<BankCategoryTransfer> objectSet = new MultiParent_Set<>(BankCategoryTransfer.class, period, category);
        return Currency.round(new TransferSet_Summary<>(objectSet, category).getTotal() * percentage);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @DisplayProperties(order = 4)
    public Double getPercentage() {
        return percentage;
    }
}
