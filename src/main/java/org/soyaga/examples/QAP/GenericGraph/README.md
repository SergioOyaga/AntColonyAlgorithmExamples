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

In the images above, we can see two different approaches for solving the QAP problem. These approaches differ in 
the pheromone addition policy used. One uses the MaxGlobalBestProportionalAddPolicy, and the other uses the 
MaxElitistFitnessProportionalAddPolicy. Each approach has its own set of advantages and disadvantages. However, they 
serve as an example of how a simple change can significantly alter the behavior of the entire algorithm.

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
  <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/resources/QAP/QAP_solution.png"  title="Optimal solution" alt="Optimal solution" width="400" height="400" />
  </td>
  </tr>
</table>

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