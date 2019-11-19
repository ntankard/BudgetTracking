package com.ntankard.Tracking.Dispaly.Frames.MainFrame.SummaryGraphs;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.DynamicGUI.Util.Update.UpdatableJPanel;
import com.ntankard.Tracking.DataBase.Core.Currency;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Summary.Period_Summary;
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

        String[] axisLabel = new String[TrackingDatabase.get().get(Period.class).size()];
        Period period = TrackingDatabase.get().get(Period.class).get(0).getFirst();
        for (int i = 0; i < TrackingDatabase.get().get(Period.class).size(); i++) {
            axisLabel[i] = period.toString();
            period = period.getNext();
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
        final XYSeriesCollection dataset = new XYSeriesCollection();

        for (Currency currency : TrackingDatabase.get().get(Currency.class)) {
            final XYSeries cur = new XYSeries(currency.getName());
            Period period = TrackingDatabase.get().get(Period.class).get(0).getFirst();
            for (int i = 0; i < TrackingDatabase.get().get(Period.class).size(); i++) {
                cur.add(i, period.getChildren(Period_Summary.class).get(0).getEndBalance(currency));
                period = period.getNext();
            }
            dataset.addSeries(cur);
        }

        final XYSeries total = new XYSeries("Total");
        Period period = TrackingDatabase.get().get(Period.class).get(0).getFirst();
        for (int i = 0; i < TrackingDatabase.get().get(Period.class).size(); i++) {
            total.add(i, period.getChildren(Period_Summary.class).get(0).getEndBalance());
            period = period.getNext();
        }
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
