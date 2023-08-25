# SparseMatrices
Solving the Travel Salesman Problem (TSP) using HashMaps as base structure to store information.

<table>
  <tr>
    <th> <b>Colony Path </b></th>
  </tr>
  <tr>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/TSP/colonyGif.gif"  title="Solution for the ColonyPath" alt="Solution for the colonyPath" width="750" height="300" /></td>
  </tr>
  <tr>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/TSP/colony.png"  title="Solution for the ColonyPath" alt="Solution for the colonyPath" width="750" height="300" /></td>
  </tr>
</table>

## In this folder:
This folder contains 3 different classes that define the structures required for solving the problem. 
These classes implement their respective interfaces from OptimizationLib.aco.
1. [SparseTspAntColonyAlgorithm](#sparsetspantcolonyalgorithm): Extends StatsAntColonyAlgorithm.
2. [ImageStat](#imagestat): Implements Stat.
3. [RunSparseTspOptimization](#runsparsetspoptimization): The main class for instantiation and optimization.

### [SparseTspAntColonyAlgorithm](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/TSP/SparseMatrices/SparseTspAntColonyAlgorithm.java):
This class extends StatsAntColonyAlgorithm, which by extension makes it an Optimizer instance. In other words, this class 
can be optimized and its results can be gathered.

````code
public void optimize(){...}
public Object getResult(Object ... resultArgs){...}
````

The <i>optimize</i> method is inherited from the abstract class StatsAntColonyAlgorithm, which defines a basic optimization 
procedure. The <i>getResults</i> function computes and stores a GIF image.

### [ImageStat](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/TSP/SparseMatrices/ImageStat.java)
This class implements Stat. This means that this class can be measured following a StatRetrievalPolicy. For this Stat, 
during its evaluation, an image is computed and stored using the best solution.

### [RunSparseTspOptimization](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/TSP/SparseMatrices/RunSparseTspOptimization.java):
This is the main class. Is where the run starts. As simple as instantiate the RunSparseTspOptimization object 
(previously defined) filled with its components, optimize it, and retrieve the results.

The specific components for the SparseTspAntColonyAlgorithm include:
- GenericWorld: A World that contains a Graph and a PheromoneContainer.
  - SparseMatrixGraph: A Graph that stores the info in HashMaps.
  - SparseMatrixPheromoneContainer: A PheromoneContainer that stores the info in HashMaps.
- Colony.
- MaxIterationCriteriaPolicy: A StoppingCriteria based on a maximum number of iterations.
- ACOInitializer: An Initializer that creates N ants in a colony.
  - SimpleMemoryAnt: An Ant, in this case, has memory.
    - Solution.
      - PathDistanceObjectiveFunction: An ObjectiveFunction that evaluates the ant solution path distance.
      - AllNodesLineBuilderEvaluator: A BuilderEvaluator that tells the ant to keep building the solution if its 
      solution doesn't start and end in specific nodes and visit certain nodes.
    - RandomProportionalEdgeSelector: An EdgeSelector that allows the ant to choose between the available edges.
- SimpleConstructorPolicy: A ConstructorPolicy, ant by ant build the solution.
- SimpleUpdatePheromonePolicy: A PheromonePolicy, first evaporate, then add.
  - SolFitnessProportionalAddPheromonePolicy: An AddPheromonePolicy that adds pheromone to the edges proportionally to the solution fitness. 
  - PercentageEvaporatePheromonePolicy: An EvaporatePheromonePolicy that evaporates a certain percentage of the pheromone on each edge.
- NIterationsStatsRetrievalPolicy: A StatsRetrievalPolicy that retrieves Stats every N iterations.
  - ImageStat: A Stat that creates and stores an image of the best solution found up to now.
  - CurrentMinFitnessStat: A Stat that retrieves the current colony's best fitness.
  - MeanSdFitnessStat: A Stat that retrieves the colony fitness mean and STD.


On the other hand, the majority of the effort in this type of algorithms must be dedicated to constructing the World.

In this scenario, certain functions are specifically focused on building the World. This is necessary because we must represent
the problem in a graphical way, creating a structure for the ants to traverse the edges of the graph.

The next function builds the world. 

````code
private static AbstractMap.SimpleEntry<HashMap<Integer, tspNode>, GenericWorld> createWorld(String path, Double initialPheromone)
````
The World is constructed using two HashMaps that depict distances and pheromones between nodes. 
Each entry's key represents a node within the problem — an Integer (in the ACO) and a tspNode (in the ImageStat).


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
Auxiliary class used to store the Graphical information of the spatial nodes. We store the ID, position and other IDs with
which this node is connected:
- ID (Integer): Contains node number.
- x (Integer): Contains the x position of the node.
- y (Integer): Contains the y position of the node.
- relatedNodes (ArrayList<Integer>): contains the IDs of the connected nodes.


## Result example:
For:
- antNumber = 30
- maxEdges = 50
- startNode = 1, endNode = 32
- alpha = 1., beta = 2.
- maxIterations = 50
- persistence = .9
- initialPheromone = 0.
- antPheromoneQuantity=10.
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
This solution doesn't precisely adhere to the classical TSP, where the route must both start and end at the same node. 
Instead, it involves commencing at one node and concluding at another. This scenario aligns with the common case of
route planning involving multiple stops. Typically, this problem is tackled using the well-established Dijkstra's 
Algorithm. :scream_cat:
