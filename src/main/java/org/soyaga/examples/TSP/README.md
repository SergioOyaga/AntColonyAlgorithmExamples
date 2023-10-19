# Travelling Salesman Problem (TSP)
The traveling salesman problem asks the following question: "Given a list of cities and the distances between 
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

For more information about the problem, refer [Here](https://en.wikipedia.org/wiki/Travelling_salesman_problem)

## In this folder:
We find three different designs approached for solving the TSP optimization problem using the Ant Colony Algorithm (ACO)
from the OptimizationLib.aco.
1. [DenseMatrices](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/TSP/DenseMatrices):
   Solve the problem using dense matrices as information containers.
2. [SparseMAtrices](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/TSP/SparseMatrices):
   Solve the problem using sparse matrices as information containers.
3. [GenericGraph](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/TSP/GenericGraph):
   Solve the problem using a Graph representation as information container.

## About the approaches:
While a complete AntColonyAlgorithm comprises at least 17 distinct classes, a minimal implementation can 
consist of just 3 to 4 classes, which are contingent on the problem being addressed. This streamlined implementation 
allows us to devise an ACO capable of optimizing computationally challenging problems categorized as P, NP, NP-Complete,
and/or NP-Hard.

In the provided examples, we leverage the built-in classes from OptimizationLib.aco to facilitate the ACO optimization 
approach. The three information structures employed are the most commonly used for storing data. The majority of the 
problems we aim to optimize can be reduced to one of these structures.

Nonetheless, we don't impose the use of these structures; you are free to adopt any World, Graph, or PheromoneContainer
that suits your needs. You can opt for more sophisticated structures that align better with your design; you just need 
to adhere to the requisite interfaces.

## Comment:
The examples provided in these directories serve solely to showcase the capabilities of ACOs and the user-friendly
implementation afforded by the aco library within the OptimizationLib framework. The problem at hand is addressed using 
three distinct configurations, with certain configurations being more apt for this particular problem than others. It's
important to bear in mind that these are illustrative examples, meant to serve as templates that you can modify to suit 
your specific problems.

