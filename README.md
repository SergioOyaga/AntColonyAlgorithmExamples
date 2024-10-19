# OptimizationLib ACO Examples
This repository contains different optimization problems solved using the OptimizationLib framework.
The examples are templates for readers who want to understand and develop their own ACOs using OptimizationLib by following the structures used in these examples.

| n | Package                                                                                                                                          | Difficulty [1→5] | Comment                                                                                   |
|---|--------------------------------------------------------------------------------------------------------------------------------------------------|------------------|-------------------------------------------------------------------------------------------|
| 1 | TSP [DenseMatrices](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/tree/master/src/main/java/org/soyaga/examples/TSP/DenseMatrices)   | 1                | Traveling Salesman Problem using `ArrayList`s as the graph and pheromone container.       |
| 2 | TSP [SparseMatrices](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/tree/master/src/main/java/org/soyaga/examples/TSP/SparseMatrices) | 1                | Traveling Salesman Problem using `HashMap`s as the graph and pheromone container.         |
| 3 | TSP [GenericGraph](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/tree/master/src/main/java/org/soyaga/examples/TSP/GenericGraph)     | 2                | Traveling Salesman Problem using a `GenericGraph` as the graph and pheromone container.   |
| 4 | QAP [GenericGraph](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/tree/master/src/main/java/org/soyaga/examples/QAP/GenericGraph)     | 3                | Quadratic Assignment Problem using a `GenericGraph` as the graph and pheromone container. |
| 5 | QAP [Complex](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/tree/master/src/main/java/org/soyaga/examples/QAP/Complex)               | 4                | Quadratic Assignment Problem in a more realistic example.                                 |
| 6 | VRP [CapacitatedVRP](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/tree/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP) | 5                | Real-time CVRP with an interactive interface.                                             |

The difficulty level is a measure of the minimum (1) and maximum (5) difficulty level in the repository. These are classical ACO
problems. We recommend that readers start with the easier ones, both conceptually and in terms of implementation.

## Comment
The examples in this repository are meant to illustrate the power of ACOs and the simplicity of use and implementation
that the OptimizationLib GA library provides. Remember that these are just examples that you can
use as templates and adapt for your specific optimization problems. 
