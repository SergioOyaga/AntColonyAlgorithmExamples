# Travelling Salesman Problem (TSP)
The travelling salesman problem asks the following question: "Given a list of cities and the distances between 
each pair of cities, what is the shortest possible route that visits each city exactly once and returns to the origin 
city?" It is an NP-hard problem in combinatorial optimization.


<table>
  <tr>
    <td colspan="2"> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/TSP/colonyGif.gif"  title="Solution for the ColonyPath" alt="Solution for the colonyPath" width="750" height="300" /></td>
  </tr>
  <tr>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/TSP/hexagonGif.gif"  title="Solution for the HexagonPath" alt="Solution for the hexagonPath" width="350" height="300" /></td>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/TSP/oliver30Gif.gif"  title="Solution for the oliver30Path" alt="Solution for the oliver30Path" width="400" height="405" /></td>
  </tr>
</table>

For more information about the problem refer [Here](https://en.wikipedia.org/wiki/Travelling_salesman_problem)

## In this folder:
We find 3 different design approached for solving the TSP optimization problem using the Ant Colony Algorithm (ACO)
from the OptimizationLib.
1. [DenseMatrices](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/TSP/DenseMatrices):
   Solve the problem using dense matrices as information containers.
2. [SparseMAtrices](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/TSP/SparseMAtrices):
   Solve the problem using sparse matrices as information containers.
3. [GenericGraph](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/TSP/GenericGraph):
   Solve the problem using a Graph representation as information container.

## About the approaches:
Although a complete AntColonyAlgorithm consist in at least 13 different classes,
with a minimal implementation of 2-3 classes (which are problem dependent) we can design an ACO that allow us to "optimize"
some computationally "hard" problems (P, NP, NP-Complete and/or NP-Hard).

In the examples we use the OptimizationLib build-in classes for the ACO optimization methodology.
The three information structures used are the three most frequently used to store information. Most of the problems we 
will want to optimize could be reduced to one of these structures.

However, we do not force the use of these structures, you can have whatever you want as World, Graph or 
PheromoneContainer. You can have any fancy structure that better fits your design, you just have to commit to the 
required interfaces. 

## Comment:
The examples in these folders are just to illustrate the power of ACOs, and the simplicity of use/implementation
that give us the aco library in the OptimizationLib framework. The problem is solved using 3 different configurations,
some configurations are more suitable for this problem and others are not. Just remember they are examples that you can
use as template to adapt for your specific problems.
