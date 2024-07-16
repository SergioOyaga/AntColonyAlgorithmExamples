package org.soyaga.examples.VRP.CapacitatedVRP.ACO;

import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;
import org.jxmapviewer.viewer.GeoPosition;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.Graph.Nodes.CDCNode;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.Graph.Nodes.WaypointNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Class that runs a test of the backend.
 */
public class RunTest {

    public static void main(String[] args) throws IOException {
        CapacitatedVRPColonyAlgorithm aco = new CapacitatedVRPColonyAlgorithm("Test1");
        readInputs("./VRP/Tests/TestACO.xlsx", aco);
        aco.setStopFlag(false); //By default, the Aco starts stopped.
        aco.optimize();
        System.out.println("TestEnd");
    }

    /**
     * Function that reads an input Excel.
     * @param inputFilePath String with the Excel filename.
     * @param aco CapacitatedVRPColonyAlgorithm to be filled.
     * @throws IOException exception.
     */
    public static void readInputs(String inputFilePath, CapacitatedVRPColonyAlgorithm aco) throws IOException{
        HashMap<String, GeoPosition> waypoints=new HashMap<>();
        InputStream stream = RunTest.class.getClassLoader().getResourceAsStream(inputFilePath); //InputStream
        assert stream != null;
        try (
                ReadableWorkbook wb = new ReadableWorkbook(stream)) {
            for(Sheet sheet:wb.getSheets().toList()){
                switch (sheet.getName()) {
                    case "Trucks" -> {
                        try (Stream<Row> rows = sheet.openStream().skip(1)) {
                            rows.forEach(r ->
                                {{
                                    aco.addNewTruckNode(
                                            r.getCellText(0),
                                            new GeoPosition(
                                                    r.getCellAsNumber(6).get().doubleValue(),
                                                    r.getCellAsNumber(5).get().doubleValue()),
                                            r.getCellAsNumber(1).get().doubleValue(),
                                            r.getCellAsNumber(2).get().doubleValue(),
                                            r.getCellAsNumber(3).get().doubleValue(),
                                            r.getCellAsNumber(4).get().doubleValue());
                                    waypoints.put(r.getCellText(0),
                                            new GeoPosition(
                                                    r.getCellAsNumber(6).get().doubleValue(),
                                                    r.getCellAsNumber(5).get().doubleValue()));
                                }}
                            );
                        }
                    }
                    case "CDCs" -> {
                        try (Stream<Row> rows = sheet.openStream().skip(1)) {
                            rows.forEach(r ->
                                {{
                                    aco.addNewCDC(
                                            r.getCellText(0),
                                            new GeoPosition(
                                                    r.getCellAsNumber(2).get().doubleValue(),
                                                    r.getCellAsNumber(1).get().doubleValue())
                                    );
                                    waypoints.put(r.getCellText(0),
                                            new GeoPosition(
                                                    r.getCellAsNumber(2).get().doubleValue(),
                                                    r.getCellAsNumber(1).get().doubleValue()));
                                }}
                            );
                        }
                    }
                    case "Waypoints" -> {
                        try (Stream<Row> rows = sheet.openStream().skip(1)) {
                            rows.forEach(r ->
                                {{
                                    aco.addNewWaypointNode(r.getCellText(0),
                                            new GeoPosition(
                                                    r.getCellAsNumber(3).get().doubleValue(),
                                                    r.getCellAsNumber(2).get().doubleValue()
                                            ),
                                            r.getCellAsNumber(1).get().doubleValue()
                                    );
                                    waypoints.put(r.getCellText(0),
                                            new GeoPosition(
                                                    r.getCellAsNumber(3).get().doubleValue(),
                                                    r.getCellAsNumber(2).get().doubleValue()));
                                }}
                            );
                        }
                    }
                    default -> {
                    }
                }
            }

            buildEdges(aco,waypoints);
        }
    }

    /**
     * Function that builds the edges in the aco.
     *
     * @param aco       CapacitatedVRPColonyAlgorithm where to build the edges
     * @param points HashMap with all the positions by ID.
     */
    private static void buildEdges(CapacitatedVRPColonyAlgorithm aco, HashMap<String, GeoPosition> points) {

        //Ligate Waypoints Between themselves
        for(Map.Entry<String, GeoPosition> origin: points.entrySet()){
            for(Map.Entry<String, GeoPosition> dest: points.entrySet()){
                if(origin.equals(dest)) continue;
                aco.editNodeEdge(
                        origin.getKey(),
                        dest.getKey(),
                        computeEuclideanDist(origin.getValue(), dest.getValue()),
                        computeEuclideanDist(origin.getValue(), dest.getValue())
                );
            }
        }
    }

    /**
     * Function that computes the Euclidean distance between to GeoPositions that represent an XY plane.
     * @param geoPosition GeoPosition with the XY point origin.
     * @param geoPosition1 GeoPosition with the XY point destination.
     * @return Double with the Euclidean distance.
     */
    private static Double computeEuclideanDist(GeoPosition geoPosition, GeoPosition geoPosition1) {
        return Math.max(1.,
                Math.sqrt(
                        (geoPosition.getLatitude()-geoPosition1.getLatitude())*(geoPosition.getLatitude()-geoPosition1.getLatitude()) +
                                (geoPosition.getLongitude()-geoPosition1.getLongitude())*(geoPosition.getLongitude()-geoPosition1.getLongitude())
                )
        );
    }
}
