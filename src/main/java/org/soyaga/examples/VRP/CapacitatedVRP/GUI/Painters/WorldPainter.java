package org.soyaga.examples.VRP.CapacitatedVRP.GUI.Painters;

import org.soyaga.examples.VRP.CapacitatedVRP.GUI.GUIWorld;
import org.soyaga.examples.VRP.CapacitatedVRP.GUI.Renderers.CustomWorldRenderer;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.AbstractPainter;

import java.awt.*;

/**
 * Paints a route
 * @author Martin Steiger
 */
public class WorldPainter extends AbstractPainter<JXMapViewer> {
    /**
     * CustomWorldRenderer to render a GUIWorld.
     */
    private final CustomWorldRenderer worldRenderer;
    /**
     * GUIWorld object to be rendered.
     */
    private final GUIWorld GUIWorld;

    /**
     * Constructor.
     * @param GUIWorld the GUIWorld object.
     */
    public WorldPainter(GUIWorld GUIWorld) {
        setAntialiasing(true);
        setCacheable(false);
        this.GUIWorld = GUIWorld;
        this.worldRenderer = new CustomWorldRenderer();
    }

    /**
     * Subclasses must implement this method and perform custom painting operations
     * here.
     *
     * @param g      The Graphics2D object in which to paint
     * @param map an optional configuration parameter
     * @param width  the width
     * @param height the height
     */
    @Override
    protected void doPaint(Graphics2D g, JXMapViewer map, int width, int height) {
        Rectangle viewportBounds = map.getViewportBounds();
        g.translate(-viewportBounds.x, -viewportBounds.y);
        this.worldRenderer.paintWorld(g,map,this.GUIWorld);
        g.translate(viewportBounds.getX(), viewportBounds.getY());
        g.dispose();
    }
}
