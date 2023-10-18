# Complex
This Quadratic Assignment Problem (QAP) is a variant of the original QAP. In this case, we've introduced a more general 
scenario where the number of facilities and locations to assign is undetermined, and the optimization focuses on not 
only the "distance" but also transportation time and costs.

## Problem Description

A custom company is planning for growth in the coming years. The forecast predicts an increased flow of units in the
system, expected to range between 8,000 and 10,000 units per week in the vicinity of a city. With their current
infrastructure, which consists of three facilities located in three different locations, the company is confident that
they won't meet the minimum number of units from the forecast.

As a result, the logistics team has identified a set of potential locations and possible facilities for each location.
The remaining question is which locations and facilities the company should open to cover the expected flow.
Additionally, the company is concerned not only with the flow but also with minimizing costs (to improve margins),
reducing transportation times (for a better customer experience), and minimizing distances
(with environmental considerations).

<table>
  <tr>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/resources/QAP/custom_map.png"  title="Locations Map." alt="Locations Map." width="500" height="500" /></td>
  </tr>
  <tr>
    <td>
        The problem has the following properties:
        <ul>
          <li>There are twelve potential locations, each with different attributes. You can view the details in the following files: 
<a href="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/resources/QAP/customLocations.csv">Locations</a> 
,
<a href="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/resources/QAP/customLocationsDistances.csv">Distances</a> 
,
<a href="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/resources/QAP/customLocationsTimes.csv">TransportationTimes</a> 
and
<a href="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/resources/QAP/customLocationsCosts.csv">TransportationCosts</a>.
          </li>
          <li> There are nine facilities, including three fixed facilities associated with specific locations and six potential 
facilities that can be assigned to a set of specific locations as needed. Each facility has different properties. 
You can view the details in the 
<a href="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/resources/QAP/customFacilities.csv">Facilities</a> 
and
<a href="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/resources/QAP/customFacilitiesFlows.csv">Flows</a>
files.
          </li>
        </ul> 
    </td>
  </tr>
</table>

## Results
We solve the problem considering four different business cases. A balance between costs, transportation times and 
distances, only cost, only transportation time and only distance.

<table>
  <tr>
    <th>Case.</th>
    <th>Best solution.</th>
    <th>Iteration best solution.</th>
    <th>Pheromones.</th>
  </tr>
  <tr>
    <td> Balance.</td>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/QAP/CustomMap__Custom_Balance.gif"  title="Best Solution." alt="Best Solution." width="300" height="300" /></td>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/QAP/CurrentCustomMap__Custom_Balance.gif"  title="Iteration Solution." alt="Iteration Solution." width="300" height="300" /></td>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/QAP/Pheromone__Custom_Balance.gif"  title="Pheromone." alt="Pheromone." width="300" height="300" /></td>
  </tr>
  <tr>
    <td> Cost.</td>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/QAP/CustomMap__Custom_Cost.gif"  title="Best Solution." alt="Best Solution." width="300" height="300" /></td>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/QAP/CurrentCustomMap__Custom_Cost.gif"  title="Iteration Solution." alt="Iteration Solution." width="300" height="300" /></td>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/QAP/Pheromone__Custom_Cost.gif"  title="Pheromone." alt="Pheromone." width="300" height="300" /></td>
  </tr>
  <tr>
    <td> Time.</td>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/QAP/CustomMap__Custom_Time.gif"  title="Best Solution." alt="Best Solution." width="300" height="300" /></td>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/QAP/CurrentCustomMap__Custom_Time.gif"  title="Iteration Solution." alt="Iteration Solution." width="300" height="300" /></td>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/QAP/Pheromone__Custom_Time.gif"  title="Pheromone." alt="Pheromone." width="300" height="300" /></td>
  </tr>
  <tr>
    <td> Distance.</td>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/QAP/CustomMap__Custom_Dist.gif"  title="Best Solution." alt="Best Solution." width="300" height="300" /></td>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/QAP/CurrentCustomMap__Custom_Dist.gif"  title="Iteration Solution." alt="Iteration Solution." width="300" height="300" /></td>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/QAP/Pheromone__Custom_Dist.gif"  title="Pheromone." alt="Pheromone." width="300" height="300" /></td>
  </tr>
</table>

In the images above, we can observe four different results that minimize various objective functions. 
Depending on the scenario, we may need to consider opening different facilities in different locations. 
We provide edge cases, along with a balanced case. This will empower the business team to determine which case yields
a more favorable outcome, and we can subsequently fine-tune the weights for each aspect of the multi-objective function.


### Results Analysis
In this section, we plot some graphs to analyze the results.
<table>
  <tr>
    <td><img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/QAP/Custom_Cost.png"  title="Costs by scenario." alt="Costs by scenario." width="500" height="300" /> </td>
    <td><img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/QAP/Custom_Dist.png"  title="Dist by scenario." alt="Dist by scenario." width="500" height="300" /> </td>
  </tr>
  <tr>
    <td><img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/QAP/Custom_Time.png"  title="Time by scenario." alt="Time by scenario." width="500" height="300" /> </td>
    <td><img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/QAP/Custom_MeanTime.png"  title="Mean time by scenario." alt="Mean time by scenario." width="500" height="300" /> </td>
  </tr>
  <tr>
    <td><img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/QAP/Custom_Flow.png"  title="Flow by scenario." alt="Flow by scenario." width="500" height="300" /> </td>
    <td><img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/QAP/Custom_MeanFlow.png"  title="Mean flow by scenario." alt="Mean flow by scenario." width="500" height="300" /> </td>
  </tr>
</table>

In each of the images above, we compare the values of either the cost, the distance, the time, or the flow between 
different optimization scenarios (balance, cost, time, dist). As we can see, when examining the cost, the best cost 
value is achieved in the cost optimization scenario. The same holds true for the distance and time. It is easiest to 
identify in the time scenario, as the time value is significantly lower than in the other scenarios.

Additionally, we included the mean time value of all the ants in each iteration to observe how the colony tends to 
converge toward lower and stable values for the balance and time scenarios, while exhibiting higher and unstable values 
for the cost and distance scenarios.

The flow plot allows us to identify cases that may seem like better solutions in terms of objective value but fall 
outside the business-imposed min-max values. In fact, all scenarios yield solutions where the flow value meets the 
minimum requirement. This is because we haven't included any profit value associated with the flow of units in the 
system (something that can be easily incorporated following the same principles used for each part of the objective 
function).

The mean flow plot is presented because it provides an interesting insight. We can observe that all means are below the 
minimum flow requirement. This indicates that the proposed solutions at the end of the optimization, expected to be 
similar to the best found, fall below the minimum requirement. However, we notice that the time scenario is considerably
lower than the rest. This suggests that the proposed solution is more unstable, and any operational change in the plan 
can significantly impact the network compared to other configurations. For example, if one of the airport locations has 
to close due to adverse weather conditions, the network will be profoundly affected.

## In this folder:
This folder contains 6 different classes that define the structures required for solving the problem and 10 classes
used to extract statistics and create GIFs.
These classes implement their respective interfaces from OptimizationLib.aco.
1. [CustomQapAntColonyAlgorithm](#customqapantcolonyalgorithm): Extends StatsAntColonyAlgorithm.
2. [CustomCostObjective](#customcostobjective): Implements ObjectiveFunction.
3. [CustomConstraint](#customconstraint): Implements Constraint.
4. [CustomBuilderEvaluator](#custombuilderevaluator): Implements BuilderEvaluator.
5. [CustomAnt](#customant): Implements Ant.
6. [RunCustomQapOptimization](#runcustomqapoptimization): The main class for instantiation and optimization.

The classes dedicated to retrieve stats all implement Stat.
1. [CurrentMinCostCustomStat](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/QAP/Complex/Stats/CurrentMinCostCustomStat.java):
   Current iteration best solution's cost.
2. [CurrentMinDistanceCustomStat](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/QAP/Complex/Stats/CurrentMinDistanceCustomStat.java):
   Current iteration best solution's distance.
3. [CurrentMinFlowCustomStat](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/QAP/Complex/Stats/CurrentMinFlowCustomStat.java):
   Current iteration best solution's flow.
4. [CurrentMinImageStat](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/QAP/Complex/Stats/CurrentMinImageStat.java):
   Current iteration best solution GIF creator.
5. [CurrentMinTimeCustomStat](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/QAP/Complex/Stats/CurrentMinTimeCustomStat.java):
   Current iteration best solution's time.
6. [ImageStat](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/QAP/Complex/Stats/ImageStat.java):
   Historical best solution GIF creator.
7. [MeanSdCostStat](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/QAP/Complex/Stats/MeanSdCostStat.java):
   Historical best solution's cost.
8. [MeanSdDistanceStat](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/QAP/Complex/Stats/MeanSdDistanceStat.java):
   Historical best solution's distance.
9. [MeanSdFlowStat](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/QAP/Complex/Stats/MeanSdFlowStat.java):
   Historical best solution's flow.
10. [MeanSdTimeStat](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/QAP/Complex/Stats/MeanSdTimeStat.java):
    Historical best solution's time.

### [CustomQapAntColonyAlgorithm](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/QAP/Complex/CustomQapAntColonyAlgorithm.java):
This class extends StatsAntColonyAlgorithm, which by extension makes it an Optimizer instance. In other words, this class
can be optimized and its results can be gathered.

````code
public void optimize(){...}
public Object getResult(Object... resultArgs){...}
````
The <i>optimize</i> method is inherited from the abstract class StatsAntColonyAlgorithm, which defines a basic optimization
procedure. The <i>getResults</i> function returns a String that will be printed in the screen with the assignations.

### [CustomCostObjective](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/QAP/Complex/CustomCostObjective.java):
This class, implementing ObjectiveFunction, is used to compute the cost of a solution. The cost is defined as the sum 
of several components, including the transportation cost, the cost of purchasing the utilized locations, and the cost 
of setting up the specific facilities in those locations.

For the time and distance objectives, we make use of the built-in class QapObjectiveFunction, as the objective value 
for each of these objectives is calculated as the sum of flows multiplied by time or distances between the used 
facilities-locations.

These three objectives are evaluated collectively within a multi-objective perspective, utilizing a weighted L1 
scalarization technique.

### [CustomConstraint](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/QAP/Complex/CustomConstraint.java):
This class, implementing Constraint, is employed to establish both the minimum and maximum flow thresholds required for 
a satisfactory solution. In essence, solutions with unit flow values outside this defined range are heavily penalized 
in terms of fitness.

In the [images](#results-analysis), you can observe how the flow tends to increase until acceptable values are attained,
even if it means compromising the objective function's value.

### [CustomBuilderEvaluator](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/QAP/Complex/CustomBuilderEvaluator.java):
This class, implementing BuilderEvaluator, serves the purpose of evaluating when an ant has completed the construction 
of a solution in the graph. In this context, an ant deems a solution as finished when a node in the graph is visited 
twice. This criterion is chosen because it is illogical to use a node twice: a facility cannot be assigned to two 
different locations, and likewise, two distinct facilities cannot be assigned to the same location.

### [CustomAnt](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/QAP/Complex/CustomAnt.java):
This class, implementing Ant, is used as an agent that builds a solution step by step. In this case, the ant has a 
memory of specific nodes. This means that a Custom ant cannot visit twice certain nodes. For the purpose of the problem, 
locations can only be visited once. If the ant is in a facility node, a new location will be selected.

This class, implementing Ant, acts as an agent responsible for constructing a solution step by step. In this particular 
case, the ant possesses memory of specific nodes, ensuring that a CustomAnt cannot revisit certain nodes. For the 
problem's context, it's essential that locations are visited only once. If the ant happens to be at a facility node, it 
will select a new location to visit. But if the ant is in a location node, any facility can be selected. When a repeated
facility is selected, the [CustomBuilderEvaluator](#custombuilderevaluator) will stop the build process.


### [RunCustomQapOptimization](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/QAP/Complex/RunCustomQapOptimization.java):
This is the main class. Is where the run starts. As simple as instantiate the RunCustomQapOptimization object 
(previously defined) filled with its components, optimize it, and retrieve the results.

The specific components for the RunCusto mQapOptimization are:
- GenericWorld: A World that contains a Graph and a PheromoneContainer.
  - GenericGraph: A Graph that stores the centrality and pheromone into in a graph like object.
- Colony.
- MaxIterationCriteriaPolicy: A StoppingCriteria based on a maximum number of iterations.
- ACOInitializer: An Initializer that creates N ants in a colony.
  - CustomAnt: Ant previously defined.
    - Solution.
      - MultiObjective: An Objective function that evaluates multiple objective functions.
        - objectiveArray: An ArrayList containing the individual ObjectiveFunctions:
          1. CustomCostObjective: An ObjectiveFunction previously defined.
          2. QapObjectiveFunction: An ObjectiveFunction that evaluates the ant solution using a QAP like objective function for the time.
          3. QapObjectiveFunction: An ObjectiveFunction that evaluates the ant solution using a QAP like objective function for the distance.
        - WeightedNormScalarize: An Scalarize that weights each objective and computes a norm over the weighted values.
      -  ConstraintBasedFeasibilityFunction: A FeasibilityFunction that is based on constraints.
        - CustomConstraint: A Constraint previously defined.
      - CustomBuilderEvaluator: A BuilderEvaluator previously defined.
    - RandomProportionalEdgeSelector: An EdgeSelector that allows the ant to choose between the available edges.
- SimpleConstructorPolicy: A ConstructorPolicy, ant by ant build the solution.
- SimpleUpdatePheromonePolicy: A PheromonePolicy, first evaporate, then add.
  - MaxGlobalBestFitnessProportionalAddPheromonePolicy: An AddPheromonePolicy that adds pheromone to the edges proportionally to the
    best historical solution fitness up to a maximum limit.
  - MinPercentageEvaporatePheromonePolicy: An EvaporatePheromonePolicy that evaporates a certain percentage of the 
    pheromone on each edge down to a minimum.
- NIterationsStatsRetrievalPolicy: A StatsRetrievalPolicy that retrieves Stats every N iterations.
  - CurrentMinFitnessStat: A Stat that retrieves the current colony's best fitness.
  - CurrentMaxFitnessStat: A Stat that retrieves the current colony's worst fitness.
  - HistoricalMinFitnessStat: A Stat that retrieves the historical colony's best fitness.
  - MeanSdFitnessStat: A Stat that retrieves the colony fitness mean and STD.
  - MeanSdPheromoneStat: A Stat that retrieves the pheromone mean and STD in the PheromoneContainer.
  - CurrentMinDistanceCustomStat: A Stat previously defined.
  - CurrentMinTimeCustomStat: A Stat previously defined.
  - CurrentMinCostCustomStat: A Stat previously defined.
  - CurrentMinFlowCustomStat: A Stat previously defined.
  - MeanSdDistanceStat: A Stat previously defined.
  - MeanSdTimeStat: A Stat previously defined.
  - MeanSdCostStat: A Stat previously defined.
  - MeanSdFlowStat: A Stat previously defined.
  - PheromoneContainerGIFStat: A Stat that generates a GIF of the pheromone evolution in the PheromoneContainer.
  - ImageStat: A Stat previously defined.
  - CurrentMinImageStat: A Stat previously defined.

On the other hand, the majority of the effort in this type of algorithms must be dedicated to constructing the World.

In this scenario, certain functions are specifically focused on building the World. This is necessary because we must represent
the problem in a graphical way, creating a structure for the ants to traverse the edges of the graph.

The next function builds the world.

````code
private static GenericWorld createWorld(
            Double initialPheromone, HashMap<String, Facility> facilitiesById, HashMap<String, Location> locationsById,
            Double distanceWeight, Double timeWeight, Double costWeight)
````
The World is built with one Graph/PheromoneContainer that illustrates centrality and pheromone between nodes. Each Node in
the Graph has associated a set of Edges that connects it with other Nodes. Those Edges contain the centrality and 
pheromone information. 

````mermaid
flowchart LR
subgraph  ide0 [World]
  direction LR
    subgraph  ide1 [Assignation]
    direction LR
        F1((F1)) --fixed assignation--> L1((L1))
        F2((F2)) --fixed assignation--> L2((L2))
        FX((FX)) --> LX((LX))
        FX((FX)) --> LN((LN))
        FN((FN)) --> LX((LX))
        FN((FN)) --> LN((LN))
    end

    subgraph  id21 [Order]
    direction LR
    LX_((LX)) --> FX_((FX))
    L2_((L2)) --> F1_((F1))
    LX_((LX)) --> F1_((F1))
    LN_((LN)) --> F1_((F1))
    L1_((L1)) --restriction--> F2_((F2))
    L2_((L2)) --> F2_((F2))
    L2_((L2)) --> FX_((FX))
    LN_((LN)) --> FX_((FX))
    L2_((L2)) --> FN_((FN))
    LX_((LX)) --> FN_((FN))
    LN_((LN)) --> FN_((FN))
    end 
    L1 -.- L1_
    L2 -.- L2_
    LX -.- LX_
    LN -.- LN_
  end
````

The distance of the edges in the Assignation part of the graph represents the heuristic distance, which reflects the 
attractiveness for the ant when selecting that edge. This attractiveness is computed in a manner similar to the classical
QAP problem, but it's calculated as a weighted sum of the attractiveness of each of the objective dimensions 
(cost, time, and distance). The weight assigned to each dimension corresponds to the weight used in the objective 
function.

In contrast, in the Order part of the graph, the distance is fixed at 1. This is because the order in which ants assign
facilities to locations is not a critical concern. It's important to note that the nodes in both the Assignation and 
Order sections share the same names because they correspond to the same nodes in the graph. This division into two 
parts is done to enhance clarity and understanding.



#### qapNode:
This auxiliary class extends Node from the Optimization.aco library and is used to store information about nodes. 
It includes the following details:

- ID (Integer): Contains the node number.

Additionally, two more classes extending qapNode are created to store information about facilities and locations.

**Location**:
- point: Point2D with the position on the map.
- description: String with a description.
- costToBuyLand: Double with the cost of using a specific location.
- locationDistances: HashMap of other locations with the distance from this location to other locations.
- locationTimes: HashMap of other locations with the times from this location to other locations.
- locationCosts: HashMap of other locations with the transportation costs from this location to other locations.
- restrictionRelation: String with the restrictions, specifying which facilities are connected to this location.

**Facility**:
- description: String with a description.
- costToBuild: Double with the cost of building a specific facility.
- facilityOutgoingFlow: HashMap of other facilities with the outgoing flow from this facility to other facilities.
- potentialLocationsIDs: HashSet of strings with the restrictions, specifying which locations are connected to this facility.



## Comment:
This problem presents a more realistic example of a QAP-like problem, where certain pre-existing constraints must 
always be met (such as the placement of the company's facilities in previous locations). There are varying numbers of 
locations and facility types available (with not all facility types suitable for all locations). 
Additionally, it involves a multi-objective function and several business-related constraints.

In this example, we didn't conduct an extensive fine-tuning of hyperparameters. Nevertheless, we were able to optimize
various scenarios and observe the convergence of solutions toward promising results. :smirk_cat:
