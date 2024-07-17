# Vehicle Routing Problem (VRP)
The vehicle routing problem asks the following question: "What is the optimal set of routes for a fleet of vehicles to traverse in order to deliver to a given set of customers?" It is an NP-hard problem in combinatorial optimization.


<table>
  <tr>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/VRP/CapacitatedVRP_Google.gif"  title="Example for the CVRP" alt="Example for the CVRP" /></td>
  </tr>
</table>

For more information about the problem, refer [Here](https://en.wikipedia.org/wiki/Vehicle_routing_problem)

## In this folder:
We find one designs approached for solving the VRP optimization problem using the Ant Colony Algorithm (ACO)
from the OptimizationLib.aco.
1. [CapacitatedVRP](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP):
   Solve the Capacitated Vehicle Routing Problem.

## About the approach:
While a minimal implementation can consist of just 3 to 4 classes, a complete AntColonyAlgorithm comprises at least 17 distinct classes. This complete implementation
allows us to devise an ACO capable of optimizing computationally challenging problems categorized as P, NP, NP-Complete,
and/or NP-Hard.

In the provided example, we implement/extend many of the build-in classes in the OptimizationLib.aco. 
The example at hand can be used as a reference for a complex ACO implementation using the classes and following the guardrails of the OptimizationLib.aco

## Comment:
The example provided in this directory serves solely to showcase the capabilities of ACOs and the intuitive, but flexible,
implementation afforded by the aco library within the OptimizationLib framework. It's
important to bear in mind that this is an illustrative example, meant to serve as templates that you can modify to suit your specific problems.

