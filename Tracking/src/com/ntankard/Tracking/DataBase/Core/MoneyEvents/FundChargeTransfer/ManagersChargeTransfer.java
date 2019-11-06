package com.ntankard.Tracking.DataBase.Core.MoneyEvents.FundChargeTransfer;

import com.ntankard.ClassExtension.ClassExtensionProperties;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.Tracking.DataBase.Core.BaseObject.DataObject;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Fund;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.SupportObjects.Currency;
import com.ntankard.Tracking.DataBase.Core.SupportObjects.FundController;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;

import java.util.ArrayList;
import java.util.List;

@ClassExtensionProperties(includeParent = true)
public class ManagersChargeTransfer extends FundChargeTransfer {

    // My parents
    private FundController fundController;

    /**
     * Constructor
     */
    public ManagersChargeTransfer(Period source, Fund destination) {
        super(TrackingDatabase.get().getNextId(FundChargeTransfer.class),
                destination.getName() + " charge",
                -1.0,
                source,
                destination,
                TrackingDatabase.get().getDefault(Currency.class));
        this.fundController = source.getChildren(FundController.class).get(0);
    }

    /**
     * {@inheritDoc
     */
    @MemberProperties(verbosityLevel = MemberProperties.INFO_DISPLAY)
    @Override
    public Class<?> getTypeClass() {
        return FundChargeTransfer.class;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public List<DataObject> getParents() {
        List<DataObject> toReturn = new ArrayList<>(super.getParents());
        toReturn.add(fundController);
        return toReturn;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public Double getValue() {
        return fundController.getValue(this);
    }

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Getters #####################################################
    //------------------------------------------------------------------------------------------------------------------

    public FundController getFundController() {
        return fundController;
    }
}
