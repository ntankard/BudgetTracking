package com.ntankard.Tracking.DataBase.Core.MoneyEvents.FundChargeTransfer;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Core.Pool.Fund;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.SupportObjects.Currency;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Set.MoneyEvent_Sets.ContainerCategory_Set;

import static com.ntankard.ClassExtension.DisplayProperties.DataType.CURRENCY;

@ClassExtensionProperties(includeParent = true)
public class TaxChargeTransfer extends FundChargeTransfer {

    /**
     * Constructor
     */
    public TaxChargeTransfer(Period source) {
        super(TrackingDatabase.get().getNextId(FundChargeTransfer.class),
                "Tax reserve",
                0.0,
                source,
                TrackingDatabase.get().getSpecialValue(Fund.class, Fund.TAX),
                TrackingDatabase.get().getDefault(Currency.class));
    }

    @Override
    @MemberProperties(verbosityLevel = MemberProperties.DEBUG_DISPLAY)
    @DisplayProperties(order = 20)
    public Class<?> getTypeClass() {
        return FundChargeTransfer.class;
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @Override
    @DisplayProperties(order = 4, dataType = CURRENCY)
    public Double getValue() {
        Period period = getSourceContainer();
        Category incomeCategory = TrackingDatabase.get().getSpecialValue(Category.class, Category.INCOME);

        double taxRate = TrackingDatabase.get().getTaxRate();
        double income = new ContainerCategory_Set(period, incomeCategory).getTotal();

        return -income * taxRate;
    }
}
