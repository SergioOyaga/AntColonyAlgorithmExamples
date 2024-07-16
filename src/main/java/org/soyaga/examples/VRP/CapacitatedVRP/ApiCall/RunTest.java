package org.soyaga.examples.VRP.CapacitatedVRP.ApiCall;

import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;
import org.jxmapviewer.viewer.GeoPosition;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Stream;
import org.json.JSONObject;

/**
 * Class that runs a test of the api call.
 */
public class RunTest {

    public static void main(String[] args) {
        try {
            HashMap<String, GeoPosition> waypoints = readInputs("./VRP/Tests/TestApiCall.xlsx");
            String apiKey = null; // Replace by a valid key.
            GoogleRouteMatrixApiCall matrixApiCall = new GoogleRouteMatrixApiCall(apiKey);
            GoogleRoutesApiCall routesApiCall = new GoogleRoutesApiCall(apiKey);
            PrintWriter out = new PrintWriter("src/out/VRP/TestResults/ApiCall_Haversine_matrix_response.txt");
            JSONObject json = new JSONObject(matrixApiCall.postRequest(waypoints));
            out.println(json.toString(4));
            out.close();
            out = new PrintWriter("src/out/VRP/TestResults/ApiCall_Haversine_route_response.txt");
            json = new JSONObject(routesApiCall.postRequest(new ArrayList<>(waypoints.values())));
            out.println(json.toString(4));
            out.close();
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Function that reads an input Excel.
     * @param inputFilePath String with the Excel filename.
     * @throws IOException exception.
     */
    public static  HashMap<String, GeoPosition> readInputs(String inputFilePath) throws IOException {
        HashMap<String, GeoPosition> waypoints = new HashMap<>();

        InputStream stream = org.soyaga.examples.VRP.CapacitatedVRP.RunCapacitatedVRP.class.getClassLoader().getResourceAsStream(inputFilePath); //InputStream
        assert stream != null;
        try (
                ReadableWorkbook wb = new ReadableWorkbook(stream)) {
            for(Sheet sheet:wb.getSheets().toList()){
                if (sheet.getName().equals("Waypoints")) {
                    try (Stream<Row> rows = sheet.openStream().skip(1)) {
                        rows.forEach(r ->
                            waypoints.put(r.getCellText(0),
                                    new GeoPosition(r.getCellAsNumber(1).get().doubleValue(),
                                            r.getCellAsNumber(2).get().doubleValue()
                                    ))
                        );
                    }
                }
            }
        }
        return waypoints;
    }
}
