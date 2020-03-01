package com.ntankard.Tracking.DataBase.Interface.Summary.Pool;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.CurrencyBound;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.Ordered;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Core.StatementEnd;
import com.ntankard.Tracking.DataBase.Core.Transfer.Bank.BankTransfer;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Set.Extended.Sum.PeriodPool_SumSet;
import com.ntankard.Tracking.DataBase.Interface.Set.TwoParent_Children_Set;

import static com.ntankard.ClassExtension.DisplayProperties.DataContext.ZERO_TARGET;
import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY;
import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;
import static com.ntankard.ClassExtension.MemberProperties.TRACE_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class Bank_Summary extends PoolSummary<Bank> implements CurrencyBound, Ordered {

    @ParameterMap(shouldSave = false)
    public Bank_Summary(ExistingPeriod period, Bank pool) {
        super(period, pool);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @Override
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 4)
    public Currency getCurrency() {
        return getPool().getCurrency();
    }

    @Override
    @DisplayProperties(order = 5, dataType = CURRENCY)
    public Double getStart() {
        int index = TrackingDatabase.get().get(Period.class).indexOf(getPeriod());
        if (index == 0) {
            throw new RuntimeException("This type of Period should not have a bank summary");
        }
        Period last = TrackingDatabase.get().get(Period.class).get(index - 1);
        if (last instanceof ExistingPeriod) {
            return new TwoParent_Children_Set<>(StatementEnd.class, last, getPool()).get().get(0).getEnd();
        }
        return getPool().getStart();
    }

    @Override
    @DisplayProperties(order = 6, dataType = CURRENCY)
    public Double getEnd() {
        return new TwoParent_Children_Set<>(StatementEnd.class, getPeriod(), getPool()).get().get(0).getEnd();
    }

    @DisplayProperties(order = 7, dataType = CURRENCY)
    public Double getNetTransfer() {
        return new PeriodPool_SumSet(BankTransfer.class, Bank.class, getPeriod(), getPool()).getTotal() / getCurrency().getToPrimary();
    }

    @DisplayProperties(order = 8, dataType = CURRENCY)
    public Double getSpend() {
        return new PeriodPool_SumSet(BankTransfer.class, Category.class, getPeriod(), getPool()).getTotal() / getCurrency().getToPrimary();
    }

    @Override
    @DisplayProperties(order = 9, dataType = CURRENCY)
    public Double getNet() {
        return super.getNet();
    }

    @Override
    @DisplayProperties(order = 10, dataType = CURRENCY)
    public Double getTransferSum() {
        return super.getTransferSum();
    }

    @Override
    @DisplayProperties(order = 11, dataContext = ZERO_TARGET, dataType = CURRENCY)
    public Double getMissing() {
        return super.getMissing();
    }

    @Override
    @MemberProperties(verbosityLevel = INFO_DISPLAY)
    @DisplayProperties(order = 12)
    public Boolean isValid() {
        return super.isValid();
    }

    @MemberProperties(verbosityLevel = TRACE_DISPLAY)
    @DisplayProperties(order = 23)
    public Integer getOrder() {
        return getPool().getOrder();
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public void setEnd(Double end) {
        new TwoParent_Children_Set<>(StatementEnd.class, getPeriod(), getPool()).get().get(0).setEnd(end);
    }
}
