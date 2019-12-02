package com.ntankard.Tracking.DataBase.Interface.Summary.Pool;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.CurrencyBound;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank.StatementEnd;
import com.ntankard.Tracking.DataBase.Core.Transfers.BankCategoryTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfers.BankTransfer.BankTransfer;
import com.ntankard.Tracking.DataBase.Core.Transfers.Transfer;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;
import com.ntankard.Tracking.DataBase.Interface.Summary.TransferSet_Summary;
import com.ntankard.Tracking.DataBase.Interface.Set.MultiParent_Set;

import static com.ntankard.ClassExtension.DisplayProperties.DataContext.ZERO_TARGET;
import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY;
import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

@ClassExtensionProperties(includeParent = true)
public class Bank_Summary extends PoolSummary<Bank> implements CurrencyBound {

    @ParameterMap(shouldSave = false)
    public Bank_Summary(Period period, Bank pool, Class<? extends Transfer> transferType) {
        super(period, pool, transferType);
    }

    @ParameterMap(shouldSave = false)
    public Bank_Summary(Period period, Bank pool) {
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
        if (getPeriod().getLast() == null)
            return getPool().getStart();
        return new MultiParent_Set<>(StatementEnd.class, getPeriod().getLast(), getPool()).get().get(0).getEnd();
    }

    @Override
    @DisplayProperties(order = 6, dataType = CURRENCY)
    public Double getEnd() {
        return new MultiParent_Set<>(StatementEnd.class, getPeriod(), getPool()).get().get(0).getEnd();
    }

    @DisplayProperties(order = 7, dataType = CURRENCY)
    public Double getNetTransfer() {
        return new TransferSet_Summary<>(BankTransfer.class, getPeriod(), getPool()).getTotal() / getCurrency().getToPrimary();
    }

    @DisplayProperties(order = 8, dataType = CURRENCY)
    public Double getSpend() {
        return new TransferSet_Summary<>(BankCategoryTransfer.class, getPeriod(), getPool()).getTotal() / getCurrency().getToPrimary();
    }

    @Override
    @DisplayProperties(order = 9, dataType = CURRENCY)
    public Double getExpected() {
        return super.getExpected();
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

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public void setEnd(Double end) {
        new MultiParent_Set<>(StatementEnd.class, getPeriod(), getPool()).get().get(0).setEnd(end);
    }
}
