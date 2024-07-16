# GUI
This folder is the frontend of the Capacitated Vehicle Routing problem.

<table>
  <tr>
    <th> <b>UI Design </b></th>
  </tr>
  <tr>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/VRP/TestResults/gui_test_design.png"  title="UI design." alt="UI design." /></td>
  </tr>
</table>

## In this folder:
For this test, we created an example input data that allows us to visualize and interact with the user interface but not with the backend.
We are able to connect the UI with the backend via events that are managed by the controller. 

This folder contains different classes that define the structures required for creating the UI.
1. [Listeners](#listeners): Mouse event listeners in the map.
2. [Painters](#painters): Painter that draws the GUIWorld on the map.
3. [Panels](#panels): Panels that compose the UI.
4. [Popups](#popups): Classes that display pop-ups to modify properties in the GUIWorld objects.
5. [Renderers](#renderers): Classes that render the GUIWorld objects in the map.
6. [Waypoints](#waypoints): Waypoints in the GUIWorld that can be drawn in the map.
7. [GUIRoute](#guiroute): route in the world done by a truck that can be drawn in the map.
8. [GUIWorld](#guiworld): Class that contains all the objects that can be drawn in the map.
9. [UI](#ui): JFrame with the user interface that contains all panels.

In addition, a test run class.
1. [RunTest](#runtest): Main class that executes a test.


### [Listeners](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/GUI/Listeners/JXMouseListener.java)
Class that implements MouseListener, MouseWheelListener, and MouseMotionListener. It manages the relevant mouse actions in the JXMapViewer and displays the corresponding pop-ups:
<ul>
    <li> Addition/removal of ToVisitWaypoints.</li>
    <li> Edition of properties of ToVisitWaypoints, VisitedWaypoints and TruckWaypoints.</li>
    <li> Drag/Drop of ToVisitWaypoints.</li>
    <li> Panning/Zooming of the map.</li>
</ul>

### [Painters](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/GUI/Painters/WorldPAinter.java)
Class that extends AbstractPainter<JXMapViewer>. In charge of painting the world in the map using specific renderers.

### [Panels](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/GUI/Panels)
Three classes that extend JPanel.
1. [JXMapPanel](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/GUI/Panels/JXMapPanel.java): Class that contains the JXMapViewer class that represents a map and the GUIWorld that contains the components to draw in the map.
2. [PheromonePanel](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/GUI/Panels/PheromonePanel.java): Class that contains and draws an image with the Pheromone graph representation.
3. [StatsPanel](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/GUI/Panels/StatsPanel.java): JScrollPane class that contains information/stats related to the routes shown in the map.
4. [WeightsPanel](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/GUI/Panels/WeightsPanel.java): JScrollPane class that contains information related to the weights used in the optimization. It allows the user to edit the weight values.

### [Popups](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/GUI/Popups)
Three classes that display popups and a class that filter the input to be a double.

1. [ToVisitWaypointPopup](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/GUI/Popups/ToVisitWaypointPopup.java): Class that displays the pop-up when a ToVisitWaypoint is clicked or created.
2. [TruckWaypointPopup](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/GUI/Popups/TruckWaypointPopup.java): Class that displays the pop-up when a TruckWaypoint is clicked.
3. [VisitedWaypointPopup](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/GUI/Popups/VisitedWaypointPopup.java): Class that displays the pop-up when a VisitedWaypoint is clicked.


1. [DoubleFilter](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/GUI/Popups/DoubleFilter.java): DocumentFilter that makes sure only double values are inserted in the pop-ups.

### [Renderers](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/GUI/Renderers)
Four classes that render specific components.
1. [CustomWorldRenderer](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/GUI/Renderers/CustomWorldRenderer.java): Class that renders a GUIWorld object.
2. [CustomRouteRenderer](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/GUI/Renderers/CustomRouteRenderer.java): Class that renders a GUIRoute object.
3. [CustomWaypointRenderer](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/GUI/Renderers/CustomWaypointRenderer.java): Class that renders a GUIWaypoint object.
4. [TruckColumnRenderer](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/GUI/Popups/TruckColumnRenderer.java): DefaultTableCellRenderer that renders the cells of a JTable using the color associated to a truck route.

### [Waypoints](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/GUI/Waypoints)
Six classes that define all the different Waypoints in the GUIWorld.
1. [GUIWaypoint](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/GUI/Waypoints/GUIWaypoint.java): abstract class that extends AbstractBean and implements Waypoint. It is the base for all waypoints in the GUIWorld.
2. [CDCWaypoint](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/GUI/Waypoints/CDCWaypoint.java): class that extends GUIWaypoint. Contains a CDC location and image.
3. [TruckWaypoint](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/GUI/Waypoints/TruckWaypoint.java): class that extends GUIWaypoint. Contains a truck location, its attributes and image.
4. [VisitedWaypoint](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/GUI/Waypoints/VisitedWaypoint.java): class that extends GUIWaypoint. Contains a visited waypoint location, delivered quantity and image.
5. [ToVisitWaypoint](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/GUI/Waypoints/ToVisitWaypoint.java): class that extends GUIWaypoint. Contains a delivery stop location, the quantity to deliver and image.
6. [TrackWaypoint](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/GUI/Waypoints/TrackWaypoint.java): class that extends GUIWaypoint. Each of the route points in the map.

### [GUIRoute](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/GUI/GUIRoute.java)
Class that represents a route to be drawn in the map.

### [GUIWorld](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/GUI/GUIWorld.java)
Class that extends AbstractBean. It represents all items that should be drawn in a map. 

### [UI](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/GUI/CapacitatedVRP/UI.java)
Class that extends JFrame and implements Runnable, PropertyChangeListener, ActionListener and TableModelListener. It is the graphical representation of all the panels and also the contact point with the "outside" in our case the controller.

### [RunTest](https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/main/java/org/soyaga/examples/VRP/CapacitatedVRP/GUI/RunTest.java)
This is the main class for the test. Is where the run starts. As simple as instantiate the UI object (previously defined), fill it with data and run a thread with the UI.

The example simply consists of:
<ul>
    <li> Two CDCd</li>
    <li> Two Trucks. One on transit, one in a CDC.</li>
    <li> One Visited waypoint.</li>
    <li> Twenty DeliveryStops to visit.</li>
    <li> Two routes, one for each truck:
        <ul>
            <li>One with visitedTrack (always in gray) and toVisitTrack (in red).</li>
            <li>One without visitedTrack but with toVisitTrack (in green).</li>
        </ul>
    </li>
</ul>

## Results:

<table>
  <tr>
    <th> <b>UI Design </b></th>
  </tr>
  <tr>
    <td> <img src="https://github.com/SergioOyaga/AntColonyAlgorithmExamples/blob/master/src/out/VRP/TestResults/gui_test_exaplan.gif"  title="UI test." alt="UI test." /></td>
  </tr>
</table>
We see that the front allows the user to perform basic actions and that the events are triggered accordingly. We also see that the Stats in the table are automatically updated (it is a meaningless update in this case)

So as a conclusion of this test, we see that the UI properly displays the elements and communicates the actions to the outside.

## Comment:
This solution is a test developed as an intermediate step for the CapacitatedVRP with real time optimization and Google API calls for the routing and data gather. :Scream_cat:

