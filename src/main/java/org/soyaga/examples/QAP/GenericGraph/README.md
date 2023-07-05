# GenericGraph
Solution of the Quadratic Assignment Problem (QAP) using a GenericGraph as base structure to store information.

## In this folder:
We find 2 different classes that defines the problem dependent structures that we have to create (Implementing their
corresponding OptimizationLib.aco interfaces).
1. [GenericGraphQapAntColonyAlgorithm](#genericgraphqapantcolonyalgorithm): Extends SimpleAntColonyAlgorithm.
2. [RunGenericGraphQapOptimization](#rungenericgraphqapoptimization): This is the main class. Here we instantiate our GenericGraphQapAntColonyAlgorithm Object with all its components.

### [GenericGraphQapAntColonyAlgorithm](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/QAP/GenericGraph/GenericGraphQapAntColonyAlgorithm.java):
This class extends SimpleAntColonyAlgorithm, what makes it an Optimizer instance. In other words this class 
can be optimized and its results can be gathered.

````code
public void optimize(){...}
public Object getResult(){...}
````

The <i>optimize</i> function is an already implemented method defined in SimpleAntColonyAlgorithm.
The <i>getResults</i> function returns a String that will be printed in the screen with the assignations.

### [RunGenericGraphQapOptimization](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/QAP/GenericGraph/RunGenericGraphQapOptimization.java):
This is the main class. Is where the run starts. As simple as instantiate the RunGenericGraphTspOptimization object 
(previously defined) filled with its components, optimize it, and retrieve the results.

The specific components for the RunGenericGraphQapOptimization are:
- <b>GenericWorld</b>: A World that contain a Graph and a PheromoneContainer.
  - <b>GenericGraph</b>: A Graph that represent the problem with Nodes and Edges.
  - <b>GenericGraph</b>: A PheromoneContainer that represent the gained information as a Double stored in the Graph Edges.
- <b>Colony</b>: The Colony where the ants "live".
- <b>antNumber</b>: Integer with the number of ants. 
- <b>BackHomeMemoryAnt</b>: An Ant that does know where it has been, tries to not visit the same node twice and tries to
go back home when no new nodes are available from its current position.
    - <b>AllNodesCircleSolution</b>: A Solution that forces the Ant to "move" until a circle is completed(start and end in the same node)
      and all nodes in the graph have been visited.
    - <b>RandomProportionalEdgeSelector</b>: An EdgeSelector procedure that looks at the amount of pheromone present and
  the attractiveness of an edge to be selected by an ant.
- <b>MaxIterationCriteriaPolicy</b>: A StoppingCriteria based on a maximum number of iteration.
- <b>ACOInitializer</b>: An Initializer that instantiate N ants in a Colony.
- <b>SimpleConstructorPolicy</b>: A ConstructorPolicy that builds solutions using the ants sequentially.
- <b>SimpleSolutionEvaluatorPolicy</b>: A SolutionEvaluatorPolicy that goes over each ant sequentially, evaluating its Solution.
  - <b> QapSolutionEvaluator</b>: A SolutionEvaluator that evaluates a proposed solution as in QAP problem.
- <b>SimpleUpdatePheromonePolicy</b>: A UpdatePheromonePolicy that first evaporates pheromone and then adds it using the ant's information.
  - <b>SolFitnessProportionalAddPheromonePolicy</b>: An AddPheromonePolicy that adds pheromone to the paths used by the ants 
    proportionally to how good those paths are.
  - <b>PercentageEvaporatePheromonePolicy</b>: An EvaporatePheromonePolicy that evaporates pheromone in a rate.
- <b>Nodes</b>: Set<Nodes> (in this case) with the facilities which are assigned to locations and not the other way around.

On the other hand, the effort in this type of optimizations has to be done when building the World.

In this class some functions are specifically focus on building the World. This is because we need to represent the problem in a 
graphical way for the ants to travel the "edges".
The next function builds the world, and fills some structures needed in the QAPSolutionEvaluator Object. 

````code
private static GenericWorld<Node, Edge> createWorld(
            String inputPathLocations, String inputPathFlows, Double initialPheromone,
            HashMap<Object, HashMap<Object, Double>> locationsDistances,
            HashMap<Object, HashMap<Object, Double>> facilitiesFlows)
````
World is build with one Graph/PheromoneContainer that represent "distances" and pheromone at the same time. Each Node in
the Graph has associated a set of Edges that connects it with other Nodes. Those Edges contain the Distance and 
Pheromone information. 

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
The Distance of the edges in the Assignation part of the Graph, is the Heuristic distance (Attractiveness for the ant of
choosing that edge). On the other hand, in the Order part of the Graph the Distance is set to 1, because we don't really
care the in which order the ants assign the facilities to locations.
Notice that the nodes in the Assignation part and Order part have the same names because they are the same Graph nodes.
This representation is split in two for easiness in the understanding.

#### qapNode:
Auxiliary class used to store the Graphical information of the spatial nodes. We store the ID:
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

    Total Time = (3 sec, 833ms)
    1	->	2944.0
    2	->	2900.0
    3	->	2858.0
    4	->	2618.0
    .
    148	->	2618.0
    149	->	2556.0
    .
    228	->	2556.0
    229	->	2470.0
    .
    337	->	2470.0
    338	->	2320.0
    .
    998	->	2320.0
    999	->	2320.0
    1000	->	2320.0
    f2-->I	f3-->A	f6-->H	f1-->B	f8-->D	f9-->C	f7-->E	f4-->F	f5-->G	
    
  </td>
  <td>
  <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/resources/QAP/QAP_solution.png"  title="Optimal solution" alt="Optimal solution" width="400" height="700" />
  </td>
  </tr>
</table>


## Comment:
This solution is tested against an internet solver, and the proposed solutions are the same.
You will notice that the solution value proposed in the image = 1160, while for the ACO = 2320.
This is because we consider the flow table as outgoing flow values, while in the solver the flows are consider as 
outgoing+ingoing flows. This makes the flow matrix symmetric. As consequence, in the web solver the solution value is 
computed only using the above diagonal flow values. We preferred to keep a more general approach and allow the 
introduction of asymmetric flows between facilities. (The same happens with the distances) :smirk_cat:

