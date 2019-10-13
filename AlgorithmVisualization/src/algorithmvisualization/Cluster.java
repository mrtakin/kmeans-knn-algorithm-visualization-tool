package algorithmvisualization;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * This class represents Cluster shapes in canvas.
 *
 * @author Murat AKIn
 */
public class Cluster implements Drawable {

    /* Width of cluster shape */
    private static final int width = 20;
    /* Height of cluster shape */
    private static final int height = 20;

    /* Representation of cluster shape in canvas */
    private Rectangle2D rect;

    /* Left-Top Location of Cluster shape (rect) in canvas */
    private Point2D location;

    /* It keeps the cluster selected or not. */
    private boolean selected;

    /* Fill color of cluster shape */
    private Color color;

    public Cluster(double posX, double posY) {
        rect = new Rectangle2D.Double(0, 0, width, height);
        location = new Point2D.Double(posX - width / 2, posY - height / 2);

        color = new Color(HelperMethods.rand(225),
                HelperMethods.rand(225),
                HelperMethods.rand(225));
    }

    /**
     * It draws cluster shape with all properties.
     */
    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.translate(location.getX(), location.getY());

        g2d.setColor(color);
        g2d.fill(rect);

        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(3f));
        g2d.draw(rect);

        if (selected) {
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(5f));
            g2d.draw(rect);
        }

        g2d.dispose();
    }

    /**
     * It checks any point is in cluster shape or not.
     */
    @Override
    public boolean contains(int x, int y) {
        double leftTopX = location.getX();
        double leftTopY = location.getY();

        double rightBottomX = leftTopX + width;
        double rightBottomY = leftTopY + height;

        if (leftTopX <= x && leftTopY <= y
                && x <= rightBottomX && y <= rightBottomY) {
            return true;
        }
        return false;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public void move(int x, int y) {
        location.setLocation(location.getX() + x, location.getY() + y);
    }

    @Override
    public double getCenterX() {
        return location.getX() + width / 2;
    }

    @Override
    public double getCenterY() {
        return location.getY() + height / 2;
    }

    public Color getColor() {
        return color;
    }

    public Point2D getLocation() {
        return location;
    }

    public void setLocation(double x, double y) {
        location.setLocation(x - width / 2, y - height / 2);
    }

}
