# OptimizationLib ACO Examples
In this folder, we can find different optimization problems solved using the OptimizationLib framework.
These examples serve as templates for readers who wish to understand and develop their own Ant Colony Optimization problems
(ACOs) using the OptimizationLib framework. By following the structures used in these examples, readers can customize and
fine-tune their ACOs to address specific problems.

| n | Package                                                                                                                                          | difficulty [1&rarr;5] | comment                                                                            |
|---|--------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------|------------------------------------------------------------------------------------|
| 1 | TSP [DenseMatrices](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/tree/master/src/main/java/org/soyaga/examples/TSP/DenseMatrices)   | 1                     | Traveling Salesman Problem using ArrayLists as Graph and PheromoneContainer.       |
| 2 | TSP [SparseMatrices](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/tree/master/src/main/java/org/soyaga/examples/TSP/SparseMatrices) | 1                     | Traveling Salesman Problem using Hashmaps as Graph and PheromoneContainer.         |
| 3 | TSP [GenericGraph](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/tree/master/src/main/java/org/soyaga/examples/TSP/GenericGraph)     | 2                     | Traveling Salesman Problem using a GenericGraph as Graph and PheromoneContainer.   |
| 4 | QAP [GenericGraph](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/tree/master/src/main/java/org/soyaga/examples/QAP/GenericGraph)     | 3                     | Quadratic Assignment Problem using a GenericGraph as Graph and PheromoneContainer. |
| 5 | QAP [Complex](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/tree/master/src/main/java/org/soyaga/examples/QAP/Complex)               | 4                     | Quadratic Assignment Problem in a more realistic example.                          |
| 6 | VRP [CapacitatedVRP](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/tree/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP) | 5                     | Real time CVRP with interactive interface.                                         |

The difficulty level indicates the range from the easiest (1) to the most challenging (5) problems in the repository.
These are classical ACO problems. We recommend that readers start with the easier ones, both conceptually and in
terms of implementation.

## In This Folder:
We find 2 different problems solved using the Ant Colony Optimization framework (ACO) from the OptimizationLib.
1. [TSP](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/tree/master/src/main/java/org/soyaga/examples/TSP):
   This problem involves finding the best (shortest) route visiting certain places.
2. [QAP](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/tree/master/src/main/java/org/soyaga/examples/QAP):
   This problem consists of assigning N facilities to N locations in a way that the proposed solution
   minimizes the sum of the product between flows and distances.
3. [VRP](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/tree/master/src/main/java/org/soyaga/examples/VRP):
   This problem involves finding the best set of routes for multiple vehicles.

## Comment:
The examples in these folders are meant to illustrate the power of ACOs and the simplicity of use/implementation
provided by the ACO library in the OptimizationLib framework. Keep in mind that these are examples you can use as
templates to adapt for your specific problems.