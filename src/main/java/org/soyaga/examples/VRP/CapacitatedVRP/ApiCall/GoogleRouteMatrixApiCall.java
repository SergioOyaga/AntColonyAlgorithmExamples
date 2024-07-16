package org.soyaga.examples.VRP.CapacitatedVRP.ApiCall;

import org.jxmapviewer.viewer.GeoPosition;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Class that makes the call to the Google RouteMatrix endpoint. With this configuration, Google RouteMatrix API
 * allows up to 625 different routes. That is a 25x25 matrix.
 */
public class GoogleRouteMatrixApiCall {
    /**
     * URL object for the Googles API call.
     */
    private final URL url ;
    /**
     * String with the Googles API Key.
     */
    private final String apiKey;

    /**
     * Constructor.
     * @param apiKey String with the Googles API Key
     */
    public GoogleRouteMatrixApiCall(String apiKey) {
        this.apiKey = apiKey;
        try {
            this.url = new URL("https://routes.googleapis.com/distanceMatrix/v2:computeRouteMatrix");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Function that computes the payload and makes the call to the Googles API.
     *
     * @param waypoints HashMap<String, GeoPosition> with the positions to use in the API call. (All with All)
     * @return JSONObject with the API response.
     */
    public HashMap<String, HashMap<String, HashMap<String, Double>>> postRequest(HashMap<String, GeoPosition> waypoints){
        HashMap<String,HashMap<String,HashMap<String,Double>>> result = new HashMap<>(); //HashMap{waypointID,{waypointID,{"distance"/"duration",Value}}}
        ArrayList<Map.Entry<String, GeoPosition>> pointsArray = new ArrayList<>(waypoints.entrySet());
        if (this.apiKey == null){
           this.buildHaversineDistances(pointsArray, result);
        }
        else {
            ArrayList<ArrayList<Map.Entry<String, GeoPosition>>> pointsChunk = new ArrayList<>();
            for(int i=0;i<pointsArray.size();i+=25) {
                pointsChunk.add(new ArrayList<>(pointsArray.subList(i,Math.min(i+25, pointsArray.size()))));
            }
            for(ArrayList<Map.Entry<String, GeoPosition>> originArray:pointsChunk){
                for(ArrayList<Map.Entry<String, GeoPosition>> destArray:pointsChunk){
                    JSONObject payload = this.buildRequest(originArray, destArray);
                    JSONArray response = this.postPayload(payload);
                    assert response != null;
                    this.extractResults(originArray, destArray, result, response);

                }
            }
        }
        return result;
    }


    /**
     * Function that computes the payload and makes the call to the Googles API. (Left with Right and Right with Left)
     *
     * @param waypointsLeft HashMap<String, GeoPosition> with the left positions to use in the API call.
     * @param waypointsRight HashMap<String, GeoPosition> with the right positions to use in the API call.
     * @return JSONObject with the API response.
     */
    public HashMap<String, HashMap<String, HashMap<String, Double>>> postRequest(HashMap<String, GeoPosition> waypointsLeft,
                                                                                 HashMap<String, GeoPosition> waypointsRight){
        HashMap<String,HashMap<String,HashMap<String,Double>>> result = new HashMap<>(); //HashMap{waypointID,{waypointID,{"distance"/"duration",Value}}}
        ArrayList<Map.Entry<String, GeoPosition>> leftArray = new ArrayList<>(waypointsLeft.entrySet());
        ArrayList<Map.Entry<String, GeoPosition>> rightArray = new ArrayList<>(waypointsRight.entrySet());
        if (this.apiKey == null){
            this.buildHaversineDistances(leftArray, rightArray, result);
        }
        else {
            ArrayList<ArrayList<Map.Entry<String, GeoPosition>>> leftChunks = new ArrayList<>();
            for(int i=0;i<leftArray.size();i+=25) {
                leftChunks.add(new ArrayList<>(leftArray.subList(i,Math.min(i+25, leftArray.size()))));
            }
            ArrayList<ArrayList<Map.Entry<String, GeoPosition>>> rightChunks = new ArrayList<>();
            for(int i=0;i<rightArray.size();i+=25) {
                rightChunks.add(new ArrayList<>(rightArray.subList(i,Math.min(i+25, rightArray.size()))));
            }
            for(ArrayList<Map.Entry<String, GeoPosition>> originArray:leftChunks){
                for(ArrayList<Map.Entry<String, GeoPosition>> destArray:rightChunks){
                    JSONObject payload = this.buildRequest(originArray, destArray);
                    JSONArray response = this.postPayload(payload);
                    assert response != null;
                    this.extractResults(originArray, destArray, result, response);
                    payload = this.buildRequest(destArray, originArray);
                    response = this.postPayload(payload);
                    assert response != null;
                    this.extractResults(destArray, originArray, result, response);
                }
            }
        }
        return result;
    }
    /**
     * Extract the information computed by Google API
     * @param originArray ArrayList<Map.Entry<String,GeoPosition>> with the origin waypoints.
     * @param destArray ArrayList<Map.Entry<String,GeoPosition>> with the dest waypoints.
     * @param result result of the API call.
     * @param response JSONArray with the API response.
     */
    private void extractResults(ArrayList<Map.Entry<String,GeoPosition>> originArray,
                                ArrayList<Map.Entry<String,GeoPosition>> destArray,
                                HashMap<String, HashMap<String, HashMap<String, Double>>> result,
                                JSONArray response) {
        for(Object routeABObject: response){
            JSONObject routeABJSON = (JSONObject) routeABObject;
            if(routeABJSON.getString("condition").equals("ROUTE_EXISTS")) {
                String origin = originArray.get(routeABJSON.getInt("originIndex")).getKey();
                String destination = destArray.get(routeABJSON.getInt("destinationIndex")).getKey();
                if(Objects.equals(origin, destination)) continue;
                double duration = Double.parseDouble(routeABJSON.getString("duration").replace("s", ""));
                double distance = 0.;
                if (duration>0.){
                    distance = routeABJSON.getDouble("distanceMeters");
                }
                result.computeIfAbsent(origin, k -> new HashMap<>()).computeIfAbsent(destination, k -> new HashMap<>());
                result.get(origin).get(destination).put("duration", duration);
                result.get(origin).get(destination).put("distance", distance);
            }
        }

    }

    /**
     * Function that builds the compute matrix JSON payload.
     * @param originArray ArrayList<Map.Entry<String, GeoPosition>> with the origins.
     * @param destArray ArrayList<Map.Entry<String, GeoPosition>> with the destinations.
     * @return JSONObject with the payload.
     */
    private JSONObject buildRequest(ArrayList<Map.Entry<String, GeoPosition>> originArray, ArrayList<Map.Entry<String, GeoPosition>> destArray) {
        JSONArray origins = new JSONArray();
        JSONArray destinations = new JSONArray();
        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put("origins", origins);
        jsonPayload.put("destinations", destinations);
        jsonPayload.put("travelMode", "DRIVE");
        jsonPayload.put("routingPreference","TRAFFIC_UNAWARE" ); //https://developers.google.com/maps/documentation/routes/reference/rest/v2/RoutingPreference
        jsonPayload.put("languageCode","en-US");
        for(Map.Entry<String,GeoPosition>  waypointEntry: originArray){
            origins.put(computeOriginJSON(waypointEntry.getValue()));
        }
        for(Map.Entry<String,GeoPosition>  waypointEntry: destArray){
            destinations.put(computeDestinationJSON(waypointEntry.getValue()));
        }
        return jsonPayload;
    }

    /**
     * Function that creates Googles like <a href="https://developers.google.com/maps/documentation/routes/reference/rest/v2/TopLevel/computeRouteMatrix#RouteMatrixDestination">RouteMatrixDestination </a> JSON object
     * @param position GeoPosition with the coordinates.
     * @return JSONObject with Googles format.
     */
    private JSONObject computeDestinationJSON(GeoPosition position) {
        JSONObject destinationJSON = new JSONObject();
        destinationJSON.put("waypoint",computeWaypointJSON(position));
        return destinationJSON;
    }

    /**
     * Function that creates Googles like <a href="https://developers.google.com/maps/documentation/routes/reference/rest/v2/TopLevel/computeRouteMatrix#RouteMatrixOrigin">RouteMatrixOrigin </a> JSON object
     * @param position GeoPosition with the coordinates.
     * @return JSONObject with Googles format.
     */
    private JSONObject computeOriginJSON(GeoPosition position) {
        JSONObject originJSON = new JSONObject();
        originJSON.put("waypoint",computeWaypointJSON(position));
        originJSON.put("routeModifiers",computeOriginModifiers());
        return originJSON;
    }

    /**
     * Function that creates Googles like <a href="https://developers.google.com/maps/documentation/routes/reference/rest/v2/RouteModifiers">feedbackRouteModifiers</a> JSON object
     * @return JSONObject with Googles format.
     */
    private JSONObject computeOriginModifiers() {
        JSONObject modifiers = new JSONObject();
        modifiers.put("avoidTolls", false);
        modifiers.put("avoidHighways", false);
        modifiers.put("avoidFerries", false);
        modifiers.put("vehicleInfo",computeVehicleInfo());
        return modifiers;
    }

    /**
     * Function that creates Googles like <a href="https://developers.google.com/maps/documentation/routes/reference/rest/v2/Waypoint">Waypoint </a> JSON object
     * @param position GeoPosition with the coordinates.
     * @return JSONObject with Googles format.
     */
    private JSONObject computeWaypointJSON( GeoPosition position) {
        JSONObject waypointJSON = new JSONObject();
        waypointJSON.put("via", false);
        waypointJSON.put("vehicleStopover", false);
        waypointJSON.put("sideOfRoad", false);
        waypointJSON.put("location",computeWaypointLocation(position));
        return waypointJSON;
    }

    /**
     * Function that creates Googles like <a href="https://developers.google.com/maps/documentation/routes/reference/rest/v2/Location">Location </a> JSON object
     * @param position GeoPosition with the coordinates.
     * @return JSONObject with Googles format.
     */
    private JSONObject computeWaypointLocation(GeoPosition position) {
        JSONObject locationWaypointJSON = new JSONObject();
        locationWaypointJSON.put("latLng",computeWaypointLatLong(position));
        return locationWaypointJSON;
    }

    /**
     * Function that creates Googles like <a href="https://developers.google.com/maps/documentation/routes/reference/rest/v2/LatLng">LatLng </a> JSON object
     * @param position GeoPosition with the Lat and Long coordinates.
     * @return JSONObject with Googles format.
     */
    private JSONObject computeWaypointLatLong(GeoPosition position) {
        JSONObject waypointLatLng = new JSONObject();
        waypointLatLng.put("latitude",position.getLatitude());
        waypointLatLng.put("longitude",position.getLongitude());
        return  waypointLatLng;
    }

    /**
     * Function that allows to select the <a href="https://developers.google.com/maps/documentation/routes/reference/rest/v2/RouteModifiers#VehicleInfoVehicleInfo">VehicleInfo</a>.
     * @return JSONObject with the vehicle type.
     */
    private JSONObject computeVehicleInfo() {
        JSONObject vehicleInfo = new JSONObject();
        vehicleInfo.put("emissionType","GASOLINE");
        return vehicleInfo;
    }

    /**
     * Function that makes the post of the payload into Googles API.
     * @param payload JSONObject with the payload.
     * @return JSONObject with the response.
     */
    private JSONArray postPayload(JSONObject payload) {
        try {// Open a connection
            HttpURLConnection connection = (HttpURLConnection) this.url.openConnection();
            // Set the request method (GET)
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("X-Goog-Api-Key", this.apiKey);
            connection.setRequestProperty("X-Goog-FieldMask", "originIndex,destinationIndex,duration,distanceMeters,status,condition");
            // Set the request body
            connection.setDoOutput(true);
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = payload.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            // Connect and get the response code
            if(connection.getResponseCode()==200){
                // Read the InputStream and build a response string
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder responseStrBuilder = new StringBuilder();
                String inputStr;
                while ((inputStr = bufferedReader.readLine()) != null) {
                    responseStrBuilder.append(inputStr);
                }
                bufferedReader.close();

                // Parse input stream into tokener to create the json object.
                JSONTokener tokener = new JSONTokener(responseStrBuilder.toString());

                // Close the connection
                connection.disconnect();
                // Parse the JSON data into a JSONObject
                return new JSONArray(tokener);
            }else {
                System.out.println("Request Error: "+connection.getResponseCode());
                // Close the connection
                connection.disconnect();
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Function that builds the distance using haversineDistance between nodes instead of the Google matrix route api
     * @param pointsArray ArrayList<Map.Entry<String, GeoPosition>> with the locations
     * @param result HashMap with the results.
     */
    private void buildHaversineDistances(ArrayList<Map.Entry<String, GeoPosition>> pointsArray,
                                        HashMap<String, HashMap<String, HashMap<String, Double>>> result) {
        for(Map.Entry<String, GeoPosition> originEntry:pointsArray){
            HashMap<String,HashMap<String,Double>> origMap = result.computeIfAbsent(originEntry.getKey(), k -> new HashMap<>());
            for(Map.Entry<String, GeoPosition> destEntry: pointsArray){
                HashMap<String,Double> origDestMap = origMap.computeIfAbsent(destEntry.getKey(), k -> new HashMap<>());
                double dist = haversineDistance(originEntry.getValue(),destEntry.getValue());
                origDestMap.put("duration", dist/30.);
                origDestMap.put("distance", dist);
            }
        }
    }

    /**
     * Function that builds the distance using haversineDistance between nodes instead of the Google matrix route api
     * @param leftArray ArrayList<Map.Entry<String, GeoPosition>> with the left
     * @param rightArray ArrayList<Map.Entry<String, GeoPosition>> with the right locations
     * @param result HashMap with the results.
     */
    private void buildHaversineDistances(ArrayList<Map.Entry<String, GeoPosition>> leftArray,
                                         ArrayList<Map.Entry<String, GeoPosition>> rightArray,
                                         HashMap<String, HashMap<String, HashMap<String, Double>>> result) {
        for(Map.Entry<String, GeoPosition> originEntry:leftArray){
            HashMap<String,HashMap<String,Double>> origMap = result.computeIfAbsent(originEntry.getKey(), k -> new HashMap<>());
            for(Map.Entry<String, GeoPosition> destEntry: rightArray){
                HashMap<String,Double> origDestMap = origMap.computeIfAbsent(destEntry.getKey(), k -> new HashMap<>());
                double dist = haversineDistance(originEntry.getValue(),destEntry.getValue());
                origDestMap.put("duration", dist/8.3);
                origDestMap.put("distance", dist);
                HashMap<String,HashMap<String,Double>> destMap = result.computeIfAbsent(destEntry.getKey(), k -> new HashMap<>());
                HashMap<String,Double> destOriginMap = destMap.computeIfAbsent(originEntry.getKey(), k -> new HashMap<>());
                dist = haversineDistance(destEntry.getValue(),originEntry.getValue());
                destOriginMap.put("duration", dist/30.);
                destOriginMap.put("distance", dist);
            }
        }
    }

    /**
     * Function that computes the Haversine distance between two GeoPosition.
     * @param orig GeoPosition origin.
     * @param dest GeoPosition dest.
     * @return double with the distance
     */
    private double haversineDistance(GeoPosition orig, GeoPosition dest) {
        double earthRadius = 6371000; // Earth radius in meters
        double dLat = Math.toRadians(dest.getLatitude() - orig.getLatitude());
        double dLon = Math.toRadians(dest.getLongitude() - orig.getLongitude());
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(orig.getLatitude())) * Math.cos(Math.toRadians(dest.getLatitude())) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }
}
