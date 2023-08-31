package org.soyaga.examples.TSP.DenseMatrices;

import lombok.Getter;
import org.soyaga.aco.Colony;
import org.soyaga.aco.Solution;
import org.soyaga.aco.StatsRetrievalPolicy.Stat.Stat;
import org.soyaga.aco.world.World;
import org.soyaga.examples.TSP.DenseMatrices.RunDenseTspOptimization.*;

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
 * This class implements the Stat interface. The plottable and writable information in this stat contains no data.
 * While executing the stat, an image is generated and stored in an internal variable. Later, the optimizer's
 * getResults() method will collect this array of images and create a GIF.
 */
public class ImageStat implements Stat {
    /**
     * ArrayList&lt;BufferedImage&gt; containing the optimization intermediate steps.
     */
    @Getter
    private final ArrayList<BufferedImage> colonyImages;
    /**
     * HashSet with the nodes and coordinates. Hashmap{@literal <Node,tspNode>}
     */
    private final HashMap<Integer, tspNode> worldMap;
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
     * Constructor.
     * @param worldMap HashSet with the nodes and coordinates.
     * @param scale Integer to scale the problem distances in the render.
     */
    public ImageStat(HashMap<Integer, tspNode> worldMap, Integer scale) {
        this.colonyImages= new ArrayList<>();
        this.worldMap = worldMap;
        this.scale = scale;
        this.minX = (int) this.worldMap.values().stream().min(Comparator.comparingInt(node -> (int) node.getX())).get().getX();
        this.minY = (int) this.worldMap.values().stream().min(Comparator.comparingInt(node -> (int) node.getY())).get().getY();
        Integer maxX = (int) this.worldMap.values().stream().max(Comparator.comparingInt(node -> (int) node.getX())).get().getX();
        Integer maxY = (int) this.worldMap.values().stream().max(Comparator.comparingInt(node -> (int) node.getY())).get().getY();
        Integer width = maxX - this.minX;
        Integer height = maxY - this.minY;
        this.radius = scale/4.;
        this.imageWidth = (int) (width * this.scale + 4 * this.radius);
        this.imageHeight = (int) (height * this.scale + 4 * this.radius);
        creteInitialImages();
        this.baseImage = this.colonyImages.get(this.colonyImages.size()-1);
    }

    /**
     * ArrayList of strings that compose the header for this stat.
     *
     * @return ArrayList{@literal <String>} with the column names.
     */
    @Override
    public ArrayList<String> getHeader() {
        return new ArrayList<>(){{add("ImageSaved");}};
    }

    /**
     * ArrayList of strings that compose the values for this stat.
     *
     * @param world    world used to measure the stats.
     * @param colony   Colony used to measure the stats.
     * @param statArgs VarArgs containing the additional information needed to apply the stat. The generation number in this case.
     * @return ArrayList{@literal <String>} with the column names.
     */
    @Override
    public ArrayList<String> apply(World world, Colony colony, Object... statArgs) {
        this.colonyImages.add((BufferedImage) this.getImage(world, colony, (Integer) statArgs[0]));
        return new ArrayList<>(){{add("true");}};
    }

    /**
     * Function that computes the new image.
     *
     * @param world World used to measure the stats.
     * @param colony Colony used to measure the stats.
     * @param generation Integer with the generation.
     * @return the image of the new solution.
     */
    private Image getImage(World world, Colony colony, Integer generation){
        Solution solution = colony.getBestSolution();
        BufferedImage image = deepCopy(this.baseImage);
        Graphics2D graphics2D = image.createGraphics();
        graphics2D.setColor(Color.WHITE);
        AffineTransform at = AffineTransform.getScaleInstance(1, -1);
        graphics2D.setTransform(at);
        graphics2D.setStroke(new BasicStroke((float) this.imageHeight /75));
        Integer node1ID= (Integer) solution.getNodesVisited().get(0);
        for(Object edge: solution.getEdgesUtilized()) {
            Integer node2ID= (Integer) world.getGraph().getNextNode(edge);
            tspNode node1 = this.worldMap.get(node1ID);
            tspNode node2 = this.worldMap.get(node2ID);
            graphics2D.drawLine(
                    (int) ((node1.getX()-this.minX)*scale+this.radius),
                    (int) ((node1.getY()-this.minY)*scale-imageHeight+this.radius),
                    (int) ((node2.getX()-this.minX)*scale+this.radius),
                    (int) ((node2.getY()-this.minY)*scale-imageHeight+this.radius));
            node1ID=node2ID;

        }
        graphics2D.setColor(Color.RED);
        tspNode node = this.worldMap.get((Integer) solution.getNodesVisited().get(0));
        double nodex= node.getX()-this.minX;
        double nodey= node.getY()-this.minY;
        Shape shape = new Ellipse2D.Double(
                nodex*this.scale,
                nodey*this.scale - imageHeight,
                2*this.radius,
                2*this.radius);
        graphics2D.fill(shape);
        node = this.worldMap.get((Integer) solution.getNodesVisited().get(solution.getNodesVisited().size()-1));
        nodex= node.getX()-this.minX;
        nodey= node.getY()-this.minY;
        shape = new Ellipse2D.Double(
                nodex*this.scale,
                nodey*this.scale - imageHeight,
                2*this.radius,
                2*this.radius);
        graphics2D.fill(shape);
        at = AffineTransform.getScaleInstance(1, 1);
        graphics2D.setTransform(at);
        graphics2D.setColor(Color.GREEN);
        graphics2D.setFont(new Font(graphics2D.getFont().getName(),graphics2D.getFont().getStyle(),this.imageHeight/25));
        graphics2D.drawString("Fitness= "+solution.getFitnessValue() + ", Generation= "+ generation,graphics2D.getFont().getSize(),graphics2D.getFont().getSize());
        graphics2D.dispose();
        return image;
    }

    /**
     * Function that creates and return a copy of a buffered image.
     * @param bi BufferedImage to copy.
     * @return BufferedImage with a deep copy.
     */
    static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    /**
     * Function that stores the first images:
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
        graphics2D.setFont(new Font("",Font.BOLD,20));
        for(tspNode node: this.worldMap.values()){
            AffineTransform at = AffineTransform.getScaleInstance(1, -1);
            graphics2D.setTransform(at);
            Double nodex= node.getX()-this.minX;
            Double nodey= node.getY()-this.minY;
            Shape shape = new Ellipse2D.Double(
                    nodex*this.scale,
                    nodey*this.scale - imageHeight,
                    2*this.radius,
                    2*this.radius);
            graphics2D.fill(shape);
            at = new AffineTransform();
            graphics2D.setTransform(at);
            graphics2D.drawString(node.getID().toString(),
                    (int) (nodex*this.scale+2*this.radius),
                    (int) (-nodey*this.scale + this.imageHeight - 2*this.radius)
            );
        }
        graphics2D.dispose();
        for(int i=0;i<2;i++) this.colonyImages.add(image);

        BufferedImage image2 = deepCopy(image);
        graphics2D = image2.createGraphics();
        graphics2D.setColor(Color.LIGHT_GRAY);
        AffineTransform at = AffineTransform.getScaleInstance(1, -1);
        graphics2D.setTransform(at);
        Stroke dashed = new BasicStroke((float) this.imageHeight /3000, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                0, new float[]{(float) this.imageHeight /30}, 0);
        graphics2D.setStroke(dashed);
        for(tspNode node1: this.worldMap.values()){
            for (Integer node2Id:node1.getRelatedNodes()){
                tspNode node2 = this.worldMap.get(node2Id);
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
}
