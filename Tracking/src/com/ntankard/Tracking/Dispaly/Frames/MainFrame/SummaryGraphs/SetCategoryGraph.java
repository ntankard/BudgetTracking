package com.ntankard.Tracking.Dispaly.Frames.MainFrame.SummaryGraphs;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.DynamicGUI.Util.Update.UpdatableJPanel;
import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Pool.Category;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Set.Extended.Sum.PeriodPool_SumSet;
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

    /**
     * Constructor
     *
     * @param master The parent of this object to be notified if data changes
     */
    public SetCategoryGraph(Updatable master) {
        super(master);
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

        String[] axisLabel = new String[TrackingDatabase.get().get(ExistingPeriod.class).size()];
        int i = 0;
        for (ExistingPeriod period : TrackingDatabase.get().get(ExistingPeriod.class)) {
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
        Map<Integer, List<Category>> setCategories = new HashMap<>();

        for (Category category : TrackingDatabase.get().get(Category.class)) {
            if (!setCategories.containsKey(category.getSet())) {
                setCategories.put(category.getSet(), new ArrayList<>());
                categories.put(category.getSet(), new XYSeries(category.getSetName()));
            }
            setCategories.get(category.getSet()).add(category);
        }

        int i = 0;
        for (ExistingPeriod period : TrackingDatabase.get().get(ExistingPeriod.class)) {
            for (Integer set : setCategories.keySet()) {
                XYSeries series = categories.get(set);
                List<Category> toSumCategories = setCategories.get(set);

                double sum = 0.0;
                for (Category category : toSumCategories) {
                    sum += new PeriodPool_SumSet(period, category).getTotal();
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
