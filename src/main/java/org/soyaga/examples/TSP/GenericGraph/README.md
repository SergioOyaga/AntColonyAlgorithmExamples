# GenericGraph
Solving the Travel Salesman Problem (TSP) using a GenericGraph as base structure to store information.

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
This folder contains 3 different classes that define the structures required for solving the problem.
These classes implement their respective interfaces from OptimizationLib.aco.
1. [GenericGraphTspAntColonyAlgorithm](#genericgraphtspantcolonyalgorithm): Extends StatsAntColonyAlgorithm.
2. [ImageStat](#imagestat): Implements Stat.
3. [RunGenericGraphTspOptimization](#rungenericgraphtspoptimization): The main class for instantiation and optimization.

### [GenericGraphTspAntColonyAlgorithm](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/TSP/GenericGraph/GenericGraphTspAntColonyAlgorithm.java):
This class extends StatsAntColonyAlgorithm, which by extension makes it an Optimizer instance. In other words, this class
can be optimized and its results can be gathered.

````code
public void optimize(){...}
public Object getResult(Object ... resultArgs){...}
````

The <i>optimize</i> method is inherited from the abstract class StatsAntColonyAlgorithm, which defines a basic optimization
procedure. The <i>getResults</i> function computes and stores a GIF image.

### [ImageStat](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/TSP/GenericGraph/ImageStat.java)
This class implements Stat. This means that this class can be measured following a StatRetrievalPolicy. For this Stat,
during its evaluation, an image is computed and stored using the best solution.

### [RunGenericGraphTspOptimization](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/TSP/GenericGraph/RunGenericGraphTspOptimization.java):
This is the main class. Is where the run starts. As simple as instantiate the RunGenericGraphTspOptimization object 
(previously defined) filled with its components, optimize it, and retrieve the results.

The specific components for the GenericGraphTspAntColonyAlgorithm include:
- GenericWorld: A World that contains a Graph and a PheromoneContainer.
  - GenericGraph: A Graph and PheromoneContainer that stores the info in a graph-like structure.
- Colony.
- MaxIterationCriteriaPolicy: A StoppingCriteria based on a maximum number of iterations.
- ACOInitializer: An Initializer that creates N ants in a colony.
  - SimpleMemoryAnt: An Ant, in this case, an ant with memory.
    - Solution.
      - PathDistanceObjectiveFunction: An ObjectiveFunction that evaluates the ant solution path distance.
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
private static AbstractMap.SimpleEntry<HashMap<Integer, tspNode>, GenericWorld> createWorld(String path, Double initialPheromone)
````
The World is built with one Graph/PheromoneContainer that represents "distances" and pheromones at the same time. 
Each Node in the Graph has associated a set of Edges that connects it with other Nodes. Those Edges contain the 
Distance and Pheromone information. 

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
For:
- antNumber = 60
- maxEdges = 100
- alpha = 1., beta = 4.
- maxIterations = 50
- persistence = .9
- initialPheromone = 0.
- antPheromoneQuantity=10.
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
This solution is tested against the oliver30 dataset. It's worth noting that the best solution for this problem has a 
distance of 423.741, whereas in our solution, the distance is 426.6002. While this may not be an exceptional 
achievement, it's important to consider that we only ran the algorithm for a few seconds without hyper-parameter 
fine-tuning. This gives us confidence that with more time and parameter adjustments, better solutions can likely 
be attained. :scream_cat:

