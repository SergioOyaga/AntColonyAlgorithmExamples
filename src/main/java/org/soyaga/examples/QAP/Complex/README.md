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
    <td style='width: 400px'> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/resources/QAP/custom_map.png"  title="Locations Map." alt="Locations Map." width="400" height="400" /></td>
    <td style='width: 600px'>
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
    <th>Scenario.</th>
    <th>Best solution.</th>
    <th>Iteration best solution.</th>
    <th>Pheromones.</th>
  </tr>
  <tr>
    <td> Balanced.</td>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/QAP/CustomMap__Custom_Balance.gif"  title="Best Solution." alt="Best Solution." width="400" height="400" /></td>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/QAP/CurrentCustomMap__Custom_Balance.gif"  title="Iteration Solution." alt="Iteration Solution." width="400" height="400" /></td>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/QAP/Pheromone__Custom_Balance.gif"  title="Pheromone." alt="Pheromone." width="400" height="400" /></td>
  </tr>
  <tr>
    <td> Costs.</td>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/QAP/CustomMap__Custom_Cost.gif"  title="Best Solution." alt="Best Solution." width="400" height="400" /></td>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/QAP/CurrentCustomMap__Custom_Cost.gif"  title="Iteration Solution." alt="Iteration Solution." width="400" height="400" /></td>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/QAP/Pheromone__Custom_Cost.gif"  title="Pheromone." alt="Pheromone." width="400" height="400" /></td>
  </tr>
  <tr>
    <td> Times.</td>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/QAP/CustomMap__Custom_Time.gif"  title="Best Solution." alt="Best Solution." width="400" height="400" /></td>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/QAP/CurrentCustomMap__Custom_Time.gif"  title="Iteration Solution." alt="Iteration Solution." width="400" height="400" /></td>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/QAP/Pheromone__Custom_Time.gif"  title="Pheromone." alt="Pheromone." width="400" height="400" /></td>
  </tr>
  <tr>
    <td> Distances.</td>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/QAP/CustomMap__Custom_Dist.gif"  title="Best Solution." alt="Best Solution." width="400" height="400" /></td>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/QAP/CurrentCustomMap__Custom_Dist.gif"  title="Iteration Solution." alt="Iteration Solution." width="400" height="400" /></td>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/QAP/Pheromone__Custom_Dist.gif"  title="Pheromone." alt="Pheromone." width="400" height="400" /></td>
  </tr>
</table>

In the images above, we can observe four different results that minimize various objective functions. Depending on the 
scenario, we may wish to open different facilities in different locations. We provide edge cases, along with a 
balanced case. This will enable the business team to determine which case exhibits a more favorable outcome, and we 
can then fine-tune the weights for each aspect of the multi-objective function.


## In this folder:
This folder contains 2 different classes that define the structures required for solving the problem.
These classes implement their respective interfaces from OptimizationLib.aco.
1. [GenericGraphQapAntColonyAlgorithm](#genericgraphqapantcolonyalgorithm): Extends SimpleAntColonyAlgorithm.
2. [RunGenericGraphQapOptimization](#rungenericgraphqapoptimization): The main class for instantiation and optimization.

### [GenericGraphQapAntColonyAlgorithm](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/QAP/GenericGraph/GenericGraphQapAntColonyAlgorithm.java):
This class extends StatsAntColonyAlgorithm, which by extension makes it an Optimizer instance. In other words, this class
can be optimized and its results can be gathered.

````code
public void optimize(){...}
public Object getResult(Object... resultArgs){...}
````
The <i>optimize</i> method is inherited from the abstract class StatsAntColonyAlgorithm, which defines a basic optimization
procedure. The <i>getResults</i> function returns a String that will be printed in the screen with the assignations.

### [RunGenericGraphQapOptimization](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/QAP/GenericGraph/RunGenericGraphQapOptimization.java):
This is the main class. Is where the run starts. As simple as instantiate the RunGenericGraphTspOptimization object 
(previously defined) filled with its components, optimize it, and retrieve the results.

The specific components for the RunGenericGraphQapOptimization are:
- GenericWorld: A World that contains a Graph and a PheromoneContainer.
  - GenericGraph: A Graph that stores the "distance" and pheromone ingo in a graph like object.
- Colony.
- MaxIterationCriteriaPolicy: A StoppingCriteria based on a maximum number of iterations.
- ACOInitializer: An Initializer that creates N ants in a colony.
  - BackHomeMemoryAnt: An Ant with memory that tries to go back home when no new paths are available.
    - Solution.
      - QapObjectiveFunction: An ObjectiveFunction that evaluates the ant solution using a QAP like objective function.
      - AllNodesCircleBuilderEvaluator: A BuilderEvaluator that tells the ant to keep building the solution if its
        solution doesn't start and end the same node and visits certain nodes.
    - RandomProportionalEdgeSelector: An EdgeSelector that allows the ant to choose between the available edges.
- SimpleConstructorPolicy: A ConstructorPolicy, ant by ant build the solution.
- SimpleUpdatePheromonePolicy: A PheromonePolicy, first evaporate, then add.
  - MaxGlobalBestProportionalAddPolicy: An AddPheromonePolicy that adds pheromone to the edges proportionally to the
    best historical solution fitness up to a maximum limit.
  - MaxElitistFitnessProportionalAddPolicy: An AddPheromonePolicy that adds pheromone to the edges proportionally to the
    N-best current solutions fitnesses up to a maximum limit.
  - MinPercentageEvaporatePheromonePolicy: An EvaporatePheromonePolicy that evaporates a certain percentage of the 
    pheromone on each edge down to a minimum.
- NIterationsStatsRetrievalPolicy: A StatsRetrievalPolicy that retrieves Stats every N iterations.
  - CurrentMinFitnessStat: A Stat that retrieves the current colony's best fitness.
  - CurrentMaxFitnessStat: A Stat that retrieves the current colony's worst fitness.
  - HistoricalMinFitnessStat: A Stat that retrieves the historical colony's best fitness.
  - HistoricalMaxFitnessStat: A Stat that retrieves the historical colony's worst fitness.
  - MeanSdFitnessStat: A Stat that retrieves the colony fitness mean and STD.
  - PercentileFitnessStat: A Stat that retrieves the colony fitness value for specific quartiles.
  - MeanSdPheromoneStat: A Stat that retrieves the pheromone mean and STD in the PheromoneContainer.
  - PercentilePheromoneStat: A Stat that retrieves the pheromone value for specific quartiles.
  - StepGradientStat: A Stat that retrieves the step gradient.
  - TimeGradientStat: A Stat that retrieves the time gradient.
  - ElapsedTimeStat: A Stat that retrieves the elapsed time.
  - PheromoneContainerGIFStat: A Stat that generates a GIF of the pheromone evolution in the PheromoneContainer.

On the other hand, the majority of the effort in this type of algorithms must be dedicated to constructing the World.

In this scenario, certain functions are specifically focused on building the World. This is necessary because we must represent
the problem in a graphical way, creating a structure for the ants to traverse the edges of the graph.

The next function builds the world.

````code
private static GenericWorld createWorld(
            String inputPathLocations, String inputPathFlows, Double initialPheromone,
            HashMap<Object, HashMap<Object, Double>> locationsDistances,
            HashMap<Object, HashMap<Object, Double>> facilitiesFlows)
````
The World is built with one Graph/PheromoneContainer that illustrates "distances" and pheromone between nodes. Each Node in
the Graph has associated a set of Edges that connects it with other Nodes. Those Edges contain the "distance" and 
pheromone information. 

````mermaid
flowchart LR
subgraph  ide0 [World]
  direction LR
    subgraph  ide1 [Assignation]
    direction LR
        F1((F1)) --> L1((L1))
        F2((F2)) --> L2((L2))
        FX((FX)) --> LX((LX))
        F1((F1)) --> L2((L2))
        F1((F1)) --> LX((LX))
        F1((F1)) --> LN((LN))
        F2((F2)) --> L1((L1))
        F2((F2)) --> LX((LX))
        F2((F2)) --> LN((LN))
        FX((FX)) --> L1((L1))
        FX((FX)) --> L2((L2))
        FX((FX)) --> LN((LN))
        FN((FN)) --> L1((L1))
        FN((FN)) --> L2((L2))
        FN((FN)) --> LX((LX))
        FN((FN)) --> LN((LN))
    end

    subgraph  id21 [Order]
    direction LR
    L1_((L1)) --> F1_((F1))
    L2_((L2)) --> F2_((F2))
    LX_((LX)) --> FX_((FX))
    L2_((L2)) --> F1_((F1))
    LX_((LX)) --> F1_((F1))
    LN_((LN)) --> F1_((F1))
    L1_((L1)) --> F2_((F2))
    LX_((LX)) --> F2_((F2))
    LN_((LN)) --> F2_((F2))
    L1_((L1)) --> FX_((FX))
    L2_((L2)) --> FX_((FX))
    LN_((LN)) --> FX_((FX))
    L1_((L1)) --> FN_((FN))
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

The distance of the edges in the Assignation part of the graph represents the heuristic distance, which indicates the 
attractiveness for the ant when choosing that edge. In contrast, in the Order part of the graph, the distance is set 
to 1. This is because the order in which the ants assign the facilities to locations is not of concern. It's important 
to note that the nodes in both the Assignation and Order parts share the same names because they correspond to the
same nodes in the graph. This representation is divided into two parts to enhance understanding.

#### qapNode:
Auxiliary class used to store the Graphical information of the nodes. We store the ID:
- ID (Integer): Contains node number.

For this particular case, this class is not necessary at all, but we preferred to keep it because it may be useful for 
the developer to store additional node information.

## Comment:
This solution has been tested against an internet solver, and both solvers propose the same solutions. However, you may
notice a difference in the solution values: the value proposed in the image is 1160, whereas the ACO (Ant Colony 
Optimization) algorithm yields a value of 2320.
This discrepancy arises from our consideration of the flow table as outgoing flow values, while the solver on the 
internet treats flows as both outgoing and ingoing. Consequently, the flow matrix becomes symmetric. As a result, 
the web solver calculates the solution value using only the values above the diagonal of the flow matrix.

We have chosen to maintain a more general approach in our implementation, allowing for the introduction of asymmetric 
flows between facilities.

(The same principle applies to distances) :smirk_cat: