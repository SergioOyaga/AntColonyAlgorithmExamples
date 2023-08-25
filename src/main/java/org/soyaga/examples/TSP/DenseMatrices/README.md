# DenseMatrices
Solving the Travel Salesman Problem (TSP) using dense Arrays as base structure to store information.

<table>
  <tr>
    <th colspan="2"> <b>Hexagon Path </b></th>
  </tr>
  <tr>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/TSP/hexagonGif.gif"  title="Solution for the HexagonPath" alt="Solution for the hexagonPath" width="400" height="342" /></td>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/TSP/hexagon.png"  title="Solution for the HexagonPath" alt="Solution for the hexagonPath" width="400" height="342" /></td>
  </tr>
  <tr>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/TSP/octagonGif.gif"  title="Solution for the OctagonPath" alt="Solution for the octagonPath" width="400" height="342" /></td>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/TSP/octagon.png"  title="Solution for the OctagonPath" alt="Solution for the octagonPath" width="400" height="342" /></td>
  </tr>
  <tr>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/TSP/50Gif.gif"  title="Solution for the 50Path" alt="Solution for the 50Path" width="400" height="342" /></td>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/TSP/50.png"  title="Solution for the 50Path" alt="Solution for the 50Path" width="400" height="342" /></td>
  </tr>
</table>

## In this folder:
This folder contains 4 different classes that define the structures required for solving the problem.
These classes implement their respective interfaces from OptimizationLib.aco.
1. [DenseTspAntColonyAlgorithm](#densetspantcolonyalgorithm): Extends StatsAntColonyAlgorithm.
2. [ImageStat](#imagestat): Implements Stat.
3. [AllNodesCircleFeasibilityFunction](#allnodescirclefeasibilityfunction): Implements FeasibilityFunction. 
4. [RunDenseTspOptimization](#rundensetspoptimization): The main class for instantiation and optimization.

### [DenseTspAntColonyAlgorithm](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/TSP/DenseMatrices/DenseTspAntColonyAlgorithm.java):
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

### [AllNodesCircleFeasibilityFunction](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/TSP/SparseMatrices/AllNodesCircleFeasibilityFunction.java)
This class implements the FeasibilityFunction interface. As a result, it can be evaluated alongside the objective 
function, penalizing solutions that do not meet specific criteria.

### [RunDenseTspOptimization](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/TSP/DenseMatrices/RunDenseTspOptimization.java):
This is the main class. Is where the run starts. As simple as instantiate the RunSparseTspOptimization object
(previously defined) filled with its components, optimize it, and retrieve the results.

The specific components for the DenseTspAntColonyAlgorithm include:
- GenericWorld: A World that contains a Graph and a PheromoneContainer.
  - DenseMatrixGraph: A Graph that stores the info in ArrayLists.
  - DenseMatrixPheromoneContainer: A PheromoneContainer that stores the info in ArrayLists.
- Colony.
- MaxIterationCriteriaPolicy: A StoppingCriteria based on a maximum number of iterations.
- ACOInitializer: An Initializer that creates N ants in a colony.
  - SimpleAnt: An Ant.
    - Solution.
      - PathDistanceObjectiveFunction: An ObjectiveFunction that evaluates the ant solution path distance.
      - AllNodesCircleFeasibilityFunction: A FeasibilityFunction that penalizes solutions that doesn't start and end in
      the same node and doesn't visit all nodes in the graph.
      - AllNodesCircleBuilderEvaluator: A BuilderEvaluator that tells the ant to keep building the solution if its
        solution doesn't start and end the same node and visits certain nodes.
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
private static AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<HashMap<Integer, tspNode>,HashMap<Integer, tspNode>>, GenericWorld> createWorld(String path, Double initialPheromone)
````
The World is built using two matrices that illustrate distances and pheromones between nodes.
Each column and row corresponds to a node in the problem. Transitions are understood from column to row. For instance, the pheromone 1&rarr;0
pertains to the pheromone deposited on the edge connecting from node 1 to node 0.


|       | col 0         | col 1         | ...             | col N         |
|-------|---------------|---------------|-----------------|---------------|
| row 0 | null          | dist 1&rarr;0 | dist ...&rarr;0 | dist N&rarr;0 |
| row 1 | dist 0&rarr;1 | null          | dist ...&rarr;1 | dist N&rarr;1 |
| ...   | ...           | ...           | null            | ...           |
| row N | dist 0&rarr;N | dist 1&rarr;N | dist ...&rarr;N | null          |



|       | col 0              | col 1              | ...                  | col N              |
|-------|--------------------|--------------------|----------------------|--------------------|
| row 0 | pheromone 0&rarr;0 | pheromone 1&rarr;0 | pheromone ...&rarr;0 | pheromone N&rarr;0 |
| row 1 | pheromone 0&rarr;1 | pheromone 1&rarr;1 | pheromone ...&rarr;1 | pheromone N&rarr;1 |
| ...   | ...                | ...                | ...                  | ...                |
| row N | pheromone 0&rarr;N | pheromone 1&rarr;N | pheromone ...&rarr;N | pheromone N&rarr;N |


#### tspNode:
Auxiliary class used to store the Graphical information of the spatial nodes. We store the ID, position and other IDs with
which this node is connected:
- ID (Integer): Contains node number.
- x (Integer): Contains the x position of the node.
- y (Integer): Contains the y position of the node.
- relatedNodes (ArrayList<Integer>): contains the IDs of the connected nodes.



## Result example:
For:
- antNumber = 6
- maxEdges = 10
- alpha = 1., beta = 2.
- maxIterations = 50
- persistence = .9
- initialPheromone = 0.
- antPheromoneQuantity=10.
```
--------------------------------------------------------------------------
| Iteration | ImageSaved | CurrentMin | MeanFitness | StandardDevFitness |
--------------------------------------------------------------------------
| 1         | true       | 154.6411   | 279.0748    | 72.7805            |
| 2         | true       | 97.3205    | 216.8740    | 83.8891            |
| 3         | true       | 87.3205    | 228.9871    | 77.6798            |
| 4         | true       | 87.3205    | 242.3205    | 90.5184            |
| 5         | true       | 87.3205    | 273.6603    | 97.3051            |
| 6         | true       | 87.3205    | 311.1006    | 76.8623            |
| 7         | true       | 87.3205    | 340.7737    | 117.6381           |
| 8         | true       | 60.0000    | 264.8804    | 137.5073           |
```

## Comment:
This is a common scenario for a TSP with multiple stops. Such problems are typically addressed using the 
well-established Dijkstra's Algorithm. :scream_cat:

