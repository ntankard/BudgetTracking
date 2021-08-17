package com.ntankard.budgetTracking.dispaly.frames.mainFrame.summaryGraphs;

import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.dynamicGUI.gui.util.update.UpdatableJPanel;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.budgetTracking.dataBase.core.period.ExistingPeriod;
import com.ntankard.budgetTracking.dataBase.core.pool.category.Category;
import com.ntankard.budgetTracking.dataBase.core.pool.fundEvent.FundEvent;
import com.ntankard.budgetTracking.dataBase.core.transfer.fund.rePay.FixedPeriodRePayFundTransfer;
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
import java.util.HashMap;
import java.util.Map;

public class HolidayGraph extends UpdatableJPanel {

    // Core database
    private final Database database;

    /**
     * Constructor
     *
     * @param master The parent of this object to be notified if data changes
     */
    public HolidayGraph(Database database, Updatable master) {
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
        Map<Integer, FundEvent> setFundGroups = new HashMap<>();

        int j = 0;
        for (FundEvent fundEvent : database.get(FundEvent.class)) {
            if (fundEvent.getCategory().getName().equals("Holiday")) {
                setFundGroups.put(j, fundEvent);
                fundGroups.put(j, new XYSeries(fundEvent.getName()));
                j++;
            }
        }
        fundGroups.put(j, new XYSeries("Sum"));

        int periodIndex = 0;
        for (ExistingPeriod period : database.get(ExistingPeriod.class)) {
            double totalSum = 0.0;
            for (int i = 0; i < setFundGroups.size(); i++) {

                double sum = -new PeriodPool_SumSet(FixedPeriodRePayFundTransfer.class, Category.class, period, setFundGroups.get(i)).getTotal();
                fundGroups.get(i).add(periodIndex, sum);
                totalSum += sum;
            }
            fundGroups.get(setFundGroups.size()).add(periodIndex, totalSum);
            periodIndex++;
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
