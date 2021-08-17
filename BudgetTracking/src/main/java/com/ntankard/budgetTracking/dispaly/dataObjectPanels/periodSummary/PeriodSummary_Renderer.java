package com.ntankard.budgetTracking.dispaly.dataObjectPanels.periodSummary;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class PeriodSummary_Renderer extends DefaultTableCellRenderer {

    public static class RendererObject {
        public Object dataObject = "";

        public int top = 0;
        public int left = 0;
        public int bottom = 0;
        public int right = 0;

        public Color foreground = null;
        public Color background = null;

        public boolean isBold = false;

        @Override
        public String toString() {
            if (dataObject != null) {
                return dataObject.toString();
            }
            return "";
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (value instanceof RendererObject) {
            RendererObject rendererObject = (RendererObject) value;
            JComponent jComponent = (JComponent) component;
            jComponent.setBorder(new MatteBorder(rendererObject.top, rendererObject.left, rendererObject.bottom, rendererObject.right, table.getGridColor()));

            if (rendererObject.foreground != null) {
                jComponent.setForeground(rendererObject.foreground);
            } else {
                jComponent.setForeground(table.getForeground());
            }

            if (rendererObject.background != null) {
                jComponent.setBackground(rendererObject.background);
            } else {
                jComponent.setBackground(table.getBackground());
            }

            if (rendererObject.isBold) {
                jComponent.setFont(new Font(table.getFont().getName(), Font.BOLD, table.getFont().getSize()));
            } else {
                jComponent.setFont(table.getFont());
            }
        }

        return component;
    }
}
