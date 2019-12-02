package com.ntankard.Tracking.Dispaly.Frames.MainFrame.Periods.IndividualPeriod;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.DynamicGUI.Util.Update.UpdatableJPanel;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Summary.Period_Summary;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.text.NumberFormat;

public class SummaryPanel extends UpdatableJPanel {

    // Core Data
    private Period core;

    // The GUI components
    private JTextField netMoney_txt;
    private JTextField total_txt;
    private JTextField nonSave_txt;
    private JLabel isValid_lbl;

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

        total_txt = new JTextField("0");
        total_txt.setEditable(false);

        nonSave_txt = new JTextField("0");
        nonSave_txt.setEnabled(false);

        isValid_lbl = new JLabel();
        isValid_lbl.setBorder(new LineBorder(Color.BLACK));

        this.add(new JLabel("Net Money"));
        this.add(netMoney_txt);
        this.add(new JLabel("Total"));
        this.add(total_txt);
        this.add(new JLabel("Non Save"));
        this.add(nonSave_txt);
        this.add(isValid_lbl);
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
            double total = new Period_Summary(period).getTotal();
            if (total > savingMax) {
                savingMax = total;
            }
            if (total < savingMin) {
                savingMin = total;
            }
            double profit = new Period_Summary(period).getProfit();
            if (profit > netMax) {
                netMax = profit;
            }
            if (profit < netMin) {
                netMin = profit;
            }
        }

        Period_Summary period_summary = new Period_Summary(core);

        double profit = period_summary.getProfit();
        netMoney_txt.setText(formatter.format(profit * YEN.getToPrimary()));
        if (profit > 0.0) {
            int scale = getScale(profit, netMax);
            netMoney_txt.setBackground(new Color(scale, 255, scale));
        } else {
            int scale = getScale(profit, netMin);
            netMoney_txt.setBackground(new Color(255, scale, scale));
        }

        double total = period_summary.getTotal();
        total_txt.setText(formatter.format(total * YEN.getToPrimary()));
        if (total > 0.0) {
            int scale = getScale(total, savingMax);
            total_txt.setBackground(new Color(scale, 255, scale));
        } else {
            int scale = getScale(total, savingMin);
            total_txt.setBackground(new Color(255, scale, scale));
        }

        nonSave_txt.setText(formatter.format(period_summary.getNonSaveTotal() * YEN.getToPrimary()));

        // Check that all spends are accounted for
        if (period_summary.isValid()) {
            isValid_lbl.setText(" All Valid ");
            isValid_lbl.setForeground(Color.GREEN);
        } else {
            isValid_lbl.setText(" Invalid ");
            isValid_lbl.setForeground(Color.RED);
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
