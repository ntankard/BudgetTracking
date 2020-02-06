package com.ntankard.Tracking.Dispaly.Frames.MainFrame;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.DynamicGUI.Util.Update.UpdatableJPanel;
import com.ntankard.Tracking.DataBase.Core.Transfers.RecurringPayment.Fixed.FixedRecurringPayment;
import com.ntankard.Tracking.DataBase.Interface.Set.Full_Set;
import com.ntankard.Tracking.Dispaly.Util.ElementControllers.FixedRecurringPayment_ElementController;
import com.ntankard.Tracking.Dispaly.Util.Panels.DataObject_DisplayList;

import java.awt.*;

public class RecurringPaymentPanel extends UpdatableJPanel {

    /**
     * The main panel
     */
    private DataObject_DisplayList<FixedRecurringPayment> dataObject_displayList;

    /**
     * Constructor
     */
    public RecurringPaymentPanel(Updatable master) {
        super(master);
        createUIComponents();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setLayout(new BorderLayout());

        dataObject_displayList = new DataObject_DisplayList<>(FixedRecurringPayment.class, new Full_Set<>(FixedRecurringPayment.class), this);
        dataObject_displayList.addControlButtons(new FixedRecurringPayment_ElementController(this));

        this.add(dataObject_displayList, BorderLayout.CENTER);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void update() {
        dataObject_displayList.update();
    }
}
