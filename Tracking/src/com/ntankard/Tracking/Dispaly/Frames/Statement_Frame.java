package com.ntankard.Tracking.Dispaly.Frames;

import com.ntankard.DynamicGUI.Containers.DynamicGUI_IntractableObject;
import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.DynamicGUI.Util.Update.UpdatableJPanel;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.Transfers.BankCategoryTransfer;
import com.ntankard.Tracking.DataBase.Core.Pool.Bank;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Set.ExtendedSets.ExtendedStatement_Set;
import com.ntankard.Tracking.DataBase.Interface.Set.PeriodPoolType_Set;
import com.ntankard.Tracking.Dispaly.Util.ElementControllers.BankCategoryTransfer_ElementController;
import com.ntankard.Tracking.Dispaly.Util.Panels.DataObject_DisplayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static com.ntankard.ClassExtension.MemberProperties.INFO_DISPLAY;

public class Statement_Frame extends UpdatableJPanel {

    // Core Data
    private Period period;
    private Bank bank;

    // The GUI components
    private DataObject_DisplayList<BankCategoryTransfer> transaction_panel;
    private DynamicGUI_IntractableObject statement_panel;

    /**
     * Create and open the period frame
     */
    public static void open(Period period, Bank bank, Updatable master) {
        JFrame _frame = new JFrame("Statement");
        _frame.setContentPane(new Statement_Frame(period, bank, master));
        _frame.pack();
        _frame.setVisible(true);

        _frame.repaint();
        _frame.setLocation(50, 100);
        _frame.toFront();
    }

    /**
     * Constructor
     */
    private Statement_Frame(Period period, Bank bank, Updatable master) {
        super(master);
        this.period = period;
        this.bank = bank;
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

        transaction_panel = new DataObject_DisplayList<>(BankCategoryTransfer.class, new PeriodPoolType_Set<>(period, bank, BankCategoryTransfer.class), this);
        transaction_panel.addControlButtons(new BankCategoryTransfer_ElementController(period, bank, this));

        JTabbedPane data_tPanel = new JTabbedPane();
        data_tPanel.addTab("Transactions", transaction_panel);
        this.add(data_tPanel, BorderLayout.CENTER);

        statement_panel = new DynamicGUI_IntractableObject<>(new ExtendedStatement_Set(period), this).setVerbosity(INFO_DISPLAY).setSources(TrackingDatabase.get());
        this.add(statement_panel, BorderLayout.EAST);
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
        transaction_panel.update();
        statement_panel.update();
    }
}
