package com.ntankard.Tracking.Frames.Master;

import com.ntankard.ClassExtension.MemberClass;
import com.ntankard.DynamicGUI.Components.List.DynamicGUI_DisplayList;
import com.ntankard.DynamicGUI.Components.List.DynamicGUI_DisplayList.ListControl_Button;
import com.ntankard.DynamicGUI.Util.Swing.Base.UpdatableJPanel;
import com.ntankard.DynamicGUI.Util.Updatable;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Statement;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.CategoryTransfer;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.NonPeriodFundTransfer;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.PeriodTransfer;
import com.ntankard.Tracking.DataBase.TrackingDatabase;
import com.ntankard.Tracking.Frames.Statement_Frame;
import com.ntankard.Tracking.Frames.Swing.PeriodSummary.PeriodSummary;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.ntankard.ClassExtension.MemberProperties.ALWAYS_DISPLAY;
import static com.ntankard.DynamicGUI.Components.List.DynamicGUI_DisplayList.*;
import static com.ntankard.DynamicGUI.Components.List.DynamicGUI_DisplayList.ListControl_Button.EnableCondition.SINGLE;

public class PeriodPanel extends UpdatableJPanel {
    // Core Data
    private TrackingDatabase trackingDatabase;
    private Period core;

    // The data displayed (clone of the data in the database)
    private List<Statement> statement_list = new ArrayList<>();
    private List<CategoryTransfer> categoryTransfer_list = new ArrayList<>();
    private List<NonPeriodFundTransfer> nonPeriodFundTransfer_list = new ArrayList<>();
    private List<PeriodTransfer> periodTransfer_list = new ArrayList<>();

    // The GUI components
    private DynamicGUI_DisplayList<Statement> statement_panel;
    private PeriodSummary periodSummary_panel;
    private DynamicGUI_DisplayList<CategoryTransfer> categoryTransfer_panel;
    private DynamicGUI_DisplayList<NonPeriodFundTransfer> nonPeriodFundTransfer_panel;
    private DynamicGUI_DisplayList<PeriodTransfer> periodTransfer_panel;
    public JTabbedPane master_tPanel;

    /**
     * Constructor
     */
    public PeriodPanel(TrackingDatabase trackingDatabase, Period core, Updatable master) {
        super(master);
        this.trackingDatabase = trackingDatabase;
        this.core = core;
        createUIComponents();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setLayout(new BorderLayout());

        statement_panel = newIntractableTable(statement_list, new MemberClass(Statement.class), true, ALWAYS_DISPLAY, this);
        statement_panel.getMainPanel().setLocaleInspector(rowObject -> {
            Statement statement = (Statement) rowObject;
            if (statement.getIdBank().getCurrency().getId().equals("YEN")) {
                return Locale.JAPAN;
            }
            return Locale.US;
        });

        ListControl_Button manageStatementBtn = new ListControl_Button<>("Manage Statement", statement_panel, SINGLE, false);
        manageStatementBtn.addActionListener(e -> {
            List selected = statement_panel.getMainPanel().getSelectedItems();
            Statement_Frame.open(trackingDatabase, (Statement) selected.get(0), this);
        });
        statement_panel.addButton(manageStatementBtn);

        periodSummary_panel = new PeriodSummary(trackingDatabase, core, this);

        categoryTransfer_panel = DynamicGUI_DisplayList.newIntractableTable(categoryTransfer_list, new MemberClass(CategoryTransfer.class), false, true, ALWAYS_DISPLAY, new DynamicGUI_DisplayList.ElementController<CategoryTransfer>() {
            @Override
            public CategoryTransfer newElement() {
                String idCode = trackingDatabase.getNextPeriodTransferId();
                return new CategoryTransfer(core, idCode, trackingDatabase.getCategory("Unaccounted"), trackingDatabase.getCategory("Unaccounted"), trackingDatabase.getCurrency("YEN"), "", 0.0);
            }

            @Override
            public void deleteElement(CategoryTransfer toDel) {
                trackingDatabase.removeCategoryTransfer(toDel);
                notifyUpdate();
            }

            @Override
            public void addElement(CategoryTransfer newObj) {
                trackingDatabase.addCategoryTransfer(newObj);
                notifyUpdate();
            }
        }, this, trackingDatabase);
        periodTransfer_panel = DynamicGUI_DisplayList.newIntractableTable(periodTransfer_list, new MemberClass(PeriodTransfer.class), false, true, ALWAYS_DISPLAY, new DynamicGUI_DisplayList.ElementController<PeriodTransfer>() {
            @Override
            public PeriodTransfer newElement() {
                String idCode = trackingDatabase.getNextPeriodTransferId();
                return new PeriodTransfer(idCode, core, trackingDatabase.getPeriods().get(1), trackingDatabase.getCurrency("YEN"), trackingDatabase.getCategory("Unaccounted"), "", 0.0);
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
        }, this, trackingDatabase);
        nonPeriodFundTransfer_panel = DynamicGUI_DisplayList.newIntractableTable(nonPeriodFundTransfer_list, new MemberClass(NonPeriodFundTransfer.class), false, true, ALWAYS_DISPLAY, new DynamicGUI_DisplayList.ElementController<NonPeriodFundTransfer>() {
            @Override
            public NonPeriodFundTransfer newElement() {
                String idCode = trackingDatabase.getNextNonPeriodFundTransferId();
                return new NonPeriodFundTransfer(idCode, core, trackingDatabase.getNonPeriodFunds().get(0), trackingDatabase.getCategory("Unaccounted"), trackingDatabase.getCurrency("YEN"), "", 0.0);
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
        }, this, trackingDatabase);

        categoryTransfer_panel.getMainPanel().setLocaleInspector(rowObject -> {
            CategoryTransfer transfer = (CategoryTransfer) rowObject;
            if (transfer.getCurrency().getId().equals("YEN")) {
                return Locale.JAPAN;
            }
            return Locale.US;
        });
        periodTransfer_panel.getMainPanel().setLocaleInspector(rowObject -> {
            PeriodTransfer transfer = (PeriodTransfer) rowObject;
            if (transfer.getCurrency().getId().equals("YEN")) {
                return Locale.JAPAN;
            }
            return Locale.US;
        });
        nonPeriodFundTransfer_panel.getMainPanel().setLocaleInspector(rowObject -> {
            NonPeriodFundTransfer transfer = (NonPeriodFundTransfer) rowObject;
            if (transfer.getCurrency().getId().equals("YEN")) {
                return Locale.JAPAN;
            }
            return Locale.US;
        });

        categoryTransfer_panel.setNorth(new JLabel("Category Transfer"));
        periodTransfer_panel.setNorth(new JLabel("Period Transfer"));
        nonPeriodFundTransfer_panel.setNorth(new JLabel("Other Transfer"));

        JPanel summaryContainer = new JPanel(new GridBagLayout());
        GridBagConstraints summaryContainer_C = new GridBagConstraints();

        summaryContainer_C.fill = GridBagConstraints.BOTH;
        summaryContainer_C.weightx = 1;

        summaryContainer_C.weighty = 6;
        summaryContainer_C.gridwidth = 3;
        summaryContainer.add(periodSummary_panel, summaryContainer_C);

        summaryContainer_C.weighty = 1;
        summaryContainer_C.gridwidth = 1;
        summaryContainer_C.gridy = 1;

        summaryContainer.add(categoryTransfer_panel, summaryContainer_C);
        summaryContainer_C.gridx = 1;
        summaryContainer.add(periodTransfer_panel, summaryContainer_C);
        summaryContainer_C.gridx = 2;
        summaryContainer.add(nonPeriodFundTransfer_panel, summaryContainer_C);

        master_tPanel = new JTabbedPane();
        master_tPanel.addTab("Summary", summaryContainer);
        master_tPanel.addTab("Statements", statement_panel);

        this.add(master_tPanel, BorderLayout.CENTER);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void update() {
        int selected = master_tPanel.getSelectedIndex();

        statement_list.clear();
        categoryTransfer_list.clear();
        periodTransfer_list.clear();
        nonPeriodFundTransfer_list.clear();

        statement_list.addAll(core.getStatements());
        for (CategoryTransfer categoryTransfer : trackingDatabase.getCategoryTransfers()) {
            if (categoryTransfer.getSourceContainer().equals(core)) {
                categoryTransfer_list.add(categoryTransfer);
            }
        }
        for (PeriodTransfer periodTransfer : trackingDatabase.getPeriodTransfers()) {
            if (periodTransfer.getSourceContainer().equals(core) || periodTransfer.getDestinationContainer().equals(core)) {
                periodTransfer_list.add(periodTransfer);
            }
        }
        for (NonPeriodFundTransfer nonPeriodFundTransfer : trackingDatabase.getNonPeriodFundTransfers()) {
            if (nonPeriodFundTransfer.getSourceContainer().equals(core)) {
                nonPeriodFundTransfer_list.add(nonPeriodFundTransfer);
            }
        }

        statement_panel.update();
        categoryTransfer_panel.update();
        periodTransfer_panel.update();
        nonPeriodFundTransfer_panel.update();

        periodSummary_panel.update();

        master_tPanel.setSelectedIndex(selected);
    }
}
