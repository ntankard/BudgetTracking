package com.ntankard.Tracking.DataBase.Core.Pool;

import com.ntankard.CoreObject.FieldContainer;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.CurrencyBound;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.HasDefault;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Interface.Ordered;
import com.ntankard.Tracking.DataBase.Core.BaseObject.Tracking_DataField;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.StatementEnd;
import com.ntankard.Tracking.DataBase.Database.ObjectFactory;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Set.TwoParent_Children_Set;

import static com.ntankard.CoreObject.Field.Properties.Display_Properties.INFO_DISPLAY;

@ObjectFactory(builtObjects = {StatementEnd.class})
public class Bank extends Pool implements CurrencyBound, Ordered, HasDefault {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constructor ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static final String Bank_Currency = "getCurrency";
    public static final String Bank_Start = "getStart";
    public static final String Bank_Default = "isDefault";
    public static final String Bank_Order = "getOrder";

    /**
     * Get all the fields for this object
     */
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = Pool.getFieldContainer();

        // ID
        // Name
        // Currency ========================================================================================================
        fieldContainer.add(new Tracking_DataField<>(Bank_Currency, Currency.class));
        // Start ========================================================================================================
        fieldContainer.add(new Tracking_DataField<>(Bank_Start, Double.class));
        fieldContainer.get(Bank_Start).getDisplayProperties().setVerbosityLevel(INFO_DISPLAY);
        // Default ========================================================================================================
        fieldContainer.add(new Tracking_DataField<>(Bank_Default, Boolean.class));
        fieldContainer.get(Bank_Default).getDisplayProperties().setVerbosityLevel(INFO_DISPLAY);
        // Order ========================================================================================================
        fieldContainer.add(new Tracking_DataField<>(Bank_Order, Integer.class));
        fieldContainer.get(Bank_Order).getDisplayProperties().setVerbosityLevel(INFO_DISPLAY);
        //==============================================================================================================
        // Parents
        // Children

        return fieldContainer.finaliseContainer(Bank.class);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void add() {
        super.add();

        for (ExistingPeriod period : TrackingDatabase.get().get(ExistingPeriod.class)) {
            if (new TwoParent_Children_Set<>(StatementEnd.class, this, period).get().size() > 1) {
                throw new RuntimeException("More than 1 statement end");
            }
            if (new TwoParent_Children_Set<>(StatementEnd.class, this, period).get().size() == 0) {
                StatementEnd.make(TrackingDatabase.get().getNextId(), period, this, 0.0).add();
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @Override
    public Currency getCurrency() {
        return get(Bank_Currency);
    }

    public Double getStart() {
        return get(Bank_Start);
    }

    @Override
    public Boolean isDefault() {
        return get(Bank_Default);
    }

    @Override
    public Integer getOrder() {
        return get(Bank_Order);
    }
}
