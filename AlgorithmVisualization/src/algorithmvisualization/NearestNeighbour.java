package algorithmvisualization;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains anything about Nearest Neighbour algorithm visualization.
 *
 * @author Murat AKIN
 */
public class NearestNeighbour {

    /* Speed of visualization animation. */
    private int speed = 150;

    /* Total distance of path */
    private int totalDist;

    private List<Point> pointsNotInPath;
    private List<Point> pointsInPath;

    private Point startPoint;

    private Color color = new Color(HelperMethods.rand(225), HelperMethods.rand(225), HelperMethods.rand(225));

    public NearestNeighbour(Point startPoint, List<Drawable> shapes) {
        this.pointsNotInPath = new ArrayList<>();
        this.pointsInPath = new ArrayList<>();

        this.startPoint = startPoint;

        loadPoints(shapes);
    }

//    public void run(){}
    
    /**
     * It prepares the environment for visualization.
     */
    public void prepare() {
        pointsNotInPath.remove(startPoint);
        pointsInPath.add(startPoint);

        startPoint.setColor(color);
        startPoint.setStartPoint(true);

    }

    /**
     * It executes a single step of algorithm.
     */
    public void step() {
        Point currentPoint = pointsInPath.get(pointsInPath.size() - 1);

        double minDist = Integer.MAX_VALUE;
        Point nearestPoint = null;

        for (Point point : pointsNotInPath) {
            double newDist = Point2D.distance(
                    currentPoint.getCenterX(), currentPoint.getCenterY(),
                    point.getCenterX(), point.getCenterY());

            if (newDist < minDist) {
                minDist = newDist;
                nearestPoint = point;
            }
        }

        if (nearestPoint == null) {
            return;
        }

        totalDist += minDist;
        pointsNotInPath.remove(nearestPoint);
        pointsInPath.add(nearestPoint);
        currentPoint.setRayLine(nearestPoint.getCenterX(), nearestPoint.getCenterY());
        nearestPoint.setColor(color);

        if (pointsNotInPath.size() == 0) {
            nearestPoint.setColor(new Color(HelperMethods.rand(225)));
            nearestPoint.setStartPoint(true);
        }

    }

    /**
     * It returns execution of algorithm done or not.
     */
    public boolean isFinished() {
        return (pointsNotInPath.size() == 0);
    }

    /**
     * It loads all shapes in canvas to this context properly.
     */
    private void loadPoints(List<Drawable> shapes) {
        for (Drawable shape : shapes) {
            if (shape instanceof Point) {
                pointsNotInPath.add((Point) shape);
            }
        }
    }

    /**
     * It prepares and returns a report of last step as string.
     */
    public String report() {
        StringBuilder strBuilder = new StringBuilder();

        strBuilder.append("\nPoints in path: ").append(pointsInPath.size()).append("\n");

        Point p1 = pointsInPath.get(pointsInPath.size() - 1);
        Point p2 = pointsInPath.get(pointsInPath.size() - 2);

        double p1X = HelperMethods.scaleDouble(p1.getCenterX(), 2);
        double p1Y = HelperMethods.scaleDouble(p1.getCenterY(), 2);
        double p2X = HelperMethods.scaleDouble(p2.getCenterX(), 2);
        double p2Y = HelperMethods.scaleDouble(p2.getCenterY(), 2);

        double dist = HelperMethods.scaleDouble(
                Point2D.distance(p1X, p1Y, p2X, p2Y), 2);

        strBuilder.append("-->Point added in: ").append(p1X).append(", ").append(p1Y).append("\n");
        strBuilder.append("\tDistance of added point: ").append(dist);
        strBuilder.append("\tTotal length of path: ").append(totalDist).append("\n");

        return strBuilder.toString();
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
