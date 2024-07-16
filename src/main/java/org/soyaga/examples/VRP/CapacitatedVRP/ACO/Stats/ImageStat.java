package org.soyaga.examples.VRP.CapacitatedVRP.ACO.Stats;

import lombok.Getter;
import org.soyaga.aco.Colony;
import org.soyaga.aco.Solution;
import org.soyaga.aco.StatsRetrievalPolicy.Stat.Stat;
import org.soyaga.aco.world.Graph.Graph;
import org.soyaga.aco.world.PheromoneContainer.PheromoneContainer;
import org.soyaga.aco.world.World;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.Graph.Nodes.CDCNode;
import org.soyaga.examples.VRP.CapacitatedVRP.ACO.Graph.Nodes.WaypointNode;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * This class implements the Stat interface. The plottable and writable information in this stat contains no data.
 * While executing the stat, an image is generated and updated in the front.
 */
public class ImageStat implements Stat {
    /**
     * BufferedImage; containing the optimization image.
     */
    @Getter
    private BufferedImage colonyImage;
    /**
     * Double with the width of the objective image.
     */
    private final Double width;
    /**
     * Double with the height of the objective image.
     */
    private final Double height;
    /**
     * Double with the pheromoneIntensity to apply to the edges.
     */
    private final Double pheromoneIntensity;
    /**
     * Double with the node radius.
     */
    private final double radius;
    /**
     * PropertyChangeSupport object to fire events when properties of this class change.
     */
    private final PropertyChangeSupport pcs;

    /**
     * Constructor.
     *
     * @param width Double with the width of the objective image.
     * @param height Double with the height of the objective image.
     * @param pheromoneIntensity Double with the intensity to apply to the pheromone in the image.
     */
    public ImageStat(Double width, Double height, Double pheromoneIntensity) {
        this.width = width;
        this.height = height;
        this.pheromoneIntensity = pheromoneIntensity;
        this.radius = (width+height)/200.;
        this.pcs = new PropertyChangeSupport(this);
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
     * @param world    The World used to measure the stats.
     * @param colony   Colony used to measure the stats.
     * @param statArgs VarArgs containing the additional information needed to apply the stat. The generation number in this case.
     * @return ArrayList{@literal <String>} with the column names.
     */
    @Override
    public ArrayList<String> apply(World world, Colony colony, Object... statArgs) {
        BufferedImage oldImage = this.colonyImage;
        this.colonyImage = this.getImage(world, colony, (Integer) statArgs[0]);
        this.pcs.firePropertyChange("colonyImage", oldImage, this.colonyImage);
        return new ArrayList<>(){{add("true");}};
    }

    /**
     * Function that computes the new image.
     *
     * @param world The World used to measure the stats.
     * @param colony Colony used to measure the stats.
     * @param generation Integer with the generation.
     * @return the image of the new solution.
     */
    private BufferedImage getImage(World world, Colony colony, Integer generation){
        Solution solution = colony.getBestSolution();
        ArrayList<Point2D> cdcs = new ArrayList<>();
        ArrayList<Point2D> waypoints = new ArrayList<>();
        ArrayList<Point2D> joined = new ArrayList<>();
        getPoints(world, cdcs, waypoints, joined);
        if(joined.size()==0) return null;
        double xMin = joined.stream().min(Comparator.comparingDouble(Point2D::getX)).get().getX();
        double xMax = joined.stream().max(Comparator.comparingDouble(Point2D::getX)).get().getX();
        double yMin = joined.stream().min(Comparator.comparingDouble(Point2D::getY)).get().getY();
        double yMax = joined.stream().max(Comparator.comparingDouble(Point2D::getY)).get().getY();
        double xRatio = this.width/(xMax-xMin);
        double yRatio = this.height/(yMax-yMin);
        double ratio = Math.min(xRatio,yRatio);

        BufferedImage image = new BufferedImage((int) (this.width+this.radius*4), (int) (this.height+this.radius*4), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = image.createGraphics();


        drawBackground(graphics2D,image);
        graphics2D.setTransform(new AffineTransform(1.,0.,0.,-1.,0.,image.getHeight()));
        drawLines(graphics2D, world, xMin, yMin, ratio);
        drawCDCsAndWaypoints(graphics2D, cdcs, waypoints, xMin, yMin, ratio);
        drawInfo(graphics2D, solution, generation);

        graphics2D.dispose();
        return image;
    }

    /**
     * Function that draws a black background to the image
     * @param graphics2D Graphics2D to draw.
     * @param image BufferedImage with for the real size.
     */
    private void drawBackground(Graphics2D graphics2D, BufferedImage image) {
        graphics2D.setBackground(Color.BLACK);
        graphics2D.clearRect(0, 0, image.getWidth(), image.getHeight());
    }

    /**
     * Function that gets the points of the relevant nodes in the solution.
     * @param world World with the graph.
     * @param cdcs ArrayList<Point2D> of CDCs.
     * @param waypoints ArrayList<Point2D> of toVisitWaypoints
     * @param joined ArrayList<Point2D with the CDCs and the to visit waypoints.
     */
    private void getPoints(World world, ArrayList<Point2D> cdcs, ArrayList<Point2D> waypoints, ArrayList<Point2D> joined) {
        for(Object node: world.getGraph().getNodes()){
            if(node instanceof CDCNode cdcNode){
                cdcs.add(cdcNode.getPoint());
                joined.add(cdcNode.getPoint());
            } else if (node instanceof  WaypointNode waypointNode) {
                waypoints.add(waypointNode.getPoint());
                joined.add(waypointNode.getPoint());
            }
        }
    }

    /**
     * Function that draws the lines in the image.
     * @param graphics2D Graphics2D where to draw the lines.
     * @param world World with the nodes.
     * @param xMin min X position in the points.
     * @param yMin min y position in the points
     * @param ratio ratio to scalate the points' range to the image size.
     */
    private void drawLines(Graphics2D graphics2D, World world, double xMin, double yMin, double ratio) {
        PheromoneContainer pheromoneContainer = world.getPheromoneContainer();
        Graph graph = world.getGraph();
        for(Object origin: graph.getNodes()){
            if(origin instanceof CDCNode cdcNodeOrigin){
                double originX = (cdcNodeOrigin.getPoint().getX() - xMin) * ratio +this.radius*2;
                double originY = (cdcNodeOrigin.getPoint().getY() - yMin) * ratio +this.radius*2;
                for(Object edge:graph.getOutputEdges(cdcNodeOrigin)){
                    if(graph.getNextNode(edge) instanceof CDCNode cdcNodeDest){
                        double destX = (cdcNodeDest.getPoint().getX() - xMin) * ratio +this.radius*2;
                        double destY = (cdcNodeDest.getPoint().getY() - yMin) * ratio +this.radius*2;
                        graphics2D.setStroke(
                                new BasicStroke(
                                        (float) (this.radius*Math.tanh(this.pheromoneIntensity*pheromoneContainer.getNextPheromone(cdcNodeOrigin, edge)))
                                )
                        );
                        graphics2D.setColor(
                                new Color(
                                        255,255,255,
                                        (int) (255*Math.tanh(this.pheromoneIntensity*pheromoneContainer.getNextPheromone(cdcNodeOrigin, edge)))
                                ));
                        graphics2D.drawLine(
                                (int) (originX),
                                (int) (originY),
                                (int) (destX),
                                (int) (destY));
                    }
                    else if(graph.getNextNode(edge) instanceof WaypointNode waypointNodeDest){
                        double destX = (waypointNodeDest.getPoint().getX() - xMin) * ratio +this.radius*2;
                        double destY = (waypointNodeDest.getPoint().getY() - yMin) * ratio +this.radius*2;
                        graphics2D.setStroke(
                                new BasicStroke(
                                        (float) (this.radius*Math.tanh(this.pheromoneIntensity*pheromoneContainer.getNextPheromone(cdcNodeOrigin, edge)))
                                )
                        );
                        graphics2D.setColor(
                                new Color(
                                        255,255,255,
                                        (int) (255*Math.tanh(this.pheromoneIntensity*pheromoneContainer.getNextPheromone(cdcNodeOrigin, edge)))
                                ));
                        graphics2D.drawLine(
                                (int) (originX),
                                (int) (originY),
                                (int) (destX),
                                (int) (destY));
                    }
                }
            }
            else if(origin instanceof WaypointNode waypointNodeOrigin){
                double originX = (waypointNodeOrigin.getPoint().getX() - xMin) * ratio +this.radius*2;
                double originY = (waypointNodeOrigin.getPoint().getY() - yMin) * ratio +this.radius*2;
                for(Object edge:graph.getOutputEdges(waypointNodeOrigin)){
                    if(graph.getNextNode(edge) instanceof CDCNode cdcNodeDest){
                        double destX = (cdcNodeDest.getPoint().getX() - xMin) * ratio +this.radius*2;
                        double destY = (cdcNodeDest.getPoint().getY() - yMin) * ratio +this.radius*2;
                        graphics2D.setStroke(
                                new BasicStroke(
                                        (float) (this.radius*Math.tanh(this.pheromoneIntensity*pheromoneContainer.getNextPheromone(waypointNodeOrigin, edge)))
                                )
                        );
                        graphics2D.setColor(
                                new Color(
                                        255,255,255,
                                        (int) (255*Math.tanh(this.pheromoneIntensity*pheromoneContainer.getNextPheromone(waypointNodeOrigin, edge)))
                                ));
                        graphics2D.drawLine(
                                (int) (originX),
                                (int) (originY),
                                (int) (destX),
                                (int) (destY));
                    }
                    else if(graph.getNextNode(edge) instanceof WaypointNode waypointNodeDest){
                        double destX = (waypointNodeDest.getPoint().getX() - xMin) * ratio +this.radius*2;
                        double destY = (waypointNodeDest.getPoint().getY() - yMin) * ratio +this.radius*2;
                        graphics2D.setStroke(
                                new BasicStroke(
                                        (float) (this.radius*Math.tanh(this.pheromoneIntensity*pheromoneContainer.getNextPheromone(waypointNodeOrigin, edge)))
                                )
                        );
                        graphics2D.setColor(
                                new Color(
                                        255,255,255,
                                        (int) (255*Math.tanh(this.pheromoneIntensity*pheromoneContainer.getNextPheromone(waypointNodeOrigin, edge)))
                                ));
                        graphics2D.drawLine(
                                (int) (originX),
                                (int) (originY),
                                (int) (destX),
                                (int) (destY));
                    }
                }

            }
        }
    }

    /**
     * Function that that draws the CDCs and the waypoints
     */
    private void drawCDCsAndWaypoints(Graphics2D graphics2D, ArrayList<Point2D> cdcs, ArrayList<Point2D> waypoints, double xMin, double yMin, double ratio) {
        graphics2D.setColor(Color.GREEN);
        for(Point2D waypoint: waypoints){
            double nodeX = (waypoint.getX() - xMin) * ratio +this.radius;
            double nodeY = (waypoint.getY() - yMin) * ratio +this.radius;
            Shape shape = new Ellipse2D.Double(nodeX, nodeY, 2*this.radius, 2*this.radius);
            graphics2D.fill(shape);
        }
        graphics2D.setColor(Color.RED);
        for(Point2D cdcPoint: cdcs) {
            double nodeX = (cdcPoint.getX() - xMin) * ratio +this.radius;
            double nodeY = (cdcPoint.getY() - yMin) * ratio +this.radius;
            Shape shape = new Rectangle.Double(nodeX, nodeY, 2 * this.radius, 2 * this.radius);
            graphics2D.fill(shape);
        }
    }

    /**
     * Function that draws the fitness value and the iteration number in the image.
     * @param graphics2D Graphics2D to draw in.
     * @param solution Solution with the fitness info.
     * @param generation Integer with the current generation.
     */
    private void drawInfo(Graphics2D graphics2D, Solution solution, Integer generation) {
        graphics2D.setTransform(AffineTransform.getScaleInstance(1, 1));
        graphics2D.setColor(Color.MAGENTA);
        graphics2D.setFont(new Font(graphics2D.getFont().getName(),graphics2D.getFont().getStyle(), (int) (this.height/25)));
        graphics2D.drawString(String.format("Fitness= %.2f",solution.getFitnessValue()) +
                        ", Generation= "+ generation,graphics2D.getFont().getSize(),
                graphics2D.getFont().getSize());

    }

    /**
     * Function that forwards the addPropertyChangeListener to the PropertyChangeSupport Object.
     * @param listener The PropertyChangeListener to be added
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }
}
