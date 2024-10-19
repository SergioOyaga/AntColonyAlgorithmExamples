# ApiCall

This folder contains the classes needed to call the Google API for the Capacitated Vehicle Routing problem.

<table>
  <tr>
    <th> <b>28 Waypoint Cost Matrix + route </b></th>
  </tr>
  <tr>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/VRP/TestResults/ApiCall_Google_cost.png"  title="Cost." alt="Cost." /></td>
  </tr>
</table>

## In This Folder:

For this test, we created example input data that allows us to call the Google API.

This folder contains two different classes that define both calls:

1.  [GoogleRouteMatrixApiCall](#googleroutematrixapicall): Call to the matrix distance API.
2.  [GoogleRoutesApiCall](#googleroutesapicall): Call to the route API.

In addition, a test run class:

1.  [RunTest](#runtest): Main class that executes a test.

### [GoogleRouteMatrixApiCall](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ApiCall/GoogleRouteMatrixApiCall.java):

Class that builds a request for the Google matrix distance endpoint, retrieves the response, and builds and transforms the response to a HashMap:

*   Build, converts a HashMap<WaypointID, GeoPositions> to a payload. For more information on the payload structure, refer to the [Google matrix documentation](https://developers.google.com/maps/documentation/routes/reference/rest/v2/TopLevel/computeRouteMatrix).
*   Posts the payload to the API.
*   Retrieves and transforms the result to HashMap<WaypointID, HashMap<WaypointID, Distance/Duration>>.

### [GoogleRoutesApiCall](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ApiCall/GoogleRoutesApiCall.java):

Class that builds a request for the Google route endpoint, retrieves the response, and builds and transforms the response to a HashMap:

*   Build, converts an ArrayList<GeoPositions> to a payload. For more information on the payload structure, refer to the [Google routes documentation](https://developers.google.com/maps/documentation/routes/reference/rest/v2/TopLevel/computeRoutes).
*   Posts the payload to the API.
*   Retrieves and transforms the result to HashMap<String, value>:
    *   "distance" → double
    *   "duration" → double
    *   "track" → ArrayList<GeoPositions>

### [RunTest](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ApiCall/RunTest.java):

This is the main class for the test. It is where the run starts. It is as simple as instantiating the ApiCallers and requesting a payload. The example consists of:

*   Twenty-eight waypoints.
*   Two modes:
    1.  `apiKey=null` → Haversine calculations for distances and straight-line routes.
    2.  `apiKey!=null` → Google calculations for distances and routes. Requires a valid key with the routes API endpoint enabled.
## Results:

In the results table, we see different payloads for the same inputs, depending on whether apiKey is enabled.

<table>
  <tr>
    <th> <b>Google Matrix Payload</b> </th>
    <th> <b>Google Routes Payload</b> </th>
    <th> <b>Google Matrix Response</b> </th>
    <th> <b>Google Routes Response</b> </th>
  </tr>
  <tr>
    <td><a href="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/VRP/TestResults/ApiCall_Google_matrix_payload.txt">Matrix payload txt.</a></td>
    <td><a href="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/VRP/TestResults/ApiCall_Google_routes_payload.txt">Routes payload txt.</a></td>
    <td><a href="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/VRP/TestResults/ApiCall_Google_matrix_response.txt">Matrix response txt.</a></td>
    <td><a href="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/VRP/TestResults/ApiCall_Google_route_response.txt">Routes response txt.</a></td>
  </tr>
  <tr>
    <th colspan="2"> <b>Haversine Matrix Response</b> </th>
    <th colspan="2"> <b>Haversine Routes Response</b> </th>
  </tr>
  <tr>
    <td colspan="2"><a href="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/VRP/TestResults/ApiCall_Haversine_matrix_response.txt">Matrix response txt.</a></td>
    <td colspan="2"><a href="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/VRP/TestResults/ApiCall_Haversine_route_response.txt">Routes response txt.</a></td>
  </tr>
</table>

Notice that in both API calls, the maximum number of waypoints is reached. Internally, the call is split into chunks: four calls for the matrix and two for the route. For 28 waypoints:

1.  Matrix 25 * 25 = 625
2.  Matrix 25 * 3 = 75
3.  Matrix 3 * 25 = 75
4.  Matrix 3 * 3 = 9
5.  Route 2 + 25 = 27
6.  Routes 2 + 0 = 2 (One more because the last point in 5 is the first point in 6)

Total of 28 * 28 = 784 for the matrix and 29 for the route.

We can see this in the following images:

<table>
    <tr>
        <th>API Usage</th>
        <th>Matrix Tokens</th>
        <th>Cost of the Google API Call</th>
    </tr>
    <tr>
        <td><img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/VRP/TestResults/ApiCall_Google_Usage.png"  title="6 Calls." alt="6 Calls." /></td>
        <td><img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/VRP/TestResults/ApiCall_Google_matrix_quota_usage.png"  title="784 Tokens." alt="784 Tokens." /></td>
        <td><img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/VRP/TestResults/ApiCall_Google_cost.png"  title="Cost." alt="Cost." /></td>
    </tr>
</table>

## Comment:

This solution is a test developed as an intermediate step for the CapacitatedVRP with real-time optimization and Google API calls for routing and data gathering. :Scream_cat:

