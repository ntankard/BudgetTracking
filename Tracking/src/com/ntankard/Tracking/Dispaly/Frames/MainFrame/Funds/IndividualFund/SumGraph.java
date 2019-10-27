package com.ntankard.Tracking.Dispaly.Frames.MainFrame.Funds.IndividualFund;

import com.ntankard.DynamicGUI.Util.Update.Updatable;
import com.ntankard.DynamicGUI.Util.Update.UpdatableJPanel;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Fund;
import com.ntankard.Tracking.DataBase.Core.MoneyContainers.Period;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.FundChargeTransfer.FundChargeTransfer;
import com.ntankard.Tracking.DataBase.Core.MoneyEvents.PeriodFundTransfer;
import com.ntankard.Tracking.DataBase.Core.ReferenceTypes.Currency;
import com.ntankard.Tracking.DataBase.TrackingDatabase;
import com.ntankard.Tracking.Dispaly.Util.Set.MoneyEvent_Sets.ContainerContainerType_Set;
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
    private Fund core;

    /**
     * Constructor
     *
     * @param master The parent of this object to be notified if data changes
     */
    public SumGraph(Fund core, Updatable master) {
        super(master);
        this.core = core;
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
        // renderer.setSeriesPaint(0, Color.BLUE);
        // renderer.setSeriesPaint(1, Color.YELLOW);
        // renderer.setSeriesPaint(2, Color.RED);
        plot.setRenderer(renderer);

        String[] axisLabel = new String[TrackingDatabase.get().get(Period.class).size()];
        int i = 0;
        for (Period period : TrackingDatabase.get().get(Period.class)) {
            axisLabel[i] = period.getId();
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
        final XYSeries total = new XYSeries("Total");
        final XYSeries charge = new XYSeries("Charge");
        final XYSeries use = new XYSeries("Use");

        int i = 0;
        double chargeTotal = 0.0;
        double useTotal = 0.0;
        for (Period period : TrackingDatabase.get().get(Period.class)) {
            chargeTotal -= new ContainerContainerType_Set<>(period, core, FundChargeTransfer.class).getTotal();
            useTotal += new ContainerContainerType_Set<>(period, core, PeriodFundTransfer.class).getTotal();

            charge.add(i, -new ContainerContainerType_Set<>(period, core, FundChargeTransfer.class).getTotal());
            use.add(i, new ContainerContainerType_Set<>(period, core, PeriodFundTransfer.class).getTotal());
            total.add(i, chargeTotal + useTotal);

            i++;
        }

        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(charge);
        dataset.addSeries(use);
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
