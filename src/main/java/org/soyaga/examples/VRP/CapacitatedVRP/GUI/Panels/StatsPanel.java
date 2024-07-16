package org.soyaga.examples.VRP.CapacitatedVRP.GUI.Panels;

import org.soyaga.examples.VRP.CapacitatedVRP.GUI.Renderers.TruckColumnRenderer;
import org.soyaga.examples.VRP.CapacitatedVRP.GUI.Waypoints.TruckWaypoint;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Class that extends JScrollPane and shows the updated stats values dynamically.
 */
public class StatsPanel extends JScrollPane {
    /**
     * HashMap<String, Integer> with the row number for each truck ID.
     */
    private final HashMap<String, Integer> rowByTruckID;
    /**
     * HashMap<String, Integer> with the col number by stat ID.
     */
    private final HashMap<String, Integer> colByStatID;
    /**
     * JTable with the stat info.
     */
    private final JTable table;
    /**
     * JTable with a row for the row header column.
     */
    private final JTable rowHeaderTable;

    /**
     * Creates a new <code>JPanel</code> with a double buffer
     * and a flow layout.
     */
    public StatsPanel() {
        super();
        ArrayList<String> statIDs = new ArrayList<>(){{
            add("Distance Km");
            add("Duration");
            add("Cost $");
            add("Quantity Delivered");
            add("Nodes Served");
            add("Times Load");
        }};
        this.rowByTruckID = new HashMap<>();
        this.colByStatID = initializeColByStatID(statIDs);
        this.setCorner(ScrollPaneConstants.UPPER_LEFT_CORNER, new JLabel("Truck ID"));
        this.table = new JTable(new DefaultTableModel(
                statIDs.toArray(new String[0]), 0)
        );
        this.setViewportView(this.table);
        initializeTable();
        this.rowHeaderTable = new JTable(new DefaultTableModel(0, 1));
        this.setRowHeaderView(this.rowHeaderTable);
        initializeRowHeaderTable();
    }

    /**
     *Function that initializes the colByStatID map.
     * @param statIDs ArrayList<String> with the stats.
     * @return HashMap<String, Integer> with the map.
     */
    private HashMap<String, Integer> initializeColByStatID(ArrayList<String> statIDs) {
        HashMap<String, Integer> colByStatID = new HashMap<>();
        for(int i=0;i<statIDs.size();i++){
            colByStatID.put(statIDs.get(i),i);
        }
        return colByStatID;
    }

    /**
     * Function that initializes the Stats table
     */
    private void initializeTable() {
        this.table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        this.table.setCellSelectionEnabled(false);
        this.table.setFocusable(false);
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

    /**
     * Function that adds a truck to the stat table.
     * @param truckWaypoint TruckWaypoint to add.
     */
    public void addTruckToTable(TruckWaypoint truckWaypoint){
        if(!this.rowByTruckID.containsKey(truckWaypoint.getID())){
            int rowNumber = table.getModel().getRowCount();
            ((DefaultTableModel)this.rowHeaderTable.getModel()).addRow(new Object[]{truckWaypoint.getID()});
            ((DefaultTableModel)this.table.getModel()).addRow(new Object[]{});
            this.rowByTruckID.put(truckWaypoint.getID(),rowNumber);
            for(int col=0;col<this.table.getModel().getColumnCount();col++) {
                this.table.getModel().setValueAt("TBD", rowNumber, col);
            }
            ((TruckColumnRenderer)this.rowHeaderTable.getColumnModel().getColumn(0).getCellRenderer()).
                    getColorMap().put(rowNumber,truckWaypoint.getRouteColor());
        }
    }

    /**
     * Function that sets the values of specific stats to a specific truckID.
     * @param truckID String with the truck ID that the stat applies.
     * @param statValuesByID HashMap<String, Object> with the values by stat name.
     */
    public void setStats(String truckID, HashMap<String, Object> statValuesByID){
        int rowNumber = this.rowByTruckID.get(truckID);
        for(Map.Entry<String, Object> entry : statValuesByID.entrySet()){
            int colNumber = this.colByStatID.get(entry.getKey());
            this.table.getModel().setValueAt(entry.getValue(), rowNumber, colNumber);
        }
    }
}
