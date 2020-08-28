package com.ntankard.Tracking.Dispaly.Frames.MainFrame.Funds.IndividualFund;

import com.ntankard.Tracking.DataBase.Interface.Set.OneParent_Children_Set;
import com.ntankard.dynamicGUI.Gui.Util.Update.Updatable;
import com.ntankard.dynamicGUI.Gui.Util.Update.UpdatableJPanel;
import com.ntankard.Tracking.DataBase.Core.Pool.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.FundEvent_Summary;
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

public class SumGraph extends UpdatableJPanel {

    // Core Data
    private FundEvent fundEvent;

    /**
     * Constructor
     *
     * @param master The parent of this object to be notified if data changes
     */
    public SumGraph(FundEvent fundEvent, Updatable master) {
        super(master);
        this.fundEvent = fundEvent;
        createUIComponents();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setLayout(new BorderLayout());

        JFreeChart xyLineChart = ChartFactory.createXYLineChart(
                "Rolling Balance",
                "Period",
                "YEN",
                createDataset(),
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(xyLineChart);
        final XYPlot plot = xyLineChart.getXYPlot();

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        plot.setRenderer(renderer);

        String[] axisLabel = new String[new OneParent_Children_Set<>(FundEvent_Summary.class, fundEvent).get().size()];
        int i = 0;
        for (FundEvent_Summary fundEvent_summary : new OneParent_Children_Set<>(FundEvent_Summary.class, fundEvent).get()) {
            axisLabel[i] = fundEvent_summary.getPeriod().toString();
            i++;
        }

        plot.setDomainAxis(new SymbolAxis("Period", axisLabel));

        this.add(chartPanel, BorderLayout.CENTER);
    }

    /**
     * Generate the data
     *
     * @return The generated data
     */
    private XYDataset createDataset() {
        XYSeries total = new XYSeries("Total");

        int i = 0;
        for (FundEvent_Summary fundEvent_summary : new OneParent_Children_Set<>(FundEvent_Summary.class, fundEvent).get()) {
            total.add(i, fundEvent_summary.getEnd());
            i++;
        }

        final XYSeriesCollection dataset = new XYSeriesCollection();
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
