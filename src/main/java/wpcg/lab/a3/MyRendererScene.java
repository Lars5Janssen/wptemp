/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

package wpcg.lab.a3;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import wpcg.base.mesh.ObjReader;
import wpcg.base.mesh.TriangleMesh;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * Drawing canvas for 3D scenes.
 */
public class MyRendererScene extends JPanel {

    /**
     * This mesh is rendered
     */
    protected TriangleMesh mesh;

    /**
     * Virtual camera.
     */
    protected Camera camera;

    /**
     * This flag enables/disables backface culling
     */
    protected boolean backfaceCulling;

    /**
     * These two render modes will be supported later.
     */
    public enum RenderMode {WIREFRAME, FILL}

    /**
     * Currently selected render mode.
     */
    protected RenderMode renderMode;

    /**
     * Last mouse position (used for mouse input).
     */
    protected Vector2f lastMousePosition;

    public MyRendererScene(int width, int height) {
        setSize(width, height);
        setPreferredSize(new Dimension(width, height));
        camera = new Camera(new Vector3f(0, 0, -2), new Vector3f(0, 0, 0),
                new Vector3f(0, 1, 0), 90.0f / 180.0f * FastMath.PI, 1,
                width, height);
        backfaceCulling = true;
        lastMousePosition = null;
        renderMode = RenderMode.WIREFRAME;

        loadMesh("models/cube.obj");

        setupListeners();
    }

    /**
     * Load a mesh from file.
     */
    private void loadMesh(String filename) {
        ObjReader reader = new ObjReader();
        mesh = reader.read(filename);
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if (mesh != null) {
            // TODO: Draw the mesh here
        }
    }

    /**
     * Draw a triangle given the three points a, b, c in pixel coordinates. The z-values represent the distance
     * from the image plane (after applying the projection matrix, no w-division).
     */
    public void drawTriangle(Graphics2D gc, RasterVertex a, RasterVertex b, RasterVertex c, Color color) {
        drawLine(gc, a, b, color);
        drawLine(gc, b, c, color);
        drawLine(gc, c, a, color);
    }

    /**
     * Draw a line between a and b given in pixel coordinates. This line rasterization includes
     * the interpolation of the z-value between the end point z-values along the line.
     */
    public void drawLine(Graphics gc, RasterVertex a, RasterVertex b, Color color) {
        // Calculate deltas
        int dx = b.x - a.x;
        int dy = b.y - a.y;
        float dz = b.z - a.z;
        int step = Math.max(Math.abs(dx), Math.abs(dy));

        // Calculate x-increment and y-increment for each step
        float x_incr = (float) dx / step;
        float y_incr = (float) dy / step;
        float z_incr = dz / step;

        // Draw
        float x = a.x;
        float y = a.y;
        float z = a.z;
        for (int i = 0; i <= step; i++) {
            drawPixel(gc, (int) x, (int) y, z, color);
            x += x_incr;
            y += y_incr;
            z += z_incr;
        }
    }

    /**
     * Draw a single pixel.
     */
    public void drawPixel(Graphics gc, int x, int y, float z, Color color) {
        gc.setColor(color);
        gc.fillRect(x, y, 1, 1);
    }

    /**
     * Provide a user interface.
     */
    public JPanel getUserInterface() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Model selection
        JComboBox<String> comboBoxModelSelection = new JComboBox<>();
        comboBoxModelSelection.addItem("Cube");
        comboBoxModelSelection.addItem("Deer");
        comboBoxModelSelection.setSelectedItem(0);
        comboBoxModelSelection.addActionListener(e -> {
            switch (comboBoxModelSelection.getSelectedIndex()) {
                case 0 -> loadMesh("models/cube.obj");
                case 1 -> loadMesh("models/deer.obj");
            }
        });
        panel.add(comboBoxModelSelection);

        // Enable/disable backface culling
        JCheckBox cbBackfaceCulling = new JCheckBox("backfaceCulling");
        cbBackfaceCulling.setSelected(backfaceCulling);
        cbBackfaceCulling.addActionListener(e -> {
            backfaceCulling = cbBackfaceCulling.isSelected();
            repaint();
        });
        panel.add(cbBackfaceCulling);

        // Render model
        JComboBox<String> comboRenderMode = new JComboBox<>();
        comboRenderMode.addItem("Wireframe");
        comboRenderMode.addItem("Fill");
        comboRenderMode.setSelectedItem(0);
        comboRenderMode.addActionListener(e -> {
            switch (comboRenderMode.getSelectedIndex()) {
                case 0 -> setRenderMode(RenderMode.WIREFRAME);
                case 1 -> setRenderMode(RenderMode.FILL);
            }
        });
        panel.add(comboRenderMode);

        // this makes the UI prettier
        JPanel containerPanel = new JPanel();
        containerPanel.add(panel);

        return containerPanel;
    }

    public void setRenderMode(RenderMode renderMode) {
        this.renderMode = renderMode;
        repaint();
    }

    /**
     * Setup listeners - used for camera control.
     */
    public void setupListeners() {
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Vector2f mPos = new Vector2f(e.getX(), e.getY());
                if (lastMousePosition != null) {
                    float dx = mPos.x - lastMousePosition.x;
                    float dy = mPos.y - lastMousePosition.y;
                    camera.rotateHorizontal(dx);
                    camera.rotateVertical(dy);
                    repaint();
                }
                lastMousePosition = mPos;
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                lastMousePosition = null;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                lastMousePosition = null;
            }
        });
    }
}
