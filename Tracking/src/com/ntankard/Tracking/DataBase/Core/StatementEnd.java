package com.ntankard.Tracking.DataBase.Core;

import com.ntankard.CoreObject.Field.DataCore.Derived_DataCore;
import com.ntankard.CoreObject.Field.DataCore.Derived_DataCore.DirectExternalSource;
import com.ntankard.CoreObject.Field.DataCore.ValueRead_DataCore;
import com.ntankard.CoreObject.FieldContainer;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.CurrencyBound;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.Ordered;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Tracking_DataField;
import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank;

import static com.ntankard.CoreObject.Field.Properties.Display_Properties.DataType.CURRENCY;
import static com.ntankard.CoreObject.Field.Properties.Display_Properties.INFO_DISPLAY;
import static com.ntankard.CoreObject.Field.Properties.Display_Properties.TRACE_DISPLAY;
import static com.ntankard.Tracking.DataBase.Core.Pool.Bank.Bank_Currency;

public class StatementEnd extends DataObject implements CurrencyBound, Ordered {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String StatementEnd_Period = "getPeriod";
    public static final String StatementEnd_Bank = "getBank";
    public static final String StatementEnd_End = "getEnd";
    public static final String StatementEnd_Currency = "getCurrency";
    public static final String StatementEnd_Order = "getOrder";

    /**
     * Get all the fields for this object
     */
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = DataObject.getFieldContainer();

        // ID
        // Period ======================================================================================================
        fieldContainer.add(new Tracking_DataField<>(StatementEnd_Period, ExistingPeriod.class));
        fieldContainer.get(StatementEnd_Period).getDisplayProperties().setVerbosityLevel(INFO_DISPLAY);
        // Bank ============================================================0============================================
        fieldContainer.add(new Tracking_DataField<>(StatementEnd_Bank, Bank.class));
        // End =========================================================================================================
        fieldContainer.add(new Tracking_DataField<>(StatementEnd_End, Double.class));
        fieldContainer.get(StatementEnd_End).getDisplayProperties().setDataType(CURRENCY);
        fieldContainer.get(StatementEnd_End).setDataCore(new ValueRead_DataCore<>(true));
        // Currency ====================================================================================================
        fieldContainer.add(new Tracking_DataField<>(StatementEnd_Currency, Currency.class));
        fieldContainer.get(StatementEnd_Currency).setDataCore(
                new Derived_DataCore<>(
                        new DirectExternalSource<>(fieldContainer.get(StatementEnd_Bank), Bank_Currency)));
        // Order =======================================================================================================
        fieldContainer.add(new Tracking_DataField<>(StatementEnd_Order, Integer.class));
        fieldContainer.get(StatementEnd_Order).getDisplayProperties().setVerbosityLevel(TRACE_DISPLAY);
        fieldContainer.<Integer>get(StatementEnd_Order).setDataCore(
                new Derived_DataCore<Integer, StatementEnd>
                        (coreObject -> coreObject.getBank().getOrder() + coreObject.getPeriod().getOrder() * 1000
                                , new Derived_DataCore.LocalSource<>(fieldContainer.get(StatementEnd_Bank))
                                , new Derived_DataCore.LocalSource<>(fieldContainer.get(StatementEnd_Period))));
        //==============================================================================================================
        // Parents
        // Children

        return fieldContainer.finaliseContainer(StatementEnd.class);
    }

    /**
     * Create a new StatementEnd object
     */
    public static StatementEnd make(Integer id, ExistingPeriod period, Bank bank, Double end) {
        return assembleDataObject(StatementEnd.getFieldContainer(), new StatementEnd()
                , DataObject_Id, id
                , StatementEnd_Period, period
                , StatementEnd_Bank, bank
                , StatementEnd_End, end
        );
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public ExistingPeriod getPeriod() {
        return get(StatementEnd_Period);
    }

    public Bank getBank() {
        return get(StatementEnd_Bank);
    }

    public Double getEnd() {
        return get(StatementEnd_End);
    }

    @Override
    public Currency getCurrency() {
        return get(StatementEnd_Currency);
    }

    @Override
    public Integer getOrder() {
        return get(StatementEnd_Order);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Setters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public void setEnd(Double value) {
        set(StatementEnd_End, value);
    }
}
