# ACO

This folder contains the backend code for the Capacitated Vehicle Routing problem.

<table>
  <tr>
    <th> <b>Graph Design </b></th>
  </tr>
  <tr>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/VRP/TestResults/vrp_nodes_exaplanation.png"  title="Graph design." alt="Graph design." /></td>
  </tr>
</table>

## In This Folder:

For this example, we decided to implement/extend many (10 of 22) of the classes that the OptimizationLib.aco library uses. This ACO example explores the versatility of the library. We can connect the ACO with a GUI via events. In addition, we implement a general approach for the CVRP, with a multi-objective function.

This folder contains different classes that define the structures required for solving the problem. These classes implement their respective interfaces from OptimizationLib.aco:

1.  [Ant](#ant): Implements Ant.
2.  [BuilderEvaluator](#builderevaluator): Implements BuilderEvaluator.
3.  [Constraints](#objectives): Implements Constraint.
4.  [Graph](#graph): Implements GenericGraph.
5.  [Nodes](#nodes): Extends Node.
6.  [Edges](#edges): Extends Edge.
7.  [Objectives](#objectives): Implements ObjectiveFunction.
8.  [Stats](#stats): Implements Stat.
9.  [MultiRouteSolution](#multiroutesolution): Extends Solution.
10. [CapacitatedVRPColonyAlgorithm](#capacitatedvrpcolonyalgorithm): Implements AntColonyAlgorithm.

In addition, one class contains a problem-specific structure:

1.  [ProblemStructures](#problemstructures): ACORoute class that represents a virtual truck route.

Finally, a test run class:

1.  [RunTest](#runtest): Main class that executes a test.

### [Ant](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO/Ant/ACOAnt.java):

Class that implements Ant. The `buildSolution` method has the following characteristics:

*   The ant goes over the selector nodes one by one until all the nodes have been served and all the trucks are in a CDC.
*   In each loop, an ACORoute is built for a selected virtual truck.
*   Each ACORoute is built until the truck arrives at a CDC.
*   When the truck is in a CDC, it is reloaded.
*   The ant avoids nodes whose pending quantity is 0. It also tries to deliver as much as possible.

### [BuilderEvaluator](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO/BuilderEvaluator/NoPendingDemandClosedRoutesTrucksAtCDC.java):

Class that implements BuilderEvaluator. The ant stops building ACORoutes when all nodes have been served and all trucks are in a CDC. Also, when the maximum number of edges has been used or if all the selector nodes have been used.

### [Constraints](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO/Constraints):

Two classes that implement Constraint:

1.  [RouteCoherence](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO/Constraints/RouteCoherence.java): Evaluates if the solution ends each route in a CDC and, if a truck has multiple routes, also evaluates the continuity of the solution.
2.  [ServiceConstraint](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO/Constraints/ServiceConstraint.java): Evaluates if the solution has completely served all WaypointNodes.

### [Graph](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO/Graph/ACOGraph.java):

Class that extends GenericGraph. Contains methods that allow the controller to interact with the world.

### [Nodes](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO/Graph/Nodes):

Five classes that define all the different nodes in the ACOGraph:

1.  [ACONode](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO/Graph/Nodes/ACONode.java): Abstract class that extends Node. Defines the base structure of a node for this problem.
2.  [CDCNode](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO/Graph/Nodes/CDCNode.java): Class that extends ACONode. Contains the CDC location.
3.  [SelectorNode](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO/Graph/Nodes/SelectorNode.java): Class that extends ACONode. Starting nodes for each loop in the ant's build process.
4.  [VirtualTruck](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO/Graph/Nodes/VirtualTruck.java): Class that extends ACONode. It does not have a fixed location; it moves to build the multi-route.
5.  [WaypointNode](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO/Graph/Nodes/WaypointNode.java): Class that extends ACONode. It represents a location to visit.

### [Edges](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO/Graph/Edges/ACOEdge.java):

Class that extends Edge. Includes information about the real distances and durations to be used by the objectives.

### [Objectives](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO/Objectives):

Four classes that compose the multi-objective solution:

1.  [CostObjective](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO/Objectives/CostObjective.java): Depending on the truck used for each route, the cost might vary. It includes a variable and a fixed cost per truck.
2.  [DistanceObjective](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO/Objectives/DistanceObjective.java): Computes the total distance covered by all trucks.
3.  [TimeObjective](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO/Objectives/TimeObjective.java): Compares the total working time for all trucks and returns the longest one.
4.  [ReloadPenalization](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO/Objectives/ReloadPenalization.java): Counts the number of reloads made by the trucks and penalizes them slightly.

### [Stats](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO/Stats):

This folder contains two classes that compute two stats:

1.  [ImageStat](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO/Stats/ImageStat.java): Stat that updates the pheromone image and fires an event to the CapacitatedVRPColonyAlgorithm that communicates this update to the outside world.
2.  [RouteChangeStat](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO/Stats/RouteChangeStat.java): Stat that updates the best current solution and fires an event to the CapacitatedVRPColonyAlgorithm that communicates this update to the outside world.

### [MultiRouteSolution](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO/MultiRouteSolution.java):

This class extends Solution. Stores the information of the ACORoutes and the quantities related to the trucks.

### [CapacitatedVRPColonyAlgorithm](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO/CapacitatedVRPColonyAlgorithm.java):

This class extends AntColonyAlgorithm, which by extension makes it an Optimizer instance. In other words, this class can be optimized, and its results can be gathered. It also implements Runnable and PropertyChangeListener to run in a thread and to communicate changes to the outside.
````java
public void optimize(){...}
public Object getResult(Object ... resultArgs){...}
````
The *optimize* method is reimplemented to allow the frontend to stop/pause the optimization when required by the Controller class. The *getResults* function returns a string meaning that the optimization has ended.

### [ProblemStructures](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO/ProblemStructures/ACORoute.java):

[ACORoute](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO/ProblemStructures/ACORoute.java) class contains information about the nodes to visit and the edges used to visit them. It is "intelligent" because it knows which type of nodes to store, leaving out the selectors and the virtual trucks.

### [RunTest](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO/RunTest.java):

This is the main class for the test. It is where the run starts. It is as simple as instantiating the CapacitatedVRPColonyAlgorithm object (previously defined) filled with its components, optimizing it, and retrieving the results.

The specific components for the CapacitatedVRPColonyAlgorithm include:

*   GenericWorld: A world that contains a graph and a pheromone container.
    *   ACOGraph: The graph previously defined.
*   Colony.
*   ACOInitializer: An initializer that creates N ants in a colony.
    *   ACOAnt: The ant previously defined.
        *   MultiRouteSolution: Previously defined.
            *   MultiObjective: An ObjectiveFunction that evaluates multiple objectives.
                *   Array of objectives. Using the objectives previously defined.
                *   WeightedNormScalarize: A scalarize function that computes a scalar value from the multi-objective.
            *   ConstraintBasedFeasibilityFunction: A feasibility function built with constraints, using the constraints previously defined.
            *   NoPendingDemandClosedRoutesTrucksAtCDC: A BuilderEvaluator previously defined.
        *   RandomProportionalEdgeSelector: An EdgeSelector that allows the ant to choose between the available edges.
*   SimpleConstructorPolicy: A ConstructorPolicy; ants build the solution one by one.
*   SimpleUpdatePheromonePolicy: A PheromonePolicy; first, evaporate, then add.
    *   MaxGlobalBestFitnessProportionalAddPheromonePolicy: An AddPheromonePolicy that adds pheromone (up to a maximum) to the edges proportionally to the best global solution fitness.
    *   MinPercentageEvaporatePheromonePolicy: An EvaporatePheromonePolicy that evaporates a certain percentage of the pheromone on each edge (down to a minimum).
*   NIterationsStatsRetrievalPolicy: A StatsRetrievalPolicy that retrieves stats every N iterations.
    *   CurrentMinFitnessStat: A stat that retrieves the current colony's best fitness.
    *   CurrentMaxFitnessStat: A stat that retrieves the current colony's worst fitness.
    *   HistoricalMinFitnessStat: A stat that retrieves the historical best fitness.
    *   MeanSdFitnessStat: A stat that retrieves the colony fitness mean and standard deviation.
    *   MeanSdPheromoneStat: A stat that retrieves the colony pheromone mean and standard deviation.
    *   ImageStat: A stat that fires an event with a new pheromone graph image.
    *   RouteChangeStat: A stat that fires an event with the best solution when the best solution has changed.

For this example, the world consists of a series of SelectorNodes (as many as WaypointNodes in the problem), connected to all VirtualTrucks. These connections have length 0, and the VirtualTrucks do not have outgoing edges. The ant detects that we have selected a VirtualTruck and that its location has to change to the current position of the truck.

## Results:

<table>
    <tr>
    <th> <b>Inputs Trucks </b></th>
    <th> <b>Inputs CDCs </b></th>
  </tr>
  <tr>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/VRP/TestResults/input_trucks.png" title="Truck input." alt="Truck input." /></td>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/VRP/TestResults/input_cdcs.png" title="CDC input." alt="CDC input." /></td>
  </tr>
  <tr>
    <th> <b>Inputs Waypoints </b></th>
    <th> <b>Distance Solution</b></th>
  </tr>
  <tr>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/VRP/TestResults/input_waypoints.png" title="Waypoint input." alt="Waypoint input." /></td>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/VRP/TestResults/dist_solution.png" title="Distance solution." alt="Distance solution." /></td>
  </tr>
  <tr>
    <th> <b>Time Solution</b></th>
    <th> <b>Cost Solution</b></th>
  </tr>
  <tr>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/VRP/TestResults/time_solution.png" title="Time solution." alt="Time solution." /></td>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/VRP/TestResults/cost_solution.png" title="Cost solution." alt="Cost solution." /></td>
  </tr>
</table>
We see that the solution changes depending on the relevancy of each objective. When minimizing the distance, the shortest route appears. When optimizing for time, the trucks distribute the workload. When optimizing for cost, Truck1 (the one with a higher variable cost) takes the shortest path to a CDC, and the remaining waypoints are covered by the other truck.

<table>
    <tr>
    <th> <b>Pheromone Generation 1 </b></th>
    <th> <b>Pheromone Generation 50 </b></th>
  </tr>
  <tr>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/VRP/TestResults/aco_test_pheromone_1.png" title="Generation 1." alt="Generation 1." /></td>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/VRP/TestResults/aco_test_pheromone_50.png" title="Generation 50." alt="Generation 50." /></td>
  </tr>
  <tr>
    <th> <b>Pheromone Generation 102 </b></th>
    <th> <b>Pheromone Generation 201</b></th>
  </tr>
  <tr>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/VRP/TestResults/aco_test_pheromone_102.png" title="Generation 102." alt="Generation 102." /></td>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/VRP/TestResults/aco_test_pheromone_201.png" title="Generation 201." alt="Generation 201." /></td>
  </tr>
</table>We see that the solution converges to the best possible routes. As a conclusion to this test, we see that the ACO properly optimizes the problem.

## Comment:
This solution is a test developed as an intermediate step for the CapacitatedVRP with real-time optimization and Google API calls for routing and data gathering. :scream_cat:

