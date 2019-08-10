package com.ntankard.Tracking.Frames;

import com.ntankard.ClassExtension.MemberClass;
import com.ntankard.DynamicGUI.Components.List.DynamicGUI_DisplayList;
import com.ntankard.DynamicGUI.Util.Swing.Base.UpdatableJPanel;
import com.ntankard.DynamicGUI.Util.Updatable;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Statement;
import com.ntankard.Tracking.DataBase.TrackingDatabase;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.ALWAYS_DISPLAY;
import static com.ntankard.DynamicGUI.Components.List.DynamicGUI_DisplayList.ListControl_Button.EnableCondition.SINGLE;

public class Period_Frame extends UpdatableJPanel {

    // Core Data
    private TrackingDatabase trackingDatabase;
    private Period core;

    // The data displayed (clone of the data in the database)
    private List<Statement> statement_list = new ArrayList<>();

    // The GUI components
    private DynamicGUI_DisplayList<Statement> statement_panel;
    private DynamicGUI_DisplayList.ListControl_Button setRecord;

    /**
     * Create and open the period frame
     *
     * @param trackingDatabase The master database
     * @param core             The Period this panel is built around
     * @param master           The parent of this frame
     */
    public static void open(TrackingDatabase trackingDatabase, Period core, Updatable master) {
        JFrame _frame = new JFrame("Period");
        _frame.setContentPane(new Period_Frame(trackingDatabase, core, master));
        _frame.pack();
        _frame.setVisible(true);
        _frame.setLocation(0, 50);

        _frame.repaint();

        //setRecord.doClick();
    }

    /**
     * Constructor
     *
     * @param trackingDatabase The master database
     * @param core             The Period this panel is built around
     * @param master           The parent of this frame
     */
    private Period_Frame(TrackingDatabase trackingDatabase, Period core, Updatable master) {
        super(master);
        this.trackingDatabase = trackingDatabase;
        this.core = core;
        createUIComponents();
        update();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setPreferredSize(new Dimension(1800, 800));
        this.setBorder(new EmptyBorder(12, 12, 12, 12));
        this.setLayout(new BorderLayout());

        statement_panel = DynamicGUI_DisplayList.newIntractableTable(statement_list, new MemberClass(Statement.class), true, ALWAYS_DISPLAY, this);

        setRecord = new DynamicGUI_DisplayList.ListControl_Button<>("Manage Period", statement_panel, SINGLE, false);
        setRecord.addActionListener(e -> {
            List selected = statement_panel.getMainPanel().getSelectedItems();
            Statement_Frame.open(trackingDatabase, (Statement) selected.get(0), this);
        });
        statement_panel.addButton(setRecord);

        this.add(statement_panel, BorderLayout.CENTER);
    }

    /**
     * Short circuit to prevent the top level panel getting effected
     */
    @Override
    public void notifyUpdate() {
        super.notifyUpdate();
        SwingUtilities.invokeLater(this::update);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void update() {
        statement_list.clear();
        statement_list.addAll(core.getStatements());

        statement_panel.update();

        statement_panel.getMainPanel().getListSelectionModel().setSelectionInterval(0, 0);
    }
}
