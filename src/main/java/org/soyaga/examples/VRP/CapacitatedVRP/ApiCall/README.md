# ApiCall
This folder contains the classes needed to call the Google Api of the Capacitated Vehicle Routing problem.


<table>
  <tr>
    <th> <b>28 Waypoint Cost Matrix + route </b></th>
  </tr>
  <tr>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/VRP/TestResults/ApiCall_Google_cost.png"  title="Cost." alt="Cost." /></td>
  </tr>
</table>

## In this folder:
For this test, we created an example input data that allows us to call the Google Api. 

This folder contains two different classes that define both calls.
1. [GoogleRouteMatrixApiCall](#googleroutematrixapicall): Call to the matrix distance Api.
2. [GoogleRoutesApiCall](#googleroutesapicall): Call to the route Api.

In addition, a test run class.
1. [RunTest](#runtest): Main class that executes a test.


### [GoogleRouteMatrixApiCall](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ApiCall/GoogleRouteMatrixApiCall.java)
Class that builds a request for the Googles matrix distance endpoint, retrieves the response and builds and transforms the response to a HashMap:
<ul>
    <li> Build, converts a Hashmap&lt;WaypointID, GeoPositions&gt; to a payload. For more information on the payload structure refer to <a href="https://developers.google.com/maps/documentation/routes/reference/rest/v2/TopLevel/computeRouteMatrix">Google matrix doc</a> </li>
    <li> Posts the payload to the api.</li>
    <li> Retrieve and transform the result to HashMap&lt;WaypointID, HashMap&lt;WaypointID, Distance/Duration&gt;&gt;.</li>
</ul>

### [GoogleRoutesApiCall](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ApiCall/GoogleRoutesApiCall.java)
Class that builds a request for the Googles route endpoint, retrieves the response and builds and transforms the response to a HashMap:
<ul>
    <li> Build, converts an ArrayList&lt;GeoPositions&gt; to a payload. For more information on the payload structure refer to <a href="https://developers.google.com/maps/documentation/routes/reference/rest/v2/TopLevel/computeRoutes">Google routes doc</a> </li>
    <li> Posts the payload to the api.</li>
    <li> Retrieve and transform the result to HashMap&lt;String, value&gt;.
        <ul>
            <li>"distance" &rarr; double</li>
            <li>"duration" &rarr; double</li>
            <li>"track" &rarr; ArrayList&lt;GeoPositions&gt;</li>
        </ul>
    </li>
</ul>

### [RunTest](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ApiCall/RunTest.java)
This is the main class for the test. Is where the run starts. As simple as instantiate the ApiCallers and request a payload.

The example simply consists of:
<ul>
    <li> Twenty-eight Waypoints.</li>
    <li> We allow to modes:
        <ol>
            <li>apiKey=null &rarr; Haversine calculations for distances and straight line routes.
            <li>apiKey!=null &rarr; Googles calculations for distances and routes. Requires a valid key with the routes Api endpoint enabled.</li>
        </ol>
    </li>
</ul>

## Results:
In the results' table, we see different payloads for the same inputs, depending on whether apiKey is enabled.
<table>
  <tr>
    <th> <b>Googles' Matrix payload </b></th>
    <th> <b>Googles' Routes payload </b></th>
    <th> <b>Googles' Matrix response </b></th>
    <th> <b>Googles' Routes response </b></th>
  </tr>
  <tr>
    <td><a href="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/VRP/TestResults/ApiCall_Google_matrix_payload.txt">Matrix payload txt.</a></td>
    <td><a href="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/VRP/TestResults/ApiCall_Google_routes_payload.txt">Routes payload txt.</a></td>
    <td><a href="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/VRP/TestResults/ApiCall_Google_matrix_response.txt">Matrix response txt.</a></td>
    <td><a href="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/VRP/TestResults/ApiCall_Google_route_response.txt">Routes response txt.</a></td>
  </tr>
  <tr>
    <th colspan="2"> <b>Haversine Matrix response </b></th>
    <th colspan="2"> <b>Haversine Routes response </b></th>
  </tr>
  <tr>
    <td colspan="2"><a href="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/VRP/TestResults/ApiCall_Haversine_matrix_response.txt">Matrix response txt.</a></td>
    <td colspan="2"><a href="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/VRP/TestResults/ApiCall_Haversine_route_response.txt">Routes response txt.</a></td>
  </tr>
</table>

Notice that in both api calls, the maximum number of waypoints is reached. Internally, the call is split into chunks. Four calls for the matrix and two for the route. For 28 waypoints:
1. Matrix 25*25 = 625
2. Matrix 25*3 = 75
3. Matrix 3*25 = 75
4. Matrix 3*3 = 9
5. Route 2+25 = 27
6. Routes 2+0 = 2 (One more because the last point in 5 is the first point in 6)

Total of 28*28=784 for the matrix and 29 for the route.

We can see this in the following images:
<table>
    <tr>
        <th>Api Usage</th>
    </tr>
    <tr>
        <td><img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/VRP/TestResults/ApiCall_Google_Usage.png"  title="6 Calls." alt="6 Calls." /></td>
    </tr>
    <tr>
        <th>Matrix tokens</th>
    </tr>
    <tr>
        <td><img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/VRP/TestResults/ApiCall_Google_matrix_quota_usage.png"  title="784 Tokens." alt="784 Tokens." /></td>
    </tr>
    <tr>
        <th>Cost of the Google Api call</th>
    </tr>
    <tr>
        <td><img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/VRP/TestResults/ApiCall_Google_cost.png"  title="Cost." alt="Cost." /></td>
    </tr>
</table>

## Comment:
This solution is a test developed as an intermediate step for the CapacitatedVRP with real time optimization and Google API calls for the routing and data gather. :Scream_cat:

