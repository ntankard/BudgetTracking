package com.ntankard.Tracking.Dispaly.Frames.MainFrame.SummaryGraphs;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.DynamicGUI.Util.Update.UpdatableJPanel;
import com.ntankard.Tracking.DataBase.Core.Period.ExistingPeriod;
import com.ntankard.Tracking.DataBase.Core.Pool.Category.SolidCategory;
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
import java.util.HashMap;
import java.util.Map;


public class CategoryGraph extends UpdatableJPanel {

    /**
     * Constructor
     *
     * @param master The parent of this object to be notified if data changes
     */
    public CategoryGraph(Updatable master) {
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
        //renderer.setSeriesPaint(0, Color.BLUE);
        //renderer.setSeriesPaint(1, Color.YELLOW);
        //renderer.setSeriesPaint(2, Color.RED);
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
        Map<SolidCategory, XYSeries> categories = new HashMap<>();

        for (SolidCategory solidCategory : TrackingDatabase.get().get(SolidCategory.class)) {
            categories.put(solidCategory, new XYSeries(solidCategory.toString()));
        }

        int i = 0;
        for (ExistingPeriod period : TrackingDatabase.get().get(ExistingPeriod.class)) {
            for (SolidCategory solidCategory : TrackingDatabase.get().get(SolidCategory.class)) {
                categories.get(solidCategory).add(i, new PeriodPool_SumSet(period, solidCategory).getTotal());
            }
            i++;
        }

        final XYSeriesCollection dataset = new XYSeriesCollection();
        for (SolidCategory solidCategory : TrackingDatabase.get().get(SolidCategory.class)) {
            dataset.addSeries(categories.get(solidCategory));
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
