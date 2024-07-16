# Capacitated VRP
Solving the Capacitated Vehicle Routing Problem (CVRP) with real time routing.

<table>
  <tr>
    <th> <b>Example </b></th>
  </tr>
  <tr>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/VRP/CapacitatedVRP_Google.gif"  title="User demo" alt="User demo"/></td>
  </tr>
</table>

## In this folder:
This folder contains 3 different folders that define the structures required for solving the problem, one class to control the app and one main class. 
1. [ACO](#aco): Folder with the ACO implementation using the OptimizationLib.aco library.
2. [GUI](#gui): Folder with the GUI implementation using JXMapViewer library.
3. [ApiCall](#apicall): Folder with the Google Route/RoutesMatrix API call classes.
4. [Controller](#controller): Class that controls the interaction between the frontend (GUI) and the backend (ACO). It also makes the calls to the Google's API (ApiCall)
5. [RunCapacitatedVRP](#runcapacitatedvrp): Main class that initiates the program.

### [ACO](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO):
This folder contains all the classes that needed to build the optimization problem.

### [GUI](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/GUI):
This folder contains all the classes that need to build the user interface.

### [ApiCall](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ApiCall):
This folder contains the classes to post and retrieve information to/from the Googles API.

### [Controller](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/Controller.java)
This class manages the interactions between the frontend and the backend. It also manages Googles' API calls.

Controller class provides the way to instantiate the frontend and the backend. It also includes functions to initialize the front with the "current" state of the network. This state is read from an Excel file, but ideally it will come from the user systems, reading CDCs, Trucks, nodes to visit, visited nodes, state of the trucks, tracks followed by them, etc.

### [RunCapacitatedVRP](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/RunCapacitatedVRP.java):
This is the main class. Is where the run starts. As simple as instantiate the Controller, fill it with the initial state and run the app.

## Result example:
<table>
    <tr>
        <th>Googles User example.</th>
    </tr>
    <tr>
        <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/VRP/CapacitatedVRP_Google.gif"  title="User demo" alt="User demo"/></td>
    </tr>
    <tr>
        <th>Haversine User example.</th>
    </tr>
    <tr>
        <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/VRP/CapacitatedVRP_Haversine.gif"  title="User demo" alt="User demo"/></td>
    </tr>
</table>

We see one example using the Googles' api to compute the distance matrix between nodes and to compute of routes between nodes. On the other hand, we see another example using the Haversine distances to compute the distance matrix between nodes and straight lines to draw the routes.


<table>
    <tr>
        <th>Api Usage</th>
    </tr>
    <tr>
        <td><img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/VRP/Googles_Api_Usage.png"  title="Usage." alt="Usage." /></td>
    </tr>
    <tr>
        <th>Cost of the Google Api call</th>
    </tr>
    <tr>
        <td><img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/VRP/Googles_Api_Cost.png"  title="Cost." alt="Cost." /></td>
    </tr>
</table>
In the table above the cost to use the Googles Api is shown.
Keep in mind that for the example, we called the API every time the ACO finds a new solution.
Ideally, we would call the API only once the solution has converged, leading to notably lower costs.

## Comment:
This CVRP is a classical example that uses the classes and structures of the OptimizationLib.aco package. It is a sample that mixes real time optimization with user interface modifications and API call responses. :scream_cat:
