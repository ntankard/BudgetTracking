package com.ntankard.Tracking.Frames.Master;

import com.ntankard.ClassExtension.MemberClass;
import com.ntankard.DynamicGUI.Components.List.DynamicGUI_DisplayList;
import com.ntankard.DynamicGUI.Components.List.DynamicGUI_DisplayList.ElementController;
import com.ntankard.DynamicGUI.Util.Swing.Base.UpdatableJPanel;
import com.ntankard.DynamicGUI.Util.Updatable;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.CategoryTransfer;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.NonPeriodFundTransfer;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.PeriodTransfer;
import com.ntankard.Tracking.DataBase.TrackingDatabase;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.ntankard.ClassExtension.MemberProperties.ALWAYS_DISPLAY;

public class TransferPanel extends UpdatableJPanel {
    // Core Data
    private TrackingDatabase trackingDatabase;

    // The data displayed (clone of the data in the database)
    private List<CategoryTransfer> categoryTransfer_list = new ArrayList<>();
    private List<PeriodTransfer> periodTransfer_list = new ArrayList<>();
    private List<NonPeriodFundTransfer> nonPeriodFundTransfer_list = new ArrayList<>();

    // The GUI components
    private DynamicGUI_DisplayList<CategoryTransfer> categoryTransfer_panel;
    private DynamicGUI_DisplayList<PeriodTransfer> periodTransfer_panel;
    private DynamicGUI_DisplayList<NonPeriodFundTransfer> nonPeriodFundTransfer_panel;

    /**
     * Constructor
     */
    public TransferPanel(TrackingDatabase trackingDatabase, Updatable master) {
        super(master);
        this.trackingDatabase = trackingDatabase;
        createUIComponents();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setLayout(new BorderLayout());

        // Panel controllers
        ElementController<NonPeriodFundTransfer> nonPeriodFundTransferController = new ElementController<NonPeriodFundTransfer>() {
            @Override
            public NonPeriodFundTransfer newElement() {
                String idCode = trackingDatabase.getNextNonPeriodFundTransferId();
                return new NonPeriodFundTransfer(idCode, trackingDatabase.getPeriods().get(0), trackingDatabase.getNonPeriodFunds().get(0), trackingDatabase.getCategory("Unaccounted"), trackingDatabase.getCurrency("YEN"), "", 0.0);
            }

            @Override
            public void deleteElement(NonPeriodFundTransfer toDel) {
                trackingDatabase.removeNonPeriodFundTransfer(toDel);
                notifyUpdate();
            }

            @Override
            public void addElement(NonPeriodFundTransfer newObj) {
                trackingDatabase.addNonPeriodFundTransfer(newObj);
                notifyUpdate();
            }
        };
        ElementController<PeriodTransfer> periodTransferController = new ElementController<PeriodTransfer>() {
            @Override
            public PeriodTransfer newElement() {
                String idCode = trackingDatabase.getNextPeriodTransferId();
                return new PeriodTransfer(idCode, trackingDatabase.getPeriods().get(0), trackingDatabase.getPeriods().get(1), trackingDatabase.getCurrency("YEN"), trackingDatabase.getCategory("Unaccounted"), "", 0.0);
            }

            @Override
            public void deleteElement(PeriodTransfer toDel) {
                trackingDatabase.removePeriodTransfer(toDel);
                notifyUpdate();
            }

            @Override
            public void addElement(PeriodTransfer newObj) {
                trackingDatabase.addPeriodTransfer(newObj);
                notifyUpdate();
            }
        };

        // The GUI components
        categoryTransfer_panel = DynamicGUI_DisplayList.newIntractableTable(categoryTransfer_list, new MemberClass(CategoryTransfer.class), true, ALWAYS_DISPLAY, this);
        periodTransfer_panel = DynamicGUI_DisplayList.newIntractableTable(periodTransfer_list, new MemberClass(PeriodTransfer.class), true, true, ALWAYS_DISPLAY, periodTransferController, this, trackingDatabase);
        nonPeriodFundTransfer_panel = DynamicGUI_DisplayList.newIntractableTable(nonPeriodFundTransfer_list, new MemberClass(NonPeriodFundTransfer.class), true, true, ALWAYS_DISPLAY, nonPeriodFundTransferController, this, trackingDatabase);

        categoryTransfer_panel.getMainPanel().setLocaleInspector(rowObject -> {
            CategoryTransfer transfer = (CategoryTransfer) rowObject;
            if (transfer.getCurrency().getId().equals("YEN")) {
                return Locale.JAPAN;
            }
            return Locale.US;
        });
        periodTransfer_panel.getMainPanel().setLocaleInspector(rowObject -> {
            PeriodTransfer transaction = (PeriodTransfer) rowObject;
            if (transaction.getCurrency().getId().equals("YEN")) {
                return Locale.JAPAN;
            }
            return Locale.US;
        });
        nonPeriodFundTransfer_panel.getMainPanel().setLocaleInspector(rowObject -> {
            NonPeriodFundTransfer transaction = (NonPeriodFundTransfer) rowObject;
            if (transaction.getCurrency().getId().equals("YEN")) {
                return Locale.JAPAN;
            }
            return Locale.US;
        });

        JTabbedPane master_tPanel = new JTabbedPane();
        master_tPanel.addTab("Category Transfer", categoryTransfer_panel);
        master_tPanel.addTab("Period Transfer", periodTransfer_panel);
        master_tPanel.addTab("Non Period Fund Transfer", nonPeriodFundTransfer_panel);

        this.add(master_tPanel, BorderLayout.CENTER);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void update() {
        categoryTransfer_list.clear();
        periodTransfer_list.clear();
        nonPeriodFundTransfer_list.clear();

        categoryTransfer_list.addAll(trackingDatabase.getCategoryTransfers());
        periodTransfer_list.addAll(trackingDatabase.getPeriodTransfers());
        nonPeriodFundTransfer_list.addAll(trackingDatabase.getNonPeriodFundTransfers());

        categoryTransfer_panel.update();
        periodTransfer_panel.update();
        nonPeriodFundTransfer_panel.update();
    }
}
