package algorithmvisualization;

import java.awt.Graphics;

/**
 * Interface for main drawable object.
 * 
 * @author Murat AKIN
 */
public interface Drawable {
    public void draw(Graphics g);
    public boolean contains(int x, int y);
    public void setSelected(boolean selected);
    public void move(int x, int y);
    public double getCenterX();
    public double getCenterY();
}
