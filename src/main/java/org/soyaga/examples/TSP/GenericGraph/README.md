# GenericGraph

Solving the Traveling Salesman Problem (TSP) using a `GenericGraph` as the base structure to store information.

<table>
  <tr>
    <th><b>Oliver30 Path</b></th>
  </tr>
  <tr>
    <td><img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/TSP/oliver30Gif.gif" title="Solution for the Oliver30 Path" alt="Solution for the Oliver30 Path" width="450" height="460" /></td>
    <td><img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/TSP/oliver30.png" title="Solution for the Oliver30 Path" alt="Solution for the Oliver30 Path" width="450" height="460" /></td>
  </tr>
</table>

## Contents of this folder:

This folder contains three classes that define the structures required to solve the problem. These classes implement their respective interfaces from `OptimizationLib.aco`.

1. [GenericGraphTspAntColonyAlgorithm](#genericgraphtspantcolonyalgorithm): Extends `StatsAntColonyAlgorithm`.
2. [ImageStat](#imagestat): Implements `Stat`.
3. [RunGenericGraphTspOptimization](#rungenericgraphtspoptimization): The main class for instantiation and optimization.

### [GenericGraphTspAntColonyAlgorithm](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/TSP/GenericGraph/GenericGraphTspAntColonyAlgorithm.java)

This class extends `StatsAntColonyAlgorithm`, making it an instance of `Optimizer`. In other words, this class can be optimized, and its results can be retrieved.

````java
public void optimize(){...}
public Object getResult(Object ... resultArgs){...}
````

The `optimize` method is inherited from the abstract class `StatsAntColonyAlgorithm`, which defines a basic optimization procedure. The `getResults` function computes and stores a GIF image.

### [ImageStat](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/TSP/GenericGraph/ImageStat.java)

This class implements `Stat`, meaning it can be measured following a `StatRetrievalPolicy`. During evaluation, an image of the best solution is computed and stored.

### [RunGenericGraphTspOptimization](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/TSP/GenericGraph/RunGenericGraphTspOptimization.java)

This is the main class, where the execution starts. The process is as simple as instantiating the `RunGenericGraphTspOptimization` object (previously defined), filling it with its components, optimizing it, and retrieving the results.

The specific components for the `GenericGraphTspAntColonyAlgorithm` include:
- **GenericWorld**: A `World` containing a `Graph` and a `PheromoneContainer`.
  - **GenericGraph**: A graph-like structure that stores information about the graph and the pheromone levels.
- **Colony**.
- **MaxIterationCriteriaPolicy**: A stopping criterion based on a maximum number of iterations.
- **ACOInitializer**: An initializer that creates N ants in a colony.
  - **SimpleMemoryAnt**: An ant with memory.
    - **Solution**.
      - **PathDistanceObjectiveFunction**: An objective function that evaluates the path distance of the ant's solution.
      - **AllNodesCircleBuilderEvaluator**: A builder evaluator that directs the ant to continue building the solution until all nodes are visited, even if the solution doesn't start and end at the same node.
    - **RandomProportionalEdgeSelector**: An edge selector that allows the ant to choose between available edges.
- **SimpleConstructorPolicy**: A constructor policy, building the solution ant by ant.
- **SimpleUpdatePheromonePolicy**: A pheromone policy, which first evaporates pheromone and then adds more.
  - **SolFitnessProportionalAddPheromonePolicy**: Adds pheromone to the edges proportionally to the solution's fitness.
  - **PercentageEvaporatePheromonePolicy**: Evaporates a certain percentage of pheromone on each edge.
- **NIterationsStatsRetrievalPolicy**: Retrieves statistics every N iterations.
  - **ImageStat**: Creates and stores an image of the best solution found so far.
  - **CurrentMinFitnessStat**: Retrieves the current best fitness of the colony.
  - **MeanSdFitnessStat**: Retrieves the colony's fitness mean and standard deviation.

### World Construction

In this type of algorithm, much of the effort is focused on constructing the `World`. This is necessary to represent the problem in a graphical form, creating a structure for the ants to traverse the graph's edges.

The following function is responsible for building the world:

````java
private static AbstractMap.SimpleEntry<HashMap<Integer, tspNode>, GenericWorld> createWorld(String path, Double initialPheromone)
````
The `World` is built using a single `Graph/PheromoneContainer`, which simultaneously represents both the distances and pheromone levels between nodes. Each node in the graph is associated with a set of edges that connect it to other nodes. These edges contain information about both the distance between the nodes and the pheromone levels.

````mermaid
flowchart TB
    subgraph  ide1 [World]
    direction LR
    A((A)) --Edge--> B((B))
    A((A)) --Edge--> C((C))
    A((A)) --Edge--> D((D))
    A((A)) --Edge--> E((E))
    B((B)) --Edge--> C((C))
    B((B)) --Edge--> D((D))
    C((C)) --Edge--> E((E))
    C((C)) --Edge--> D((D))
    D((D)) --Edge--> E((E))
    D((D)) --Edge--> A((A))
    E((E)) --Edge--> A((A))
    E((E)) --Edge--> D((D))
    end

````

#### tspNode:
An auxiliary class used to store the graphical information of the spatial nodes. It stores the following details:
- **ID (Integer)**: Represents the node number.
- **x (Integer)**: Represents the x-coordinate of the node.
- **y (Integer)**: Represents the y-coordinate of the node.
- **relatedNodes (ArrayList<Integer>)**: Contains the IDs of the nodes connected to this node.

## Result example:
For the following parameters:
- **antNumber** = 60
- **maxEdges** = 100
- **alpha** = 1, **beta** = 4
- **maxIterations** = 50
- **persistence** = 0.9
- **initialPheromone** = 0
- **antPheromoneQuantity** = 10
```
--------------------------------------------------------------------------
| Iteration | ImageSaved | CurrentMin | MeanFitness | StandardDevFitness |
--------------------------------------------------------------------------
| 1         | true       | 1255.0454  | 2427.3864   | 918.2601           |
| 2         | true       | 507.9967   | 919.2740    | 196.2754           |
| 3         | true       | 483.1379   | 866.4462    | 193.4960           |
| 4         | true       | 471.7590   | 899.5800    | 228.5353           |
| 5         | true       | 471.7590   | 893.8129    | 180.0848           |
| 6         | true       | 471.7590   | 963.6639    | 215.0524           |
| 7         | true       | 469.0721   | 907.9243    | 233.4406           |
| 8         | true       | 458.8652   | 934.1984    | 213.6528           |
| 9         | true       | 458.8652   | 908.7556    | 233.8525           |
| 10        | true       | 426.6002   | 883.9294    | 220.2153           |
```

## Comment:
This solution was tested against the oliver30 dataset. It is worth noting that the best-known solution for this problem has a distance of 423.741, while our solution achieved a distance of 426.6002. Although this may not be an exceptional result, it's important to consider that we only ran the algorithm for a few seconds without fine-tuning the hyperparameters. This suggests that with more time and adjustments to the parameters, it is likely that better solutions could be attained. :scream_cat:
