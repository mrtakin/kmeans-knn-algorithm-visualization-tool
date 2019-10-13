package algorithmvisualization;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class contains anything about KMeans algorithm visualization.
 *
 * @author Murat AKIN
 */
public class KMeans {

    /* Speed of visualization animation. */
    private int speed = 150;

    /* Number of clusters in canvas when algorithm executed. */
    private int clusterNumber;
    /* Number of points in canvas when algorithm executed. */
    private int pointNumber;
    /* Which iteration executed at any time */
    private int iteration;

    /* It keeps clusters that changed positions at last step - flag for finish */
    private boolean pointClusterChanged;

    private List<Point> points;

    private List<Cluster> clusters;

    /**
     * This data structure stores points and their clusters properly.
     */
    private Map<Cluster, List<Point>> pointsInClustersMap;

    public KMeans(List<Drawable> shapes) {
        points = new ArrayList<>();
        clusters = new ArrayList<>();

        pointsInClustersMap = new HashMap<>();

        loadShapes(shapes);
    }

//    public void run() {}
    public void prepare() {}

    /**
     * This execute a single step of algorithm.
     *
     * @see KMeans#pointClustering()
     * @see KMeans#updatePoisitonsOfClusters()
     */
    public void step() {
        pointClustering();
        updatePoisitonsOfClusters();
        iteration++;
    }

    /**
     * It returns execution of algorithm done or not.
     */
    public boolean isFinished() {
        return !pointClusterChanged;
    }

    /**
     * It put points to clusters as algorithm logic properly.
     */
    private void pointClustering() {
        double pX, pY;
        double cX, cY;
        double minDist, newDist;
        Cluster nearestCluster;

        for (Map.Entry<Cluster, List<Point>> entry : pointsInClustersMap.entrySet()) {
            List<Point> value = entry.getValue();

            value.clear();
        }

        pointClusterChanged = false;
        for (Point point : points) {
            pX = point.getCenterX();
            pY = point.getCenterY();

            minDist = Integer.MAX_VALUE;
            nearestCluster = null;
            for (Cluster cluster : clusters) {

                cX = cluster.getCenterX();
                cY = cluster.getCenterY();

                newDist = Point2D.distance(pX, pY, cX, cY);
                if (newDist < minDist) {
                    minDist = newDist;
                    nearestCluster = cluster;
                }
            }

            if (nearestCluster != point.getCluster()) {
                pointClusterChanged = true;
            }

            point.setColor(nearestCluster.getColor());
            point.setRayLine(nearestCluster.getCenterX(), nearestCluster.getCenterY());
            point.setCluster(nearestCluster);
            pointsInClustersMap.get(nearestCluster).add(point);
        }

    }

    /**
     * It updates position of clusters before next iteration because of logic of
     * algorithm.
     */
    private void updatePoisitonsOfClusters() {
        for (Map.Entry<Cluster, List<Point>> entry : pointsInClustersMap.entrySet()) {
            Cluster cluster = entry.getKey();
            List<Point> points = entry.getValue();

            if (points.isEmpty()) {
                continue;
            }

            double xSum = 0, ySum = 0;
            for (Point point : points) {
                xSum += point.getCenterX();
                ySum += point.getCenterY();
            }

            int n = points.size();
            double newX = xSum / n;
            double newY = ySum / n;

            cluster.setLocation(newX, newY);

            for (Point point : points) {
                point.setRayLine(cluster.getCenterX(), cluster.getCenterY());
            }

        }
    }

    /**
     * It prepares and returns a report of last step as string.
     */
    public String report() {
        StringBuilder strBuilder = new StringBuilder();

        strBuilder.append("\nIteration: ").append(iteration).append("\n");

        int count = 0;
        for (Map.Entry<Cluster, List<Point>> entry : pointsInClustersMap.entrySet()) {
            Cluster c = entry.getKey();
            List<Point> pList = entry.getValue();

            double x = HelperMethods.scaleDouble(c.getCenterX(), 2);
            double y = HelperMethods.scaleDouble(c.getCenterY(), 2);

            strBuilder.append("-->").append("Cluster: ").append(count).append("\n");
            strBuilder.append("\t").append("Location: ").append(x).append(", ").append(y).append("\n");
            strBuilder.append("\tContains ").append(pList.size()).append(" point!\n");

            count++;
        }

        return strBuilder.toString();
    }

    /**
     * It loads all shapes in canvas to this context properly.
     */
    private void loadShapes(List<Drawable> shapes) {
        for (Drawable shape : shapes) {
            if (shape instanceof Point) {
                points.add((Point) shape);
                pointNumber++;
            } else if (shape instanceof Cluster) {
                Cluster cluster = (Cluster) shape;
                clusters.add(cluster);
                clusterNumber++;

                pointsInClustersMap.put(cluster, new ArrayList<>());
            }
        }
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

}
