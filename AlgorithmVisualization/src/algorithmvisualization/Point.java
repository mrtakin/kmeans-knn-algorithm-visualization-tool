package algorithmvisualization;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 * This class represents Point shapes in canvas.
 *
 * @author Murat AKIN
 */
public class Point implements Drawable {

    /* Radius of point shapes */
    private final int radius = 5;

    /* Representation of point shape in canvas */
    private Ellipse2D point;

    /* Left-Top Location of Cluster shape (ellipse bounded rect) in canvas */
    private Point2D location;

    /* It keeps the cluster selected or not. */
    private boolean selected;

    /* Fill color of cluster shape */
    private Color color = Color.GRAY;

    /* Anchor line from points to its cluster */
    private Line rayLine;

    /* This varible keeps cluster that contains this point. */
    private Cluster cluster;

    /* Indicated it is start point or not in NN algorithm.
       If it is, it will be drawn differently.
     */
    private boolean startPoint;

    public Point(double posX, double posY) {
        point = new Ellipse2D.Double(0, 0, 2 * radius, 2 * radius);
        location = new Point2D.Double(posX - radius, posY - radius);
    }

    /**
     * It draws Point shape with all properties.
     */
    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.translate(location.getX(), location.getY());

        g2d.setColor(color);

        g2d.fill(point);
        g2d.draw(point);

        if (rayLine != null) {
            rayLine.draw(g);
        }

        if (isStartPoint()) {
            g2d.setStroke(new BasicStroke(8F));
            g2d.draw(point);
        }

        if (selected) {
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2f));
            g2d.draw(point);
        }

        g2d.dispose();
    }

    /**
     * It checks any point is in point shape or not.
     */
    @Override
    public boolean contains(int x, int y) {
        double dist = Point2D.distance(location.getX() + radius, location.getY() + radius, x, y);
        if (dist < radius) {
            return true;
        }

        return false;
    }

    @Override
    public void move(int x, int y) {
        location.setLocation(location.getX() + x, location.getY() + y);
    }

    @Override
    public double getCenterX() {
        return location.getX() + radius;
    }

    @Override
    public double getCenterY() {
        return location.getY() + radius;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setLocation(double posX, double posY) {
        location.setLocation(posX, posY);
    }

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public boolean isStartPoint() {
        return startPoint;
    }

    public void setStartPoint(boolean startPoint) {
        this.startPoint = startPoint;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setRayLine(double x2, double y2) {
        this.rayLine = new Line(getCenterX(), getCenterY(), x2, y2);
        this.rayLine.setColor(color);
    }

    public void setRayLine(Line rayLine) {
        this.rayLine = rayLine;
    }

}
