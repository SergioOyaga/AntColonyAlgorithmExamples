# SparseMatrices

Solving the Traveling Salesman Problem (TSP) using HashMaps as the base structure to store information.

<table>
  <tr>
    <th><b>Colony Path</b></th>
  </tr>
  <tr>
    <td><img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/TSP/colonyGif.gif" title="Solution for the ColonyPath" alt="Solution for the ColonyPath" width="750" height="300" /></td>
  </tr>
  <tr>
    <td><img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/TSP/colony.png" title="Solution for the ColonyPath" alt="Solution for the ColonyPath" width="750" height="300" /></td>
  </tr>
</table>

## Overview

This folder contains three different classes that define the structures required to solve the problem. These classes implement their respective interfaces from `OptimizationLib.aco`.

1. [SparseTspAntColonyAlgorithm](#sparsetspantcolonyalgorithm): Extends `StatsAntColonyAlgorithm`.
2. [ImageStat](#imagestat): Implements `Stat`.
3. [RunSparseTspOptimization](#runsparsetspoptimization): The main class for instantiation and optimization.

### [SparseTspAntColonyAlgorithm](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/TSP/SparseMatrices/SparseTspAntColonyAlgorithm.java)

This class extends `StatsAntColonyAlgorithm`, which makes it an instance of `Optimizer`. This means it can be optimized, and its results can be gathered.


````java
public void optimize(){...}
public Object getResult(Object ... resultArgs){...}
````
The `optimize` method is inherited from the abstract class `StatsAntColonyAlgorithm`, which defines a basic optimization procedure. The `getResult` function computes and stores a GIF image of the solution.

### [ImageStat](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/TSP/SparseMatrices/ImageStat.java)

This class implements `Stat`, which means it can be measured according to a `StatRetrievalPolicy`. During its evaluation, an image is computed and stored using the best solution found.

### [RunSparseTspOptimization](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/TSP/SparseMatrices/RunSparseTspOptimization.java)

This is the main class where execution begins. To run the optimization, simply instantiate the `RunSparseTspOptimization` object with the required components, optimize it, and retrieve the results.

#### Key components for `SparseTspAntColonyAlgorithm`:

- **GenericWorld**: A world containing a graph and a pheromone container.
  - **SparseMatrixGraph**: A graph storing information in `HashMaps`.
  - **SparseMatrixPheromoneContainer**: A pheromone container also storing information in `HashMaps`.
- **Colony**: The group of ants solving the TSP.
- **MaxIterationCriteriaPolicy**: A stopping criterion based on a maximum number of iterations.
- **ACOInitializer**: An initializer that creates `N` ants in the colony.
  - **SimpleMemoryAnt**: An ant that possesses memory, used to solve the problem.
    - **Solution**: The solution constructed by the ants.
      - **PathDistanceObjectiveFunction**: An objective function that evaluates the path distance of the ant’s solution.
      - **AllNodesLineBuilderEvaluator**: A builder evaluator that instructs the ant to continu


````java
private static AbstractMap.SimpleEntry<HashMap<Integer, tspNode>, GenericWorld> createWorld(String path, Double initialPheromone)
````
The `World` is constructed using two `HashMap`s that represent the distances and pheromone levels between nodes. Each entry’s key corresponds to a node in the problem — represented as an `Integer` in the context of the Ant Colony Optimization (ACO) algorithm, and as a `tspNode` in the `ImageStat` class.



````mermaid
flowchart TB
    subgraph  ide1 [Unordered Structures]
    direction TB    
    Graph o--o Node_1
    Graph o--o Node_
    Graph o--o Node_X
    Node_1 o--o Node_3
    Node_1 o--o Node..
    Node_1 o--o Node_Y
    Node_ o--o Node_0
    Node_ o--o Node...
    Node_ o--o Node_Z
    Node_X o--o Node_10
    Node_X o--o Node.
    Node_X o--o Node_A
    end
    
    
````

#### tspNode:
An auxiliary class used to store the graphical information of the spatial nodes. It holds the ID, position, and the IDs of other nodes it is connected to:
- **ID (Integer)**: The unique identifier of the node.
- **x (Integer)**: The x-coordinate of the node’s position.
- **y (Integer)**: The y-coordinate of the node’s position.
- **relatedNodes (ArrayList<Integer>)**: A list of IDs representing the nodes that are connected to this node.

## Result Example:
For the following parameters:
- **antNumber** = 30
- **maxEdges** = 50
- **startNode** = 1, **endNode** = 32
- **alpha** = 1.0, **beta** = 2.0
- **maxIterations** = 50
- **persistence** = 0.9
- **initialPheromone** = 0.0
- **antPheromoneQuantity** = 10

```
--------------------------------------------------------------------------
| Iteration | ImageSaved | CurrentMin | MeanFitness | StandardDevFitness |
--------------------------------------------------------------------------
| 1         | true       | 364.7577   | 448.4405    | 48.8232            |
| 2         | true       | 166.4066   | 249.0471    | 30.9118            |
| 3         | true       | 166.4066   | 216.5800    | 26.0227            |
| 4         | true       | 166.4066   | 208.1145    | 28.2782            |
| 5         | true       | 118.1623   | 194.0057    | 26.4890            |
| 6         | true       | 118.1623   | 196.2808    | 28.0147            |
| 7         | true       | 118.1623   | 186.4047    | 20.8036            |
| 8         | true       | 118.1623   | 197.2447    | 22.8559            |
| 9         | true       | 118.1623   | 181.0381    | 20.3545            |
| 10        | true       | 118.1623   | 180.8276    | 20.0710            |
| 11        | true       | 118.1623   | 182.2151    | 22.0849            |
| 12        | true       | 115.0000   | 179.4922    | 30.8367            |
```

## Comment:
This solution does not strictly adhere to the classical TSP, where the route must begin and end at the same node. Instead, it involves starting at one node and ending at another, which is a common scenario in route planning with multiple stops. Typically, this problem is addressed using the well-established Dijkstra's Algorithm. :scream_cat:
