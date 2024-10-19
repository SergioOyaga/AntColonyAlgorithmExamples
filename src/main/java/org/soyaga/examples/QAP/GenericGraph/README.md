# GenericGraph

Solving the Quadratic Assignment Problem (QAP) using a GenericGraph as base structure to store information.


<table>
  <tr>
    <th colspan="2"> <b>QAP Pheromones.</b></th>
  </tr>
  <tr>
  <td>Best Historical Solution</td>
  <td>10 Best Iteration Solutions</td>
  </tr>
  <tr>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/QAP/QAP_Pheromone_GIF_Best.gif"  title="Pheromone evolution rewarding the historical best solution" alt="Pheromone evolution rewarding the historical best solution" width="400" height="342" /></td>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/QAP/QAP_Pheromone_GIF_10_Ants.gif"  title="Pheromone evolution rewarding the 10 best iteration solutions" alt="Pheromone evolution rewarding the 10 best iteration solutions" width="400" height="342" /></td>
  </tr>
  <tr>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/QAP/QAP_Convergence_Best.png"  title="Fitness evolution rewarding the historical best solution" alt="Fitness evolution rewarding the historical best solution" width="400" height="342" /></td>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/QAP/QAP_Convergence_10_Ants.png"  title="Fitness evolution rewarding the 10 best iteration solutions" alt="Fitness evolution rewarding the 10 best iteration solutions" width="400" height="342" /></td>
  </tr>
</table>

In the images above, we can observe two different approaches for solving the Quadratic Assignment Problem (QAP). These approaches differ in the pheromone addition policy used: one employs the **MaxGlobalBestProportionalAddPolicy**, while the other uses the **MaxElitistFitnessProportionalAddPolicy**. Each approach has its own set of advantages and disadvantages. However, they serve as an example of how a simple change can significantly alter the behavior of the entire algorithm.

## In this folder:
This folder contains two different classes that define the structures required for solving the problem. These classes implement their respective interfaces from `OptimizationLib.aco`:
1. [GenericGraphQapAntColonyAlgorithm](#genericgraphqapantcolonyalgorithm): Extends `StatsAntColonyAlgorithm`.
2. [RunGenericGraphQapOptimization](#rungenericgraphqapoptimization): The main class for instantiation and optimization.

### [GenericGraphQapAntColonyAlgorithm](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/QAP/GenericGraph/GenericGraphQapAntColonyAlgorithm.java):
This class extends `StatsAntColonyAlgorithm`, which effectively makes it an instance of an optimizer. In other words, this class can be optimized, and its results can be collected.

````java
public void optimize(){...}
public Object getResult(Object... resultArgs){...}
````
The `<i>optimize</i>` method is inherited from the abstract class `StatsAntColonyAlgorithm`, which defines a basic optimization procedure. The `<i>getResults</i>` function returns a string that will be printed on the screen with the assignments.

### [RunGenericGraphQapOptimization](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/QAP/GenericGraph/RunGenericGraphQapOptimization.java):
This is the main class where the execution starts. It is as simple as instantiating the `RunGenericGraphQapOptimization` object (previously defined), filling it with its components, optimizing it, and retrieving the results.

The specific components for the `RunGenericGraphQapOptimization` are:
- **GenericWorld**: A world that contains a graph and a pheromone container.
  - **GenericGraph**: A graph that stores the "distance" and pheromone in a graph-like object.
- **Colony**.
- **MaxIterationCriteriaPolicy**: A stopping criterion based on a maximum number of iterations.
- **ACOInitializer**: An initializer that creates N ants in a colony.
  - **BackHomeMemoryAnt**: An ant with memory that tries to return home when no new paths are available.
    - **Solution**.
      - **QapObjectiveFunction**: An objective function that evaluates the ant's solution using a QAP-like objective function.
      - **AllNodesCircleBuilderEvaluator**: A builder evaluator that instructs the ant to keep building the solution if it doesn't start and end at the same node and visits certain nodes.
    - **RandomProportionalEdgeSelector**: An edge selector that allows the ant to choose between the available edges.
- **SimpleConstructorPolicy**: A constructor policy that builds the solution ant by ant.
- **SimpleUpdatePheromonePolicy**: A pheromone policy that first evaporates pheromone and then adds new pheromone.
  - **MaxGlobalBestFitnessProportionalAddPheromonePolicy**: An add pheromone policy that adds pheromone to the edges proportionally to the best historical solution's fitness, up to a maximum limit.
  - **MaxElitistFitnessProportionalAddPheromonePolicy**: An add pheromone policy that adds pheromone to the edges proportionally to the fitnesses of the N-best current solutions, up to a maximum limit.
  - **MinPercentageEvaporatePheromonePolicy**: An evaporate pheromone policy that evaporates a certain percentage of the pheromone on each edge down to a minimum.
- **NIterationsStatsRetrievalPolicy**: A stats retrieval policy that retrieves statistics every N iterations.
  - **CurrentMinFitnessStat**: A stat that retrieves the current colony's best fitness.
  - **CurrentMaxFitnessStat**: A stat that retrieves the current colony's worst fitness.
  - **HistoricalMinFitnessStat**: A stat that retrieves the historical colony's best fitness.
  - **HistoricalMaxFitnessStat**: A stat that retrieves the historical colony's worst fitness.
  - **MeanSdFitnessStat**: A stat that retrieves the colony's fitness mean and standard deviation (STD).
  - **PercentileFitnessStat**: A stat that retrieves the colony's fitness value for specific quartiles.
  - **MeanSdPheromoneStat**: A stat that retrieves the mean and standard deviation of pheromone in the pheromone container.
  - **PercentilePheromoneStat**: A stat that retrieves the pheromone value for specific quartiles.
  - **StepGradientStat**: A stat that retrieves the step gradient.
  - **TimeGradientStat**: A stat that retrieves the time gradient.
  - **ElapsedTimeStat**: A stat that retrieves the elapsed time.
  - **PheromoneContainerGIFStat**: A stat that generates a GIF of the pheromone evolution in the pheromone container.

On the other hand, the majority of the effort in these types of algorithms must be dedicated to constructing the world.

In this scenario, certain functions are specifically focused on building the world. This is necessary to represent the problem graphically, creating a structure for the ants to traverse the edges of the graph.

The next function builds the world.


````java
private static GenericWorld createWorld(
            String inputPathLocations, String inputPathFlows, Double initialPheromone,
            HashMap<Object, HashMap<Object, Double>> locationsDistances,
            HashMap<Object, HashMap<Object, Double>> facilitiesFlows)
````
The World is constructed with a single Graph/PheromoneContainer that represents the "distances" and pheromones between nodes. Each Node in the Graph is associated with a set of Edges that connect it to other Nodes. These Edges contain information about both the "distance" and the pheromone levels.

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

The distance of the edges in the Assignment part of the graph represents the heuristic distance, which indicates the attractiveness for the ant when choosing that edge. In contrast, in the Order part of the graph, the distance is set to 1, as the order in which the ants assign the facilities to locations is not a concern. It is important to note that the nodes in both the Assignment and Order parts share the same names because they correspond to the same nodes in the graph. This dual representation is designed to enhance understanding.

#### qapNode:
An auxiliary class used to store graphical information about the nodes. We store the following ID:
- ID (Integer): Contains the node number.

In this particular case, this class is not strictly necessary; however, we chose to keep it because it may be useful for developers to store additional node information.

## Result example:
<table>
  <tr>
    <td><b>Execution Example</b></td>
    <td><b>Optimal Solution</b></td>
  </tr>
  <tr>
  <td> 

    -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    | Iteration | CurrentMin | CurrentMax | HistoricalMin | HistoricalMax | MeanFitness | StandardDevFitness | P0    | P25   | P50   | P75   | P100  | MeanPheromone | StandardDevPheromone | P0    | P25   | P50   | P75   | P100  | StepGradient(u/iter) | TimeGradient(u/s) | ElapsedTime(s) | PheromoneImageSaved |
    -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    ...
    | 500       | 2320.0000  | 4090.0000  | 2320.0000     | 4748.0000     | 2740.1000   | 490.5180           | 2320. | 2320. | 2518. | 3066. | 4090. | 0.1746        | 0.2819               | 0.001 | 0.001 | 0.001 | 0.271 | 1.000 | -170.7200            | -294293.0726      | 0.0580         | true                |
    f5-->G	f2-->I	f1-->B	f6-->H	f7-->E	f3-->A	f9-->C	f8-->D	f4-->F	

    BUILD SUCCESSFUL in 29s
    
  </td>
  </tr>
  <tr>
  <td>
  <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/resources/QAP/QAP_solution.png"  title="Optimal solution" alt="Optimal solution" width="800" height="600" />
  </td>
  </tr>
</table>

## Comment:
This solution has been tested against an online solver, and both solvers propose the same solutions. However, you may notice a difference in the solution values: the value presented in the image is 1160, whereas the Ant Colony Optimization (ACO) algorithm yields a value of 2320. 

This discrepancy arises from our treatment of the flow table as containing only outgoing flow values, while the online solver considers flows as both outgoing and incoming. Consequently, the flow matrix becomes symmetric, leading the web solver to calculate the solution value using only the values above the diagonal of the flow matrix.

We have opted to maintain a more general approach in our implementation, allowing for the introduction of asymmetric flows between facilities.

(The same principle applies to distances.) :smirk_cat: