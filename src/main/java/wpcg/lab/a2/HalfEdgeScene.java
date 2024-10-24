/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

package wpcg.lab.a2;

import com.jme3.math.Vector2f;
import wpcg.lab.a2.halfedge.HalfEdge;
import wpcg.lab.a2.halfedge.HalfEdgeMesh;
import wpcg.ui.swing2D.Scene2D;

import javax.swing.*;
import java.awt.*;

/**
 * In this scene a 2D half edge mesh can be represented.
 */
public class HalfEdgeScene extends Scene2D {

    /**
     * This mesh contains the half edge structure.
     */
    protected final HalfEdgeMesh mesh;

    public HalfEdgeScene(int width, int height) {
        super(width, height, new Vector2f(-0.75f, -0.75f), new Vector2f(0.75f, 0.75f));
        mesh = new HalfEdgeMesh();
        mesh.createDummyMesh(true);
    }

    /**
     * Smooth the inner nodes in a half edge mesh.
     */
    protected void smoothMesh() {
        // TODO
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        drawMesh(g2);
    }

    /**
     * Draw a half edge mesh.
     */
    private void drawMesh(Graphics2D g) {
        // Facets
        for (int i = 0; i < mesh.getNumberOfHalfEdges(); i++) {
            drawHalfEdge(g, mesh.getHalfEdge(i));
        }

        // Vertices
        for (int i = 0; i < mesh.getNumberOfVertices(); i++) {
            var v = mesh.getVertex(i);
            drawPoint(g, v.getPosition(), Color.BLACK);
        }
    }

    /**
     * Draw a single half edge.
     */

    private void drawHalfEdge(Graphics2D g, HalfEdge he) {
        Vector2f start = he.getStartVertex().getPosition();
        Vector2f end = he.getNext().getStartVertex().getPosition();

        float shrink = 0.01f;
        Vector2f l = end.subtract(start).normalize();
        Vector2f offset = new Vector2f(-l.y, l.x).mult(0.01f);

        Color heColor = Color.DARK_GRAY;
        drawLine(g, start.add(offset).add(l.mult(shrink)), end.add(offset).subtract(l.mult(shrink)), heColor);
        // Arrow
        drawLine(g, end.add(offset).subtract(l.mult(shrink)),
                end.add(offset).subtract(l.mult(shrink)).subtract(l.mult(shrink)).subtract(offset), heColor);
        drawLine(g, end.add(offset).subtract(l.mult(shrink)),
                end.add(offset).subtract(l.mult(shrink)).subtract(l.mult(shrink)).add(offset), heColor);

        // Link to opposite
        HalfEdge opp = he.getOpposite();
        if (opp != null) {
            Vector2f a = he.getStartVertex().getPosition().add(he.getEndVertex().getPosition()).mult(0.5f).add(offset);
            Vector2f b = opp.getStartVertex().getPosition().add(opp.getEndVertex().getPosition()).mult(0.5f).subtract(offset);
            drawLine(g, a, b, Color.RED);
        }
    }

    @Override
    public String getTitle() {
        return "Half-Edge";
    }

    @Override
    public JPanel getUserInterface() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JButton buttonReset = new JButton("Reset");
        buttonReset.addActionListener(e -> {
            mesh.createDummyMesh(true);
            repaint();
        });
        panel.add(buttonReset);

        JButton buttonSmooth = new JButton("Smooting");
        buttonSmooth.addActionListener(e -> {
            smoothMesh();
            repaint();
        });
        panel.add(buttonSmooth);

        return panel;
    }
}
