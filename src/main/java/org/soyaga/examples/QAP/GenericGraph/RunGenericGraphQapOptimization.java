package org.soyaga.examples.QAP.GenericGraph;

import lombok.Getter;
import org.soyaga.Initializer.ACOInitializer;
import org.soyaga.aco.Ant.BackHomeMemoryAnt;
import org.soyaga.aco.Ant.EdgeSelector.RandomProportionalEdgeSelector;
import org.soyaga.aco.Ant.SimpleMemoryAnt;
import org.soyaga.aco.Colony;
import org.soyaga.aco.Solution.AllNodesCircleSolution;
import org.soyaga.aco.SolutionConstructorPolicy.SimpleConstructorPolicy;
import org.soyaga.aco.SolutionEvaluatorPolicy.SimpleSolutionEvaluatorPolicy;
import org.soyaga.aco.SolutionEvaluatorPolicy.SolutionEvaluator.QapSolutionEvaluator;
import org.soyaga.aco.StopingCriteriaPolicy.MaxIterationCriteriaPolicy;
import org.soyaga.aco.UpdatePheromonePolicy.AddPheromonePolicy.SolFitnessProportionalAddPheromonePolicy;
import org.soyaga.aco.UpdatePheromonePolicy.EvaporatePheromonePolicy.PercentageEvaporatePheromonePolicy;
import org.soyaga.aco.UpdatePheromonePolicy.SimpleUpdatePheromonePolicy;
import org.soyaga.aco.world.GenericWorld;
import org.soyaga.aco.world.Graph.Elements.Edge;
import org.soyaga.aco.world.Graph.Elements.Node;
import org.soyaga.aco.world.Graph.GenericGraph;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Instantiates and optimize a GenericGraphQapAntColonyAlgorithm Object. Fills it with all the ACO classes needed to
 * perform the optimization. Optimizes given the previous configuration and prints on the screen the results.
 */
public class RunGenericGraphQapOptimization {
    public static void main(String ... args) throws IOException {
        //Integer with the number of ants.
        Integer antNumber =100;
        //Integer with the maximum number of edges an ants solution can have.
        Integer maxEdges = 100;
        //Double with the penalization added to a solution when the build solution is not a real solution.
        Double penalization = 1000.;
        //Double with the ants Alpha (>0) parameter (importance of the edges pheromones against the edges distances).
        Double alpha = 1.;
        //Double with the ants Beta (>0) parameter (importance of the edges distances against the edges pheromones).
        Double beta = 2.;
        //Integer with the maximum number of iterations for the optimization to run.
        Integer maxIterations = 1000;
        //Double with pheromone persistence in the edge.
        Double persistence = .9;
        //Double with initial pheromone present in the edge.
        Double initialPheromone = 0.;
        //String with the path to the distances between locations.
        String inputPathLocations = "QAP/locationsDistances.csv";
        //String with the path to the flows between facilities.
        String inputPathFlows = "QAP/facilitiesFlows.csv";
        //HashMap with the distances between Graph nodes that represent locations.
        HashMap<Object,HashMap<Object,Double>> locationsDistances=new HashMap<>();
        //HashMap with the flows between Graph nodes that represent facilities.
        HashMap<Object,HashMap<Object,Double>> facilitiesFlows = new HashMap<>();
        //GenericWorld Object (World Instance).
        GenericWorld<Node,Edge> world = createWorld(inputPathLocations, inputPathFlows, initialPheromone,
                locationsDistances,facilitiesFlows);
        //AntColonyAlgorithm instance.
        GenericGraphQapAntColonyAlgorithm aco = new GenericGraphQapAntColonyAlgorithm(
                "GenericGraphQAP",                                          //ID
                world,                                                          //GenericWorld
                new Colony(),                                                   //Colony
                antNumber,                                                      //Integer
                new BackHomeMemoryAnt(                                          //Ant Type
                        new AllNodesCircleSolution(                                 //SolutionType
                                world.getGraph(),                                       //Worlds Graph
                                maxEdges,                                               //Integer
                                penalization),                                          //Double
                        new RandomProportionalEdgeSelector(                         //EdgeSelector
                                alpha,                                                  //Double
                                beta)),                                                 //Double
                new MaxIterationCriteriaPolicy(                                 //StoppingCriteriaPolicy
                        maxIterations),                                             //Integer
                new ACOInitializer(),                                           //Initializer
                new SimpleConstructorPolicy(),                                  //ConstructorPolicy
                new SimpleSolutionEvaluatorPolicy(                              //SolutionEvaluatorPolicy
                        new QapSolutionEvaluator(                                   //SolutionEvaluator
                                locationsDistances,                                     //LocationsMap
                                facilitiesFlows,                                        //FacilitiesMap
                                true)),                                                 //Boolean
                new SimpleUpdatePheromonePolicy(                                //UpdatePheromonePolicy
                        new SolFitnessProportionalAddPheromonePolicy<>(),           //AddPheromonePolicy
                        new PercentageEvaporatePheromonePolicy<>(                   //EvaporatePheromonePolicy
                                persistence)),                                          //Double
                facilitiesFlows.keySet()                                        //Set of facilities
        );
        //Run optimizer.
        aco.optimize();
        //Print results.
        System.out.println(aco.getResult());
    }

    /**
     * Function that creates a World object and fills locationsDistances and facilitiesFlows maps.
     * <ul>
     *     <li><b>locationsDistances</b> HashMap{@literal <graphNode<graphNode,distance>>} where graphNode are nodes that refers to locations.</li>
     *     <li><b>facilitiesFlows</b> HashMap{@literal <graphNode<graphNode,flow>>} where graphNode are nodes that refers to facilities.</li>
     * </ul>
     *
     * @param inputPathLocations String with the filename path, where the problem information is contained.
     * @param locationsDistances HashMap{@literal <Node<Node,double>>} with the distanced between graph location-nodes.
     * @param facilitiesFlows HashMap{@literal <Node<Node,double>>} with the distanced between graph facility-nodes.
     * @return {@literal GenericWorld<Node,Edge>} with the world.
     */
    private static GenericWorld<Node, Edge> createWorld(
            String inputPathLocations, String inputPathFlows, Double initialPheromone,
            HashMap<Object, HashMap<Object, Double>> locationsDistances,
            HashMap<Object, HashMap<Object, Double>> facilitiesFlows) throws IOException {

        // Read Locations-distances file.
        InputStream stream = RunGenericGraphQapOptimization.class.getClassLoader().getResourceAsStream(inputPathLocations); //InputStream
        assert stream != null;
        BufferedReader br = new BufferedReader(new InputStreamReader(stream)); //BufferedReader
        HashMap<qapNode, HashMap<qapNode,Double>> mapLocationNodes = new HashMap<>(); //Hashmap<qapNode,HashMap<qapNode,distance>
        ArrayList<qapNode> locationsNodes = Arrays.stream(br.readLine().split(",")).map(qapNode::new).collect(Collectors.toCollection(ArrayList::new));
        Integer rowCounter = 0;
        String line = br.readLine();
        //read and build objects.
        while (line != null) {
            String [] lSplit = line.split(","); //Split line
            for (int i = 0; i < lSplit.length; i++) {
                HashMap<qapNode,Double> value =mapLocationNodes.computeIfAbsent(locationsNodes.get(rowCounter), k -> new HashMap<>());
                value.put(locationsNodes.get(i),Double.valueOf(lSplit[i]));
            }
            rowCounter+=1;
            // read next line
            line = br.readLine();
        }
        br.close(); //Close BufferedReader

        // Read Facilities-flows file.
        stream = RunGenericGraphQapOptimization.class.getClassLoader().getResourceAsStream(inputPathFlows); //InputStream
        assert stream != null;
        br = new BufferedReader(new InputStreamReader(stream)); //BufferedReader
        HashMap<qapNode, HashMap<qapNode,Double>> mapFacilitiesNodes = new HashMap<>(); //Hashmap<qapNode,HashMap<qapNode,distance>
        ArrayList<qapNode> facilitiesNodes = Arrays.stream(br.readLine().split(",")).map(qapNode::new).collect(Collectors.toCollection(ArrayList::new));
        rowCounter = 0;
        line = br.readLine();
        //read and build objects.
        while (line != null) {
            String [] lSplit = line.split(","); //Split line
            for (int i = 0; i < lSplit.length; i++) {
                HashMap<qapNode,Double> value =mapFacilitiesNodes.computeIfAbsent(facilitiesNodes.get(rowCounter), k -> new HashMap<>());
                value.put(facilitiesNodes.get(i),Double.valueOf(lSplit[i]));
            }
            rowCounter+=1;
            // read next line
            line = br.readLine();
        }
        br.close(); //Close BufferedReader

        //Create meaningful-heuristic maps
        HashMap<qapNode,Double> locationsMap = new HashMap<>(); // HashMap<location,sum of distances to other nodes>
        HashMap<qapNode,Double> facilityMap = new HashMap<>(); // HashMap<facility,sum of outgoing flows to other facilities>
        computeRelevantVectors(mapLocationNodes,mapFacilitiesNodes,locationsMap,facilityMap); // Compute maps
        HashMap<qapNode, HashMap<qapNode,Double>> heuristicInformation=
                computeHeuristicInformation(locationsMap,facilityMap);// Compute heuristic information distance=locationMap*facilityMap_transpose

        GenericGraph graph=new GenericGraph(initialPheromone); //CreateGraph
        heuristicInformation.forEach((key,val) -> graph.addNode(key.getID())); //Fill with nodes
        heuristicInformation.forEach(//Fill with edges
                (key,val) -> val.forEach(
                        (key2,val2)-> graph.initializeEdge(//InitializeEdges
                                key.getID(), //Integer with node1ID
                                key2.getID(), //Integer with node2ID
                                val2// double with the heuristic Distance
                        )
                )
        );

        //Compute map of distances between Location nodes in the Graph.
        for(Map.Entry<qapNode,HashMap<qapNode,Double>> locationEntry1: mapLocationNodes.entrySet()){
            for(Map.Entry<qapNode,Double> locationEntry2: locationEntry1.getValue().entrySet()){
                locationsDistances.computeIfAbsent(graph.getNode(locationEntry1.getKey().getID()), k->new HashMap<>()).
                        put(graph.getNode(locationEntry2.getKey().getID()),locationEntry2.getValue());
            }
        }

        //Compute map of flows between Facility nodes in the Graph.
        for(Map.Entry<qapNode,HashMap<qapNode,Double>> facilityEntry1: mapFacilitiesNodes.entrySet()){
            for(Map.Entry<qapNode,Double> facilityEntry2: facilityEntry1.getValue().entrySet()){
                facilitiesFlows.computeIfAbsent(graph.getNode(facilityEntry1.getKey().getID()), k->new HashMap<>()).
                        put(graph.getNode(facilityEntry2.getKey().getID()),facilityEntry2.getValue());
            }
        }
        return new GenericWorld<>(graph,graph);
    }


    /**
     * This function computes the "centrality" of the nodes in their spaces. How central a location is and how demanded a
     * facility is. this is represented by the sum of the distances/flows between one location/facility and the rest of
     * the locations/facilities.
     * @param mapLocationNodes HashMap with the locations and distances.
     * @param mapFacilitiesNodes HashMap with the facilities and their flow.
     * @param locationsMap HashMap with the sum of distances from this location to the rest (connected nodes).
     * @param facilityMap HashMap with the sum of flows that income to each Facility.
     */
    private static void computeRelevantVectors(HashMap<qapNode, HashMap<qapNode, Double>> mapLocationNodes,
                                               HashMap<qapNode, HashMap<qapNode, Double>> mapFacilitiesNodes,
                                               HashMap<qapNode, Double> locationsMap,
                                               HashMap<qapNode, Double> facilityMap) {
        //Compute locations cumulative distance Map.
        for (Map.Entry<qapNode,HashMap<qapNode,Double>> entry:mapLocationNodes.entrySet()){
            locationsMap.put(entry.getKey(),entry.getValue().values().stream().reduce(0., Double::sum));
        }
        //Compute facilities cumulative outgoing flow Map.
        for (Map.Entry<qapNode,HashMap<qapNode,Double>> entry:mapFacilitiesNodes.entrySet()){
            facilityMap.put(entry.getKey(),entry.getValue().values().stream().reduce(0., Double::sum));
        }
    }


    /**
     * Function that returns a map containing &lt;originNode,&lt;desNode,"distance"&gt;&gt;. Here the "distance" is
     * the potential benefit of assigning a high demanded node to a centralized place. This is done by multiplying the
     * sum of distances from one node(location) to the others (the smaller, the more central) by the inverse of the
     * sum of flows from one node(facility) to others (the smaller, the more demanded).
     * @param locationsVector HashMaps with the locations and the "total" outgoing distance.
     * @param facilityVectors HashMap with the facilities and the "total" outgoing flow.
     * @return Hashmap {@literal qapNode,<HashMap<qapNode,Double>>} with the computed heuristic "distance".
     */
    private static HashMap<qapNode, HashMap<qapNode, Double>> computeHeuristicInformation(
            HashMap<qapNode, Double> locationsVector,
            HashMap<qapNode, Double> facilityVectors) {
        // Heuristic information HashMap.
        HashMap<qapNode, HashMap<qapNode, Double>> heuristicInformation=new HashMap<>();
        //For each location
        for(Map.Entry<qapNode,Double> locationEntry: locationsVector.entrySet()){
            //For each facility
            for(Map.Entry<qapNode,Double> facilityEntry: facilityVectors.entrySet()){
                // Going from a facility to a location is assign a facility to a location.
                // This heuristic "distance" is smaller (more attractive for the ant) when a high demanded facility is
                // located in a centralized location.
                heuristicInformation.computeIfAbsent(locationEntry.getKey(),key ->new HashMap<>()).put(
                        facilityEntry.getKey(),locationEntry.getValue()/facilityEntry.getValue()
                );
                // Going from a location to a facility is choosing the order in which we assign the locations.
                // This heuristic information is 1 for all the cases because we want to allow the ants to find the order
                // of assignment by their own. (we could have chosen some metric based on the facility relevance, like
                // the more demanded the facility the more attractive to choose that facility as next).
                heuristicInformation.computeIfAbsent(facilityEntry.getKey(), key -> new HashMap<>()).put(
                        locationEntry.getKey(),1.
                );
            }
        }
        return heuristicInformation;
    }

    /**
     * Class that represents a QAP node.  Contains:
     * <ul>
     *     <li> Integer with a node ID.</li>
     * </ul>
     * Although this class don't have much information, we prefer to keep it in order to allow the reader to extrapolate
     * this use case to others where we have locations with positions, os facilities with complex nodes. These kind of
     * classes help as information containers in the ETL.
     */
    @Getter
    public static class qapNode {
        /**
         * Integer with node ID.
         */
        private final String ID;

        /**
         * Constructor
         * @param id String with the node ID assumes it is an Integer.
         */
        private qapNode(String id) {
            this.ID = id;
        }
    }
}
