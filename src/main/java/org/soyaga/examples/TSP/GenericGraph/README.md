# GenericGraph
Solution of the Travel Salesman Problem (TSP) using a GenericGraph as base structure to store information.

<table>
  <tr>
    <th> <b>Oliver30 Path </b></th>
  </tr>
  <tr>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/TSP/oliver30Gif.gif"  title="Solution for the oliver30Path" alt="Solution for the oliver30Path" width="450" height="460" /></td>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/TSP/oliver30.png"  title="Solution for the oliver30Path" alt="Solution for the oliver30Path" width="450" height="460" /></td>
  </tr>
</table>

## In this folder:
We find 2 different classes that defines the problem dependent structures that we have to create (Implementing their
corresponding OptimizationLib.aco interfaces).
1. [GenericGraphTspAntColonyAlgorithm](#genericgraphtspantcolonyalgorithm): Implements AntColonyAlgorithm.
2. [RunGenericGraphTspOptimization](#rungenericgraphtspoptimization): This is the main class. Here we instantiate our GenericGraphTspAntColonyAlgorithm Object with all his components.

### [GenericGraphTspAntColonyAlgorithm](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/TSP/GenericGraph/GenericGraphTspAntColonyAlgorithm.java):
This class implements AntColonyAlgorithm, which by extension makes it an Optimizer instance. In other words this class 
can be optimized and its results can be gathered.

````code
public void optimize(){...}
public Object getResult(){...}
````

The <i>optimize</i> function is implemented in a way that the generation number is passed as VarArg to most of the actions
executed by the GenericGraphTspAntColonyAlgorithm parts (solutionConstructorPolicy, updatePheromonePolicy...). 
The <i>getResults</i> function returns an Image that will be stored or used to build the gif.

### [RunGenericGraphTspOptimization](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/TSP/GenericGraph/RunGenericGraphTspOptimization.java):
This is the main class. Is where the run starts. As simple as instantiate the RunGenericGraphTspOptimization object 
(previously defined) filled with its components, optimize it, and retrieve the results.

The specific components for the GenericGraphTspAntColonyAlgorithm are:
- <b>GenericWorld</b>: A World that contain a Graph and a PheromoneContainer.
  - <b>GenericGraph</b>: A Graph that represent connections as transitions between columns and rows in HashMaps,
    storing distances as values.
  - <b>GenericGraph</b>: A PheromoneContainer that represent connections as transitions between 
    columns and rows in HashMaps, storing pheromone as values.
- <b>Colony</b>: The Colony where the ants "live".
- <b>antNumber</b>: Integer with the number of ants.
- <b>SimpleMemoryAnt</b>: An Ant that does know where it has been, and tries to not visit the same node twice.
    - <b>AllNodesCircleSolution</b>: A Solution that forces the Ant to "move" until a circle is completed(start and end in the same node)
      and all nodes in the graph have been visited.
    - <b>RandomProportionalEdgeSelector</b>: An EdgeSelector procedure that looks at the amount of pheromone present and
      the attractiveness of an edge to be selected by an ant.
- <b>MaxIterationCriteriaPolicy</b>: A StoppingCriteria based on a maximum number of iteration.
- <b>ACOInitializer</b>: An Initializer that instantiate N ants in a Colony.
- <b>SimpleConstructorPolicy</b>: A ConstructorPolicy that builds solutions using the ants sequentially.
- <b>SimpleSolutionEvaluatorPolicy</b>: A SolutionEvaluatorPolicy that goes over each ant sequentially, evaluating its Solution.
  - <b> PathDistanceSolutionEvaluator</b>: A SolutionEvaluator that evaluates a proposed solution as in TSP problem.
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
private static AbstractMap.SimpleEntry<HashMap<Integer, tspNode>, GenericWorld<Node,Edge>> createWorld(String path, Double initialPheromone)
````
World is build with one Graph/PheromoneContainer that represent "distances" and pheromone at the same time. Each Node in
the Graph has associated a set of Edges that connects it with other Nodes. Those Edges contain the Distance and 
Pheromone information. 

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
Auxiliary class used to store the Graphical information of the spatial nodes. We store the ID, position and other IDs with
which this node is connected:
- ID (Integer): Contains node number.
- x (Integer): Contains the x position of the node.
- y (Integer): Contains the y position of the node.
- relatedNodes (ArrayList<Integer>): contains the IDs of the connected nodes.

## Result example:

## Comment:
This solution is tested against the oliver30 dataset. Notice that the bet solution for this problem is a 
distance equals to 423.741, while in our solution, the distance is 425.820. This is not an amazing achievement,
but we only run the algorithm for a few seconds with no hyper-parameter fine-tuning, so we are confident that better 
solutions could be obtained. :scream_cat:

