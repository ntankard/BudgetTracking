package com.ntankard.Tracking.Dispaly.Frames.MainFrame.Periods.IndividualPeriod;

import com.ntankard.DynamicGUI.Util.Update.UpdatableJPanel;
import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Statement;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Currency;
import com.ntankard.Tracking.DataBase.Interface.Summary.PeriodTransaction_Summary;
import com.ntankard.Tracking.DataBase.TrackingDatabase;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.text.NumberFormat;

public class SummaryPanel extends UpdatableJPanel {

    // Core Data
    private Period core;

    // The GUI components
    private JTextField netMoney_txt;
    private JTextField savings_txt;
    private JLabel transferStatus_lbl;
    private JLabel periodLink_lbl;
    private JLabel missingSpend_lbl;

    /**
     * Constructor
     *
     * @param master The parent of this object to be notified if data changes
     */
    protected SummaryPanel(Period core, Updatable master) {
        super(master);
        this.core = core;
        createUIComponents();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();

        netMoney_txt = new JTextField("0");
        netMoney_txt.setEditable(false);

        savings_txt = new JTextField("0");
        savings_txt.setEditable(false);

        transferStatus_lbl = new JLabel();
        transferStatus_lbl.setBorder(new LineBorder(Color.BLACK));

        periodLink_lbl = new JLabel();
        periodLink_lbl.setBorder(new LineBorder(Color.BLACK));

        missingSpend_lbl = new JLabel();
        missingSpend_lbl.setBorder(new LineBorder(Color.BLACK));

        this.add(new JLabel("Net Money"));
        this.add(netMoney_txt);
        this.add(new JLabel("Savings"));
        this.add(savings_txt);
        this.add(periodLink_lbl);
        this.add(transferStatus_lbl);
        this.add(missingSpend_lbl);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void update() {
        NumberFormat formatter = TrackingDatabase.get().get(Currency.class, "YEN").getNumberFormat();
        Currency YEN = TrackingDatabase.get().get(Currency.class, "YEN");

        double savingMin = Double.MAX_VALUE;
        double savingMax = Double.MIN_VALUE;
        double netMin = Double.MAX_VALUE;
        double netMax = Double.MIN_VALUE;
        for (Period period : TrackingDatabase.get().get(Period.class)) {
            double saving = new PeriodTransaction_Summary(period).getSavings();
            if (saving > savingMax) {
                savingMax = saving;
            }
            if (saving < savingMin) {
                savingMin = saving;
            }
            double profit = period.getProfit();
            if (profit > netMax) {
                netMax = profit;
            }
            if (profit < netMin) {
                netMin = profit;
            }
        }

        double profit = core.getProfit();
        netMoney_txt.setText(formatter.format(profit * YEN.getToPrimary()));
        if (profit > 0.0) {
            int scale = getScale(profit, netMax);
            netMoney_txt.setBackground(new Color(scale, 255, scale));
        } else {
            int scale = getScale(profit, netMin);
            netMoney_txt.setBackground(new Color(255, scale, scale));
        }

        double saving = new PeriodTransaction_Summary(core).getSavings();
        savings_txt.setText(formatter.format(saving * YEN.getToPrimary()));
        if (saving > 0.0) {
            int scale = getScale(saving, savingMax);
            savings_txt.setBackground(new Color(scale, 255, scale));
        } else {
            int scale = getScale(saving, savingMin);
            savings_txt.setBackground(new Color(255, scale, scale));
        }

        // Check that all spends are accounted for
        boolean missing = false;
        for (Statement statement : core.<Statement>getChildren(Statement.class)) {
            if (statement.getMissingSpend() != 0) {
                missing = true;
            }
        }
        if (missing) {
            missingSpend_lbl.setText(" Spend Missing ");
            missingSpend_lbl.setForeground(Color.RED);
        } else {
            missingSpend_lbl.setText(" Spend ");
            missingSpend_lbl.setForeground(Color.GREEN);
        }

        // Check the this periods start balances all match the previous ones end
        Period last = TrackingDatabase.get().get(Period.class, core.getLastId());
        if (last == null) {
            periodLink_lbl.setText(" First ");
            periodLink_lbl.setForeground(Color.GREEN);
        } else {
            boolean match = true;
            for (Statement statement : core.<Statement>getChildren(Statement.class)) {
                String lastStatementId = statement.getIdBank().getId() + " " + last.getId();
                Statement lastStatement = last.getChildren(Statement.class, lastStatementId);

                if (!(lastStatement.getEnd().equals(statement.getStart()))) {
                    match = false;
                }

            }
            if (match) {
                periodLink_lbl.setText(" Start matches last End ");
                periodLink_lbl.setForeground(Color.GREEN);
            } else {
                periodLink_lbl.setText(" Start ,last End mismatch ");
                periodLink_lbl.setForeground(Color.RED);
            }
        }

        // Check that all transfers in and out are accounted for
        if (core.getTransferRate() == 0.0) {
            if (core.getAUDMissingTransfer() != 0.0 || core.getYENMissingTransfer() != 0.0) {
                if (core.getAUDMissingTransfer() != 0.0) {
                    transferStatus_lbl.setText(" Unresolved transfer AUD " + core.getAUDMissingTransfer() + " ");
                    transferStatus_lbl.setForeground(Color.RED);
                } else {
                    transferStatus_lbl.setText(" Unresolved transfer YEN " + core.getYENMissingTransfer() + " ");
                    transferStatus_lbl.setForeground(Color.RED);
                }
            } else {
                transferStatus_lbl.setText(" Valid transfer rate ");
                transferStatus_lbl.setForeground(Color.GREEN);
            }
        } else {
            if (core.getTransferRate() != 0.0 && (core.getTransferRate() < 60 || core.getTransferRate() > 80)) {
                transferStatus_lbl.setText(" Imposable transfer rate ");
                transferStatus_lbl.setForeground(Color.RED);
            } else {
                transferStatus_lbl.setText(" Valid transfer rate ");
                transferStatus_lbl.setForeground(Color.GREEN);
            }
        }
    }

    /**
     * Get the color value scaled against the range of the column
     *
     * @param value The value
     * @param range The max/min value
     * @return The color value scaled against the range of the column
     */
    private int getScale(double value, double range) {
        double offset = 20;
        return (int) (((255 - offset) * (range - value)) / range);
    }
}
