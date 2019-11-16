package com.ntankard.Tracking.Dispaly.Frames.MainFrame.Periods.IndividualPeriod;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.DynamicGUI.Util.Update.UpdatableJPanel;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.ClassExtension.ExtendedPeriod;
import com.ntankard.Tracking.DataBase.Interface.Summary.PeriodTransaction_Summary;

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

        missingSpend_lbl = new JLabel();
        missingSpend_lbl.setBorder(new LineBorder(Color.BLACK));

        this.add(new JLabel("Net Money"));
        this.add(netMoney_txt);
        this.add(new JLabel("Savings"));
        this.add(savings_txt);
        this.add(transferStatus_lbl);
        this.add(missingSpend_lbl);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void update() {
        NumberFormat formatter = TrackingDatabase.get().getDefault(Currency.class).getNumberFormat();
        Currency YEN = TrackingDatabase.get().getDefault(Currency.class);

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
            double profit = new ExtendedPeriod(period).getProfit();
            if (profit > netMax) {
                netMax = profit;
            }
            if (profit < netMin) {
                netMin = profit;
            }
        }

        double profit = new ExtendedPeriod(core).getProfit();
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
        if (new PeriodTransaction_Summary(core).isValidSpend()) {
            missingSpend_lbl.setText(" Spend ");
            missingSpend_lbl.setForeground(Color.GREEN);
        } else {
            missingSpend_lbl.setText(" Spend Missing ");
            missingSpend_lbl.setForeground(Color.RED);
        }

        // Check that all transfers in and out are accounted for
        if (new PeriodTransaction_Summary(core).isValidTransfer()) {
            transferStatus_lbl.setText(" Valid transfer rate ");
            transferStatus_lbl.setForeground(Color.GREEN);
        } else {
            transferStatus_lbl.setText(" Imposable transfer rate, or missing transfer ");
            transferStatus_lbl.setForeground(Color.RED);
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
