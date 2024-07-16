package org.soyaga.examples.TSP.SparseMatrices;

import lombok.Getter;
import org.soyaga.Initializer.ACOInitializer;
import org.soyaga.aco.Ant.EdgeSelector.RandomProportionalEdgeSelector;
import org.soyaga.aco.Ant.SimpleMemoryAnt;
import org.soyaga.aco.BuilderEvaluator.AllNodesLineBuilderEvaluator;
import org.soyaga.aco.Colony;
import org.soyaga.aco.Evaluable.Objective.PathDistanceObjectiveFunction;
import org.soyaga.aco.Solution;
import org.soyaga.aco.SolutionConstructorPolicy.SimpleConstructorPolicy;
import org.soyaga.aco.StatsRetrievalPolicy.NIterationsStatsRetrievalPolicy;
import org.soyaga.aco.StatsRetrievalPolicy.Stat.CurrentMinFitnessStat;
import org.soyaga.aco.StatsRetrievalPolicy.Stat.MeanSdFitnessStat;
import org.soyaga.aco.StoppingPolicy.MaxIterationCriteriaPolicy;
import org.soyaga.aco.UpdatePheromonePolicy.AddPheromonePolicy.SolFitnessProportionalAddPheromonePolicy;
import org.soyaga.aco.UpdatePheromonePolicy.EvaporatePheromonePolicy.PercentageEvaporatePheromonePolicy;
import org.soyaga.aco.UpdatePheromonePolicy.SimpleUpdatePheromonePolicy;
import org.soyaga.aco.world.GenericWorld;
import org.soyaga.aco.world.Graph.SparseMatrixGraph;
import org.soyaga.aco.world.PheromoneContainer.SparseMatrixPheromoneContainer;
import org.soyaga.aco.world.World;

import java.io.*;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;


/**
 * This class instantiates and optimizes a SparseTspAntColonyAlgorithm object. It populates the object with all
 * the necessary ACO classes required to perform the optimization. The optimization is carried out based on the
 * provided configuration, and GIFs/images of the results are generated.
 */
public class RunSparseTspOptimization {
    public static void main(String ... args) throws IOException {
        //Integer with the number of ants. Approximately the number of nodes in the graph.
        Integer antNumber = 30;
        //Integer with the maximum number of edges an ant solution can have. This number has to ensure the
        // solution can be found.
        Integer maxEdges = 50;
        //Integer with the start node.
        Integer startNode = 1;
        //Integer with the end node.
        Integer endNode = 32;
        //Double with the ants Alpha (>0) parameter (importance of the edges pheromones against the edges distances).
        Double alpha = 1.;
        //Double with the ants Beta (>0) parameter (importance of the edges distances against the edges pheromones).
        Double beta = 2.;
        //Integer with the maximum number of iterations for the optimization to run.
        Integer maxIterations = 50;
        //Double with pheromone persistence in the edge.
        Double persistence = .9;
        //Double with initial pheromone present in the edge.
        Double initialPheromone = 0.;
        //Double with the amount of pheromone each ant can deposit in its track.
        Double antPheromoneQuantity=10.;
        //String with the path to the image we want to recreate, without the .png/.jpg, it is added by default.
        String inputPath = "TSP/colonyPath.csv";
        //String with the path to the image we want to recreate, without the .png/.jpg, it is added by default.
        String outputPath = "src/out/TSP/image";
        //Integer with the scale to draw the image.
        Integer scale = 30;
        //Entry with the world information. Entry<HashMap<Node,tspNode>,World>
        AbstractMap.SimpleEntry<HashMap<Integer, tspNode>, GenericWorld> entryWorld = createWorld(
                inputPath,
                initialPheromone);
        //GenericWorld Object (World Instance). Contains a SparseMatrixPheromoneContainer and a SparseMatrixGraph.
        World world = entryWorld.getValue();
        //HashMap with the mapping from node (Integer) to real-node (tspNode).
        HashMap<Integer, tspNode> worldMap = entryWorld.getKey();
        //AntColonyAlgorithm instance.
        SparseTspAntColonyAlgorithm aco = new SparseTspAntColonyAlgorithm(
                "SparseTSP",                                        //ID
                world,                                                  //World, GenericWorld (sparse components).
                new Colony(),                                           //Colony.
                new MaxIterationCriteriaPolicy(                         //StoppingCriteriaPolicy, max iterations:
                        maxIterations),                                     //Integer, max iterations.
                new ACOInitializer(                                     //Initializer:
                        new SimpleMemoryAnt(                                //Ant, SimpleMemoryAnt:
                                new Solution(                                   //Solution:
                                        new PathDistanceObjectiveFunction(),        //Objective function, path distance.
                                        null,                                       //Constraints function, none.
                                        null,                                       //Double, penalization, none.
                                        maxEdges,                                   //Integer, max number of edges.
                                        new AllNodesLineBuilderEvaluator(           //Builder evaluator, line, all nodes:
                                                startNode,                              //Integer, start node.
                                                endNode,                                //Integer, end node.
                                                new ArrayList<>(world.getGraph().getNodes())    //Nodes to visit.
                                        )
                                ),
                                new RandomProportionalEdgeSelector(             //Edge Selector, random-proportional:
                                        alpha,                                      //Double, pheromone importance.
                                        beta                                        //Double, distance importance.
                                ),
                                antPheromoneQuantity                            //Double, pheromone quantity.
                        ),
                        antNumber                                           //Integer, number of ants to initialize.
                ),
                new SimpleConstructorPolicy(),                          //ConstructorPolicy, ant by ant.
                new SimpleUpdatePheromonePolicy(                        //UpdatePheromonePolicy, first evaporate then add:
                        new SolFitnessProportionalAddPheromonePolicy(),     //AddPheromonePolicy, prop to sol fitness.
                        new PercentageEvaporatePheromonePolicy(             //EvaporatePheromonePolicy, percentage persistence:
                                persistence)                                    //Double, persistence.
                ),
                new NIterationsStatsRetrievalPolicy(                    //Stats retrieval policy, every n iterations:
                        1,                                                  //Integer, steps between measures.
                        new ArrayList<>(){{                                 //ArrayList, with the stats:
                            add(new ImageStat(                                  //Stat, create and store images.
                                    worldMap,                                       //HashSet, map Node to tspNode
                                    scale                                           //Double, image scale.
                            ));
                            add(new CurrentMinFitnessStat(                      //Stat, current mean fitness.
                                    4
                            ));
                            add(new MeanSdFitnessStat(                          //Stat, colony mean and std fitness.
                                    4
                            ));
                        }},
                        outputPath,                                         //String, with the output path.
                        true,                                               //Boolean, print in console.
                        false                                               //Boolean, store in csv file.
                )
        );
        aco.optimize();
        //Plot the results
        aco.getResult(outputPath);
    }

    /**
     * Function that creates a World object along with a map of tspNodes.
     *
     * @param path String with the filename path, CSV file where the problem information is contained.
     * @return AbstractMap.SimpleEntry{@literal <HashMap<Integer, qapNode>,GenericWorld>}
     */
    private static AbstractMap.SimpleEntry<HashMap<Integer, tspNode>, GenericWorld> createWorld(
            String path, Double initialPheromone) throws IOException {
        InputStream stream = RunSparseTspOptimization.class.getClassLoader().getResourceAsStream(path); //InputStream
        assert stream != null;
        BufferedReader br = new BufferedReader(new InputStreamReader(stream)); //BufferedReader
        HashMap<Integer, tspNode> mapNodes = new HashMap<>(); //Hashmap<nodeId,qapNode>
        //read and build objects.
        br.lines().skip(1).forEach(l->{
            String [] lSplit = l.split(","); //Split line
            tspNode node = new tspNode(lSplit[0],lSplit[1],lSplit[2],lSplit[3]); //create qapNode
            mapNodes.put(node.getID(), node); //add node to graph
        });
        br.close(); //Close BufferedReader
        SparseMatrixGraph graph=new SparseMatrixGraph(); //CreateGraph
        mapNodes.values().forEach(//Fill with edges
                node1 -> node1.getRelatedNodes().forEach(
                        node2Id -> graph.setNextDistance(//InitializeEdges
                                node1.getID(), //Integer with node1ID
                                node2Id, //Integer with node2ID
                                tspNode.computeEuclideanDistance( //Euclidean distance
                                        node1,  //tspNode1
                                        mapNodes.get(node2Id) //tspNode2
                                )
                        )
                )
        );
        SparseMatrixPheromoneContainer pheromoneMatrix= new SparseMatrixPheromoneContainer(initialPheromone);
        return new AbstractMap.SimpleEntry<>(mapNodes, new GenericWorld(graph,pheromoneMatrix));
    }

    /**
     * Class that represents a TSP node. Contains:
     * <ul>
     *     <li> Integer with a node ID.</li>
     *     <li> double with x position.</li>
     *     <li> double with y position.</li>
     *     <li> ArrayList{@literal <Integer>} with the IDs of connected nodes.</li>
     * </ul>
     */
    @Getter
    public static class tspNode{
        /**
         * Integer with node ID.
         */
        private final Integer ID;
        /**
         * Double with x position.
         */
        private final double x;
        /**
         * Double with y position.
         */
        private final double y;
        /**
         * ArrayList{@literal <Integer>} with the IDs of connected nodes.
         */
        private final ArrayList<Integer> relatedNodes;

        /**
         * Constructor
         *
         * @param id String with the node ID assumes it is an Integer.
         * @param x String with the node x position assumes it is a double.
         * @param y String with the node y position assumes it is a double.
         * @param nodesString String with the related nodes assumes something of the form "1;2;3..."
         */
        private tspNode(String id, String x, String y, String nodesString) {
            this.ID = Integer.valueOf(id);
            this.x = Double.parseDouble(x);
            this.y = Double.parseDouble(y);
            this.relatedNodes = Arrays.stream(nodesString.split(";")).map(Integer::valueOf).collect(Collectors.toCollection(ArrayList::new));
        }

        /**
         * Function that computes the Euclidean distance of teo tspNodes
         * @param node1 tspNode1.
         * @param node2 tspNode2.
         * @return Double with the Euclidean distance.
         */
        private static Double computeEuclideanDistance(tspNode node1, tspNode node2){
            return Math.sqrt((node1.x - node2.x)*(node1.x - node2.x) + (node1.y - node2.y)*(node1.y - node2.y));
        }
    }
}
