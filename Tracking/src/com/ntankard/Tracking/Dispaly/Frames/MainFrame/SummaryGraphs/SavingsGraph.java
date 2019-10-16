package com.ntankard.Tracking.Dispaly.Frames.MainFrame.SummaryGraphs;

import com.ntankard.DynamicGUI.Util.Swing.Base.UpdatableJPanel;
import com.ntankard.DynamicGUI.Util.Updatable;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.TrackingDatabase;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;

public class SavingsGraph extends UpdatableJPanel {

    /**
     * Constructor
     *
     * @param master The parent of this object to be notified if data changes
     */
    public SavingsGraph(Updatable master) {
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
                "Savings",
                "Period",
                "YEN",
                createDataset(),
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(xyLineChart);
        final XYPlot plot = xyLineChart.getXYPlot();

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.BLUE);
        renderer.setSeriesPaint(1, Color.YELLOW);
        renderer.setSeriesPaint(2, Color.RED);
        plot.setRenderer(renderer);

        this.add(chartPanel, BorderLayout.CENTER);
    }

    /**
     * Generate the data
     *
     * @return The generated data
     */
    private XYDataset createDataset() {
        final XYSeries aud = new XYSeries("AUD");
        final XYSeries yen = new XYSeries("YEN");
        final XYSeries total = new XYSeries("Total");

        int i = -1;
        for (Period period : TrackingDatabase.get().getPeriods()) {
            aud.add(i, period.getEndBalance(TrackingDatabase.get().getCurrency("AUD")));
            yen.add(i, period.getEndBalance(TrackingDatabase.get().getCurrency("YEN")));
            total.add(i, period.getEndBalance());
            i++;
        }

        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(aud);
        dataset.addSeries(yen);
        dataset.addSeries(total);
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
