# OptimizationLib ACO examples
In this repository, we can find different optimization problems. The problems are solved using the OptimizationLib framework.
The examples are templates for the reader. Who can understand and develop his/her own ACOs using the
OptimizationLib by following the structures used in these examples.

| n | Package                                                                                                                                          | difficulty [1&rarr;5] | comment                                                                            |
|---|--------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------|------------------------------------------------------------------------------------|
| 1 | TSP [DenseMatrices](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/tree/master/src/main/java/org/soyaga/examples/TSP/DenseMatrices)   | 1                     | Traveling Salesman Problem using ArrayLists as Graph and PheromoneContainer.       |
| 2 | TSP [SparseMatrices](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/tree/master/src/main/java/org/soyaga/examples/TSP/SparseMatrices) | 1                     | Traveling Salesman Problem using Hashmaps as Graph and PheromoneContainer.         |
| 3 | TSP [GenericGraph](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/tree/master/src/main/java/org/soyaga/examples/TSP/GenericGraph)     | 2                     | Traveling Salesman Problem using a GenericGraph as Graph and PheromoneContainer.   |
| 4 | QAP [GenericGraph](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/tree/master/src/main/java/org/soyaga/examples/QAP/GenericGraph)     | 3                     | Quadratic Assignment Problem using a GenericGraph as Graph and PheromoneContainer. |
| 5 | QAP [Complex](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/tree/master/src/main/java/org/soyaga/examples/QAP/Complex)               | 4                     | Quadratic Assignment Problem in a more realistic example.                          |
| 6 | VRP [CapacitatedVRP](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/tree/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP) | 5                     | Real time CVRP with interactive interface.                                         |

The difficulty level is a measure of the minimum (1) and maximum (5) difficulty level in the repo. These are classical ACO
problems. We recommend the reader to start with the easier ones, conceptually and in implementation.

## Comment:
The examples in this repository are just to illustrate the power of ACOs, and the simplicity of use/implementation
that give us the ga library in the OptimizationLib framework. Just remember, they are examples that you can
use as template to adapt for your specific optimization problems. 