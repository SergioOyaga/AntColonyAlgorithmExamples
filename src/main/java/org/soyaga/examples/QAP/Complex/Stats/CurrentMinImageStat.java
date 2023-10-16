package org.soyaga.examples.QAP.Complex.Stats;

import org.soyaga.aco.Ant.Ant;
import org.soyaga.aco.Colony;
import org.soyaga.aco.Solution;
import org.soyaga.aco.world.World;
import org.soyaga.examples.QAP.Complex.RunCustomQapOptimization.*;
import org.soyaga.aco.StatsRetrievalPolicy.Stat.Stat;

import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CurrentMinImageStat implements Stat {
    /**
     * A BufferedImage serving as the base image. It is used to overlay the assignment and flows between facilities.
     */
    private final BufferedImage baseImage;
    /**
     * Integer with the size of the image.
     */
    private final int size;
    /**
     * Set{@literal <Node>} with the facilities.
     */
    private final Set<Facility> facilities;

    //////////////////////////////////////////
    // Attributes to interpolate flow width //
    //////////////////////////////////////////
    /**
     * Double with the min flow in the problem.
     */
    private final double minFlow;
    /**
     * Double with the delta flow between the max and min flows in the problem.
     */
    private final double deltaFlow;
    /**
     * Double with the min pixel with of the arch.
     */
    private final double minPixels;
    /**
     * Double with the delta pixels between the max and min pixels widths of the archs.
     */
    private final double deltaPixels;

    /////////////////////////////
    //  Attributes for the GIF //
    /////////////////////////////
    /**
     * ImageWriter.
     */
    private final ImageWriter writer;
    /**
     * Output stream to write the GIF in optimization time.
     */
    private ImageOutputStream out;
    /**
     * ImageWriteParam.
     */
    private final ImageWriteParam params;

    /**
     * Constructor.
     * @param outputPath String where the GIF has to be saved.
     * @param baseImage  BufferedImage with the original map.
     * @param facilities Set with the facilities.
     */
    public CurrentMinImageStat(String outputPath, BufferedImage baseImage, Set<Facility> facilities) {
        this.baseImage = baseImage;
        this.size = Math.max(baseImage.getHeight(), baseImage.getWidth());
        this.writer = ImageIO.getImageWritersByFormatName("gif").next();
        this.params = writer.getDefaultWriteParam();
        this.facilities = facilities;

        //////////////////////////////////////////
        // Attributes to interpolate flow width //
        //////////////////////////////////////////
        this.minPixels = this.size/250.;
        this.deltaPixels = this.size/100.;
        double minFlow1 =facilities.stream().findAny().get().facilityOutgoingFlow.values().stream().findAny().get();
        double maxFlow= minFlow1;
        for(Facility facility:facilities){
            for(Double flow:facility.facilityOutgoingFlow.values()){
                minFlow1 =minFlow1<=flow?minFlow1:flow;
                maxFlow= maxFlow>=flow?maxFlow:flow;
            }
        }
        this.minFlow=minFlow1;
        this.deltaFlow= maxFlow-this.minFlow;

        /////////////////////////////
        /// Attributes for the GIF //
        /////////////////////////////
        try {
            outputPath+= "CurrentCustomMap__" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd__HH-mm")) + ".gif";
            // Configure the image writer to write multiple images into a single GIF file
            this.out = ImageIO.createImageOutputStream(new File(outputPath));
            this.writer.setOutput(this.out);
            this.writer.prepareWriteSequence(null);
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }

    /**
     * Function that returns an ArrayList of strings that compose the header for this stat.
     *
     * @return ArrayList{@literal <String>} with the column names.
     */
    @Override
    public ArrayList<String> getHeader() {
        return new ArrayList<>(){{add("CurrentCustomMapSaved");}};
    }

    /**
     * Function that returns an ArrayList of strings that compose the values for this stat.
     *
     * @param world    World used to measure the stats.
     * @param colony   Colony used to measure the stats.
     * @param statArgs VarArgs containing the additional information needed to apply the stat. The generation number in this case.
     * @return ArrayList{@literal <String>} with the column names.
     */
    @Override
    public ArrayList<String> apply(World world, Colony colony, Object... statArgs) {
        Solution currentBestSolution = colony.getAnts().stream().map(Ant::getSolution).sorted().findFirst().get();
        this.addImageToGif((BufferedImage) this.getImage(world, currentBestSolution, (Integer) statArgs[0]));
        return new ArrayList<>(){{add("true");}};
    }

    /**
     * Function that is executed when the StatsRetrievalPolicy closes its writer.
     */
    @Override
    public void closeStat() {
        try {
            if (this.writer != null) {
                writer.endWriteSequence();
            }
            if (this.out != null) {
                this.out.close();
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    /**
     * Function that computes the new image.
     *
     * @param world World with the world.
     * @param solution Solution with the best found solution.
     * @param generation Integer with the generation.
     * @return the image of the new solution.
     */
    private Image getImage(World world, Solution solution, Integer generation){
        BufferedImage image = deepCopy(this.baseImage);
        Graphics2D graphics2D = image.createGraphics();
        HashMap<Facility, Location> assignation = new HashMap<>();
        Object currentNode = solution.getNodesVisited().get(0);
        for (Object edge : solution.getEdgesUtilized()) {
            if (this.facilities.contains(currentNode)) {
                Location destination = (Location) world.getGraph().getNextNode(edge);
                assignation.put((Facility) currentNode, destination);
            }
            currentNode =  world.getGraph().getNextNode(edge);
        }
        //graphics2D.setColor(new Color(238,130,238));
        //graphics2D.setColor(Color.YELLOW);
        graphics2D.setColor(Color.GREEN);
        for(Map.Entry<Facility, Location> asignationEntry:assignation.entrySet()){
            Facility fOrigin =  asignationEntry.getKey();
            Location lOrigin =  asignationEntry.getValue();
            for (Map.Entry<Object,Double> facilityDest: fOrigin.facilityOutgoingFlow.entrySet()){
                Facility fDest = (Facility) facilityDest.getKey();
                float flow = (float) (double)(facilityDest.getValue());
                if(assignation.keySet().contains(fDest) &&
                        lOrigin.locationDistances.keySet().contains(assignation.getOrDefault(fDest,null))){
                    Location lDest = assignation.get(fDest);
                    //Linear interpolation of the path width
                    graphics2D.setStroke(new BasicStroke((int)(deltaPixels*(flow-minFlow)/deltaFlow+minPixels)));
                    drawArch(graphics2D,lOrigin.point,lDest.point);
                }
            }
        }
        graphics2D.setTransform(new AffineTransform());
        graphics2D.setColor(Color.RED);
        graphics2D.setFont(new Font("",Font.BOLD,this.size/50));
        for(Map.Entry<Facility, Location> asignationEntry:assignation.entrySet()){
            Location destination = asignationEntry.getValue();
            graphics2D.drawString(asignationEntry.getKey().toString(),
                    (int) destination.point.getX(),
                    (int) destination.point.getY()
            );
        }
        graphics2D.setColor(Color.GREEN);
        graphics2D.setFont(new Font(graphics2D.getFont().getName(),graphics2D.getFont().getStyle(),this.size/50));
        graphics2D.drawString("Generation= "+ generation,graphics2D.getFont().getSize(),graphics2D.getFont().getSize());
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
     * Function that adds an image to the GIF writer.
     *
     * @param image BufferedImage to add.
     */
    private void addImageToGif(BufferedImage image){
        // Create a new metadata node for the current image
        IIOMetadataNode node = new IIOMetadataNode("javax_imageio_1.0");
        IIOMetadataNode child = new IIOMetadataNode("ImageDescriptor");
        child.setAttribute("imageLeftPosition", "0");
        child.setAttribute("imageTopPosition", "0");
        child.setAttribute("imageWidth", Integer.toString(image.getWidth()));
        child.setAttribute("imageHeight", Integer.toString(image.getHeight()));
        node.appendChild(child);

        // Set the delay time for the current image
        IIOMetadataNode delayNode = new IIOMetadataNode("GraphicControlExtension");
        delayNode.setAttribute("delayTime", Integer.toString(10));
        delayNode.setAttribute("disposalMethod", "none");
        node.appendChild(delayNode);

        // Add the metadata nodes to the metadata object and write the image
        IIOMetadata metadata = writer.getDefaultImageMetadata(new ImageTypeSpecifier(image), params);
        try {
            metadata.mergeTree("javax_imageio_1.0", node);
            writer.writeToSequence(new javax.imageio.IIOImage(image, null, metadata), params);
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    /**
     * Function that adds an arch between nodes, the width of the arch depends on the flow between the facilities.
     *
     * @param ga Graphics2D Object with the graphics to update.
     * @param origin Point2D with the origin point.
     * @param destiny Point2D with the destination point.
     */
    private void drawArch(Graphics2D ga, Point2D origin, Point2D destiny ) {
        double x1=origin.getX();
        double y1=origin.getY();
        double x2=destiny.getX();
        double y2= destiny.getY();

        double dx = x2 - x1, dy = y2 - y1;
        double l = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)); // line length
        double angle = (Math.atan2(dy, dx)); //get angle (Radians) between ours line and x vectors line. (counter clockwise)
        double width = l/3.;

        //rotation over the origin node.
        AffineTransform at = AffineTransform.getRotateInstance(angle, x1, y1);
        ga.setTransform(at);
        //Create arch with the arch start placed in the origin node.
        Arc2D.Double arc =new Arc2D.Double(
                x1, y1-width/2, l, width, 0, 180, Arc2D.OPEN
        );
        ga.draw(arc);
    }
}
