package org.soyaga.examples.QAP.Complex;

import org.soyaga.Initializer.ACOInitializer;
import org.soyaga.aco.Ant.EdgeSelector.RandomProportionalEdgeSelector;
import org.soyaga.aco.Colony;
import org.soyaga.aco.Evaluable.Feasibility.ConstraintBasedFeasibilityFunction;
import org.soyaga.aco.Evaluable.Objective.MultiObjective.MultiObjective;
import org.soyaga.aco.Evaluable.Objective.MultiObjective.Scalarize.WeightedNormScalarize;
import org.soyaga.aco.Evaluable.Objective.ObjectiveFunction;
import org.soyaga.aco.Evaluable.Objective.QapObjectiveFunction;
import org.soyaga.aco.Solution;
import org.soyaga.aco.SolutionConstructorPolicy.SimpleConstructorPolicy;
import org.soyaga.aco.StatsRetrievalPolicy.NIterationsStatsRetrievalPolicy;
import org.soyaga.aco.StatsRetrievalPolicy.Stat.*;
import org.soyaga.aco.StoppingPolicy.MaxIterationCriteriaPolicy;
import org.soyaga.aco.UpdatePheromonePolicy.AddPheromonePolicy.MaxGlobalBestFitnessProportionalAddPheromonePolicy;
import org.soyaga.aco.UpdatePheromonePolicy.EvaporatePheromonePolicy.MinPercentageEvaporatePheromonePolicy;
import org.soyaga.aco.UpdatePheromonePolicy.SimpleUpdatePheromonePolicy;
import org.soyaga.aco.world.GenericWorld;
import org.soyaga.aco.world.Graph.Elements.Node;
import org.soyaga.aco.world.Graph.GenericGraph;
import org.soyaga.examples.QAP.Complex.Stats.*;

import javax.imageio.ImageIO;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;



/**
 * This class instantiates and optimizes a CustomQapAntColonyAlgorithm object. It populates the object with all
 * the necessary ACO classes required to perform the optimization. The optimization is carried out based on the
 * provided configuration. The statistics are stored and displayed on the screen, while the final result is also printed.
 */
public class RunCustomQapOptimization {
    public static void main(String ... args) throws IOException {
        //////////////////////////////////////////////////////////
        //                       Paths                          //
        //////////////////////////////////////////////////////////
        String inputPath = "QAP";   //String with the path to the distances between locations.
        String outputPath = "src/out/QAP";  //String with the path to where we want to store the stats.

        //////////////////////////////////////////////////////////
        //      Read inputs and basic dictionary structures     //
        //////////////////////////////////////////////////////////
        HashMap<String, Facility> facilitiesById = new HashMap<>(); //HashMap with the Facility (qapNode) by ID.
        HashMap<String, Location> locationsById = new HashMap<>();  //HashMap with the Location (qapNode) by ID.
        BufferedImage image = readInputs(inputPath,facilitiesById, locationsById);  //BufferedImage with the original map image.

        ////////////////////////////////////////////////////////////////
        //     Heuristic information and multi-objective weights      //
        // Recommended: distanceWeight + timeWeight + costWeight = 1. //
        ////////////////////////////////////////////////////////////////
        Double distanceWeight = 1/3.;  //Double with the relevance of the distances for the problem. (Used in heuristic info and multi-objective)
        Double timeWeight= 1/3.;   //Double with the relevance of the travel times for the problem. (Used in heuristic info and multi-objective)
        Double costWeight= 1/3.;   //Double with the relevance of the transportation costs for the problem. (Used in heuristic info and multi-objective)

        //////////////////////////////////////////////////////////
        //                       World                          //
        //////////////////////////////////////////////////////////
        Double initialPheromone = 1.;   //Double with initial pheromone present in the edge.
        GenericWorld world = createWorld(initialPheromone, facilitiesById, locationsById,
                distanceWeight, timeWeight, costWeight );   //GenericWorld Object (World Instance).

        //////////////////////////////////////////////////////////
        //                  StoppingPolicy                      //
        //////////////////////////////////////////////////////////
        Integer maxIterations = 500;   //Integer with the maximum number of iterations for the optimization to run.

        //////////////////////////////////////////////////////////
        //                  Initializer                         //
        //////////////////////////////////////////////////////////
        int antNumber =30;  //Integer with the number of ants.

        //////////////////////////////////////////////////////////
        //             Ant (inside Initializer)                 //
        //////////////////////////////////////////////////////////
        Double antPheromoneQuantity=100000.; //Double with the amount of pheromone each ant can deposit in its track (same order of the problem optimal fitness).

        //////////////////////////////////////////////////////////
        //             EdgeSelector (inside Ant)                //
        //////////////////////////////////////////////////////////
        Double alpha = 1.;  //Double with the ants Alpha (>0) parameter (importance of the edges pheromones against the edges "distances").
        Double beta = 2.;   //Double with the ants Beta (>0) parameter (importance of the edges "distances" against the edges pheromones).

        //////////////////////////////////////////////////////////
        //               Solution (inside Ant)                  //
        //////////////////////////////////////////////////////////
        Double penalization = 10000000.; //Double with the infeasibility penalization.
        Integer maxEdges = 30;  //Integer with the maximum number of edges an ant's solution can have.

        //////////////////////////////////////////////////////////
        //           Objectives (inside Solution)               //
        //////////////////////////////////////////////////////////
        Integer norm = 1;   //Integer with the norm to use in the scalarization of the multi-objective function.
        ArrayList<ObjectiveFunction> objectiveArray =new ArrayList<>(); //ArrayList with the objectives to evaluate.
        HashMap<ObjectiveFunction,Double> weights = new HashMap<>();    //Hashmap with the objectives and their weights.
        computeObjectives(distanceWeight,timeWeight,costWeight,objectiveArray,weights, facilitiesById,locationsById);

        //////////////////////////////////////////////////////////
        //           Constraints (inside Solution)              //
        //////////////////////////////////////////////////////////
        Double minFlow=8000.;   //Double with the minimum flow in the system. Used in a Constraint.
        Double maxFlow = 10000.; //Double with the maximum flow in the system. Used in a Constraint.

        //////////////////////////////////////////////////////////
        //         SolutionBuilder (inside Solution)            //
        //////////////////////////////////////////////////////////
        Facility initialFacility = facilitiesById.get("F1");    //Facility with the initial node.

        //////////////////////////////////////////////////////////
        //   AddPheromonePolicy (inside UpdatePheromonePolicy)  //
        //////////////////////////////////////////////////////////
        Double maxPheromone = 1.;   //Double with max pheromone an edge can have.

        /////////////////////////////////////////////////////////////
        // EvaporatePheromonePolicy (inside UpdatePheromonePolicy) //
        /////////////////////////////////////////////////////////////
        Double persistence = .99;   //Double with pheromone persistence in the edge.
        Double minPheromone = .01;  //Double with min pheromone an edge can have.

        //////////////////////////////////////////////////////////
        //                      Stats                           //
        //////////////////////////////////////////////////////////
        Double pheromoneIntensityForGIF = 10.;  //Double with the pheromone intensity.
        Integer steps = 1;  //Integer with the steps between stat measures.
        Integer nOfDecimals = 4;   //Integer with the number of decimals to plot in the screen.

        //////////////////////////////////////////////////////////
        //                  AntColonyAlgorithm                  //
        //////////////////////////////////////////////////////////
        CustomQapAntColonyAlgorithm aco = new CustomQapAntColonyAlgorithm(  //AntColonyAlgorithm instance.
                "ComplexQAP",                                           //ID
                world,                                                  //World, GenericWorld (sparse components).
                new Colony(),                                           //Colony.
                new MaxIterationCriteriaPolicy(                         //StoppingCriteriaPolicy, max iterations:
                        maxIterations),                                     //Integer, max iterations.
                new ACOInitializer(                                     //Initializer:
                        new CustomAnt(                                      //Ant, custom ant.
                                new Solution(                                   //Solution:
                                        new MultiObjective(                         //ObjectiveFunction, multi-objective:
                                                objectiveArray,                         //Array Of objectives:
                                                new WeightedNormScalarize(              //Scalarize, WeightedNorm:
                                                   weights,                                 //HashMap, weights.
                                                   norm                                     //Integer, Lx norm.
                                                )
                                        ),
                                        new ConstraintBasedFeasibilityFunction(     //FeasibilityFunction, constraint based:
                                                new ArrayList<>(){{                     //Array of constraints.
                                                    add(new CustomConstraint(               //Constraint, business boundaries.
                                                            minFlow,                            //Double, min flow.
                                                            maxFlow,                            //Double, max flow.
                                                            new HashSet<>(){{addAll(facilitiesById.values());}} //Facilities.
                                                    ));
                                                }}
                                        ),
                                        penalization,                               //Double, penalization.
                                        maxEdges,                                   //Integer, max number of edges.
                                        new CustomBuilderEvaluator(                 //BuilderEvaluator, stops when a node is visited twice.
                                                initialFacility                         //Node, initial node.
                                        )
                                ),
                                new RandomProportionalEdgeSelector(             //Edge Selector, random-proportional:
                                        alpha,                                      //Double, pheromone importance.
                                        beta                                        //Double, distance importance.
                                ),
                                antPheromoneQuantity,                           //Double, pheromone quantity.
                                new HashSet<>(){{addAll(locationsById.values());}}  //HashSet with the locations.
                        ),
                        antNumber                                           //Integer, number of ants to initialize.
                ),
                new SimpleConstructorPolicy(),                          //ConstructorPolicy, ant by ant.
                new SimpleUpdatePheromonePolicy(                        //UpdatePheromonePolicy, first evaporate then add:
                        new MaxGlobalBestFitnessProportionalAddPheromonePolicy( //AddPheromonePolicy, prop to sol fitness:
                                maxPheromone                                        //Double, max pheromones.
                        ),
                        new MinPercentageEvaporatePheromonePolicy(          //EvaporatePheromonePolicy, percentage persistence:
                                persistence,                                    //Double, persistence.
                                minPheromone)                                   // Double, min pheromone.
                ),
                new NIterationsStatsRetrievalPolicy(                    //Stats retrieval policy, every n iterations:
                        steps,                                              //Integer, steps between measures.
                        new ArrayList<>(){{                                 //Array Of Stats
                            add(new CurrentMinFitnessStat(nOfDecimals));        //Min Fitness Stat.
                            add(new CurrentMaxFitnessStat(nOfDecimals));        //Max Fitness Stat.
                            add(new HistoricalMinFitnessStat(nOfDecimals));     //Hist Min Fitness Stat.
                            add(new MeanSdFitnessStat(nOfDecimals));            //Fitness Mean and Standard Dev Stat.
                            add(new MeanSdPheromoneStat(nOfDecimals));          //Fitness Mean and Standard Dev Stat.
                            add(new CurrentMinDistanceCustomStat(nOfDecimals,   //Min Distance Stat
                                    new ArrayList<>(){{
                                        addAll(facilitiesById.values());
                                    }}
                            ));
                            add( new CurrentMinTimeCustomStat(nOfDecimals,      //Min Time Stat
                                    new ArrayList<>(){{
                                        addAll(facilitiesById.values());
                                    }}
                            ));
                            add( new CurrentMinCostCustomStat(nOfDecimals,      //Min Cost Stat
                                    new ArrayList<>(){{
                                        addAll(facilitiesById.values());
                                    }}
                            ));
                            add( new CurrentMinFlowCustomStat(nOfDecimals,      //Min Flow Stat
                                    new ArrayList<>(){{
                                        addAll(facilitiesById.values());
                                    }}
                            ));
                            add(new MeanSdDistanceStat(nOfDecimals,             //MeanSd Distance Stat
                                    new ArrayList<>(){{
                                        addAll(facilitiesById.values());
                                    }}
                            ));
                            add( new MeanSdTimeStat(nOfDecimals,                //MeanSd Time Stat
                                    new ArrayList<>(){{
                                        addAll(facilitiesById.values());
                                    }}
                            ));
                            add( new MeanSdCostStat(nOfDecimals,                //MeanSd Cost Stat
                                    new ArrayList<>(){{
                                        addAll(facilitiesById.values());
                                    }}
                            ));
                            add( new MeanSdFlowStat(nOfDecimals,                //MeanSd Flow Stat
                                    new ArrayList<>(){{
                                        addAll(facilitiesById.values());
                                    }}
                            ));
                            add(new PheromoneContainerGIFStat(                  //Stat, Pheromone container representation.
                                    world,
                                    outputPath+"/",
                                    pheromoneIntensityForGIF));
                            add(new ImageStat(                                  //Stat, Problem real image.
                                    outputPath+"/",
                                    image,
                                    new HashSet<>(){{addAll(facilitiesById.values());}}
                                    ));
                            add(new CurrentMinImageStat(                                  //Stat, Problem real image.
                                    outputPath+"/",
                                    image,
                                    new HashSet<>(){{addAll(facilitiesById.values());}}
                            ));
                        }},
                        outputPath,                                         //String, with the output path.
                        true,                                               //Boolean, print in console.
                        true                                                //Boolean, store in csv file.
                )
        );

        //////////////////////////////////////////////////////////
        //                Run AntColonyAlgorithm                //
        //////////////////////////////////////////////////////////
        aco.optimize();//Run optimizer.

        //////////////////////////////////////////////////////////
        //                  Print Assignation                   //
        //////////////////////////////////////////////////////////
        System.out.println(aco.getResult(facilitiesById.values()));//Print results.
    }


    /**
     * This function computes the individual components of the multi-objective function: Distance, Time, and
     * Transportation Cost.
     *
     * @param distanceWeight Double representing the weight of the distance objective.
     * @param timeWeight Double representing the weight of the time objective.
     * @param costWeight Double representing the weight of the cost objective.
     * @param objectiveArray ArrayList used to store the objectives.
     * @param weights HashMap used to store the weight of each objective.
     * @param facilitiesById HashMap containing the facility nodes (Graph Nodes).
     * @param locationsById HashMap containing the location nodes (Graph Nodes).
     */
    private static void computeObjectives(Double distanceWeight, Double timeWeight, Double costWeight,
                                          ArrayList<ObjectiveFunction> objectiveArray,
                                          HashMap<ObjectiveFunction, Double> weights,
                                          HashMap<String, Facility> facilitiesById,
                                          HashMap<String, Location> locationsById) {
        HashMap<Object, HashMap<Object, Double>> facilityFlows=new HashMap<>();
        for(Map.Entry<String, Facility> facility1Entry:facilitiesById.entrySet()){
            facilityFlows.put(facility1Entry.getValue(),facility1Entry.getValue().facilityOutgoingFlow);
        }
        //HashMaps to use the QAP Objective function (no need to implement custom Objectives)
        HashMap<Object, HashMap<Object, Double>> locationDistances=new HashMap<>();
        HashMap<Object, HashMap<Object, Double>> locationTimes=new HashMap<>();
        HashMap<Object, HashMap<Object, Double>> locationCosts=new HashMap<>();
        for(Map.Entry<String, Location> location1Entry:locationsById.entrySet()){
            locationDistances.put(location1Entry.getValue(),location1Entry.getValue().locationDistances);
            locationTimes.put(location1Entry.getValue(),location1Entry.getValue().locationTimes);
            locationCosts.put(location1Entry.getValue(),location1Entry.getValue().locationCosts);
        }
        //Create the objectives.
        ObjectiveFunction distancesObjective = new QapObjectiveFunction(locationDistances,facilityFlows,true);
        ObjectiveFunction timesObjective = new QapObjectiveFunction(locationTimes,facilityFlows,true);
        ObjectiveFunction costsObjective = new CustomCostObjective(locationCosts,facilityFlows);
        // Add to the array for the multi-objective.
        objectiveArray.add(distancesObjective);
        objectiveArray.add(timesObjective);
        objectiveArray.add(costsObjective);
        // Put in the map with the weights.
        weights.put(distancesObjective,distanceWeight);
        weights.put(timesObjective,timeWeight);
        weights.put(costsObjective,costWeight);
    }

    /**
     * Function that creates a World object.
     *
     * @param initialPheromone Double with the amount of pheromone to place initially in the edges.
     * @param facilitiesById HashMap{@literal <String, Facility>} with the facilities.
     * @param locationsById HashMap{@literal <String, Location>} with the locations.
     * @param distanceWeight  Double with the weight of the distance to compute centrality.
     * @param timeWeight  Double with the weight of the travel time to compute centrality.
     * @param costWeight  Double with the weight of the transportation cost to compute centrality.
     * @return {@literal GenericWorld<Node,Edge>} with the world.
     */
    private static GenericWorld createWorld(
            Double initialPheromone, HashMap<String, Facility> facilitiesById, HashMap<String, Location> locationsById,
            Double distanceWeight, Double timeWeight, Double costWeight){

        //Create meaningful-heuristic maps
        HashMap<Location,Double> locationMap = new HashMap<>(); // HashMap<location,sum of weighted distances,times and costs to other nodes>
        HashMap<Facility,Double> facilityMap = new HashMap<>(); // HashMap<facility,sum of outgoing flows to other facilities>
        computeRelevantVectors(facilitiesById,locationsById,facilityMap,locationMap,distanceWeight, timeWeight, costWeight); // Compute maps
        HashMap<qapNode, HashMap<qapNode,Double>> heuristicInformation=
                computeHeuristicInformation(facilityMap,locationMap);// Compute heuristic information distance=locationMap*facilityMap_transpose

        GenericGraph graph=new GenericGraph(initialPheromone); //CreateGraph
        facilitiesById.forEach((key,val) -> graph.addNode(val)); //Fill with facilities
        locationsById.forEach((key,val) -> graph.addNode(val)); //fill with locations
        //Edges from facilities to locations
        for(Facility facility: facilitiesById.values()){
            for(String locationID: facility.potentialLocationsIDs){
                graph.initializeEdge(//InitializeEdges
                        facility, //Node with node1ID
                        locationsById.get(locationID), //Node with node2ID
                        heuristicInformation.get(facility).get(locationsById.get(locationID)));// double with the heuristic Distance
            }
        }
        //Edges from locations to facilities
        for(Location location: locationsById.values()){
            Collection<Facility> facilities = facilitiesById.values();
            if (location.restrictionRelation !=null){
                facilities = new HashSet<>(){{add(facilitiesById.get(location.restrictionRelation));}};
            }
            for(Facility facility: facilities){
                graph.initializeEdge(//InitializeEdges
                        location, //Node with node1ID
                        facility, //Node with node2ID
                        heuristicInformation.get(location).get(facility));// double with the heuristic Distance
            }
        }
        return new GenericWorld(graph,graph);
    }

    /**
     * This function computes the "centrality" of the nodes in their respective spaces. It assesses how central a location is
     *      * and how demanded a facility is. This is determined by the weighted sum of distances, times, and costs between
     *      * locations, as well as the sum of flows between facilities.
     *
     * @param facilitiesById HashMap{@literal <String, Facility>} containing the facilities.
     * @param locationsById HashMap{@literal <String, Location>} containing the locations.
     * @param facilityMap HashMap containing the sum of flows that come into each Facility.
     * @param locationMap HashMap containing the centrality of each location in relation to connected nodes.
     * @param distanceWeight Double representing the weight of distance when computing centrality.
     * @param timeWeight Double representing the weight of travel time when computing centrality.
     * @param costWeight Double representing the weight of transportation cost when computing centrality.
     */
    private static void computeRelevantVectors(HashMap<String, Facility> facilitiesById, HashMap<String, Location> locationsById,
                                               HashMap<Facility, Double> facilityMap,
                                               HashMap<Location, Double> locationMap,
                                               Double distanceWeight, Double timeWeight, Double costWeight) {
        //Compute facilities cumulative outgoing flow Map.
        for (Map.Entry<String, Facility> entry: facilitiesById.entrySet()){
            facilityMap.put(entry.getValue(),entry.getValue().facilityOutgoingFlow.values().stream().reduce(0.,Double::sum));
        }
        //Compute locations cumulative centrality Map.
        for (Map.Entry<String, Location> entry: locationsById.entrySet()){
            locationMap.put(entry.getValue(),
                    distanceWeight*entry.getValue().locationDistances.values().stream().reduce(0.,Double::sum)+
                    timeWeight*entry.getValue().locationTimes.values().stream().reduce(0.,Double::sum)+
                    costWeight*entry.getValue().locationCosts.values().stream().reduce(0.,Double::sum)
            );
        }
    }


    /**
     * This function returns a map containing {@literal <originNode,<destNode,"heuristicDistance>>"}.
     * Here, "heuristicDistance" represents the potential benefit of assigning a highly demanded node to a centralized place.
     * This is calculated by multiplying the sum of heuristic distances from one node (location) to the others
     * (the smaller the distance, the more central), by the inverse sum of flows from one node (facility) to others
     * (the smaller the flow, the more demanded).
     *
     * @param locationVector HashMap with locations and their "total" outgoing metrics.
     * @param facilityVectors HashMap with facilities and their "total" outgoing flows.
     * @return HashMap {@literal <qapNode, HashMap<qapNode, Double>>} with the computed heuristic "distance."
     */
    private static HashMap<qapNode, HashMap<qapNode, Double>> computeHeuristicInformation(
            HashMap<Facility, Double> facilityVectors,
            HashMap<Location, Double> locationVector) {
        // Heuristic information HashMap.
        HashMap<qapNode, HashMap<qapNode, Double>> heuristicInformation=new HashMap<>();
        //For each facility
        for(Map.Entry<Location,Double> locationEntry: locationVector.entrySet()){
            //For each location
            for(Map.Entry<Facility,Double> facilityEntry: facilityVectors.entrySet()){
                // Going from a facility to a location is assign a facility to a location.
                // This heuristic "distance" is smaller (more attractive for the ant) when a high demanded facility is
                // located in a centralized location.
                heuristicInformation.computeIfAbsent(facilityEntry.getKey(),key ->new HashMap<>()).put(
                        locationEntry.getKey(),locationEntry.getValue()/facilityEntry.getValue()
                );
                // Going from a location to a facility is choosing the order in which we assign the locations.
                // This heuristic information is 1 for all the cases because we want to allow the ants to find the order
                // of assignment by their own. (We could have chosen some metric based on the facility relevance, like
                // the more demanded the facility the more attractive to choose that facility as next).
                heuristicInformation.computeIfAbsent(locationEntry.getKey(), key -> new HashMap<>()).put(
                        facilityEntry.getKey(),1.
                );
            }
        }
        return heuristicInformation;
    }


    /**
     * This function reads inputs and populates the facilities and locations HashMaps.
     *
     * @param inputPath String path from which to read the input files.
     * @param facilitiesById HashMap of facilities by id (to be filled in this function).
     * @param locationsById HashMap of locations by id (to be filled in this function).
     * @throws IOException Exception.
     */
    private static BufferedImage readInputs(String inputPath, HashMap<String, Facility> facilitiesById,
                                   HashMap<String, Location> locationsById) throws IOException {
        // Read facility file.
        InputStream stream = RunCustomQapOptimization.class.getClassLoader().getResourceAsStream(inputPath+"/customFacilities.csv"); //InputStream
        assert stream != null;
        BufferedReader br = new BufferedReader(new InputStreamReader(stream)); //BufferedReader
        br.readLine(); //Skip header
        String line = br.readLine();
        //read and build objects.
        while (line != null) {
            String [] lSplit = line.split(","); //Split line
            for (int i = 0; i < lSplit.length; i++) {
                facilitiesById.put(lSplit[0], new Facility(lSplit[0], lSplit[1], Double.valueOf(lSplit[2]),
                        new HashSet<>(){{addAll(List.of(lSplit[3].split(";")));}}
                ));
            }
            // read next line
            line = br.readLine();
        }
        br.close(); //Close BufferedReader

        // Read facility flows file.
        stream = RunCustomQapOptimization.class.getClassLoader().getResourceAsStream(inputPath+"/customFacilitiesFlows.csv"); //InputStream
        assert stream != null;
        br = new BufferedReader(new InputStreamReader(stream)); //BufferedReader
        ArrayList<String> facilityArray = Arrays.stream(br.readLine().split(",")).collect(Collectors.toCollection(ArrayList::new));
        HashMap<Integer, String> facilitiesByColumn = new HashMap<>();
        IntStream.range(0, facilityArray.size()).forEach(index -> facilitiesByColumn.put(index,facilityArray.get(index)));
        line = br.readLine();
        //read and build objects.
        while (line != null) {
            String [] lSplit = line.split(","); //Split line
            String destFacility = lSplit[0];
            for (int i = 1; i < lSplit.length; i++) {
                try{
                    Double flow = Double.valueOf(lSplit[i]);
                    String origFacility = facilitiesByColumn.get(i);
                    facilitiesById.get(origFacility).facilityOutgoingFlow.put(facilitiesById.get(destFacility),flow);
                }
                catch (NumberFormatException ignored){
                }
            }
            // read next line
            line = br.readLine();
        }
        br.close(); //Close BufferedReader

        // Read Locations file.
        stream = RunCustomQapOptimization.class.getClassLoader().getResourceAsStream(inputPath+"/customLocations.csv"); //InputStream
        assert stream != null;
        br = new BufferedReader(new InputStreamReader(stream)); //BufferedReader
        br.readLine(); //Skip header
        line = br.readLine();
        //read and build objects.
        while (line != null) {
            String [] lSplit = line.split(","); //Split line
            for (int i = 0; i < lSplit.length; i++) {
                try {
                    locationsById.put(lSplit[0], new Location(lSplit[0], Integer.valueOf(lSplit[2]),
                            Integer.valueOf(lSplit[3]), lSplit[1], Double.valueOf(lSplit[4]), lSplit.length==6?lSplit[5]:null));
                }
                catch (ArrayIndexOutOfBoundsException ignored){
                }
            }
            // read next line
            line = br.readLine();
        }
        br.close(); //Close BufferedReader

        // Read location distances file.
        stream = RunCustomQapOptimization.class.getClassLoader().getResourceAsStream(inputPath+"/customLocationsDistances.csv"); //InputStream
        assert stream != null;
        br = new BufferedReader(new InputStreamReader(stream)); //BufferedReader
        ArrayList<String> locationArray = Arrays.stream(br.readLine().split(",")).collect(Collectors.toCollection(ArrayList::new));
        HashMap<Integer, String> locationsByColumn = new HashMap<>();
        IntStream.range(0, locationArray.size()).forEach(index -> locationsByColumn.put(index,locationArray.get(index)));
        line = br.readLine();
        //read and build objects.
        while (line != null) {
            String [] lSplit = line.split(","); //Split line
            String destLocation = lSplit[0];
            for (int i = 1; i < lSplit.length; i++) {
                try{
                    Double dist = Double.valueOf(lSplit[i]);
                    String origLocation = locationsByColumn.get(i);
                    locationsById.get(origLocation).locationDistances.put(locationsById.get(destLocation),dist);
                }
                catch (NumberFormatException ignored){
                }
            }
            // read next line
            line = br.readLine();
        }
        br.close();

        // Read location times file.
        stream = RunCustomQapOptimization.class.getClassLoader().getResourceAsStream(inputPath+"/customLocationsTimes.csv"); //InputStream
        assert stream != null;
        br = new BufferedReader(new InputStreamReader(stream)); //BufferedReader
        ArrayList<String> LocationArray2 = Arrays.stream(br.readLine().split(",")).collect(Collectors.toCollection(ArrayList::new));
        HashMap<Integer, String> locationsByColumn2 = new HashMap<>();
        IntStream.range(0, LocationArray2.size()).forEach(index -> locationsByColumn2.put(index,LocationArray2.get(index)));
        line = br.readLine();
        //read and build objects.
        while (line != null) {
            String [] lSplit = line.split(","); //Split line
            String destLocation = lSplit[0];
            for (int i = 1; i < lSplit.length; i++) {
                try{
                    Double time = Double.valueOf(lSplit[i]);
                    String origLocation = locationsByColumn2.get(i);
                    locationsById.get(origLocation).locationTimes.put(locationsById.get(destLocation),time);
                }
                catch (NumberFormatException ignored){
                }
            }
            // read next line
            line = br.readLine();
        }
        br.close();

        // Read location times file.
        stream = RunCustomQapOptimization.class.getClassLoader().getResourceAsStream(inputPath+"/customLocationsTransportCosts.csv"); //InputStream
        assert stream != null;
        br = new BufferedReader(new InputStreamReader(stream)); //BufferedReader
        ArrayList<String> LocationArray3 = Arrays.stream(br.readLine().split(",")).collect(Collectors.toCollection(ArrayList::new));
        HashMap<Integer, String> locationsByColumn3 = new HashMap<>();
        IntStream.range(0, LocationArray3.size()).forEach(index -> locationsByColumn3.put(index,LocationArray3.get(index)));
        line = br.readLine();
        //read and build objects.
        while (line != null) {
            String [] lSplit = line.split(","); //Split line
            String destLocation = lSplit[0];
            for (int i = 1; i < lSplit.length; i++) {
                try{
                    Double cost = Double.valueOf(lSplit[i]);
                    String origLocation = locationsByColumn3.get(i);
                    locationsById.get(origLocation).locationCosts.put(locationsById.get(destLocation),cost);
                }
                catch (NumberFormatException ignored){
                }
            }
            // read next line
            line = br.readLine();
        }
        br.close();

        stream = RunCustomQapOptimization.class.getClassLoader().getResourceAsStream(inputPath+"/custom_map.png"); //InputStream
        assert stream != null;
        return ImageIO.read(stream);
    }


    /**
     * This class represents a QAP node. It extends the Node class from GraphElements, allowing it to be used in the Graph.
     */
    public static class qapNode extends Node {
        /**
         * Constructor
         */
        private qapNode(String id) {
            super(id);
        }
    }

    /**
     * This class represents a Location.
     */
    public static class Location extends qapNode {
        public final Point2D point;
        public final String description;
        public final Double costToBuyLand;
        public final HashMap<Object,Double> locationDistances;
        public final HashMap<Object,Double> locationTimes;
        public final HashMap<Object,Double> locationCosts;
        public final String restrictionRelation;

        /**
         * Constructor
         *
         * @param id String with the node ID.
         * @param x Integer with the X position of the location.
         * @param y Integer with the y position of the location.
         * @param description String with the description of the Location.
         * @param costToBuyLand Double with the cost to build the location.
         * @param restrictionRelation  String with the restriction relation between this location to the next factory.
         */
        private Location(String id, Integer x, Integer y, String description, Double costToBuyLand,
                         String restrictionRelation) {
            super(id);
            this.point = new Point2D.Double(x,y);
            this.description = description;
            this.costToBuyLand = costToBuyLand;
            this.locationDistances = new HashMap<>();
            this.locationTimes = new HashMap<>();
            this.locationCosts = new HashMap<>();
            this.restrictionRelation = restrictionRelation;
        }
    }

    /**
     * This class represents a Facility.
     */
    public static class Facility extends qapNode {
        public final String description;
        public final Double costToBuild;
        public final HashMap<Object,Double> facilityOutgoingFlow;
        public final HashSet<String> potentialLocationsIDs;

        /**
         * Constructor.
         *
         * @param id String with the node ID.
         * @param description String with the description
         * @param costToBuild Double with the cost to build the facility.
         * @param potentialLocations HashSet with the potential Locations for this factory.
         */
        private Facility(String id, String description, Double costToBuild, HashSet<String> potentialLocations) {
            super(id);
            this.description = description;
            this.costToBuild = costToBuild;
            this.facilityOutgoingFlow = new HashMap<>();
            this.potentialLocationsIDs = potentialLocations;
        }
    }
}
