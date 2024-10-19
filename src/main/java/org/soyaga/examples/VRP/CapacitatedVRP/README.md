# Capacitated VRP

Solving the Capacitated Vehicle Routing Problem (CVRP) with real-time routing.

<table>
  <tr>
    <th> <b>Example </b></th>
  </tr>
  <tr>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/VRP/CapacitatedVRP_Google.gif"  title="User demo" alt="User demo"/></td>
  </tr>
</table>

## In This Folder:

This folder contains three different folders that define the structures required for solving the problem, one class to control the app, and one main class.

1.  [ACO](#aco): Folder with the ACO implementation using the OptimizationLib.aco library.
2.  [GUI](#gui): Folder with the GUI implementation using the JXMapViewer library.
3.  [ApiCall](#apicall): Folder with the Google Route/RoutesMatrix API call classes.
4.  [Controller](#controller): Class that controls the interaction between the frontend (GUI) and the backend (ACO). It also makes calls to the Google API (ApiCall).
5.  [RunCapacitatedVRP](#runcapacitatedvrp): Main class that initiates the program.

### [ACO](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO):

This folder contains all the classes needed to build the optimization problem.

### [GUI](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/GUI):

This folder contains all the classes needed to build the user interface.

### [ApiCall](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ApiCall):

This folder contains the classes to post and retrieve information to/from the Google API.

### [Controller](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/Controller.java):

This class manages the interactions between the frontend and the backend. It also manages Google API calls.

The controller class provides the way to instantiate the frontend and the backend. It also includes functions to initialize the front with the "current" state of the network. This state is read from an Excel file, but ideally, it will come from the user systems, reading CDCs, trucks, nodes to visit, visited nodes, the state of the trucks, tracks followed by them, etc.

### [RunCapacitatedVRP](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/RunCapacitatedVRP.java):

This is the main class. It is where the run starts. It is as simple as instantiating the controller, filling it with the initial state, and running the app.

## Result Example:

<table>
    <tr>
        <th>Google User Example</th>
        <th>Haversine User Example</th>
    </tr>
    <tr>
        <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/VRP/CapacitatedVRP_Google.gif"  title="User demo" alt="User demo"/></td>
        <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/VRP/CapacitatedVRP_Haversine.gif"  title="User demo" alt="User demo"/></td>
    </tr>
</table>

We see one example using the Google API to compute the distance matrix between nodes and to compute the routes between nodes. On the other hand, we see another example using Haversine distances to compute the distance matrix between nodes and straight lines to draw the routes.

<table>
    <tr>
        <th>API Usage</th>
        <th>Cost of the Google API Call</th>
    </tr>
    <tr>
        <td><img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/VRP/Googles_api_Usage.png"  title="Usage." alt="Usage." /></td>
        <td><img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/VRP/Googles_Api_Cost.png"  title="Cost." alt="Cost." /></td>
    </tr>
</table>

In the table above, the cost of using the Google API is shown. Keep in mind that for this example, we called the API every time the ACO found a new solution. Ideally, we would call the API only once the solution has converged, leading to notably lower costs.

## Comment:

This CVRP is a classic example that uses the classes and structures of the OptimizationLib.aco package. It is a sample that mixes real-time optimization with user interface modifications and API call responses. :scream_cat:
