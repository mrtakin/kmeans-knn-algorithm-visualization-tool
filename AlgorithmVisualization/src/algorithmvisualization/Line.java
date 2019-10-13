package algorithmvisualization;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * This class represent of lines used as anchor between points and cluster in
 * K-Means algorithm.
 *
 * @author Murat AKIN
 */
public class Line {

    private Line2D line;

    private Color color = Color.BLACK;
    private Stroke stroke = new BasicStroke(2f);

    public Line(double x1, double y1, double x2, double y2) {
        this.line = new Line2D.Double(x1, y1, x2, y2);
    }

    /**
     * It draws line shape with all properties.
     */
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(color);
        g2d.setStroke(stroke);

        g2d.draw(line);

        g2d.dispose();
    }

    public double getX1() {
        return line.getX1();
    }

    public double getY1() {
        return line.getY1();
    }

    public double getX2() {
        return line.getX2();
    }

    public double getY2() {
        return line.getY2();
    }

    public void setStart(double x1, double y1) {
        line.setLine(new Point2D.Double(x1, y1), line.getP2());
    }

    public void setEnd(double x1, double y1) {
        line.setLine(line.getP1(), new Point2D.Double(x1, y1));
    }

    public void setColor(Color color) {
        this.color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 75);
    }
}
