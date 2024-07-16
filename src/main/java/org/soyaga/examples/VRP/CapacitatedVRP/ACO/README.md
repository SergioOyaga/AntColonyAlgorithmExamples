# ACO
This folder is the backend of the Capacitated Vehicle Routing problem.

<table>
  <tr>
    <th> <b>Graph Design </b></th>
  </tr>
  <tr>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/VRP/TestResults/vrp_nodes_exaplanation.png"  title="Graph design." alt="Graph design." /></td>
  </tr>
</table>

## In this folder:
For this example, we decided to implement/extend many (10 of 22) of the classes that the OptimizationLib.aco library uses.
This ACO example explores the versatility of the library. We are able to connect the ACO with a GUI via events. In addition,
we implement a general approach for the CVRP, with a multi-objective function.

This folder contains different classes that define the structures required for solving the problem.
These classes implement their respective interfaces from OptimizationLib.aco.
1. [Ant](#ant): Implements Ant.
2. [BuilderEvaluator](#builderevaluator): Implements BuilderEvaluator.
3. [Constraints](#objectives): Implements Constraint.
4. [Graph](#graph): Implements GenericGraph.
5. [Nodes](#nodes): Extends Node.
6. [Edges](#edges): Extends Edge.
7. [Objectives](#objectives): Implements ObjectiveFunction.
8. [Stats](#stats): Implements Stat.
9. [MultiRouteSolution](#multiroutesolution): Extends Solution.
10. [CapacitatedVRPColonyAlgorithm](#capacitatedvrpcolonyalgorithm): Implements AntColonyAlgorithm.

In addition, one class that contains a problem-specific structure is included:
1. [ProblemStructures](#problemstructures): ACORoute class that represents a virtualTruck route.

Finally, a test run class.
1. [RunTest](#runtest): Main class that executes a test.


### [Ant](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO/Ant/ACOAnt.java)
Class that implements Ant. The buildSolution method has the following characteristics:
<ul>
    <li>The ant goes over the Selector nodes one by one until all the nodes have been served and all the trucks are in a CDC.</li>
    <li> In each loop, an ACORoute is built for a selected VirtualTruck.</li>
    <li> Each ACORoute is built until the truck arrives a CDC.</li>
    <li> When the truck is in a CDC, its reloaded.</li>
    <li> The ant avoids nodes which pending quantity is 0. It also tries to deliver as much as possible.</li>
</ul>

### [BuilderEvaluator](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO/BuilderEvaluator/NoPendingDemandClosedRoutesTrucksAtCDC.java)
Class that implements BuilderEvaluator. The ant stops building ACORoutes when all nodes have been served, and all trucks are in a CDC. Also, when we have used the maximum number of edges or if we have used all the Selector nodes.

### [Constraints](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO/Constraints)
Two classes that implement Constraint.
1. [RouteCoherence](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO/Constraints/RouteCoherence.java): evaluates if the solution ends each route in a CDC and, if a truck has multiple routes, also evaluates the continuity of the solution.
2. [ServiceConstraint](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO/Constraints/ServiceConstraint.java): evaluates if the solution has completely served all WaypointNodes.

### [Graph](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO/Graph/ACOGraph.java)
Class that extends GenericGraph.
Contains methods that allow the Controller to interact with the world.

### [Nodes](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO/Graph/Nodes)
Five classes that define all the different Nodes in the ACOGraph.
1. [ACONode](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO/Graph/Nodes/ACONode.java): abstract class that extends Node. Define the base structure of a Node for this problem.
2. [CDCNode](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO/Graph/Nodes/CDCNode.java): class that extends ACONode. Contains de CDC location.
3. [SelectorNode](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO/Graph/Nodes/SelectorNode.java): class that extends ACONode. Starting nodes for each loop in the ant's build process.
4. [VirtualTruck](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO/Graph/Nodes/VirtualTruck.java): class that extends ACONode. It does not have a fixed location it moves to build the multi-route.
5. [WaypointNode](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO/Graph/Nodes/WaypointNode.java): class that extends ACONode. It represents a location to visit.

### [Edges](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO/Graph/Edges/ACOEdge.java)
Class that extends Edge. Includes information about the real distances and durations, to be used by the objectives.

### [Objectives](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO/Objectives)
Four classes that compose the multi-objective solution.
1. [CostObjective](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO/Objectives/CostObjectives.java): Depending on the Truck used for each route, the cost might vary. It includes a variable and a fixed cost per truck.
2. [DistanceObjective](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO/Objectives/DistanceObjective.java): Computes the total distance covered by all the trucks.
3. [TimeObjective](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO/Objectives/TimeObjective.java): Compares the total working time for all trucks and returns the biggest one.
4. [ReloadPenalization](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO/Objectives/ReloadPenalization.java): Counts the number of reloads that are made by the trucks and penalize them slightly.

### [Stats](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/Stats)
This folder contains two classes that compute 2 stats:
1. [ImageStat](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/Stats/ImageStat.java):
   Stat that updates the pheromone image and fires an event to the CapacitatedVRPColonyAlgorithm that communicates this update to the outside world.
2. [RouteChangeStat](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/Stats/RouteChangeStat.java):
   Stat that updates the best current solution and fires an event to the CapacitatedVRPColonyAlgorithm that communicates this update to the outside world.

### [MultiRouteSolution](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO/MultiRouteSolution.java)
This class extends Solution. Stores the information of the ACORoutes and the quantities related to the trucks.


### [CapacitatedVRPColonyAlgorithm](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO/CapacitatedVRPColonyAlgorithm.java):
This class extends AntColonyAlgorithm, which by extension makes it an Optimizer instance. In other words, this class
can be optimized and its results can be gathered. It also implements Runnable and PropertyChangeListener to run in a thread and to communicate changes to the outside.

````code
public void optimize(){...}
public Object getResult(Object ... resultArgs){...}
````

The <i>optimize</i> method is reimplemented to allow the front to stop/pause the optimization when required by the Controller class. The <i>getResults</i> function returns a String meaning that the optimization has ended.

### [ProblemStructures](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO/ProblemStructures/ACORoute.java)
[ACORoute](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO/ProblemStructures/ACORoute.java) class contains the information of the nodes to visit and the edges used to visit them. It is "intelligent" because it knows which type of nodes to store, leaving out the selectors and the virtual trucks.

### [RunTest](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/ACO/RunTest.java)
This is the main class for the test. Is where the run starts. As simple as instantiate the CapacitatedVRPColonyAlgorithm object
(previously defined) filled with its components, optimize it, and retrieve the results.

The specific components for the CapacitatedVRPColonyAlgorithm include:
- GenericWorld: A World that contains a Graph and a PheromoneContainer.
    - ACOGraph: The Graph previously defined.
- Colony.
- ACOInitializer: An Initializer that creates N ants in a colony.
    - ACOAnt: The Ant previously defined.
        - MultiRouteSolution: previously defined.
            - MultiObjective: An ObjectiveFunction that evaluates multiple objectives.
              - Array of objectives. Using the Objectives previously defined.
              - WeightedNormScalarize: a Scalarize that computes a scalar from the multi-objective.
            - ConstraintBasedFeasibilityFunction: a feasibility function built with constraints, using the constraints previously defined.
            - NoPendingDemandClosedRoutesTrucksAtCDC: A BuilderEvaluator previously defined.
        - RandomProportionalEdgeSelector: An EdgeSelector that allows the ant to choose between the available edges.
- SimpleConstructorPolicy: A ConstructorPolicy, ant by ant build the solution.
- SimpleUpdatePheromonePolicy: A PheromonePolicy, first evaporate, then add.
    - MaxGlobalBestFitnessProportionalAddPheromonePolicy: An AddPheromonePolicy that adds pheromone (up to a maximum) to the edges proportionally to the best global solution fitness.
    - MinPercentageEvaporatePheromonePolicy: An EvaporatePheromonePolicy that evaporates a certain percentage of the pheromone on each edge (down to a minimum).
- NIterationsStatsRetrievalPolicy: A StatsRetrievalPolicy that retrieves Stats every N iterations.
    - CurrentMinFitnessStat: A Stat that retrieves the current colony's best fitness.
    - CurrentMaxFitnessStat: A Stat that retrieves the current colony's worst fitness.
    - HistoricalMinFitnessStat: A Stat that retrieves the historical best fitness.
    - MeanSdFitnessStat: A Stat that retrieves the colony fitness mean and STD.
    - MeanSdPheromoneStat: A Stat that retrieves the colony pheromone mean and STD.
    - ImageStat: A Stat that fires an event with a new pheromone graph image.
    - RouteChangeStat: A Stat that fires an event with the best solution when the best solution has changed.

For this example, the world consists of a series of SelectorNodes (as many as WaypointNodes in the problem), connected to all VirtualTrucks. These connections have length 0, and the VirtualTrucks don't have outgoingEdges. The ant is in charge of detecting that we have selected a VirtualTruck, and that its location has to change to the currentPosition of the truck.

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
We see that the solution changes depending on the relevancy of each objective. When we minimize the distance, the shortest route appears. When we optimize the time, the trucks distribute the workload. When optimizing the cost, the Truck1 (The one with higher variable cost) made the shortest path to a CDC, and the remaining Waypoints are covered by the other truck.

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
</table>
We see that the solution converges to the best possible routes.

So as a conclusion of this test, we see that the ACO properly optimizes the problem.


## Comment:
This solution is a test developed as an intermediate step for the CapacitatedVRP with real time optimization and Google API calls for the routing and data gather. :Scream_cat:

