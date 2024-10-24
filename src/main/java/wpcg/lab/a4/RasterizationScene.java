/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

package wpcg.lab.a4;

import wpcg.lab.a3.MyRendererScene;
import wpcg.lab.a3.RasterVertex;

import java.awt.*;

public class RasterizationScene extends MyRendererScene {
    public RasterizationScene(int width, int height) {
        super(width, height);
    }

    /**
     * Fill the triangle give by the points a, b, c
     */
    @Override
    public void drawTriangle(Graphics2D gc, RasterVertex a, RasterVertex b, RasterVertex c, Color color) {
        if (renderMode == RenderMode.WIREFRAME) {
            super.drawTriangle(gc, a, b, c, color);
        } else if (renderMode == RenderMode.FILL) {
            // TODO
        }
    }
}
