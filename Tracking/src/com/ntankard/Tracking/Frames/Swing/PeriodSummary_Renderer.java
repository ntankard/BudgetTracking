package com.ntankard.Tracking.Frames.Swing;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class PeriodSummary_Renderer extends DefaultTableCellRenderer {

    public static class RendererObject {
        private Object coreObject;

        public int top;
        public int left;
        public int bottom;
        public int right;

        public RendererObject(Object obj) {
            this(obj, 1, 1);
        }

        public RendererObject(Object obj, int bottom, int right) {
            this(obj, 0, 0, bottom, right);
        }

        public RendererObject(Object coreObject, int top, int left, int bottom, int right) {
            this.coreObject = coreObject;
            this.top = top;
            this.left = left;
            this.bottom = bottom;
            this.right = right;
        }

        @Override
        public String toString() {
            return coreObject.toString();
        }
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (value instanceof RendererObject) {
            RendererObject rendererObject = (RendererObject) value;
            JComponent jComponent = (JComponent) component;
            jComponent.setBorder(new MatteBorder(rendererObject.top, rendererObject.left, rendererObject.bottom, rendererObject.right, table.getGridColor()));
        }

        return component;
    }
}
