/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

package wpcg.lab.a6;

import com.jme3.math.Vector2f;
import wpcg.base.math.MathF;
import wpcg.ui.swing2D.Scene2D;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Timer;

public class KDTreeScene extends Scene2D {

    /**
     * List of all data points in the scene.
     */
    protected List<KDTreeData> points;

    protected NearestNeighborSearch nearestNeighborSearch;

    /**
     * Contains functionality to build a kd tree for the points.
     */
    protected KDTreeBuilder builder;

    protected KDTreeNode rootNode;

    /**
     * Position and direction angle of the current point.
     */
    private final Vector2f currentPoint;
    private float currentAngle;

    /**
     * Caching of the node colors (for redraw).
     */
    private final Map<KDTreeNode, Color> nodeColors;

    public KDTreeScene(int width, int height) {
        super(width, height,
                new Vector2f(-1, -1), new Vector2f(11, 11));
        points = makeRandomPoints(20);
        builder = new KDTreeBuilder();

        // You need to implement this method
        rootNode = builder.create(points);

        nearestNeighborSearch = new NearestNeighborSearch(rootNode, points);

        // The currentPoint randomly traverses the scene.
        currentPoint = new Vector2f(5, 5);
        currentAngle = 0;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                float speed = 0.5f / (float) Math.sqrt(points.size());
                var currentDir = new Vector2f(MathF.cos(currentAngle), MathF.sin(currentAngle));
                currentPoint.set(currentPoint.add(currentDir.mult(speed)));
                currentAngle += MathF.random(0.1f) - 0.05f;
                if (currentPoint.x < 0 || currentPoint.y < 0 || currentPoint.x > 10 || currentPoint.y > 10) {
                    currentAngle += (float) Math.PI;
                    currentPoint.set(Math.max(0, Math.min(10, currentPoint.x)),
                            Math.max(0, Math.min(10, currentPoint.y)));
                }
                // Redraw all
                SwingUtilities.invokeLater(() -> repaint());
            }
        }, 50, 50);

        nodeColors = new HashMap<>();
    }

    private enum DrawMode {AREA, HYPERPLANE}

    @Override
    public void paint(Graphics g) {
        // Clear all
        g.clearRect(0, 0, getWidth(), getHeight());

        // Draw node areas
        drawKDTreeNode(g, rootNode, DrawMode.AREA);

        // Draw hyperplanes
        drawKDTreeNode(g, rootNode, DrawMode.HYPERPLANE);

        // Draw points
        for (KDTreeData data : points) {
            drawPoint(g, data.getP(), Color.BLACK);
        }

        // Draw current point and its nearest neighbor
        drawPoint(g, currentPoint, Color.MAGENTA);
        var nearest = nearestNeighborSearch.getNearestNeighbor(currentPoint);
        if (nearest != null) {
            drawLine(g, currentPoint, nearest.getP(), Color.MAGENTA);
        }
    }

    /**
     * Draw the hyperplane in a node (and recursively its children)
     */
    private void drawKDTreeNode(Graphics g, KDTreeNode node, DrawMode drawMode) {
        if (node == null || node.getData() == null) {
            return;
        }

        // Draw bounding box
        if (drawMode == DrawMode.AREA) {
            List<Vector2f> poly = Arrays.asList(node.getLL(),
                    new Vector2f(node.getLL().x, node.getUR().y),
                    node.getUR(),
                    new Vector2f(node.getUR().x, node.getLL().y));
            float brightness = 0.5f;
            if (nodeColors.get(node) == null) {
                Color polyColor = new Color(brightness + MathF.random() * (1 - brightness),
                        brightness + MathF.random() * (1 - brightness),
                        brightness + MathF.random() * (1 - brightness));
                nodeColors.put(node, polyColor);
            }
            drawPoly(g, poly, Color.LIGHT_GRAY, nodeColors.get(node));
        } else if (drawMode == DrawMode.HYPERPLANE) {
            // Draw hyplerplane
            Vector2f p0, p1;
            if (node.getSplittingDirection() == KDTreeNode.SplitDirection.X) {
                p0 = new Vector2f(node.getData().getP().x, node.getLL().y);
                p1 = new Vector2f(node.getData().getP().x, node.getUR().y);
            } else {
                p0 = new Vector2f(node.getLL().x, node.getData().getP().y);
                p1 = new Vector2f(node.getUR().x, node.getData().getP().y);
            }
            drawLine(g, p0, p1, Color.BLUE);
        }

        // Recursion
        drawKDTreeNode(g, node.getPosChild(), drawMode);
        drawKDTreeNode(g, node.getNegChild(), drawMode);

    }

    /**
     * Generate a list of random data points in [0,10]^2
     */
    protected List<KDTreeData> makeRandomPoints(int n) {
        List<KDTreeData> points = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Vector2f pos = new Vector2f((float) Math.random() * 10.0f,
                    (float) Math.random() * 10.0f);
            points.add(new KDTreeData(new Vector2f(pos.x, pos.y)));
        }
        return points;
    }


    @Override
    public String getTitle() {
        return "KD tree";
    }
}