package com.ntankard.budgetTracking.display.dataObjectPanels.periodSummary;

import com.ntankard.dynamicGUI.gui.util.table.TableColumnAdjuster;
import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.dynamicGUI.gui.util.update.UpdatableJPanel;
import com.ntankard.budgetTracking.dataBase.core.period.Period;
import com.ntankard.budgetTracking.dataBase.core.pool.Pool;

import javax.swing.*;
import java.awt.*;

public class PeriodSummary_Table<P extends Pool> extends UpdatableJPanel {

    // Core Data
    private Period core;
    private boolean addTransfers;
    private Class<P> columnClass;

    /**
     * The model driving the table
     */
    private PeriodSummary_Model<P> model;

    /**
     * TableColumnAdjuster used to shrink the table
     */
    private TableColumnAdjuster tableColumnAdjuster;

    /**
     * Constructor
     */
    public PeriodSummary_Table(Period core, boolean addTransfers, Class<P> columnClass, Updatable master) {
        super(master);
        this.core = core;
        this.addTransfers = addTransfers;
        this.columnClass = columnClass;
        createUIComponents();
        update();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setLayout(new BorderLayout());
        model = new PeriodSummary_Model<P>(core, addTransfers, columnClass);

        JTable table = new JTable(model);

        // Allow custom borders in the render
        table.setDefaultRenderer(Object.class, new PeriodSummary_Renderer());
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        // Shrink the table to minimum size
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableColumnAdjuster = new TableColumnAdjuster(table);

        this.add(table, BorderLayout.CENTER);
    }

    /**
     * Get the model driving the table
     *
     * @return The model driving the table
     */
    public PeriodSummary_Model<P> getModel() {
        return model;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void update() {
        model.update();
        //tableColumnAdjuster.adjustColumns();
    }
}
