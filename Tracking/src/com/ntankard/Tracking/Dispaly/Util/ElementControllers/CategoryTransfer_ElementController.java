package com.ntankard.Tracking.Dispaly.Util.ElementControllers;

import com.ntankard.DynamicGUI.Components.List.DynamicGUI_DisplayList.ElementController;
import com.ntankard.DynamicGUI.Util.Updatable;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.CategoryTransfer;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.PeriodTransfer;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Category;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Currency;
import com.ntankard.Tracking.DataBase.TrackingDatabase;

public class CategoryTransfer_ElementController implements ElementController<CategoryTransfer> {

    /**
     * Data to use when creating a new object
     */
    private Period period;

    /**
     * The container to notify if the buttons make a database change
     */
    private Updatable master;

    /**
     * Constructor
     */
    public CategoryTransfer_ElementController(Period period, Updatable master) {
        this.period = period;
        this.master = master;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public CategoryTransfer newElement() {
        String idCode = TrackingDatabase.get().getNextId(CategoryTransfer.class);
        return new CategoryTransfer(
                period,
                idCode,
                TrackingDatabase.get().get(Category.class,"Unaccounted"),
                TrackingDatabase.get().get(Category.class,"Unaccounted"),
                TrackingDatabase.get().get(Currency.class, "YEN"),
                "",
                0.0);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void deleteElement(CategoryTransfer toDel) {
        TrackingDatabase.get().remove(toDel);
        master.notifyUpdate();
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void addElement(CategoryTransfer newObj) {
        TrackingDatabase.get().add(newObj);
        master.notifyUpdate();
    }
}
