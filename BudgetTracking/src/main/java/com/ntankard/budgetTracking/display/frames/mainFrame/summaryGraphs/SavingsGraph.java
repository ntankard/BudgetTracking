package com.ntankard.budgetTracking.display.frames.mainFrame.summaryGraphs;

import com.ntankard.budgetTracking.dataBase.core.Currency;
import com.ntankard.budgetTracking.dataBase.core.period.ExistingPeriod;
import com.ntankard.budgetTracking.dataBase.interfaces.summary.Period_Summary;
import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.dynamicGUI.gui.util.update.UpdatableJPanel;
import com.ntankard.javaObjectDatabase.database.Database;
import com.ntankard.javaObjectDatabase.util.set.Single_OneParent_Children_Set;
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

    // Core database
    private final Database database;

    /**
     * Constructor
     *
     * @param master The parent of this object to be notified if data changes
     */
    public SavingsGraph(Database database, Updatable master) {
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
                "Savings",
                "Period",
                "YEN",
                createDataset(),
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(xyLineChart);
        final XYPlot plot = xyLineChart.getXYPlot();
        plot.setRangeZeroBaselineVisible(true);

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.BLUE);
        renderer.setSeriesPaint(1, Color.YELLOW);
        renderer.setSeriesPaint(2, Color.RED);
        plot.setRenderer(renderer);

        String[] axisLabel = new String[database.get(ExistingPeriod.class).size()];
        int i = 0;
        for (ExistingPeriod period : database.get(ExistingPeriod.class)) {
            axisLabel[i++] = period.toString();
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

        for (Currency currency : database.get(Currency.class)) {
            final XYSeries cur = new XYSeries(currency.getName());
            int i = 0;
            for (ExistingPeriod period : database.get(ExistingPeriod.class)) {
                cur.add(i++, new Single_OneParent_Children_Set<>(Period_Summary.class, period).getItem().getBankEnd(currency));
            }
            dataset.addSeries(cur);
        }

        final XYSeries total = new XYSeries("Total");
        int i = 0;
        for (ExistingPeriod period : database.get(ExistingPeriod.class)) {
            total.add(i++, new Single_OneParent_Children_Set<>(Period_Summary.class, period).getItem().getBankEnd());
        }
        dataset.addSeries(total);

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
