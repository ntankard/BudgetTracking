package com.ntankard.Tracking.Dispaly.Frames.MainFrame.Funds.IndividualFund;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.DynamicGUI.Util.Update.UpdatableJPanel;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.Core.Pool.Fund.Fund;
import com.ntankard.Tracking.DataBase.Core.Pool.Fund.FundEvent.FundEvent;
import com.ntankard.Tracking.DataBase.Database.TrackingDatabase;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.FundEvent_Summary;
import com.ntankard.Tracking.DataBase.Interface.Summary.Pool.Fund_Summary;
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

public class SumGraph extends UpdatableJPanel {

    // Core Data
    private Fund fund;

    /**
     * Constructor
     *
     * @param master The parent of this object to be notified if data changes
     */
    public SumGraph(Fund fund, Updatable master) {
        super(master);
        this.fund = fund;
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

        String[] axisLabel = new String[TrackingDatabase.get().get(Period.class).size()];
        int i = 0;
        for (Period period : TrackingDatabase.get().get(Period.class)) {
            axisLabel[i] = period.toString();
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
        Map<FundEvent, XYSeries> categories = new HashMap<>();

        for (FundEvent fundEvent : fund.getChildren(FundEvent.class)) {
            categories.put(fundEvent, new XYSeries(fundEvent.toString()));
        }
        XYSeries total = new XYSeries("Total");

        int i = 0;
        for (Period period : TrackingDatabase.get().get(Period.class)) {
            for (FundEvent fundEvent : fund.getChildren(FundEvent.class)) {
                if (fundEvent.isActiveThisPeriod(period)) {
                    categories.get(fundEvent).add(i, new FundEvent_Summary(period, fundEvent).getEnd());
                }
            }
            total.add(i, new Fund_Summary(period, fund).getEnd());
            i++;
        }

        final XYSeriesCollection dataset = new XYSeriesCollection();
        for (FundEvent fundEvent : fund.getChildren(FundEvent.class)) {
            dataset.addSeries(categories.get(fundEvent));
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
