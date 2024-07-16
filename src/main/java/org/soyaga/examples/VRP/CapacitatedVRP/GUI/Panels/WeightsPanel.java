package org.soyaga.examples.VRP.CapacitatedVRP.GUI.Panels;

import lombok.Getter;
import org.soyaga.examples.VRP.CapacitatedVRP.GUI.Renderers.TruckColumnRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class WeightsPanel extends JScrollPane {
    /**
     * JTable with the weight info.
     */
    @Getter
    private final JTable weights;
    /**
     * JTable with a row for the row header column.
     */
    private final JTable rowHeaderTable;

    /**
     * Creates a new <code>JPanel</code> with a double buffer
     * and a flow layout.
     */
    public WeightsPanel() {
        super();
        this.weights = new JTable(new DefaultTableModel(new String[][]{new String[]{"0.33","0.33","0.33"}},
                new String []{"Distance", "Time", "Cost"})
        );
        this.setViewportView(this.weights);
        initializeTable();
        this.rowHeaderTable = new JTable(new DefaultTableModel(new String[][]{new String[]{"Weights"}},
                new String []{"RowName"}));
        this.setRowHeaderView(this.rowHeaderTable);
        initializeRowHeaderTable();
    }

    /**
     * Function that initializes the Stats table
     */
    private void initializeTable() {
        this.weights.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        this.weights.setCellSelectionEnabled(false);
        this.weights.setFocusable(false);
    }

    /**
     * Function that initializes the JScrollPane header
     */
    private void initializeRowHeaderTable() {
        this.rowHeaderTable.getColumnModel().getColumn(0).setCellRenderer(new TruckColumnRenderer());
        this.rowHeaderTable.setCellSelectionEnabled(false);
        this.rowHeaderTable.setFocusable(false);
        this.rowHeaderTable.setColumnSelectionAllowed(false);
        this.rowHeaderTable.setRowSelectionAllowed(false);
        this.getRowHeader().setPreferredSize(new Dimension(this.rowHeaderTable.getPreferredSize().width, 0));

    }
}
