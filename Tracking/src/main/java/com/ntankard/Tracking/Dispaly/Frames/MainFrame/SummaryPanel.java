package com.ntankard.Tracking.Dispaly.Frames.MainFrame;

import com.ntankard.Tracking.DataBase.Interface.Set.Full_Set;
import com.ntankard.Tracking.DataBase.Interface.Set.OneParent_Children_Set;
import com.ntankard.dynamicGUI.Gui.Util.Update.Updatable;
import com.ntankard.dynamicGUI.Gui.Util.Update.UpdatableJPanel;
import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Period.Period;
import com.ntankard.javaObjectDatabase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Summary.Period_Summary;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.Bank_Summary;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.Category_Summary;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.FundEvent_Summary;
import com.ntankard.Tracking.Dispaly.Util.Panels.DataObject_VerbosityDisplayList;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SummaryPanel extends UpdatableJPanel {

    private DataObject_VerbosityDisplayList<Period_Summary> periodSummary_panel;
    private final List<DataObject_VerbosityDisplayList<Bank_Summary>> bankSummary_panels = new ArrayList<>();
    private final List<DataObject_VerbosityDisplayList<Category_Summary>> categorySummary_panels = new ArrayList<>();
    private final List<DataObject_VerbosityDisplayList<FundEvent_Summary>> fundEventSummary_panels = new ArrayList<>();

    /**
     * Constructor
     *
     * @param master The parent of this object to be notified if data changes
     */
    protected SummaryPanel(Updatable master) {
        super(master);
        createUIComponents();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setLayout(new BorderLayout());

        periodSummary_panel = new DataObject_VerbosityDisplayList<>(Period_Summary.class, new Full_Set<>(Period_Summary.class), this);

        JTabbedPane bank_tPanel = new JTabbedPane();
        JTabbedPane category_tPanel = new JTabbedPane();
        JTabbedPane fundEvent_tPanel = new JTabbedPane();
        for (Period period : TrackingDatabase.get().get(ExistingPeriod.class)) { // TODO changed here to only do exiting, change back
            if (period instanceof ExistingPeriod) {
                DataObject_VerbosityDisplayList<Bank_Summary> bankPanel = new DataObject_VerbosityDisplayList<>(Bank_Summary.class, new OneParent_Children_Set<>(Bank_Summary.class, (ExistingPeriod) period), this);
                bankSummary_panels.add(bankPanel);
                bank_tPanel.addTab(period.toString(), bankPanel);
            }

            DataObject_VerbosityDisplayList<Category_Summary> categoryPanel = new DataObject_VerbosityDisplayList<>(Category_Summary.class, new OneParent_Children_Set<>(Category_Summary.class, period), this);
            categorySummary_panels.add(categoryPanel);
            category_tPanel.addTab(period.toString(), categoryPanel);

            DataObject_VerbosityDisplayList<FundEvent_Summary> fundEventPanel = new DataObject_VerbosityDisplayList<>(FundEvent_Summary.class, new OneParent_Children_Set<>(FundEvent_Summary.class, period), this);
            fundEventSummary_panels.add(fundEventPanel);
            fundEvent_tPanel.addTab(period.toString(), fundEventPanel);
        }

        JTabbedPane master_tPanel = new JTabbedPane();
        master_tPanel.addTab("Period", periodSummary_panel);
        master_tPanel.addTab("Bank", bank_tPanel);
        master_tPanel.addTab("Category", category_tPanel);
        master_tPanel.addTab("Fund Event", fundEvent_tPanel);

        this.add(master_tPanel, BorderLayout.CENTER);
    }

    @Override
    public void update() {
        periodSummary_panel.update();
        for (Updatable updatable : bankSummary_panels) {
            updatable.update();
        }
        for (Updatable updatable : categorySummary_panels) {
            updatable.update();
        }
        for (Updatable updatable : fundEventSummary_panels) {
            updatable.update();
        }
    }
}
