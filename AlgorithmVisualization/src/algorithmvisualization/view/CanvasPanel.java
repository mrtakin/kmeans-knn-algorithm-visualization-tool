package algorithmvisualization.view;

import algorithmvisualization.AlgorithmType;
import algorithmvisualization.Cluster;
import algorithmvisualization.Drawable;
import algorithmvisualization.HelperMethods;
import algorithmvisualization.KMeans;
import algorithmvisualization.MultipleSelection;
import algorithmvisualization.NearestNeighbour;
import algorithmvisualization.Point;
import algorithmvisualization.SelectMode;
import algorithmvisualization.ShapeType;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * Visualization of algorithm implemented in this JPanel.
 *
 * @author Murat AKIN
 */
public class CanvasPanel extends JPanel {

    /* It keeps all drawn shapes (points and clusters) */
    private List<Drawable> shapes;

    /* It keeps selected shape, if selection selectMode is single */
    private Drawable selectedShape;

    /* It keeps algorithm that working / readyTowork at any time. */
    private AlgorithmType algorithm;

    /* It keeps shape type will be drawn  */
    private ShapeType shapeDrawer = ShapeType.CLUSTER;

    /* Selection selectMode information. SINGLE / MULTIPLE */
    private SelectMode selectMode = SelectMode.SINGLE;

    private MultipleSelection multipleSelection;

    private KMeans kmeans;
    private NearestNeighbour nearestNeighbour;

    private Timer animationTimer;

    /* A visualization of algorithm is ready to work OR not*/
    private boolean ready;

    private ToolPanel toolPanel;

    public CanvasPanel() {
        this.shapes = new ArrayList<>();

        MouseActionHandler handler = new MouseActionHandler();
        addMouseListener(handler);
        addMouseMotionListener(handler);
        setBorder(BorderFactory.createMatteBorder(4, 4, 4, 5, Color.GRAY));

        multipleSelection = new MultipleSelection();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Drawable shape : shapes) {
            shape.draw(g);
        }

        if (selectMode == SelectMode.MULTIPLE && multipleSelection.isStarted()) {
            multipleSelection.draw(g);
        }
    }

    /**
     * Check any shape selected or not, and get it.
     * 
     * @see CanvasPanel#checkAnyShapeSelected(int, int) 
     * 
     * @return selected shape or null
     */
    public Drawable checkAnyShapeSelected(int x, int y) {
        return getSelectedShape(x, y, shapes);
    }

    /**
     * Check any shape selected or not, and get it.
     * 
     * @return selected shape or null
     */
    public Drawable getSelectedShape(int x, int y, List<Drawable> shapeList) {
        ListIterator itr = shapeList.listIterator(shapeList.size());
        while (itr.hasPrevious()) {
            Drawable shape = (Drawable) itr.previous();

            if (shape.contains(x, y)) {
                return shape;
            }
        }
        return null;
    }

    /**
     * Set all shapes are not-selected except given as parameter 
     * 
     * @param currShape 
     */
    public void resetSelected(Drawable currShape) {
        for (Drawable shape : shapes) {
            shape.setSelected(false);
        }

        if (currShape != null) {
            currShape.setSelected(true);
        }
    }

    /**
     * Remove all cluster shapes in canvas.
     */
    public void clearClusters() {
        for (int i = shapes.size() - 1; i >= 0; i--) {
            if (shapes.get(i) instanceof Cluster) {
                shapes.remove(i);
            }
        }

        repaint();
    }

    /**
     * This method prepares environment for visualization.
     * 
     * @param algorithm Which algorithm will be visualizated.
     */
    public void ready(AlgorithmType algorithm) {
        if (algorithm == AlgorithmType.KMEANS) {
            kmeans = new KMeans(shapes);
            kmeans.prepare();

            System.out.println(kmeans.getSpeed());
            animationTimer = new Timer(kmeans.getSpeed(), new KMeansAnimation());

            appendToReport("\n*** Ready to Work: K-Means Algorithm ***\n");
        } else {
            Point startPoint = (Point) selectedShape;
            if (selectedShape == null) {
                startPoint = (Point) multipleSelection.getSelectedShapes().get(0);
            }

            nearestNeighbour = new NearestNeighbour(startPoint, shapes);
            nearestNeighbour.prepare();

            animationTimer = new Timer(500, new NearestNeighbourAnimation());

            appendToReport("\n*** Ready to Work: Nearest Neighbour Algorithm ***\n");
        }

        repaint();

        ready = true;
    }

    /**
     * It starts the animation of visualization.
     */
    public void start() {
        animationTimer.start();
    }

    /**
     * It steps a single step in algorithm visualization.
     */
    public void step() {
        if (algorithm == AlgorithmType.KMEANS) {
            kmeans.step();
            appendToReport(kmeans.report());
        } else {
            nearestNeighbour.step();
            appendToReport(nearestNeighbour.report());
        }

        repaint();
    }

    /**
     * It stop the animation of visualization.
     */
    public void stop() {
        animationTimer.stop();
    }

    /**
     * @return is it ready to visualization
     */
    public boolean isReady() {
        return ready;
    }

    /**
     * It sets animation speed. 
     * @param speed 
     */
    public void setSpeed(int speed) {
        if (algorithm == AlgorithmType.KMEANS) {
            kmeans.setSpeed(speed);
            animationTimer.setDelay(speed);
        } else if (algorithm == AlgorithmType.NEAREST_NEIGHBOUR) {
            nearestNeighbour.setSpeed(speed);
            animationTimer.setDelay(speed);
        }
    }

    /**
     * It returns current animation speed.
     * @return speed of animation so delay of timer.
     */
    public int getSpeed() {
        if (algorithm == AlgorithmType.KMEANS) {
            return kmeans.getSpeed();
        } else {
            return nearestNeighbour.getSpeed();
        }

    }

    /**
     * It returns algorithm step informations as a string. 
     * 
     * @return Which operations executed at last step.
     */
    public String getReport() {
        if (algorithm == AlgorithmType.KMEANS) {
            return kmeans.report();
        } else if (algorithm == AlgorithmType.NEAREST_NEIGHBOUR) {
            return nearestNeighbour.report();
        }

        return "";
    }


    /**
     * It counts points in canvas and return it.
     * @return number of points in canvas.
     */
    public int getPointNumber() {
        int countPoints = 0;
        for (Drawable shape : shapes) {
            if (shape instanceof Point) {
                countPoints++;
            }
        }
        return countPoints;
    }

    /**
     * It counts clusters in canvas and return it.
     * @return number of clusters in canvas.
     */
    public int getClusterNumber() {
        int countClusters = 0;
        for (Drawable shape : shapes) {
            if (shape instanceof Cluster) {
                countClusters++;
            }
        }
        return countClusters;
    }
    
    
    /**
     * It handles mousePressed action for Single Selection.
     * 
     * @param leftClick is left mouse clicked or not?
     * @param newSelection current selected shape.
     * @param mouseX x position of mouse clicked.
     * @param mouseY y position of mouse clicked
     */
    public void handleMousePressedActionForSingleSelection(boolean leftClick, Drawable newSelection, int mouseX, int mouseY) {
        if (leftClick && newSelection != null) {
            selectedShape = newSelection;
        } else if (leftClick && newSelection == null) {
            Drawable newShape = null;
            if (shapeDrawer == ShapeType.POINT) {
                newShape = new Point(mouseX, mouseY);
            } else if (shapeDrawer == ShapeType.CLUSTER) {
                newShape = new Cluster(mouseX, mouseY);
            }

            shapes.add(newShape);

            selectedShape = newShape;
        } else if (!leftClick && newSelection != null) {
            shapes.remove(newSelection);

            selectedShape = null;
        }

        resetSelected(selectedShape);   //IF parameter is null, reset all
    }

    /**
     * It handles mouseDragged action for Single Selection.
     * 
     * @param leftClick is left mouse clicked or not?
     * @param deltaX x distance of last two contigous drag action.
     * @param deltaY y distance of last two contigous drag action.
     */
    public void handleMouseDraggedActionForSingleSelection(boolean leftClick, int deltaX, int deltaY) {
        if (leftClick && selectedShape != null) {
            selectedShape.move(deltaX, deltaY);
        }
    }

    /**
     * It handles mousePressed action for Multiple Selection.
     * 
     * @param leftClick is left mouse clicked or not?
     * @param mouseX x position of mouse clicked.
     * @param mouseY y position of mouse clicked
     */
    public void handleMousePressedActionForMultipleSelection(boolean leftClick, int mouseX, int mouseY) {
        boolean readyToDrad = false;
        if (multipleSelection.isSelected()) {   //MOVE OR DELETE

            Drawable selectedShape = getSelectedShape(mouseX, mouseY, multipleSelection.getSelectedShapes());
            boolean anySelection = selectedShape == null ? false : true;

            if (!anySelection) {
                multipleSelection.setSelected(false);
            } else if (leftClick) {
                //Free to drag
                readyToDrad = true;
            } else {
                shapes.removeAll(multipleSelection.getSelectedShapes());
            }
        }

        if (!readyToDrad) {
            multipleSelection = new MultipleSelection();
            multipleSelection.setLocation(mouseX, mouseY);
            multipleSelection.setStarted(true);
        }
    }

    public void setShapeMode(ShapeType type) {
        shapeDrawer = type;
    }

    public void setSelectionMode(SelectMode mode) {
        selectMode = mode;
    }

    public Drawable getSelectedShape() {
        return selectedShape;
    }

    /**
     * It checks any point is selected, if it is returns true, otherwise returns else.
     * @return any point selected or not.
     */
    public boolean isPointSelected() {
        if (selectMode == SelectMode.SINGLE) {
            return (selectedShape instanceof Point) ? true : false;
        } else {
            List<Drawable> shapes = multipleSelection.getSelectedShapes();
            if (shapes != null && shapes.size() == 1 && (shapes.get(0) instanceof Point)) {
                return true;
            }
            return false;
        }
    }

    /**
     * It removes all shapes in canvas. 
     */
    public void clearCanvas() {
        shapes.clear();
        repaint();
    }

    /**
     * It generates some random points by means of given parameters
     * 
     * @param startX Lower limit of random x value.
     * @param startY Lower limit of random y value.
     * @param endX Upper limit of random x value.
     * @param endY Upper limit of random y value.
     * @param n number of points will be generated.
     */
    public void generateRandomPoints(int startX, int startY, int endX, int endY, int n) {
        Random r = new Random();
        for (int i = 0; i < n; i++) {
            Point p = new Point(HelperMethods.rand(startX, endX), HelperMethods.rand(startY, endY));
            shapes.add(p);
        }

        repaint();
    }

    /**
     * It generates some random clusters by means of given parameters
     * 
     * @param startX Lower limit of random x value.
     * @param startY Lower limit of random y value.
     * @param endX Upper limit of random x value.
     * @param endY Upper limit of random y value.
     * @param n number of clusters will be generated.
     */
    public void generateRandomClusters(int startX, int startY, int endX, int endY, int n) {
        Random r = new Random();
        for (int i = 0; i < n; i++) {
            Cluster c = new Cluster(HelperMethods.rand(startX, endX), HelperMethods.rand(startY, endY));
            shapes.add(c);
        }

        repaint();
    }

    public void setAlgorithm(AlgorithmType algorithm) {
        this.algorithm = algorithm;
    }

    public void setToolPanel(ToolPanel toolPanel) {
        this.toolPanel = toolPanel;
    }

    public void appendToReport(String str) {
        toolPanel.appendStringToOutput(str);
    }

    /**
     * This class handles all mouse actions in canvas.
     */
    private class MouseActionHandler extends MouseAdapter {

        private int firstMouseX;
        private int firstMouseY;
        private int beforeMouseX;
        private int beforeMouseY;

        @Override
        public void mousePressed(MouseEvent e) {
            boolean leftClick = SwingUtilities.isLeftMouseButton(e);
            int mouseX = e.getX();
            int mouseY = e.getY();

            if (selectMode == SelectMode.SINGLE) {
                Drawable shape = checkAnyShapeSelected(mouseX, mouseY);
                handleMousePressedActionForSingleSelection(leftClick, shape, mouseX, mouseY);
            } else if (selectMode == SelectMode.MULTIPLE) {
                handleMousePressedActionForMultipleSelection(leftClick, mouseX, mouseY);
            }

            firstMouseX = beforeMouseX = mouseX;
            firstMouseY = beforeMouseY = mouseY;

            repaint();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            boolean leftClick = SwingUtilities.isLeftMouseButton(e);
            int mouseX = e.getX();
            int mouseY = e.getY();

            if (selectMode == SelectMode.SINGLE) {
                handleMouseDraggedActionForSingleSelection(leftClick, mouseX - beforeMouseX, mouseY - beforeMouseY);
            } else if (selectMode == SelectMode.MULTIPLE) {
                if (multipleSelection.isSelected()) {
                    multipleSelection.moveAllShapes(mouseX - beforeMouseX, mouseY - beforeMouseY);
                }
                if (multipleSelection.isStarted()) {
                    multipleSelection.setLocation(Math.min(mouseX, firstMouseX), Math.min(mouseY, firstMouseY));
                    multipleSelection.setSize(Math.abs(mouseX - firstMouseX), Math.abs(mouseY - firstMouseY));
                }
            }

            beforeMouseX = mouseX;
            beforeMouseY = mouseY;

            repaint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            boolean leftClick = SwingUtilities.isLeftMouseButton(e);
            int mouseX = e.getX();
            int mouseY = e.getX();

            if (selectMode == SelectMode.MULTIPLE && multipleSelection.isStarted()) {
                List<Drawable> intersections = multipleSelection.checkIntersections(shapes);
                resetSelected(null);
                for (Drawable intersection : intersections) {  //Select shapes that are inside selection rect.
                    intersection.setSelected(true);
                }
                multipleSelection.setStarted(false);
            }

            repaint();
        }
    }

    /**
     * This class implements action of K-Means algorithm animations.
     */
    class KMeansAnimation implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            kmeans.step();
            appendToReport(kmeans.report());
            if (kmeans.isFinished()) {
                stop();
            }
            repaint();
        }
    }

    /**
     * This class implements action of Nearest Neighbour algorithm animations.
     */
    class NearestNeighbourAnimation implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            nearestNeighbour.step();
            appendToReport(nearestNeighbour.report());
            if (nearestNeighbour.isFinished()) {
                stop();
            }
            repaint();
        }
    }
}
