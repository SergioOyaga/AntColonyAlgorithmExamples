package org.soyaga.examples.VRP.CapacitatedVRP.GUI;

import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;
import org.jxmapviewer.viewer.GeoPosition;
import org.soyaga.examples.VRP.CapacitatedVRP.GUI.Waypoints.*;

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
 * Class that runs a test of the frontend.
 */
public class RunTest {

    public static void main(String[] args) {
        try {
            // Read input images
            HashMap<String, BufferedImage> imageMap  =new HashMap<>(){{
                put("ToVisit",ImageIO.read(Objects.requireNonNull(RunTest.class.getResource("/VRP/Icons/ToVisitPlaceholder.png"))));
                put("Visited",ImageIO.read(Objects.requireNonNull(RunTest.class.getResource("/VRP/Icons/VisitedPlaceholder.png"))));
                put("CDC",ImageIO.read(Objects.requireNonNull(RunTest.class.getResource("/VRP/Icons/WarehousePlaceholder.png"))));
                put("Truck",ImageIO.read(Objects.requireNonNull(RunTest.class.getResource("/VRP/Icons/TruckPlaceholder.png"))));
            }};
            UI ui = readInputs("./VRP/Tests/TestGUI.xlsx", imageMap);

            Thread uiThread = new Thread(ui);
            uiThread.start();
            Thread.sleep(2000);
            //Random stat modification
            ui.setStats("Truck02", new HashMap<>(){{
                put("Duration", "ChangedValue");
            }});
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Function that reads an input Excel.
     * @param inputFilePath String with the Excel filename.
     * @param images HashMap<String, BufferedImage> with the images by type.
     * @throws IOException exception.
     */
    public static UI readInputs(String inputFilePath, HashMap<String, BufferedImage> images) throws IOException {
        HashMap<String, ToVisitWaypoint> toVisitWaypoints = new HashMap<>();
        HashMap<String, CDCWaypoint> cdcWaypoints = new HashMap<>();
        HashMap<String, ArrayList<GeoPosition>> visitedTrackWaypoints = new HashMap<>();
        HashMap<String, ArrayList<GeoPosition>> toVisitTrackWaypoints = new HashMap<>();
        HashMap<String, TruckWaypoint> truckWaypoints = new HashMap<>();
        HashMap<String, VisitedWaypoint> visitedWaypoints = new HashMap<>();

        InputStream stream = org.soyaga.examples.VRP.CapacitatedVRP.ACO.RunTest.class.getClassLoader().getResourceAsStream(inputFilePath); //InputStream
        assert stream != null;
        try (
                ReadableWorkbook wb = new ReadableWorkbook(stream)) {
            for(Sheet sheet:wb.getSheets().toList()){
                switch (sheet.getName()) {
                    case "POI" -> {
                        try (Stream<Row> rows = sheet.openStream().skip(1)) {
                            rows.forEach(r -> toVisitWaypoints.put(r.getCellText(0),
                                    new ToVisitWaypoint(r.getCellText(0),
                                            new GeoPosition(r.getCellAsNumber(2).get().doubleValue(),
                                                    r.getCellAsNumber(3).get().doubleValue()),
                                            r.getCellAsNumber(4).get().doubleValue(),
                                            images.get("ToVisit")
                                    )));
                        }
                    }
                    case "CDCs" -> {
                        try (Stream<Row> rows = sheet.openStream().skip(1)) {
                            rows.forEach(r -> cdcWaypoints.put(r.getCellText(0),
                                    new CDCWaypoint(r.getCellText(0),
                                            new GeoPosition(
                                                    r.getCellAsNumber(1).get().doubleValue(),
                                                    r.getCellAsNumber(2).get().doubleValue()),
                                            images.get("CDC")
                                    )
                            ));
                        }
                    }
                    case "Trucks" -> {
                        try (Stream<Row> rows = sheet.openStream().skip(1)) {
                            rows.forEach(r -> truckWaypoints.put(r.getCellText(0),
                                    new TruckWaypoint(r.getCellText(0),
                                            new GeoPosition(
                                                    r.getCellAsNumber(1).get().doubleValue(),
                                                    r.getCellAsNumber(2).get().doubleValue()),
                                            r.getCellAsNumber(3).get().doubleValue(),
                                            r.getCellAsNumber(4).get().doubleValue(),
                                            r.getCellAsNumber(5).get().doubleValue(),
                                            r.getCellAsNumber(6).get().doubleValue(),
                                            images.get("Truck"),
                                            convertStringToColor(r.getCellText(7))
                                    )
                            ));
                        }
                    }
                    case "Visited" -> {
                        try (Stream<Row> rows = sheet.openStream().skip(1)) {
                            rows.forEach(r -> visitedWaypoints.put(r.getCellText(0),
                                    new VisitedWaypoint(r.getCellText(0),
                                            new GeoPosition(
                                                    r.getCellAsNumber(1).get().doubleValue(),
                                                    r.getCellAsNumber(2).get().doubleValue()
                                            ),
                                            r.getCellAsNumber(3).get().doubleValue(),
                                            r.getCellText(4),
                                            images.get("Visited")
                                    )
                            ));
                        }
                    }
                    case "VisitedTracks" -> {
                        try (Stream<Row> rows = sheet.openStream().skip(1)) {
                            rows.forEach(r -> visitedTrackWaypoints.computeIfAbsent(
                                    r.getCellText(0), k ->new ArrayList<>()).add(
                                            new GeoPosition(
                                                    r.getCellAsNumber(1).get().doubleValue(),
                                                    r.getCellAsNumber(2).get().doubleValue()
                                            )
                                    )
                            );
                        }
                    }
                    case "ToVisitTracks" -> {
                        try (Stream<Row> rows = sheet.openStream().skip(1)) {
                            rows.forEach(r -> toVisitTrackWaypoints.computeIfAbsent(
                                    r.getCellText(0), k ->new ArrayList<>()).add(
                                            new GeoPosition(
                                                    r.getCellAsNumber(1).get().doubleValue(),
                                                    r.getCellAsNumber(2).get().doubleValue()
                                            )
                                    )

                            );
                        }
                    }
                    default -> {
                    }
                }
            }
        }
        UI ui = new UI();
        for(Map.Entry<String, CDCWaypoint> entry: cdcWaypoints.entrySet()){
            ui.addGUICDC(entry.getValue());
        }
        for(Map.Entry<String, TruckWaypoint> entry: truckWaypoints.entrySet()){
            ui.addGUITruck(entry.getValue());
        }
        for(Map.Entry<String, VisitedWaypoint> entry: visitedWaypoints.entrySet()){
            ui.addGUIVisitedWaypoint(entry.getValue());
        }
        for(Map.Entry<String, ToVisitWaypoint> entry: toVisitWaypoints.entrySet()){
            ui.addGUIToVisitWaypoint(entry.getValue());
        }
        for(Map.Entry<String, ArrayList<GeoPosition>> entry : visitedTrackWaypoints.entrySet()){
            ui.addVisitedTrack(entry.getKey(),entry.getValue(), 100., 10.);
        }
        for(Map.Entry<String, ArrayList<GeoPosition>> entry : toVisitTrackWaypoints.entrySet()){
            ui.addToVisitTrack(entry.getKey(),entry.getValue(), 20., 2.);
        }
        return ui;
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

        // Create a new Color instance
        return new Color(red, green, blue);
    }
}
