package org.soyaga.examples.VRP.CapacitatedVRP;

import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;
import org.jxmapviewer.viewer.GeoPosition;
import org.soyaga.examples.VRP.CapacitatedVRP.ApiCall.GoogleRoutesApiCall;
import org.soyaga.examples.VRP.CapacitatedVRP.GUI.RunTest;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Class that runs an example of the CapacitatedVRP.
 */
public class RunCapacitatedVRP {

    public static void main(String[] args) throws IOException {
        String apiKey = null; // Add a valid Google API key with routes endpoint enabled.
        // Read input images
        HashMap<String, BufferedImage> imageMap  =new HashMap<>(){{
            put("ToVisit", ImageIO.read(Objects.requireNonNull(RunTest.class.getResource("/VRP/Icons/ToVisitPlaceholder.png"))));
            put("Visited",ImageIO.read(Objects.requireNonNull(RunTest.class.getResource("/VRP/Icons/VisitedPlaceholder.png"))));
            put("CDC",ImageIO.read(Objects.requireNonNull(RunTest.class.getResource("/VRP/Icons/WarehousePlaceholder.png"))));
            put("Truck",ImageIO.read(Objects.requireNonNull(RunTest.class.getResource("/VRP/Icons/TruckPlaceholder.png"))));
        }};
        Controller controller = new Controller(apiKey);
        readInputs("./VRP/CapacitatedRoutePlanning.xlsx", controller, imageMap, apiKey);
        controller.startApp();
    }

    /**
     * Function that reads an input Excel and initializes the GUI using the Controller.
     * @param inputFilePath String with the Excel filename.
     * @param controller Controller to manage the App.
     * @param images HashMap<String, BufferedImage> with the images by type.
     * @param apiKey String with the Googles' API key. If null, Haversine distances and straight line routes are computed.
     * @throws IOException exception.
     */
    public static void readInputs(String inputFilePath, Controller controller, HashMap<String, BufferedImage> images, String apiKey) throws IOException {
        InputStream stream = RunCapacitatedVRP.class.getClassLoader().getResourceAsStream(inputFilePath); //InputStream
        HashMap<String, GeoPosition> positions = new HashMap<>();
        HashMap<String, Double> visitedByTruck = new HashMap<>();
        HashMap<String, ArrayList<String>> trackByTruck = new HashMap<>();
        assert stream != null;
        try (
                ReadableWorkbook wb = new ReadableWorkbook(stream)) {
            for(Sheet sheet:wb.getSheets().toList()){
                switch (sheet.getName()) {
                    case "ToVisit" -> {
                        try (Stream<Row> rows = sheet.openStream().skip(1)) {
                            rows.forEach(r ->{
                                {
                                    GeoPosition position = new GeoPosition(r.getCellAsNumber(2).get().doubleValue(),
                                            r.getCellAsNumber(3).get().doubleValue());
                                    controller.addGUIToVisit(
                                            r.getCellText(0),
                                            position,
                                            r.getCellAsNumber(4).get().doubleValue(),
                                            images.get("ToVisit")
                                    );
                                    positions.put(r.getCellText(0),position);
                                }}
                            );
                        }
                    }
                    case "Visited" -> {
                        try (Stream<Row> rows = sheet.openStream().skip(1)) {
                            rows.forEach(r ->
                                {{
                                    GeoPosition position = new GeoPosition(r.getCellAsNumber(2).get().doubleValue(),
                                            r.getCellAsNumber(3).get().doubleValue());
                                    controller.addGUIVisited(
                                            r.getCellText(0),
                                            position,
                                            r.getCellAsNumber(4).get().doubleValue(),
                                            r.getCellText(1),
                                            images.get("Visited")
                                    );
                                    visitedByTruck.put(r.getCellText(1),visitedByTruck.getOrDefault(r.getCellText(1),0.)+1.);
                                    positions.put(r.getCellText(0),position);
                                }}
                            );
                        }
                    }
                    case "CDCs" -> {
                        try (Stream<Row> rows = sheet.openStream().skip(1)) {
                            rows.forEach(r ->
                                {{
                                    GeoPosition position = new GeoPosition(r.getCellAsNumber(1).get().doubleValue(),
                                            r.getCellAsNumber(2).get().doubleValue());
                                    controller.addGUICDC(
                                            r.getCellText(0),
                                            position,
                                            images.get("CDC")
                                    );
                                    positions.put(r.getCellText(0),position);
                                }}
                            );
                        }
                    }
                    case "Trucks" -> {
                        try (Stream<Row> rows = sheet.openStream().skip(1)) {
                            rows.forEach(r ->
                                {{
                                    GeoPosition position = new GeoPosition(r.getCellAsNumber(1).get().doubleValue(),
                                            r.getCellAsNumber(2).get().doubleValue());
                                    controller.addGUITruck(
                                            r.getCellText(0),
                                            position,
                                            r.getCellAsNumber(3).get().doubleValue(),
                                            r.getCellAsNumber(4).get().doubleValue(),
                                            r.getCellAsNumber(5).get().doubleValue(),
                                            r.getCellAsNumber(6).get().doubleValue(),
                                            images.get("Truck"),
                                            convertStringToColor(r.getCellText(7))
                                    );
                                    positions.put(r.getCellText(0),position);
                                }}
                            );
                        }
                    }
                    case "VisitedTrack" -> {
                        try (Stream<Row> rows = sheet.openStream().skip(1)) {
                            rows.forEach(r ->
                                    trackByTruck.computeIfAbsent(r.getCellText(0), k ->new ArrayList<>()).add(r.getCellText(1))
                            );
                        }
                    }
                    default -> {
                    }
                }
            }
        }

        for(Map.Entry<String, Double> visitedEntry:visitedByTruck.entrySet()){
            controller.changeVisitedWaypointNumber(visitedEntry.getKey(), visitedEntry.getValue());
        }
        for(Map.Entry<String, ArrayList<String>> trackEntry: trackByTruck.entrySet()){
            ArrayList<GeoPosition> positionsTrack = new ArrayList<>();
            for(String ID: trackEntry.getValue()){
                positionsTrack.add(positions.get(ID));
            }
            computeVisitedTrack(controller,apiKey,trackEntry.getKey(),positionsTrack);
        }
    }

    /**
     * Function that computes the visited tracks of for the trucks and the visited waypoints. This information is
     * computed here, but it will probably come from the real truck data.
     * @param controller Controller to fill the visited tracks
     * @param apiKey String with the Googles' API key. If null, Haversine distances and straight line routes are computed.
     * @param truckID String with the ID of the truck to fill the visited track.
     * @param geoPositions ArrayList<GeoPosition> with the GeoPositions followed by the truck.
     */
    private static void computeVisitedTrack(Controller controller, String apiKey, String truckID, ArrayList<GeoPosition> geoPositions) {
        GoogleRoutesApiCall routesApi = new GoogleRoutesApiCall(apiKey);
        controller.addVisitedTrack(truckID, (ArrayList<GeoPosition>) routesApi.postRequest(geoPositions).get("track"),
                (Double) routesApi.postRequest(geoPositions).get("distance"),
                (Double) routesApi.postRequest(geoPositions).get("duration"));
    }

    /**
     * Function that converts the read string to a color.
     * @param colorString String of the color.
     * @return Color of the String.
     */
    public static Color convertStringToColor(String colorString) {
        // Split the string by commas
        String[] rgbValues = colorString.split(",");

        // Convert the string values to integers
        int red = Integer.parseInt(rgbValues[0].trim());
        int green = Integer.parseInt(rgbValues[1].trim());
        int blue = Integer.parseInt(rgbValues[2].trim());
        int alpha = Integer.parseInt(rgbValues[3].trim());

        // Create a new Color instance
        return new Color(red, green, blue, alpha);
    }

}
