package org.soyaga.examples.TSP.DenseMatrices;

import lombok.Getter;
import org.soyaga.Initializer.ACOInitializer;
import org.soyaga.aco.Ant.EdgeSelector.RandomProportionalEdgeSelector;
import org.soyaga.aco.Ant.SimpleAnt;
import org.soyaga.aco.Colony;
import org.soyaga.aco.Solution.AllNodesCircleSolution;
import org.soyaga.aco.SolutionConstructorPolicy.SimpleConstructorPolicy;
import org.soyaga.aco.SolutionEvaluatorPolicy.SimpleSolutionEvaluatorPolicy;
import org.soyaga.aco.SolutionEvaluatorPolicy.SolutionEvaluator.PathDistanceSolutionEvaluator;
import org.soyaga.aco.StopingCriteriaPolicy.MaxIterationCriteriaPolicy;
import org.soyaga.aco.UpdatePheromonePolicy.AddPheromonePolicy.SolFitnessProportionalAddPheromonePolicy;
import org.soyaga.aco.UpdatePheromonePolicy.EvaporatePheromonePolicy.PercentageEvaporatePheromonePolicy;
import org.soyaga.aco.UpdatePheromonePolicy.SimpleUpdatePheromonePolicy;
import org.soyaga.aco.world.GenericWorld;
import org.soyaga.aco.world.Graph.DenseMatrixGraph;
import org.soyaga.aco.world.PheromoneContainer.DenseMatrixPheromoneContainer;
import org.soyaga.aco.world.World;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * Instantiates and optimize a DenseTspAntColonyAlgorithm Object. Fills it with all the ACO classes needed to
 * perform the optimization. Optimizes given the previous configuration and creates GIFs/Images of the results.
 */
public class RunDenseTspOptimization {
    public static void main(String ... args) throws IOException {
        //Integer with the number of ants.
        Integer antNumber =6;
        //Integer with the maximum number of edges an ants solution can have.
        Integer maxEdges = 50;
        //Double with the penalization added to a solution when the build solution is not a real solution.
        Double penalization = 1000.;
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
        //String with the path to the image we want to recreate, without the .png/.jpg, it is added by default.
        String inputPath = "TSP/hexagonPath.csv";
        //String with the path to the image we want to recreate, without the .png/.jpg, it is added by default.
        String outputPath = "src/out/TSP/image";
        //Integer with the scale to draw the image.
        Integer scale = 30;
        //Entry with the world information.
        AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<HashMap<Integer, tspNode>,HashMap<Integer, tspNode>>,
                GenericWorld<Integer,Integer>> entryWorld = createWorld(
                inputPath,
                initialPheromone);
        //GenericWorld Object (World Instance).
        World<Integer,Integer> world = entryWorld.getValue();
        //HashMap with the mapping from nodeId (Integer) to node (qapNode).
        HashMap<Integer,tspNode> worldMap = entryWorld.getKey().getKey();
        //HashMap with the mapping from nodeId (Integer) to node (qapNode).
        HashMap<Integer,tspNode> worldMapOutput = entryWorld.getKey().getValue();
        //AntColonyAlgorithm instance.
        DenseTspAntColonyAlgorithm aco = new DenseTspAntColonyAlgorithm(
                "DenseTSP",                                             //ID
                world,                                                      //GenericWorld
                new Colony(),                                               //Colony
                antNumber,                                                  //Integer
                new SimpleAnt(                                              //Ant
                        new AllNodesCircleSolution(                             //SolutionType
                                world.getGraph(),                                   //Worlds
                                maxEdges,                                           //Integer
                                penalization),                                      //Double
                        new RandomProportionalEdgeSelector(                     //EdgeSelector
                                alpha,                                              //Double
                                beta)),                                             //Double
                new MaxIterationCriteriaPolicy(                             //StoppingCriteriaPolicy
                        maxIterations),                                         //Integer
                new ACOInitializer(),                                       //Initializer
                new SimpleConstructorPolicy(),                              //ConstructorPolicy
                new SimpleSolutionEvaluatorPolicy(                          //SolutionEvaluatorPolicy
                        new PathDistanceSolutionEvaluator()),                   //SolutionEvaluator
                new SimpleUpdatePheromonePolicy(                            //UpdatePheromonePolicy
                        new SolFitnessProportionalAddPheromonePolicy<>(),       //AddPheromonePolicy
                        new PercentageEvaporatePheromonePolicy<>(               //EvaporatePheromonePolicy
                                persistence)),                                      //Double
                worldMap,                                                   //HashSet
                worldMapOutput,                                             //HashSet
                scale                                                       //Integer
        );
        aco.optimize();
        //Plot the results
        GifCreator.createGif(aco.getColonyImages(),outputPath+".gif",100);
        ImageIO.write((BufferedImage)aco.getResult(), "png",  new File(outputPath+".png"));
    }

    /**
     * Function that creates a World object along with map of tspNodes.
     * @param path String with the filename path, where the problem information is contained.
     * @return AbstractMap.SimpleEntry{@literal <HashMap<Integer, qapNode>,GenericWorld<Integer,Integer>>}
     */
    private static AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<HashMap<Integer, tspNode>,HashMap<Integer, tspNode>>,
            GenericWorld<Integer,Integer>> createWorld(
            String path, Double initialPheromone) throws IOException {
        InputStream stream = RunDenseTspOptimization.class.getClassLoader().getResourceAsStream(path); //InputStream
        assert stream != null;
        BufferedReader br = new BufferedReader(new InputStreamReader(stream)); //BufferedReader
        HashMap<Integer, tspNode> mapNodes = new HashMap<>(); //Hashmap<nodeId,qapNode>
        HashMap<Integer, tspNode> mapNodesOutput = new HashMap<>(); //Hashmap<nodeId,qapNode>
        HashMap<Integer, Integer> mapColNumbToNodeId = new HashMap<>(); //Hashmap<colNum,nodeId>
        HashMap<Integer, Integer> mapNodeIdToColNumb = new HashMap<>(); //Hashmap<nodeId,colNum>
        //read and build objects.
        br.lines().skip(1).forEach(l->{
            String [] lSplit = l.split(","); //Split line
            tspNode node = new tspNode(lSplit[0],lSplit[1],lSplit[2],lSplit[3]); //create qapNode
            mapNodes.put(node.getID(), node); //add node to graph
        });
        br.close(); //Close BufferedReader
        Integer colNumber=0; //Integer
        DenseMatrixGraph graph=new DenseMatrixGraph(); //CreateGraph
        for (tspNode node:mapNodes.values()){
            mapColNumbToNodeId.put(colNumber, node.getID());
            mapNodeIdToColNumb.put(node.getID(), colNumber);
            mapNodesOutput.put(colNumber,node);
            graph.addNode(); //Fill with nodes
            colNumber+=1;
        }
        mapNodes.values().forEach(//Fill with edges
                node1 -> node1.getRelatedNodes().forEach(
                        node2Id -> graph.setNextDistance(//InitializeEdges
                                mapNodeIdToColNumb.get(node1.getID()), //Integer with node1ID
                                mapNodeIdToColNumb.get(node2Id), //Integer with node2ID
                                tspNode.computeEuclideanDistance( //Euclidean distance
                                        node1,  //tspNode1
                                        mapNodes.get(node2Id) //tspNode2
                                )
                        )
                )
        );
        DenseMatrixPheromoneContainer pheromoneMatrix = new DenseMatrixPheromoneContainer(graph.getNodes().size(),initialPheromone);
        return new AbstractMap.SimpleEntry<>(new AbstractMap.SimpleEntry<>(mapNodes,mapNodesOutput),
                new GenericWorld<>(graph,pheromoneMatrix));
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
