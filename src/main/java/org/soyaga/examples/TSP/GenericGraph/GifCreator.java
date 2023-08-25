package org.soyaga.examples.TSP.GenericGraph;

import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class enables the creation of a GIF.
 */
public class GifCreator {
    /**
     * Static function that enables the creation of a GIF from an array of images.
     *
     * @param images ArrayList&lt;BufferedImage&gt; containing the images used to create the gif.
     * @param outputPath String where to store the gif.
     * @param delay Integer with the time in ms between each frame.
     */
    public static void createGif(ArrayList<BufferedImage> images, String outputPath, int delay) throws IOException {
        ImageWriter writer = null;
        ImageOutputStream out = null;
        try {
            // Create a new image writer and get its parameters
            writer = ImageIO.getImageWritersByFormatName("gif").next();
            ImageWriteParam params = writer.getDefaultWriteParam();

            // Configure the image writer to write multiple images into a single GIF file
            out = ImageIO.createImageOutputStream(new File(outputPath));
            writer.setOutput(out);
            writer.prepareWriteSequence(null);

            // Loop through each image and write it to the GIF file
            for (BufferedImage image : images) {
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
                delayNode.setAttribute("delayTime", Integer.toString(delay));
                delayNode.setAttribute("disposalMethod", "none");
                node.appendChild(delayNode);

                // Add the metadata nodes to the metadata object and write the image
                IIOMetadata metadata = writer.getDefaultImageMetadata(new ImageTypeSpecifier(image), params);
                metadata.mergeTree("javax_imageio_1.0", node);
                writer.writeToSequence(new javax.imageio.IIOImage(image, null, metadata), params);
            }

        } finally {
            if (writer != null) {
                writer.endWriteSequence();
            }
            if (out != null) {
                out.close();
            }
        }
    }
}
