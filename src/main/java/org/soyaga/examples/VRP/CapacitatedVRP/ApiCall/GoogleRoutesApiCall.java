package org.soyaga.examples.VRP.CapacitatedVRP.ApiCall;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.jxmapviewer.viewer.GeoPosition;

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
import java.util.List;

/**
 * Class that makes the call to the Google Route endpoint. Googles Route API allow a max of 25 intermediate points.
 */
public class GoogleRoutesApiCall {
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
    public GoogleRoutesApiCall(String apiKey) {
        this.apiKey = apiKey;
        try {
            this.url = new URL("https://routes.googleapis.com/directions/v2:computeRoutes");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Function that computes the payload and makes the calls to the Googles API.
     * @param waypointsInRoute ArrayList<GUIWaypoint> Object waypoints in a route.
     */
    public HashMap<String,Object> postRequest(ArrayList<GeoPosition> waypointsInRoute){
        HashMap<String,Object> result = new HashMap<>();
        result.put("distance", 0.);
        result.put("duration", 0.);
        result.put("track", new ArrayList<GeoPosition>());
        if(this.apiKey == null){
            this.buildStraightLinesRoute(waypointsInRoute, result);
            this.computeDistancesAndDurations(result);
            return result;
        }
        else {
            for (int i = 1; i <= waypointsInRoute.size() - 1; i += 26) {
                int endIndex = Math.min(i + 25, waypointsInRoute.size() - 1);
                List<GeoPosition> subList = waypointsInRoute.subList(i - 1, endIndex + 1);
                JSONObject payload = this.buildRequest(subList);
                JSONObject response = this.postPayload(payload);
                assert response != null;
                this.extractResults(result, response);
            }
        }
        return  result;
    }

    /**
     * Extract the information computed by Google API
     * @param result result of the API call.
     * @param response JSONObject with the API response.
     */
    private void extractResults(HashMap<String, Object> result, JSONObject response) {
        JSONObject routeJSON = response.getJSONArray("routes").getJSONObject(0);
        double duration = Double.parseDouble(routeJSON.getString("duration").replace("s",""));
        if(duration>0.) {
            result.compute("distance", (k, v) -> (double) v + routeJSON.getInt("distanceMeters"));
            result.compute("duration", (k, v) -> (double) v + duration);
            JSONArray polyline = routeJSON.getJSONObject("polyline")
                    .getJSONObject("geoJsonLinestring")
                    .getJSONArray("coordinates");
            for (Object coordObject : polyline) {
                JSONArray coordJSON = (JSONArray) coordObject;
                ((ArrayList<GeoPosition>) result.get("track")).add(new GeoPosition(coordJSON.getDouble(1), coordJSON.getDouble(0)));
            }
        }
    }

    /**
     * Function that builds the route JSON payload.
     * @param subList List<GUIWaypoint> with all the waypoints to visit.
     * @return JSONObject with the payload.
     */
    private JSONObject buildRequest(List<GeoPosition> subList) {
        // Create a JSON payload (customize as needed)
        JSONObject jsonPayload = new JSONObject();
        jsonPayload.put("origin", computeWaypointJSON(subList.get(0)));
        jsonPayload.put("destination", computeWaypointJSON(subList.get(subList.size()-1)));
        JSONArray intermediates = computeIntermediatesJSON(subList.subList(1,subList.size()-1));
        jsonPayload.put("intermediates", intermediates);
        jsonPayload.put("travelMode", "DRIVE");
        jsonPayload.put("routingPreference","TRAFFIC_UNAWARE" );//https://developers.google.com/maps/documentation/routes/reference/rest/v2/RoutingPreference
        jsonPayload.put("polylineQuality","OVERVIEW");
        jsonPayload.put("polylineEncoding","GEO_JSON_LINESTRING");
        jsonPayload.put("computeAlternativeRoutes",false);
        jsonPayload.put("units","METRIC");
        jsonPayload.put("languageCode","en-US");
        jsonPayload.put("optimizeWaypointOrder",false);
        jsonPayload.put("routeModifiers",computeOriginModifiers());
        return jsonPayload;
    }

    /**
     * Function that creates Googles like <a href="https://developers.google.com/maps/documentation/routes/reference/rest/v2/TopLevel/computeRoutes">intermediates </a> JSON object
     * @param positions List<GeoPosition> with the intermediate waypoints to visit.
     * @return JSONObject with Googles format.
     */
    private JSONArray computeIntermediatesJSON(List<GeoPosition> positions) {
        JSONArray intermediates = new JSONArray();
        for(GeoPosition position: positions){
            intermediates.put(computeWaypointJSON(position));
        }
        return intermediates;
    }

    /**
     * Function that creates Googles like <a href="https://developers.google.com/maps/documentation/routes/reference/rest/v2/Waypoint">Waypoint </a> JSON object
     * @param position GeoPosition with the coordinates.
     * @return JSONObject with Googles format.
     */
    private JSONObject computeWaypointJSON(GeoPosition position) {
        JSONObject waypointJSON = new JSONObject();
        waypointJSON.put("via", false);
        waypointJSON.put("vehicleStopover", false);
        waypointJSON.put("sideOfRoad", true);
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
    private JSONObject postPayload(JSONObject payload) {
        try {// Open a connection
            HttpURLConnection connection = (HttpURLConnection) this.url.openConnection();
            // Set the request method (GET)
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("X-Goog-Api-Key", this.apiKey);
            connection.setRequestProperty("X-Goog-FieldMask", "routes.duration,routes.distanceMeters,routes.polyline");
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
                return new JSONObject(tokener);
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
     * Function that builds a route in straight lines between waypoints.
     * @param waypointsInRoute ArrayList<GeoPosition> with the route positions.
     * @param result HashMap with the result
     */
    private void buildStraightLinesRoute(ArrayList<GeoPosition> waypointsInRoute, HashMap<String, Object> result) {
        for(GeoPosition position: waypointsInRoute) ((ArrayList<GeoPosition>)result.get("track")).add(position);
    }

    /**
     * Function that computes the distance and duration using the haversine formula assuming linear routes.
     * @param result HashMap<String, Object> with the result and track to follow.
     */
    private void computeDistancesAndDurations(HashMap<String, Object> result) {
        GeoPosition origin =null;
        for(GeoPosition geoPosition: (ArrayList<GeoPosition>)result.get("track")){
            if(origin!=null){
                double dist = this.haversineDistance(origin, geoPosition);
                result.put("distance", (double)result.get("distance")+dist);
                result.put("duration", (double)result.get("duration")+dist/8.3);
            }
            origin= geoPosition;
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
