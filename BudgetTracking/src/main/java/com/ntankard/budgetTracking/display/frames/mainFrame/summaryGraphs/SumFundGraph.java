package com.ntankard.budgetTracking.display.frames.mainFrame.summaryGraphs;

import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.dynamicGUI.gui.util.update.UpdatableJPanel;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.budgetTracking.dataBase.core.period.ExistingPeriod;
import com.ntankard.budgetTracking.dataBase.core.pool.fundEvent.FundEvent;
import com.ntankard.budgetTracking.dataBase.core.pool.fundEvent.SavingsFundEvent;
import com.ntankard.budgetTracking.dataBase.interfaces.set.extended.sum.PeriodPool_SumSet;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SumFundGraph extends UpdatableJPanel {

    // Core database
    private final Database database;

    /**
     * Constructor
     *
     * @param master The parent of this object to be notified if data changes
     */
    public SumFundGraph(Database database, Updatable master) {
        super(master);
        this.database = database;
        createUIComponents();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setLayout(new BorderLayout());

        JFreeChart xyLineChart = ChartFactory.createXYLineChart(
                "Fund",
                "Period",
                "Fund",
                createDataset(),
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(xyLineChart);
        final XYPlot plot = xyLineChart.getXYPlot();
        plot.setRangeZeroBaselineVisible(true);

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        plot.setRenderer(renderer);

        String[] axisLabel = new String[database.get(ExistingPeriod.class).size()];
        int i = 0;
        for (ExistingPeriod period : database.get(ExistingPeriod.class)) {
            axisLabel[i] = period.toString();
            i++;
        }
        SymbolAxis sa = new SymbolAxis("Period", axisLabel);
        plot.setDomainAxis(sa);

        this.add(chartPanel, BorderLayout.CENTER);
    }

    /**
     * Generate the data
     *
     * @return The generated data
     */
    private XYDataset createDataset() {
        Map<Integer, XYSeries> fundGroups = new HashMap<>();
        Map<Integer, java.util.List<FundEvent>> setFundGroups = new HashMap<>();

        setFundGroups.put(0, new ArrayList<>());
        setFundGroups.put(1, new ArrayList<>());
        setFundGroups.put(2, new ArrayList<>());
        setFundGroups.put(3, new ArrayList<>());

        fundGroups.put(0, new XYSeries("Savings"));
        fundGroups.put(1, new XYSeries("Tax"));
        fundGroups.put(2, new XYSeries("Other"));
        fundGroups.put(3, new XYSeries("Sum"));

        for (FundEvent fundEvent : database.get(FundEvent.class)) {
            if (fundEvent instanceof SavingsFundEvent) {
                setFundGroups.get(0).add(fundEvent);
            } else if (fundEvent.getName().equals("Hex") || fundEvent.getName().equals("Hex2") || fundEvent.getName().equals("19-20 Tax") || fundEvent.getName().equals("18-19 Tax")) {
                setFundGroups.get(1).add(fundEvent);
            } else {
                setFundGroups.get(2).add(fundEvent);
            }
        }
        Double[] num = new Double[4];
        for (int i = 0; i < 4; i++) {
            num[i] = 0.0;
        }

        int i = 0;
        for (ExistingPeriod period : database.get(ExistingPeriod.class)) {
            double totalSum = 0.0;
            for (int set = 0; set < 3; set++) {
                double sum = num[set];
                for (FundEvent fundEvent : setFundGroups.get(set)) {
                    sum += -new PeriodPool_SumSet(period, fundEvent).getTotal();
                }
                totalSum += sum;
                num[set] = sum;
                fundGroups.get(set).add(i, sum);
            }
            fundGroups.get(3).add(i, totalSum);
            i++;
        }

        final XYSeriesCollection dataset = new XYSeriesCollection();
        for (Integer category : fundGroups.keySet()) {
            dataset.addSeries(fundGroups.get(category));
        }

        return dataset;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void update() {
        createUIComponents();
    }
}
