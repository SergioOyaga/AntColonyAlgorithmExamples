package org.soyaga.examples.VRP.CapacitatedVRP.GUI.Renderers;

import lombok.Getter;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.HashMap;

/**
 * Class that renders the StatsPanel row header using the color of each truck.
 */
public class TruckColumnRenderer  extends DefaultTableCellRenderer {
    /**
     * HashMap<Integer, Color> with theColor by row of the StatsPane table
     */
    @Getter
    private final HashMap<Integer, Color> colorMap;

    /**
     * Constructor.
     */
    public TruckColumnRenderer() {
        super();
        this.colorMap = new HashMap<>();
    }

    /**
     *
     * Returns the default table cell renderer.
     * <p>
     * During a printing operation, this method will be called with
     * <code>isSelected</code> and <code>hasFocus</code> values of
     * <code>false</code> to prevent selection and focus from appearing
     * in the printed output. To do another customization based on whether
     * the table is being printed, check the return value from
     * {@link javax.swing.JComponent#isPaintingForPrint()}.
     *
     * @param table  the <code>JTable</code>
     * @param value  the value to assign to the cell at
     *                  <code>[row, column]</code>
     * @param isSelected true if the cell is selected
     * @param hasFocus true if cell has focus
     * @param row  the row of the cell to render
     * @param col the column of the cell to render
     * @return the default table cell renderer
     * @see javax.swing.JComponent#isPaintingForPrint()
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int col) {
        Component component = super.getTableCellRendererComponent(table, value,
                isSelected, hasFocus,
                row, col);
        component.setBackground(this.colorMap.getOrDefault(row,component.getBackground()));
        return component;
    }
}