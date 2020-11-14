package com.ntankard.tracking.dispaly.frames.mainFrame.summaryGraphs;

import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.dynamicGUI.gui.util.update.UpdatableJPanel;
import com.ntankard.tracking.dataBase.core.period.ExistingPeriod;
import com.ntankard.tracking.dataBase.core.pool.category.SolidCategory;
import com.ntankard.javaObjectDatabase.database.TrackingDatabase;
import com.ntankard.tracking.dataBase.interfaces.set.extended.sum.PeriodPool_SumSet;
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
import java.util.List;
import java.util.Map;


public class SetCategoryGraph extends UpdatableJPanel {

    // Core database
    private final TrackingDatabase trackingDatabase;

    /**
     * Constructor
     *
     * @param master The parent of this object to be notified if data changes
     */
    public SetCategoryGraph(TrackingDatabase trackingDatabase, Updatable master) {
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

        JFreeChart xyLineChart = ChartFactory.createXYLineChart(
                "Category",
                "Period",
                "YEN",
                createDataset(),
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(xyLineChart);
        final XYPlot plot = xyLineChart.getXYPlot();

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        plot.setRenderer(renderer);

        String[] axisLabel = new String[trackingDatabase.get(ExistingPeriod.class).size()];
        int i = 0;
        for (ExistingPeriod period : trackingDatabase.get(ExistingPeriod.class)) {
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
        Map<Integer, XYSeries> categories = new HashMap<>();
        Map<Integer, List<SolidCategory>> setCategories = new HashMap<>();

        for (SolidCategory solidCategory : trackingDatabase.get(SolidCategory.class)) {
            if (!setCategories.containsKey(solidCategory.getSet())) {
                setCategories.put(solidCategory.getSet(), new ArrayList<>());
                categories.put(solidCategory.getSet(), new XYSeries(solidCategory.getSetName()));
            }
            setCategories.get(solidCategory.getSet()).add(solidCategory);
        }

        int i = 0;
        for (ExistingPeriod period : trackingDatabase.get(ExistingPeriod.class)) {
            for (Integer set : setCategories.keySet()) {
                XYSeries series = categories.get(set);
                List<SolidCategory> toSumCategories = setCategories.get(set);

                double sum = 0.0;
                for (SolidCategory solidCategory : toSumCategories) {
                    sum += new PeriodPool_SumSet(period, solidCategory).getTotal();
                }
                series.add(i, sum);
            }
            i++;
        }

        final XYSeriesCollection dataset = new XYSeriesCollection();
        for (Integer category : categories.keySet()) {
            dataset.addSeries(categories.get(category));
        }

        return dataset;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update() {
        createUIComponents();
    }
}
