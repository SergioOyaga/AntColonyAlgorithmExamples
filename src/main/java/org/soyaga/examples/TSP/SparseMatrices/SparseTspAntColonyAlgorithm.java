package org.soyaga.examples.TSP.SparseMatrices;

import lombok.Getter;
import org.soyaga.Initializer.ACOInitializer;
import org.soyaga.aco.Ant.Ant;
import org.soyaga.aco.AntColonyAlgorithm.AntColonyAlgorithm;
import org.soyaga.aco.Colony;
import org.soyaga.aco.Solution.Solution;
import org.soyaga.aco.SolutionConstructorPolicy.SolutionConstructorPolicy;
import org.soyaga.aco.StopingCriteriaPolicy.StoppingCriteriaPolicy;
import org.soyaga.aco.UpdatePheromonePolicy.UpdatePheromonePolicy;
import org.soyaga.aco.world.World;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Extends AntColonyAlgorithm and defines how we perform the optimization cycles and how we gather the results.
 */
public class SparseTspAntColonyAlgorithm implements AntColonyAlgorithm {
    //Attributes for the ACO
    /**
     * String with the name of the ACO.
     */
    private String ID;
    /**
     * World with the Graph and Pheromone container.
     */
    @Getter
    private World world;
    /**
     * Colony with the Ants.
     */
    @Getter
    private Colony colony;
    /**
     * Integer with the number of ants in the colony.
     */
    @Getter
    private Integer initialColonySize;
    /**
     * Ant instance with the type of ant.
     */
    @Getter
    private Ant ant;
    /**
     * StoppingCriteriaPolicy object of the ACO.
     */
    private StoppingCriteriaPolicy stoppingCriteriaPolicy;
    /**
     * ACOInitializer Object used to initialize the Ants that are going to be used in the optimization.
     */
    private ACOInitializer acoInitializer;
    /**
     * SolutionConstructorPolicy Object used to build the solutions in each iteration.
     */
    private SolutionConstructorPolicy solutionConstructorPolicy;
    /**
     * UpdatePheromonePolicy Object used to update the pheromone in the World.
     */
    private UpdatePheromonePolicy updatePheromonePolicy;

    //Attributes for the render.
    /**
     * HashSet with the nodes and coordinates.
     */
    HashMap<Integer, RunSparseTspOptimization.tspNode> worldMap;
    /**
     * ArrayList&lt;BufferedImage&gt; containing the optimization intermediate steps.
     */
    @Getter
    private ArrayList<BufferedImage> colonyImages;
    /**
     * Integer with the current iteration number. Needed as class parameter because in the getResults method is used to
     * plot some useful information.
     */
    private Integer generation;
    /**
     * Integer to scale the problem distances in the render.
     */
    private final Integer scale;
    /**
     * Integer with the min x present in the problem.
     */
    private final Integer minX;
    /**
     * Integer with the min y present in the problem.
     */
    private final Integer minY;
    /**
     * Integer with the width of the image;
     */
    private final Integer width;
    /**
     * Integer with the height of the image;
     */
    private final Integer height;
    /**
     * Double with the node radius.
     */
    private final double radius;
    /**
     * BufferedImage with the base image. It is used to draw on top of it the best solution edges.
     */
    private final BufferedImage baseImage;
    /**
     * Integer with the image width.
     */
    private final Integer imageWidth;
    /**
     * Integer with the image height.
     */
    private final Integer imageHeight;


    /**
     * It receives all parameters needed to create an object of this class.
     * @param ID String with the name of the ACO.
     * @param stoppingCriteriaPolicy  StoppingCriteriaPolicy object with the criteria already defined.
     * @param acoInitializer  ACOInitializer object with the initializer defined.
     */
    public SparseTspAntColonyAlgorithm(String ID, World world, Colony colony, Integer initialColonySize, Ant ant,
                                       StoppingCriteriaPolicy stoppingCriteriaPolicy, ACOInitializer acoInitializer,
                                       SolutionConstructorPolicy solutionConstructorPolicy,
                                       UpdatePheromonePolicy updatePheromonePolicy,
                                       HashMap<Integer, RunSparseTspOptimization.tspNode> worldMap,Integer scale) {
        super();
        this.ID = ID;
        this.world = world;
        this.colony = colony;
        this.initialColonySize = initialColonySize;
        this.ant = ant;
        this.stoppingCriteriaPolicy = stoppingCriteriaPolicy;
        this.acoInitializer = acoInitializer;
        this.solutionConstructorPolicy = solutionConstructorPolicy;
        this.updatePheromonePolicy = updatePheromonePolicy;
        this.worldMap = worldMap;
        this.colonyImages= new ArrayList<>();
        this.scale = scale;
        this.minX = (int) this.worldMap.values().stream().min(Comparator.comparingInt(node -> (int) node.getX())).get().getX();
        this.minY = (int) this.worldMap.values().stream().min(Comparator.comparingInt(node -> (int) node.getY())).get().getY();
        Integer maxX = (int) this.worldMap.values().stream().max(Comparator.comparingInt(node -> (int) node.getX())).get().getX();
        Integer maxY = (int) this.worldMap.values().stream().max(Comparator.comparingInt(node -> (int) node.getY())).get().getY();
        this.width = maxX-this.minX;
        this.height = maxY-this.minY;
        this.radius = scale/4.;
        this.imageWidth = (int) (this.width * this.scale + 4 * this.radius);
        this.imageHeight = (int) (this.height * this.scale + 4 * this.radius);
        creteInitialImages();
        this.baseImage = this.colonyImages.get(this.colonyImages.size()-1);
    }

    /**
     * Function that performs the typical optimization for a genetic algorithm.
     */
    public void optimize(){
        this.generation=0;
        this.acoInitializer.initialize(this);
        while (this.stoppingCriteriaPolicy.hasToContinue(this.generation)){
            this.solutionConstructorPolicy.apply(this.getWorld(), this.getColony());
            this.getColony().computeBestSolution();
            this.colonyImages.add((BufferedImage) this.getResult());
            System.out.println(this.generation+"\t->\t"+this.getColony().getBestSolution().getFitness());
            this.updatePheromonePolicy.apply(this.getWorld(), this.getColony());
            this.generation++;
        }
    }


    /**
     * Method that returns from an optimized solution the actual result in the
     * form that is convenient for the problem.
     *
     * @return Object containing the result of the optimization. In theis case an image with the Path.
     */
    @Override
    public Object getResult() {
        Solution solution = this.getColony().getBestSolution();
        BufferedImage image = deepCopy(this.baseImage);
        Graphics2D graphics2D = image.createGraphics();
        graphics2D.setColor(Color.WHITE);
        AffineTransform at = AffineTransform.getScaleInstance(1, -1);
        graphics2D.setTransform(at);
        graphics2D.setStroke(new BasicStroke(this.imageHeight/75));
        Integer node1ID= (Integer) solution.getStartNode();
        for(Object edge: solution.getPath()) {
            Integer node2ID= (Integer) this.world.getGraph().getNextNode(edge);
            RunSparseTspOptimization.tspNode node1 = this.worldMap.get(node1ID);
            RunSparseTspOptimization.tspNode node2 = this.worldMap.get(node2ID);
            graphics2D.drawLine(
                    (int) ((node1.getX()-this.minX)*scale+this.radius),
                    (int) ((node1.getY()-this.minY)*scale-imageHeight+this.radius),
                    (int) ((node2.getX()-this.minX)*scale+this.radius),
                    (int) ((node2.getY()-this.minY)*scale-imageHeight+this.radius));
            node1ID=node2ID;
        }
        at = AffineTransform.getScaleInstance(1, 1);
        graphics2D.setTransform(at);
        graphics2D.setColor(Color.GREEN);
        graphics2D.setFont(new Font(graphics2D.getFont().getName(),graphics2D.getFont().getStyle(),this.imageHeight/50));
        graphics2D.drawString("Fitness= "+solution.getFitness() + ", Generation= "+ this.generation,graphics2D.getFont().getSize(),graphics2D.getFont().getSize());
        graphics2D.dispose();
        return image;
    }

    /**
     * Function that stores a first images:
     * <ol>
     *     <li>Only nodes * 2 times.</li>
     *     <li>(Nodes + all edges) * 5 times.</li>
     * </ol>
     */
    private void creteInitialImages() {
        BufferedImage image = new BufferedImage(this.imageWidth, this.imageHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = image.createGraphics();
        graphics2D.setBackground(Color.DARK_GRAY);
        graphics2D.clearRect(0, 0, imageWidth, imageHeight);
        graphics2D.setColor(Color.GREEN);
        AffineTransform at = AffineTransform.getScaleInstance(1, -1);
        graphics2D.setTransform(at);
        for(RunSparseTspOptimization.tspNode node: this.worldMap.values()){
            Double nodex= node.getX()-this.minX;
            Double nodey= node.getY()-this.minY;
            Shape shape = new Ellipse2D.Double(
                    nodex*this.scale,
                    nodey*this.scale - imageHeight,
                    2*this.radius,
                    2*this.radius);
            graphics2D.fill(shape);
        }
        graphics2D.dispose();
        for(int i=0;i<2;i++) this.colonyImages.add(image);

        BufferedImage image2 = deepCopy(image);
        graphics2D = image2.createGraphics();
        graphics2D.setColor(Color.LIGHT_GRAY);
        graphics2D.setTransform(at);
        Stroke dashed = new BasicStroke(this.imageHeight/3000, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                0, new float[]{this.imageHeight/30}, 0);
        graphics2D.setStroke(dashed);
        for(RunSparseTspOptimization.tspNode node1: this.worldMap.values()){
            for (Integer node2Id:node1.getRelatedNodes()){
                RunSparseTspOptimization.tspNode node2 = this.worldMap.get(node2Id);
                graphics2D.drawLine(
                        (int) ((node1.getX()-this.minX)*scale+this.radius),
                        (int) ((node1.getY()-this.minY)*scale-imageHeight+this.radius),
                        (int) ((node2.getX()-this.minX)*scale+this.radius),
                        (int) ((node2.getY()-this.minY)*scale-imageHeight+this.radius));
            }
        }
        graphics2D.dispose();
        for(int i=0;i<5;i++) this.colonyImages.add(image2);
    }

    /**
     * Functiont that creates and return a copy of a buffered image.
     * @param bi BufferedImage to copy.
     * @return BufferedImage with a deep copy.
     */
    static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
}
