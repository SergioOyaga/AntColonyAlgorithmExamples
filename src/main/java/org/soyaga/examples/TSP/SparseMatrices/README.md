# SparseMatrices
Solution of the Travel Salesman Problem (TSP) using HashMaps as base structure to store information.

<table>
  <tr>
    <th> <b>Colony Path </b></th>
  </tr>
  <tr>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/TSP/colonyGif.gif"  title="Solution for the ColonyPath" alt="Solution for the colonyPath" width="750" height="300" /></td>
  </tr>
  <tr>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/TSP/colony.jpg"  title="Solution for the ColonyPath" alt="Solution for the colonyPath" width="750" height="300" /></td>
  </tr>
</table>

## In this folder:
We find 2 different classes that defines the problem dependent structures that we have to create (Implementing their
corresponding OptimizationLib.ga interfaces).
1. [SparseTspAntColonyAlgorithm](#sparsetspantcolonyalgorithm): Implements AntColonyAlgorithm.
2. [RunSparseTspOptimization](#runsparsetspoptimization): This is the main class. Here we instantiate our SparseTspAntColonyAlgorithm Object with all his components.

### [SparseTspAntColonyAlgorithm](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/TSP/SparseMatrices/SparseTspAntColonyAlgorithm.java):
This class implements AntColonyAlgorithm, which by extension makes it an Optimizer instance. In other words this class 
can be optimized and its results can be gathered.

````code
public void optimize(){...}
public Object getResult(){...}
````

The <i>optimize</i> function is implemented in a way that the generation number is passed as VarArg to most of the actions
executed by the SparseTspAntColonyAlgorithm parts (solutionConstructorPolicy, updatePheromonePolicy...). 
The <i>getResults</i> function returns an Image that will be stored or used to build the gif.

### [RunSparseTspOptimization](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/TSP/SparseMatrices/RunSparseTspOptimization.java):
This is the main class. Is where the run starts. As simple as instantiate the RunSparseTspOptimization object 
(previously defined) filled with its components, optimize it, and retrieve the results.

The specific components for the SparseTspAntColonyAlgorithm are:
- <b>GenericWorld</b>: A World that contain a Graph and a PheromoneContainer.
  - <b>SparseMatrixGraph</b>: A Graph that represent connections as transitions between columns and rows in HashMaps,
    storing distances as values.
  - <b>SparseMatrixPheromoneContainer</b>: A PheromoneContainer that represent connections as transitions between 
    columns and rows in HashMaps, storing pheromone as values.
- <b>SimpleMemoryAnt</b>: An Ant that does know where it has been, and tries to not visit the same node twice.
    - <b>AllNodesLineSolution</b>: A Solution that forces the Ant to "move" until the final node is found and all nodes
  in the graph have been visited. 
- <b>MaxIterationCriteriaPolicy</b>: A StoppingCriteria based on a maximum number of iteration.
- <b>ACOInitializer</b>: An Initializer that instantiate N ants in a Colony.
- <b>SimpleConstructorPolicy</b>: A ConstructorPolicy that builds solutions using the ants sequentially.
- <b>SimpleUpdatePheromonePolicy</b>: A UpdatePheromonePolicy that first evaporates pheromone and then adds it using the ant's information.
  - <b>SolFitnessProportionalAddPheromonePolicy</b>: An AddPheromonePolicy that adds pheromone to the paths used by the ants 
    proportionally to how good those paths are.
  - <b>PercentageEvaporatePheromonePolicy</b>: An EvaporatePheromonePolicy that evaporates pheromone in a rate.
- <b>worldMap</b>: HashMap<Integer,Integer> that maps the internal nodeID (0 based) to the real one.
- <b>scale</b>: Double that allows to scale the output image.

On the other hand, the effort in this type of optimizations has to be done when building the World.

In this class some functions are specifically focus on building the World. This is because we need to represent the problem in a 
graphical way for the ants to travel the "edges".
The next function builds the world. 

````code
private static AbstractMap.SimpleEntry<HashMap<Integer, tspNode>, GenericWorld<Integer,Integer>> createWorld(String path, Double initialPheromone)
````
World is build with two Maps that represent "distances" and pheromone between nodes. Each entry.key represents one node 
in the problem, so the transitions are considered from entry1.key to entry2.key where entry2=entry1.value. 

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

## Comment:
This solution is not exactly the classical TSP where the route has to start and end en the same node, but to start in 
one node and finish in another. This is the typical case of a route planning with multiple stops.This problem is 
typically solved using the well establish Dijkstra's Algorithm. :scream_cat:

