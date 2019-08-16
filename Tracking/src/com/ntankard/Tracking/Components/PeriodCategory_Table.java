package com.ntankard.Tracking.Components;

import com.ntankard.DynamicGUI.Util.Swing.Base.UpdatableJScrollPane;
import com.ntankard.DynamicGUI.Util.Updatable;
import com.ntankard.Tracking.DataBase.Core.Category;
import com.ntankard.Tracking.DataBase.Core.Period;
import com.ntankard.Tracking.DataBase.TrackingDatabase;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PeriodCategory_Table extends UpdatableJScrollPane {

    /**
     * GUI Objects
     */
    private JTable table;

    /**
     * The names of the above periods
     */
    private DefaultTableModel model;

    /**
     * The main database used to get the categories
     */
    private TrackingDatabase database;

    /**
     * The database to render
     */
    private List<Period> periods;

    /**
     * Constructor
     *
     * @param periods  The database to render
     * @param database The main database used to get the categories
     * @param master   The parent of this object to be notified if data changes
     */
    public PeriodCategory_Table(List<Period> periods, TrackingDatabase database, Updatable master) {
        super(master);
        this.periods = periods;
        this.database = database;
        createUIComponents();
        update();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        model = new DefaultTableModel();
        table = new JTable() {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);
                int rendererWidth = component.getPreferredSize().width;
                TableColumn tableColumn = getColumnModel().getColumn(column);
                tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
                return component;
            }
        };
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setAutoCreateRowSorter(true);

        this.setViewportView(table);
    }

    private void addRow(Period rowObject) {
        ArrayList<String> rowString = new ArrayList<>();
        DecimalFormat df2 = new DecimalFormat("#");

        rowString.add(rowObject.toString());

        // Add each column
        for (Category member : database.getCategories()) {
            String toAdd;
            double data = rowObject.getCategoryTotalYen(member, true);
            toAdd = df2.format(data);
            rowString.add(toAdd);
        }

        model.addRow(rowString.toArray());
    }

    /**
     * {@inheritDoc Bottom of the tree
     */
    @Override
    public void update() {
        model = new DefaultTableModel();

        if (periods != null && periods.size() != 0) {
            for (Period o : periods) {

                // One time extract the members
                if (model.getColumnCount() == 0) {
                    model.addColumn("Period");
                    database.getCategories().forEach(cat -> model.addColumn(cat.toString()));
                }

                // Add the row data
                addRow(o);
            }
        }

        table.setModel(model);
    }
}
