package com.ntankard.Tracking.DataBase.Interface.Summary.Pool;

import com.ntankard.CoreObject.Field.DataCore.Method_DataCore;
import com.ntankard.CoreObject.FieldContainer;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.CurrencyBound;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.Ordered;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Tracking_DataField;
import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.SolidCategory;
import com.ntankard.Tracking.DataBase.Core.Pool.Pool;
import com.ntankard.Tracking.DataBase.Core.StatementEnd;
import com.ntankard.Tracking.DataBase.Core.Transfer.Bank.BankTransfer;
import com.ntankard.Tracking.DataBase.Database.ParameterMap;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Set.Extended.Sum.PeriodPool_SumSet;
import com.ntankard.Tracking.DataBase.Interface.Set.TwoParent_Children_Set;

import static com.ntankard.CoreObject.Field.Properties.Display_Properties.DataType.CURRENCY;
import static com.ntankard.CoreObject.Field.Properties.Display_Properties.TRACE_DISPLAY;

@ParameterMap(shouldSave = false)
public class Bank_Summary extends PoolSummary<Bank> implements CurrencyBound, Ordered {


    public static final String Bank_Summary_NetTransfer = "getNetTransfer";
    public static final String Bank_Summary_Spend = "getSpend";
    public static final String Bank_Summary_Order = "getOrder";

    /**
     * Get all the fields for this object
     */
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = PoolSummary.getFieldContainer();

        // ID
        // Period
        // Pool
        // Currency ====================================================================================================
        fieldContainer.get(PoolSummary_Currency).setDataCore(new Method_DataCore<>(container -> ((Bank_Summary) container).getPool().getCurrency()));
        // Start =======================================================================================================
        fieldContainer.get(PoolSummary_Start).setDataCore(new Method_DataCore<>(container -> ((Bank_Summary) container).getStart_impl()));
        // End =========================================================================================================
        fieldContainer.get(PoolSummary_End).setDataCore(new Method_DataCore<>(container -> ((Bank_Summary) container).getEnd_impl()));
        // Net
        // TransferSum
        // Missing
        // Valid
        // NetTransfer =================================================================================================
        fieldContainer.add(new Tracking_DataField<>(Bank_Summary_NetTransfer, Double.class));
        fieldContainer.get(Bank_Summary_NetTransfer).getDisplayProperties().setDataType(CURRENCY);
        fieldContainer.get(Bank_Summary_NetTransfer).setDataCore(new Method_DataCore<>(container -> ((Bank_Summary) container).getNetTransfer_impl()));
        // Spend =======================================================================================================
        fieldContainer.add(new Tracking_DataField<>(Bank_Summary_Spend, Double.class));
        fieldContainer.get(Bank_Summary_Spend).getDisplayProperties().setDataType(CURRENCY);
        fieldContainer.get(Bank_Summary_Spend).setDataCore(new Method_DataCore<>(container -> ((Bank_Summary) container).getSpend_impl()));
        // Order =======================================================================================================
        fieldContainer.add(new Tracking_DataField<>(Bank_Summary_Order, Integer.class));
        fieldContainer.get(Bank_Summary_Order).getDisplayProperties().setVerbosityLevel(TRACE_DISPLAY);
        fieldContainer.get(Bank_Summary_Order).setDataCore(new Method_DataCore<>(container -> ((Bank_Summary) container).getPool().getOrder()));
        //==============================================================================================================
        // Parents
        // Children

        return fieldContainer.finaliseContainer(Bank_Summary.class);
    }

    /**
     * Create a new StatementEnd object
     */
    public static Bank_Summary make(Period period, Pool pool) {
        return assembleDataObject(Bank_Summary.getFieldContainer(), new Bank_Summary()
                , DataObject_Id, -1
                , PoolSummary_Period, period
                , PoolSummary_Pool, pool
        );
    }

    private Double getStart_impl() {
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

    private Double getEnd_impl() {
        return new TwoParent_Children_Set<>(StatementEnd.class, getPeriod(), getPool()).get().get(0).getEnd();
    }

    private Double getNetTransfer_impl() {
        return new PeriodPool_SumSet(BankTransfer.class, Bank.class, getPeriod(), getPool()).getTotal() / getCurrency().getToPrimary();
    }

    private Double getSpend_impl() {
        return new PeriodPool_SumSet(BankTransfer.class, SolidCategory.class, getPeriod(), getPool()).getTotal() / getCurrency().getToPrimary();
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public Double getNetTransfer() {
        return get(Bank_Summary_NetTransfer);
    }

    public Double getSpend() {
        return get(Bank_Summary_Spend);
    }

    public Integer getOrder() {
        return get(Bank_Summary_Order);
    }
}
