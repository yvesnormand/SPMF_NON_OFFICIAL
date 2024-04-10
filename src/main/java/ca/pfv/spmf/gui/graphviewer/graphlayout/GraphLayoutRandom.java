package ca.pfv.spmf.gui.graphviewer.graphlayout;

import ca.pfv.spmf.gui.graphviewer.GraphViewerPanel;
import ca.pfv.spmf.gui.graphviewer.graphmodel.GEdge;
import ca.pfv.spmf.gui.graphviewer.graphmodel.GNode;

import java.util.List;
import java.util.Random;

/**
 * Automatically place the nodes at random positions。 This can be used with the
 * GraphViewerPanel of SPMF.
 *
 * @author Philippe Fournier-Viger
 * @see GraphViewerPanel
 */
public class GraphLayoutRandom extends AbstractGraphLayout {
    /** The number of iterations to perform */

    /**
     * Calculate the layout of the graph
     *
     * @param edges        a list of edges
     * @param nodes        a list of nodes
     * @param canvasWidth  the width of the canvas
     * @param canvasHeight the height of the canvas
     */
    public void autoLayout(List<GEdge> edges, List<GNode> nodes, int canvasWidth, int canvasHeight) {

        // Random number generator
        // It is always initialized using the same number on purpose so that the
        // generated graph is consistent between each execution.
        Random random = new Random(System.currentTimeMillis());

        // Calculate the maximum values of X and Y that we can use for node positions
        // if we assume that the node have a given radius so that node do not appear
        // outside the canvas.
        int maxX = canvasWidth - GNode.RADIUS;
        int maxY = canvasHeight - GNode.RADIUS;

        // Calculate the real size of the canvas after we remove a margin
        // that has the size of the radius.
        int realWidth = maxX - GNode.RADIUS;
        int realHeigth = maxY - GNode.RADIUS;

        // Give random positions to the nodes, whithin the space
        // that we can use in the canvas so that node dont appear outside the canvas
        for (GNode node : nodes) {
            int newX = (int) (GNode.RADIUS + realWidth * random.nextDouble());
            int newY = (int) (GNode.RADIUS + realHeigth * random.nextDouble());
            node.updatePosition(newX, newY);
        }

    }

    @Override
    public String getGeneratorName() {
        return "Random layout";
    }
}
