# DenseMatrices
Solution of the Travel Salesman Problem (TSP) using dense Arrays as base structure to store information.

<table>
  <tr>
    <th colspan="2"> <b>Hexagon Path </b></th>
  </tr>
  <tr>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/TSP/hexagonGif.gif"  title="Solution for the HexagonPath" alt="Solution for the hexagonPath" width="400" height="342" /></td>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/TSP/hexagon.png"  title="Solution for the HexagonPath" alt="Solution for the hexagonPath" width="400" height="342" /></td>
  </tr>
</table>

## In this folder:
We find 2 different classes that defines the problem dependent structures that we have to create (Implementing their
corresponding OptimizationLib.ga interfaces).
1. [DenseTspAntColonyAlgorithm](#densetspantcolonyalgorithm): Implements AntColonyAlgorithm.
2. [RunDenseTspOptimization](#rundensetspoptimization): This is the main class. Here we instantiate our DenseTspAntColonyAlgorithm Object with all his components.

### [DenseTspAntColonyAlgorithm](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/TSP/DenseMatrices/DenseTspAntColonyAlgorithm.java):
This class implements AntColonyAlgorithm, which by extension makes it an Optimizer instance. In other words this class 
can be optimized and its results can be gathered.

````code
public void optimize(){...}
public Object getResult(){...}
````

The <i>optimize</i> function is implemented in a way that the generation number is passed as VarArg to most of the actions
executed by the DenseTspAntColonyAlgorithm parts (solutionConstructorPolicy, updatePheromonePolicy...). 
The <i>getResults</i> function returns an Image that will be stored or used to build the gif.

### [RunDenseTspOptimization](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/TSP/DenseMatrices/RunDenseTspOptimization.java):
This is the main class. Is where the run starts. As simple as instantiate the RunDenseTspOptimization object 
(previously defined) filled with its components, optimize it, and retrieve the results.

The specific components for the DenseTspAntColonyAlgorithm are:
- <b>GenericWorld</b>: A World that contain a Graph and a PheromoneContainer.
  - <b>DenseMatrixGraph</b>: A Graph that represent connections as transitions between columns and rows in a dense matrix,
    storing distances as values.
  - <b>DenseMatrixPheromoneContainer</b>: A PheromoneContainer that represent connections as transitions between columns 
    and rows in a dense matrix, storing pheromone as values.
- <b>SimpleAnt</b>: An Ant that does not know where it has been.
    - <b>AllNodesCircleSolution</b>: A Solution that forces the Ant to "move" until a circle is completed(start and end in the same node)
      and all nodes in the graph have been visited. 
- <b>MaxIterationCriteriaPolicy</b>: A StoppingCriteria based on a maximum number of iteration.
- <b>ACOInitializer</b>: An Initializer that instantiate N ants in a Colony.
- <b>SimpleConstructorPolicy</b>: A ConstructorPolicy that builds solutions using the ants sequentially.
- <b>SimpleUpdatePheromonePolicy</b>: A UpdatePheromonePolicy that first evaporates pheromone and then adds it using the ant's information.
  - <b>SolFitnessProportionalAddPheromonePolicy</b>: An AddPheromonePolicy that adds pheromone to the paths used by the ants 
    proportionally to how good those paths are.
  - <b>PercentageEvaporatePheromonePolicy</b>: An EvaporatePheromonePolicy that evaporates pheromone in a rate.
- <b>worldMap</b>: HashMap<Integer,Integer> that maps the internal nodeID (0 based) to the real one.
- <b>worldMapOutput</b>: HashMap<Integer,Integer> that maps the real nodeID to the internal one (0 based).
- <b>scale</b>: Double that allows to scale the output image.

On the other hand, the effort in this type of optimizations has to be done when building the World.

In this class some functions are specifically focus on building the World. This is because we need to represent the problem in a 
graphical way for the ants to travel the "edges".
The next function builds the world. 

````code
private static AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<HashMap<Integer, tspNode>,HashMap<Integer, tspNode>>, GenericWorld<Integer,Integer>> createWorld(String path, Double initialPheromone)
````
World is build with two matrices that represent the "distances" and pheromone between nodes. each column and row 
represents one node in the problem, so the transitions are considered from column to row. I.E. pheromone 1&rarr;0 is the
pheromone lay down in the edge that connects from node 1 with to node 0.

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

## Comment:
This is the typical case for a TSP with multiple stops.This problem is 
typically solved using the well establish Dijkstra's Algorithm. :scream_cat:

