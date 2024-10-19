# Quadratic Assignment Problem (QAP)

The QAP can best be described as: "The problem of assigning a set of facilities to a set of locations with given distances between the locations and given flows between the facilities. The goal is to place the facilities on locations in such a way that the sum of the product between flows and distances is minimal."

<table>
  <tr>
    <td colspan="2"> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/QAP/CurrentCustomMap__Custom_Balance.gif" title="Solution complex balance case." alt="Solution complex balance case." width="500" height="500" /></td>
  </tr>
  <tr>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/QAP/QAP_Convergence_10_Ants.png" title="Convergence for the GenericGraph QAP." alt="Convergence for the GenericGraph QAP." width="400" height="400" /></td>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/QAP/QAP_Pheromone_GIF_10_Ants.gif" title="Pheromone for the GenericGraph QAP." alt="Pheromone for the GenericGraph QAP." width="400" height="400" /></td>
  </tr>
</table>

For more information about the problem, refer here.

## In this folder:

We find two different cases for the QAP optimization problem using the Ant Colony Algorithm (ACO) from the OptimizationLib.aco:

1. GenericGraph: Solve the problem using a graph representation as an information container.
2. Complex: Solve a QAP-like problem that addresses a real company optimization problem.

## About the Cases:

While a complete Ant Colony Algorithm typically comprises a minimum of 17 distinct classes, a minimal implementation can be as concise as two classes, tailored to the specific problem at hand. This streamlined approach empowers us to develop an Ant Colony Optimization (ACO) capable of efficiently optimizing computationally challenging problems, including those categorized as P, NP, NP-Complete, and/or NP-Hard.

In the provided examples, we make use of built-in classes from OptimizationLib.aco to facilitate the ACO optimization approach.

**1. GenericGraph**:
This example offers a simple implementation. The main challenge lies in creating the world representation. The world should accurately represent the problem at hand, and a solution must be constructed by navigating this world. Ideally, the world should contain heuristic information to guide the ants in the solution-building process.

**2. Complex**:
This is a real-world scenario, subject to logistic constraints, business constraints, multi-objective optimizations, and variations in the number of locations and facilities. It necessitates a precise definition of what constitutes a solution and the rules that ants should follow while constructing it.

## Comment:

The examples provided in these directories serve solely to showcase the capabilities of ACOs and the user-friendly implementation afforded by the ACO library within the OptimizationLib framework. The problem at hand is addressed using three distinct configurations, with certain configurations being more apt for this particular problem than others. It's important to bear in mind that these are illustrative examples, meant to serve as templates that you can modify to suit your specific problems.
