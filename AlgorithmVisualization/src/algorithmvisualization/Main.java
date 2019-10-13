package algorithmvisualization;

import algorithmvisualization.view.CanvasPanel;
import algorithmvisualization.view.ToolPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;


public class Main {

    public static void main(String[] args) {
        JFrame canvasFrame = new JFrame();

        CanvasPanel canvasPanel = new CanvasPanel();
        ToolPanel toolPanel = new ToolPanel();
        toolPanel.setCanvasPanel(canvasPanel);
        canvasPanel.setToolPanel(toolPanel);

        canvasFrame.setLayout(new BorderLayout());
        canvasFrame.add(canvasPanel, BorderLayout.CENTER);
        canvasFrame.add(toolPanel, BorderLayout.EAST);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        canvasFrame.setSize(screenSize.width, screenSize.height);

        canvasFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        canvasFrame.setVisible(true);

        canvasPanel.generateRandomPoints(10, 10, canvasPanel.getWidth() - 70, canvasPanel.getHeight() - 30, 5000);
        canvasPanel.generateRandomClusters(20, 20, canvasPanel.getWidth() - 80, canvasPanel.getHeight() - 40, 10);
    }

}
