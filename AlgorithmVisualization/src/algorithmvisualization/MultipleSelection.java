package algorithmvisualization;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * This class handles multiple shape selection operation.
 *
 * @author Murat AKIN
 */
public class MultipleSelection {

    private Rectangle2D selectionShape;
    private List<Drawable> selectedShapes;
    private Point2D location;

    private boolean started;
    private boolean selected;

    private BasicStroke dashedStroke = new BasicStroke(
            2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{5f}, 0.0f);
    private Color fillColor = new Color(155, 225, 225, 50);

    public MultipleSelection() {
        selectionShape = new Rectangle2D.Double();
        location = new Point2D.Double();
    }

    /**
     * All selected drawable objects can be moved by this method.
     * 
     * @param deltaX 
     * @param deltaY 
     */
    public void moveAllShapes(int deltaX, int deltaY) {
        for (Drawable shape : selectedShapes) {
            shape.move(deltaX, deltaY);
        }
    }

    /**
     * It draws multiple selection shape with all properties.
     */
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.translate(location.getX(), location.getY());
        g2d.setStroke(dashedStroke);

        g2d.draw(selectionShape);

        g2d.setColor(fillColor);
        g2d.fill(selectionShape);

        g2d.dispose();
    }

    /**
     * It find all shapes in selection area and return as list.
     */
    public List<Drawable> checkIntersections(List<Drawable> shapes) {
        selectedShapes = new ArrayList<>();

        double leftTopX = getLocationX();
        double leftTopY = getLocationY();

        double rightBottomX = leftTopX + selectionShape.getWidth();
        double rightBottomY = leftTopY + selectionShape.getHeight();

        selected = false;
        for (Drawable shape : shapes) {
            double pX = shape.getCenterX();
            double pY = shape.getCenterY();

            if (leftTopX <= pX && leftTopY <= pY
                    && pX <= rightBottomX && pY <= rightBottomY) {

                selectedShapes.add(shape);
                selected = true;
            }

        }

        return selectedShapes;
    }

    public void setLocation(double posX, double posY) {
        location.setLocation(posX, posY);
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public void setSize(double w, double h) {
        selectionShape = new Rectangle2D.Double(0, 0, w, h);
    }

    public void setLocationX(double x) {
        location.setLocation(x, location.getY());
    }

    public void setLocationY(double y) {
        location.setLocation(location.getX(), y);
    }

    public double getLocationX() {
        return location.getX();
    }

    public double getLocationY() {
        return location.getY();
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public List<Drawable> getSelectedShapes() {
        return selectedShapes;
    }
}
