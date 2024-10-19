# Complex

The Quadratic Assignment Problem (QAP) presented here is a variant of the original QAP. In this case, we've introduced a more general scenario where the number of facilities and locations to assign is undetermined. The optimization focuses not only on "distance" but also on transportation time and costs.

## Problem Description

A custom company is planning for growth in the coming years. Forecasts predict an increased flow of units in the system, expected to range between 8,000 and 10,000 units per week in the vicinity of a city. With their current infrastructure, consisting of three facilities located in three different locations, the company is concerned that they won't be able to meet the minimum unit forecast.

Consequently, the logistics team has identified a set of potential locations and possible facilities for each location. The remaining question is which locations and facilities the company should open to accommodate the expected flow. Additionally, the company aims to minimize costs (to improve margins), reduce transportation times (for a better customer experience), and minimize distances (with environmental considerations).


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

In each of the images above, we compare values related to cost, distance, time, or flow across different optimization scenarios (balance, cost, time, distance). As observed, the best cost value is achieved in the cost optimization scenario. The same applies to distance and time, with the time scenario being the most straightforward to identify since its value is significantly lower than those in the other scenarios.

Additionally, we have included the mean time value of all the ants in each iteration to demonstrate how the colony tends to converge toward lower and more stable values in the balance and time scenarios, while exhibiting higher and more unstable values in the cost and distance scenarios.

The flow plot helps us identify cases that may appear to offer better solutions in terms of objective value but fall outside the business-imposed min-max constraints. Notably, all scenarios yield solutions where the flow value meets the minimum requirement. This is because we have not included any profit value associated with the flow of units in the system—something that can be easily incorporated following the same principles used for each part of the objective function.

We present the mean flow plot because it offers interesting insights. We observe that all means fall below the minimum flow requirement, indicating that the proposed solutions at the end of the optimization process, which are expected to resemble the best found, do not meet this minimum threshold. However, we notice that the time scenario is considerably lower than the others, suggesting that the proposed solution is more unstable. Any operational change in the plan could significantly impact the network compared to other configurations. For instance, if one of the airport locations must close due to adverse weather conditions, the network will be profoundly affected.

## In This Folder
This folder contains six different classes that define the structures required for solving the problem and ten classes used to extract statistics and create GIFs. These classes implement their respective interfaces from `OptimizationLib.aco`.

1. [CustomQapAntColonyAlgorithm](#customqapantcolonyalgorithm): Extends `StatsAntColonyAlgorithm`.
2. [CustomCostObjective](#customcostobjective): Implements `ObjectiveFunction`.
3. [CustomConstraint](#customconstraint): Implements `Constraint`.
4. [CustomBuilderEvaluator](#custombuilderevaluator): Implements `BuilderEvaluator`.
5. [CustomAnt](#customant): Implements `Ant`.
6. [RunCustomQapOptimization](#runcustomqapoptimization): The main class for instantiation and optimization.

The classes dedicated to retrieving statistics all implement `Stat`.

1. [CurrentMinCostCustomStat](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/QAP/Complex/Stats/CurrentMinCostCustomStat.java): Current iteration's best solution cost.
2. [CurrentMinDistanceCustomStat](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/QAP/Complex/Stats/CurrentMinDistanceCustomStat.java): Current iteration's best solution distance.
3. [CurrentMinFlowCustomStat](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/QAP/Complex/Stats/CurrentMinFlowCustomStat.java): Current iteration's best solution flow.
4. [CurrentMinImageStat](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/QAP/Complex/Stats/CurrentMinImageStat.java): Current iteration's best solution GIF creator.
5. [CurrentMinTimeCustomStat](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/QAP/Complex/Stats/CurrentMinTimeCustomStat.java): Current iteration's best solution time.
6. [ImageStat](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/QAP/Complex/Stats/ImageStat.java): Historical best solution GIF creator.
7. [MeanSdCostStat](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/QAP/Complex/Stats/MeanSdCostStat.java): Historical best solution cost.
8. [MeanSdDistanceStat](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/QAP/Complex/Stats/MeanSdDistanceStat.java): Historical best solution distance.
9. [MeanSdFlowStat](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/QAP/Complex/Stats/MeanSdFlowStat.java): Historical best solution flow.
10. [MeanSdTimeStat](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/QAP/Complex/Stats/MeanSdTimeStat.java): Historical best solution time.

### [CustomQapAntColonyAlgorithm](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/QAP/Complex/CustomQapAntColonyAlgorithm.java)
This class extends `StatsAntColonyAlgorithm`, thereby making it an instance of `Optimizer`. In other words, this class can be optimized, and its results can be gathered.

````java
public void optimize(){...}
public Object getResult(Object... resultArgs){...}
````
The `<i>optimize</i>` method is inherited from the abstract class `StatsAntColonyAlgorithm`, which defines a basic optimization procedure. The `<i>getResults</i>` function returns a string that will be printed on the screen with the assignments.

### [CustomCostObjective](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/QAP/Complex/CustomCostObjective.java)
This class, which implements `ObjectiveFunction`, is used to compute the cost of a solution. The cost is defined as the sum of several components, including transportation costs, the costs of purchasing the utilized locations, and the costs associated with setting up the specific facilities in those locations.

For the time and distance objectives, we utilize the built-in class `QapObjectiveFunction`, as the objective value for each of these goals is calculated as the sum of flows multiplied by the time or distances between the used facilities and locations.

These three objectives are evaluated collectively within a multi-objective framework, employing a weighted L1 scalarization technique.

### [CustomConstraint](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/QAP/Complex/CustomConstraint.java)
This class, which implements `Constraint`, is used to establish both minimum and maximum flow thresholds required for a satisfactory solution. Essentially, solutions with unit flow values outside this defined range are heavily penalized in terms of fitness.

In the [images](#results-analysis), you can observe how the flow tends to increase until acceptable values are achieved, even if it compromises the objective function's value.

### [CustomBuilderEvaluator](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/QAP/Complex/CustomBuilderEvaluator.java)
This class, which implements `BuilderEvaluator`, is responsible for evaluating when an ant has completed the construction of a solution in the graph. An ant considers a solution finished when a node in the graph is visited twice. This criterion is logical because a facility cannot be assigned to two different locations, and two distinct facilities cannot be assigned to the same location.

### [CustomAnt](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/QAP/Complex/CustomAnt.java)
This class, which implements `Ant`, serves as an agent that builds a solution step by step. In this case, the ant retains memory of specific nodes, ensuring that a `CustomAnt` cannot revisit certain nodes. For the problem's context, it is crucial that locations are visited only once. If the ant is located at a facility node, it will select a new location to visit. However, if the ant is at a location node, it can choose any facility. If a facility is selected that has already been assigned, the [CustomBuilderEvaluator](#custombuilderevaluator) will halt the build process.

### [RunCustomQapOptimization](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/QAP/Complex/RunCustomQapOptimization.java)
This is the main class where the execution begins. It involves simply instantiating the `RunCustomQapOptimization` object (previously defined), filling it with its components, optimizing it, and retrieving the results.

The specific components for `RunCustomQapOptimization` include:

- **GenericWorld**: A world that contains a graph and a pheromone container.
    - **GenericGraph**: A graph that stores centrality and pheromone information in a graph-like structure.
- **Colony**: The group of ants.
- **MaxIterationCriteriaPolicy**: A stopping criterion based on a maximum number of iterations.
- **ACOInitializer**: An initializer that creates N ants in a colony.
    - **CustomAnt**: The ant defined earlier.
        - **Solution**:
            - **MultiObjective**: An objective function that evaluates multiple objectives.
                - **objectiveArray**: An `ArrayList` containing individual `ObjectiveFunctions`:
                    1. **CustomCostObjective**: The previously defined objective function.
                    2. **QapObjectiveFunction**: An objective function that evaluates the ant's solution using a QAP-like objective function for time.
                    3. **QapObjectiveFunction**: Another objective function that evaluates the ant's solution using a QAP-like objective function for distance.
                - **WeightedNormScalarize**: A scalarization technique that weights each objective and computes a norm over the weighted values.
            - **ConstraintBasedFeasibilityFunction**: A feasibility function based on constraints.
                - **CustomConstraint**: The previously defined constraint.
            - **CustomBuilderEvaluator**: The previously defined builder evaluator.
        - **RandomProportionalEdgeSelector**: An edge selector that allows the ant to choose among available edges.
- **SimpleConstructorPolicy**: A constructor policy that builds the solution ant by ant.
- **SimpleUpdatePheromonePolicy**: A pheromone policy that first evaporates pheromone and then adds it.
    - **MaxGlobalBestFitnessProportionalAddPheromonePolicy**: An add pheromone policy that adds pheromone to edges proportionally based on the best historical solution fitness, up to a maximum limit.
    - **MinPercentageEvaporatePheromonePolicy**: An evaporate pheromone policy that evaporates a certain percentage of pheromone on each edge down to a minimum.
- **NIterationsStatsRetrievalPolicy**: A stats retrieval policy that gathers statistics every N iterations.
    - **CurrentMinFitnessStat**: A stat that retrieves the current colony's best fitness.
    - **CurrentMaxFitnessStat**: A stat that retrieves the current colony's worst fitness.
    - **HistoricalMinFitnessStat**: A stat that retrieves the historical colony's best fitness.
    - **MeanSdFitnessStat**: A stat that retrieves the mean and standard deviation of colony fitness.
    - **MeanSdPheromoneStat**: A stat that retrieves the mean and standard deviation of pheromone in the pheromone container.
    - **CurrentMinDistanceCustomStat**: A previously defined stat.
    - **CurrentMinTimeCustomStat**: A previously defined stat.
    - **CurrentMinCostCustomStat**: A previously defined stat.
    - **CurrentMinFlowCustomStat**: A previously defined stat.
    - **MeanSdDistanceStat**: A previously defined stat.
    - **MeanSdTimeStat**: A previously defined stat.
    - **MeanSdCostStat**: A previously defined stat.
    - **MeanSdFlowStat**: A previously defined stat.
    - **PheromoneContainerGIFStat**: A stat that generates a GIF of pheromone evolution in the pheromone container.
    - **ImageStat**: A previously defined stat.
    - **CurrentMinImageStat**: A previously defined stat.

The majority of the effort in this type of algorithm must be dedicated to constructing the world.

In this scenario, certain functions are specifically focused on building the world. This is necessary to represent the problem graphically, creating a structure for the ants to traverse the edges of the graph.

The next function builds the world.

````java
private static GenericWorld createWorld(
            Double initialPheromone, HashMap<String, Facility> facilitiesById, HashMap<String, Location> locationsById,
            Double distanceWeight, Double timeWeight, Double costWeight)
````
The World is constructed using a single Graph and PheromoneContainer, which illustrate the centrality and pheromone levels between nodes. Each Node in the Graph is associated with a set of Edges that connect it to other Nodes. These Edges contain information about centrality and pheromone levels.

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

The distance of the edges in the Assignation part of the graph represents the heuristic distance, reflecting the attractiveness for the ant when selecting that edge. This attractiveness is computed similarly to the classical QAP problem but is calculated as a weighted sum of the attractiveness of each objective dimension (cost, time, and distance). The weight assigned to each dimension corresponds to the weight used in the objective function.

In contrast, the Order part of the graph has a fixed distance of 1. This is because the order in which ants assign facilities to locations is not a critical concern. It's important to note that the nodes in both the Assignation and Order sections share the same names, as they correspond to the same nodes in the graph. This division into two parts enhances clarity and understanding.

### qapNode:
This auxiliary class extends the Node class from the Optimization.aco library and is used to store information about nodes. It includes the following details:

- **ID (Integer)**: Contains the node number.

Additionally, two more classes extend qapNode to store information about facilities and locations.

**Location**:
- **point**: Point2D indicating the position on the map.
- **description**: A String providing a description.
- **costToBuyLand**: A Double representing the cost of using a specific location.
- **locationDistances**: A HashMap of other locations, indicating the distance from this location to others.
- **locationTimes**: A HashMap of other locations, indicating the time from this location to others.
- **locationCosts**: A HashMap of other locations, representing the transportation costs from this location to others.
- **restrictionRelation**: A String specifying restrictions that indicate which facilities are connected to this location.

**Facility**:
- **description**: A String providing a description.
- **costToBuild**: A Double representing the cost of building a specific facility.
- **facilityOutgoingFlow**: A HashMap of other facilities, indicating the outgoing flow from this facility to others.
- **potentialLocationsIDs**: A HashSet of Strings specifying restrictions that indicate which locations are connected to this facility.

## Comment:
This problem presents a more realistic example of a QAP-like problem, where certain pre-existing constraints must always be met (such as the placement of the company's facilities in previous locations). There is a varying number of locations and facility types available, with not all facility types being suitable for all locations. Additionally, it involves a multi-objective function and several business-related constraints.

In this example, we did not conduct extensive fine-tuning of hyperparameters. Nevertheless, we were able to optimize various scenarios and observe the convergence of solutions toward promising results. :smirk_cat:
