# DenseMatrices
Solving the Traveling Salesman Problem (TSP) using dense arrays as the base structure to store information.

<table>
  <tr>
    <th colspan="2"> <b>Hexagon Path </b></th>
  </tr>
  <tr>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/TSP/hexagonGif.gif"  title="Solution for the HexagonPath" alt="Solution for the hexagonPath" width="400" height="342" /></td>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/TSP/hexagonPheromoneGif.gif"  title="Solution for the HexagonPath" alt="Solution for the hexagonPath" width="400" height="342" /></td>
  </tr>
  <tr>
    <th colspan="2"> <b>Octagon Path </b></th>
  </tr>
  <tr>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/TSP/octagonGoodGif.gif"  title="Solution for the OctagonPath" alt="Solution for the octagonPath" width="400" height="342" /></td>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/TSP/octagonGoodPheromoneGif.gif"  title="Solution for the OctagonPath" alt="Solution for the octagonPath" width="400" height="342" /></td>
  </tr>
  <tr>
    <th colspan="2"> <b>Octagon local minima Path </b></th>
  </tr>
  <tr>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/TSP/octagonBadGif.gif"  title="Solution for the OctagonPath" alt="Solution for the octagonPath" width="400" height="342" /></td>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/TSP/octagonBadPheromoneGif.gif"  title="Solution for the OctagonPath" alt="Solution for the octagonPath" width="400" height="342" /></td>
  </tr>
  <tr>
    <th colspan="2"> <b>Pentacontagon Path </b></th>
  </tr>
  <tr>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/TSP/50Gif.gif"  title="Solution for the 50Path" alt="Solution for the 50Path" width="400" height="342" /></td>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/TSP/50.png"  title="Solution for the 50Path" alt="Solution for the 50Path" width="400" height="342" /></td>
  </tr>
</table>

## In this folder:
This folder contains four different classes that define the structures required to solve the problem. These classes implement their respective interfaces from OptimizationLib.aco:

1. [DenseTspAntColonyAlgorithm](#densetspantcolonyalgorithm): Extends `StatsAntColonyAlgorithm`.
2. [ImageStat](#imagestat): Implements `Stat`.
3. [AllNodesCircleFeasibilityFunction](#allnodescirclefeasibilityfunction): Implements `FeasibilityFunction`.
4. [RunDenseTspOptimization](#rundensetspoptimization): The main class for instantiation and optimization.

### [DenseTspAntColonyAlgorithm](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/TSP/DenseMatrices/DenseTspAntColonyAlgorithm.java):
This class extends `StatsAntColonyAlgorithm`, which makes it an instance of an optimizer. In other words, this class can be optimized, and its results can be gathered.

````java
public void optimize(){...}
public Object getResult(Object ... resultArgs){...}
````

The `optimize` method is inherited from the abstract class `StatsAntColonyAlgorithm`, which defines a basic optimization procedure. The `getResults` function computes and stores a GIF image of the best solution.

### [ImageStat](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/TSP/DenseMatrices/ImageStat.java)
This class implements `Stat`, allowing it to be measured following a `StatRetrievalPolicy`. During its evaluation, an image is computed and stored using the best solution found.

### [AllNodesCircleFeasibilityFunction](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/TSP/DenseMatrices/AllNodesCircleFeasibilityFunction.java)
This class implements the `FeasibilityFunction` interface, enabling it to be evaluated alongside the objective function. It penalizes solutions that do not meet specific criteria, such as visiting all nodes and returning to the starting node.

### [RunDenseTspOptimization](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/TSP/DenseMatrices/RunDenseTspOptimization.java)
This is the main class where the optimization process begins. It involves instantiating the `RunDenseTspOptimization` object (previously defined), filling it with its components, optimizing it, and retrieving the results.

#### Specific components for the `DenseTspAntColonyAlgorithm` include:
- **GenericWorld**: A world that contains a graph and a pheromone container.
  - **DenseMatrixGraph**: A graph that stores information in `ArrayLists`.
  - **DenseMatrixPheromoneContainer**: A pheromone container that also uses `ArrayLists` to store information.
- **Colony**: Represents a group of ants.
- **MaxIterationCriteriaPolicy**: A stopping criteria based on a maximum number of iterations.
- **ACOInitializer**: An initializer that creates N ants in the colony.
  - **SimpleAnt**: A basic ant representation.
    - **Solution**:
      - **PathDistanceObjectiveFunction**: An objective function that evaluates the path distance of the ant's solution.
      - **AllNodesCircleFeasibilityFunction**: A feasibility function that penalizes solutions that do not start and end at the same node or fail to visit all nodes.
      - **AllNodesCircleBuilderEvaluator**: A builder evaluator that instructs the ant to continue building the solution if it does not start and end at the same node while visiting all required nodes.
    - **RandomProportionalEdgeSelector**: An edge selector that allows the ant to choose from available edges proportionally.
- **SimpleConstructorPolicy**: A constructor policy that builds the solution ant by ant.
- **SimpleUpdatePheromonePolicy**: A pheromone policy that first evaporates pheromones and then adds new pheromones.
  - **SolFitnessProportionalAddPheromonePolicy**: An add pheromone policy that adds pheromones to edges proportionally to the solution's fitness.
  - **PercentageEvaporatePheromonePolicy**: An evaporate pheromone policy that reduces the pheromone on each edge by a specified percentage.
- **NIterationsStatsRetrievalPolicy**: A stats retrieval policy that gathers statistics every N iterations.
  - **ImageStat**: A stat that creates and stores an image of the best solution found so far.
  - **CurrentMinFitnessStat**: A stat that retrieves the current best fitness of the colony.
  - **MeanSdFitnessStat**: A stat that retrieves the mean and standard deviation of the colony's fitness.

### Building the World
A significant part of developing algorithms for this problem involves constructing the world. Specific functions focus on building this world, which is essential for graphically representing the problem and creating a structure for the ants to traverse the graph's edges.

The next function will focus on building the world.

````java
private static AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<HashMap<Integer, tspNode>,HashMap<Integer, tspNode>>, GenericWorld> createWorld(String path, Double initialPheromone)
````
The **World** is constructed using two matrices that illustrate the distances and pheromones between nodes. Each column and row corresponds to a node in the problem. Transitions are understood as moving from column to row.

For instance, the pheromone from node 1 to node 0 (denoted as \( \text{pheromone}[1][0] \)) pertains to the pheromone deposited on the edge connecting from node 1 to node 0.


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


#### tspNode

The `tspNode` is an auxiliary class used to store the graphical information of the spatial nodes. The following information is stored:

- **ID (Integer)**: Contains the node number.
- **x (Integer)**: Contains the x position of the node.
- **y (Integer)**: Contains the y position of the node.
- **relatedNodes (ArrayList<Integer>)**: Contains the IDs of the connected nodes.

## Result Example

For the following parameters:
- **antNumber** = 6
- **maxEdges** = 10
- **alpha** = 1.0
- **beta** = 2.0
- **maxIterations** = 50
- **persistence** = 0.9
- **initialPheromone** = 0.0
- **antPheromoneQuantity** = 10
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

## Comment

This is a common scenario for a TSP with multiple stops. Such problems are typically addressed using the
well-established Dijkstra's Algorithm. :scream_cat:

